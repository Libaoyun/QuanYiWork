package com.xxx.springdataredisdemo;

import com.xxx.springdataredisdemo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
class SpringdataredisDemoApplicationTests {
	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private RedisTemplate<String, String> stringStringRedisTemplate;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Test
	public void testRedis1(){
		ValueOperations ops = redisTemplate.opsForValue();
		ops.set("name", "zhangsan");
		System.out.println(ops.get("name"));
	}

	@Test
	public void testRedis2(){
		ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
		ops.set("name", "zhangsan");
		System.out.println(ops.get("name"));
	}

	@Test
	public void testRedis3(){
		ValueOperations<String, String> ops = stringStringRedisTemplate.opsForValue();
		ops.set("name", "zhangsan");
		System.out.println(ops.get("name"));
	}

	@Test
	public void testRedis4(){
		ValueOperations ops = redisTemplate.opsForValue();
		User user = new User("1", "zhangsan", "20");
		System.out.println(user.getAge());
		ops.set("user", user);
		Object user1 = ops.get("user");
		System.out.println(user1);
	}


}
