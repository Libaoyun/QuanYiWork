package com.Eban.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: yunEban_Service
 * @description: 测试Swagger
 * @packagename: com.Eban.server.controller
 * @author: Libaoyun
 * @date: 2022-08-16 13:26
 **/

@Api(value = "/hello", tags = "测试hello")
@RestController
public class HelloController {

    @ApiOperation("hello")
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @ApiOperation("基本资料")
    @GetMapping("/employee/basic/hello")
    public String hello2(){
        return "/employee/basic/hello";
    }

    @ApiOperation("高级资料")
    @GetMapping("/employee/advanced/hello")
    public String hello3(){
        return "/employee/advanced/hello";
    }
}
