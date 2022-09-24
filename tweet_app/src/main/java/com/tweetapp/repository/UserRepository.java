package com.tweetapp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tweetapp.model.User;

public interface UserRepository extends MongoRepository<User, String> {

	User findByUserName(String userName);

}
