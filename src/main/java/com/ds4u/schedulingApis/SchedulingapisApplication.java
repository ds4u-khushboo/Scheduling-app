package com.ds4u.schedulingApis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages={
        "com.ds4u.schedulingApis","com.ds4u.schedulingApis.configuration"})
public class SchedulingapisApplication {

    public static void main(String[] args) {

        SpringApplication.run(SchedulingapisApplication.class, args);
    }
}
