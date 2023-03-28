package com.estore.api.estoreapi.persistence;

import java.io.IOException;

import com.estore.api.estoreapi.users.User;
import com.estore.api.estoreapi.users.Admin;
import com.estore.api.estoreapi.users.Customer;

public interface UserDAO {

    /**
     * Creates and saves a {@linkplain Customer customer}
     * 
     * @param user {@linkplain Customer customer} object to be created and saved
     *
     * @return new {@link Customer customer} if successful, null otherwise
     * 
     * @throws IOException if an issue with underlying storage
     */
    Customer createCustomer(Customer customer) throws IOException;

    /**
     * Creates and saves a {@linkplain Admin admin}
     * 
     * @param user {@linkplain Admin admin} object to be created and saved
     *
     * @return new {@link Admin admin} if successful, null otherwise
     * 
     * @throws IOException if an issue with underlying storage
     */
    Admin createAdmin(Admin admin) throws IOException;

    /**
     * Retrieves a {@linkplain User user} with the given username
     * 
     * @param username The username of the {@link User user} to get
     * 
     * @return a {@link User user} object with the matching username
     *         <br>
     *         null if no {@link User user} with a matching username is found
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
     *         <br>
     *         null if no {@link User user} with a matching username is found
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
     *         <br>
     *         null if no {@link Customer customer} with a matching username is
     *         found
     * 
     * @throws IOException if an issue with underlying storage
     */
    boolean Checkout(String username) throws IOException;

    /**
     * Adds to a customer's shopping cart {@linkplain Customer customer}
     * 
     * @param username The username of the {@link Customer customer to add to cart
     * of
     * 
     * @param beefid   The id of the beef to remove
     * 
     * @return a {@link Boolean boolean} depending on if the object was successfully added
     * 
     * @throws IOException if an issue with underlying storage
     */
    Boolean AddToCart(String username, int beefId, float weight) throws IOException;

    /**
     * Clears a customer's shopping cart {@linkplain Customer customer}
     * 
     * @param username The username of the {@link Customer customer} to clear the
     *                 shopping cart of
     * @param beefId   ID of the beef to add the the shopping cart
     * 
     * @param weight   Weight of the beef to add to the shopping cart
     * 
     * @return a {@link Boolean boolean} depending on if the cart was successfully cleared
     * 
     * @throws IOException if an issue with underlying storage
     */
    Boolean ClearCart(String username) throws IOException;

    /**
     * Retrieves all {@linkplain User user}
     * 
     * @return An array of {@link User user} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    User[] GetUsers() throws IOException;

    /**
     * Deletes a {@linkplain User user} with the given username
     * 
     * @param username The username of the {@link User user}
     * 
     * @return true if the {@link User user} was deleted
     *         <br>
     *         false if user with the given username does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean DeleteUser(String username) throws IOException;

    /**
     * Removes a {@linkplain Beef beef} from a shopping cart of a
     * {@linkplain} User user}
     * 
     * @param username The username of the {@link User user}
     * 
     * @param beefid   The id of the beef to remove
     * 
     * @return a {@link Boolean boolean} object whether the item was removed successfully
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Boolean RemoveFromCart(String username, int beefId) throws IOException;
}
