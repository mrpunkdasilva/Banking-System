package org.jala.university.presentation.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for managing JavaFX scenes.
 * Implements the Singleton pattern.
 */
public class SceneManager {
    
    private static SceneManager instance;
    private Stage primaryStage;
    private final Map<String, Scene> scenes = new HashMap<>();
    
    /**
     * Private constructor to enforce Singleton pattern.
     */
    private SceneManager() {}
    
    /**
     * Gets the singleton instance of SceneManager.
     * 
     * @return the SceneManager instance
     */
    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }
    
    /**
     * Sets the primary stage for the application.
     * 
     * @param stage the primary stage
     */
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }
    
    /**
     * Loads a scene from an FXML file.
     * 
     * @param fxmlPath the path to the FXML file
     */
    public void loadScene(String fxmlPath) {
        try {
            if (!scenes.containsKey(fxmlPath)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxmlPath));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                scenes.put(fxmlPath, scene);
            }
            
            // Garantir que o stage permaneça redimensionável
            primaryStage.setResizable(true);
            
            primaryStage.setScene(scenes.get(fxmlPath));
            primaryStage.show();
            
            // Verificar se o stage está redimensionável após mostrar a cena
            System.out.println("Stage resizable após carregar " + fxmlPath + ": " + primaryStage.isResizable());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar cena: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro inesperado ao carregar cena: " + e.getMessage());
        }
    }
    
    /**
     * Removes a scene from the cache.
     * 
     * @param fxmlPath the path to the FXML file
     */
    public void removeScene(String fxmlPath) {
        scenes.remove(fxmlPath);
    }
    
    /**
     * Clears all cached scenes.
     */
    public void clearCache() {
        scenes.clear();
    }
}