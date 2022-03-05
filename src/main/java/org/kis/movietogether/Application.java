package org.kis.movietogether;

import org.kis.movietogether.controller.ui.UiController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Collections;

@SpringBootApplication
public class Application {

    private static String[] applicationArgs;
    public static final String APPLICATION_DEFAULT_PORT = "29292";

    public static ConfigurableApplicationContext createAndRunSpringApplication() {
        final SpringApplication app = new SpringApplication(Application.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", APPLICATION_DEFAULT_PORT));
        return app.run(applicationArgs);
    }

    public static void main(String[] args) {
        applicationArgs = args.clone();
        javafx.application.Application.launch(UiController.class, args);
    }
}