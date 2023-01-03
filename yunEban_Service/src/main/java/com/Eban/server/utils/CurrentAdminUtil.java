package com.Eban.server.utils;

import com.Eban.server.pojo.Admin;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @program: yunEban_Service
 * @description: 获取当前用户信息
 * @packagename: com.Eban.server.utils
 * @author: Libaoyun
 * @date: 2022-09-02 14:03
 **/
public class CurrentAdminUtil {

    public static Admin getCurrentAdmin(){
        return (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
