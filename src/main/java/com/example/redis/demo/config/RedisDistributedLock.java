package com.example.redis.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Configuration
public class RedisDistributedLock {

    private static final Logger logger = LoggerFactory.getLogger(RedisDistributedLock.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final Long RELEASE_SUCCESS = 1L;

    private static int RETRY_TIMES = 2;

    private static final long TIMEOUT = 2000;

    /**
     * 尝试获取分布式锁
     *
     * @param lockKey    锁
     * @param lockValue  请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public boolean tryGetDistributedLock(String lockKey, String lockValue, long expireTime) {
        return stringRedisTemplate.execute((RedisCallback<Boolean>) connection -> {
            RedisStringCommands commands = connection.stringCommands();
            boolean result = commands.set(lockKey.getBytes(), lockValue.getBytes(), Expiration.seconds(expireTime), RedisStringCommands.SetOption.SET_IF_ABSENT);
            return result;
        });
    }

    /**
     * 尝试获取分布式锁
     *
     * @param lockKey    锁
     * @param requestId  请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public boolean tryLockTimes(String lockKey, String requestId, long expireTime) {
        long beginTime = System.nanoTime();  // 用nanos、mills具体看需求.
        long timeout = TimeUnit.SECONDS.toNanos(TIMEOUT);
        try {
            while (System.nanoTime() - beginTime < timeout) {
                if (tryGetDistributedLock(lockKey, requestId, expireTime)) {
                    return true;
                }
                // 短暂休眠后轮询，避免可能的活锁

                TimeUnit.MILLISECONDS.sleep(3);
            }
        } catch (InterruptedException e) {
            logger.error("线程中断");
        }
        return false;
    }

    /**
     * 释放分布式锁
     *
     * @param lockKey   锁
     * @param lockValue 请求标识
     * @return 是否释放成功
     */
    public boolean releaseDistributedLock(String lockKey, String lockValue) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
//        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then redis.call('del', KEYS[1]) return true end return false";
        return stringRedisTemplate.execute(new DefaultRedisScript<>(script, Boolean.class), Collections.singletonList(lockKey), lockValue);
    }

    public long permit(String lockKey, String count, String time) throws IOException {
        byte[] lua = Files.readAllBytes(ResourceUtils.getFile("classpath:limit_1.lua").toPath());
        String luaScript = new String(lua);
        Long res = stringRedisTemplate.execute(new DefaultRedisScript<>(luaScript, Long.class), Collections.singletonList(lockKey), count, time);
        return res.longValue();
    }

}
