package org.jala.university;

import javafx.application.Application;
import javafx.stage.Stage;
import org.jala.university.presentation.MainView;

public final class MainApp extends Application {

    public static void main(String[] args) {
        MainView.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.show();
    }

}
