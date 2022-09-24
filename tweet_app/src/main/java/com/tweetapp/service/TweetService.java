package com.tweetapp.service;

import java.util.ArrayList;
import java.util.List;

import com.tweetapp.Kafka.ProducerService;
import com.tweetapp.model.*;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tweetapp.exception.UserAlreadyExistsException;
import com.tweetapp.exception.UserNotFoundException;
import com.tweetapp.repository.RegistrationRepository;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.repository.UserRepository;

@Log4j2
@Service
public class TweetService {

	@Autowired
	TweetRepository tweetRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RegistrationRepository registrationRepository;

//	@Autowired
//	ProducerService kafkaProducer;

	Logger logger = LoggerFactory.getLogger(TweetService.class);

	/**
	 * new User registartion
	 *
	 * @param registrationDb
	 * @return
	 * @throws Exception
	 */
	public User createUser(RegistrationDb registrationDb) throws Exception {
		logger.info("Inside CreatUser");
		if (registrationRepository.findByUserNameOrEmail(registrationDb.getUserName(), registrationDb.getEmail())
				.isEmpty()) {
			if (registrationDb.getPassword().equals(registrationDb.getConfirmPassword())) {
				User user = addObjectToUserFromRegistration(registrationDb);
				registrationRepository.save(registrationDb);
				//kafkaProducer.sendMessage("User created...");
				logger.info("Registration successfully completed");
				return userRepository.save(user);
			} else {
				throw new Exception("New Password and confirm password must be same..!");
			}
		} else {
			throw new UserAlreadyExistsException("User Already exists so you cannot add this user");
		}
	}

	public User addObjectToUserFromRegistration(RegistrationDb registrationDb) {
		User user = new User();
		user.setUserName(registrationDb.getUserName());
		user.setFirstName(registrationDb.getFirstName());
		user.setLastName(registrationDb.getLastName());
		user.setEmailId(registrationDb.getEmail());
		user.setContactNumber(registrationDb.getContactNumber());
		return user;
	}

	/**
	 * Forgot password
	 *
	 * @param forgotPassword
	 * @return
	 */
	public TweetForgotPasswordResponse forgotPassword(TweetUserForgotPassword forgotPassword) {
		RegistrationDb registrationDb = registrationRepository.findByUserName(forgotPassword.getUserName());
		registrationDb.setPassword(forgotPassword.getNewPassword());
		registrationDb.setConfirmPassword(forgotPassword.getConfirmPassword());
		registrationRepository.save(registrationDb);
		//kafkaProducer.sendMessage("password changed successfully...!");
		logger.info("Completed..");

		TweetForgotPasswordResponse tweetForgotPasswordResponse = new TweetForgotPasswordResponse();
		String status ="password changed successfully...!";
		tweetForgotPasswordResponse.setStatus(status);
		return tweetForgotPasswordResponse;
	}

	/**
	 * Get all tweets
	 *
	 * @return
	 */
	public List<TweetDb> getAllTweet() {
		List<TweetDb> listTweets = null;
		try {
			listTweets = tweetRepository.findAll();
			logger.info("Retriving all the tweet data");
			//kafkaProducer.sendMessage("Get all fetch " + listTweets.size() + " data from DB");
			logger.info("hi");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return listTweets;
	}

	/**
	 * Post a new tweet
	 *
	 * @param tweetDb
	 * @return
	 */
	public TweetDb postTweet(TweetDb tweetDb) {
		if (tweetDb.getTweetReply() == null) {
			tweetDb.setTweetReply(new ArrayList<>());
		}
		if (tweetDb.getUserName().equals(userRepository.findByUserName(tweetDb.getUserName()).getUserName())) {
			logger.info("Tweet is updated successfully...");
			//kafkaProducer.sendMessage("Tweet posted by the user : ");
			return tweetRepository.save(tweetDb);
		} else {
			throw new UserNotFoundException("Give Valid User name...!");
		}
	}

	/**
	 * Get all users
	 *
	 * @return
	 */
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	/**
	 * Get User Details By Username
	 *
	 * @param userName
	 * @return
	 */
	public User getUserByUserName(String userName) {
		log.debug("Getting the user by the userName..{}" , userName);
		//kafkaProducer.sendMessage("Getting the user by the userName ");
		return userRepository.findByUserName(userName);
	}

	/**
	 * Get all tweets by username
	 *
	 * @param userName
	 * @return
	 */
	public List<TweetDb> getTweetByUserName(String userName) {
		log.debug("Retriving tweets of user: " + userName);
		//kafkaProducer.sendMessage("Retriving tweets of user");
		return tweetRepository.findByUserName(userName);

	}

	/**
	 * Get all tweets by username
	 *
	 * @param userName
	 * @return
	 */
	public TweetDb getTweetByTweetId(int tweetId) {
		log.debug("Retriving tweet of tweetId: {}" , tweetId);
		//kafkaProducer.sendMessage("Retriving tweet of tweetId:");
		return tweetRepository.findByTweetId(tweetId);

	}

	/**
	 * Update Tweet by Username and Id
	 *
	 * @param userName
	 * @param tweetId
	 * @param updateTweetRequest
	 * @return
	 */
	public TweetDb updateTweetByUserNameAndTweetId(String userName, int tweetId,
												   UpdateTweetRequest updateTweetRequest) {
		TweetDb tweet = tweetRepository.getTweetDbByTweetIdAndUserName(tweetId, userName);
		tweet.setTweet(updateTweetRequest.getTweet());
		logger.info("Tweet is updated successfully...");
		return tweetRepository.save(tweet);
	}

	/**
	 * Delete tweet by Username and Id
	 *
	 * @param tweetId
	 * @param userName
	 * @return
	 */
	public TweetDeleteResponse deleteTweetByTweetIdAndUserName(int tweetId, String userName) {
		if (tweetRepository.getTweetDbByTweetIdAndUserName(tweetId, userName) != null) {
			tweetRepository.deleteTweetDbByTweetIdAndUserName(tweetId, userName);
			log.debug("Deleted the tweet for the tweet id {}" , tweetId);
			TweetDeleteResponse tweetDeleteResponse = new TweetDeleteResponse();
			String status = "successfully deleted...!";
			tweetDeleteResponse.setStatus(status);
			return tweetDeleteResponse;
		} else {
			TweetDeleteResponse tweetDeleteResponse = new TweetDeleteResponse();
			String status = "Tweet not found...!";
			tweetDeleteResponse.setStatus(status);
			return tweetDeleteResponse;
		}
	}

	/**
	 * Like tweet by Username and Id
	 *
	 * @param tweetId
	 * @return
	 */
	public TweetDb likeTweetByTweetIdAndUserName(int tweetId) {
		TweetDb tweet = tweetRepository.findByTweetId(tweetId);
		tweet.setLike(tweet.getLike() + 1);
		logger.info("Liked Tweet with Id: {} is", tweetId);
		return tweetRepository.save(tweet);
	}

	/**
	 * Reply to a tweet by Id
	 *
	 * @param tweetId
	 * @param replyRequest
	 * @return
	 */
	public TweetDb replyToTweetByTweet(int tweetId, TweetReplyRequest replyRequest) {
		TweetDb tweet = tweetRepository.findByTweetId(tweetId);
		List<String> tweetReply = tweet.getTweetReply();
		tweetReply.add(replyRequest.getTweetReply());
		tweet.setTweetReply(tweetReply);
		return tweetRepository.save(tweet);
	}

}
