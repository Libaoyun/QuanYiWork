package com.Eban.server.config;

import com.Eban.server.config.security.component.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @description: WebSocket配置类
 * @author: Libaoyun
 * @date: 2023-02-08 14:39
 **/
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;


    /**
     * 添加这个Endpoint，这样在网页可以通过websocket连接上服务
     * 也就是我们配置websocket的服务地址，并且可以指定是否使用socketJS
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        /**
         * 1.将路径ws/ep注册为stomp的端点，用户连接了这个端点就可以进行websocket通讯，支持socketJs
         * 2.setAllowed0rigins( "*"):允许跨域
         * 3.withSockJS():支持socketJS访问
         */
        System.out.println("\naaaa\n");
        registry.addEndpoint("/ws/ep").setAllowedOrigins("*");
    }

    /**
     * 输入通道参数配置，因为使用到了JWT，会被拦截，因此要预处理，防止被JWT过滤器拦截
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                // 判断是否为连接，如果是则需获取token直接预登陆，设置用户对象，否则直接返回即可
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String token = accessor.getFirstNativeHeader("Auth-Token");
                    if (!StringUtils.isEmpty(token)) {
                        String authToken = token.substring(tokenHead.length());
                        String username = jwtTokenUtil.getUsernameFromToken(authToken);
                        // 如果token中存在用户名,则登录，放入全局对象
                        if (!StringUtils.isEmpty(username)) {
                            // 登录
                            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                            // 验证token是否有效，有效则重新设置用户对象
                            if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                                UsernamePasswordAuthenticationToken authenticationToken =
                                        new UsernamePasswordAuthenticationToken(userDetails,
                                                null, userDetails.getAuthorities());
                                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                                accessor.setUser(authenticationToken);
                            }
                        }
                    }
                }
                return message;
            }
        });
    }

    // 配置消息代理
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //配置代理域，可以配置多个，自定义代理目的地前缀为/queue,可以在配置域上向客户端推送消息
        registry.enableSimpleBroker("/queue");
        /**
         * 第一个方法配置就是让客户端连接此服务端，但服务端主动给客户端推消息得有个地方接收，
         * 第三个方法就是配置推给客户端地址的的前缀，从而接收
         */

    }
}
