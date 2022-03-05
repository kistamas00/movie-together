package org.kis.movietogether.controller.ui.management;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import net.synedra.validatorfx.Check;
import net.synedra.validatorfx.TooltipWrapper;
import net.synedra.validatorfx.Validator;
import org.apache.commons.validator.routines.InetAddressValidator;

public class ManagementController {
    private static final String INVALID_IP = "Invalid IP address!";
    private static final String USERNAME_EMPTY = "Username can not be empty!";
    private static final String CAN_NOT_CONNECT = "Can not connect because:\n";

    private static final String BUTTON_START = "Start";
    private static final String BUTTON_STOP = "Stop";
    private static final String BUTTON_CONNECT = "Connect";
    private static final String BUTTON_DISCONNECT = "Disconnect";
    public static final String FXML = "management-view.fxml";


    private final Validator inputValidator = new Validator();

    private boolean inServerMode;
    private boolean isConnected;

    @FXML
    private ListView<String> playlist;

    @FXML
    private ToggleGroup serverOrClient;

    @FXML
    private String serverText;

    @FXML
    private TextField addressField;

    @FXML
    private Button connectButton;

    @FXML
    private VBox buttonBox;

    @FXML
    private Boolean isServerDefault;

    @FXML
    private String defaultAddress;

    @FXML
    private TextArea logArea;

    @FXML
    private RadioButton clientRadio;

    @FXML
    private RadioButton serverRadio;

    @FXML
    private TextField usernameField;

    @FXML
    protected void initialize() {
        inServerMode = isServerDefault;
        isConnected = false;

        serverOrClient.selectedToggleProperty().addListener(this::onToggle);

        createInputValidator();
    }

    private static void checkAddress(Check.Context context, boolean inServerMode) {
        String ipAddress = context.get("ip");

        if (!inServerMode && !InetAddressValidator.getInstance().isValid(ipAddress)) {
            context.error(INVALID_IP);
        }

    }

    private void onToggle(ObservableValue<? extends Toggle> observableValue, Toggle oldToggle, Toggle newToggle) {
        final String value = ((RadioButton) newToggle).getText();

        inServerMode = serverText.equals(value);

        if (inServerMode) {
            addressField.setText(defaultAddress);
        }

        setConnectButtonText();

        addressField.setDisable(inServerMode);

    }

    private static void checkUsername(Check.Context context) {
        String username = context.get("username");
        if (username.isEmpty()) {
            context.error(USERNAME_EMPTY);
        }
    }

    public void exit() {
        // Function called before application exits
    }

    public void log(String string) {
        logArea.appendText(string + "\n");
    }


    private void connect() {
        //try to connect

        //success:
        isConnected = true;
        clientRadio.setDisable(true);
        serverRadio.setDisable(true);
        addressField.setDisable(true);
        usernameField.setDisable(true);
    }

    private void disconnect() {
        isConnected = false;
        clientRadio.setDisable(false);
        serverRadio.setDisable(false);
        usernameField.setDisable(false);
        addressField.setDisable(inServerMode);
    }

    @FXML
    protected void onConnectButton() {
        if (isConnected) {
            disconnect();
        } else {
            connect();
        }

        setConnectButtonText();
    }

    private void setConnectButtonText() {
        if (inServerMode) {
            if (isConnected) {
                connectButton.setText(BUTTON_STOP);
            } else {
                connectButton.setText(BUTTON_START);
            }
        } else {
            if (isConnected) {
                connectButton.setText(BUTTON_DISCONNECT);
            } else {
                connectButton.setText(BUTTON_CONNECT);
            }
        }
    }

    private void createInputValidator() {
        inputValidator.createCheck()
                .dependsOn("ip", addressField.textProperty())
                .withMethod((Check.Context context) -> checkAddress(context, inServerMode))
                .decorates(addressField)
                .immediate();

        inputValidator.createCheck()
                .dependsOn("username", usernameField.textProperty())
                .withMethod(ManagementController::checkUsername)
                .decorates(usernameField)
                .immediate();

        TooltipWrapper<Button> connectTooltip = new TooltipWrapper<>(
                connectButton,
                inputValidator.containsErrorsProperty(),
                Bindings.concat(CAN_NOT_CONNECT, inputValidator.createStringBinding())
        );
        connectTooltip.setId("connectionTooltip");
        buttonBox.getChildren().add(connectTooltip);
    }
}
