module org.example.library_management {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.library_management to javafx.fxml;
    exports org.example.library_management;
}