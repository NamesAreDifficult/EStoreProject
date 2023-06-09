package com.estore.api.estoreapi.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
class CustomerTests {

  @Test
  void testCtor() {

    Customer testCustomer = new Customer("Test", "password", new ShoppingCart());

    assertNotNull(testCustomer, () -> "The customer should not be null");
  }

  @Test
  void testIsAdmin() {
    Customer testCustomer = new Customer("Test", "password", new ShoppingCart());

    assertFalse(testCustomer.isAdmin(), "The customer should not be an admin");
  }

  @Test
  void testGetUsername() {
    Customer testCustomer = new Customer("Test", "password", new ShoppingCart());

    assertEquals("Test", testCustomer.getUsername());
  }

  @Test
  void testAddCard(){
    Customer testCustomer = new Customer("Test", "password", new ShoppingCart());
    boolean result = testCustomer.addCard(new CreditCard("1234567890123456", "4/20", "420"));
    assertTrue(result);
  }

  @Test
  void testAddCardMax(){
    Customer testCustomer = new Customer("Test", "password", new ShoppingCart());
    testCustomer.addCard(new CreditCard("1234567890123456", "4/20", "420"));
    testCustomer.addCard(new CreditCard("1234567890123457", "4/20", "420"));
    testCustomer.addCard(new CreditCard("1234567890123458", "4/20", "420"));
    boolean result = testCustomer.addCard(new CreditCard("1234567890123459", "4/20", "420"));
    assertFalse(result);
  }

  @Test
  void testAddCardDuplicate(){
    Customer testCustomer = new Customer("Test", "password", new ShoppingCart());
    testCustomer.addCard(new CreditCard("1234567890123456", "4/20", "420"));
    boolean result = testCustomer.addCard(new CreditCard("1234567890123456", "4/20", "420"));
    assertFalse(result);
  }

  @Test
  void testGetCard(){
    Customer testCustomer = new Customer("Test", "password", new ShoppingCart());
    CreditCard testCard = new CreditCard("1234567890123456", "4/20", "420");
    testCustomer.addCard(testCard);
    assertEquals(testCard, testCustomer.getCard(testCard.getNumber()));
  }

  @Test
  void testRemoveCard(){
    Customer testCustomer = new Customer("Test", "password", new ShoppingCart());
    CreditCard testCard = new CreditCard("1234567890123456", "4/20", "420");
    CreditCard testCard2 = new CreditCard("0987654321234567","01/23", "232");
    testCustomer.addCard(testCard);
    testCustomer.addCard(testCard2);
    boolean result = testCustomer.removeCard(testCard);
    assertTrue(result);
  }

  @Test
  void testRemoveCardAbsent(){
    Customer testCustomer = new Customer("Test", "password", new ShoppingCart());
    CreditCard testCard = new CreditCard("1234567890123456", "4/20", "420");
    boolean result = testCustomer.removeCard(testCard);
    assertFalse(result);
  }

}

