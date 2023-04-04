package com.estore.api.estoreapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Logger;
import com.estore.api.estoreapi.persistence.UserDAO;
import com.estore.api.estoreapi.users.Admin;
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
   * Responds to PUT request for a {@linkplain User user} to update passwords
   * 
   * @param username - username of the user to update
   * @param newPassword - password to update the user's password to
   * @param oldPassword - current password of the user
   * @return
   */
  @PutMapping("/{username}")
  public ResponseEntity<Boolean> updatePassword(@PathVariable String username, @RequestHeader("NewAuth") String newPassword, @RequestHeader("Authorization") String oldPassword){
    LOG.info(String.format("attempted to update user: %s oldPass: %s, newPass: %s", username, oldPassword, newPassword));
    try{
      int status = this.userDao.updatePassword(username, oldPassword, newPassword);
      if(status == 0){
        return new ResponseEntity<>(HttpStatus.OK);
      }else if(status == 1){
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }else {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }
    }catch(IOException e){
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/login/{username}")
  public ResponseEntity<User> loginUser(@PathVariable String username, @RequestHeader("Authorization") String password){
    try{
      User loginUser = this.userDao.loginUser(username, password);
      if(loginUser == null){
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<User>(loginUser, HttpStatus.OK);
    }catch(IOException e){
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
}