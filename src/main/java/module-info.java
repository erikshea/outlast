module com.erikshea.outlast {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.erikshea.outlast.application to javafx.fxml;
    opens com.erikshea.outlast.controller to javafx.fxml;
    exports com.erikshea.outlast.application;
}
