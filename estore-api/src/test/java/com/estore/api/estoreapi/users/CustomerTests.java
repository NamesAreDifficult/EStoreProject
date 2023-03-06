package com.estore.api.estoreapi.users;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.products.CartBeef;

@Tag("Model-tier")
public class CustomerTests {

    @Test
    public void testCtor() {

        Customer testCustomer = new Customer("Test", new CartBeef[0]);

        assertNotNull(testCustomer, () -> "The customer should not be null");
    }

    @Test
    public void testIsAdmin() {
        Customer testCustomer = new Customer("Test", new CartBeef[0]);

        assertFalse(testCustomer.isAdmin(), "The customer should not be an admin");
    }

    @Test
    public void testGetUsername() {
        Customer testCustomer = new Customer("Test", new CartBeef[0]);

        assertEquals("Test", testCustomer.getUsername());
    }

    @Test
    public void testGetCart() {
        // Dummy shopping cart
        CartBeef[] shoppingCart = new CartBeef[2];
        shoppingCart[0] = new CartBeef(1, 1);
        shoppingCart[1] = new CartBeef(2, 1);

        Customer testCustomer = new Customer("Test", shoppingCart);

        assertArrayEquals(shoppingCart, testCustomer.getCart());

    }

    @Test
    public void testAddToCart() {
        // Dummy shopping cart
        CartBeef[] shoppingCart = new CartBeef[2];
        shoppingCart[0] = new CartBeef(1, 1);
        shoppingCart[1] = new CartBeef(2, 2);

        Customer testCustomer = new Customer("Test", new CartBeef[0]);

        // Asserts
        assertTrue(testCustomer.addToCart(new CartBeef(1, 1)), "Add to cart should be true");
        assertTrue(testCustomer.addToCart(new CartBeef(2, 1)), "Add to cart should be true");
        assertArrayEquals(shoppingCart, testCustomer.getCart());

    }

    @Test
    public void testFailAddToCart() {
        CartBeef[] shoppingCart = new CartBeef[1];

        shoppingCart[0] = new CartBeef(1, 1);

        Customer testCustomer = new Customer("Test", new CartBeef[0]);

        // Asserts
        assertTrue(testCustomer.addToCart(new CartBeef(1, 1)), "Add to cart should be true");

        // Checks that you cannot add a duplicate beef
        assertFalse(testCustomer.addToCart(new CartBeef(1, 2)), "Add to cart should be true");

        assertArrayEquals(shoppingCart, testCustomer.getCart());

    }

    @Test
    public void testClearCart() {

        // Dummy shopping cart
        CartBeef[] shoppingCart = new CartBeef[1];
        shoppingCart[0] = new CartBeef(1, 1);

        Customer testCustomer = new Customer("Test", shoppingCart);

        testCustomer.clearCart();

        assertArrayEquals(new CartBeef[0], testCustomer.getCart());

    }

    @Test
    public void testRemoveFromCart() {
        CartBeef[] shoppingCart = new CartBeef[2];

        shoppingCart[0] = new CartBeef(1, 1);
        shoppingCart[1] = new CartBeef(2, 1);

        CartBeef[] removedShoppingCart = new CartBeef[1];

        removedShoppingCart[0] = new CartBeef(2, 1);

        Customer testCustomer = new Customer("Test", shoppingCart);

        // Asserts
        assertTrue(testCustomer.removeFromCart(1));

        // Tests removing a CartBeef that cannot be found
        assertFalse(testCustomer.removeFromCart(1));

        // Tests that the shopping cart looks as it should
        assertArrayEquals(removedShoppingCart, testCustomer.getCart());

    }

}
