package edu.virginia.sde.reviews;

import edu.virginia.sde.reviews.model.User;

/**
 * Session Manager for the UVA Course Reviews application.
 * 
 * This singleton class maintains the state of the current user session,
 * providing a centralized way to track the logged-in user across
 * different parts of the application.
 * 
 * Key responsibilities:
 * - Store the currently authenticated user
 * - Provide access to the current user information
 * - Handle user logout
 */
public class SessionManager {
    /** Singleton instance of the SessionManager */
    private static SessionManager instance;
    
    /** The currently logged-in user, or null if no user is logged in */
    private User currentUser;

    /**
     * Private constructor to enforce singleton pattern.
     * Use getInstance() to access the SessionManager.
     */
    private SessionManager() {
        // Private constructor to enforce singleton pattern
    }

    /**
     * Gets the singleton instance of the SessionManager.
     * Creates a new instance if one does not exist.
     * 
     * @return The singleton SessionManager instance
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Gets the currently logged-in user.
     * 
     * @return The current User object, or null if no user is logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the currently logged-in user.
     * Called after successful authentication.
     * 
     * @param currentUser The authenticated user to set as current
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Checks if a user is currently logged in.
     * 
     * @return true if a user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Logs out the current user by clearing the currentUser reference.
     * After calling this method, isLoggedIn() will return false.
     */
    public void logout() {
        currentUser = null;
    }
} 