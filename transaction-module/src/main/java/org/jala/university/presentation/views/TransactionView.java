package org.jala.university.presentation.views;

import lombok.Getter;

@Getter
public enum TransactionView {
    LOGIN("fxml/login-view.fxml"),
    SIGNUP("fxml/signup-view.fxml"),
    TRANSACTIONS("fxml/transfer-view.fxml"),
    DASHBOARD("fxml/dashboard-view.fxml"),
    BENEFICIARY("fxml/beneficiary-view.fxml"),
    HISTORIC("fxml/history-view.fxml"),
    TRANSACTION_DETAILS("fxml/transaction-details-screen.fxml"),
    PROFILE("fxml/profile-view.fxml"),
    TWO_FACTOR_VERIFICATION("fxml/two-factor-verification.fxml")
    , STATEMENT("fxml/statement-view.fxml"),
    TWO_FACTOR_SETTINGS("fxml/two-factor-settings.fxml");

    private final String fxmlFile;

    TransactionView(String fxmlFile) {
        this.fxmlFile = fxmlFile;
    }
}
