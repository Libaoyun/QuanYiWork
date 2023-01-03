package com.Eban.server.service;

import com.Eban.server.pojo.Employee;
import com.Eban.server.response.RespPageBean;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LBY
 * @since 2022-08-13
 */
public interface IEmployeeService extends IService<Employee> {

    RespPageBean getAllEmployee(Integer currentPage, Integer size, Employee employee, LocalDate[] beginDateScope);
}
