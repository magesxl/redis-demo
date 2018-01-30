package com.example.redis.demo.service.impl;

import com.example.redis.demo.dao.CityDao;
import com.example.redis.demo.domain.City;
import com.example.redis.demo.service.CityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CityServiceImpl implements CityService {

    private static final Logger logger = LoggerFactory.getLogger(CityServiceImpl.class);

    @Autowired
    private CityDao cityDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public City findCityById(Long id) {
        //缓存key
        String key = "city_" + id;
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            logger.info("从缓存中获取数据");
            return (City) redisTemplate.opsForValue().get(key);
        }

        City city = cityDao.findById(id);
        //加入缓存
        redisTemplate.opsForValue().set(key,city);
        return city;
    }

    @Override
    public Long saveCity(City city) {
        return null;
    }

    @Override
    public Long deleteCity(Long id) {
        return null;
    }

    @Override
    public Long updateCity(City city) {
        return null;
    }
}
