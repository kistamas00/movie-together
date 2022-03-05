package org.kis.movietogether.controller.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.kis.movietogether.controller.ApplicationController;
import org.kis.movietogether.controller.ui.management.ManagementController;
import org.kis.movietogether.controller.ui.player.PlayerController;
import org.kis.movietogether.model.websocket.WebSocketMode;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

public class UiController extends Application {

    private ConfigurableApplicationContext springContext;
    private ApplicationController applicationController;
    private ManagementController managementController;
    private PlayerController playerController;

    private Stage management;
    private Stage player;

    @Override
    public void init() {
        this.springContext = org.kis.movietogether.Application
                .createAndRunSpringApplication();
        this.applicationController = springContext.getBean(ApplicationController.class);

        //TODO remove begin
        applicationController.changeUserName("Server");

        /*applicationController.changeUserMode(WebSocketMode.GUEST);
        applicationController.changeUserName("TestUser");
        applicationController.connectToHost("localhost", "29292");*/
        //TODO remove end
    }

    @Override
    public void start(Stage stage) throws IOException {
        management = stage;
        player = new Stage();

        createManagement();
        createPlayer();

        management.setOnCloseRequest(this::onExit);
        player.setOnCloseRequest(this::onExit);

        management.show();
        player.show();
    }

    private void createManagement() throws IOException {
        FXMLLoader managementLoader = new FXMLLoader(UiController.class.getResource(ManagementController.FXML));
        Scene managementScene = new Scene(managementLoader.load());

        managementController = managementLoader.getController();

        management.setResizable(false);
        management.setTitle("MovieTogether");
        management.setScene(managementScene);
        management.sizeToScene();
    }

    private void createPlayer() throws IOException {
        FXMLLoader playerLoader = new FXMLLoader(UiController.class.getResource(PlayerController.FXML));
        Scene playerScene = new Scene(playerLoader.load());

        playerController = playerLoader.getController();

        player.setResizable(false);
        player.setTitle("MovieTogether");
        player.setScene(playerScene);
        player.sizeToScene();
    }

    private void onExit(WindowEvent event) {
        player.hide();
        management.hide();

        playerController.exit();
        managementController.exit();

        springContext.close();

        player.close();
        management.close();
    }
}
