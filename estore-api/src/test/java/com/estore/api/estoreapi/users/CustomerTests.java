package com.estore.api.estoreapi.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

}
