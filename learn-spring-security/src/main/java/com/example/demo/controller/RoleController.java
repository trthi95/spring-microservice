package com.example.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class RoleController {
	
	@RequestMapping(path = "/admin" ,method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('role_admin')")
	public String admin() {
		return "admin";
	}
	
	@RequestMapping(path = "/user" ,method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('role_user') or hasAuthority('role_admin')")
	public String user() {
		return "user";
	}
	
	@RequestMapping(path = "/abc" ,method = RequestMethod.GET)
	public String abc() {
		return "abc";
	}
}
