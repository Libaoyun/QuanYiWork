package com.Eban.server.controller;

import com.Eban.server.pojo.Employee;
import com.Eban.server.pojo.Salary;
import com.Eban.server.response.RespBean;
import com.Eban.server.response.RespPageBean;
import com.Eban.server.service.impl.EmployeeServiceImpl;
import com.Eban.server.service.impl.SalaryServiceImpl;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 员工账套设置
 * @author: Libaoyun
 * @date: 2023-02-05 17:23
 **/
@RestController
@RequestMapping("/sal/sobcfg")
public class EmployeeSalaryController {

    @Autowired
    private SalaryServiceImpl salaryService;

    @Autowired
    private EmployeeServiceImpl employeeService;


    // 账套模块
    @ApiOperation("查询所有账套")
    @GetMapping("/")
    public List<Salary> getAllSalary(){
        return salaryService.list();
    }


    @ApiOperation("分页获取员工以及对应账套")
    @GetMapping("/salaries")
    public RespPageBean getAllEmployeeWithSalary(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer size
    ){
        return salaryService.getAllEmployeeWithSalary(currentPage, size);
    }

    @ApiOperation("更新员工账套")
    @PostMapping("/salaries")
    public RespBean updateEmployeeSalary(Integer eid, Integer sid){
        if (employeeService.update(new UpdateWrapper<Employee>()
                .set("salaryId", sid).eq("id", eid))){
            return RespBean.success("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

}
