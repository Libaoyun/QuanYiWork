package com.Eban.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @program: yunEban_Service
 * @description: Redis配置类（序列化）
 * @packagename: com.Eban.server.config
 * @author: Libaoyun
 * @date: 2022-08-22 14:59
 **/
@Configuration
public class RedisConfig {

//    要对RedisTemplate序列化，否则返回缓存中存的是二进制，除非使用的是StringRedisTemplate，可以自动转为String
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        //String类型key序列器
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //String类型value序列器
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        //Hash类型key序列器
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //Hash类型value序列器
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }


}
