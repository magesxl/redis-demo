package com.example.redis.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.redis.demo.dao.CityDao;
import com.example.redis.demo.domain.City;
import com.example.redis.demo.service.CityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CityServiceImpl implements CityService {

    private static final Logger logger = LoggerFactory.getLogger(CityServiceImpl.class);

    @Autowired
    private CityDao cityDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public City findCityById(Long id) {
        //缓存key
        String key = "city_" + id;
        boolean hasKey = stringRedisTemplate.hasKey(key);
        if (hasKey) {
            logger.info("从缓存中获取数据");
            return JSON.parseObject(stringRedisTemplate.opsForValue().get(key), City.class);
        }

        City city = cityDao.findById(id);
        logger.info("从数据库中获取数据");
        //加入缓存
        stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(city));
        stringRedisTemplate.opsForValue().setBit(key, 1, true);
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

    /**
     * 缓存更新
     *
     * @param city
     * @return
     */
    @Override
    public Long updateCity(City city) {
        String key = "city_" + city.getId();
        Long sum = cityDao.updateCity(city);
        boolean hasKey = stringRedisTemplate.hasKey(key);
        if (hasKey) {
            stringRedisTemplate.delete(key);  //删除缓存
            logger.info("删除缓存成功");
        }
        return sum;
    }
}
