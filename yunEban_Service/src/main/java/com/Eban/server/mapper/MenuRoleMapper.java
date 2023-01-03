package com.Eban.server.mapper;

//import com.Eban.generator.pojo.MenuRole;
import com.Eban.server.pojo.MenuRole;
import com.Eban.server.response.RespBean;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
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

public interface MenuRoleMapper extends BaseMapper<MenuRole> {

    Integer insertRoleMenu(@Param("rid") Integer rid, @Param("mids") Integer[] mids);
}
