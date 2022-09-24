package com.tweetapp.controller;

import java.util.List;

import com.tweetapp.model.*;
import com.tweetapp.service.TweetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tweetapp.exception.TweetNotFoundException;

@CrossOrigin(origins = "*")
@RestController
//@RequestMapping("/api/v1.0/tweets")
public class TweetController {

	@Autowired
	TweetService tweetService;

	Logger logger = LoggerFactory.getLogger(TweetController.class);

	/**
	 * Show all tweets
	 * @return
	 */
	@GetMapping("/all")
	public ResponseEntity<?> getAllTweets() {
		logger.info("get all the tweets by all users");
		List<TweetDb> listTweets = tweetService.getAllTweet();
		return new ResponseEntity<>(listTweets, HttpStatus.OK);
	}

	/**
	 * Post a new Tweet
	 * @param userName
	 * @param tweetDb
	 * @return
	 */
	@PostMapping("/{userName}/add")
	public ResponseEntity<?> postTweet(@PathVariable(value = "userName") String userName, @RequestBody TweetDb tweetDb) {
		TweetDb tweetResponse = tweetService.postTweet(tweetDb);
		logger.info("Tweet posted by the user - Success");
		return new ResponseEntity<>(tweetResponse, HttpStatus.CREATED);
	}

	/**
	 * Get all Users List
	 * @return
	 */
	@GetMapping("/user/all")
	public ResponseEntity<List<User>> getAllUsers() {
		logger.info("Get all the users");
		List<User> listUsers = tweetService.getAllUsers();
		return new ResponseEntity<>(listUsers, HttpStatus.OK);
	}

	/**
	 * Search For an User
	 * @param userName
	 * @return
	 */
	@GetMapping("/user/search/{userName}")
	public ResponseEntity<User> getUserByUserName(@PathVariable(value = "userName") String userName) {
		logger.info("Get the user details by username");
		User user = tweetService.getUserByUserName(userName);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	/**
	 * Get Tweets based on tweetId
	 * @param tweetId
	 * @return
	 */
	@GetMapping("/tweetDetail/{tweetId}")
	public ResponseEntity<TweetDb> getTweetByTweetId(@PathVariable(value = "tweetId") int tweetId) {
		logger.info("Get the tweet by tweetId");
		TweetDb listUserTweet = tweetService.getTweetByTweetId(tweetId);
		if (listUserTweet != null) {
			return new ResponseEntity<>(listUserTweet, HttpStatus.OK);
		} else {
			throw new TweetNotFoundException("Tweets not found...!");
		}
	}

	/**
	 * Get Tweets based on UserName
	 * @param userName
	 * @return
	 */
	@GetMapping("/{userName}")
	public ResponseEntity<List<TweetDb>> getTweetByUserName(@PathVariable(value = "userName") String userName) {
		logger.info("Get all the tweets by username");
		List<TweetDb> listUserTweets = tweetService.getTweetByUserName(userName);
			return new ResponseEntity<>(listUserTweets, HttpStatus.OK);
	}

	/**
	 * Update/edit tweet by Username
	 * @param userName
	 * @param tweetId
	 * @param updateTweetRequest
	 * @return
	 */
	@PutMapping("/{userName}/update/{tweetId}")
	public ResponseEntity<?> updateTweetByUserNameAndTweetId(@PathVariable(value = "userName") String userName,
			@PathVariable(value = "tweetId") int tweetId, @RequestBody UpdateTweetRequest updateTweetRequest) {
		if(userName.equals(updateTweetRequest.getUserName())&& tweetId==updateTweetRequest.getTweetId()) {
		TweetDb tweet = tweetService.updateTweetByUserNameAndTweetId(userName, tweetId, updateTweetRequest);
			logger.info("Tweet updated by the user - success");
		return new ResponseEntity<>(tweet, HttpStatus.OK);
		}else {
			return new ResponseEntity<>("Please verify data given...!", HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/{userName}/delete/{tweetId}")
	public ResponseEntity<TweetDeleteResponse> deleteTweetByTweetID(@PathVariable(value = "userName") String userName,
																	@PathVariable(value = "tweetId") int tweetId) {
		logger.info("Tweet deleted by the user");
		TweetDeleteResponse response = tweetService.deleteTweetByTweetIdAndUserName(tweetId, userName);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Like a tweet by Username and Id
	 * @param userName
	 * @param tweetId
	 * @return
	 */
	@GetMapping("/{userName}/like/{tweetId}")
	public ResponseEntity<TweetDb> likeTweetByTweetIdAndUserName(@PathVariable(value = "userName") String userName,
			@PathVariable(value = "tweetId") int tweetId) {
		logger.info("Like a tweet ");
		TweetDb tweet = tweetService.likeTweetByTweetIdAndUserName(tweetId);
		return new ResponseEntity<>(tweet, HttpStatus.OK);
	}

	/**
	 * Reply to a tweet
	 * @param userName
	 * @param tweetId
	 * @param tweetReplyRequest
	 * @return
	 */
	@PutMapping("{userName}/reply/{tweetId}")
	public ResponseEntity<?> replyToTweetByTweet(@PathVariable(value = "userName") String userName,
			@PathVariable(value = "tweetId") int tweetId, @RequestBody TweetReplyRequest tweetReplyRequest) {
		logger.info("Replying to the tweet by user");
		if(tweetId== tweetReplyRequest.getTweetId()) {
		TweetDb tweet = tweetService.replyToTweetByTweet(tweetId, tweetReplyRequest);
		return new ResponseEntity<>(tweet, HttpStatus.OK);
		}else {
			return new ResponseEntity<>("check Id...!", HttpStatus.BAD_REQUEST);
		}
	}

}
