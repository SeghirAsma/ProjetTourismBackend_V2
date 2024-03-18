package com.TourismApp.TourismApplication.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogController {
    private static final Logger logger = LoggerFactory.getLogger(LogController.class);
    @RequestMapping("/logapi")
    public String logMethod(){
        logger.info("hello from spring boot app");
        return "hello tech";
    }

}
