package com.estore.api.estoreapi.users;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.estore.api.estoreapi.products.CartBeef;

/*
 * Represents a Customer User
 *  
 * @author Brendan Battisti
 */
public class Customer extends User {

    @JsonProperty("cart")
    private CartBeef[] cart;

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
            @JsonProperty("cart") CartBeef[] cart) {
        super(username);

        this.cart = cart;
    }

    /*
     * Returns the Customer's cart
     * 
     * @return Contents of the customer's cart
     */
    public CartBeef[] getCart() {
        return this.cart;
    }

    /*
     * Adds a product to the customer's cart
     * 
     * @param cartBeef beef to be added to the cart
     * 
     * @return boolean depending on whether or no the addition was
     * successful
     */
    public boolean addToCart(CartBeef cartBeef) {

        CartBeef[] newCart = new CartBeef[this.cart.length + 1];
        int newCartIndex = 0;
        for (CartBeef currentBeef : this.cart) {
            if (currentBeef.getId() == cartBeef.getId()) {
                return false;
            }

            newCart[newCartIndex] = currentBeef;
            newCartIndex += 1;
        }

        newCart[newCartIndex] = cartBeef;
        this.cart = newCart;
        return true;
    }

    /*
     * Removes a product from the customer's cart
     * 
     * @param id id of the beef to be removed from the cart
     * 
     * @return boolean depending on whether or no the removal was
     * successful
     */
    public boolean removeFromCart(int id) {

        CartBeef[] newCart = new CartBeef[this.cart.length - 1];
        int newCartIndex = 0;
        boolean removed = false;
        for (CartBeef currentBeef : this.cart) {
            if (currentBeef.getId() != id) {
                newCart[newCartIndex] = currentBeef;
                newCartIndex += 1;
            } else {
                removed = true;
            }

        }
        this.cart = newCart;
        return removed;
    }

    /*
     * Clears the contents of the customer's cart
     */
    public void clearCart() {
        this.cart = new CartBeef[0];
    }

    /**
     ** {@inheritDoc}
     */
    public boolean isAdmin() {
        return false;
    }

}
