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
import java.util.logging.Logger;
import com.estore.api.estoreapi.persistence.InventoryDAO;
import com.estore.api.estoreapi.persistence.UserDAO;
import com.estore.api.estoreapi.products.Beef;
import com.estore.api.estoreapi.products.CartBeef;
import com.estore.api.estoreapi.users.Customer;
import com.estore.api.estoreapi.users.User;
import com.estore.api.estoreapi.users.CreditCard;

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
                    try{
                        Beef copyBeef = new Beef(cartBeef.getId(), retrievedBeef.getCut(), cartBeef.getWeight(),
                                retrievedBeef.getGrade(), retrievedBeef.getPrice(), retrievedBeef.getImageUrl());
                        beefs[index++] = copyBeef;
                    }catch(NullPointerException e){
                        customer.getCart().removeFromCart(cartBeef.getId());
                    }
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
     * @param cardNumber - String containing the credit card number being used at checkout
     * 
     * @return ResponseEntity with boolean depending on success HTTP status
     *         of CREATED<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("/checkout/{username}/{cardNumber}")
    public ResponseEntity<Boolean> checkoutShoppingCart(@PathVariable String username, @PathVariable String cardNumber) {
        try {
            Customer customer = this.getCustomer(username);
            if (customer != null) {
                CreditCard newCard = customer.getCard(cardNumber);
                if (newCard == null){
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }
                boolean ret = userDAO.Checkout(customer.getUsername(), cardNumber);
                if (ret) {
                    return new ResponseEntity<Boolean>(ret, HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<Boolean>(ret, HttpStatus.BAD_REQUEST);
                }
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
            Beef retrievedBeef = this.inventoryDao.getBeef(cartBeef.getId());

            // Checks if beef exists
            if (retrievedBeef != null) {
                Customer customer = this.getCustomer(username);

                if (retrievedBeef.getWeight() < cartBeef.getWeight()) {
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }

                // Checks if user exists and is a customer
                if (customer != null) {
                    Beef copyBeef = new Beef(cartBeef.getId(), retrievedBeef.getCut(), -1 * cartBeef.getWeight(),
                                    retrievedBeef.getGrade(), retrievedBeef.getPrice(), retrievedBeef.getImageUrl());
                    inventoryDao.updateBeef(copyBeef);
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
                for (CartBeef retrievedBeef : customer.getCart().getContents()){
                    Beef inventoryBeef = inventoryDao.getBeef(retrievedBeef.getId());
                    if (retrievedBeef != null) {
                        Beef copyBeef = new Beef(retrievedBeef.getId(), inventoryBeef.getCut(), retrievedBeef.getWeight(),
                                        inventoryBeef.getGrade(), inventoryBeef.getPrice(), inventoryBeef.getImageUrl());
                        inventoryDao.updateBeef(copyBeef);
                    }
                }
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
                CartBeef retrievedBeef = customer.getCart().getCartBeef(beefId);
                Beef inventoryBeef = inventoryDao.getBeef(beefId);
                if (retrievedBeef != null) {
                    Beef copyBeef = new Beef(retrievedBeef.getId(), inventoryBeef.getCut(), retrievedBeef.getWeight(),
                                    inventoryBeef.getGrade(), inventoryBeef.getPrice(), inventoryBeef.getImageUrl());
                    inventoryDao.updateBeef(copyBeef);
                }
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