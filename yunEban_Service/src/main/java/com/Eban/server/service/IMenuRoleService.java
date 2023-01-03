package com.Eban.server.service;

import com.Eban.server.pojo.MenuRole;
import com.Eban.server.response.RespBean;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LBY
 * @since 2022-08-13
 */
public interface IMenuRoleService extends IService<MenuRole> {

    RespBean updateRoleMenu(Integer rid, Integer[] mids);
}
