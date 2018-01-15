package org.cfets.ddd.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by pluto on 16/01/2018.
 */
@EnableAutoConfiguration
@RestController
public class AppController {

    @RequestMapping("/")
    public String index(){
        return "Hello, CFETS";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(AppController.class, args);
    }
}
