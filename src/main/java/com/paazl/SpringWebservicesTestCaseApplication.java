package com.paazl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringWebservicesTestCaseApplication {
    /*
     * Extend the server with rest resource endpoints that will allow a client to
     * order new sheep, and get the current status (balance of the shepherds account
     * and the amount of sheep (dead and alive)).
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringWebservicesTestCaseApplication.class, args);
    }
}
