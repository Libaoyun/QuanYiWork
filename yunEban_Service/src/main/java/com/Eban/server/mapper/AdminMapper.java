package com.Eban.server.mapper;

import com.Eban.server.pojo.Admin;
import com.Eban.server.pojo.Menu;
import com.Eban.server.pojo.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LBY
 * @since 2022-08-13
 */
@Repository
public interface AdminMapper extends BaseMapper<Admin> {

    List<Admin> getAllAdmins(Integer id, String keywords);
}
