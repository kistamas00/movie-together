module org.kis.movietogether {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;

    opens org.kis.movietogether to javafx.fxml;
    exports org.kis.movietogether;
}