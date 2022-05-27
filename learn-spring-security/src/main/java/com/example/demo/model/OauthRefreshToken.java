package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "oauth_refresh_token")
@Data
public class OauthRefreshToken {
	@Id
	private String token_id;
	
	@Column(length = 100000)
	private byte[] token;
	
	@Column(length = 100000)
	private byte[] authentication;

}
