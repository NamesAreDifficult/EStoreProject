package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.any;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.estore.api.estoreapi.users.User;
import com.estore.api.estoreapi.users.Customer;
import com.estore.api.estoreapi.users.Admin;
import com.estore.api.estoreapi.products.Beef;
import com.estore.api.estoreapi.products.CartBeef;

@Tag("Persistence-tier")
public class UserFileDAOTests {
    UserFileDAO userFileDAO;
    User[] testUsers;
    ObjectMapper mockObjectMapper;

    @BeforeEach
    public void setupUserFileDao() throws IOException{
        mockObjectMapper = mock(ObjectMapper.class);
        testUsers = new User[3];
        CartBeef first = new CartBeef(new Beef(0, "cut1", 2, "grade1", 129.99), 2);
        CartBeef second = new CartBeef(new Beef(1, "cut2", 3, "grade2", 139.99), 3);
        CartBeef[] firstCart = new CartBeef[1];
        CartBeef[] secondCart = new CartBeef[1];
        firstCart[0] = first;
        secondCart[0] = second;
        testUsers[0] = new Customer("Joe", firstCart);
        testUsers[1] = new Customer("Candice", secondCart);
        testUsers[2] = new Admin("Wendy");

        when(mockObjectMapper.readValue(new File("test.txt"),User[].class)).thenReturn(testUsers);

        userFileDAO = new UserFileDAO("test.txt", mockObjectMapper);
    }

    @Test
    public void testSaveExceptionCustomer() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(User[].class));
        Customer newCustomer = new Customer("John", new CartBeef[1]);
        assertThrows(IOException.class,
                        () -> userFileDAO.createCustomer(newCustomer),
                        "IOException not thrown");
    }

    @Test
    public void testSaveExceptionAdmin() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(User[].class));
        Admin admin = new Admin("John");
        assertThrows(IOException.class,
                        () -> userFileDAO.createAdmin(admin),
                        "IOException not thrown");
    }

    @Test
    public void testConstructorException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("test.txt"),User[].class);
        assertThrows(IOException.class,
                        () -> new UserFileDAO("test.txt",mockObjectMapper),
                        "IOException not thrown");
    }

    @Test
    public void testGetUsers(){
        User[] users = assertDoesNotThrow(() -> userFileDAO.GetUsers(),
                                "Unexpected exception thrown");
        assertEquals(users.length, testUsers.length);
        for (int i = 0; i < testUsers.length;++i)
            assertEquals(users[i], testUsers[i]);
    }

    @Test
    public void testGetUser(){
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
    public void testDeleteUser(){
        boolean result = assertDoesNotThrow(() -> userFileDAO.DeleteUser("Joe"),
                            "Unexpected exception thrown");
        assertTrue(result);
        assertEquals(userFileDAO.users.size(), testUsers.length - 1);
    }

    @Test
    public void testIsAdmin(){
        boolean adminResult = assertDoesNotThrow(() -> userFileDAO.IsAdmin("Wendy"),
                            "Unexpected exception thrown");
        boolean customerResult = assertDoesNotThrow(() -> userFileDAO.IsAdmin("Joe"),
                            "Unexpected exception thrown");
        assertTrue(adminResult);
        assertFalse(customerResult);
    }

    @Test
    public void testCreateCustomer(){
        Customer newCustomer = new Customer("John", new CartBeef[1]);
        Customer result = assertDoesNotThrow(() -> userFileDAO.createCustomer(newCustomer),
                            "Unexpected exception thrown");
        assertNotNull(result);
        Customer actual = (Customer) assertDoesNotThrow(() -> userFileDAO.GetUser("John"),
                            "Unexpected exception thrown");
        assertEquals(newCustomer.getCart(), actual.getCart());
        assertEquals(newCustomer.getUsername(), actual.getUsername());
    }

    @Test
    public void testCreateAdmin(){
        Admin newAdmin = new Admin("Giant Rat");
        Admin result = assertDoesNotThrow(() -> userFileDAO.createAdmin(newAdmin),
                            "Unexpected exception thrown");
        assertNotNull(result);
        Admin actual = (Admin) assertDoesNotThrow(() -> userFileDAO.GetUser("Giant Rat"),
                            "Unexpected exception thrown");
        assertEquals(newAdmin.getUsername(), actual.getUsername());
    }

    // TODO: Implement tests related to CartBeef
    @Test
    public void testCheckout(){}

    @Test
    public void testAddToCart(){
    }

    @Test
    public void testRemoveFromCart(){}

    @Test
    public void clearCart(){}

    // TODO: Implement defensive testing, might not use all of these
    @Test public void testDeleteUserAbsent(){
        boolean result = assertDoesNotThrow(() -> userFileDAO.DeleteUser("Josh Allen"),
                                "Unexpected exception thrown");
        assertEquals(userFileDAO.users.size(), testUsers.length);
        assertFalse(result);
    }
    
    @Test public void testCreateAdminPresent(){
        Admin existAdmin = new Admin("Wendy");
        Admin result = assertDoesNotThrow(() -> userFileDAO.createAdmin(existAdmin),
                            "Unexpected exception thrown");
        assertNull(result);
    }

    @Test public void testCreateCustomerPresent(){
        Customer customer = new Customer("Joe", new CartBeef[1]);
        Customer result = assertDoesNotThrow(() -> userFileDAO.createCustomer(customer),
                            "Unexpected exception thrown");
        assertNull(result);
    }

    @Test public void testGetUserAbsent(){
        User result = assertDoesNotThrow(() -> userFileDAO.GetUser("Zuckerberg"),
                        "Unexpected exception thrown");
        assertNull(result);
    }

    @Test public void testAddToCartAdmin(){}

    @Test public void testAddToCartPresent(){}

    @Test public void testRemoveFromCartAbsent(){}
}
