package com.shaikhabdulgani.ConnectHub.service;

import com.shaikhabdulgani.ConnectHub.dto.LikedAndBookmarkedDto;
import com.shaikhabdulgani.ConnectHub.dto.NewComment;
import com.shaikhabdulgani.ConnectHub.exception.BadRequestException;
import com.shaikhabdulgani.ConnectHub.exception.NotFoundException;
import com.shaikhabdulgani.ConnectHub.exception.UnauthorizedAccessException;
import com.shaikhabdulgani.ConnectHub.model.*;
import com.shaikhabdulgani.ConnectHub.projection.CommentProjection;
import com.shaikhabdulgani.ConnectHub.projection.PostProjection;
import com.shaikhabdulgani.ConnectHub.projection.SavedProjection;
import com.shaikhabdulgani.ConnectHub.repo.BookmarkRepo;
import com.shaikhabdulgani.ConnectHub.repo.CommentRepo;
import com.shaikhabdulgani.ConnectHub.repo.LikeRepo;
import com.shaikhabdulgani.ConnectHub.repo.PostRepo;
import com.shaikhabdulgani.ConnectHub.util.CustomPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoExpression;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepo postRepo;
    private final ImageService imageService;
    private final BasicUserService basicUserService;
    private final LikeRepo likeRepo;
    private final BookmarkRepo bookmarkRepo;
    private final MongoTemplate mongoTemplate;
    private final MongoTransactionManager mongoTransactionManager;
    private final CommentRepo commentRepo;

    public PostProjection getPost(String postId) throws NotFoundException {
        Optional<Post> post = postRepo.findById(postId);
        if (post.isEmpty()){
            throw new NotFoundException("Post not found by post id: "+postId);
        }

        PostProjection postProjection = new PostProjection();
        User user = basicUserService.getById(post.get().getUserId());

        postProjection.setPostId(post.get().getPostId());
        postProjection.setImageId(post.get().getImageId());
        postProjection.setText(post.get().getText());
        postProjection.setCreateDate(post.get().getCreateDate());
        postProjection.setUserId(post.get().getUserId());
        postProjection.setTotalLikes(post.get().getTotalLikes());
        postProjection.setTotalComments(post.get().getTotalComments());

        postProjection.setUsername(user.getUsername());
        postProjection.setProfileImageId(user.getProfileImageId());
        return postProjection;
    }

//    @Transactional
    public Post uploadNewPost(MultipartFile file, String userId,String text, String token) throws NotFoundException, UnauthorizedAccessException, BadRequestException, IOException {
        User user = basicUserService.getById(userId);
        basicUserService.checkIfUserIsAuthorized(user,token);
        if (file==null && text.trim().equals("")){
            throw new BadRequestException("post should at least have text or image. both cannot be empty at the same time");
        }
        String imageId = "";
        if (file!=null){
            imageId = imageService.uploadImage(file,userId);
        }

        Update update = new Update();
        update.inc("totalPost");
        Query query = new Query(Criteria.where("_id").is(userId));
        mongoTemplate.updateFirst(query,update, User.class);

        Post post = Post.builder()
                .userId(userId)
                .imageId(imageId)
                .text(text)
                .createDate(new Date())
                .totalComments(0)
                .totalLikes(0)
                .build();

        return postRepo.save(post);
    }

    public Page<Post> getUserPosts(String userId,int pageNumber, int pageSize) throws NotFoundException {
        User user = basicUserService.getById(userId);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createDate").descending());
        return postRepo.findByUserId(userId,pageable);
    }

//    @Transactional
    public void postLiked(String postId, String userId, String token) throws NotFoundException, UnauthorizedAccessException {
        User user = basicUserService.getById(userId);
        basicUserService.checkIfUserIsAuthorized(user,token);
        if (likeRepo.findByUserIdAndPostId(userId,postId).isPresent()){
            return;
        }

        //increment like counter of post
        Update update = new Update();
        update.inc("totalLikes");
        Query query = new Query(Criteria.where("_id").is(postId));
        mongoTemplate.updateFirst(query,update, Post.class);

        likeRepo.save(Like.builder().userId(userId).postId(postId).build());
    }

//    @Transactional
    public void postUnliked(String postId, String userId, String token) throws NotFoundException, UnauthorizedAccessException {
        User user = basicUserService.getById(userId);
        basicUserService.checkIfUserIsAuthorized(user,token);
        Optional<Like> like  = likeRepo.findByUserIdAndPostId(userId,postId);
        if (like.isEmpty()){
            return;
        }

        likeRepo.deleteById(like.get().getLikeId());

        Update update = new Update();
        update.inc("totalLikes",-1);
        Query query = new Query(Criteria.where("_id").is(postId));
        mongoTemplate.updateFirst(query,update, Post.class);
    }

    public boolean isLiked(String postId, String userId, String token) throws NotFoundException, UnauthorizedAccessException {
        User user = basicUserService.getById(userId);
        basicUserService.checkIfUserIsAuthorized(user,token);
        Optional<Like> like  = likeRepo.findByUserIdAndPostId(userId,postId);
        return like.isPresent();
    }

    public void bookmark(String postId, String userId, String token) throws NotFoundException, UnauthorizedAccessException {
        User user = basicUserService.getById(userId);
        basicUserService.checkIfUserIsAuthorized(user,token);
        if (bookmarkRepo.existsByUserIdAndPostId(userId,postId)){
            return;
        }
        bookmarkRepo.save(Bookmark.builder().userId(userId).postId(postId).date(new Date()).build());
    }

    public void removeBookmark(String postId, String userId, String token) throws NotFoundException, UnauthorizedAccessException {
        User user = basicUserService.getById(userId);
        basicUserService.checkIfUserIsAuthorized(user,token);
        Optional<Bookmark> bookmark  = bookmarkRepo.findByUserIdAndPostId(userId,postId);
        if (bookmark.isEmpty()){
            return;
        }
        bookmarkRepo.deleteById(bookmark.get().getBookmarkId());
    }

    public boolean isBookmarked(String postId, String userId, String token) throws NotFoundException, UnauthorizedAccessException {
        User user = basicUserService.getById(userId);
        basicUserService.checkIfUserIsAuthorized(user,token);
        return bookmarkRepo.existsByUserIdAndPostId(userId,postId);
    }

    public LikedAndBookmarkedDto isLikedAndBookmarked(String postId, String userId, String token) throws NotFoundException, UnauthorizedAccessException {
        User user = basicUserService.getById(userId);
        basicUserService.checkIfUserIsAuthorized(user,token);
        boolean isLiked  = likeRepo.existsByUserIdAndPostId(userId,postId);
        boolean isBookmarked  = bookmarkRepo.existsByUserIdAndPostId(userId,postId);
        return new LikedAndBookmarkedDto(isLiked,isBookmarked);
    }

    public CustomPage<SavedProjection> getAllBookmarkedPost(String userId, String token, int pageNumber, int pageSize) throws NotFoundException, UnauthorizedAccessException {
        User user = basicUserService.getById(userId);
        basicUserService.checkIfUserIsAuthorized(user,token);

        MatchOperation matchOperation = Aggregation
                .match(Criteria.where("userId").is(userId));

        MatchOperation postMatch = Aggregation
                .match(AggregationExpression.from(MongoExpression.create("'$expr': {'$eq': ['$_id', {'$toObjectId': '$$p_Id'}]}")));

        LookupOperation postLookup = LookupOperation.newLookup()
                .from("posts")
                .let(VariableOperators.Let.ExpressionVariable.newVariable("p_Id").forField("postId"))
                .pipeline(postMatch)
                .as("posts");

        MatchOperation userMatch = Aggregation
                .match(AggregationExpression.from(MongoExpression.create("'$expr': {'$eq': ['$_id', {'$toObjectId': '$$u_Id'}]}")));

        LookupOperation userLookup = LookupOperation.newLookup()
                .from("users")
                .let(VariableOperators.Let.ExpressionVariable.newVariable("u_Id").forField("posts.userId"))
                .pipeline(userMatch)
                .as("user");

        SortOperation sortOperation = Aggregation.sort(Sort.by(Sort.Order.desc("date")));
        SkipOperation skipOperation = Aggregation.skip((long)pageNumber * pageSize);
        LimitOperation limitResult = Aggregation.limit(pageSize);
        UnwindOperation postUnwind = Aggregation.unwind("posts");
        UnwindOperation userUnwind = Aggregation.unwind("user");
        ProjectionOperation projectionOperation = Aggregation.project()
                .and("posts.imageId").as("imageId")
                .and("posts.text").as("text")
                .and("posts.createDate").as("createDate")
                .and("posts.userId").as("userId")
                .and("posts.totalLikes").as("totalLikes")
                .and("posts.totalComments").as("totalComments")
                .andInclude("postId")
                .and("user.username").as("username")
                .and("user.profileImageId").as("profileImageId");
        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation,
                postLookup,
                sortOperation,
                skipOperation,
                limitResult,
                postUnwind,
                userLookup,
                userUnwind,
                projectionOperation
        );
        List<SavedProjection> result = mongoTemplate.aggregate(aggregation, "bookmarks", SavedProjection.class).getMappedResults();
        CustomPage<SavedProjection> pagedResult = new CustomPage<>();
        pagedResult.setPageNumber(pageNumber);
        pagedResult.setPageSize(pageSize);
        pagedResult.setSize(result.size());
        pagedResult.setTotalElements((int)bookmarkRepo.countByUserId(userId));
        pagedResult.setContent(result);
        return pagedResult;
    }

//    @Transactional
    public Comment postComment(String postId, NewComment newComment, String token) throws NotFoundException, UnauthorizedAccessException {
        User user = basicUserService.getById(newComment.getUserId());
        basicUserService.checkIfUserIsAuthorized(user,token);

        Comment comment = Comment
                .builder()
                .postId(postId)
                .userId(newComment.getUserId())
                .comment(newComment.getComment())
                .date(new Date())
                .build();

        Update update = new Update();
        update.inc("totalComments");
        Query query = new Query(Criteria.where("_id").is(postId));
        mongoTemplate.updateFirst(query,update, Post.class);

        return commentRepo.save(comment);
    }

    public CustomPage<CommentProjection> getComment(String postId, int pageNumber, int pageSize){

        MatchOperation matchOperation = Aggregation
                .match(Criteria.where("postId").is(postId));

        MatchOperation matchOperation1 = Aggregation
                .match(AggregationExpression.from(MongoExpression.create("'$expr': {'$eq': ['$_id', {'$toObjectId': '$$u_Id'}]}")));

        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("users")
                .let(VariableOperators.Let.ExpressionVariable.newVariable("u_Id").forField("userId"))
                .pipeline(matchOperation1)
                .as("user");

        SortOperation sortOperation = Aggregation.sort(Sort.by(Sort.Order.desc("date")));
        SkipOperation skipOperation = Aggregation.skip((long)pageNumber * pageSize);
        LimitOperation limitResult = Aggregation.limit(pageSize);
        UnwindOperation unwindOperation = Aggregation.unwind("user");
        ProjectionOperation projectionOperation = Aggregation.project()
                .andInclude("comment")
                .andInclude("userId")
                .and("user.username").as("username")
                .and("user.profileImageId").as("profileImageId")
                .andInclude("date");
        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation,
                lookupOperation,
                sortOperation,
                skipOperation,
                limitResult,
                unwindOperation,
                projectionOperation
        );
        List<CommentProjection> result = mongoTemplate.aggregate(aggregation, "comments", CommentProjection.class).getMappedResults();
        CustomPage<CommentProjection> pagedResult = new CustomPage<>();
        pagedResult.setPageNumber(pageNumber);
        pagedResult.setPageSize(pageSize);
        pagedResult.setSize(result.size());
        pagedResult.setTotalElements((int)postRepo.countByPostId(postId));
        pagedResult.setContent(result);
        return pagedResult;

    }

    public List<PostProjection> getUserFeed(String userId, String token, int pageNumber, int pageSize) throws NotFoundException, UnauthorizedAccessException {
        User user = basicUserService.getById(userId);
        basicUserService.checkIfUserIsAuthorized(user,token);

        MatchOperation matchOperation = Aggregation
                .match(Criteria.where("user1").is(userId));

        LookupOperation lookupFriends = LookupOperation.newLookup()
                .from("posts")
                .localField("user2")
                .foreignField("userId")
                .as("posts");

        UnwindOperation unwindFriends = Aggregation.unwind("posts");
        ReplaceRootOperation replaceRootOp = Aggregation.replaceRoot("posts");
        SortOperation sortPosts = Aggregation.sort(Sort.by(Sort.Order.desc("createDate")));
        SkipOperation skipOperation = Aggregation.skip((long)pageNumber * pageSize);
        LimitOperation limitResult = Aggregation.limit(pageSize);

        MatchOperation matchOperation1 = Aggregation
                .match(AggregationExpression.from(MongoExpression.create("'$expr': {'$eq': ['$_id', {'$toObjectId': '$$u_Id'}]}")));

        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("users")
                .let(VariableOperators.Let.ExpressionVariable.newVariable("u_Id").forField("userId"))
                .pipeline(matchOperation1)
                .as("user");

        UnwindOperation unwindUser = Aggregation.unwind("user");
        ProjectionOperation projectionOperation = Aggregation.project()
                .andInclude("imageId","text","createDate","userId","totalLikes","totalComments")
                .and("user.profileImageId").as("profileImageId")
                .and("user.username").as("username");

        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation,
                lookupFriends,
                unwindFriends,
                replaceRootOp,
                sortPosts,
                skipOperation,
                limitResult,
                lookupOperation,
                unwindUser,
                projectionOperation
        );

        return mongoTemplate.aggregate(aggregation, "friends", PostProjection.class).getMappedResults();
    }
}
