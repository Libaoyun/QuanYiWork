package com.Eban.server.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @description: 日期转换
 * @author: Libaoyun
 * @date: 2022-09-16 15:51
 **/
public class DateConverter implements Converter<String, LocalDate> {

//    这里是为了将前端传过来的字符串形式日期改为数据库对应的形式，而pojo中加的jsonformat是传给前端时转为固定格式字符串。
    @Override
    public LocalDate convert(String source) {
//        Idea抛异常快捷键：alt + enter + T
        try {
            return LocalDate.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
