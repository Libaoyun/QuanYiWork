package com.Eban.server.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @description: 日期转换
 * @author: Libaoyun
 * @date: 2022-09-16 15:51
 **/
//@Component
public class DateConverter implements Converter<String, LocalDate> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateConverter.class);

    @Override
    public LocalDate convert(String source) {
        try {
            return LocalDate.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
/*    @Override
    public LocalDate convert(String source) {
        if(!source.matches("\\d{4}-\\d{2}-\\d{2}")){
            LOGGER.error("Invalid date format for {}", source);
            throw new IllegalArgumentException("Invalid date format");
        }
        try {
            return LocalDate.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            LOGGER.error("Error parsing date {}", source, e);
            throw e;
        }
    }*/

}
