package com.wise.transdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
//
@EnableScheduling // 启用定时任务
public class WiseTransactionApplication {

    public static void main(String[] args) {
        SpringApplication.run(WiseTransactionApplication.class, args);
    }

}
