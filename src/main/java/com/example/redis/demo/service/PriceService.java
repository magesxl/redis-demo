package com.example.redis.demo.service;

import com.example.redis.demo.domain.Price;

public interface PriceService {
    int insert(Price record);

    Price selectByPrimaryKey(Integer id);

    int updateByVersion(Price record);
}
