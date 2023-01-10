package com.example.springsecurityoauth2demo.service;

import com.example.springsecurityoauth2demo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.xml.ws.Action;

/**
 * @description: 用户类
 * @author: Libaoyun
 * @date: 2023-01-04 16:22
 **/
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String password = passwordEncoder.encode("123456");
//        可以返回Security自带的User，也可以我们自定义实现类后使用。
        return new User("admin", password, AuthorityUtils
                .commaSeparatedStringToAuthorityList("admin"));
    }
}
