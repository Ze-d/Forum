package com.example.forum.provider;

import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import okhttp3.*;
import com.alibaba.fastjson2.JSON;
import com.example.forum.dto.GithubUserDTO;
import com.example.forum.dto.AccessTokenDTO;

@Component
public class GithubProvider {
    private static final Logger LOGGER = Logger.getLogger(GithubProvider.class.getName());

    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        final MediaType mediaType = MediaType.Companion.parse("application/json");

        OkHttpClient client = new OkHttpClient();

        String accString = JSON.toJSONString(accessTokenDTO);

        RequestBody body = RequestBody.Companion.create(accString,mediaType);
        Request request = new Request.Builder()
            .url("https://github.com/login/oauth/access_token?client_id="
            +accessTokenDTO.getClientId()
            +"&client_secret="+accessTokenDTO.getClientSecret()
            +"&code="+accessTokenDTO.getCode()
            +"&redirect_uri="+accessTokenDTO.getRedirectUri()
            +"&state="+accessTokenDTO.getState())
            .post(body)
            .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String[] split = string.split("&");
            String token = split[0].split("=")[1];
            LOGGER.info("Access Token: " + token);
            return token;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUserDTO getUser(String accessToken) {
        final OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                // token 后面有一个空格
                .header("Authorization", "token " + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUserDTO githubUserDTO = JSON.parseObject(string, GithubUserDTO.class);
            return githubUserDTO;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
