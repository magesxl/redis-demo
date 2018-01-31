package com.example.redis.demo.service.impl;

import com.example.redis.demo.dao.PriceDao;
import com.example.redis.demo.domain.Price;
import com.example.redis.demo.service.PriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriceServiceImpl implements PriceService {

    private static final Logger logger = LoggerFactory.getLogger(PriceServiceImpl.class);

    @Autowired
    private PriceDao priceDao;

    @Override
    public int insert(Price record) {
        return priceDao.insert(record);
    }

    @Override
    public int updateByVersion(Price record) {
        return priceDao.updateByVersion(record);
    }

    @Override
    public Price selectByPrimaryKey(Integer id) {
        return priceDao.selectByPrimaryKey(id);
    }
}
