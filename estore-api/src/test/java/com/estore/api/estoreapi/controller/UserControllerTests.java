package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.estore.api.estoreapi.users.User;
import com.estore.api.estoreapi.persistence.UserDAO;
import com.estore.api.estoreapi.users.Admin;
import com.estore.api.estoreapi.users.Customer;
import com.estore.api.estoreapi.products.CartBeef;

@Tag("Controller-tier")
public class UserControllerTests {
    private UserController userController;
    private UserDAO mockUserDAO;

    @BeforeEach
    public void setupUserController(){
        mockUserDAO = mock(UserDAO.class);
        userController = new UserController(mockUserDAO);
    }

    // TODO: Implement these tests after GetUser is implemented
    @Test
    public void testGetUser() throws IOException{}
    @Test
    public void testGetUserNotFound() throws Exception{}
    @Test
    public void testGetUserHandleException() throws Exception{}

    @Test
    public void testCreateCustomer() throws IOException{
        Customer customer = new Customer("Jack", new CartBeef[1]);
        when(mockUserDAO.createCustomer(customer)).thenReturn(customer);
        ResponseEntity<Customer> response = userController.createCustomer(customer);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customer, response.getBody());
    }
    @Test
    public void testCreateCustomerFailed() throws IOException{
        Customer customer = new Customer("Jack", new CartBeef[1]);
        when(mockUserDAO.createCustomer(customer)).thenReturn(null);
        ResponseEntity<Customer> response = userController.createCustomer(customer);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
    @Test
    public void testCreateCustomerHandleException() throws IOException {
        Customer customer = new Customer("Jack", new CartBeef[1]);
        doThrow(new IOException()).when(mockUserDAO).createCustomer(customer);
        ResponseEntity<Customer> response = userController.createCustomer(customer);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCreateAdmin() throws IOException{
        Admin admin = new Admin("John");
        when(mockUserDAO.createAdmin(admin)).thenReturn(admin);
        ResponseEntity<Admin> response = userController.createAdmin(admin);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(admin, response.getBody());
    }
    @Test
    public void testCreateAdminFailed() throws IOException{
        Admin admin = new Admin("John");
        when(mockUserDAO.createAdmin(admin)).thenReturn(null);
        ResponseEntity<Admin> response = userController.createAdmin(admin);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
    @Test
    public void testCreateAdminHandleException() throws IOException {
        Admin admin = new Admin("John");
        doThrow(new IOException()).when(mockUserDAO).createAdmin(admin);
        ResponseEntity<Admin> response = userController.createAdmin(admin);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetUsers() throws IOException{
        User[] users = new User[2];
        users[0] = new Customer("Jack", new CartBeef[1]);
        users[1] = new Admin("John");
        when(mockUserDAO.GetUsers()).thenReturn(users);
        ResponseEntity<User[]> response = userController.GetUsers();
        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
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
    public void testDeleteUser() throws IOException {}
    @Test
    public void testDeleteUserNotFound() throws IOException {}
    @Test
    public void testDeleteUserHandleException() throws IOException {}
}

