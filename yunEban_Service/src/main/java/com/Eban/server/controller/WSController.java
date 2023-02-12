package com.Eban.server.controller;

import com.Eban.server.pojo.Admin;
import com.Eban.server.pojo.ChatMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @description: WS控制类
 * @author: Libaoyun
 * @date: 2023-02-08 15:27
 **/
@Controller
public class WSController {
    @Autowired
    private SimpMessagingTemplate simpleMessagingTemplate;
    @MessageMapping("/ws/chat")
    public void handelMsg(Authentication authentication, ChatMsg chatMsg){
        Admin admin = (Admin) authentication.getPrincipal();
        chatMsg.setFrom(admin.getUsername());
        chatMsg.setFromNickName(admin.getName());
        chatMsg.setDate(LocalDateTime.now());
        // 相当于把消息中转到了/queue/chat频道上，必须要订阅该频道才会关联消息
        simpleMessagingTemplate.convertAndSendToUser(
                chatMsg.getTo(), "/queue/chat", chatMsg);
    }
}
