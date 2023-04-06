package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
//import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import com.estore.api.estoreapi.persistence.InventoryDAO;
import com.estore.api.estoreapi.persistence.UserDAO;
import com.estore.api.estoreapi.products.Beef;
import com.estore.api.estoreapi.products.CartBeef;
import com.estore.api.estoreapi.users.Customer;
import com.estore.api.estoreapi.users.ShoppingCart;
import com.estore.api.estoreapi.users.CreditCard;

@Tag("Controller-tier")
class ShoppingControllerTests {
  private ShoppingController shoppingController;
  private UserDAO mockUserDAO;
  private InventoryDAO mockInventoryDAO;
  private Customer mockCustomer;
  private ShoppingCart mockShoppingCart;
  private CartBeef mockCartBeef;
  private Beef mockBeef;
  private CreditCard mockCreditCard;

  /**
   * Before each test, create a new ShoppingController object and inject
   * a mock User DAO and mockInventoryDAO
   * Mock out all other required objects:
   * Customer, ShoppingCart, CartBeef and Beef
   * Set standard behavior for some methods to persist over the majority of tests
   */

  @BeforeEach
  void setupShoppingController() throws IOException {
    // Create Mock Objects
    mockUserDAO = mock(UserDAO.class);
    mockInventoryDAO = mock(InventoryDAO.class);
    mockCustomer = mock(Customer.class);
    mockShoppingCart = mock(ShoppingCart.class);
    mockCartBeef = mock(CartBeef.class);
    mockBeef = mock(Beef.class);
    mockCreditCard = mock(CreditCard.class);

    // Set behaviors
    when(mockUserDAO.GetUser(anyString())).thenReturn(mockCustomer);
    when(mockCustomer.getCart()).thenReturn(mockShoppingCart);
    when(mockInventoryDAO.getBeef(anyInt())).thenReturn(mockBeef);
    when(mockShoppingCart.getContents()).thenReturn(new CartBeef[] { mockCartBeef });
    when(mockShoppingCart.removeFromCart(anyInt())).thenReturn(true);
    when(mockCartBeef.getId()).thenReturn(0);

    doNothing().when(mockBeef).setWeight(anyFloat());

    // Create controller
    shoppingController = new ShoppingController(mockInventoryDAO, mockUserDAO);
  }

  // Test normal function of get shopping cart
  @Test
  void testGetShoppingCart() {
    Beef[] expected = new Beef[] { mockBeef };

    ResponseEntity<Beef[]> response = shoppingController.getShoppingCart("TestCustomer");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    Beef[] shoppingCartBeefs = response.getBody();
    assertEquals(expected.length, response.getBody().length);
    for (int i = 0; i < expected.length; i++) {
      assertEquals(expected[i].getId(), shoppingCartBeefs[i].getId());
    }
  }

  // Test function of getShoppingCart when the user is not found
  @Test
  void testGetShoppingCartNotFound() throws IOException {
    when(mockUserDAO.GetUser(anyString())).thenReturn(null);

    ResponseEntity<Beef[]> response = shoppingController.getShoppingCart("TestCustomer");

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  // Test the function of getShoppingCart when the server has a read error
  @Test
  void testGetShoppingCartError() throws IOException {
    doThrow(new IOException("Failed to read from file"))
        .when(mockUserDAO)
        .GetUser(anyString());
    ResponseEntity<Beef[]> response = shoppingController.getShoppingCart("TestCustomer");

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  // Test the normal functionality of checkoutShoppingCart (Unimplemented)
  @Test
  void testCheckoutShoppingCart() throws IOException {
    when(mockUserDAO.Checkout(any(), any())).thenReturn(true);
    when(mockCustomer.getCard(anyString())).thenReturn(mockCreditCard);
    ResponseEntity<Boolean> response = shoppingController.CheckoutShoppingCart("TestCustomer", "1234567812345678");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody());
  }

  @Test
  void testBadCheckoutShoppingCart() throws IOException {
    when(mockUserDAO.Checkout(any(), any())).thenReturn(false);
    when(mockCustomer.getCard(anyString())).thenReturn(mockCreditCard);
    ResponseEntity<Boolean> response = shoppingController.CheckoutShoppingCart("TestCustomer", "1234567812345678");

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
  
  @Test
  void testUserNotFound() throws IOException{
    when(mockUserDAO.GetUser(anyString())).thenReturn(null);
    ResponseEntity<Boolean> response = shoppingController.CheckoutShoppingCart("TestCustomer", "1234567812345678");

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void testCheckoutError() throws IOException{
    doThrow(new IOException("Failed to read from file"))
        .when(mockUserDAO)
        .GetUser(anyString());
    ResponseEntity<Boolean> response = shoppingController.CheckoutShoppingCart("TestCustomer", "1234567812345678");
    
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }
  // Test the normal functionality of adding items to the shopping cart
  @Test 
  void testAddToShoppingCart() throws IOException{
    when(mockCartBeef.getWeight()).thenReturn((float)3);
    when(mockBeef.getWeight()).thenReturn((float)5);
    ResponseEntity<CartBeef> response = shoppingController.AddToShoppingCart("TestCustomer", mockCartBeef);
    
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockCartBeef, response.getBody());
  }

  // Test the functionality of AddToShoppingCart when provided bad input
  @Test
  void testAddToShoppingCartBadRequest() throws IOException{
    when(mockCartBeef.getWeight()).thenReturn((float)-2);
    ResponseEntity<CartBeef> response = shoppingController.AddToShoppingCart("TestCustomer", mockCartBeef);
    
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  // Test the functionality of addToShoppingCart when the beef is not found
  @Test
  void testAddToShoppingCartNotFound() throws IOException{
    when(mockInventoryDAO.getBeef(anyInt())).thenReturn(null);
    when(mockCartBeef.getWeight()).thenReturn((float)3);
    ResponseEntity<CartBeef> response = shoppingController.AddToShoppingCart("TestCustomer", mockCartBeef);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  // Test the functionality of addToShoppingCart when the server encounters an
  // error
  @Test
  void testAddToShoppingCartError() throws IOException {
    doThrow(new IOException("Failed to read from file"))
        .when(mockUserDAO)
        .GetUser(anyString());
    when(mockCartBeef.getWeight()).thenReturn((float) 3);
    ResponseEntity<CartBeef> response = shoppingController.AddToShoppingCart("TestCustomer", mockCartBeef);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  // Test normal functionality of clearShoppingCart
  @Test
  void testClearShoppingCart() {
    ResponseEntity<Boolean> response = shoppingController.ClearShoppingCart("TestCustomer");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(true, response.getBody());
  }

  // Test the functionality of shopping cart when the specified user is not found
  @Test
  void testClearShoppingCartNotFound() throws IOException{
    when(mockUserDAO.GetUser(anyString())).thenReturn(null);
    ResponseEntity<Boolean> response = shoppingController.ClearShoppingCart("TestCustomer");
  
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  // Test the functionality of ClearShoppingCart when the server encounters an
  // error
  @Test
  void testClearShoppingCartError() throws IOException {
    doThrow(new IOException("Failed to read from file"))
        .when(mockUserDAO)
        .GetUser(anyString());
    ResponseEntity<Boolean> response = shoppingController.ClearShoppingCart("TestCustomer");

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  // Test the normal functionality of RemoveFromShoppingCart
  @Test
  void testRemoveFromShoppingCart() {
    ResponseEntity<Boolean> response = shoppingController.RemoveFromShoppingCart("TestCustomer", 1);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  // Test the functionality of remove from shopping cart when the user is not
  // found
  @Test
  void testRemoveFromShoppingCartNotFound() throws IOException {
    when(mockUserDAO.GetUser(anyString())).thenReturn(null);
    ResponseEntity<Boolean> response = shoppingController.RemoveFromShoppingCart("TestCustomer", 1);
    
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  // Test the functionality of remove from shopping cart when the server
  // encounters an error
  @Test
  void testRemoveFromShoppingCartError() throws IOException {
    doThrow(new IOException("Failed to read from file"))
        .when(mockUserDAO)
        .GetUser(anyString());
    ResponseEntity<Boolean> response = shoppingController.RemoveFromShoppingCart("TestCustoemr", 1);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }
}
