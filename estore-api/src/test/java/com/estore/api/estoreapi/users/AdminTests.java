package com.estore.api.estoreapi.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdminTests {

    @Test
    public void testCtor() {

        Admin testAdmin = new Admin("Test");

        assertNotNull(testAdmin, () -> "The admin should not be null");
    }

    @Test
    public void testIsAdmin() {
        Admin testAdmin = new Admin("Test");

        assertTrue(testAdmin.isAdmin(), "The admin should be an admin");
    }

    @Test
    public void testGetUsername() {
        Admin testAdmin = new Admin("Test");

        assertEquals("Test", testAdmin.getUsername());
    }
}
