package com.estore.api.estoreapi.users;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Represents a User
 * 
 * @author Brendan Battisti
 */
public abstract class User {

    @JsonProperty("username")
    private final String username;

    /**
     * Create a user with a username
     * 
     * @param username The username of the user
     * 
     *                 {@literal @}JsonProperty is used in serialization and
     *                 deserialization
     *                 of the JSON object to the Java object in mapping the fields.
     *                 If a field
     *                 is not provided in the JSON object, the Java field gets the
     *                 default Java
     *                 value, i.e. 0 for int
     */
    public User(
            @JsonProperty("username") String username) {
        this.username = username;
    }

    /*
     * Gets the username of the user
     * 
     * @return username of the user
     */
    public String getUsername() {
        return this.username;
    }

    /*
     * Returns if the user is an admin
     * 
     * @return true if user is an admin
     */
    @JsonProperty("admin")
    public abstract boolean isAdmin();
}