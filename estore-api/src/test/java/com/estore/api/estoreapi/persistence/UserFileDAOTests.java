package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        testUsers[1] = new Customer("Candice", new CartBeef[2]);
        testUsers[1] = new Customer("Wendy", new CartBeef[0]);

        when(mockObjectMapper.readValue(new File("doesnt_matter.txt"),User[].class)).thenReturn(testUsers);

        userFileDAO = new UserFileDAO("test.txt", mockObjectMapper);
    }

    @Test
    public void testGetUserArray(){}

    @Test
    public void testGetCustomer(){}

    @Test
    public void testGetUser(){}

    @Test
    public void testGetUsers(){}

    @Test
    public void testDeleteUser(){}

    @Test
    public void testIsAdmin(){}
}
