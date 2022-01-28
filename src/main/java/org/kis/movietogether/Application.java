package org.kis.movietogether;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class Application {

    public static final String APPLICATION_DEFAULT_PORT = "29292";

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", APPLICATION_DEFAULT_PORT));
        app.run(args);
    }
}