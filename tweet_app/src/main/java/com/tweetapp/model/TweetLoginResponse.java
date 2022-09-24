package com.tweetapp.model;

public class TweetLoginResponse {
	
	private final String jwt;
	private String loginStatus;
	private User user;
	
	public TweetLoginResponse(String jwt, String loginStatus, User user) {
		this.jwt=jwt;
		this.loginStatus=loginStatus;
		this.user = user;
	}
	
	public String getJwt() {
		return jwt;
	}
	public String getLoginStatus(){
		return loginStatus;
	}

	public User getUser() {
		return user;
	}
}
