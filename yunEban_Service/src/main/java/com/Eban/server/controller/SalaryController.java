package com.Eban.server.controller;


import com.Eban.server.pojo.Salary;
import com.Eban.server.response.RespBean;
import com.Eban.server.service.impl.SalaryServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  工资账套管理
 * </p>
 *
 * @author LBY
 * @since 2022-08-13
 */
@RestController
@RequestMapping("/sal/sob")
public class SalaryController {

    @Autowired
    private SalaryServiceImpl salaryService;

    // 账套模块
    @ApiOperation("查询所有账套")
    @GetMapping("/")
    public List<Salary> getAllSalary(){
        return salaryService.list();
    }

    @ApiOperation("添加账套")
    @PostMapping("/")
    public RespBean addSalary(@RequestBody Salary salary){
        salary.setCreateDate(LocalDateTime.now());
        if (salaryService.save(salary)){
            return RespBean.success("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @ApiOperation("更新账套")
    @PostMapping("/update")
    public RespBean updateSalary(@RequestBody Salary salary){
        if (salaryService.updateById(salary)){
            return RespBean.success("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @ApiOperation("删除账套")
    @DeleteMapping("/{id}")
    public RespBean getAllSalary(@PathVariable Integer id){
        if (salaryService.removeById(id)){
            return RespBean.success("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

}
