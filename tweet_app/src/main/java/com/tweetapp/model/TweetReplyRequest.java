package com.tweetapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TweetReplyRequest {

	private int tweetId;
	
	private String tweetReply;
}
