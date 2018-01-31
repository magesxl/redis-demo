package com.example.redis.demo.dao;


import com.example.redis.demo.domain.Price;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PriceDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Price record);

    int insertSelective(Price record);

    Price selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Price record);

    int updateByPrimaryKey(Price record);

    int updateByVersion(Price record);
}