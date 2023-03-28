package com.estore.api.estoreapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.logging.Logger;
import com.estore.api.estoreapi.persistence.InventoryDAO;
import com.estore.api.estoreapi.persistence.UserDAO;
import com.estore.api.estoreapi.persistence.UserFileDAO;
import com.estore.api.estoreapi.products.Beef;
import com.estore.api.estoreapi.products.CartBeef;
import com.estore.api.estoreapi.users.Customer;
import com.estore.api.estoreapi.users.User;

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
     * Gets the contents of a {@linkplain Customer customer} shopping cart
     * 
     * @param username - The {@link Customer customer} of the shopping cart
     * 
     * 
     * @return ResponseEntity with a an array of {@linkplain} CartBeef cartBeef}
     *         depending on success HTTP status
     *         ResponseEntity of not found if customer does not exist
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{username}")
    public ResponseEntity<Beef[]> getShoppingCart(@PathVariable String username) {
        try {
            Customer customer = this.getCustomer(username);
            // Checks if user exists and is a customer
            if (customer != null) {
                CartBeef[] cartBeefs = customer.getCart().getContents();
                Beef[] beefs = new Beef[cartBeefs.length];
                int index = 0;
                for (CartBeef cartBeef : customer.getCart().getContents()) {
                    Beef retrievedBeef = this.inventoryDao.getBeef(cartBeef.getId());
                    Beef copyBeef = new Beef(cartBeef.getId(), retrievedBeef.getCut(), cartBeef.getWeight(),
                                retrievedBeef.getGrade(), retrievedBeef.getPrice());
                    beefs[index++] = copyBeef;
                }

                return new ResponseEntity<Beef[]>(beefs, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
     * @param username - The {@link Customer customer} of the shopping cart
     * 
     * @param beefId   - ID of the {@link Beef beef} to add to the shopping cart
     * 
     * @return ResponseEntity with boolean depending on success HTTP status
     *         of adding to the shopping cart<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("/{username}")
    public ResponseEntity<CartBeef> AddToShoppingCart(@PathVariable String username, @RequestBody CartBeef cartBeef) {

        try {
            if (cartBeef.getWeight() <= 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            // Checks if beef exists
            if (this.inventoryDao.getBeef(cartBeef.getId()) != null) {
                Customer customer = this.getCustomer(username);

                // Checks if user exists and is a customer
                if (customer != null) {
                    userDAO.AddToCart(username, cartBeef.getId(), cartBeef.getWeight());

                    return new ResponseEntity<CartBeef>(cartBeef, HttpStatus.OK);

                }

            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Clears the shopping cart of a {@linkplain Customer customer}
     * 
     * @param username - The {@link Customer customer} of the shopping cart
     * 
     * @return ResponseEntity with boolean depending on success HTTP status
     *         of CREATED<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/clear/{username}")
    public ResponseEntity<Boolean> ClearShoppingCart(@PathVariable String username) {
        try {

            Customer customer = this.getCustomer(username);

            if (customer != null) {
                customer.getCart().clearCart();
                return new ResponseEntity<Boolean>(true, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Removes a {@linkplain Beef beef} from a {@linkplain Customer customer}
     * shopping cart
     * 
     * @param username - The {@link Customer customer} of the shopping cart
     * 
     * @param beefId   - id of the beef to remove from the shopping cart
     * 
     * @return ResponseEntity with boolean depending on success HTTP status
     *         of CREATED<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{username}/{beefId}")
    public ResponseEntity<Boolean> RemoveFromShoppingCart(@PathVariable String username, @PathVariable int beefId) {
        try {
            Customer customer = this.getCustomer(username);

            if (customer != null) {
                boolean result = userDAO.RemoveFromCart(username, beefId);
                return new ResponseEntity<Boolean>(result, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * Returns a customer given a username
     * 
     * @param username username of the customer
     * 
     * @return Customer instance
     */
    private Customer getCustomer(String username) throws IOException {
        User user = this.userDAO.GetUser(username);

        // Checks if user exists and is a customer
        if (user != null && (user instanceof Customer)) {
            Customer customer = (Customer) user;
            return customer;
        }
        return null;
    }
}