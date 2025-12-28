package org.jala.university.presentation.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import jakarta.persistence.EntityManager;
import org.jala.university.application.service.implementations.AccountServiceImpl;
import org.jala.university.application.service.interfaces.AccountService;
import org.jala.university.domain.entity.AccountBeneficiaryId;
import org.jala.university.application.dto.BeneficiaryDTO;
import org.jala.university.application.usecases.BeneficiaryUseCases;
import org.jala.university.domain.entity.AccountBeneficiary;
import org.jala.university.domain.entity.Beneficiary;
import org.jala.university.domain.repository.AccountBeneficiaryRepository;
import org.jala.university.domain.repository.AccountRepository;
import org.jala.university.domain.repository.BeneficiaryRepository;
import org.jala.university.domain.services.ValidationService;
import org.jala.university.infrastructure.persistence.AccountBeneficiaryRepositoryImp;
import org.jala.university.infrastructure.persistence.AccountRepositoryImp;
import org.jala.university.infrastructure.persistence.BeneficiaryRepositoryImp;
import org.jala.university.infrastructure.config.JPAConfig;
import org.jala.university.infrastructure.utils.SessionManager;
import org.jala.university.presentation.util.ViewSwitcher;
import org.jala.university.presentation.views.TransactionView;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BeneficiaryController {
    @FXML
    private GridPane formPane;
    @FXML
    private TextField nameField;
    @FXML
    private TextField documentField;
    @FXML
    private TextField accountNumberField;
    @FXML
    private TextField bankField;
    @FXML
    private CheckBox favoriteCheckBox;
    @FXML
    private Label statusLabel;
    @FXML
    private TableView<Beneficiary> beneficiaryTable;
    @FXML
    private Button toggleFavoriteButton;
    @FXML
    private ComboBox<String> filterComboBox;
    @FXML
    private ComboBox<String> categoryComboBox;

    private final ObservableList<Beneficiary> beneficiaryList = FXCollections.observableArrayList();
    private final BeneficiaryUseCases beneficiaryUseCases;
    private final AccountBeneficiaryRepository accountBeneficiaryRepository;
    private final AccountService accountService;
    private Long editingBeneficiaryId;
    private Long currentAccountId;

    // Construtor padrão para o JavaFXorg.jala.university.application.service.impl
    public BeneficiaryController() {
        // Criação de dependências padrão para manter compatibilidade
        ValidationService validationService = new ValidationService();
        EntityManager entityManager = JPAConfig.getEntityManagerFactory().createEntityManager();
        BeneficiaryRepository beneficiaryRepository = new BeneficiaryRepositoryImp(entityManager);
        AccountRepository accountRepository = new AccountRepositoryImp(entityManager);
        AccountBeneficiaryRepository accountBeneficiaryRepository = new AccountBeneficiaryRepositoryImp(entityManager);
        this.accountService = new AccountServiceImpl();

        this.accountBeneficiaryRepository = accountBeneficiaryRepository;
        this.beneficiaryUseCases = new BeneficiaryUseCases(
                beneficiaryRepository,
                accountRepository,
                accountBeneficiaryRepository,
                validationService
        );

        // Obter o ID da conta do usuário logado
        this.currentAccountId = SessionManager.getCurrentAccountId();
    }

    @FXML
    private void initialize() {
        // Verificar se há um usuário logado e uma conta selecionada
        if (SessionManager.getCurrentAccountId() == null) {
            showAlert("Erro", "Nenhuma conta selecionada. Faça login novamente.");
            formPane.setDisable(true);
            return;
        }

        // Atualizar o ID da conta atual
        this.currentAccountId = SessionManager.getCurrentAccountId();

        // Configurar a tabela
        setupTable();

        // Carregar beneficiários
        loadBeneficiaries();

        // Inicialmente esconder o formulário
        formPane.setVisible(false);

        // Configurar o botão de favorito
        toggleFavoriteButton.setDisable(true);

        // Adicionar listener para seleção na tabela
        beneficiaryTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {
                    toggleFavoriteButton.setDisable(newSelection == null);
                });

        // Configurar categorias
        List<String> defaultCategories = beneficiaryUseCases.getDefaultCategories();
        categoryComboBox.getItems().addAll(defaultCategories);
        categoryComboBox.setEditable(true);

        // Configurar filtro (chamando o novo método)
        setupFilter();
    }

    private void setupTable() {
        // Configurar colunas da tabela
        TableColumn<Beneficiary, String> nameColumn = new TableColumn<>("Nome");
        TableColumn<Beneficiary, String> documentColumn = new TableColumn<>("Documento");
        TableColumn<Beneficiary, String> accountColumn = new TableColumn<>("Conta");
        TableColumn<Beneficiary, String> bankColumn = new TableColumn<>("Banco");
        TableColumn<Beneficiary, Boolean> favoriteColumn = new TableColumn<>("Favorito");

        // Configurar fábricas de células
        nameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        documentColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDocument()));
        accountColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAccountNumber()));
        bankColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getBank()));
        favoriteColumn.setCellValueFactory(cellData -> {
            Beneficiary beneficiary = cellData.getValue();
            boolean isFavorite = isBeneficiaryFavorite(beneficiary.getId());
            return new javafx.beans.property.SimpleBooleanProperty(isFavorite);
        });

        // Configurar renderizador para a coluna de favoritos
        favoriteColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item ? "★" : "☆");
                }
            }
        });

        TableColumn<Beneficiary, String> categoryColumn = new TableColumn<>("Categoria");
        categoryColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCategory()));

        // Adicionar colunas à tabela
        beneficiaryTable.getColumns().addAll(nameColumn, documentColumn, accountColumn, bankColumn, favoriteColumn);

        beneficiaryTable.getColumns().add(categoryColumn);

        // Definir a fonte de dados da tabela
        beneficiaryTable.setItems(beneficiaryList);
    }

    private void setupFilter() {
        // Limpar itens existentes
        filterComboBox.getItems().clear();

        // Adicionar opção "Todas" primeiro
        filterComboBox.getItems().add("Todas");

        // Adicionar categorias padrão
        filterComboBox.getItems().addAll(beneficiaryUseCases.getDefaultCategories());

        // Definir valor padrão
        filterComboBox.setValue("Todas");

        // Adicionar listener para filtrar
        filterComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            filterBeneficiariesByCategory(newVal);
        });
    }

    // Novo método para filtrar
    private void filterBeneficiariesByCategory(String category) {
        if (category == null || category.equals("Todas")) {
            beneficiaryTable.setItems(beneficiaryList);
        } else {
            ObservableList<Beneficiary> filteredList = FXCollections.observableArrayList();
            for (Beneficiary b : beneficiaryList) {
                if (category.equals(b.getCategory())) {
                    filteredList.add(b);
                }
            }
            beneficiaryTable.setItems(filteredList);
        }
    }

    private void loadBeneficiaries() {
        beneficiaryList.clear();
        if (currentAccountId != null) {
            beneficiaryList.addAll(beneficiaryUseCases.listBeneficiariesByAccountId(currentAccountId));
        } else {
            showAlert("Erro", "Nenhuma conta selecionada");
        }
    }

    @FXML
    private void showAddBeneficiaryDialog() {
        nameField.clear();
        documentField.clear();
        accountNumberField.clear();
        bankField.clear();
        favoriteCheckBox.setSelected(false);
        formPane.setVisible(true);
        statusLabel.setText("");
        editingBeneficiaryId = null;
        categoryComboBox.getSelectionModel().clearSelection();
        categoryComboBox.setValue(null);
    }

    @FXML
    private void showEditBeneficiaryDialog() {
        Beneficiary selected = beneficiaryTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            editingBeneficiaryId = selected.getId();
            nameField.setText(selected.getName());
            documentField.setText(selected.getDocument());
            accountNumberField.setText(selected.getAccountNumber());
            bankField.setText(selected.getBank());
            categoryComboBox.setValue(selected.getCategory());

            // Verificar se é favorito
            boolean isFavorite = isBeneficiaryFavorite(selected.getId());
            favoriteCheckBox.setSelected(isFavorite);

            formPane.setVisible(true);
            statusLabel.setText("");
        } else {
            showAlert("Erro", "Nenhum beneficiário selecionado para edição!");
        }
    }

    @FXML
    private void hideForm() {
        formPane.setVisible(false);
        editingBeneficiaryId = null;
        statusLabel.setText("");
    }

    @FXML
    private void saveBeneficiary() {
        try {
            if (currentAccountId == null) {
                throw new IllegalStateException("Nenhuma conta selecionada");
            }

            String name = nameField.getText();
            String document = documentField.getText();
            String accountNumber = accountNumberField.getText();
            String bank = bankField.getText();
            boolean favorite = favoriteCheckBox.isSelected();
            String category = categoryComboBox.getValue();

            if (name.isEmpty() || document.isEmpty() || accountNumber.isEmpty() || bank.isEmpty()) {
                throw new IllegalArgumentException("Todos os campos são obrigatórios!");
            }

            BeneficiaryDTO beneficiaryDTO = BeneficiaryDTO.builder()
                    .name(name)
                    .document(document)
                    .accountNumber(accountNumber)
                    .bank(bank)
                    .favorite(favorite)
                    .category(category)
                    .build();

            if (editingBeneficiaryId == null) {
                // Adicionar novo beneficiário
                beneficiaryUseCases.addBeneficiary(beneficiaryDTO, currentAccountId);
            } else {
                // Editar beneficiário existente
                beneficiaryUseCases.editBeneficiary(editingBeneficiaryId, beneficiaryDTO);
                beneficiaryUseCases.setFavoriteBeneficiary(currentAccountId, editingBeneficiaryId, favorite);
            }

            loadBeneficiaries(); // Atualiza a tabela
            hideForm();

            showAlert("Sucesso", "Beneficiário salvo com sucesso!");
        } catch (Exception e) {
            showAlert("Erro", e.getMessage());
        }
    }

    @FXML
    private void handleDeleteBeneficiary() {
        Beneficiary selected = beneficiaryTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                // Confirmar exclusão
                if (showConfirmationDialog("Confirmar exclusão",
                        "Tem certeza que deseja excluir o beneficiário " + selected.getName() + "?")) {
                    beneficiaryUseCases.removeBeneficiary(selected.getId());
                    loadBeneficiaries();
                    showAlert("Sucesso", "Beneficiário removido com sucesso!");
                }
            } catch (Exception e) {
                showAlert("Erro", "Erro ao remover beneficiário: " + e.getMessage());
            }
        } else {
            showAlert("Erro", "Nenhum beneficiário selecionado!");
        }
    }

    // Alterado de private para @FXML public para ser acessível pelo FXML
    @FXML
    public void toggleFavorite() {
        Beneficiary selected = beneficiaryTable.getSelectionModel().getSelectedItem();
        if (selected != null && currentAccountId != null) {
            try {
                // Obter o status atual e inverter
                boolean currentStatus = isBeneficiaryFavorite(selected.getId());
                beneficiaryUseCases.setFavoriteBeneficiary(currentAccountId, selected.getId(), !currentStatus);
                loadBeneficiaries();
                showAlert("Sucesso", "Status de favorito atualizado!");
            } catch (Exception e) {
                showAlert("Erro", "Erro ao atualizar favorito: " + e.getMessage());
            }
        } else {
            showAlert("Erro", "Nenhum beneficiário selecionado!");
        }
    }

    private boolean isBeneficiaryFavorite(Long beneficiaryId) {
        if (currentAccountId == null || beneficiaryId == null) {
            return false;
        }

        try {
            // Buscar a relação entre conta e beneficiário
            AccountBeneficiaryId id =
                    new AccountBeneficiaryId(currentAccountId, beneficiaryId);
            AccountBeneficiary relation = accountBeneficiaryRepository.findById(id);

            // Verificar se é favorito
            return relation != null && relation.getFavorite() != null && relation.getFavorite();
        } catch (Exception e) {
            System.err.println("Erro ao verificar favorito: " + e.getMessage());
            return false;
        }
    }

    private void showAlert(String title, String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: " + (title.equals("Sucesso") ? "green" : "red") + ";");
    }

    private boolean showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType buttonTypeYes = new ButtonType("Sim");
        ButtonType buttonTypeNo = new ButtonType("Não", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        return alert.showAndWait().orElse(buttonTypeNo) == buttonTypeYes;
    }


    /**
     * Navega de volta para a tela de dashboard.
     */
    @FXML
    public void navigateBackToDashboard() {
        try {
            ViewSwitcher.switchTo(TransactionView.DASHBOARD);

            System.out.println("Navegado para tela de dashboard");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Erro ao navegar para o dashboard: " + e.getMessage());
        }
    }
}
