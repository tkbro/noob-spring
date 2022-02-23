package com.tkbro.noobmatch.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
public class NoobMatchApplicationContextLoader {
    // todo : log

    public static void main(String[] args) {
        SpringApplication.run(NoobMatchApplicationContextLoader.class, args);
       /* try {
            SpringApplication.run
            AbstractApplicationContext context = new ClassPathXmlApplicationContext();
            context.registerShutdownHook();
        } catch (Exception ex) {
            System.exit(1);
        }*/
    }
}

