package com.estore.api.estoreapi.controller;


import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;

import com.estore.api.estoreapi.persistence.InventoryDAO;
import com.estore.api.estoreapi.persistence.UserDAO;

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

}
