package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.estore.api.estoreapi.products.CartBeef;
import com.estore.api.estoreapi.users.Admin;
import com.estore.api.estoreapi.users.Customer;
import com.estore.api.estoreapi.users.User;
import com.estore.api.estoreapi.users.CreditCard;
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
  private String customerFilename; // Filename to read from and write to for storing customers
  private String adminFilename; // Filename to read from and write to for storing admins

  public UserFileDAO(@Value("${customer.file}") String customerFilename, @Value("${admin.file}") String adminFilename,
      ObjectMapper objectMapper) {
    this.customerFilename = customerFilename;
    this.adminFilename = adminFilename;
    this.objectMapper = objectMapper;
    try {
      load(); // load the users from the file
    } catch (IOException err) {
      this.users = new TreeMap<>();
      Admin defaultAdmin = new Admin("admin", "admin");
      this.users.put(defaultAdmin.getUsername(), defaultAdmin);
    }
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
    Customer[] customerArray = this.getCustomers();
    Admin[] adminArray = this.getAdmins();

    // Serializes the Java Objects to JSON objects into the file
    // writeValue will thrown an IOException if there is an issue
    // with the file or reading from the file
    try {
      objectMapper.writeValue(new File(customerFilename), customerArray);
      objectMapper.writeValue(new File(adminFilename), adminArray);
    } catch (IOException err) {

      // Creates the file then writes to it
      FileUtility.createFileWithDirectories(customerFilename);
      FileUtility.createFileWithDirectories(adminFilename);
      objectMapper.writeValue(new File(customerFilename), customerArray);
      objectMapper.writeValue(new File(adminFilename), adminArray);
    }
    return true;
  }

  /**
   * Returns all the customers
   * 
   * @return an array of all the customers
   */
  private Customer[] getCustomers() {
    ArrayList<Customer> customerArrayList = new ArrayList<>();
    for (User user : this.users.values()) {
      if (user instanceof Customer) {
        customerArrayList.add((Customer) user);
      }
    }

    int i = 0;
    Customer[] result = new Customer[customerArrayList.size()];
    for (Customer customer : customerArrayList) {
      result[i++] = customer;
    }

    return result;
  }

  /**
   * Returns all the admins
   * 
   * @return an array of all the admins
   */
  private Admin[] getAdmins() {
    ArrayList<Admin> adminArrayList = new ArrayList<>();
    for (User user : this.users.values()) {
      if (user instanceof Admin) {
        adminArrayList.add((Admin) user);
      }
    }

    int i = 0;
    Admin[] result = new Admin[adminArrayList.size()];
    for (Admin admin : adminArrayList) {
      result[i++] = admin;
    }

    return result;
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
    Customer[] customerArray = objectMapper.readValue(new File(customerFilename), Customer[].class);
    Admin[] adminArray = objectMapper.readValue(new File(adminFilename), Admin[].class);

    // Adds each user to the map
    for (Customer customer : customerArray) {
      users.put(customer.getUsername(), customer);
    }

    for (Admin admin : adminArray) {
      users.put(admin.getUsername(), admin);
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

  /**
   * Returns a customer if the username refers to a customer
   */
  private Customer getCustomer(String username) throws IOException {
    synchronized (users) {
      User user = getUser(username);
      if(user == null)
        return null;
      // Check if the user is an admin
      if (user.isAdmin())
        return null;

      // Cast the user to a customer
      return (Customer) user;
    }
  }

  /**
   ** {@inheritDoc}
   */
  @Override
  public User getUser(String username) throws IOException {
    synchronized (users) {
      return users.get(username);
    }
  }

  /**
   ** {@inheritDoc}
   */
  @Override
  public User[] getUsers() throws IOException {
    synchronized (users) {
      return getUserArray();
    }
  }

  /**
   ** {@inheritDoc}
   */
  @Override
  public boolean deleteUser(String username) throws IOException {

    synchronized (users) {
      if (users.containsKey(username)) {
        users.remove(username);
        return save();
      } else {
        return false;
      }
    }
  }

  /**
   ** {@inheritDoc}
   */
  @Override
  public boolean isAdmin(String username) throws IOException {
    synchronized (users) {
      User user = getUser(username);

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
  public boolean checkout(String username, String cardNumber) throws IOException {
    synchronized (users) {
      Customer customer = getCustomer(username);
      if (customer == null){
        return false;
      }
      CreditCard card = customer.getCard(cardNumber);
      if (card == null){
        return false;
      }
      boolean ret = customer.getCart().checkout();
      save();
      return ret;
    }
  }

  /**
   ** {@inheritDoc}
   */
  @Override
  public Boolean addToCart(String username, int beefId, BigDecimal weight) throws IOException {
    synchronized (users) {

      Customer customer = getCustomer(username);
      if(customer == null){
        return false;
      }
      Boolean ret = customer.getCart().addToCart(new CartBeef(beefId, weight));
      save();
      return ret;
    }
  }

  /**
   ** {@inheritDoc}
   */
  @Override
  public Boolean removeFromCart(String username, int beefId) throws IOException {
    synchronized (users) {

      Customer customer = getCustomer(username);
      if(customer == null){
        return false;
      }
      Boolean ret = customer.getCart().removeFromCart(beefId);
      save();
      return ret;
    }
  }

  /**
   ** {@inheritDoc}
   */
  @Override
  public Boolean clearCart(String username) throws IOException {
    synchronized (users) {

      Customer customer = getCustomer(username);
      if(customer == null){
        return false;
      }
      customer.getCart().clearCart();
      save();
      return true;
    }
  }

  /**
   ** {@inheritDoc}
   */
  @Override
  public Customer createCustomer(Customer customer) throws IOException {
    synchronized (users) {
      // Checks duplicate username
      if (users.containsKey(customer.getUsername())) {
        return null;
      }

      // Adds new user
      users.put(customer.getUsername(), customer);
      save();
      return customer;
    }
  }

  /**
   ** {@inheritDoc}
   */
  @Override
  public Admin createAdmin(Admin admin) throws IOException {
    synchronized (users) {
      // Checks duplicate username
      if (users.containsKey(admin.getUsername())) {
        return null;
      }

      // Adds new user
      users.put(admin.getUsername(), admin);
      save();
      return admin;
    }
  }

  /**
   ** {@inheritDoc}
   */
  @Override
  public Boolean addCard(String username, CreditCard creditCard) throws IOException {
    synchronized (users) {

      Customer customer = getCustomer(username);
      if (customer == null){
        return false;
      }
      Boolean ret = customer.addCard(creditCard);
      save();
      return ret;
    }
  }

  /**
   ** {@inheritDoc}
   */
  @Override
  public Boolean removeCard(String username, CreditCard creditCard) throws IOException {
    synchronized (users) {

      Customer customer = getCustomer(username);
      if (customer == null){
        return false;
      }
      Boolean ret = customer.removeCard(creditCard);
      save();
      return ret;
    }
  }

  /**
   ** {@inheritDoc}
   */
  @Override
  public CreditCard[] getCards(String username) throws IOException {
    synchronized (users) {
      Customer customer = getCustomer(username);
      if (customer == null){
        return null;
      }
      CreditCard[] ret = customer.getCards();
      save();
      return ret;
    }
  }
  
  /**
   ** {@inheritDoc}}
   */
  public User loginUser(String username, String password) throws IOException{
    User user = getUser(username);
    if(user == null){
      return null;
    }
    if(user.getPassword().equals(password)){
      return user;
    }
    return null;
  }

  /**
   * {@inheritDoc}}
   */
  public int updatePassword(String username, String oldPassword, String newPassword) throws IOException{
    User targetUser = getUser(username);
    if(targetUser == null){
      return 1;
    }
    if(!targetUser.getPassword().equals(oldPassword)){
      return 2;
    }
    targetUser.setPassword(newPassword);
    save();
    return 0;
  }
}
