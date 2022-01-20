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

    private static final String CLIENT_RADIO = "#clientRadio";
    private static final String SERVER_RADIO = "#serverRadio";
    private static final String CONNECT_BUTTON = "#connectButton";
    private static final String ADDRESS_FIELD = "#addressField";
    private static final String LOG_AREA = "#logArea";

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
        RadioButton server = robot.lookup(SERVER_RADIO).queryAs(RadioButton.class);
        RadioButton client = robot.lookup(CLIENT_RADIO).queryAs(RadioButton.class);
        Button connect = robot.lookup(CONNECT_BUTTON).queryAs(Button.class);
        TextField address = robot.lookup(ADDRESS_FIELD).queryAs(TextField.class);

        //when
        robot.clickOn(SERVER_RADIO);
        robot.clickOn(CONNECT_BUTTON);

        //then
        assertThat(managementController.isServerInServerMode()).isTrue();

        assertThat(address).isDisabled();

        assertThat(server.isSelected()).isTrue();
        assertThat(server).isDisabled();

        assertThat(client.isSelected()).isFalse();
        assertThat(client).isDisabled();

        assertThat(connect.getText()).isEqualTo("Stop");
        assertThat(connect).isEnabled();
    }

    /**
     * param robot
     */
    @Test
    @DisplayName("Start client")
    void testStartClient(FxRobot robot) {
        //given
        RadioButton server = robot.lookup(SERVER_RADIO).queryAs(RadioButton.class);
        RadioButton client = robot.lookup(CLIENT_RADIO).queryAs(RadioButton.class);
        Button connect = robot.lookup(CONNECT_BUTTON).queryAs(Button.class);
        TextField address = robot.lookup(ADDRESS_FIELD).queryAs(TextField.class);

        //when
        robot.clickOn(CLIENT_RADIO);
        robot.clickOn(ADDRESS_FIELD);
        robot.eraseText(address.getText().length());
        robot.write("192.168.0.1");
        robot.clickOn(CONNECT_BUTTON);

        //then
        assertThat(managementController.isServerInServerMode()).isFalse();

        assertThat(address).isDisabled();

        assertThat(server.isSelected()).isFalse();
        assertThat(server).isDisabled();

        assertThat(client.isSelected()).isTrue();
        assertThat(client).isDisabled();

        assertThat(connect.getText()).isEqualTo("Disconnect");
        assertThat(connect).isEnabled();
    }

    /**
     * param robot
     */
    @Test
    @DisplayName("Set invalid IP")
    void testSetInvalidIP(FxRobot robot) {
        //given
        Button connect = robot.lookup(CONNECT_BUTTON).queryAs(Button.class);
        TextField address = robot.lookup(ADDRESS_FIELD).queryAs(TextField.class);

        //when
        robot.clickOn(CLIENT_RADIO);
        robot.clickOn(ADDRESS_FIELD);
        robot.eraseText(address.getText().length());
        robot.write("asdasd");

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
        TextArea logArea = robot.lookup(LOG_AREA).queryAs(TextArea.class);

        //when
        managementController.log(logMessage1);
        managementController.log(logMessage1);

        //then
        assertThat(logArea.getText()).contains(logMessage1);
        assertThat(logArea.getText()).contains(logMessage2);
    }

    /**
     * param robot
     */
    @ParameterizedTest(name = "[{index}] Is server selected: {0}")
    @ValueSource(booleans = {true, false})
    @DisplayName("Test default connect button text")
    void testDefaultButtonText(boolean arg, FxRobot robot) {
        //given
        Button connect = robot.lookup(CONNECT_BUTTON).queryAs(Button.class);
        TextField address = robot.lookup(ADDRESS_FIELD).queryAs(TextField.class);

        //when
        if (arg) {
            robot.clickOn(SERVER_RADIO);
        } else {
            robot.clickOn(CLIENT_RADIO);
        }

        //then
        if (arg) {
            assertThat(connect.getText()).isEqualTo("Start");
            assertThat(address).isDisabled();
        } else {
            assertThat(connect.getText()).isEqualTo("Connect");
            assertThat(address).isEnabled();
        }
    }
}
