package org.jala.university.presentation.util;

import org.jala.university.application.dto.UserDTO;

/**
 * Utility class for managing user sessions.
 * Implements the Singleton pattern.
 */
public class SessionManager {
    
    private static SessionManager instance;
    private UserDTO currentUser;
    private String token;
    
    /**
     * Private constructor to enforce Singleton pattern.
     */
    private SessionManager() {}
    
    /**
     * Gets the singleton instance of SessionManager.
     * 
     * @return the SessionManager instance
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    /**
     * Sets the current user.
     * 
     * @param user the current user
     */
    public void setCurrentUser(UserDTO user) {
        this.currentUser = user;
    }
    
    /**
     * Gets the current user.
     * 
     * @return the current user
     */
    public UserDTO getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Gets the current user's ID.
     * 
     * @return the current user's ID or null if no user is logged in
     */
    public Long getCurrentUserId() {
        return currentUser != null ? currentUser.getId() : null;
    }
    
    /**
     * Sets the authentication token.
     * 
     * @param token the authentication token
     */
    public void setToken(String token) {
        this.token = token;
    }
    
    /**
     * Gets the authentication token.
     * 
     * @return the authentication token
     */
    public String getToken() {
        return token;
    }
    
    /**
     * Clears the current session.
     */
    public void clearSession() {
        currentUser = null;
        token = null;
    }
    
    /**
     * Checks if a user is logged in.
     * 
     * @return true if a user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return currentUser != null && token != null;
    }
}