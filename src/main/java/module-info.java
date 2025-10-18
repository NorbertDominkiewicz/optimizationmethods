module com.ndominkiewicz.optui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.ndominkiewicz.optui to javafx.fxml;
    exports com.ndominkiewicz.optui;
    opens com.ndominkiewicz.optui.controllers to javafx.fxml;
    exports com.ndominkiewicz.optui.controllers;
}