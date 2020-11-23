package com.test.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.test.config.auth.PrincipalDetails;
import com.test.model.User;
import com.test.repository.UserRepository;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter
{
	
	private UserRepository userRepository;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
		super(authenticationManager);
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String jwtHeader = request.getHeader("Authorization");
		System.out.println("Authorization :" + jwtHeader);
		
		//토큰이 없거나 조건 만족하지 못할 경우 서명 인증 안함
		if(jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
			System.out.println("토큰이 없음");
			chain.doFilter(request, response);
			return;
		}
		
		//토큰 값 만 빼오기
		String jwtToken = jwtHeader.replace("Bearer ", "");
		
		//토큰 디코딩하여 username 가져오기
		String username = JWT.require(Algorithm.HMAC512("studybook"))
				.build()
				.verify(jwtToken)
				.getClaim("username")
				.asString();
		
		System.out.println("디코딩된 유저아이디 : " +username);
		
		if(username != null) {
			User user = userRepository.findByUsername(username);
			System.out.println("디코딩된 id에 해당하는 유저 정보 " + user);
			PrincipalDetails principalDetails = new PrincipalDetails(user);
			
			Authentication authentication
			= new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
		
			System.out.println("authentication 객체 : " + authentication);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
			
		chain.doFilter(request, response);
	}

}
