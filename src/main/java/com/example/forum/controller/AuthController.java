package com.example.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.forum.provider.GithubProvider;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// import javax.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.example.forum.dto.AccessTokenDTO;
import com.example.forum.dto.GithubUserDTO;
import com.example.forum.mapper.UserMapper;
import com.example.forum.model.User;

@Controller
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired 
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(
            @RequestParam(name = "code") String code,
            @RequestParam(name = "state") String state,
            HttpServletRequest request,
            HttpServletResponse response) {
        LOGGER.info("Received callback with code: {}, state: {}", code, state);
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirectUri(redirectUri);
        accessTokenDTO.setClientId(clientId); 
        accessTokenDTO.setClientSecret(clientSecret);

        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUserDTO githubUser = githubProvider.getUser(accessToken);
        LOGGER.info("GitHub User: {}", githubUser != null ? githubUser.getLogin() : "null");
        if (githubUser != null && githubUser.getId() != null) {
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setName(githubUser.getLogin());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setToken(token);
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(githubUser.getAvatar_url());
            userMapper.insert(user);
            response.addCookie(new Cookie("token", token)); // Set token in cookie
            return "redirect:/"; // Redirect to home page after successful login
        }else{
            return "redirect:/"; // Redirect to home page if user is null
        }
    }

}