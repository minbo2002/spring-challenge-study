package com.example.springchallenge2week;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling   // 스케줄러 사용을 위한 어노테이션
@SpringBootApplication
public class SpringChallenge2weekApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringChallenge2weekApplication.class, args);
    }

}
