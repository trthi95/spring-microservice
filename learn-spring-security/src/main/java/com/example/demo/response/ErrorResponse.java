package com.example.demo.response;

import java.util.Calendar;

import lombok.Data;

@Data
public class ErrorResponse {
	private Calendar timeStamp;
	private int status;
	private String error;
	private String message;
	private String path;

}
