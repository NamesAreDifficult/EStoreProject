package com.estore.api.estoreapi.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;

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
import com.estore.api.estoreapi.users.User;

@Tag("Controller-tier")
public class ShoppingControllerTests {
    private ShoppingController shoppingController;
    private UserDAO mockUserDAO;
    private InventoryDAO mockInventoryDAO;
    private Customer mockCustomer;
    private ShoppingCart mockShoppingCart;
    private CartBeef mockCartBeef;
    private Beef mockBeef;


  /**
   * Before each test, create a new ShoppingController object and inject 
   * a mock User DAO
   */

  @BeforeEach
  public void setupShoppingController() throws IOException {
      mockUserDAO = mock(UserDAO.class);
      mockInventoryDAO = mock(InventoryDAO.class);
      mockCustomer = mock(Customer.class);
      mockShoppingCart = mock(ShoppingCart.class);
      mockCartBeef = mock(CartBeef.class);
      mockBeef = mock(Beef.class);

      when(mockUserDAO.GetUser(anyString())).thenReturn(mockCustomer);
      when(mockCustomer.getCart()).thenReturn(mockShoppingCart);
      when(mockInventoryDAO.getBeef(anyInt())).thenReturn(mockBeef);
      when(mockShoppingCart.getContents()).thenReturn(new CartBeef[] {mockCartBeef});
      when(mockShoppingCart.removeFromCart(anyInt())).thenReturn(true);
      when(mockCartBeef.getId()).thenReturn(1);

      doNothing().when(mockBeef).setWeight(anyFloat());

      shoppingController = new ShoppingController(mockInventoryDAO, mockUserDAO);

  }

  @Test
  public void testGetShoppingCart() {
    Beef[] expected = new Beef[] {mockBeef};

    ResponseEntity<Beef[]> response = shoppingController.getShoppingCart("TestCustomer");

    assertEquals(mockBeef, mockBeef);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(Arrays.equals(expected, response.getBody()));
  }

  @Test
  public void testGetShoppingCartNotFound() throws IOException {
    when(mockUserDAO.GetUser(anyString())).thenReturn(null);

    ResponseEntity<Beef[]> response = shoppingController.getShoppingCart("TestCustomer");

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test public void testGetShoppingCartError() throws IOException {
    doThrow(new IOException("Failed to read from file"))
      .when(mockUserDAO)
      .GetUser(anyString());
    ResponseEntity<Beef[]> response = shoppingController.getShoppingCart("TestCustomer");

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  public void testCheckoutShoppingCart() throws IOException {
    ResponseEntity<Boolean> response = shoppingController.CheckoutShoppingCart("Test");
    
    assertEquals(HttpStatus.NOT_IMPLEMENTED, response.getStatusCode());
  }

  @Test 
  public void testAddToShoppingCart() throws IOException{
    when(mockCartBeef.getWeight()).thenReturn((float)3);
    ResponseEntity<CartBeef> response = shoppingController.AddToShoppingCart("TestCustomer", mockCartBeef);
    
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockCartBeef, response.getBody());
  }

  @Test
  public void testAddToShoppingCartBadRequest() throws IOException{
    when(mockCartBeef.getWeight()).thenReturn((float)-2);
    ResponseEntity<CartBeef> response = shoppingController.AddToShoppingCart("TestCustomer", mockCartBeef);
    
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void testAddToShoppingCartNotFound() throws IOException{
    when(mockInventoryDAO.getBeef(anyInt())).thenReturn(null);
    when(mockCartBeef.getWeight()).thenReturn((float)3);
    ResponseEntity<CartBeef> response = shoppingController.AddToShoppingCart("TestCustomer", mockCartBeef);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void testAddToShoppingCartError() throws IOException{
    doThrow(new IOException("Failed to read from file"))
      .when(mockUserDAO)
      .GetUser(anyString());
    when(mockCartBeef.getWeight()).thenReturn((float)3);
    ResponseEntity<CartBeef> response = shoppingController.AddToShoppingCart("TestCustomer", mockCartBeef);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test 
  public void testClearShoppingCart(){
    ResponseEntity<Boolean> response = shoppingController.ClearShoppingCart("TestCustomer");
    
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(true, response.getBody());
  }

  @Test
  public void testClearShoppingCartNotFound() throws IOException{
    when(mockUserDAO.GetUser(anyString())).thenReturn(null);
    ResponseEntity<Boolean> response = shoppingController.ClearShoppingCart("TestCustomer");
  
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void testClearShoppingCartError() throws IOException{
  doThrow(new IOException("Failed to read from file"))
    .when(mockUserDAO)
    .GetUser(anyString());
  ResponseEntity<Boolean> response = shoppingController.ClearShoppingCart("TestCustomer");

  assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  public void testRemoveFromShoppingCart(){
    ResponseEntity<Boolean> response = shoppingController.RemoveFromShoppingCart("TestCustomer", 1);
    
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void testRemoveFromShoppingCartNotFound() throws IOException {
    when(mockUserDAO.GetUser(anyString())).thenReturn(null);
    ResponseEntity<Boolean> response = shoppingController.RemoveFromShoppingCart("TestCustomer", 1);
    
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void testRemoveFromShoppingCartError() throws IOException {
    doThrow(new IOException("Failed to read from file"))
      .when(mockUserDAO)
      .GetUser(anyString());
    ResponseEntity<Boolean> response = shoppingController.RemoveFromShoppingCart("TestCustoemr", 1);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }
}
