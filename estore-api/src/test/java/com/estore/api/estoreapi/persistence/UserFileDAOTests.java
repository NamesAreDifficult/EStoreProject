package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.estore.api.estoreapi.users.User;
import com.estore.api.estoreapi.users.Customer;
import com.estore.api.estoreapi.users.ShoppingCart;
import com.estore.api.estoreapi.users.Admin;
import com.estore.api.estoreapi.users.CreditCard;
import com.estore.api.estoreapi.products.Beef;
import com.estore.api.estoreapi.products.CartBeef;

@Tag("Persistence-tier")
class UserFileDAOTests {
  UserFileDAO userFileDAO;
  User[] testUsers;
  ObjectMapper mockObjectMapper;
  ShoppingCart mockShoppingCart;
  CreditCard mockCreditCard;

  @BeforeEach
  void setupUserFileDao() throws IOException {
    mockObjectMapper = mock(ObjectMapper.class);
    mockShoppingCart = mock(ShoppingCart.class);
    mockCreditCard = mock(CreditCard.class);
    testUsers = new User[4];

    Customer[] testCustomers = new Customer[3];
    Admin[] testAdmins = new Admin[1];

    CartBeef first = new CartBeef(new Beef(0, "cut1", 2, "grade1", 129.99, "https://www.youtube.com/watch?v=dQw4w9WgXcQ"), 2);
    CartBeef second = new CartBeef(new Beef(1, "cut2", 3, "grade2", 139.99, "https://www.youtube.com/watch?v=dQw4w9WgXcQ"), 3);
    CartBeef[] firstCart = new CartBeef[2];
    CartBeef[] secondCart = new CartBeef[0];

    firstCart[0] = first;
    firstCart[1] = second;

    testUsers[0] = new Customer("Joe", "password", mockShoppingCart);
    testUsers[1] = new Customer("Candice", "password", new ShoppingCart(secondCart));
    testUsers[2] = new Admin("Wendy", "password");
    testUsers[3] = new Customer("Heisenberg", "password", new ShoppingCart(firstCart));

    testCustomers[0] = new Customer("Joe", "password", mockShoppingCart);
    testCustomers[1] = new Customer("Candice", "password", new ShoppingCart(secondCart));
    testCustomers[2] = new Customer("Heisenberg", "password", new ShoppingCart(firstCart));

    testAdmins[0] = new Admin("Wendy", "password");

    when(mockObjectMapper.readValue(new File("customer.txt"), Customer[].class)).thenReturn(testCustomers);
    when(mockObjectMapper.readValue(new File("admin.txt"), Admin[].class)).thenReturn(testAdmins);

    userFileDAO = new UserFileDAO("customer.txt", "admin.txt", mockObjectMapper);
  }

  @AfterEach
  void cleanupTests(){
    File[] files = new File[] {new File("customer.txt"), new File("admin.txt")};
    for(File file : files){
    if (file.exists()){
    file.delete();
    }
    }
  }
  
  @Test
  void testGetUsers() {
    User[] users = assertDoesNotThrow(() -> userFileDAO.getUsers(),
        "Unexpected exception thrown");
    Arrays.sort(testUsers);
    assertArrayEquals(users, testUsers);
    //
    // assertEquals(users.length, testUsers.length);
    // for (int i = 0; i < testUsers.length;++i)
    // assertEquals(users[i], testUsers[i]);
  }

  @Test
  void testSave() throws IOException{
    doThrow(new IOException()).doNothing()
      .when(mockObjectMapper)
      .writeValue(any(File.class), any(Customer[].class));
    assertDoesNotThrow(() -> {
      userFileDAO.addCard("Joe", mockCreditCard);
      userFileDAO.addCard("Candice", mockCreditCard);
    });
  }

  @Test
  void testLoad() throws IOException{
    doThrow(new IOException())
      .when(mockObjectMapper)
      .readValue(any(File.class), eq(Customer[].class));
    assertDoesNotThrow(() ->{
      UserFileDAO testDAO = new UserFileDAO("customer.txt", "admin.txt", mockObjectMapper);
    });
    
  }

  @Test
  void testAddCard() throws IOException{
    assertTrue(userFileDAO.addCard("Joe", mockCreditCard));
  }

  @Test
  void testAddCardNullCust() throws IOException{
    assertFalse(userFileDAO.addCard("Jack", mockCreditCard));
  }
  
  @Test
  void testRemoveCard() throws IOException{
    userFileDAO.addCard("Joe", mockCreditCard);
    assertTrue(userFileDAO.removeCard("Joe", mockCreditCard));
  }

  @Test
  void testRemoveCardNullCust() throws IOException{
    assertFalse(userFileDAO.removeCard("Jack", mockCreditCard));
  }

  @Test
  void testGetCards() throws IOException{
    userFileDAO.addCard("Joe",mockCreditCard);
    assertEquals(mockCreditCard, userFileDAO.getCards("Joe")[0]);
  }
  
  @Test
  void testGetCardsNull() throws IOException{
    assertNull(userFileDAO.getCards("Jack"));
  }

  @Test
  void testLoginUser() throws IOException{
    User testUser = testUsers[0];
    assertEquals(testUser, userFileDAO.loginUser(testUser.getUsername(), testUser.getPassword()));
  }

  @Test
  void testLoginUserNull() throws IOException{
    assertNull(userFileDAO.loginUser("Joel","Ellie"));
  }

  @Test
  void testGetUser() {
    User first = assertDoesNotThrow(() -> userFileDAO.getUser("Joe"),
        "Unexpected exception thrown");
    User second = assertDoesNotThrow(() -> userFileDAO.getUser("Candice"),
        "Unexpected exception thrown");
    User third = assertDoesNotThrow(() -> userFileDAO.getUser("Wendy"),
        "Unexpected exception thrown");
    assertEquals(first, testUsers[0]);
    assertEquals(second, testUsers[1]);
    assertEquals(third, testUsers[2]);
  }

  @Test
  void testDeleteUser() {
    boolean result = assertDoesNotThrow(() -> userFileDAO.deleteUser("Joe"),
        "Unexpected exception thrown");
    assertTrue(result);
    assertEquals(userFileDAO.users.size(), testUsers.length - 1);
  }

  @Test
  void testIsAdmin() {
    boolean adminResult = assertDoesNotThrow(() -> userFileDAO.isAdmin("Wendy"),
        "Unexpected exception thrown");
    boolean customerResult = assertDoesNotThrow(() -> userFileDAO.isAdmin("Joe"),
        "Unexpected exception thrown");
    assertTrue(adminResult);
    assertFalse(customerResult);
  }

  @Test
  void testCreateCustomer() {
    Customer newCustomer = new Customer("John", "password", new ShoppingCart());
    Customer result = assertDoesNotThrow(() -> userFileDAO.createCustomer(newCustomer),
        "Unexpected exception thrown");
    assertNotNull(result);
    Customer actual = (Customer) assertDoesNotThrow(() -> userFileDAO.getUser("John"),
        "Unexpected exception thrown");
    assertEquals(newCustomer.getCart(), actual.getCart());
    assertEquals(newCustomer.getUsername(), actual.getUsername());
  }

  @Test
  void testCreateAdmin() {
    Admin newAdmin = new Admin("Giant Rat", "password");
    Admin result = assertDoesNotThrow(() -> userFileDAO.createAdmin(newAdmin),
        "Unexpected exception thrown");
    assertNotNull(result);
    Admin actual = (Admin) assertDoesNotThrow(() -> userFileDAO.getUser("Giant Rat"),
        "Unexpected exception thrown");
    assertEquals(newAdmin.getUsername(), actual.getUsername());
  }

  @Test
  void testCheckout() {
    assertDoesNotThrow(() -> userFileDAO.addCard("Heisenberg", new CreditCard("1234567812345678", "04/20", "123")), "Unexpected exception thrown");
    boolean result = assertDoesNotThrow(() -> userFileDAO.checkout("Heisenberg", "1234567812345678"),
        "Unexpected exception thrown");
    assertTrue(result);
  }

  @Test
  void testCheckoutEmpty() {
    assertDoesNotThrow(() -> userFileDAO.addCard("Candice", new CreditCard("1234567812345678", "04/20", "123")), "Unexpected exception thrown");
    boolean result = assertDoesNotThrow(() -> userFileDAO.checkout("Candice", "1234567812345678"),
        "Unexpected exception thrown");
    assertFalse(result);
  }

  @Test
  void testAddToCart() {
  when(mockShoppingCart.addToCart(any(CartBeef.class))).thenReturn(true);
  assertDoesNotThrow(() -> {
  assertTrue(userFileDAO.addToCart("Joe", 1, (float)3.4));
  });
  }

  @Test
  void testRemoveFromCart() {
  //Test for when an item is not removed from the cart
  when(mockShoppingCart.removeFromCart(anyInt())).thenReturn(true);
  assertDoesNotThrow(() -> {
  assertTrue(userFileDAO.RemoveFromCart("Joe", 1));
  });

  //Test for when an item is not removed from cart
  when(mockShoppingCart.removeFromCart(anyInt())).thenReturn(false);
  assertDoesNotThrow(() -> {
  assertFalse(userFileDAO.RemoveFromCart("Joe", 0));
  });
  }

  @Test
  void clearCart() {
  assertDoesNotThrow(() -> {
  userFileDAO.clearCart("Joe");
  });
  }

  // TODO: Implement defensive testing, might not use all of these
  @Test
  void testDeleteUserAbsent() {
    boolean result = assertDoesNotThrow(() -> userFileDAO.deleteUser("Josh Allen"),
        "Unexpected exception thrown");
    assertEquals(userFileDAO.users.size(), testUsers.length);
    assertFalse(result);
  }

  @Test
  void testCreateAdminPresent() {
    Admin existAdmin = new Admin("Wendy", "password");
    Admin result = assertDoesNotThrow(() -> userFileDAO.createAdmin(existAdmin),
        "Unexpected exception thrown");
    assertNull(result);
  }

  @Test
  void testCreateCustomerPresent() {
    Customer customer = new Customer("Joe", "password", new ShoppingCart());
    Customer result = assertDoesNotThrow(() -> userFileDAO.createCustomer(customer),
        "Unexpected exception thrown");
    assertNull(result);
  }

  @Test
  void testGetUserAbsent() {
    User result = assertDoesNotThrow(() -> userFileDAO.getUser("Zuckerberg"),
        "Unexpected exception thrown");
    assertNull(result);
  }

  @Test
  void testSaveExceptionCustomer() throws IOException {
    doThrow(new IOException("Failed to write"))
        .when(mockObjectMapper)
        .writeValue(any(File.class), any(User[].class));
    Customer newCustomer = new Customer("John", "password", new ShoppingCart());
    assertThrows(IOException.class,
        () -> userFileDAO.createCustomer(newCustomer),
        "IOException not thrown");
  }

  @Test
  void testSaveExceptionAdmin() throws IOException {
    doThrow(new IOException())
        .when(mockObjectMapper)
        .writeValue(any(File.class), any(Admin[].class));
    Admin admin = new Admin("John", "password");
    assertThrows(IOException.class,
        () -> userFileDAO.createAdmin(admin),
        "IOException not thrown");
  }

  @Test
  void testAddToCartPresent() {
    Customer customer = new Customer("Jeremy", "password", new ShoppingCart());
    float weight = (float) .15;
    CartBeef beef = new CartBeef(3, weight);
    CartBeef[] newCart = new CartBeef[1];
    newCart[0] = beef;
    customer.getCart().addToCart(beef);
    assertArrayEquals(customer.getCart().getContents(), newCart);
    float weight2 = (float) .3;
    newCart[0] = new CartBeef(3, weight2);
    customer.getCart().addToCart(beef);
    assertArrayEquals(newCart, customer.getCart().getContents());
  }

  @Test
  void testRemoveFromCartAbsent() {
    Customer customer = new Customer("Liam", "password", new ShoppingCart());
    boolean test = customer.getCart().removeFromCart(4);
    assertEquals(false, test);
  }
}
