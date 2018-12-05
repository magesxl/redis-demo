package com.example.redis.demo.service.impl;

import com.example.redis.demo.config.RedisDistributedLock;
import com.example.redis.demo.dao.CityDao;
import com.example.redis.demo.domain.City;
import com.example.redis.demo.errors.CommonDefinedException;
import com.example.redis.demo.service.CityService;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class CityServiceImpl implements CityService {

    private static final Logger logger = LoggerFactory.getLogger(CityServiceImpl.class);

    @Autowired
    private CityDao cityDao;

    @Autowired
    private RedisDistributedLock redisDistributedLock;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public City findCityById(Long id) {
        //缓存key
        String lockKey = "12346789";
        String lockValue = "1234678910_" + id + "_" + Thread.currentThread().getId();
        boolean lock = redisDistributedLock.tryGetDistributedLock(lockKey, lockValue, 10);
        logger.info("当前获取锁lock为：" + lock + "value为：" + lockValue);
//        try {
//            TimeUnit.SECONDS.sleep(6);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        logger.info("当前获取锁线程为：" + Thread.currentThread().getId());
        boolean unLock = redisDistributedLock.releaseDistributedLock(lockKey, lockValue);
        logger.info("当前释放锁锁unLock为：" + unLock + "value为：" + lockValue);

//        String key = "city_" + id;
//        boolean hasKey = stringRedisTemplate.hasKey(key);
//        if (hasKey) {
//            logger.info("从缓存中获取数据");
//            return JSON.parseObject(stringRedisTemplate.opsForValue().get(key), City.class);
//        }
//        City city = cityDao.findById(id);
//        logger.info("从数据库中获取数据");
//        //加入缓存
//        stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(city));
//        stringRedisTemplate.opsForValue().setBit(key, 1, true);
        return null;
    }

    @Override
    public Long saveCity(City city) {
        return null;
    }

    @Override
    public Long deleteCity(Long id) {
        String lockKey = "12346789";
        String lockValue = "1234678910_" + id + "_" + Thread.currentThread().getId();
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        RedissonClient redissonClient = Redisson.create(config);
        RLock lock = redissonClient.getLock(lockValue);
        logger.info("从数据库中获取数据" + lock.getName());
        lock.lock(10, TimeUnit.SECONDS);
        logger.info("锁住了" + lock.getName() + ":" + lock.isHeldByCurrentThread());
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock.unlock();
//        redissonClient.shutdown();
        return 1L;
    }

    @Override
    public void redisLimit() {
        //限流按商户分 控制幅度为每秒一次  获取当前描述为key 每秒调用1次
        String limitKey = "limited_key" + "_" + System.currentTimeMillis() / 1000;
        String count = "1";
        long limit = 0;
        logger.info("当前key为：" + limitKey);
        try {
            limit = redisDistributedLock.permit(limitKey, count, "4");
        } catch (IOException e) {
            logger.error("文件异常");
        }
        if (limit == 0) {
            throw CommonDefinedException.MENU_ROOT_EXITS_ERROR;
        }
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
