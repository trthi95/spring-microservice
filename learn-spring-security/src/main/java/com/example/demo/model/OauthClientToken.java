package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "oauth_client_token")
@Data
public class OauthClientToken {
	@Id
	private String authentication_id;
	
	private String token_id;
	
	@Column(length = 100000)
	private byte[] token;
	
	private String user_name;
	
	private String client_id;

}
