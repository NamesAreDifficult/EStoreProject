package com.estore.api.estoreapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import com.estore.api.estoreapi.persistence.UserDAO;
import com.estore.api.estoreapi.users.Admin;
import com.estore.api.estoreapi.users.CreditCard;
import com.estore.api.estoreapi.users.Customer;
import com.estore.api.estoreapi.users.User;


/**
 * Handles the REST API requests for the users
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST
 * API
 * method handler to the Spring framework
 * 
 * @author Brendan Battisti
 */

@RestController
@RequestMapping("user")
public class UserController {
  private static final Logger LOG = Logger.getLogger(UserController.class.getName());
  private UserDAO userDao;

  /**
   * Creates a REST API controller to reponds to requests
   * 
   * @param UserDAO The {@link UserDAO User Data Access Object} to perform CRUD
   *                operations
   *                <br>
   *                This dependency is injected by the Spring Framework
   */
  public UserController(UserDAO userDao) {
    this.userDao = userDao;
  }

  /**
   * Responds to the GET request for all {@linkplain User user}
   * 
   * @return ResponseEntity with array of {@link User user} objects (may be empty)
   *         and
   *         HTTP status of OK<br>
   *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
   */
  @GetMapping("")
  public ResponseEntity<User[]> GetUsers() {
    try {
      return new ResponseEntity<User[]>(this.userDao.GetUsers(), HttpStatus.OK);
    } catch (IOException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Responds to the GET request for a {@linkplain User user}
   * 
   * @param username - username of the user
   * 
   * @return ResponseEntity with a {@link User user} object (may be empty)
   *         and
   *         HTTP status of OK<br>
   *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
   */
  @GetMapping("/{username}")
  public ResponseEntity<User> getUser(@PathVariable String username) {
    try {
      User user = this.userDao.GetUser(username);
      // User not found
      if (user == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      // User found
      return new ResponseEntity<User>(user, HttpStatus.OK);
    } catch (IOException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Retrieves {@linkplain CreditCard[] creditCards} given a username
   * 
   * @param username - username of the user
   * 
   * @return ResponseEntity with a {@link CreditCard[] creditCards} (may be empty)
   *         and
   *         HTTP status of OK<br>
   *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR or NOT_FOUND otherwise
   */
  @GetMapping("/cards/{username}")
  public ResponseEntity<CreditCard[]> getCards(@PathVariable String username) {
    try {
      Customer customer = this.getCustomer(username);
      // User not found
      if (customer == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<CreditCard[]>(this.userDao.getCards(username), HttpStatus.OK);
    } catch (IOException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Adds a {@linkplain CreditCard creditCard} to the specified customer
   * 
   * @param username - String containing the username of the customer
   * @param creditCard - CreditCard to be added
   * 
   * @return ResponseEntity with created {@link Boolean boolean} object and HTTP
   *         status of OK<br>
   *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR if file
   *         error
   *         ResponseEntity with HTTP status of CONFLICT if card exists on user
   *         ResponseEntity with HTTP status NOT_FOUND if user is not found
   *         ResponseEntity with HTTP status BAD_REQUEST if credit card is invalid
   */
  @PostMapping("/{username}")
  public ResponseEntity<Boolean> addCard(@PathVariable String username, @RequestBody CreditCard creditCard) {
    try {
      Customer customer = this.getCustomer(username);
      if (customer == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      boolean isValid = isValid(creditCard);
      if (!isValid){
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
      boolean result = this.userDao.addCard(username, creditCard);
      if (result) {
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
      }
      else {
        return new ResponseEntity<Boolean>(result, HttpStatus.CONFLICT);
      }
    } catch (IOException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Removes a {@linkplain CreditCard creditCard} to the specified customer
   * 
   * @param username - String containing the username of the customer
   * @param cardNumber - Number of credit card to be removed
   * 
   * @return ResponseEntity with created {@link Boolean boolean} object and HTTP
   *         status of OK<br>
   *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR if file
   *         error
   *         ResponseEntity with HTTP status of BAD_REQUEST if card is not found on user
   *         ResponseEntity with HTTP status NOT_FOUND if user/card is not found
   */
  @DeleteMapping("/{username}/{cardNumber}")
    public ResponseEntity<Boolean> RemoveFromShoppingCart(@PathVariable String username, @PathVariable String cardNumber) {
        try {
            Customer customer = this.getCustomer(username);
            if (customer != null) {
                CreditCard card = customer.getCard(cardNumber);
                if (card != null){
                  boolean result = customer.removeCard(card);
                  if (result) {
                    return new ResponseEntity<Boolean>(result, HttpStatus.OK);
                  }
                  else {
                    return new ResponseEntity<Boolean>(result, HttpStatus.BAD_REQUEST);
                  }
              }
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

  /**
   * Creates a {@linkplain Customer customer} with the provided customer object
   * 
   * @param customer - The {@link Customer customer} to create
   * 
   * @return ResponseEntity with created {@link Customer customer} object and HTTP
   *         status of CREATED<br>
   *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR if file
   *         error
   *         ResponseEntity with HTTP status of CONFLICT username already exists
   */
  @PostMapping("/customer")
  public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {

    try {
      Customer newCustomer = this.userDao.createCustomer(customer);
      if (newCustomer == null) {
        return new ResponseEntity<>(HttpStatus.CONFLICT);
      }
      return new ResponseEntity<Customer>(newCustomer, HttpStatus.OK);
    } catch (IOException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Creates a {@linkplain Admin admin} with the provided admin object
   * 
   * @param admin - The {@link Admin admin} to create
   * 
   * @return ResponseEntity with created {@link Admin admin} object and HTTP
   *         status of CREATED<br>
   *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR if file
   *         error
   *         ResponseEntity with HTTP status of CONFLICT username already exists
   */
  @PostMapping("/admin")
  public ResponseEntity<Admin> createAdmin(@RequestBody Admin admin) {
    try {
      Admin newAdmin = this.userDao.createAdmin(admin);
      if (newAdmin == null) {
        return new ResponseEntity<>(HttpStatus.CONFLICT);
      }
      return new ResponseEntity<Admin>(newAdmin, HttpStatus.OK);
    } catch (IOException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Deletes a {@linkplain User user} with the provided username
   * 
   * @param username - The username of the {@linkplain User user} to delete
   * 
   * @return ResponseEntity with created {@link User user} object <br>
   *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
   */
  @DeleteMapping("/{username}")
  public ResponseEntity<User> DeleteUser(@PathVariable String username) {
    try {
      User user = this.userDao.GetUser(username);
      if(user == null){
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      userDao.DeleteUser(username);
      return new ResponseEntity<User>(HttpStatus.OK);
      }catch(IOException e) {
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
      User user = this.userDao.GetUser(username);

      // Checks if user exists and is a customer
      if (user != null && (user instanceof Customer)) {
          Customer customer = (Customer) user;
          return customer;
      }
      return null;
  }

  private boolean isValid(CreditCard creditCard){
    if (creditCard == null){
      return false;
    }
    if ((creditCard.getNumber().matches("\\d+") && creditCard.getNumber().strip().length() == 16) &&
        (creditCard.getExpiration().matches("(?:0[1-9]|1[0-2])/[0-9]{2}")) &&
        (creditCard.getCVV().matches("^[0-9]{3,4}$"))){
          return true;
    }
    else{
      return false;
    }
  }
}