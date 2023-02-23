package com.estore.api.estoreapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;
import com.estore.api.estoreapi.persistence.InventoryDAO;
import com.estore.api.estoreapi.persistence.UserDAO;

/**
 * Handles the REST API requests for interactions between users and inventory
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST
 * API
 * method handler to the Spring framework
 * 
 * @author Brendan Battisti
 */
@RestController
@RequestMapping("shopping")
public class ShoppingController {
    private static final Logger LOG = Logger.getLogger(InventoryController.class.getName());
    private InventoryDAO inventoryDao;
    private UserDAO userDAO;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param inventoryDao The {@link InventoryDAO Inventory Data Access Object} to
     *                     perform CRUD operations
     *                     <br>
     * @param inventoryDao The {@link UserDAO User Data Access Object} to perform
     *                     CRUD operations
     *                     <br>
     *                     This dependency is injected by the Spring Framework
     */
    public ShoppingController(InventoryDAO inventoryDao, UserDAO userDAO) {
        this.inventoryDao = inventoryDao;
        this.userDAO = userDAO;
    }

    /**
     * Checks out a {@linkplain Customer customer} shopping cart
     * 
     * @param username - The username of the {@link Customer customer} checkout
     * 
     * @return ResponseEntity with boolean depending on success HTTP status
     *         of CREATED<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("/checkout/{username}")
    public ResponseEntity<Boolean> CheckoutShoppingCart(@PathVariable String username) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * Adds a product to a {@linkplain Customer customer} shopping cart
     * 
     * @param beef - The {@link Beef beef} to create
     * 
     * @return ResponseEntity with boolean depending on success HTTP status
     *         of CREATED<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("/{username}")
    public ResponseEntity<Boolean> AddToShoppingCart(@PathVariable String username, @PathVariable int beefId,
            @PathVariable float weight) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @DeleteMapping("/clear/{username}")
    public ResponseEntity<Boolean> ClearShoppingCart(@PathVariable String username) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Boolean> RemoveFromShoppingCart(@PathVariable String username, @PathVariable int beefId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}