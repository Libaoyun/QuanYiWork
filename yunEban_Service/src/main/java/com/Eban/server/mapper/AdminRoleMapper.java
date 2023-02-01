package com.Eban.server.mapper;

import com.Eban.server.pojo.AdminRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LBY
 * @since 2022-08-13
 */
@Repository

public interface AdminRoleMapper extends BaseMapper<AdminRole> {

    Integer updateAdminRoles(Integer adminId, Integer[] rids);
}