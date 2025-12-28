package org.jala.university.presentation.controller;

import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.jala.university.commons.presentation.BaseController;
import org.jala.university.infrastructure.config.JpaInitializer;

public class MainController extends BaseController {
    private EntityManagerFactory emf;

    @FXML
    private void initialize() {
        try {
            // Inicializa o JPA quando o controller é carregado
            emf = JpaInitializer.getEntityManagerFactory();
            System.out.println("JPA inicializado com sucesso no MainController!");

            // Aqui você pode adicionar outras inicializações necessárias
        } catch (Exception e) {
            System.err.println("Erro ao inicializar JPA no MainController:");
            e.printStackTrace();
            System.err.println("Erro de inicialização: Não foi possível conectar ao banco de dados");
        }
    }

    @FXML
    public void handleCreateAccountAction(ActionEvent actionEvent) {
        //
    }

    @FXML
    public void handleViewAccountAction(ActionEvent actionEvent) {
        // Implementação da visualização de contas
    }
}
