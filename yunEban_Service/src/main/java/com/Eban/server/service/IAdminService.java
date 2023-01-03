package com.Eban.server.service;

import com.Eban.server.pojo.Admin;
import com.Eban.server.pojo.Menu;
import com.Eban.server.pojo.Role;
import com.Eban.server.response.RespBean;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LBY
 * @since 2022-08-13
 */
public interface IAdminService extends IService<Admin> {

//    登录后返回token
    RespBean login(String username, String password, String code, HttpServletRequest request);

    Admin getAdminByUsername(String username);

    List<Role> getRolesByAdminId(Integer adminId);

    List<Admin> getAllAdmins(String keywords);

    RespBean updateAdminRoles(Integer adminId, Integer[] rids);
}
