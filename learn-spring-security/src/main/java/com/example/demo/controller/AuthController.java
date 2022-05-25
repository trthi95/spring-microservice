package com.example.demo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constant.CommonMessage;
import com.example.demo.exception.model.UsernameExistsException;
import com.example.demo.exception.model.UsernamePasswordInputException;
import com.example.demo.model.User;
import com.example.demo.request.LoginRequest;
import com.example.demo.request.RefreshTokenRequest;
import com.example.demo.request.RegisterRequest;
import com.example.demo.response.LoginResponse;
import com.example.demo.response.RefreshTokenResponse;
import com.example.demo.service.UserService;
import com.example.demo.service.impl.UserDetailServiceImpl;
import com.example.demo.userdetail.CustomUserDetail;
import com.example.demo.utils.JwtTokenProvider;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	UserDetailServiceImpl userDetailServiceImpl;
	
	@RequestMapping(path = "/register", method = RequestMethod.POST)
	public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
		String res = CommonMessage.REGISTER_SUCCESS;
		try {
			userService.saveUser(registerRequest);
		} catch (UsernamePasswordInputException | UsernameExistsException e) {
			res = e.getMessage();
		}
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
		String username = loginRequest.getUsername();
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(username, loginRequest.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);

		CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
		
		User user = userService.updateTokenUserByLogin(customUserDetail);
        
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(user.getAccessToken());
        loginResponse.setRefreshToken(user.getRefreshToken());
        loginResponse.setUser(user);
        loginResponse.setTokenType("Bearber");
        
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
	}

	@PostMapping("/refreshToken")
	public ResponseEntity<?> refeshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
		String refreshToken = refreshTokenRequest.getRefreshToken();
		
		try {
			User user = userService.updateTokenUserByRefreshToken(refreshToken);
			RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse();
			refreshTokenResponse.setAccessToken(user.getAccessToken());
			refreshTokenResponse.setRefeshToken(user.getRefreshToken());
			
			return new ResponseEntity<>(refreshTokenResponse, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
		
	}
}
