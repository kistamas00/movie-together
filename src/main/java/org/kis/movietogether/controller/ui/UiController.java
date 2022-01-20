package org.kis.movietogether.controller.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kis.movietogether.controller.ui.management.ManagementController;

import java.io.IOException;

public class UiController extends Application {
    private ManagementController managementController;

    public static void start() {
        launch();
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

    public ManagementController getManagementController() {
        return managementController;
    }
}
