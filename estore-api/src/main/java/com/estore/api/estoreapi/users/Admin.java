package com.estore.api.estoreapi.users;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Admin extends User {


    public Admin(@JsonProperty("username") String username) {
        super(username);
    }

    public boolean isAdmin(){
        return true;
    }

}
