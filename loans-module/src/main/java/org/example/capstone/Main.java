package org.example.capstone;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Caminho correto (se o FXML estiver em resources/view/emprestimo/)
        Parent root = FXMLLoader.load(getClass().getResource("/view/emprestimo/SolicitarEmprestimo.fxml")); // aqui um dos caminhos para ver essa tela futuramente deve mudar ou nao nao sei ainda
        primaryStage.setTitle("Simulador de Empréstimo");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
