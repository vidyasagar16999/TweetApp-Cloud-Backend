package com.tweetapp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tweetapp.model.RegistrationDb;

public interface RegistrationRepository extends MongoRepository<RegistrationDb, String> {

	RegistrationDb findByUserName( String userName);

	List<RegistrationDb> findByUserNameOrEmail( String userName, String email);

}
