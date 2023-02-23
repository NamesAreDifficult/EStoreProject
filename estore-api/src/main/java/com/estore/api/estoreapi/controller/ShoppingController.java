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

@RestController
@RequestMapping("shopping")
public class ShoppingController {
    private static final Logger LOG = Logger.getLogger(InventoryController.class.getName());
    private InventoryDAO inventoryDao;
    private UserDAO userDAO;

    public ShoppingController(InventoryDAO inventoryDao, UserDAO userDAO) {
        this.inventoryDao = inventoryDao;
        this.userDAO = userDAO;
    }

    @PutMapping("/checkout/{username}")
    public ResponseEntity<Boolean> CheckoutShoppingCart(@PathVariable String username) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PutMapping("/{username}")
    public ResponseEntity<Boolean> AddToShoppingCart(@PathVariable String username, @PathVariable int beefId) {
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