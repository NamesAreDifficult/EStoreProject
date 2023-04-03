package com.estore.api.estoreapi.users;

import java.util.ArrayList;

import com.estore.api.estoreapi.products.CartBeef;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ShoppingCart {

    @JsonProperty("cart")
    private CartBeef[] cart;

    /*
     * Constructs a shopping cart using a {@linkplain CartBeef cartBeef}
     * object
     */
    public ShoppingCart(@JsonProperty("cart") CartBeef[] cart) {
        this.cart = cart;
    }

    /*
     * Constructs a blank shopping cart
     */
    public ShoppingCart() {
        this.cart = new CartBeef[0];
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
            if (currentBeef.equals(cartBeef)) {
                return false;
            }

            newCart[newCartIndex] = currentBeef;
            newCartIndex += 1;
        }

        newCart[newCartIndex] = cartBeef;
        this.cart = newCart;
        return true;
    }

    public CartBeef[] getContents() {
        return this.cart;
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
        ArrayList<CartBeef> newCart = new ArrayList<>();

        for (CartBeef cartBeef : this.cart) {
            if (cartBeef.getId() != id) {
                newCart.add(cartBeef);
            }
        }

        boolean result = newCart.size() != this.cart.length;

        // Copies the new array list to the final cart array
        if (result) {
            CartBeef[] finalCart = new CartBeef[this.cart.length - 1];
            int index = 0;
            for (CartBeef cartBeef : newCart) {
                finalCart[index] = cartBeef;
                index++;
            }
            this.cart = finalCart;
        }
        return result;
    }

    public CartBeef getCartBeef(int id){
        for (CartBeef cartBeef : this.cart) {
            if (cartBeef.getId() == id) {
                return cartBeef;
            }
        }
        return null;
    }

    /*
     * Clears the contents of the customer's cart
     */
    public void clearCart() {
        this.cart = new CartBeef[0];
    }

}
