package com.example.redis.demo;

import com.example.redis.demo.dao.CityDao;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisDemoApplicationTests {

	@Autowired
	private CityDao cityDao;

	@Test
	public void contextLoads() {
	}

	@Autowired
	private RedisTemplate redisTemplate;

	@Test
	public void saveString(){
		redisTemplate.opsForValue().set("aab", "111");
		Assert.assertEquals("111",redisTemplate.opsForValue().get("aaa"));
		System.out.println(redisTemplate.opsForValue().get("aaa"));
	}

	@Test
	public void findAlltest(){
		System.out.println(cityDao.findAllCity());
	}

	@Test
	public void findAlltest1(){

	}
}
