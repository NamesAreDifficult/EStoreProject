package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.estore.api.estoreapi.products.CartBeef;
import com.estore.api.estoreapi.users.Customer;
import com.estore.api.estoreapi.users.User;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserFileDAO implements UserDAO {

  Map<String, User> users; // Provides a local cache of the inventory objects
  // so that we don't need to read from the file
  // each time
  private ObjectMapper objectMapper; // Provides conversion between user
                                     // objects and JSON text format written
                                     // to the file
  private String filename; // Filename to read from and write to

  public UserFileDAO(@Value("${user.file}") String filename, ObjectMapper objectMapper) throws IOException {
    this.filename = filename;
    this.objectMapper = objectMapper;
    load(); // load the users from the file
  }

  /**
   * Saves the {@linkplain User user} from the map into the file as an array of
   * JSON objects
   * 
   * @return true if the {@link User user} were written successfully
   * 
   * @throws IOException when file cannot be accessed or written to
   */
  private boolean save() throws IOException {
    User[] userArray = getUserArray();

    // Serializes the Java Objects to JSON objects into the file
    // writeValue will thrown an IOException if there is an issue
    // with the file or reading from the file
    objectMapper.writeValue(new File(filename), userArray);
    return true;
  }

  /**
   * Loads {@linkplain User user} from the JSON file into the map
   * 
   * @return true if the file was read successfully
   * 
   * @throws IOException when file cannot be accessed or read from
   */
  private boolean load() throws IOException {
    users = new TreeMap<>();

    // Deserializes the JSON objects from the file into an array of userArray
    // readValue will throw an IOException if there's an issue with the file
    // or reading from the file
    User[] userArray = objectMapper.readValue(new File(filename), User[].class);

    // Adds each user to the map
    for (User user : userArray) {
      users.put(user.getUsername(), user);
    }
    return true;
  }

  /**
   * Generates an array of {@linkplain User user} from the tree map for any
   * {@linkplain User user} that contains the text specified by containsText
   * <br>
   * If containsText is null, the array contains all of the {@linkplain User user}
   * in the tree map
   * 
   * @return The array of {@link User user}, may be empty
   */
  private User[] getUserArray() {
    ArrayList<User> userArrayList = new ArrayList<>();

    for (User user : users.values()) {
      userArrayList.add(user);
    }

    User[] userArray = new User[userArrayList.size()];
    userArrayList.toArray(userArray);
    return userArray;
  }

  /*
   * Returns a customer if the username refers to a customer
   */
  private Customer GetCustomer(String username) throws IOException {
    synchronized (users) {
      User user = GetUser(username);

      // Check if the user is an admin
      if (user.isAdmin())
        return null;

      // Cast the user to a customer
      Customer customer = (Customer) user;

      return customer;
    }
  }

  /**
   ** {@inheritDoc}
   */
  @Override
  public User CreateUser(User user) throws IOException {
    synchronized (users) {
      // Checks duplicate username
      if (users.containsKey(user.getUsername())) {
        return null;
      }

      // Adds new user
      users.put(user.getUsername(), user);
      save();
      return user;
    }
  }

  /**
   ** {@inheritDoc}
   */
  @Override
  public User GetUser(String username) throws IOException {
    synchronized (users) {
      return users.get(username);
    }
  }

  /**
   ** {@inheritDoc}
   */
  @Override
  public User[] GetUsers() throws IOException {
    synchronized (users) {
      return getUserArray();
    }
  }

  /**
   ** {@inheritDoc}
   */
  @Override
  public boolean DeleteUser(String username) throws IOException {

    synchronized (users) {
      User removed_user = users.remove(username);
      return (removed_user != null);
    }
  }

  /**
   ** {@inheritDoc}
   */
  @Override
  public boolean IsAdmin(String username) throws IOException {
    synchronized (users) {
      User user = GetUser(username);

      if (user != null) {
        return user.isAdmin();
      }
      // Returns false if user is not found
      return false;

    }
  }

  /**
   ** {@inheritDoc}
   */
  @Override
  public boolean Checkout(String username) throws IOException {
    synchronized (users) {
      return true; // Todo implement

    }
  }

  /**
   ** {@inheritDoc}
   */
  @Override
  public Customer AddToCart(String username, int beefId, float weight) throws IOException {
    synchronized (users) {

      Customer customer = GetCustomer(username);

      customer.addToCart(new CartBeef(beefId, weight));
      return customer;
    }
  }

  /**
   ** {@inheritDoc}
   */
  @Override
  public Customer RemoveFromCart(String username, int beefId) throws IOException {
    synchronized (users) {

      Customer customer = GetCustomer(username);

      customer.removeFromCart(beefId);
      return customer;
    }
  }

  /**
   ** {@inheritDoc}
   */
  @Override
  public Customer ClearCart(String username) throws IOException {
    synchronized (users) {

      Customer customer = GetCustomer(username);

      customer.clearCart();
      return customer;
    }
  }

}
