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

    private static final int PORT_MAX = 65535;
    private static final int PORT_MIN = 0;

    private static final String INVALID_IP = "Invalid IP address!";
    private static final String CAN_NOT_CONNECT = "Can not connect because:\n";
    private static final String PORT_NAN = "Port has to be an integer!";
    private static final String PORT_MIN_ERROR = "Port has to be greater than " + PORT_MIN + "!";
    private static final String PORT_MAX_ERROR = "Port has to be lower than " + PORT_MAX + "!";


    private static final String BUTTON_START = "Start";
    private static final String BUTTON_CONNECT = "Connect";


    private final Validator inputValidator = new Validator();

    private boolean inServerMode;

    @FXML
    private ListView<String> playlist;

    @FXML
    private ToggleGroup serverOrClient;

    @FXML
    private String serverText;

    @FXML
    private TextField address;

    @FXML
    private TextField port;

    @FXML
    private Button connect;

    @FXML
    private VBox buttonBox;

    @FXML
    private Boolean isServerDefault;

    @FXML
    private String defaultAddress;

    @FXML
    private TextArea logArea;

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

    private static void checkPort(Check.Context context) {
        try {
            int portNumber = Integer.parseInt(context.get("port"));

            if (portNumber < PORT_MIN) {
                context.error(PORT_MIN_ERROR);
            }

            if (portNumber > PORT_MAX) {
                context.error(PORT_MAX_ERROR);
            }
        } catch (NumberFormatException exception) {
            context.error(PORT_NAN);
        }
    }

    @FXML
    protected void initialize() {
        inServerMode = isServerDefault;

        serverOrClient.selectedToggleProperty().addListener(this::onToggle);

        createInputValidator();
    }

    private void onToggle(ObservableValue<? extends Toggle> observableValue, Toggle oldToggle, Toggle newToggle) {
        final String value = ((RadioButton) newToggle).getText();

        inServerMode = serverText.equals(value);

        if (inServerMode) {
            address.setText(defaultAddress);
            connect.setText(BUTTON_START);
        } else {
            connect.setText(BUTTON_CONNECT);
        }

        address.setDisable(inServerMode);

    }

    private String getAddress() {
        return address.getText();
    }

    private int getPort() {
        try {
            return Integer.parseInt(port.getText());
        } catch (NumberFormatException exception) {
            exception.printStackTrace();
            return -1;
        }
    }

    public boolean isServerInServerMode() {
        return inServerMode;
    }

    private void createInputValidator() {
        inputValidator.createCheck()
                .dependsOn("ip", address.textProperty())

                .withMethod((Check.Context context) -> {
                    checkAddress(context, inServerMode);
                })
                .decorates(address)
                .immediate();

        inputValidator.createCheck()
                .dependsOn("port", port.textProperty())
                .withMethod(ManagementController::checkPort)
                .decorates(port)
                .immediate();

        TooltipWrapper<Button> connectTooltip = new TooltipWrapper<>(
                connect,
                inputValidator.containsErrorsProperty(),
                Bindings.concat(CAN_NOT_CONNECT, inputValidator.createStringBinding())
        );
        connectTooltip.setId("connectionTooltip");
        buttonBox.getChildren().add(connectTooltip);
    }

    public void log(String string) {
        logArea.appendText(string + "\n");
    }


}
