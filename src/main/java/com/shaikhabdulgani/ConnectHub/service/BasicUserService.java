package com.shaikhabdulgani.ConnectHub.service;

import com.shaikhabdulgani.ConnectHub.exception.AlreadyExistsException;
import com.shaikhabdulgani.ConnectHub.exception.NotFoundException;
import com.shaikhabdulgani.ConnectHub.exception.UnauthorizedAccessException;
import com.shaikhabdulgani.ConnectHub.model.User;
import com.shaikhabdulgani.ConnectHub.model.UserInfoDetail;
import com.shaikhabdulgani.ConnectHub.repo.UserRepo;
import com.shaikhabdulgani.ConnectHub.service.JwtService;
import com.shaikhabdulgani.ConnectHub.util.enums.AuthenticationMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicUserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final MongoTemplate mongoTemplate;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Optional<User> user = userRepo.findByUsername(username);
        if (user.isEmpty()){
            throw new UsernameNotFoundException(String.format("cannot find user by username %s",username));
        }
        return user.map(UserInfoDetail::new).get();
    }

    public User getByUsername(String username) throws NotFoundException {
        Optional<User> user = userRepo.findByUsername(username);
        if (user.isEmpty()){
            throw new NotFoundException(String.format("user not found by username %s",username));
        }
        user.get().setPassword(null);
        return user.get();
    }

    public User getByEmail(String email) throws NotFoundException {
        Optional<User> user = userRepo.findByEmail(email);
        if (user.isEmpty()){
            throw new NotFoundException(String.format("user not found by email %s",email));
        }
        user.get().setPassword(null);
        return user.get();
    }

    public User getById(String userId) throws NotFoundException {
        Optional<User> user = userRepo.findById(userId);
        if (user.isEmpty()){
            throw new NotFoundException(String.format("user not found by userId %s",userId));
        }
        user.get().setPassword(null);
        return user.get();
    }

    public User getUser(String credential, AuthenticationMethod authMethod) throws NotFoundException {
        if (authMethod!=null && authMethod.isUsername())
            return getByUsername(credential);
        return getByEmail(credential);
    }

    public void checkIfUserAlreadyExists(String username, String email) throws AlreadyExistsException {
        if (userRepo.findByUsername(username).isPresent()){
            throw new AlreadyExistsException("username already taken");
        }
        if (userRepo.findByEmail(email).isPresent()){
            throw new AlreadyExistsException("email already present");
        }
    }

    public User updateWithPassword(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public User getUserWithPassword(String userId) throws NotFoundException {
        Optional<User> user = userRepo.findById(userId);
        if (user.isEmpty()){
            throw new NotFoundException("user not found with id: "+userId);
        }
        return user.get();
    }

//    @Transactional
    public void update(String userId,String field,String newValue){
        Query query = new Query(Criteria.where("_id").is(userId));
        Update update = new Update().set(field, newValue);

        mongoTemplate.updateFirst(query, update, User.class);
    }

    public void updateLastSeen(String username){
        userRepo.updateLastSeenByUsername(username,new Date());
    }

    public void checkIfUserIsAuthorized(User user, String token) throws UnauthorizedAccessException {
        if (!jwtService.extractUsername(token).equals(user.getUsername())){
            throw new UnauthorizedAccessException("JWT Token doesn't match the expected JWT Token");
        }
    }

    public Page<User> searchUser(String username,int pageNumber,int pageSize) {

        return userRepo.findByUsernameRegex(".*" + username + ".*",PageRequest.of(pageNumber, pageSize));

    }

    public boolean getUserIsVerified(String userId, String token) throws NotFoundException, UnauthorizedAccessException {
        User user = getById(userId);
        checkIfUserIsAuthorized(user,token);
        return user.isVerified();
    }

    public void setUserIsVerified(String userId) {
        userRepo.setIsVerifyById(userId,true);
    }

    public void checkIfPasswordMatches(User user, String oldPassword) throws UnauthorizedAccessException {

        if (!encoder.matches(oldPassword,user.getPassword())){
            throw new UnauthorizedAccessException("password doesn't match");
        }

    }

    public void decrementFriendCounter(String userId) {
        Update update = new Update();
        update.inc("totalFriends",-1);
        Query query = new Query(Criteria.where("_id").is(userId));
        mongoTemplate.updateFirst(query,update, User.class);
    }

    public void incrementFriendCounter(String userId) {
        Update update = new Update();
        update.inc("totalFriends");
        Query query = new Query(Criteria.where("_id").is(userId));
        mongoTemplate.updateFirst(query,update, User.class);
    }
}
