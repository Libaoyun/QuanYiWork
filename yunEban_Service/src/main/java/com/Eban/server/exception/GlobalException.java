package com.Eban.server.exception;

import com.Eban.server.response.RespBean;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @program: yunEban_Service
 * @description: 全局异常处理
 * @packagename: com.Eban.server.exception
 * @author: Libaoyun
 * @date: 2022-08-24 13:04
 **/

//该注解就相当于@ResponseBody + @ControllerAdvice,返回JSON数据
//@RestControllerAdvice
public class GlobalException {

    //处理数据库外键等原因抛出异常，例如某些数据被其他数据关联（外键），那删除操作就会失败，也就会报异常
    @ExceptionHandler(SQLException.class)
    public RespBean mySqlException(SQLException e){
        if (e instanceof SQLIntegrityConstraintViolationException){
            return RespBean.error("该数据有关联数据，操作失败！");
        }
        return RespBean.error("数据库异常，操作失败！");
    }
}
