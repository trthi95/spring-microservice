package com.example.demo.service;

import com.example.demo.exception.model.UsernameExistsException;
import com.example.demo.exception.model.UsernamePasswordInputException;
import com.example.demo.model.User;
import com.example.demo.request.RegisterRequest;
import com.example.demo.userdetail.CustomUserDetail;

public interface UserService {
	User saveUser(RegisterRequest registerRequest) throws UsernamePasswordInputException, UsernameExistsException;
	
	User updateTokenUserByLogin(CustomUserDetail customUserDetail);

	String updateRefreshToken(User user);

	String updateAccessToken(User user);

	User updateTokenUserByRefreshToken(String refreshToken) throws Exception;
}
