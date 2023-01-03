package com.Eban.server.service;

import com.Eban.server.pojo.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LBY
 * @since 2022-08-13
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> getMenusByAdminId();

    List<Menu> getRolesWithEachMenu();

    List<Menu> getAllMenus();
}
