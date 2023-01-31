package com.Eban.server.service;

import com.Eban.server.pojo.Employee;
import com.Eban.server.request.DateScope;
import com.Eban.server.response.RespBean;
import com.Eban.server.response.RespPageBean;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LBY
 * @since 2022-08-13
 */
public interface IEmployeeService extends IService<Employee> {

    RespPageBean getAllEmployee(Integer currentPage, Integer size, Employee employee, LocalDate[] dateScope);

    RespBean maxWorkId();

    RespBean addEmp(Employee employee);

    RespBean updateEmp(Employee employee);

    List<Employee> getEmployee(Integer id);
}
