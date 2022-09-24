package com.tweetapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tweetapp.model.RegistrationDb;
import com.tweetapp.model.TweetUserForgotPassword;
import com.tweetapp.model.User;
import com.tweetapp.service.TweetService;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
	
	@InjectMocks
    UserController userController;
	
	@Mock
	TweetService tweetService;

	@Test
	void registerUserTest() throws Exception {
		RegistrationDb register=new RegistrationDb();
		when(tweetService.createUser(ArgumentMatchers.any())).thenReturn(new User());
		assertEquals(201, userController.registerUser(register).getStatusCodeValue());
	}
	
	@Test
	void forgotPasswordSuccessTest() {
		TweetUserForgotPassword forgotPassword=new TweetUserForgotPassword();
		forgotPassword.setUserName("test-user1");
		forgotPassword.setNewPassword("test-pass");
		forgotPassword.setConfirmPassword("test-pass");
		assertEquals(200, userController.forgotPassword(forgotPassword).getStatusCodeValue());
	}
	
	@Test
	void forgotPassword404Test() {
		TweetUserForgotPassword forgotPassword=new TweetUserForgotPassword();
		forgotPassword.setUserName("test-user1");
		forgotPassword.setNewPassword("test-pass");
		forgotPassword.setConfirmPassword("test");
		assertEquals(400, userController.forgotPassword(forgotPassword).getStatusCodeValue());
	}
}
