package com.Eban.server.controller;

import com.Eban.server.pojo.Admin;
import com.Eban.server.service.IAdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description: chat聊天相关用户
 * @author: Libaoyun
 * @date: 2023-02-08 15:48
 **/
@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private IAdminService adminService;
    @ApiOperation("获取所有操作员")
    @GetMapping("/admin")
    public List<Admin> getAllAdmins(String keywords){
        return adminService.getAllAdmins(keywords);
    }
}
