package com.example.demo.response;

import lombok.Data;

@Data
public class RefreshTokenResponse {
	private String refeshToken;
	
	private String accessToken;
	
	private String tokenType = "Bearer";
}
