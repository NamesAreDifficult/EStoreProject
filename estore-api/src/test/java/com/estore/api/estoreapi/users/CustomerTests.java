package com.estore.api.estoreapi.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
public class CustomerTests {

    @Test
    public void testCtor() {

        Customer testCustomer = new Customer("Test", new ShoppingCart());

        assertNotNull(testCustomer, () -> "The customer should not be null");
    }

    @Test
    public void testIsAdmin() {
        Customer testCustomer = new Customer("Test", new ShoppingCart());

        assertFalse(testCustomer.isAdmin(), "The customer should not be an admin");
    }

    @Test
    public void testGetUsername() {
        Customer testCustomer = new Customer("Test", new ShoppingCart());

        assertEquals("Test", testCustomer.getUsername());
    }

}
