package com.Eban.server.mapper;

//import com.Eban.generator.pojo.Department;
import com.Eban.server.pojo.Department;
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

public interface DepartmentMapper extends BaseMapper<Department> {

    List<Department> getAllDepartments(Integer parentId);

    void addDepartment(Department department);

    void deleteDepartmentById(Department dep);
}
