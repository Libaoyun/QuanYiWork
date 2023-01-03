package com.Eban.server.controller;

import com.Eban.server.pojo.Menu;
import com.Eban.server.pojo.MenuRole;
import com.Eban.server.pojo.Role;
import com.Eban.server.response.RespBean;
import com.Eban.server.service.IMenuRoleService;
import com.Eban.server.service.IMenuService;
import com.Eban.server.service.IRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: yunEban_Service
 * @description: 权限组相关
 * @packagename: com.Eban.server.controller
 * @author: Libaoyun
 * @date: 2022-08-27 12:21
 **/

@RestController
@RequestMapping("/system/basic/per")
public class PermissionController {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IMenuRoleService menuRoleService;

    @GetMapping("/role")
    @ApiOperation("获取所有角色")
    public List<Role> getAllRoles(){
        return roleService.list();
    }

    @PostMapping("/role")
    @ApiOperation("添加角色信息")
    public RespBean addRole(@RequestBody Role role){
        //应Security要求，如果不以ROLE_开头的角色，需要加上前缀，且与数据库一致
        if (!role.getName().startsWith("ROLE_")){
            role.setName("ROLE_" + role.getName());
        }
        if (roleService.save(role)){
            return RespBean.success("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @DeleteMapping("/role/{rid}")
    @ApiOperation("删除角色信息")
    public RespBean deleteRole(@PathVariable Integer rid){
        if (roleService.removeById(rid)){
            return RespBean.success("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    @GetMapping("/menu")
    @ApiOperation("获取所有菜单信息（三级嵌套）")
    public List<Menu> getAllMenus(){
        return menuService.getAllMenus();
    }

    @GetMapping("/menu/{rid}")
    @ApiOperation("根据角色id查询菜单列表")
    public List<Integer> getMenusByRid(@PathVariable Integer rid){
//        注意这里只是获取菜单id，因此将查询到的menuRole对象获取流然后取出其中mid转为list列表
        return menuRoleService.list(new QueryWrapper<MenuRole>().eq("rid", rid)).stream().map
                (MenuRole::getMid).collect(Collectors.toList());
    }

    @PutMapping("/role_menu/{rid}")
    @ApiOperation("更新角色对应菜单")
    public RespBean updateRoleMenu(@PathVariable Integer rid, Integer[] mids){
        return menuRoleService.updateRoleMenu(rid, mids);
    }
}
