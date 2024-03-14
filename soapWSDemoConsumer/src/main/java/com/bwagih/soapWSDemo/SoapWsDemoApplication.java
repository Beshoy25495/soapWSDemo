package com.bwagih.soapWSDemo;

import com.bwagih.soapWSDemo.service.HelloWorldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class SoapWsDemoApplication implements CommandLineRunner {

    @Autowired
    HelloWorldService helloWorldClient;

    public static void main(String[] args) {
        SpringApplication.run(SoapWsDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Age: {}", helloWorldClient.getAge());
    }
}
