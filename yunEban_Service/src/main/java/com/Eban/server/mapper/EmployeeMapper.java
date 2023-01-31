package com.Eban.server.mapper;

//import com.Eban.generator.pojo.Employee;

import com.Eban.server.pojo.Employee;
import com.Eban.server.request.DateScope;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author LBY
 * @since 2022-08-13
 */
@Repository

public interface EmployeeMapper extends BaseMapper<Employee> {

    //    分页获取所有员工，这里分页是属于AOP自动完成的，因此SQL语句中不用涉及分页，正常查询即可，因此也不需要Param注解
    IPage<Employee> getAllEmployeeByPage(Page<Employee> page,
                                         @Param("employee") Employee employee,
                                         @Param("dateScope") LocalDate[] dateScope);

    List<Employee> getEmployee(Integer id);
}
