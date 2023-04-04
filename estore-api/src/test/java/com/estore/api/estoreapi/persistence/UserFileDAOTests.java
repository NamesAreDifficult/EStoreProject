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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.any;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.estore.api.estoreapi.users.User;
import com.estore.api.estoreapi.users.Customer;
import com.estore.api.estoreapi.users.ShoppingCart;
import com.estore.api.estoreapi.users.Admin;
import com.estore.api.estoreapi.products.Beef;
import com.estore.api.estoreapi.products.CartBeef;

@Tag("Persistence-tier")
public class UserFileDAOTests {
  UserFileDAO userFileDAO;
  User[] testUsers;
  ObjectMapper mockObjectMapper;
  ShoppingCart mockShoppingCart;

  @BeforeEach
  public void setupUserFileDao() throws IOException {
    mockObjectMapper = mock(ObjectMapper.class);
    mockShoppingCart = mock(ShoppingCart.class);
    testUsers = new User[4];

    Customer[] testCustomers = new Customer[3];
    Admin[] testAdmins = new Admin[1];

    CartBeef first = new CartBeef(new Beef(0, "cut1", 2, "grade1", 129.99), 2);
    CartBeef second = new CartBeef(new Beef(1, "cut2", 3, "grade2", 139.99), 3);
    CartBeef[] firstCart = new CartBeef[2];
    CartBeef[] secondCart = new CartBeef[0];

    firstCart[0] = first;
    firstCart[1] = second;

    testUsers[0] = new Customer("Joe", "password", mockShoppingCart);
    testUsers[1] = new Customer("Candice", "password", new ShoppingCart(secondCart));
    testUsers[2] = new Admin("Wendy", "password");

    testCustomers[0] = new Customer("Joe", "password", mockShoppingCart);
    testCustomers[1] = new Customer("Candice", "password", new ShoppingCart(secondCart));

    testAdmins[0] = new Admin("Wendy", "password");

    when(mockObjectMapper.readValue(new File("customer.txt"), Customer[].class)).thenReturn(testCustomers);
    when(mockObjectMapper.readValue(new File("admin.txt"), Admin[].class)).thenReturn(testAdmins);

    userFileDAO = new UserFileDAO("customer.txt", "admin.txt", mockObjectMapper);
  }

  @AfterEach
  public void cleanupTests(){
    File[] files = new File[] {new File("customer.txt"), new File("admin.txt")};
    for(File file : files){
    if (file.exists()){
    file.delete();
    }
    }
  }
  
  @Test
  public void testGetUsers() {
    User[] users = assertDoesNotThrow(() -> userFileDAO.GetUsers(),
        "Unexpected exception thrown");
    Arrays.sort(testUsers);
    assertArrayEquals(users, testUsers);
    //
    // assertEquals(users.length, testUsers.length);
    // for (int i = 0; i < testUsers.length;++i)
    // assertEquals(users[i], testUsers[i]);
  }

  @Test
  public void testGetUser() {
    User first = assertDoesNotThrow(() -> userFileDAO.GetUser("Joe"),
        "Unexpected exception thrown");
    User second = assertDoesNotThrow(() -> userFileDAO.GetUser("Candice"),
        "Unexpected exception thrown");
    User third = assertDoesNotThrow(() -> userFileDAO.GetUser("Wendy"),
        "Unexpected exception thrown");
    assertEquals(first, testUsers[0]);
    assertEquals(second, testUsers[1]);
    assertEquals(third, testUsers[2]);
  }

  @Test
  public void testDeleteUser() {
    boolean result = assertDoesNotThrow(() -> userFileDAO.DeleteUser("Joe"),
        "Unexpected exception thrown");
    assertTrue(result);
    assertEquals(userFileDAO.users.size(), testUsers.length - 1);
  }

  @Test
  public void testIsAdmin() {
    boolean adminResult = assertDoesNotThrow(() -> userFileDAO.IsAdmin("Wendy"),
        "Unexpected exception thrown");
    boolean customerResult = assertDoesNotThrow(() -> userFileDAO.IsAdmin("Joe"),
        "Unexpected exception thrown");
    assertTrue(adminResult);
    assertFalse(customerResult);
  }

  @Test
  public void testCreateCustomer() {
    Customer newCustomer = new Customer("John", "password", new ShoppingCart());
    Customer result = assertDoesNotThrow(() -> userFileDAO.createCustomer(newCustomer),
        "Unexpected exception thrown");
    assertNotNull(result);
    Customer actual = (Customer) assertDoesNotThrow(() -> userFileDAO.GetUser("John"),
        "Unexpected exception thrown");
    assertEquals(newCustomer.getCart(), actual.getCart());
    assertEquals(newCustomer.getUsername(), actual.getUsername());
  }

  @Test
  public void testCreateAdmin() {
    Admin newAdmin = new Admin("Giant Rat", "password");
    Admin result = assertDoesNotThrow(() -> userFileDAO.createAdmin(newAdmin),
        "Unexpected exception thrown");
    assertNotNull(result);
    Admin actual = (Admin) assertDoesNotThrow(() -> userFileDAO.GetUser("Giant Rat"),
        "Unexpected exception thrown");
    assertEquals(newAdmin.getUsername(), actual.getUsername());
  }

  @Test
  public void testCheckout() {
    boolean result = assertDoesNotThrow(() -> userFileDAO.Checkout("Heisenberg"),
        "Unexpected exception thrown");
    assertTrue(result);
  }

  @Test
  public void testCheckoutEmpty() {
    boolean result = assertDoesNotThrow(() -> userFileDAO.Checkout("Candice"),
        "Unexpected exception thrown");
    assertFalse(result);
  }

  @Test
  public void testAddToCart() {
  when(mockShoppingCart.addToCart(any(CartBeef.class))).thenReturn(true);
  assertDoesNotThrow(() -> {
  assertTrue(userFileDAO.AddToCart("Joe", 1, (float)3.4));
  });
  }

  @Test
  public void testRemoveFromCart() {
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
  public void clearCart() {
  assertDoesNotThrow(() -> {
  userFileDAO.ClearCart("Joe");
  });
  }

  // TODO: Implement defensive testing, might not use all of these
  @Test
  public void testDeleteUserAbsent() {
    boolean result = assertDoesNotThrow(() -> userFileDAO.DeleteUser("Josh Allen"),
        "Unexpected exception thrown");
    assertEquals(userFileDAO.users.size(), testUsers.length);
    assertFalse(result);
  }

  @Test
  public void testCreateAdminPresent() {
    Admin existAdmin = new Admin("Wendy", "password");
    Admin result = assertDoesNotThrow(() -> userFileDAO.createAdmin(existAdmin),
        "Unexpected exception thrown");
    assertNull(result);
  }

  @Test
  public void testCreateCustomerPresent() {
    Customer customer = new Customer("Joe", "password", new ShoppingCart());
    Customer result = assertDoesNotThrow(() -> userFileDAO.createCustomer(customer),
        "Unexpected exception thrown");
    assertNull(result);
  }

  @Test
  public void testGetUserAbsent() {
    User result = assertDoesNotThrow(() -> userFileDAO.GetUser("Zuckerberg"),
        "Unexpected exception thrown");
    assertNull(result);
  }

  @Test
  public void testSaveExceptionCustomer() throws IOException {
    doThrow(new IOException("Failed to write"))
        .when(mockObjectMapper)
        .writeValue(any(File.class), any(User[].class));
    Customer newCustomer = new Customer("John", "password", new ShoppingCart());
    assertThrows(IOException.class,
        () -> userFileDAO.createCustomer(newCustomer),
        "IOException not thrown");
  }

  @Test
  public void testSaveExceptionAdmin() throws IOException {
    doThrow(new IOException())
        .when(mockObjectMapper)
        .writeValue(any(File.class), any(Admin[].class));
    Admin admin = new Admin("John", "password");
    assertThrows(IOException.class,
        () -> userFileDAO.createAdmin(admin),
        "IOException not thrown");
  }

  @Test
  public void testAddToCartPresent() {
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
  public void testRemoveFromCartAbsent() {
    Customer customer = new Customer("Liam", "password", new ShoppingCart());
    boolean test = customer.getCart().removeFromCart(4);
    assertEquals(false, test);
  }
}
