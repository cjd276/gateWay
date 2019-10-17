package com.cjd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan(basePackages="**.dao")
public class GateWayStarter {

    public static void main(String[] args) {
        SpringApplication.run(GateWayStarter.class,args);
    }

}
