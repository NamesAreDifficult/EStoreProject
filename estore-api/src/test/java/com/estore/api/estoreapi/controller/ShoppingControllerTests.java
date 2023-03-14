package com.estore.api.estoreapi.controller;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import com.estore.api.estoreapi.persistence.InventoryDAO;
import com.estore.api.estoreapi.persistence.UserDAO;
import com.estore.api.estoreapi.users.Customer;
import com.estore.api.estoreapi.users.User;

@Tag("Controller-tier")
public class ShoppingControllerTests {
    private ShoppingController shoppingController;
    private UserDAO mockUserDAO;
    private InventoryDAO mockInventoryDAO;

/**
 * Before each test, create a new ShoppingController object and inject 
 * a mock User DAO
 */

@BeforeEach
public void setupShoppingController() {
    mockUserDAO = mock(UserDAO.class);
    shoppingController = new ShoppingController(mockInventoryDAO, mockUserDAO);
}


@Test
public void testCheckoutShoppingCart() throws IOException {

//     //Setup
//    User user = new User("name");

//    //when the username is passed in, our mock User DAO will return the User object 
//    when(mockUserDAO.GetUser(user.getUsername())).thenReturn(user);

//    //Invoke 
//    ResponseEntity<User> response = Shopping

}

@Test 
public void testAddToShoppingCart() throws IOException{


}

@Test 
public void testClearShoppingCart(){

}

@Test
public void testRemoveFromShoppingCart(){
    
}



}
