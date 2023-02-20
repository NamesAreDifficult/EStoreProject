package com.estore.api.estoreapi.users;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class User {
    @JsonProperty("username") private final String username;

    public User(
        @JsonProperty("username") String username
        ) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public abstract boolean isAdmin();
}