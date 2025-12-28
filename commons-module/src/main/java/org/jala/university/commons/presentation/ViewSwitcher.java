package org.jala.university.commons.presentation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public final class ViewSwitcher {

    private static final int STAGE_HEIGHT = 20;

    private ViewSwitcher() {
    }

    private static Scene scene;
    private static Stage stage;
    public static void switchTo(View view) {
        switchTo(view, null);
    }

    public static void switchTo(View view, ViewContext context) {
        if (scene == null) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    ViewSwitcher.class.getClassLoader()
                            .getResource(view.getFileName()));
            Pane root = loader.load();
            BaseController controller = loader.getController();
            controller.setContext(context);
            loader.setController(controller);
            stage.setWidth(root.getPrefWidth());
            stage.setHeight(root.getPrefHeight() + STAGE_HEIGHT);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setup(Stage stage, Scene scene) {
        ViewSwitcher.stage = stage;
        ViewSwitcher.scene = scene;
    }
}
