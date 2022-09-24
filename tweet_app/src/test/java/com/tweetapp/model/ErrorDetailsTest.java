package com.tweetapp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ErrorDetailsTest {

	ErrorDetails errorDetail;
	@BeforeEach
	public void init(){
		errorDetail=new ErrorDetails();
		errorDetail.setCode("404");
		errorDetail.setMsg("Not Found");
	}
	
	@Test
	void getCodeTest() {
		assertEquals("404", errorDetail.getCode());
	}
	
	@Test
	void getMsgTest() {
		assertEquals("Not Found", errorDetail.getMsg());
	}
}
