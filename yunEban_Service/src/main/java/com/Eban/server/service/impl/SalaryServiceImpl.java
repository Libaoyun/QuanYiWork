package com.Eban.server.service.impl;

import com.Eban.server.mapper.EmployeeMapper;
import com.Eban.server.mapper.SalaryMapper;
import com.Eban.server.pojo.Employee;
import com.Eban.server.pojo.Salary;
import com.Eban.server.response.RespPageBean;
import com.Eban.server.service.ISalaryService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LBY
 * @since 2022-08-13
 */
@Service
public class SalaryServiceImpl extends ServiceImpl<SalaryMapper, Salary> implements ISalaryService {

    @Autowired
    private SalaryMapper salaryMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    public RespPageBean getAllEmployeeWithSalary(Integer currentPage, Integer size) {
        Page page = new Page<>(currentPage, size);
        IPage<Employee> employeeIPage = employeeMapper.getAllEmployeeWithSalary(page);
        return new RespPageBean(employeeIPage.getTotal(), employeeIPage.getRecords());
    }
}
