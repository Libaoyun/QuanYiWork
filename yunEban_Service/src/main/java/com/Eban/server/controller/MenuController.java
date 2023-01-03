package com.Eban.server.controller;


import com.Eban.server.pojo.Menu;
import com.Eban.server.service.IAdminService;
import com.Eban.server.service.IMenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/system/cfg")  //这里不能直接menu，因为不同菜单的路径有些会被拦截，这里菜单泛指系统管理菜单
public class MenuController {

    @Autowired
    private IMenuService menuService;

    @ApiOperation(value = "通过用户ID查询菜单列表")
    @GetMapping("/menu")
    public List<Menu> getMenusByAdminId(
//            这里需要注意，根据id查询但是前段没有传递id，是因为后端可以直接用过Security登录后获取上下文全局对象得到用户信息，如果前端传递可能会被篡改
    ){
        return menuService.getMenusByAdminId();
    }
}
