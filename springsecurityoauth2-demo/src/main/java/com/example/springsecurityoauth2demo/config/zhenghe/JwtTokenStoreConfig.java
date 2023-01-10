package com.example.springsecurityoauth2demo.config.zhenghe;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * @description: Jwt转换配置类
 * @author: Libaoyun
 * @date: 2023-01-05 19:06
 **/
@Configuration
public class JwtTokenStoreConfig {
    // 这里就是将TokenStore转为JWT
    @Bean
    public TokenStore jwtTokenStore(){
        return new JwtTokenStore(jwtAccessTokenConverter());
    }
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        // 配置JWT使用的秘钥并返回JWT
        accessTokenConverter.setSigningKey("test_key");
        return accessTokenConverter;
    }
}
