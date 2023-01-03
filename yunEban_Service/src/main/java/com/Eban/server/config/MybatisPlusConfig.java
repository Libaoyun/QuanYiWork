package com.Eban.server.config;

import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: MP配置
 * @author: Libaoyun
 * @date: 2022-09-16 15:47
 **/

@Configuration
public class MybatisPlusConfig {

//    这里使用MP自带继承的分页工具，也可以手动使用PageHelper或其他，但没必要
    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor(){
        return new PaginationInnerInterceptor();
    }
}
