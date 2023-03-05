package com.estore.api.estoreapi.users;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.estore.api.estoreapi.products.CartBeef;

@SpringBootTest
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
        CartBeef[] shoppingCart = new CartBeef[2];

        shoppingCart[0] = new CartBeef(1, 1);
        shoppingCart[1] = new CartBeef(2, 1);

        Customer testCustomer = new Customer("Test", shoppingCart);

        assertEquals(shoppingCart, testCustomer.getCart());

    }

    @Test
    public void testAddToCart() {
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
        assertFalse(testCustomer.addToCart(new CartBeef(1, 2)), "Add to cart should be true");
        assertArrayEquals(shoppingCart, testCustomer.getCart());

    }

    @Test
    public void testClearCart() {
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
        assertFalse(testCustomer.removeFromCart(1));
        assertArrayEquals(removedShoppingCart, testCustomer.getCart());

    }

}
