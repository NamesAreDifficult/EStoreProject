package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.estore.api.estoreapi.users.User;
import com.estore.api.estoreapi.users.Customer;
import com.estore.api.estoreapi.users.Admin;
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
        testUsers[0] = new Customer("Joe", new CartBeef[1]);
        testUsers[1] = new Customer("Candice", new CartBeef[5]);
        testUsers[2] = new Admin("Wendy");

        when(mockObjectMapper.readValue(new File("test.txt"),User[].class)).thenReturn(testUsers);

        userFileDAO = new UserFileDAO("test.txt", mockObjectMapper);
    }

    @Test
    public void testGetUsers(){
        User[] users = assertDoesNotThrow(() -> userFileDAO.GetUsers(),
                                "Unexpected exception thrown");
        assertEquals(users.length, testUsers.length);
        assertArrayEquals(users, testUsers);
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
    public void testCreateAdmin(){}
}
