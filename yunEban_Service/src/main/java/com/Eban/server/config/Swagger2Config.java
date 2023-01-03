package com.Eban.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: yunEban_Service
 * @description: Swagger2配置类
 * @packagename: com.Eban.server.config.security
 * @author: Libaoyun
 * @date: 2022-08-16 13:17
 **/
@Configuration
@EnableSwagger2
public class Swagger2Config extends WebMvcConfigurationSupport {

    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.Eban.server.controller"))
                .paths(PathSelectors.any())
                .build()
                //配置全局登录
                .securityContexts(securityContexts())
                .securitySchemes(securitySchemes());
    }

//    生成文档的简介
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("云E办接口文档")
                .description("云E办接口文档")
                .contact(new Contact("xxxx", "http:localhost:8081/doc.html", "xxxx@xxxx.com"))
                .version("1.0")
                .build();
    }


//    下面是用来配置访问swagger文档时需要登录的问题，也使用token
    private List<ApiKey> securitySchemes() {
//        设置请求头信息
        List<ApiKey> apiKeys = new ArrayList<>();
        apiKeys.add(new ApiKey("Authorization", "Authorization", "header"));
        return apiKeys;
    }
    private List<SecurityContext> securityContexts() {
        //设置需要登录认证的路径
        List<SecurityContext> list = new ArrayList<>();
        list.add(getContextByPath("/hello/.*"));
        return list;
    }

    private SecurityContext getContextByPath(String pathRegex) {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex(pathRegex))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        List<SecurityReference> list = new ArrayList<>();
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        list.add(new SecurityReference("Authorization", authorizationScopes));
        return list;
    }




//    可能会导致No Get for Mapping doc.html，两种解决方法：降低SB版本至2.3.x


//   或者 在配置类中继承WebMvcConfigurationSupport类，重写addResourceHandlers方法
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}
