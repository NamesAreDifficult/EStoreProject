package com.estore.api.estoreapi.users;

import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Represents a Customer User
 *  
 * @author Brendan Battisti
 */
public class Customer extends User {

    @JsonProperty("cart")
    private ShoppingCart cart;

    @JsonProperty("cards")
    private CreditCard[] cards;

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
            @JsonProperty("cart") ShoppingCart cart) {
        super(username);
        this.cards = new CreditCard[0];
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

    /*
     * Adds a credit card to the customer's card list if there is space
     * 
     * @return boolean of whether the addition was successful
     */
    public boolean addCard(CreditCard card){
        if (cards.length == 3){
            return false;
        }
        else{
            this.cards = Arrays.copyOf(cards, cards.length + 1);
            this.cards[cards.length - 1] = card;
            return true;
        }
    }

    /**
     ** {@inheritDoc}
     */
    public boolean isAdmin() {
        return false;
    }

}
