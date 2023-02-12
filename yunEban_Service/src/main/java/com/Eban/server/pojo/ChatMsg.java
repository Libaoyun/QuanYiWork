package com.Eban.server.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @description: 消息类
 * @author: Libaoyun
 * @date: 2023-02-08 15:21
 **/

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors
public class ChatMsg {

    private String from;
    private String to;
    private String content;
    private LocalDateTime date;
    private String fromNickName;

}
