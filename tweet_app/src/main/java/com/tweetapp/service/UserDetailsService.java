package com.tweetapp.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tweetapp.model.RegistrationDb;
import com.tweetapp.repository.RegistrationRepository;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
	
	@Autowired
	RegistrationRepository registrationRepository;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		RegistrationDb registrationDb= registrationRepository.findByUserName(userName);
		return new User(registrationDb.getUserName(), registrationDb.getPassword(), new ArrayList<>());
	}
}
