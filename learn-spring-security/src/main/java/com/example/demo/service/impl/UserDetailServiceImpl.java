package com.example.demo.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repo.UserRepo;
import com.example.demo.userdetail.CustomUserDetail;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUsername(username);
		if (null == user) {
			throw new UsernameNotFoundException(username);
		}
		return new CustomUserDetail(user);
	}

	public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
		Optional<User> userOptional = userRepo.findById(id);
		if (userOptional.isEmpty()) {
			return null;
		}
		return new CustomUserDetail(userOptional.get());
	}

}
