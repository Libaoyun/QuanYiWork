package com.Eban.server.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * @description: 起止日期参数
 * @author: Libaoyun
 * @date: 2023-01-29 16:07
 **/

@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel(value = "起止日期参数类", description = "")
public class DateScope {

    @ApiModelProperty(value = "起始日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate startSearchDate;

    @ApiModelProperty(value = "结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate endSearchDate;
}
