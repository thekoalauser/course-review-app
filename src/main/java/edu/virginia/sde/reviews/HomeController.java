package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controller for the home page scene of the UVA Course Reviews application.
 * This page serves as the main navigation hub after login, providing access to:
 * - Course Search
 * - Course Reviews
 * - My Reviews
 */
public class HomeController {
    /** Label displaying welcome message with current username */
    @FXML
    private Label welcomeLabel;
    
    /** Button to navigate to the course search feature */
    @FXML
    private Button courseSearchButton;
    
    /** Button to navigate to the user's own reviews */
    @FXML
    private Button myReviewsButton;
    
    /** Button to log out of the application */
    @FXML
    private Button logoutButton;

    /**
     * Initializes the controller.
     * This method is automatically called after the FXML is loaded.
     * Sets up the welcome message and configures button event handlers.
     */
    public void initialize() {
        // Set welcome message with username if user is logged in
        if (SessionManager.getInstance().isLoggedIn()) {
            String username = SessionManager.getInstance().getCurrentUser().getUsername();
            welcomeLabel.setText("Welcome, " + username);
        } else {
            welcomeLabel.setText("Welcome, Guest");
        }
        
        // Set up button event handlers
        courseSearchButton.setOnAction(event -> handleCourseSearchButton());
        myReviewsButton.setOnAction(event -> handleMyReviewsButton());
        logoutButton.setOnAction(event -> handleLogoutButton());
    }
    
    /**
     * Handles the course search button click.
     * Navigates to the course search screen.
     */
    private void handleCourseSearchButton() {
        Stage stage = (Stage) courseSearchButton.getScene().getWindow();
        SceneManager.switchToCourseSearchScene(stage);
    }

    /**
     * Handles the my reviews button click.
     * Navigates to the user's reviews screen.
     */
    private void handleMyReviewsButton() {
        Stage stage = (Stage) myReviewsButton.getScene().getWindow();
        SceneManager.switchToMyReviewsScene(stage);
    }
    
    /**
     * Handles the logout button click.
     * Logs out the current user and returns to the login screen.
     */
    private void handleLogoutButton() {
        // Clear the current user session
        SessionManager.getInstance().clearSession();
        
        // Return to login screen
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        SceneManager.switchToLoginScene(stage);
    }
} 