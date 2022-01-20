package org.kis.movietogether.controller.ui.management;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import net.synedra.validatorfx.Check;
import net.synedra.validatorfx.TooltipWrapper;
import net.synedra.validatorfx.Validator;

import java.util.regex.Pattern;


public class ManagementController {
    private static final String VALID_IPV6 =
            "(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))";
    private static final String VALID_IPV4 =
            "(\\b25[0-5]|\\b2[0-4][0-9]|\\b[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}";

    private static final Pattern IPV6_PATTERN = Pattern.compile(VALID_IPV6);
    private static final Pattern IPV4_PATTERN = Pattern.compile(VALID_IPV4);

    private static final String INVALID_IP = "Invalid IP address!";
    private static final String USERNAME_EMPTY = "Username can not be empty!";
    private static final String CAN_NOT_CONNECT = "Can not connect because:\n";

    private static final String BUTTON_START = "Start";
    private static final String BUTTON_STOP = "Stop";
    private static final String BUTTON_CONNECT = "Connect";
    private static final String BUTTON_DISCONNECT = "Disconnect";


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

        if (
                !inServerMode &&
                        !IPV4_PATTERN.matcher(ipAddress).matches() &&
                        !IPV6_PATTERN.matcher(ipAddress).matches()
        ) {
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

    private String getAddress() {
        return addressField.getText();
    }

    public boolean isServerInServerMode() {
        return inServerMode;
    }

    private void createInputValidator() {
        inputValidator.createCheck()
                .dependsOn("ip", addressField.textProperty())
                .withMethod((Check.Context context) -> checkAddress(context, inServerMode))
                .decorates(addressField)
                .immediate();

        inputValidator.createCheck()
                .dependsOn("username", usernameField.textProperty())
                .withMethod(this::checkUsername)
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
        setAddressField();
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

    private void setAddressField() {
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

    private void checkUsername(Check.Context context) {
        String username = context.get("username");
        if (username.equals("")) {
            context.error(USERNAME_EMPTY);
        }
    }


}
