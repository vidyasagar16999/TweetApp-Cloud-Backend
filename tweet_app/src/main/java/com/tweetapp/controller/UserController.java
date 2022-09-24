package com.tweetapp.controller;

import com.tweetapp.model.*;
import com.tweetapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.tweetapp.repository.RegistrationRepository;
import com.tweetapp.service.UserDetailsService;
import com.tweetapp.service.TweetService;
import com.tweetapp.util.JwtTokenUtil;

@CrossOrigin(origins = "*")
@RestController
//@RequestMapping("/api/v1.0/tweets")
public class UserController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Autowired
	UserDetailsService myUserDetailService;

	@Autowired
	TweetService tweetService;

	@Autowired
	RegistrationRepository registrationRepository;

	@Autowired
	UserRepository userRepository;

	Logger logger = LoggerFactory.getLogger(UserController.class);

	/**
	 * User Login
	 * @param loginRequest
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/login")
	public ResponseEntity<TweetLoginResponse> createAuthenticationToken(@RequestBody TweetLoginRequest loginRequest)
			throws Exception {
		logger.debug("In User login()");
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect User-name or password ", e);

		}
		final UserDetails userDetails = myUserDetailService.loadUserByUsername(loginRequest.getUserName());
		final String jwt = jwtTokenUtil.generateToken(userDetails);
		String loginStatus ="success";
		User user = userRepository.findByUserName(loginRequest.getUserName());
		return ResponseEntity.ok(new TweetLoginResponse(jwt,loginStatus, user));
	}

	/**
	 * New User registration
	 * @param registrationDb
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/register")
	public ResponseEntity<User> registerUser(@RequestBody RegistrationDb registrationDb) throws Exception {
		User user = tweetService.createUser(registrationDb);
		logger.info("User created successfully!!!...");
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}

	/**
	 * Forgot password
	 * @param forgotPassword
	 * @return
	 */
	@PostMapping("/{userName}/forgot")
	public ResponseEntity<TweetForgotPasswordResponse> forgotPassword(@RequestBody TweetUserForgotPassword forgotPassword) {
		logger.info("Forgot Password request along with username: ");
		if (forgotPassword.getNewPassword().equals(forgotPassword.getConfirmPassword())) {
			TweetForgotPasswordResponse tweetForgotPasswordResponse = tweetService.forgotPassword(forgotPassword);
		return new ResponseEntity<>(tweetForgotPasswordResponse, HttpStatus.OK);
		}
		else {
			TweetForgotPasswordResponse tweetForgotPasswordResponse = new TweetForgotPasswordResponse();
			tweetForgotPasswordResponse.setStatus("Wrong credentials");
			return new ResponseEntity<>(tweetForgotPasswordResponse, HttpStatus.BAD_REQUEST);
		}
	}
}
