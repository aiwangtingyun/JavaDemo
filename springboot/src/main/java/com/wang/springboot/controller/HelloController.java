package com.wang.springboot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Map;

//@RestController
//使用@RestController将无法生效thymeleaf，需要使用@Controller
@Controller
public class HelloController {

    @Value("${person.last-name}")
    private String name;

//    @RequestMapping({"/", "/index"})
//    public String index() {
//        return "index";
//    }

    @ResponseBody
    @RequestMapping("/hello")
    public String sayHello() {
        return "hello " + name;
    }

    @RequestMapping("/success")
    public String success(Map<String, Object> map) {
        // 返回 classpath:templates/success.html
        map.put("hello", "<b>你好!</b>");
        map.put("users", Arrays.asList("Marry", "Andy", "John"));
        return "success";
    }
}
