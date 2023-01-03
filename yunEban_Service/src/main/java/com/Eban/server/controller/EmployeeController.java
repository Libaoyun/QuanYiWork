package com.Eban.server.controller;


import com.Eban.server.pojo.Employee;
import com.Eban.server.response.RespPageBean;
import com.Eban.server.service.IEmployeeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LBY
 * @since 2022-08-13
 */
@RestController
@RequestMapping("/employee/basic")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;

    @ApiOperation("分页获取所有员工")
    @GetMapping("/")
    public RespPageBean getAllEmployee(@RequestParam(defaultValue = "1") Integer currentPage,
                                       @RequestParam(defaultValue = "10") Integer size,
                                       Employee employee,
                                       LocalDate[] beginDateScope){
        return employeeService.getAllEmployee(currentPage, size, employee, beginDateScope);
    }
}
