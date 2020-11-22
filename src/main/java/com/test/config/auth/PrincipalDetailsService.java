package com.test.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.test.model.User;
import com.test.repository.UserRepository;

@Service
public class PrincipalDetailsService implements UserDetailsService{
	
	private final UserRepository userRepository;
	
	public PrincipalDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("PrincipalDetailsService 진입");
		System.out.println("파라미터로 받은 아이디 : " +username);
		User userEntity = userRepository.findByUsername(username);
		System.out.println("로그인 인증 정보 : " +  userEntity);
		return new PrincipalDetails(userEntity);
	}
	
}
