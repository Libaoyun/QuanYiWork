package com.Eban.server.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @program: yunEban_Service
 * @description: 用户登录参数实体类
 * @packagename: com.Eban.server.request
 * @author: Libaoyun
 * @date: 2022-08-15 14:41
 **/
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel(value = "AdminLogin参数对象", description = "")
public class AdminLoginRequest {

    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    @ApiModelProperty(value = "密码", required = true)
    private String password;

    @ApiModelProperty(value = "验证码", required = true)
    private String code;

}
