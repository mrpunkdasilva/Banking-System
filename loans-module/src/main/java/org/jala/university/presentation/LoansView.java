package org.jala.university.presentation;

import lombok.Getter;
import org.jala.university.commons.presentation.View;

@Getter
public enum LoansView {
    MAIN("main-view.fxml");

    private final View view;

    LoansView(String fileName) {
        this.view = new View(fileName);
    }

}
