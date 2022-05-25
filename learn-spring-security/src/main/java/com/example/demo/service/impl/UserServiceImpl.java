package com.example.demo.service.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.demo.constant.CommonMessage;
import com.example.demo.exception.model.UsernameExistsException;
import com.example.demo.exception.model.UsernamePasswordInputException;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repo.RoleRepo;
import com.example.demo.repo.UserRepo;
import com.example.demo.request.RegisterRequest;
import com.example.demo.service.UserService;
import com.example.demo.userdetail.CustomUserDetail;
import com.example.demo.utils.JwtTokenProvider;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepo userRepo;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	RoleRepo roleRepo;

	@Autowired
	JwtTokenProvider tokenProvider;
	
	@Autowired
	UserDetailServiceImpl userDetailServiceImpl;

	@Override
	public User saveUser(RegisterRequest registerRequest)
			throws UsernamePasswordInputException, UsernameExistsException {
		String username = registerRequest.getUsername();
		String password = registerRequest.getPassword();

		if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
			throw new UsernamePasswordInputException(CommonMessage.ERROR_INPUT_USERNAME_PASSWORD);
		}

		if (userRepo.existsByUsername(username)) {
			throw new UsernameExistsException(CommonMessage.ERROR_USERNAME_EXISTS);
		}

		Role[] roles = registerRequest.getRoles();

		Set<Role> setRoles = new HashSet<>();
		if (roles.length > 0) {
			Arrays.stream(roles).forEach(role -> {
				Role newRole = roleRepo.findByName(role.getName());
				setRoles.add(newRole);
			});
		} else {
			Role role = roleRepo.findByName(CommonMessage.ROLE_USER);
			setRoles.add(role);
		}

		User user = new User();
		user.setUsername(username);
		user.setPassword(passwordEncoder.encode(password));
		user.setRoles(setRoles);

		return userRepo.save(user);
	}

	@Override
	public User updateTokenUserByLogin(CustomUserDetail customUserDetail) {
		User user = customUserDetail.getUser();

		String accessTokenRes = "";
		String refreshTokenRes = "";
		String accessToken = user.getAccessToken();
		if (tokenProvider.isExpiredToken(accessToken)) {
			accessTokenRes = updateAccessToken(user);
			user.setAccessToken(accessTokenRes);

			refreshTokenRes = updateRefreshToken(user);
			user.setRefreshToken(refreshTokenRes);

			return userRepo.save(user);
		}
		return user;
	}

	@Override
	public User updateTokenUserByRefreshToken(String refreshToken) throws Exception {
		
		boolean isValidToken = tokenProvider.validateToken(refreshToken);

		if (isValidToken) {
			String username = tokenProvider.getUsernameFromJwt(refreshToken);
			CustomUserDetail customUserDetail = (CustomUserDetail) userDetailServiceImpl.loadUserByUsername(username);
			User user = customUserDetail.getUser();
			
			if (!user.getRefreshToken().equals(refreshToken)) {
				throw new Exception("Refresh Token was used.");
			}
			
			String accessTokenRes = updateAccessToken(user);
			String refreshTokenRes = updateRefreshToken(user);
			user.setAccessToken(accessTokenRes);
			user.setRefreshToken(refreshTokenRes);
			return userRepo.save(user);
		} else {
			throw new Exception("Refresh Token is fail");
		}
	}

	@Override
	public String updateRefreshToken(User user) {
		return tokenProvider.generateRefreshToken(user);
	}

	@Override
	public String updateAccessToken(User user) {
		return tokenProvider.generateAccessToken(user);
	}
}
