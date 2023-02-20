package com.estore.api.estoreapi.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.estore.api.estoreapi.products.Beef;

/*
 * Represents a Customer User
 * 
 * @author Brendan Battisti
 */
public class Customer extends User {

    @JsonProperty("cart") private final Beef[] cart;

    /**
   * Create a customer with a username
   * @param username The username of the customer
   * 
   * {@literal @}JsonProperty is used in serialization and deserialization
   * of the JSON object to the Java object in mapping the fields.  If a field
   * is not provided in the JSON object, the Java field gets the default Java
   * value, i.e. 0 for int
   */
    public Customer(
        @JsonProperty("username") String username,
        @JsonProperty("cart") Beef[] cart
        ) {
        super(username);

        this.cart = cart;
    }

    /*
     * Returns the Customer's cart
     * 
     * @return Contents of the customer's cart
     */
    public Beef[] getCart() {
        return this.cart;
    }

    /*
     * Adds a product to the customer's cart
     * 
     * @param beef beef to be added to the cart
     */
    public void addToCart(Beef beef) {
        // Todo Implement function
    }

    /*
     * Clears the contents of the customer's cart
     */
    public void clearCart() {
        // Todo Implement function
    }

    /**
     ** {@inheritDoc}
    */
    public boolean isAdmin() {
        return false;
    }

}
