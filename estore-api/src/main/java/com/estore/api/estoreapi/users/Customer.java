package com.estore.api.estoreapi.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.estore.api.estoreapi.products.Beef;

public class Customer extends User {

    @JsonProperty("cart") private final Beef[] cart;

    public Customer(
        @JsonProperty("username") String username,
        @JsonProperty("cart") Beef[] cart
        ) {
        super(username);

        this.cart = cart;
    }

    public boolean isAdmin(){
        return false;
    }

}
