package com.example.demo.response;

import com.example.demo.model.User;

import lombok.Data;

@Data
public class LoginResponse {
	private String accessToken;
	
	private User user;
	
	private String tokenType;
	
	private String refreshToken;
}
