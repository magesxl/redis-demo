package com.example.redis.demo.controller;

import com.example.redis.demo.domain.City;
import com.example.redis.demo.errors.Result;
import com.example.redis.demo.errors.ResultGenerator;
import com.example.redis.demo.service.CityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CityController {

    private static final Logger logger = LoggerFactory.getLogger(CityController.class);

    @Autowired
    private CityService cityService;

    @RequestMapping(value = "/city/{id}", method = RequestMethod.GET)
    public Result getId(@PathVariable Long id) {
        return ResultGenerator.genSuccessResult(cityService.findCityById(id));
    }

    @RequestMapping(value = "/city/update", method = RequestMethod.POST)
    public String updateById(@RequestBody City city) {
        cityService.updateCity(city);
        return "success";
    }

    @RequestMapping(value = "/city/", method = RequestMethod.POST)
    public Result redisLimit() {
        cityService.redisLimit();
        return ResultGenerator.genSuccessResult();
    }
}
