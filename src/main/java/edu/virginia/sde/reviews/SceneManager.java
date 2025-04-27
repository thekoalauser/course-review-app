package edu.virginia.sde.reviews;

import edu.virginia.sde.reviews.model.Course;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Utility class for managing scene transitions in the application.
 * Handles loading FXML files and setting up scene content.
 */
public class SceneManager {
    private static final double SCENE_WIDTH = 800;
    private static final double SCENE_HEIGHT = 600;

    /**
     * Switches to the login scene.
     * This is the entry point of the application.
     * 
     * @param stage The main application stage
     */
    public static void switchToLoginScene(Stage stage) {
        try {
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
     * Switches to the course browse scene.
     * Displays a list of all courses for the user to select from.
     * 
     * @param stage The main application stage
     */
    public static void switchToCourseBrowseScene(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("course-browse-scene.fxml"));
            Scene scene = new Scene(loader.load(), SCENE_WIDTH, SCENE_HEIGHT);
            stage.setScene(scene);
            stage.setTitle("UVA Course Reviews - Browse Courses");
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Error loading course browse scene: " + e.getMessage());
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