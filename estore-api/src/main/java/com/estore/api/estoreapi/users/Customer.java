package com.estore.api.estoreapi.users;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Represents a Customer User
 *  
 * @author Brendan Battisti
 */
public class Customer extends User {

    @JsonProperty("cart")
    private ShoppingCart cart;

    /**
     * Create a customer with a username
     * 
     * @param username The username of the customer
     * 
     *                 {@literal @}JsonProperty is used in serialization and
     *                 deserialization
     *                 of the JSON object to the Java object in mapping the fields.
     *                 If a field
     *                 is not provided in the JSON object, the Java field gets the
     *                 default Java
     *                 value, i.e. 0 for int
     */
    public Customer(
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("cart") ShoppingCart cart) {
        super(username, password);

        if (cart == null) {
            this.cart = new ShoppingCart();
        } else {
            this.cart = cart;

        }
    }

    /*
     * Returns the Customer's cart
     * 
     * @return Contents of the customer's cart
     */
    public ShoppingCart getCart() {
        return this.cart;
    }

    /**
     ** {@inheritDoc}
     */
    public boolean isAdmin() {
        return false;
    }

}
