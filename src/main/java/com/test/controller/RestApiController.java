package com.test.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.test.config.jwt.JwtTokenProvider;
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

	@PostMapping("/login")
	public Map<String, String> login(@RequestBody User user) {
		Map<String, String> result  = new HashMap<>();
		System.out.println("로그인 컨트롤러 진입");
		System.out.println("로그인 아이디 : "+ user);
		User loginUser =  userRepoitory.findByUsername(user.getUsername());
		
		if(loginUser == null) {
			result.put("false", "해당하는 아이디가 없습니다");
			return result;
		}
		
		
		if(!passwordEncoder.matches(user.getPassword(), loginUser.getPassword())) {
			result.put("false", "비밀번호가 일치하지 않습니다");
			return result;
		}
		
		result.put("token", 
			new JwtTokenProvider().createToken(loginUser.getId(), loginUser.getUsername()));
		return result;
	}
	
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
