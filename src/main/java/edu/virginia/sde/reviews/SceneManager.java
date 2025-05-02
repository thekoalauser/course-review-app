package edu.virginia.sde.reviews;

import edu.virginia.sde.reviews.model.Course;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Stack;

/**
 * Utility class for managing scene transitions in the application.
 * Handles loading FXML files and setting up scene content.
 */
public class SceneManager {
    private static final double SCENE_WIDTH = 800;
    private static final double SCENE_HEIGHT = 600;
    
    // Navigation history stack to track screens for back button functionality
    private static final Stack<SceneInfo> navigationHistory = new Stack<>();
    
    /**
     * A class to store information about scenes for navigation history
     */
    private static class SceneInfo {
        private final String fxmlPath;
        private final String title;
        private final Course course; // For course review scenes
        
        // Constructor for regular scenes
        public SceneInfo(String fxmlPath, String title) {
            this.fxmlPath = fxmlPath;
            this.title = title;
            this.course = null;
        }
        
        // Constructor for course review scenes
        public SceneInfo(String fxmlPath, String title, Course course) {
            this.fxmlPath = fxmlPath;
            this.title = title;
            this.course = course;
        }
    }

    /**
     * Switches to the login scene.
     * This is the entry point of the application.
     * 
     * @param stage The main application stage
     */
    public static void switchToLoginScene(Stage stage) {
        try {
            // Clear navigation history when switching to login
            navigationHistory.clear();
            
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("login-scene.fxml"));
            Scene scene = new Scene(loader.load(), SCENE_WIDTH, SCENE_HEIGHT);
            stage.setScene(scene);
            stage.setTitle("UVA Course Reviews - Login");
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Error loading login scene: " + e.getMessage());
        }
    }

    /**
     * Switches to the home scene.
     * This is the main navigation hub after login.
     * 
     * @param stage The main application stage
     */
    public static void switchToHomeScene(Stage stage) {
        try {
            // Store current scene in navigation history if not going to login
            if (!stage.getTitle().contains("Login")) {
                pushCurrentSceneToHistory(stage);
            } else {
                // Clear navigation history when coming from login
                navigationHistory.clear();
            }
            
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("home-scene.fxml"));
            Scene scene = new Scene(loader.load(), SCENE_WIDTH, SCENE_HEIGHT);
            stage.setScene(scene);
            stage.setTitle("UVA Course Reviews - Home");
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Error loading home scene: " + e.getMessage());
        }
    }

    /**
     * Switches to the course search scene.
     * This scene will be implemented by another team member.
     * 
     * @param stage The main application stage
     */
    public static void switchToCourseSearchScene(Stage stage) {
        try {
            // Store current scene in navigation history
            pushCurrentSceneToHistory(stage);
            
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("course-search-scene.fxml"));
            Scene scene = new Scene(loader.load(), SCENE_WIDTH, SCENE_HEIGHT);
            stage.setScene(scene);
            stage.setTitle("UVA Course Reviews - Browse Courses");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Error with course search scene: " + e.getMessage());
        }
    }


    /**
     * Switches to the course review scene.
     * Displays reviews for a specific course and allows adding/editing reviews.
     *
     * @param stage The main application stage
     * @param course The course to display reviews for
     */
    public static void switchToCourseReviewScene(Stage stage, Course course) {
        try {
            // Store current scene in navigation history
            pushCurrentSceneToHistory(stage);
            
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("course-review-scene.fxml"));
            Scene scene = new Scene(loader.load(), SCENE_WIDTH, SCENE_HEIGHT);
            
            CourseReviewController controller = loader.getController();
            controller.initData(course);

            stage.setScene(scene);
            stage.setTitle("UVA Course Reviews - " + course.toString());
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Error loading course review scene: " + e.getMessage());
        }
    }
 
    /**
     * Switches to the my reviews scene.
     * Displays all reviews created by the current user.
     * 
     * @param stage The main application stage
     */
    public static void switchToMyReviewsScene(Stage stage) {
        try {
            // Store current scene in navigation history
            pushCurrentSceneToHistory(stage);
            
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("my-reviews-scene.fxml"));
            Scene scene = new Scene(loader.load(), SCENE_WIDTH, SCENE_HEIGHT);
            stage.setScene(scene);
            stage.setTitle("UVA Course Reviews - My Reviews");
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Error loading my reviews scene: " + e.getMessage());
        }
    }
    
    /**
     * Goes back to the previous scene in the navigation history.
     * If the history is empty, returns to the home scene.
     * 
     * @param stage The main application stage
     */
    public static void goBack(Stage stage) {
        if (navigationHistory.isEmpty()) {
            // If no history, go to home screen
            switchToHomeScene(stage);
            return;
        }
        
        try {
            SceneInfo previousScene = navigationHistory.pop();
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(previousScene.fxmlPath));
            Scene scene = new Scene(loader.load(), SCENE_WIDTH, SCENE_HEIGHT);
            
            // If returning to a course review scene, we need to initialize the controller
            if (previousScene.course != null && previousScene.fxmlPath.contains("course-review-scene")) {
                CourseReviewController controller = loader.getController();
                controller.initData(previousScene.course);
            }
            
            stage.setScene(scene);
            stage.setTitle(previousScene.title);
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Error navigating back: " + e.getMessage());
            // If error occurs, try to go to home as fallback
            switchToHomeScene(stage);
        }
    }
    
    /**
     * Pushes the current scene information to the navigation history stack.
     * 
     * @param stage The current stage with the scene to store
     */
    private static void pushCurrentSceneToHistory(Stage stage) {
        String currentTitle = stage.getTitle();
        String fxmlPath;
        Course course = null;
        
        // Determine which scene we're currently on
        if (currentTitle.contains("Login")) {
            fxmlPath = "login-scene.fxml";
        } else if (currentTitle.contains("Home")) {
            fxmlPath = "home-scene.fxml";
        } else if (currentTitle.contains("Browse Courses")) {
            fxmlPath = "course-search-scene.fxml";
        } else if (currentTitle.contains("My Reviews")) {
            fxmlPath = "my-reviews-scene.fxml";
        } else {
            // This is likely a course review scene, we need to get the course from the controller
            fxmlPath = "course-review-scene.fxml";
            // We can't easily get the course here, so we'll handle this separately
        }
        
        navigationHistory.push(new SceneInfo(fxmlPath, currentTitle, course));
    }

    /**
     * Displays an information alert.
     *
     * @param stage The owner stage
     * @param title The alert title
     * @param message The alert message
     */
    private static void showAlert(Stage stage, String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(stage);
        alert.showAndWait();
    }

    /**
     * Displays an error message in the console.
     * 
     * @param message The error message to display
     */
    private static void showErrorMessage(String message) {
        System.err.println("Scene Manager Error: " + message);
    }
} 