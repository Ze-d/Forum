package com.example.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.forum.mapper.QuestionMapper;
import com.example.forum.mapper.UserMapper;
import com.example.forum.model.Question;
import com.example.forum.model.User;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;

@Controller
public class PublishController {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/publish")
    public String publish(Model model) {
        model.addAttribute("question", new Question());
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
        @RequestParam(name = "title",required = false) String title,
        @RequestParam(name = "description",required = false) String description,
        @RequestParam(name = "tag",required = false) String tag,
        HttpServletRequest request,
        Model model
    ) {

        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        if (title == null || title.isEmpty()) {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (description == null || description.isEmpty()) {
            model.addAttribute("error", "内容不能为空");
            return "publish";
        }
        if (tag == null || tag.isEmpty()) {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }


        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "publish";
        }
        Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length != 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("token")) {
					String token = cookie.getValue();
					user = userMapper.findByToken(token);
					if (user != null) {
						request.getSession().setAttribute("user", user);
					}
					break;
				}
			}
		}
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        questionMapper.create(question);
        return "publish";
    }
}
