package com.Eban.server.config.security.component;

import com.Eban.server.pojo.Menu;
import com.Eban.server.pojo.Role;
import com.Eban.server.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

/**
 * 权限控制
 * @program: yunEban_Service
 * @description: 根据用户url分析请求所需角色
 * @packagename: com.Eban.server.config.security.component
 * @author: Libaoyun
 * @date: 2022-08-22 17:17
 **/
@Component
//该步骤主要是用于用户直接输入url以后，判断是否有该菜单的访问权限，如果有就放行，反之过滤，如果未登录则去登陆
//    根据菜单中的url与路径进行匹配，相同则去mapper查找所需用户角色，并与当前角色比对。
public class CustomFilter implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private IMenuService menuService;

//    用于路径匹配
    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //获取请求url
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        List<Menu> menus = menuService.getRolesWithEachMenu();
        for (Menu menu : menus){
            if (antPathMatcher.match(menu.getUrl(), requestUrl)){
//                如果遍历中与某一个menu的url匹配上，则获取该菜单的所有对应角色
                String[] str = menu.getRoles().stream().map(Role::getName).toArray(String[]::new);
                return SecurityConfig.createList(str);
            }
        }
        //            如果没匹配上，那就默认登录即可访问，role_login这个角色就是登录后就能访问
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }
}
