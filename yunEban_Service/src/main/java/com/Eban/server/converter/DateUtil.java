package com.Eban.server.converter;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @description: 日期工具类
 * @author: Libaoyun
 * @date: 2023-01-29 16:30
 **/

@Component
public class DateUtil {
    public static LocalDate[] dateConvert(String[] sources) throws Exception {
        if (sources == null || sources.length == 0){
            return new LocalDate[0];
        }
        LocalDate[] localDates = new LocalDate[sources.length];
        for (int i = 0 ; i<sources.length; i++) {
            if (sources[i].matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")){
                try {
                    localDates[i] = LocalDate.parse(sources[i], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                throw new Exception("日期参数" + sources[i] + "不正确!");
            }
        }
        return localDates;
    }

/*    public  LocalDate parseDate(String dateStr, String format) {
        Date date=null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            date = dateFormat.parse(dateStr);
        } catch (Exception e) {

        }
        return date;
    }*/

}
