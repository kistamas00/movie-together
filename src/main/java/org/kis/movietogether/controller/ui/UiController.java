package org.kis.movietogether.controller.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kis.movietogether.controller.application.UiApplicationController;
import org.kis.movietogether.controller.ui.management.ManagementController;
import org.kis.movietogether.model.websocket.WebSocketMode;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

public class UiController extends Application {

    private ConfigurableApplicationContext springContext;
    private UiApplicationController applicationController;
    private ManagementController managementController;

    @Override
    public void init() {
        this.springContext = org.kis.movietogether.Application
                .createAndRunSpringApplication();
        this.applicationController = springContext.getBean(UiApplicationController.class);

        //TODO remove begin
        applicationController.changeUserName("Server");

//        applicationController.changeUserMode(WebSocketMode.GUEST);
//        applicationController.changeUserName("TestUser");
//        applicationController.connectToHost("localhost", "29292");
        //TODO remove end
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(UiController.class.getResource("management-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        managementController = fxmlLoader.getController();
        stage.setResizable(false);
        stage.setTitle("MovieTogether");
        stage.sizeToScene();
        stage.setScene(scene);

        stage.show();
    }

    @Override
    public void stop() {
        springContext.close();
        Platform.exit();
    }

    private ManagementController getManagementController() {
        return managementController;
    }
}
