package com.Eban.server.controller;


import com.Eban.server.pojo.Admin;
import com.Eban.server.pojo.Role;
import com.Eban.server.response.RespBean;
import com.Eban.server.service.IAdminService;
import com.Eban.server.service.IRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LBY
 * @since 2022-08-13
 */
@RestController
@RequestMapping("/system/admin")
public class AdminController {

    @Autowired
    private IAdminService adminService;

    @Autowired
    private IRoleService roleService;

    @ApiOperation("获取所有操作员")
    @GetMapping("/")
    public List<Admin> getAllAdmins(String keywords){
        return adminService.getAllAdmins(keywords);
    }

    @ApiOperation("更新操作员")
    @PutMapping("/")
    public RespBean updateAdmin(@RequestBody Admin admin){
        if (adminService.updateById(admin)){
            return RespBean.success("更新成功!");
        }
        return RespBean.error("更新失败!");
    }
    @ApiOperation("删除操作员")
    @DeleteMapping("/{id}")
    public RespBean deleteAdmin(@PathVariable Integer id){
        if (adminService.removeById(id)){
            return RespBean.success("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    @ApiOperation("获取所有角色")
    @GetMapping("/roles")
    public List<Role> getAllRoles(){
        return roleService.list();
    }

    @ApiOperation("修改管理员角色")
    @PutMapping("/role/{adminId}")
    public RespBean updateAdminRoles(@PathVariable Integer adminId, @RequestBody Integer[] rids){
        return adminService.updateAdminRoles(adminId, rids);
    }
}
