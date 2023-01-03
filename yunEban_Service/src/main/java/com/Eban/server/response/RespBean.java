package com.Eban.server.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: yunEban_Service
 * @description: 公共返回类型
 * @packagename: com.Eban.server.response
 * @author: Libaoyun
 * @date: 2022-08-15 14:12
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {

    private long code;
    private String message;
    private Object obj;


    public static RespBean success(String message ){
        return new RespBean(200, message, null);
    }

    public static RespBean success(String message, Object obj){
        return new RespBean(200, message, obj);
    }

    public static RespBean error(String message){
        return new RespBean(500, message, null);
    }

    public static RespBean error(String message, Object obj){
        return new RespBean(500, message, obj);
    }
}
