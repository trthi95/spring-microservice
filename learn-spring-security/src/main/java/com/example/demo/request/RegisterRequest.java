package com.example.demo.request;

import com.example.demo.model.Role;

import lombok.Data;

@Data
public class RegisterRequest {
	
	private String username;
	
	private String password;
	
	private Role[] roles;
}
