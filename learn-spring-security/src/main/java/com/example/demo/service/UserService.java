package com.example.demo.service;

import com.example.demo.exception.model.UsernameExistsException;
import com.example.demo.exception.model.UsernamePasswordInputException;
import com.example.demo.model.User;
import com.example.demo.request.RegisterRequest;

public interface UserService {
	User saveUser(RegisterRequest registerRequest) throws UsernamePasswordInputException, UsernameExistsException;
}
