package com.Eban.server.config.security.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: yunEban_Service
 * @description: JWT登录授权过滤器
 * @packagename: com.Eban.server.config.security
 * @author: Libaoyun
 * @date: 2022-08-15 17:04
 **/
//q前置拦截
@PropertySource("classpath:application.yml")
public class JwtAuthencationTokenFilter extends OncePerRequestFilter {

    @Value("${tokenHeader:Authorization}")
    private String tokenHeader;

    @Value("${tokenHead:Bearer}")
    private String tokenHead;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(tokenHeader);
//        判断token是否存在，以bearer开头
        if (null != authHeader && authHeader.startsWith(tokenHead)){
            String authToken = authHeader.substring(tokenHead.length());
            String username = jwtTokenUtil.getUsernameFromToken(authToken);
//            token存在用户名但未登录(未授权）
            if (null != username && null == SecurityContextHolder.getContext().getAuthentication()){
//                登录
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                验证token是否有效
                if (jwtTokenUtil.validateToken(authToken,userDetails)){
//                    重新注入用户对象，放入全局Security上下文，否则下次过滤器登录时还是要此操作，拿不到用户信息
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }

            }
        }
//        放行
        filterChain.doFilter(request,response);

    }
}
