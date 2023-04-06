package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;

import java.io.IOException;

import com.estore.api.estoreapi.users.User;

import com.estore.api.estoreapi.persistence.UserDAO;
import com.estore.api.estoreapi.users.Admin;
import com.estore.api.estoreapi.users.CreditCard;
import com.estore.api.estoreapi.users.Customer;
import com.estore.api.estoreapi.users.ShoppingCart;

@Tag("Controller-tier")
public class UserControllerTests {
  private UserController userController;
  private UserDAO mockUserDAO;
  private Customer mockCustomer;
  private CreditCard mockCreditCard;

  @BeforeEach
  public void setupUserController() throws IOException{
    mockCustomer = mock(Customer.class);
    mockUserDAO = mock(UserDAO.class);
    mockCreditCard = mock(CreditCard.class);

    userController = new UserController(mockUserDAO);

    when(mockUserDAO.GetUser(anyString())).thenReturn(mockCustomer);
    when(mockCreditCard.getNumber()).thenReturn("1234567890987654");
    when(mockCreditCard.getExpiration()).thenReturn("01/27");
    when(mockCreditCard.getCVV()).thenReturn("999");
    when(mockCustomer.getCard(anyString())).thenReturn(mockCreditCard);
  }

  @Test
  public void testAddCard() throws IOException{
    when(mockUserDAO.addCard(anyString(), any(CreditCard.class))).thenReturn(true);
    ResponseEntity<Boolean> response = userController.addCard("Shrek", mockCreditCard);
    
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody());
  }

  @Test
  public void testAddCardInvalidCustomer() throws IOException{
    when(mockUserDAO.GetUser(anyString())).thenReturn(null);
    ResponseEntity<Boolean> response = userController.addCard("Mona Lisa", mockCreditCard);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  public void testAddCardInvalidCard() throws IOException{
    when(mockCreditCard.getCVV()).thenReturn("2");
    ResponseEntity<Boolean> response = userController.addCard("Dragonborn", mockCreditCard);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  public void testAddCardConflict() throws IOException{
    when(mockUserDAO.addCard(anyString(), any(CreditCard.class))).thenReturn(false);
    ResponseEntity<Boolean> response = userController.addCard("Your Mother", mockCreditCard);

    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    assertFalse(response.getBody());
  }

  @Test 
  public void testAddCardError() throws IOException{
    doThrow(new IOException("Failed to access file"))
      .when(mockUserDAO)
      .GetUser(anyString());
    ResponseEntity<Boolean> response = userController.addCard("Luca", mockCreditCard);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  public void testRemoveCard() throws IOException{
    when(mockCustomer.removeCard(any(CreditCard.class))).thenReturn(true);
    ResponseEntity<Boolean> response = userController.removeCard("I am so hungry", "5");
    
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody());
  }

  @Test
  public void testRemoveCardConflict() throws IOException{
    when(mockCustomer.removeCard(any(CreditCard.class))).thenReturn(false);
    ResponseEntity<Boolean> response = userController.removeCard("Samuel Jackson", "5");
    
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertFalse(response.getBody());
  }

  @Test
  public void testRemoveCardNotFound() throws IOException{
    when(mockUserDAO.GetUser(anyString())).thenReturn(null);
    ResponseEntity<Boolean> response = userController.removeCard("Brolaf", "5");

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  public void testRemoveCardError() throws IOException{
    doThrow(new IOException("Cannot read from file"))
      .when(mockUserDAO)
      .GetUser(anyString());
    ResponseEntity<Boolean> response = userController.removeCard("Hercules", "5");

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertNull(response.getBody());
  }
  
  @Test
  public void testIsValidNull(){
    ResponseEntity<Boolean> response = userController.addCard("jack", null);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
  @Test
  public void testGetCards() throws IOException{
    CreditCard[] expected = new CreditCard[] {mockCreditCard};
    when(mockUserDAO.getCards(anyString())).thenReturn(expected);
    ResponseEntity<CreditCard[]> response = userController.getCards("Mister Green Jeans");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expected, response.getBody());
  }

  @Test
  public void testGetCardsNotFound() throws IOException{
    when(mockUserDAO.GetUser(anyString())).thenReturn(null);
    ResponseEntity<CreditCard[]> response = userController.getCards("Headless Horsemen");

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  public void testGetCardsError() throws IOException{
    doThrow(new IOException())
      .when(mockUserDAO)
      .GetUser(anyString());
    ResponseEntity<CreditCard[]> response = userController.getCards("John Cena");
    
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  public void testLoginUser() throws IOException{
    when(mockUserDAO.loginUser(anyString(), anyString())).thenReturn(mockCustomer);
    ResponseEntity<User> response = userController.loginUser("Mr. Security", "SafestPassword");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockCustomer, response.getBody());
  }

  @Test
  public void testLoginUserUnauthorized() throws IOException{
    when(mockUserDAO.loginUser(anyString(), anyString())).thenReturn(null);
    ResponseEntity<User> response = userController.loginUser("Mr. Security", "WeakestPassword");

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  public void testLoginUserError() throws IOException{
    doThrow(new IOException())
      .when(mockUserDAO)
      .loginUser(anyString(), anyString());
    ResponseEntity<User> response = userController.loginUser("aaaaaaa", "BBBBBB");

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  public void testGetUser() throws IOException{
    ResponseEntity<User> response = userController.getUser("Charles");
    
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockCustomer, response.getBody());
  }

  @Test
  public void testGetUserNotFound() throws IOException{
    when(mockUserDAO.GetUser(anyString())).thenReturn(null);
    ResponseEntity<User> response = userController.getUser("Bugs Bunny");

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  public void testGetUserError() throws IOException{
    doThrow(new IOException())
      .when(mockUserDAO)
      .GetUser(anyString());
    ResponseEntity<User> response = userController.getUser("I have to go soon");

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertNull(response.getBody());
  }
  @Test
  public void testCreateCustomer() throws IOException {
    Customer customer = new Customer("Jack","password", new ShoppingCart());
    when(mockUserDAO.createCustomer(customer)).thenReturn(customer);
    ResponseEntity<Customer> response = userController.createCustomer(customer);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(customer, response.getBody());
  }

  @Test
  public void testCreateCustomerFailed() throws IOException {
    Customer customer = new Customer("Jack","password", new ShoppingCart());
    when(mockUserDAO.createCustomer(customer)).thenReturn(null);
    ResponseEntity<Customer> response = userController.createCustomer(customer);
    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
  }

  @Test
  public void testCreateCustomerHandleException() throws IOException {
    Customer customer = new Customer("Jack", "password", new ShoppingCart());
    doThrow(new IOException()).when(mockUserDAO).createCustomer(customer);
    ResponseEntity<Customer> response = userController.createCustomer(customer);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  public void testCreateAdmin() throws IOException {
    Admin admin = new Admin("John", "password");
    when(mockUserDAO.createAdmin(admin)).thenReturn(admin);
    ResponseEntity<Admin> response = userController.createAdmin(admin);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(admin, response.getBody());
  }

  @Test
  public void testCreateAdminFailed() throws IOException {
    Admin admin = new Admin("John", "password");
    when(mockUserDAO.createAdmin(admin)).thenReturn(null);
    ResponseEntity<Admin> response = userController.createAdmin(admin);
    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
  }

  @Test
  public void testCreateAdminHandleException() throws IOException {
    Admin admin = new Admin("John", "password");
    doThrow(new IOException()).when(mockUserDAO).createAdmin(admin);
    ResponseEntity<Admin> response = userController.createAdmin(admin);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  public void testGetUsers() throws IOException {
    User[] users = new User[2];
    users[0] = new Customer("Jack", "password", new ShoppingCart());
    users[1] = new Admin("John", "password");
    when(mockUserDAO.GetUsers()).thenReturn(users);
    ResponseEntity<User[]> response = userController.GetUsers();
    // Analyze
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(users, response.getBody());
  }

  @Test
  public void testGetUsersHandleException() throws IOException {
    doThrow(new IOException()).when(mockUserDAO).GetUsers();
    ResponseEntity<User[]> response = userController.GetUsers();
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  // TODO: Implement these tests once DeleteUser has been implemented
  @Test
  public void testDeleteUser() throws IOException {
    when(mockUserDAO.DeleteUser(anyString())).thenReturn(true);
    ResponseEntity<Boolean> response  = userController.DeleteUser("Slim Jim");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody());
  }

  @Test
  public void testDeleteUserNotFound() throws IOException {
    when(mockUserDAO.GetUser(anyString())).thenReturn(null);
    ResponseEntity<Boolean> response  = userController.DeleteUser("Santa Clause");

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  public void testDeleteUserHandleException() throws IOException {
    doThrow(new IOException())
      .when(mockUserDAO)
      .GetUser(anyString());
    ResponseEntity<Boolean> response  = userController.DeleteUser("Jolly Green Giant");

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertNull(response.getBody());
  }
}
