package edu.virginia.sde.reviews.dao;

import edu.virginia.sde.reviews.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object for handling user-related database operations.
 * 
 * This class provides CRUD operations for User entities:
 * - Create: Register new users
 * - Read: Retrieve user information
 * - Update: (not implemented in this version)
 * - Delete: (not implemented in this version)
 * 
 * It also handles user authentication for the login process.
 */
public class UserDAO {
    /** SQLite database connection URL */
    private static final String DB_URL = "jdbc:sqlite:course_reviews.db";

    /**
     * Retrieves a user by username.
     *
     * @param username The username to search for
     * @return User object if found, null otherwise
     */
    public User getUserByUsername(String username) {
        String sql = "SELECT id, username, password FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String password = rs.getString("password");
                return new User(id, username, password);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user: " + e.getMessage());
        }

        return null;
    }

    /**
     * Creates a new user in the database.
     * 
     * @param user The User object containing username and password
     * @return true if user was created successfully, false otherwise
     */
    public boolean createUser(User user) {
        String sql = "INSERT INTO users(username, password) VALUES(?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error creating user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Authenticates a user with the given username and password.
     * 
     * @param username The username to authenticate
     * @param password The password to verify
     * @return true if authentication is successful, false otherwise
     */
    public boolean authenticateUser(String username, String password) {
        User user = getUserByUsername(username);
        
        if (user != null) {
            return user.getPassword().equals(password);
        }
        
        return false;
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id The user ID to search for
     * @return User object if found, null otherwise
     */
    public User getUserById(int id) {
        String sql = "SELECT id, username, password FROM users WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                return new User(id, username, password);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user: " + e.getMessage());
        }

        return null;
    }
} 