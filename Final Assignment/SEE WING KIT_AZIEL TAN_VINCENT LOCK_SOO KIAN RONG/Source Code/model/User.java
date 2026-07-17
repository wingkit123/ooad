package model;

import java.io.Serializable;

/**
 * (Code Explanation - Data Model & State):
 * Plain Old Java Object (POJO) representing a system user. 
 * Implements Serializable for saving state. Contains an inner Enum (UserType) 
 * used extensively by BillingManager to calculate role-based discounts.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum UserType implements Serializable {
        STUDENT,
        STAFF,
        FINAL_YEAR_STUDENT;

        private static final long serialVersionUID = 1L;
    }

    private String userId;
    private String name;
    private UserType type;

    public User(String userId, String name, UserType type) {
        this.userId = userId;
        this.name = name;
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public UserType getType() {
        return type;
    }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}
