package com.example.demo.exception.handle;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.example.demo.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		HttpStatus httpStatus = HttpStatus.FORBIDDEN;
		
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setError(httpStatus.name());
		errorResponse.setMessage(accessDeniedException.getMessage());
		errorResponse.setTimeStamp(Calendar.getInstance());
		errorResponse.setPath(request.getServletPath());
		errorResponse.setStatus( HttpServletResponse.SC_FORBIDDEN);
		
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.getOutputStream().println(objectMapper.writeValueAsString(errorResponse));
	}

}
