package org.kis.movietogether.controller.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kis.movietogether.controller.ui.management.ManagementController;

import java.io.IOException;

public class UiController extends Application {

    private static final int HEIGHT = 600;
    private static final int WIDTH = 500;

    private ManagementController managementController;

    public static void start() {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(UiController.class.getResource("management-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        managementController = fxmlLoader.getController();
        stage.setResizable(false);
        stage.setTitle("Hello!");
        stage.setScene(scene);

        stage.show();
    }

    public ManagementController getManagementController() {
        return managementController;
    }
}
