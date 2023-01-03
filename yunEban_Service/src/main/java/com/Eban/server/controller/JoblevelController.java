package com.Eban.server.controller;


import com.Eban.server.pojo.Joblevel;
import com.Eban.server.response.RespBean;
import com.Eban.server.service.IJoblevelService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
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
@RequestMapping("/system/basic/joblevel")
public class JoblevelController {

    @Autowired
    private IJoblevelService jobLevelService;

    @GetMapping("/")
    @ApiOperation(value = "获取所有职称信息")
    public List<Joblevel> getAllJobLevels(){
//        MP的优点体现出来，无需对service做任何操作便可以简单增删改查！！！！
        return jobLevelService.list();
    }

    @PostMapping("/")
    @ApiOperation(value = "添加职称信息")
    public RespBean addJobLevel(@RequestBody Joblevel joblevel){
//        日期在后端设置即可
        joblevel.setCreateDate(LocalDateTime.now());
        if (jobLevelService.save(joblevel)){
            return RespBean.success("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @PutMapping("/")
    @ApiOperation(value = "修改职称信息")
    public RespBean updateJobLevel(@RequestBody Joblevel joblevel){
        if (jobLevelService.updateById(joblevel)){
            return RespBean.success("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除职称信息")
    public RespBean deleteJobLevel(@PathVariable Integer id){
        if (jobLevelService.removeById(id)){
            return RespBean.success("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    @DeleteMapping("/")
    @ApiOperation(value = "批量删除职称信息")
    public RespBean deleteJobLevelByIds(Integer[] ids){       //ids也可以是String，通过逗号分隔，并拆分取出来
        if (jobLevelService.removeByIds(Arrays.asList(ids))){
            return RespBean.success("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

}
