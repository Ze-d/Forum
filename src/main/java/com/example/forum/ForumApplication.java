package com.example.forum;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// springboot 3.x wont autoscan mappers
@MapperScan("com.example.forum.mapper") 
public class ForumApplication {

	public static void main(String[] args) {
		//  tomcat and spring mvc
		SpringApplication.run(ForumApplication.class, args);
	}

}
