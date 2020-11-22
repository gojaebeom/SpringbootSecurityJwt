package com.test.config.jwt;



import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.config.auth.PrincipalDetails;
import com.test.model.User;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	private final AuthenticationManager authenticationManager;
	
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		System.out.println("/login 시도 감지");
		System.out.println("JwtAuthenticationFilter 진입");
		
		ObjectMapper om = new ObjectMapper(); //json 데이터 파싱
		User user = null;
		
		try {
			 user = om.readValue(request.getInputStream(), User.class);
		}catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println("넘어온 User 데이터 : " + user);
		
		UsernamePasswordAuthenticationToken authenticationToken
		= new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
		
		System.out.println("username+password "+authenticationToken);
		
		Authentication authentication = authenticationManager.authenticate(authenticationToken);
		
		return authentication;
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		System.out.println("로그인 성공시 진입 : 인증완료");

		PrincipalDetails principalDetails = (PrincipalDetails)authResult.getPrincipal();
		
		String jwtToken = JWT.create()
				.withSubject("Token")
				.withExpiresAt(new Date(System.currentTimeMillis()+(60000*10)))
				.withClaim("id", principalDetails.getUser().getId())
				.withClaim("username", principalDetails.getUser().getUsername())
				.sign(Algorithm.HMAC512("studybook"));
		
		response.addHeader("Authorization", "Bearer "+jwtToken);
	}

}
