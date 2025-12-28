package org.jala.university.presentation.util;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import org.jala.university.application.dto.UserDTO;
import org.jala.university.presentation.controller.TwoFactorVerificationController;
import org.jala.university.presentation.views.TransactionView;

import java.io.IOException;
import java.net.URL;

/**
 * Utility class for switching JavaFX views (scenes) in the application.
 * Handles loading FXML files, applying CSS, and managing the primary stage.
 * <p>
 * All methods are static. This class cannot be instantiated.
 * </p>
 */
public final class ViewSwitcher {

    /**
     * Private constructor to prevent instantiation.
     */
    private ViewSwitcher() {
        throw new UnsupportedOperationException("Utility class");
    }

    private static Scene scene;
    /**
     * -- GETTER --
     *  Returns the current primary Stage reference.
     *
     * @return the current primary Stage, or null if not set
     */
    @Getter
    private static Stage primaryStage;

    /**
     * Sets the current scene reference. If the scene's window is a Stage, also sets the primary stage.
     *
     * @param scene the JavaFX Scene to set as current
     */
    public static void setScene(Scene scene) {
        ViewSwitcher.scene = scene;
        if (scene != null && scene.getWindow() instanceof Stage stage) {
            ViewSwitcher.primaryStage = stage;
        }
    }

    /**
     * Sets the primary stage reference.
     *
     * @param stage the JavaFX Stage to set as primary
     */
    public static void setPrimaryStage(Stage stage) {
        ViewSwitcher.primaryStage = stage;
    }

    /**
     * Switches the current view to the given TransactionView.
     * Loads the corresponding FXML, applies CSS, and updates the scene.
     * If no scene exists, creates a new one.
     * <p>
     * This method is always run on the JavaFX Application Thread.
     * </p>
     *
     * @param view the TransactionView to switch to
     */
    public static void switchTo(TransactionView view) {
        Platform.runLater(() -> {
            try {
                System.out.println("Loading view: " + view.getFxmlFile());

                String fxmlPath = "/" + view.getFxmlFile();
                URL fxmlUrl = ViewSwitcher.class.getResource(fxmlPath);

                if (fxmlUrl == null) {
                    System.err.println("FXML file not found: " + fxmlPath);
                    return;
                }

                FXMLLoader loader = new FXMLLoader(fxmlUrl);
                Parent root = loader.load();

                if (scene == null) {
                    scene = new Scene(root, 1000, 700);
                    if (primaryStage != null) {
                        primaryStage.setScene(scene);
                    }
                } else {
                    scene.setRoot(root);
                }

                applyCssToScene(scene);

                if (primaryStage != null) {
                    configureAndShowStage(primaryStage);
                } else if (scene != null && scene.getWindow() instanceof Stage stage) {
                    configureAndShowStage(stage);
                }

                System.out.println("View loaded and displayed successfully: " + view.getFxmlFile());

            } catch (IOException e) {
                System.err.println("IO error loading view: " + view.getFxmlFile());
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Unexpected error loading view: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public static void switchTo(TransactionView view, UserDTO user) {
    Platform.runLater(() -> {
        try {
            System.out.println("Loading view: " + view.getFxmlFile());

            String fxmlPath = "/" + view.getFxmlFile();
            URL fxmlUrl = ViewSwitcher.class.getResource(fxmlPath);

            if (fxmlUrl == null) {
                System.err.println("FXML file not found: " + fxmlPath);
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            // Get the controller and pass the user data
            TwoFactorVerificationController controller = loader.getController();
            controller.initData(user);

            if (scene == null) {
                scene = new Scene(root, 1000, 700);
                if (primaryStage != null) {
                    primaryStage.setScene(scene);
                }
            } else {
                scene.setRoot(root);
            }

            applyCssToScene(scene);

            if (primaryStage != null) {
                configureAndShowStage(primaryStage);
            } else if (scene != null && scene.getWindow() instanceof Stage stage) {
                configureAndShowStage(stage);
            }

            System.out.println("View loaded and displayed successfully: " + view.getFxmlFile());

        } catch (IOException e) {
            System.err.println("IO error loading view: " + view.getFxmlFile());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error loading view: " + e.getMessage());
            e.printStackTrace();
        }
    });
}


    /**
     * Switches the view to the given TransactionView using the specified Stage.
     * Loads the FXML, applies CSS, and sets the scene on the provided stage.
     * <p>
     * This method is always run on the JavaFX Application Thread.
     * </p>
     *
     * @param view  the TransactionView to switch to
     * @param stage the Stage to use for displaying the view
     */
    public static void switchTo(TransactionView view, Stage stage) {
        Platform.runLater(() -> {
            try {
                System.out.println("Loading view with stage: " + view.getFxmlFile());

                String fxmlPath = "/" + view.getFxmlFile();
                URL fxmlUrl = ViewSwitcher.class.getResource(fxmlPath);

                if (fxmlUrl == null) {
                    System.err.println("FXML file not found: " + fxmlPath);
                    return;
                }

                FXMLLoader loader = new FXMLLoader(fxmlUrl);
                Parent root = loader.load();

                if (scene == null || stage.getScene() == null) {
                    scene = new Scene(root, 1000, 700);
                    stage.setScene(scene);
                } else {
                    scene = stage.getScene();
                    scene.setRoot(root);
                }

                applyCssToScene(scene);

                configureAndShowStage(stage);

                System.out.println("View loaded and displayed successfully: " + view.getFxmlFile());

            } catch (IOException e) {
                System.err.println("IO error loading view: " + view.getFxmlFile());
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Unexpected error loading view: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * Applies the application's CSS stylesheet to the given scene.
     *
     * @param scene the Scene to apply the CSS to
     */
    private static void applyCssToScene(Scene scene) {
        try {
            String cssPath = "/styles/styles.css";
            URL cssUrl = ViewSwitcher.class.getResource(cssPath);

            if (cssUrl != null) {
                String cssExternalForm = cssUrl.toExternalForm();
                scene.getStylesheets().clear();
                scene.getStylesheets().add(cssExternalForm);
                System.out.println("CSS applied successfully: " + cssExternalForm);
            } else {
                System.err.println("CSS file not found: " + cssPath);
            }
        } catch (Exception e) {
            System.err.println("Error applying CSS: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Configures and shows the given stage with minimum size and focus.
     *
     * @param stage the Stage to configure and show
     */
    private static void configureAndShowStage(Stage stage) {
        try {
            stage.setResizable(true);
            stage.setMinWidth(800);
            stage.setMinHeight(600);

            if (stage.getWidth() < 800) {
                stage.setWidth(1000);
            }
            if (stage.getHeight() < 600) {
                stage.setHeight(700);
            }

            if (!stage.isShowing()) {
                stage.show();
            }

            stage.toFront();
            stage.requestFocus();

            System.out.println("Stage configured and shown - Resizable: " + stage.isResizable() +
                    ", Showing: " + stage.isShowing() +
                    ", Size: " + stage.getWidth() + "x" + stage.getHeight());

        } catch (Exception e) {
            System.err.println("Error configuring and showing stage: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Prints debug information about the current state of the ViewSwitcher.
     */
    public static void debugCurrentState() {
        System.out.println("=== DEBUG ViewSwitcher ===");
        System.out.println("Scene: " + (scene != null ? "Present" : "Null"));
        System.out.println("PrimaryStage: " + (primaryStage != null ? "Present" : "Null"));

        if (scene != null) {
            System.out.println("Scene Root: " + (scene.getRoot() != null ? scene.getRoot().getClass().getSimpleName() : "Null"));
            System.out.println("Scene Window: " + (scene.getWindow() != null ? "Present" : "Null"));
        }

        if (primaryStage != null) {
            System.out.println("Stage Showing: " + primaryStage.isShowing());
            System.out.println("Stage Scene: " + (primaryStage.getScene() != null ? "Present" : "Null"));
        }
        System.out.println("========================");
    }

    /**
     * Returns the current Scene reference.
     *
     * @return the current Scene, or null if not set
     */
    public static Scene getCurrentScene() {
        return scene;
    }

    /**
     * Checks if the FXML file for the given TransactionView exists in the resources.
     *
     * @param view the TransactionView to check
     * @return true if the FXML file exists, false otherwise
     */
    public static boolean viewExists(TransactionView view) {
        String fxmlPath = "/" + view.getFxmlFile();
        URL fxmlUrl = ViewSwitcher.class.getResource(fxmlPath);
        return fxmlUrl != null;
    }
}
