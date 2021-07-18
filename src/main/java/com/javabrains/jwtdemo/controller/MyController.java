package com.javabrains.jwtdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javabrains.jwtdemo.bean.AuthenticateRequest;
import com.javabrains.jwtdemo.bean.AuthenticationResponse;
import com.javabrains.jwtdemo.service.MyUserDetailsService;
import com.javabrains.jwtdemo.util.JwtUtil;

@RestController
@RequestMapping(path = "/api")
public class MyController {

	@Autowired
	private JwtUtil jwtTokenUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	@PostMapping(path = "/hello")
	public String sayHello() {
		return "Hello, World!";
	}
	
	@PostMapping(value = "/auth")
	public ResponseEntity<?> createAuthenticationToken(
			@RequestBody AuthenticateRequest authenticationRequest) throws Exception {
		
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
			);
		} catch (AuthenticationException e) {
			throw new Exception("AuthenticationException: " + e.getMessage());
		}
		
		final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String jwt = jwtTokenUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
		
	}
}
