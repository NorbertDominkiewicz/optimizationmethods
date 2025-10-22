module com.ndominkiewicz.optui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.google.common;
    requires exp4j;
    requires org.checkerframework.checker.qual;

    opens com.ndominkiewicz.optui to javafx.fxml;
    exports com.ndominkiewicz.optui;
    opens com.ndominkiewicz.optui.controllers to javafx.fxml;
    exports com.ndominkiewicz.optui.controllers;
}