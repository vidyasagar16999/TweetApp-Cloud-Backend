package com.tweetapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tweetapp.exception.TweetNotFoundException;
import com.tweetapp.model.TweetDb;
import com.tweetapp.model.TweetReplyRequest;
import com.tweetapp.model.UpdateTweetRequest;
import com.tweetapp.model.User;
import com.tweetapp.service.TweetService;

@ExtendWith(MockitoExtension.class)
class TweetControllerTest {

	@InjectMocks
	TweetController tweetController;

	@Mock
	TweetService tweetService;

	TweetDb tweetDb;

	@BeforeEach
	public void init() {
		tweetDb = new TweetDb();
		tweetDb.setUserName("test-un");
		tweetDb.setTweetId(1001);
		tweetDb.setLike(1);
		tweetDb.setTweet("test-tweet");
		tweetDb.setTweetTag("tag");
		List<String> reply = new ArrayList<>();
		reply.add("test-reply");
		tweetDb.setTweetReply(reply);
	}

	@Test
	void getAllTweetsSuccessTest() {
		when(tweetService.getAllTweet()).thenReturn(new ArrayList<>());
		assertEquals(200, tweetController.getAllTweets().getStatusCodeValue());
	}

	@Test
	void postTweetSuccessTest() {
		when(tweetService.postTweet(tweetDb)).thenReturn(tweetDb);
		assertEquals(201, tweetController.postTweet("test-u1", tweetDb).getStatusCodeValue());
	}

	@Test
	void getAllUsersSuccessTest() {
		when(tweetService.getAllUsers()).thenReturn(new ArrayList<>());
		assertEquals(200, tweetController.getAllUsers().getStatusCodeValue());
	}

	@Test
	void getUserByUserNameSuccess() {
		when(tweetService.getUserByUserName(ArgumentMatchers.any())).thenReturn(new User());
		assertEquals(200, tweetController.getUserByUserName("test-u1").getStatusCodeValue());
	}

	@Test
	void getTweetByUserNameSuccessTest() {
		List<TweetDb> listTweetDb = new ArrayList<>();
		listTweetDb.add(tweetDb);
		when(tweetService.getTweetByUserName(ArgumentMatchers.any())).thenReturn(listTweetDb);
		assertEquals(200, tweetController.getTweetByUserName("test-u1").getStatusCodeValue());
	}

	@Test
	void updateTweetByUserNameAndTweetIdSuccessTest() {
		when(tweetService.updateTweetByUserNameAndTweetId(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(),
				ArgumentMatchers.any())).thenReturn(tweetDb);
		UpdateTweetRequest updateTweetRequest = new UpdateTweetRequest();
		updateTweetRequest.setUserName(tweetDb.getUserName());
		updateTweetRequest.setTweetId(tweetDb.getTweetId());
		updateTweetRequest.setTweet(tweetDb.getTweet());
		assertEquals(200, tweetController
				.updateTweetByUserNameAndTweetId(tweetDb.getUserName(), tweetDb.getTweetId(), updateTweetRequest)
				.getStatusCodeValue());
	}

	@Test
	void updateTweetByUserNameAndTweetIdTest() {
		UpdateTweetRequest updateTweetRequest = new UpdateTweetRequest();
		updateTweetRequest.setUserName("test");
		updateTweetRequest.setTweetId(tweetDb.getTweetId());
		updateTweetRequest.setTweet(tweetDb.getTweet());
		assertEquals(400, tweetController
				.updateTweetByUserNameAndTweetId(tweetDb.getUserName(), tweetDb.getTweetId(), updateTweetRequest)
				.getStatusCodeValue());
	}

	@Test
	void likeTweetByTweetIdAndUserNameSuccess() {
		when(tweetService.likeTweetByTweetIdAndUserName(tweetDb.getTweetId())).thenReturn(tweetDb);
		assertEquals(tweetDb.getTweetTag(), tweetController.likeTweetByTweetIdAndUserName(tweetDb.getUserName(), tweetDb.getTweetId()).getBody().getTweetTag());
	}

	@Test
	void replyToTweetByTweetSuccessTest() {
		TweetReplyRequest replyRequest=new TweetReplyRequest();
		replyRequest.setTweetId(tweetDb.getTweetId());
		replyRequest.setTweetReply("reply-test");
		when(tweetService.replyToTweetByTweet(tweetDb.getTweetId(), replyRequest)).thenReturn(tweetDb);
		assertEquals(200,tweetController.replyToTweetByTweet(tweetDb.getUserName(), tweetDb.getTweetId(), replyRequest).getStatusCodeValue());

	}

	@Test
	void replyToTweetByTweetTest() {
		TweetReplyRequest replyRequest=new TweetReplyRequest();
		replyRequest.setTweetId(2222);
		replyRequest.setTweetReply("reply-test");
		assertEquals(400,tweetController.replyToTweetByTweet(tweetDb.getUserName(), tweetDb.getTweetId(), replyRequest).getStatusCodeValue());
	}
}
