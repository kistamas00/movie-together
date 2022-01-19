module org.kis.movietogether {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;

    opens org.kis.movietogether to javafx.fxml;
    opens org.kis.movietogether.controller.ui to javafx.fxml;
    opens org.kis.movietogether.controller.ui.management to javafx.fxml;

    exports org.kis.movietogether;
    exports org.kis.movietogether.controller.ui;
    exports org.kis.movietogether.controller.ui.management;
}