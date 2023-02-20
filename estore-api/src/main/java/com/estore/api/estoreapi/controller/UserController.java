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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.estore.api.estoreapi.persistence.UserDAO;
import com.estore.api.estoreapi.users.User;

/**
 * Handles the REST API requests for the Beef resource
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
  public ResponseEntity<User[]> getUsers() {
    try {
      return new ResponseEntity<User[]>(this.userDao.GetUsers(), HttpStatus.OK);
    } catch (IOException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getUser() {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

}