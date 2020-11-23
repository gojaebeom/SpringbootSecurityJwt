package com.test.config.jwt;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class JwtTokenProvider{
	
	private final String SECRET = "studybook";
	private final int EXPIRATION_TIME = 864000000;
	
	public String createToken(long userID, String username) {
		
		return JWT.create()
				.withSubject("Token")
				.withExpiresAt(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
				.withClaim("id", userID)
				.withClaim("username", username)
				.sign(Algorithm.HMAC512(SECRET));
	}
}
