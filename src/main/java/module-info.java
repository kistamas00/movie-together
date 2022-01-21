module org.kis.movietogether {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires validatorfx;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.bootstrapicons;
    requires spring.boot.autoconfigure;
    requires spring.boot;

    opens org.kis.movietogether to javafx.fxml;
    opens org.kis.movietogether.controller.ui to javafx.fxml;
    opens org.kis.movietogether.controller.ui.management to javafx.fxml;
    opens org.kis.movietogether to javafx.fxml, spring.core;

    exports org.kis.movietogether;
    exports org.kis.movietogether.controller.ui;
    exports org.kis.movietogether.controller.ui.management;
}