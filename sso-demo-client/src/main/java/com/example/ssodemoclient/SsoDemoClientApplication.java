package com.example.ssodemoclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@SpringBootApplication
//开启单点登录功能注解
@EnableOAuth2Sso
public class SsoDemoClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoDemoClientApplication.class, args);
    }

}
