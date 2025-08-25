package com.example.forum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.forum.dto.QuestionDTO;
import com.example.forum.mapper.UserMapper;
import jakarta.servlet.http.Cookie;

import com.example.forum.model.User;
import com.example.forum.service.QuestionService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

@Controller
public class IndexController {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private QuestionService questionService;

	@GetMapping("/")
	public String index(HttpServletRequest request,
			Model model) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length != 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("token")) {
					String token = cookie.getValue();
					User user = userMapper.findByToken(token);
					if (user != null) {
						request.getSession().setAttribute("user", user);
					}
					break;
				}
			}

		}

		List<QuestionDTO> questionsList = questionService.list();
		model.addAttribute("questions", questionsList);
		return "index";
	}
}