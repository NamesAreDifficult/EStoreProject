package com.estore.api.estoreapi.persistence;

import com.estore.api.estoreapi.users.User;
import com.estore.api.estoreapi.users.Customer;

import java.io.IOException;

public interface UserDAO {
    
    /**
     * Creates and saves a {@linkplain User user}
     * 
     * @param user {@linkplain User user} object to be created and saved
     *
     * @return new {@link User user} if successful, null otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    User CreateUser(User user) throws IOException; 

    /**
     * Retrieves a {@linkplain User user} with the given username
     * 
     * @param username The username of the {@link User user} to get
     * 
     * @return a {@link User user} object with the matching username
     * <br>
     * null if no {@link User user} with a matching username is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    User GetUser(String username) throws IOException;

    /**
     * Checks if a {@linkplain User user} is an admin
     * 
     * @param username The username of the {@link User user} to check
     * 
     * @return a {@link User user} object with the matching username
     * <br>
     * null if no {@link User user} with a matching username is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    boolean IsAdmin(String username) throws IOException;

    /**
     * Checks out a customer's shopping cart {@linkplain Customer customer}
     * 
     * @param username The username of the {@link Customer customer} to checkout
     * 
     * @return a {@link Customer customer} object with the matching username
     * <br>
     * null if no {@link Customer customer} with a matching username is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    boolean Checkout(String username) throws IOException;

    /**
     * Adds to a customer's shopping cart {@linkplain Customer customer}
     * 
     * @param username The username of the {@link Customer customer} to checkout
     * 
     * @return a {@link User user} object with the matching username
     * <br>
     * null if no {@link User user} with a matching username is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Customer AddToCart(String username) throws IOException;

    /**
     * Clears a customer's shopping cart {@linkplain Customer customer}
     * 
     * @param username The username of the {@link Customer customer} to clear the shopping cart of
     * 
     * @return a {@link User user} object with the matching username
     * <br>
     * null if no {@link User user} with a matching username is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Customer ClearCart(String username) throws IOException;

    /**
     * Retrieves all {@linkplain User user}
     * 
     * @return An array of {@link User user} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    User[] GetUsers()throws IOException;

    /**
     * Deletes a {@linkplain User user} with the given username
     * 
     * @param username The username of the {@link User user}
     * 
     * @return true if the {@link User user} was deleted
     * <br>
     * false if user with the given username does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean DeleteUser(String username) throws IOException;
}
