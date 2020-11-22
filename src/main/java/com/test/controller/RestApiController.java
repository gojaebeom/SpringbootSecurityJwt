package com.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.test.model.User;
import com.test.repository.UserRepository;

@RestController
public class RestApiController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepoitory;

	@GetMapping({"/",""})
	public String home() {
		return "home";
	}
	
	@GetMapping("/user")
	public String user() {
		return "user";
	}
	
	@GetMapping("/admin")
	public String admin() {
		return "admin";
	}
	
// attemptAuthentication 에서 대신 로그인 인증
//	@PostMapping("/login")
//	public String login(@RequestBody String username) {
//		System.out.println("로그인 컨트롤러 진입");
//		System.out.println("로그인 아이디 : "+ username);
//		User user =  userRepoitory.findByUsername(username);
//		System.out.println("매치된 유저 정보 : " + user);
//		
//		return "login success";
//	}
	
	@PostMapping("/join")
	public String join(@RequestBody User user) {
		System.out.println("회원가입 컨트롤러 진입");
		System.out.println("회원가입 받은 객체 :" + user);
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole("ROLE_USER");
		
		userRepoitory.save(user);
		
		return "join success";
	}
}
