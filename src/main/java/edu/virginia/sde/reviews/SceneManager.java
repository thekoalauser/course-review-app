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
     * Switches to the course review scene.
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
     * Displays an error message in the console.
     * 
     * @param message The error message to display
     */
    private static void showErrorMessage(String message) {
        System.err.println("Scene Manager Error: " + message);
    }
} 