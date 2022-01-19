package org.kis.movietogether.controller.ui.management;

import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.kis.movietogether.controller.ui.UiController;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
class ManagementControllerTest {

    private static final String CLIENT_ID = "#clientRadio";
    private static final String SERVER_ID = "#serverRadio";
    private static final String CONNECT_ID = "#connect";
    private static final String ADDRESS_ID = "#address";
    private static final String PORT_ID = "#port";
    private static final String LOG_ID = "#logArea";

    private static ManagementController managementController;

    @BeforeEach
    void beforeEach() throws TimeoutException {
        FxToolkit.registerStage(Stage::new);

        FxToolkit.setupStage(stage -> {
            try {
                UiController uiController = new UiController();
                uiController.start(stage);
                managementController = uiController.getManagementController();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        FxToolkit.showStage();
    }

    @AfterEach
    void afterEach() throws TimeoutException {
        FxToolkit.hideStage();
    }


    /**
     * param robot
     */
    @Test
    @DisplayName("Start server")
    void testStartServer(FxRobot robot) {
        //given
        RadioButton server = robot.lookup(SERVER_ID).queryAs(RadioButton.class);
        RadioButton client = robot.lookup(CLIENT_ID).queryAs(RadioButton.class);
        Button connect = robot.lookup(CONNECT_ID).queryAs(Button.class);
        TextField port = robot.lookup(PORT_ID).queryAs(TextField.class);

        //when
        robot.clickOn(SERVER_ID);
        robot.clickOn(PORT_ID);
        robot.eraseText(port.getText().length());
        robot.write("8080");

        //then
        assertThat(server).isNotNull();
        assertThat(client).isNotNull();
        assertThat(connect).isNotNull();
        assertThat(managementController.isServerInServerMode()).isTrue();

        assertThat(server.isSelected()).isTrue();
        assertThat(client.isSelected()).isFalse();
        assertThat(connect.getText()).isEqualTo("Start");
        assertThat(connect).isEnabled();
    }

    /**
     * param robot
     */
    @Test
    @DisplayName("Start client")
    void testStartClient(FxRobot robot) {
        //given
        RadioButton server = robot.lookup(SERVER_ID).queryAs(RadioButton.class);
        RadioButton client = robot.lookup(CLIENT_ID).queryAs(RadioButton.class);
        Button connect = robot.lookup(CONNECT_ID).queryAs(Button.class);
        TextField port = robot.lookup(PORT_ID).queryAs(TextField.class);
        TextField address = robot.lookup(ADDRESS_ID).queryAs(TextField.class);

        //when
        robot.clickOn(CLIENT_ID);
        robot.clickOn(ADDRESS_ID);
        robot.eraseText(address.getText().length());
        robot.write("192.168.0.1");
        robot.clickOn(PORT_ID);
        robot.eraseText(port.getText().length());
        robot.write("8080");

        //then
        assertThat(managementController.isServerInServerMode()).isFalse();

        assertThat(server.isSelected()).isFalse();
        assertThat(client.isSelected()).isTrue();
        assertThat(connect.getText()).isEqualTo("Connect");
        assertThat(connect).isEnabled();
    }

    /**
     * param robot
     */
    @Test
    @DisplayName("Set invalid IP")
    void testSetInvalidIP(FxRobot robot) {
        //given
        Button connect = robot.lookup(CONNECT_ID).queryAs(Button.class);
        TextField address = robot.lookup(ADDRESS_ID).queryAs(TextField.class);


        //when
        robot.clickOn(CLIENT_ID);
        robot.clickOn(ADDRESS_ID);
        robot.eraseText(address.getText().length());
        robot.write("asdasd");

        //then
        assertThat(connect).isDisabled();
    }

    /**
     * param robot
     */
    @ParameterizedTest
    @ValueSource(strings = {"asdasd", "-1", "100000"})
    @DisplayName("Set invalid port")
    void testSetInvalidPort(String arg, FxRobot robot) {
        //given
        Button connect = robot.lookup(CONNECT_ID).queryAs(Button.class);
        TextField port = robot.lookup(PORT_ID).queryAs(TextField.class);

        //when
        robot.clickOn(CLIENT_ID);
        robot.clickOn(PORT_ID);
        robot.eraseText(port.getText().length());
        robot.write(arg);

        //then
        assertThat(connect).isDisabled();
    }

    /**
     * param robot
     */
    @Test
    @DisplayName("Log message")
    void testLog(FxRobot robot) {
        //given
        String logMessage1 = "message 1";
        String logMessage2 = "message 1";
        TextArea logArea = robot.lookup(LOG_ID).queryAs(TextArea.class);

        //when
        managementController.log(logMessage1);
        managementController.log(logMessage1);

        //then
        assertThat(logArea.getText()).contains(logMessage1);
        assertThat(logArea.getText()).contains(logMessage2);
    }
}
