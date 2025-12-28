package org.jala.university.presentation;

import lombok.Getter;
import org.jala.university.commons.presentation.View;

@Getter
public enum AccountView {
    MAIN("main-view.fxml"),
    CREATE("create-account.fxml"),
    UPDATE("update-account.fxml"),
    CLOSE("close-account.fxml"),
    LIST("list-account.fxml");

    private final View view;

    AccountView(String fileName) {
        this.view = new View(fileName);
    }

}
