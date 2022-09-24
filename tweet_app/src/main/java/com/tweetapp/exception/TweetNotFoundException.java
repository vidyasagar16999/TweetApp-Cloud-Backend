package com.tweetapp.exception;

public class TweetNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public TweetNotFoundException(String message) {
		super(message);
	}

}