module org.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.zaxxer.hikari;
    requires java.sql;
    requires static lombok;

    opens org.example.demo1 to javafx.fxml;
    exports org.example.demo1;
}
