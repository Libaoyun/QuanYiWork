package com.Eban.server.service.impl;

import com.Eban.server.mapper.EmployeeMapper;
import com.Eban.server.pojo.Employee;
import com.Eban.server.response.RespPageBean;
import com.Eban.server.service.IEmployeeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LBY
 * @since 2022-08-13
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public RespPageBean getAllEmployee(Integer currentPage, Integer size, Employee employee, LocalDate[] beginDateScope) {
//        开启分页
        Page<Employee> page = new Page<>(currentPage,size);
        IPage<Employee> allEmployeeByPage = employeeMapper.getAllEmployeeByPage(page, employee, beginDateScope);
        return new RespPageBean(allEmployeeByPage.getTotal(), allEmployeeByPage.getRecords());

    }
}
