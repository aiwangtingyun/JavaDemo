package com.wang.boot_activemq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling   // 开启 shedule
public class BootActivemqApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootActivemqApplication.class, args);
    }

}
