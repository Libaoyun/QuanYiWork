package com.xxx.springdataredisdemo.entity;

import lombok.Data;

/**
 * @description:
 * @author: Libaoyun
 * @date: 2023-01-19 18:04
 **/

@Data
public class User {

    private String id;
    private String name;
    private String age;

    public User(String id, String name, String age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
