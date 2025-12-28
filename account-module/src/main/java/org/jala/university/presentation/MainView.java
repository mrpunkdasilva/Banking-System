package org.jala.university.presentation;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.jala.university.commons.presentation.ViewSwitcher;

public final class MainView extends Application {

    private static final String APPLICATION_TITLE = "Account Module Application";

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(new Pane());
        ViewSwitcher.setup(primaryStage, scene);
        ViewSwitcher.switchTo(AccountView.MAIN.getView());
        primaryStage.setScene(scene);
        primaryStage.setTitle(APPLICATION_TITLE);
        primaryStage.show();
    }
}
