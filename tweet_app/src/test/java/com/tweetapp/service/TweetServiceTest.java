package com.tweetapp.service;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.tweetapp.Kafka.ProducerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tweetapp.exception.TweetNotFoundException;
import com.tweetapp.exception.UserAlreadyExistsException;
import com.tweetapp.exception.UserNotFoundException;
import com.tweetapp.model.RegistrationDb;
import com.tweetapp.model.TweetDb;
import com.tweetapp.model.TweetReplyRequest;
import com.tweetapp.model.TweetUserForgotPassword;
import com.tweetapp.model.UpdateTweetRequest;
import com.tweetapp.model.User;
import com.tweetapp.repository.RegistrationRepository;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class TweetServiceTest {

	@InjectMocks
	TweetService tweetService;

	@Mock
	TweetRepository tweetRepository;

	@Mock
	UserRepository userRepository;

	@Mock
	RegistrationRepository registrationRepository;

	@Mock
	ProducerService kafkaProducer;

	RegistrationDb registrationDb;

	RegistrationDb registrationDb2;

	TweetDb tweetDb;

	TweetDb tweetDb2;

	User user;

	@BeforeEach
	public void init() {
		registrationDb = new RegistrationDb();
		registrationDb.setFirstName("test-first");
		registrationDb.setLastName("test-last");
		registrationDb.setUserName("test-un");
		registrationDb.setPassword("test-pass");
		registrationDb.setConfirmPassword("test-pass");
		registrationDb.setContactNumber("1234567890");
		registrationDb.setEmail("test@test.com");

		registrationDb2 = new RegistrationDb();
		registrationDb2.setFirstName("test-first");
		registrationDb2.setLastName("test-last");
		registrationDb2.setUserName("test-un");
		registrationDb2.setPassword("test-pass");
		registrationDb2.setConfirmPassword("test-pass2");
		registrationDb2.setContactNumber("1234567890");
		registrationDb2.setEmail("test@test.com");

		user = new User();
		user.setFirstName(registrationDb.getFirstName());
		user.setLastName(registrationDb.getLastName());
		user.setEmailId(registrationDb.getEmail());
		user.setUserName(registrationDb.getUserName());
		user.setContactNumber(registrationDb.getContactNumber());

		tweetDb = new TweetDb();
		tweetDb.setUserName("test-un");
		tweetDb.setTweetId(1001);
		tweetDb.setLike(1);
		tweetDb.setTweet("test-tweet");
		tweetDb.setTweetTag("tag");
		List<String> reply = new ArrayList<>();
		reply.add("test-reply");
		tweetDb.setTweetReply(reply);

		tweetDb2 = new TweetDb();
		tweetDb2.setUserName("test-u");
		tweetDb2.setTweetId(1001);
		tweetDb2.setLike(1);
		tweetDb2.setTweet("test-tweet");
		tweetDb2.setTweetTag("tag");
	}

	@Test
	void createUserSuccess() throws Exception {
		when(registrationRepository.findByUserNameOrEmail(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
				.thenReturn(new ArrayList<>());
		when(registrationRepository.save(registrationDb)).thenReturn(null);
		when(userRepository.save(ArgumentMatchers.any())).thenReturn(user);
		assertEquals(registrationDb.getEmail(), tweetService.createUser(registrationDb).getEmailId());

	}

	@Test
	void createUserExceptionTest() throws Exception {
		when(registrationRepository.findByUserNameOrEmail(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
				.thenReturn(new ArrayList<>());
		assertThrows(Exception.class, () -> tweetService.createUser(registrationDb2));
	}

	@Test
	void createUser() throws Exception {
		List<RegistrationDb> listReg = new ArrayList<>();
		listReg.add(registrationDb);
		listReg.add(new RegistrationDb());
		when(registrationRepository.findByUserNameOrEmail(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
				.thenReturn(listReg);
		assertThrows(UserAlreadyExistsException.class, () -> tweetService.createUser(registrationDb));
	}

	@Test
	void forgotPasswordTest() {
		TweetUserForgotPassword forgotPassword = new TweetUserForgotPassword();
		forgotPassword.setUserName(registrationDb.getUserName());
		forgotPassword.setNewPassword(registrationDb.getPassword());
		forgotPassword.setConfirmPassword(registrationDb.getConfirmPassword());
		when(registrationRepository.findByUserName(ArgumentMatchers.anyString())).thenReturn(registrationDb);
		when(registrationRepository.save(ArgumentMatchers.any())).thenReturn(null);
		Assertions.assertNotNull(tweetService.forgotPassword(forgotPassword));

	}

	@Test
	void getAllTweetSuccessTest() {
		List<TweetDb> listTweet = new ArrayList<TweetDb>();
		listTweet.add(tweetDb);
		when(tweetRepository.findAll()).thenReturn(listTweet);
		assertEquals(tweetDb.getUserName(), tweetService.getAllTweet().get(0).getUserName());
	}

	@Test
	void postTweetSuccess() {
		when(userRepository.findByUserName(ArgumentMatchers.anyString())).thenReturn(user);
		when(tweetRepository.save(ArgumentMatchers.any())).thenReturn(tweetDb);
		assertEquals(tweetDb.getTweetTag(), tweetService.postTweet(tweetDb).getTweetTag());
	}

	@Test
	void postTweetTest() {
		when(userRepository.findByUserName(ArgumentMatchers.anyString())).thenReturn(user);
		assertThrows(UserNotFoundException.class, () -> tweetService.postTweet(tweetDb2));
	}

	@Test
	void getAllUsersSuccess() {
		List<User> listUser = new ArrayList<>();
		listUser.add(user);
		when(userRepository.findAll()).thenReturn(listUser);
		assertEquals(user.getContactNumber(), tweetService.getAllUsers().get(0).getContactNumber());
	}

	@Test
	void getUserByUserNameSuccess() {
		when(userRepository.findByUserName(ArgumentMatchers.anyString())).thenReturn(user);
		assertEquals(user.getFirstName(), tweetService.getUserByUserName(user.getUserName()).getFirstName());
	}

	@Test
	void getTweetByUserNameSuccess() {
		List<TweetDb> listTweet = new ArrayList<TweetDb>();
		listTweet.add(tweetDb);
		when(tweetRepository.findByUserName(ArgumentMatchers.anyString())).thenReturn(listTweet);
		assertEquals(tweetDb.getTweetId(),
				tweetService.getTweetByUserName(tweetDb.getUserName()).get(0).getTweetId());
	}

	@Test
	void updateTweetByUserNameAndTweetIdSuccess() {
		UpdateTweetRequest updateTweetRequest = new UpdateTweetRequest();
		updateTweetRequest.setTweetId(tweetDb.getTweetId());
		updateTweetRequest.setUserName(tweetDb.getUserName());
		updateTweetRequest.setTweet(tweetDb.getTweet());
		when(tweetRepository.getTweetDbByTweetIdAndUserName(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString()))
				.thenReturn(tweetDb);
		when(tweetRepository.save(ArgumentMatchers.any())).thenReturn(tweetDb);
		assertEquals(tweetDb.getLike(), tweetService
				.updateTweetByUserNameAndTweetId(tweetDb.getUserName(), tweetDb.getTweetId(), updateTweetRequest)
				.getLike());
	}

	@Test
	void deleteTweetByTweetIdAndUserNameSuccess() {
		when(tweetRepository.getTweetDbByTweetIdAndUserName(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString()))
				.thenReturn(tweetDb);
		Assertions.assertNotNull(tweetService.deleteTweetByTweetIdAndUserName(tweetDb.getTweetId(), tweetDb.getUserName()));
	}

	@Test
	void likeTweetByTweetIdAndUserNameSuccess() {
		when(tweetRepository.findByTweetId(ArgumentMatchers.anyInt())).thenReturn(tweetDb);
		when(tweetRepository.save(ArgumentMatchers.any())).thenReturn(tweetDb);
		assertEquals(tweetDb.getTweetReply(),
				tweetService.likeTweetByTweetIdAndUserName(tweetDb.getTweetId()).getTweetReply());
	}

	@Test
	void replyToTweetByTweetSuccess() {
		TweetReplyRequest replyRequest = new TweetReplyRequest();
		replyRequest.setTweetId(tweetDb.getTweetId());
		replyRequest.setTweetReply("tweet");
		when(tweetRepository.findByTweetId(ArgumentMatchers.anyInt())).thenReturn(tweetDb);
		when(tweetRepository.save(ArgumentMatchers.any())).thenReturn(tweetDb);
		assertEquals(tweetDb.getLike(),
				tweetService.replyToTweetByTweet(tweetDb.getTweetId(), replyRequest).getLike());
	}
}
