package com.example.demo.controller;

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
import com.example.demo.request.LoginRequest;
import com.example.demo.request.RegisterRequest;
import com.example.demo.response.LoginResponse;
import com.example.demo.service.UserService;
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

        // Trả về jwt cho người dùng.
		CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        String jwt = tokenProvider.generateToken(customUserDetail);
        
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(jwt);
        loginResponse.setUser(customUserDetail.getUser());
        loginResponse.setTokenType("Bearber");
        
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
	}

}
