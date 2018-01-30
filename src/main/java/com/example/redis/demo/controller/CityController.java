package com.example.redis.demo.controller;

import com.example.redis.demo.domain.City;
import com.example.redis.demo.service.CityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CityController {

    private static final Logger logger = LoggerFactory.getLogger(CityController.class);

    @Autowired
    private CityService cityService;

    @RequestMapping(value = "/city/{id}",method = RequestMethod.GET)
    public City getId(@PathVariable Long id) {
        return cityService.findCityById(id);
    }
}
