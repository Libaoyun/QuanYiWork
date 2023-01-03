package com.Eban.server.controller;


import com.Eban.server.pojo.Department;
import com.Eban.server.response.RespBean;
import com.Eban.server.service.IDepartmentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LBY
 * @since 2022-08-13
 */
@RestController
@RequestMapping("/system/basic/department")
public class DepartmentController {

    @Autowired
    private IDepartmentService departmentService;

    @ApiOperation("获取所有部门")
    @GetMapping("/")
    public List<Department> getAllDepartments(){
        return departmentService.getAllDepartments();
    }

    @ApiOperation("添加部门")
    @PostMapping("/")
    public RespBean addDepartment(@RequestBody Department department){
        return departmentService.addDepartment(department);
    }

    @ApiOperation("删除部门")
    @DeleteMapping("/{id}")
    public RespBean deleteDepartmentById(@PathVariable Integer id){
        return departmentService.deleteDepartmentById(id);
    }

}
