package com.Eban.server.service.impl;

import com.Eban.server.mapper.MenuRoleMapper;
import com.Eban.server.pojo.MenuRole;
import com.Eban.server.response.RespBean;
import com.Eban.server.service.IMenuRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LBY
 * @since 2022-08-13
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {

    @Autowired
    private MenuRoleMapper menuRoleMapper;

    @Override
    @Transactional
    public RespBean updateRoleMenu(Integer rid, Integer[] mids) {
        // 需要考虑到，如果有增也有删的操作，会特别麻烦，需要判断每一个是否更改，然后又要分别增删，
        // 这里简便方法就是，先全部删了，然后将传过来的mids全部添加即可，因此需要用到@Trans事务注解
        menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq("rid", rid));
        // 考虑如果本身就是全部删除了，那就直接返回
        if (null == mids || 0 == mids.length){
            return RespBean.success("更新成功!");
        }
        return menuRoleMapper.insertRoleMenu(rid, mids) == mids.length ?
                RespBean.success("更新成功!") : RespBean.error("更新失败");
    }
}
