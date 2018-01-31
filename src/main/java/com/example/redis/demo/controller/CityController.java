package com.example.redis.demo.controller;

import com.example.redis.demo.domain.City;
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
    public City getId(@PathVariable Long id) {
        return cityService.findCityById(id);
    }

    @RequestMapping(value = "/city/update", method = RequestMethod.POST)
    public String updateById(@RequestBody City city) {
        cityService.updateCity(city);
        return "success";
    }
}
