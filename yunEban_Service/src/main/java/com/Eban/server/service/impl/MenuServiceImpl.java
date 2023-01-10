package com.Eban.server.service.impl;

import com.Eban.server.mapper.MenuMapper;
import com.Eban.server.pojo.Admin;
import com.Eban.server.pojo.Menu;
import com.Eban.server.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author LBY
 * @since 2022-08-13
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<Menu> getMenusByAdminId() {
//        通过登录后的Security上下文全局用户信息来获其取id
//        return menuMapper.getMenusByAdminId(((Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
        Integer adminId = ((Admin) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal()).getId();
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

//        从redis获取当前用户对应菜单
        List<Menu> menus = (List<Menu>) valueOperations.get("menu_" + adminId);
        System.out.println(redisTemplate.keys("*"));
//        如果为空，那就从数据库获取，并存入redis
        if (CollectionUtils.isEmpty(menus)) {
            menus = menuMapper.getMenusByAdminId(adminId);
            valueOperations.set("menu_" + adminId, menus);
        }
        return menus;
    }

    @Override
    public List<Menu> getRolesWithEachMenu() {
        return menuMapper.getRolesWithEachMenu();
    }

    @Override
    public List<Menu> getAllMenus() {
        return menuMapper.getAllMenus();
    }
}
