package com.example.forum.dto;

import lombok.Data;

@Data
public class GithubUserDTO {

    private String login;
    private Long id;
    private String bio;
    private String avatar_url;
}
