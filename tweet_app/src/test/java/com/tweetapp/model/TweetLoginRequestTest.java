package com.tweetapp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TweetLoginRequestTest {

	TweetLoginRequest tweetLoginReq;
	
	@BeforeEach
	public void init(){
		tweetLoginReq=new TweetLoginRequest();
		tweetLoginReq.setUserName("test-u1");
		tweetLoginReq.setPassword("test-pass");
	}
	
	@Test
	void getUserName() {
		assertEquals("test-u1", tweetLoginReq.getUserName());
	}
	
	@Test
	void getPasswordTest() {
		assertEquals("test-pass", tweetLoginReq.getPassword());
	}
}
