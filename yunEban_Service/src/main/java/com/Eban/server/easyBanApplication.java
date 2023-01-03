package com.Eban.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@MapperScan("com.Eban.server.mapper")
public class easyBanApplication {
    public static void main(String[] args) {
        SpringApplication.run(easyBanApplication.class);
        System.out.println("\n系统启动成功!!!");
    }
    // 设置@Value注解取值不到忽略（不报错）
//    @Bean
//    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
//        PropertySourcesPlaceholderConfigurer c = new PropertySourcesPlaceholderConfigurer();
//        c.setIgnoreUnresolvablePlaceholders(true);
//        return c;
//    }
}
