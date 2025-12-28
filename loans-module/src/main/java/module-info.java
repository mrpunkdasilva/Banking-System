module org.example.capstone {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.example.capstone to javafx.fxml;
    opens org.example.capstone.controller.emprestimo to javafx.fxml;

    exports org.example.capstone;
    exports org.example.capstone.controller.emprestimo;
}
