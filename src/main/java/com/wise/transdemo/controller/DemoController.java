package com.wise.transdemo.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/hello") // 映射 GET 请求到 /hello 路径
    public String hello() {
        return "Hello, Spring Boot!";
    }

}
