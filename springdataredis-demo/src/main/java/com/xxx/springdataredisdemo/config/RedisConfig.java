package com.xxx.springdataredisdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @description: redis序列化配置类
 * @author: Libaoyun
 * @date: 2023-01-19 17:56
 **/

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            LettuceConnectionFactory lettuceConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 为String类型的key设置序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 为String类型的value设置序列化
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // 为Hash类型的key设置序列化
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        // 为Hash类型的value设置序列化
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        return redisTemplate;


    }
}
