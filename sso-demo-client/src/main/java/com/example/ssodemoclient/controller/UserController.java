package com.example.ssodemoclient.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 客户端获取用户信息
 * @author: Libaoyun
 * @date: 2023-01-05 20:33
 **/
@RestController
@RequestMapping("/user")
public class UserController {


    /**
     * 获取当前用户信息
     * @param authentication
     * @return
     */
    @RequestMapping("/current-user")
    public Object getCurrentUser(Authentication authentication){
        return authentication;
    }
}
