package com.youtube.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class JwtApplication {

	@GetMapping("")
	public String hello(){
		return "Hello world";
	}
	public static void main(String[] args) {
		SpringApplication.run(JwtApplication.class, args);
	}

}
