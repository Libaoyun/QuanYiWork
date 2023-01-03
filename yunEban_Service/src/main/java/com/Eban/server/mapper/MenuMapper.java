package com.Eban.server.mapper;

//import com.Eban.generator.pojo.Menu;
import com.Eban.server.pojo.Menu;
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
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> getMenusByAdminId(Integer id);

    List<Menu> getRolesWithEachMenu();

    List<Menu> getAllMenus();
}
