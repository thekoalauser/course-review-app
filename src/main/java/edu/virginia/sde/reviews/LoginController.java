package edu.virginia.sde.reviews;

import edu.virginia.sde.reviews.dao.UserDAO;
import edu.virginia.sde.reviews.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controller for the login scene of the UVA Course Reviews application.
 * Handles user authentication, new account creation, and application closure.
 * 
 * This controller manages two main functions:
 * 1. Authenticating existing users
 * 2. Registering new users with validation
 */
public class LoginController {
    // Login tab UI components
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label loginErrorLabel;
    @FXML
    private Button loginButton;
    
    // Create account tab UI components
    @FXML
    private TextField newUsernameField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label createAccountErrorLabel;
    @FXML
    private Button createAccountButton;
    
    // Common UI components
    @FXML
    private Button closeButton;

    /** Data Access Object for user operations */
    private UserDAO userDAO;

    /**
     * Initializes the controller.
     * This method is automatically called after the FXML is loaded.
     * Sets up event handlers and initializes UI components.
     */
    public void initialize() {
        userDAO = new UserDAO();
        loginErrorLabel.setText("");
        createAccountErrorLabel.setText("");
        
        // Configure button event handlers
        loginButton.setOnAction(event -> handleLogin());
        createAccountButton.setOnAction(event -> handleCreateAccount());
        closeButton.setOnAction(event -> handleClose());
    }

    /**
     * Handles the login button click event.
     * Validates credentials and authenticates the user.
     * If successful, stores the user in the session.
     */
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        
        // Validate input fields
        if (username.isEmpty() || password.isEmpty()) {
            loginErrorLabel.setText("Username and password are required");
            return;
        }
        
        // Authenticate user
        if (userDAO.authenticateUser(username, password)) {
            User user = userDAO.getUserByUsername(username);
            SessionManager.getInstance().setCurrentUser(user);
            
            // Show success message
            showAlert("Login Successful", "Welcome, " + username + "!");
            
            // Note: Navigation to next scene would normally happen here
            // but is removed as course search scene is not implemented
        } else {
            loginErrorLabel.setText("Invalid username or password");
        }
    }

    /**
     * Handles the account creation button click event.
     * Validates input and creates a new user account if validation passes.
     * Requirements:
     * - Username must be unique
     * - Password must be at least 8 characters
     * - Password and confirmation must match
     */
    private void handleCreateAccount() {
        String username = newUsernameField.getText().trim();
        String password = newPasswordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        
        createAccountErrorLabel.setText("");
        
        // Input validation
        if (username.isEmpty()) {
            createAccountErrorLabel.setText("Username is required");
            return;
        }
        
        if (password.isEmpty()) {
            createAccountErrorLabel.setText("Password is required");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            createAccountErrorLabel.setText("Passwords do not match");
            return;
        }
        
        if (password.length() < 8) {
            createAccountErrorLabel.setText("Password must be at least 8 characters");
            return;
        }
        
        // Check username uniqueness
        if (userDAO.getUserByUsername(username) != null) {
            createAccountErrorLabel.setText("Username already exists");
            return;
        }
        
        // Create the user account
        User newUser = new User(username, password);
        if (userDAO.createUser(newUser)) {
            showAlert("Account Created", "Your account has been created successfully. Please log in.");
            clearCreateAccountFields();
        } else {
            createAccountErrorLabel.setText("Failed to create account. Please try again.");
        }
    }

    /**
     * Handles the close button click event.
     * Closes the application.
     */
    private void handleClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Clears all input fields in the account creation form.
     * Called after successful account creation.
     */
    private void clearCreateAccountFields() {
        newUsernameField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
    }

    /**
     * Displays an information alert with the specified title and message.
     *
     * @param title The title of the alert
     * @param message The message to display in the alert
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 
