package com.estore.api.estoreapi.users;

import java.util.Arrays;
import java.util.ArrayList;
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

    /*
     * Returns an array containing all the customer's credit cards
     * 
     * @return The customer's credit cards
     */
    public CreditCard[] getCards(){
        return this.cards;
    }

    /*
     * Returns a credit card with the specified number if a credit card exists, otherwise null
     * 
     * @param number: The credit card number for retrival from customer
     * 
     * @return The customer's credit card with the number, or null
     */
    public CreditCard getCard(String number){
        for (CreditCard creditCard : this.cards){
            if (creditCard.getNumber().equals(number)){
                return creditCard;
            }
        }
        return null;
    }

    /*
     * Adds a credit card to the customer's card list if there is space
     * 
     * The credit card object to be added to the card list
     * 
     * @return boolean of whether the addition was successful
     */
    public boolean addCard(CreditCard card){
        if (cards.length >= 3){
            return false;
        }
        CreditCard[] newCards = new CreditCard[this.cards.length + 1];
        int newCardIndex = 0;
        for (CreditCard creditCard : this.cards) {
            if (creditCard.equals(card)) {
                return false;
            }

            newCards[newCardIndex] = creditCard;
            newCardIndex += 1;
        }

        newCards[newCardIndex] = card;
        this.cards = newCards;
        return true;
    }
    
    /*
     * Removes a credit card to the customer's card list if it exists
     * 
     * @param card: The credit card object to be removed
     * 
     * @return True if card is present and removed, false otherwise
     */
    public boolean removeCard(CreditCard card){
        if (this.cards.length == 0){
            return false;
        }
        ArrayList<CreditCard> newCards = new ArrayList<>();

        for (CreditCard creditCard : this.cards) {
            if (!creditCard.equals(card)) {
                newCards.add(creditCard);
            }
        }

        boolean result = newCards.size() != this.cards.length;

        if (result) {
            CreditCard[] finalCards = new CreditCard[this.cards.length - 1];
            int index = 0;
            for (CreditCard creditCard : newCards) {
                finalCards[index] = creditCard;
                index++;
            }
            this.cards = finalCards;
        }
        return result;
    }

    /**
     ** {@inheritDoc}
     */
    public boolean isAdmin() {
        return false;
    }

}
