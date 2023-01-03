package com.Eban.server.service;

import com.Eban.server.pojo.Department;
import com.Eban.server.response.RespBean;
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
public interface IDepartmentService extends IService<Department> {

    List<Department> getAllDepartments();

    RespBean addDepartment(Department department);

    RespBean deleteDepartmentById(Integer id);
}
