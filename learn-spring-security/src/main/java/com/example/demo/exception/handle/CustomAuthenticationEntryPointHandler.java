package com.example.demo.exception.handle;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.demo.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CustomAuthenticationEntryPointHandler implements AuthenticationEntryPoint {

	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
		
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setError(httpStatus.name());
		errorResponse.setMessage(authException.getMessage());
		errorResponse.setTimeStamp(Calendar.getInstance());
		errorResponse.setPath(request.getServletPath());
		errorResponse.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getOutputStream().println(objectMapper.writeValueAsString(errorResponse));
		
	}
}
