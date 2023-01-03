package com.Eban.server.controller;

import com.Eban.server.pojo.Admin;
import com.Eban.server.request.AdminLoginRequest;
import com.Eban.server.response.RespBean;
import com.Eban.server.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * @program: yunEban_Service
 * @description: 登录
 * @packagename: com.Eban.server.controller
 * @author: Libaoyun
 * @date: 2022-08-15 14:44
 **/
@RestController
@Api(tags = "LoginController")
public class LoginController {

    @Autowired
    private IAdminService adminService;

    @ApiOperation("登陆之后返回Token")
    @PostMapping("/login")
    public RespBean login(@RequestBody AdminLoginRequest adminLoginRequest, HttpServletRequest httpServletRequest){
        return adminService.login(adminLoginRequest.getUsername(), adminLoginRequest.getPassword(), adminLoginRequest.getCode(), httpServletRequest);
    }

    @ApiOperation("获取当前用户对象信息")
    @GetMapping("/admin/info")
    public Admin getAdminInfo(Principal principal){
//        上面的principle就是Service中全局Security对象，可以直接获取信息
        if (null == principal){
            return null;
        }
//        这个getName就是直接获取username
        String username = principal.getName();
        Admin admin = adminService.getAdminByUsername(username);
//        安全起见，密码不对外展示
        admin.setPassword(null);
        admin.setRoles(adminService.getRolesByAdminId(admin.getId()));
        return admin;
    }



    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public RespBean logout(){
        return RespBean.success("注销成功！");
//        这里不做过多处理，只需前端获取到200响应码后直接在前端清除token，下次登录就会被拦截。
    }
}
