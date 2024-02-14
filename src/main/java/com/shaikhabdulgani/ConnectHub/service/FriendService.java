package com.shaikhabdulgani.ConnectHub.service;

import com.shaikhabdulgani.ConnectHub.dto.FriendRequestData;
import com.shaikhabdulgani.ConnectHub.dto.UnfriendRequest;
import com.shaikhabdulgani.ConnectHub.exception.AlreadyExistsException;
import com.shaikhabdulgani.ConnectHub.exception.NotFoundException;
import com.shaikhabdulgani.ConnectHub.exception.UnauthorizedAccessException;
import com.shaikhabdulgani.ConnectHub.model.Friend;
import com.shaikhabdulgani.ConnectHub.model.FriendRequest;
import com.shaikhabdulgani.ConnectHub.model.User;
import com.shaikhabdulgani.ConnectHub.projection.FriendProjection;
import com.shaikhabdulgani.ConnectHub.projection.FriendRequestProjection;
import com.shaikhabdulgani.ConnectHub.repo.FriendRepo;
import com.shaikhabdulgani.ConnectHub.repo.FriendRequestRepo;
import com.shaikhabdulgani.ConnectHub.util.CustomPage;
import com.shaikhabdulgani.ConnectHub.util.enums.Relation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoExpression;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRequestRepo friendRequestRepo;
    private final FriendRepo friendRepo;
    private final BasicUserService basicUserService;
    private final MongoTemplate mongoTemplate;

    public void newFriendRequest(FriendRequestData request, String token) throws NotFoundException, UnauthorizedAccessException, AlreadyExistsException {

        User sender = basicUserService.getById(request.getSenderId());
        User receiver = basicUserService.getById(request.getReceiverId());
        basicUserService.checkIfUserIsAuthorized(sender,token);
        checkIfRequestAlreadySent(sender.getUserId(),receiver.getUserId());

        FriendRequest friendRequest = new FriendRequest(sender.getUserId(),receiver.getUserId());
        friendRequestRepo.save(friendRequest);

    }

    public FriendRequest getFriendRequest(String requestId) throws NotFoundException {
        Optional<FriendRequest> request = friendRequestRepo.findById(requestId);
        if (request.isEmpty()){
            throw new NotFoundException("friend request not found by id: "+requestId);
        }
        return request.get();
    }

    public FriendRequest getFriendRequest(String senderId,String receiverId) throws NotFoundException {
        Optional<FriendRequest> request = friendRequestRepo.findBySenderAndReceiver(senderId,receiverId);
        if (request.isEmpty()){
            throw new NotFoundException("Friend request not found");
        }
        return request.get();
    }

    public Friend getFriend(String user1,String user2) throws NotFoundException {
        Optional<Friend> friend = friendRepo.findByUser1AndUser2(user1,user2);
        if (friend.isEmpty()){
            throw new NotFoundException("Not friendship found");
        }
        return friend.get();
    }

//    @Transactional
    public void acceptFriendRequest(String requestId, String token) throws NotFoundException, UnauthorizedAccessException {
        FriendRequest request = getFriendRequest(requestId);

        User receiver = basicUserService.getById(request.getReceiver());
        basicUserService.checkIfUserIsAuthorized(receiver,token);

        friendRequestRepo.deleteById(request.getRequestId());
        createFriendshipFromRequest(request);
    }

//    @Transactional
    public void unfriend(UnfriendRequest request, String token) throws NotFoundException, UnauthorizedAccessException {
        User user = basicUserService.getById(request.getAsker());
        basicUserService.checkIfUserIsAuthorized(user,token);

        Friend friend = getFriend(request.getAsker(), request.getFriendToUnfriend());
        Friend friend2 = getFriend(request.getFriendToUnfriend(), request.getAsker());

        friendRepo.deleteById(friend.getFriendId());
        friendRepo.deleteById(friend2.getFriendId());

        basicUserService.decrementFriendCounter(friend.getUser1());
        basicUserService.decrementFriendCounter(friend.getUser2());
    }

    public void save(String user1,String user2){
        friendRepo.save(new Friend(user1,user2));
    }

    public void createFriendshipFromRequest(FriendRequest request){

        save(request.getReceiver(),request.getSender());
        save(request.getSender(),request.getReceiver());
        basicUserService.incrementFriendCounter(request.getReceiver());
        basicUserService.incrementFriendCounter(request.getSender());

    }

    public void deleteFriendRequest(String requestId, String token) throws NotFoundException, UnauthorizedAccessException {
        FriendRequest request = getFriendRequest(requestId);
        User sender = basicUserService.getById(request.getSender());
        basicUserService.checkIfUserIsAuthorized(sender,token);

        friendRequestRepo.deleteById(request.getRequestId());
    }

    public void rejectFriendRequest(String requestId, String token) throws NotFoundException, UnauthorizedAccessException {
        FriendRequest request = getFriendRequest(requestId);
        User receiver = basicUserService.getById(request.getReceiver());
        basicUserService.checkIfUserIsAuthorized(receiver,token);

        friendRequestRepo.deleteById(request.getRequestId());
    }

    public CustomPage<FriendProjection> getAllFriends(String userId, String token, int pageNumber, int pageSize) throws NotFoundException, UnauthorizedAccessException {
        User user = basicUserService.getById(userId);

        MatchOperation matchOperation = Aggregation
                .match(Criteria.where("user1").is(userId));

        MatchOperation matchOperation1 = Aggregation
                .match(AggregationExpression.from(MongoExpression.create("'$expr': {'$eq': ['$_id', {'$toObjectId': '$$u_Id'}]}")));

        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("users")
                .let(VariableOperators.Let.ExpressionVariable.newVariable("u_Id").forField("user2"))
                .pipeline(matchOperation1)
                .as("user");

        SkipOperation skipOperation = Aggregation.skip((long)pageNumber * pageSize);
        LimitOperation limitResult = Aggregation.limit(pageSize);

        UnwindOperation unwindUser = Aggregation.unwind("user");

        ProjectionOperation projectionOperation = Aggregation.project()
                .and("user2").as("userId")
                .and("user.profileImageId").as("profileImageId")
                .and("user.email").as("email")
                .and("user.lastSeen").as("lastSeen")
                .and("user.username").as("username");

        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation,
                skipOperation,
                limitResult,
                lookupOperation,
                unwindUser,
                projectionOperation
        );

        List<FriendProjection> result = mongoTemplate.aggregate(aggregation, "friends", FriendProjection.class).getMappedResults();

        CustomPage<FriendProjection> pagedResult = new CustomPage<>();
        pagedResult.setPageNumber(pageNumber);
        pagedResult.setPageSize(pageSize);
        pagedResult.setSize(result.size());
        pagedResult.setTotalElements((int)friendRepo.countByUser1(userId));
        pagedResult.setContent(result);

        return pagedResult;
    }

    public CustomPage<FriendRequestProjection> getAllFriendsRequest(String userId,String token,int pageNumber,int pageSize) throws NotFoundException, UnauthorizedAccessException {
        User user = basicUserService.getById(userId);
        basicUserService.checkIfUserIsAuthorized(user,token);

        MatchOperation matchOperation = Aggregation
                .match(Criteria.where("receiver").is(userId));

        MatchOperation matchOperation1 = Aggregation
                .match(AggregationExpression.from(MongoExpression.create("'$expr': {'$eq': ['$_id', {'$toObjectId': '$$u_Id'}]}")));

        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("users")
                .let(VariableOperators.Let.ExpressionVariable.newVariable("u_Id").forField("sender"))
                .pipeline(matchOperation1)
                .as("user");

        SortOperation sortPosts = Aggregation.sort(Sort.by(Sort.Order.desc("timestamp")));
        SkipOperation skipOperation = Aggregation.skip((long)pageNumber * pageSize);
        LimitOperation limitResult = Aggregation.limit(pageSize);

        UnwindOperation unwindUser = Aggregation.unwind("user");

        ProjectionOperation projectionOperation = Aggregation.project()
                .andInclude("timestamp")
                .and("sender").as("userId")
                .and("user.profileImageId").as("profileImageId")
                .and("user.email").as("email")
                .and("user.username").as("username");

        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation,
                sortPosts,
                skipOperation,
                limitResult,
                lookupOperation,
                unwindUser,
                projectionOperation
        );

        List<FriendRequestProjection> result = mongoTemplate.aggregate(aggregation, "friendRequests", FriendRequestProjection.class).getMappedResults();

        CustomPage<FriendRequestProjection> pagedResult = new CustomPage<>();
        pagedResult.setPageNumber(pageNumber);
        pagedResult.setPageSize(pageSize);
        pagedResult.setSize(result.size());
        pagedResult.setTotalElements((int)friendRequestRepo.countByReceiver(userId));
        pagedResult.setContent(result);

        return pagedResult;
    }

    public Relation getRelation(String userId, String otherUserId) throws NotFoundException {

        User user = basicUserService.getById(otherUserId);
        if (friendRequestRepo.findBySenderAndReceiver(userId,otherUserId).isPresent())
            return Relation.FR_SENT;
        if (friendRequestRepo.findBySenderAndReceiver(otherUserId,userId).isPresent())
            return Relation.FR_RECEIVED;
        if (friendRepo.findByUser1AndUser2(userId,otherUserId).isPresent())
            return Relation.FRIENDS;
        return Relation.NONE;

    }



    private void checkIfRequestAlreadySent(String sender, String receiver) throws AlreadyExistsException {
        boolean request1 = friendRequestRepo.findBySenderAndReceiver(sender,receiver).isPresent();
        boolean request2 = friendRequestRepo.findBySenderAndReceiver(receiver,sender).isPresent();
        if (request1 || request2){
            throw new AlreadyExistsException("Friend Request already exists");
        }

    }
}
