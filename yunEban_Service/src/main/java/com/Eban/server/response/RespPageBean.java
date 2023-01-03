package com.Eban.server.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: 分页查询公共返回对象
 * @author: Libaoyun
 * @date: 2022-09-16 15:49
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespPageBean {

    // 总条数
    private Long total;

    //数据List
    private List<?> data;
}
