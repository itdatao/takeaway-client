package com.ruoyi.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author Huhuitao
 * @Date 2021/2/3 18:30
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@MapperScan(basePackages = "com.ruoyi.*")
public class TakeawayApplication  {
    public static void main(String[] args) {
        SpringApplication.run(TakeawayApplication.class,args);
    }
}
