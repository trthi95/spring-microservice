package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "oauth_code")
@Data
public class OauthCode {
	@Id
	private String code;
	
	@Column(length = 100000)
	private byte[] authentication;

}
