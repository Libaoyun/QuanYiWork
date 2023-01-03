package com.Eban.server.config.security;

import com.Eban.server.config.security.component.*;
import com.Eban.server.pojo.Admin;
import com.Eban.server.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @program: yunEban_Service
 * @description: Security配置类
 * @packagename: com.Eban.server.config.security
 * @author: Libaoyun
 * @date: 2022-08-15 16:43
 **/


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private IAdminService adminService;

    @Autowired
    private RestAuthorizationEntryPoint restAuthorizationEntryPoint;

    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    @Autowired
    private CustomFilter customFilter;

    @Autowired
    private CustomUrlDecisionManager customUrlDecisionManager;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
//        下面配置将SpirngSecurity到时候走重写过得UserDetailService，里面有getAdminByUsername
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

//    下面要放行swagger相关的资源，否则也访问不到
    @Override
    public void configure(WebSecurity web) throws Exception{
        web.ignoring().antMatchers(
                "/login",
                "/logout",
                "/css/**",
                "/js/**",
                "/index.html",
                "favicon.ico",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources/**",
                "/v2/api-docs/**",
                "/captcha"
        );
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception{
//        使用JWT不需要CSRF
        http.csrf().disable()
//                基于token，因此不用session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
//                .antMatchers("/login", "/
//                logout")
//                .permitAll()
//                除了上面，所有请求都要认证
                .anyRequest()
                .authenticated()
//                这个是动态权限配置，用于将根据路径查找菜单所需角色并判断当前角色是否可以访问
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setAccessDecisionManager(customUrlDecisionManager);
                        object.setSecurityMetadataSource(customFilter);
                        return object;
                    }
                })
                .and()
//                禁用缓存
                .headers()
                .cacheControl();
//        添加jwt授权过滤器
        http.addFilterBefore(jwtAuthencationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//        添加自定义未授权和未登录结果返回
        http.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthorizationEntryPoint);

    }


//    这里要重写是因为原先默认是通过内存来获取用户比对的，事实是通过数据库，所以重写修改
    @Override
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> {
//            其实也就是重写了LoadUserByUsername
            Admin admin = adminService.getAdminByUsername(username);
            if (null != admin){
                admin.setRoles(adminService.getRolesByAdminId(admin.getId()));
                return admin;
            }
//            该异常类是UserDetailsService自带的
            throw new UsernameNotFoundException("用户名或密码不正确！");
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public JwtAuthencationTokenFilter jwtAuthencationTokenFilter(){
        return new JwtAuthencationTokenFilter();
    }
}
