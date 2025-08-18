package com.example.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.forum.provider.GithubProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.forum.dto.AccessTokenDTO;
import com.example.forum.dto.GithubUserDTO;

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

    @GetMapping("/callback")
    public String callback(
            @RequestParam(name = "code") String code,
            @RequestParam(name = "state") String state) {
        LOGGER.info("Received callback with code: {}, state: {}", code, state);
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirectUri(redirectUri);
        accessTokenDTO.setClientId(clientId); 
        accessTokenDTO.setClientSecret(clientSecret);

        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUserDTO user = githubProvider.getUser(accessToken);
        LOGGER.info("GitHub User: {}", user != null ? user.getLogin() : "null");
        return "index";
    }

}