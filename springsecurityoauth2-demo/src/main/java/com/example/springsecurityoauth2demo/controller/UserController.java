package com.example.springsecurityoauth2demo.controller;

import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * @description: 资源服务器可获取信息
 * @author: Libaoyun
 * @date: 2023-01-04 17:30
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    // 获取当前用户信息
    @GetMapping("/current-user")
    public Object getCurrentUser(Authentication authentication, HttpServletRequest request){
        // 这也可以用token,总之和前端约定好请求头就行
        String head = request.getHeader("Authorization");
        String token = head.substring(head.indexOf("bearer") + 7);
        return Jwts.parser()
                .setSigningKey("test_key".getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
//        return authentication.getPrincipal();
    }
}
