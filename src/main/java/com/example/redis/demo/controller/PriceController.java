package com.example.redis.demo.controller;

import com.example.redis.demo.domain.Price;
import com.example.redis.demo.service.PriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RestController
public class PriceController {
    private static final Logger logger = LoggerFactory.getLogger(PriceController.class);

    @Autowired
    private PriceService priceService;

    /**
     * 乐观锁使用 对表字段添加version  每次更新数据前  对比版本号是否一致  若一致更新
     *
     */
    @RequestMapping(value = "/price/update", method = RequestMethod.POST)
    public void threadPrice() {
        Price price = priceService.selectByPrimaryKey(1268);
        Integer versionNo = price.getVersion();
        ThreadPoolExecutor pool = new ThreadPoolExecutor(3, 5, 2, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(20), new ThreadPoolExecutor.CallerRunsPolicy());
        for (int i = 0; i < 3; i++) {
            pool.execute(()->{
                int ron = new Random().nextInt(20);
                logger.info("本次消费=" + ron);
                price.setFront(new BigDecimal(ron));
                int count = priceService.updateByVersion(price);
                if (count == 0) {
                    logger.error("更新失败");
                } else {
                    logger.info("更新成功");
                }
            });
        }
    }
}
