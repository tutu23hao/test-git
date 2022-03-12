package com.tianle.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：tianLe
 * @date ：Created in 2022/3/12 8:54 上午
 * @description：
 * @modified By：
 * @version:
 */
@RestController
public class FirstController {

    @GetMapping("/hello")
    public String hello(){
        return "helloWorld!!!!!"+"very good";
    }
}
