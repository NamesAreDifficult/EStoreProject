package com.estore.api.estoreapi.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
class AdminTests {

  @Test
  void testCtor() {

    Admin testAdmin = new Admin("Test", "password");

    assertNotNull(testAdmin, () -> "The admin should not be null");
  }

  @Test
  void testIsAdmin() {
    Admin testAdmin = new Admin("Test", "password");

    assertTrue(testAdmin.isAdmin(), "The admin should be an admin");
  }

  @Test
  void testGetUsername() {
    Admin testAdmin = new Admin("Test", "password");

    assertEquals("Test", testAdmin.getUsername());
  }

  @Test
  void testHashCode(){
    Admin testAdmin = new Admin("Test", "password");

    assertEquals(2031001933, testAdmin.hashCode());
  }
}
