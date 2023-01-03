package com.Eban.server.controller;


import com.Eban.server.pojo.Position;
import com.Eban.server.response.RespBean;
import com.Eban.server.service.IPositionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import javafx.geometry.Pos;
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
//注意下面一定要改成/system/basic开头（与数据库一致），因为职位管理属于基本信息管理这一级菜单，
// 而且数据库中菜单url的作用就在这里，为了控制权限，让没有权限的用户无法操作该步骤
//这样在很多地方都可以控制操作的权限，如果没有该权限就会被驳回。
@RequestMapping("/system/basic/pos")
public class PositionController {

    @Autowired
    private IPositionService positionService;

    @GetMapping("/")
    @ApiOperation(value = "获取所有职位信息")
    public List<Position> getAllPositions(){
//        MP的优点体现出来，无需对service做任何操作便可以简单增删改查！！！！
        return positionService.list();
    }

    @PostMapping("/")
    @ApiOperation(value = "添加职位信息")
    public RespBean addPosition(@RequestBody Position position){
//        日期在后端设置即可
        position.setCreateDate(LocalDateTime.now());
        if (positionService.save(position)){
            return RespBean.success("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @PutMapping("/")
    @ApiOperation(value = "修改职位信息")
    public RespBean updatePosition(@RequestBody Position position){
        if (positionService.updateById(position)){
            return RespBean.success("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

//    使用/{}的地址传参形式，前端可以直接传原类型，且无需加上?param=data这种形式，
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除职位信息")
    public RespBean deletePosition(@PathVariable Integer id){
        if (positionService.removeById(id)){
            return RespBean.success("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    //    使用Integer[]ids的直接传参形式，默认是@RequestParam，也就是地址字符串拼接,需要加上?param=id的形式
    @DeleteMapping("/")
    @ApiOperation(value = "批量删除职位信息")
    public RespBean deleteByIds(Integer[] ids){       //ids也可以是String，通过逗号分隔，并拆分取出来
        if (positionService.removeByIds(Arrays.asList(ids))){
            return RespBean.success("删除成功!");
        }
        return RespBean.error("删除失败!");
    }


}
