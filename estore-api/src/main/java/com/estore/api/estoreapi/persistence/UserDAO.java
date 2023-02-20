package com.estore.api.estoreapi.persistence;

import com.estore.api.estoreapi.users.User;
import java.io.IOException;

public interface UserDAO {
    
    /**
     * Creates and saves a {@linkplain User user}
     * 
     * @param user {@linkplain User user} object to be created and saved
     * <br>
     * The id of the beef object is ignored and a new uniqe id is assigned
     *
     * @return new {@link User user} if successful, null otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    User CreateUser(User user) throws IOException; 

    /**
     * Retrieves a {@linkplain User user} with the given id
     * 
     * @param username The username of the {@link User user} to get
     * 
     * @return a {@link User user} object with the matching username
     * <br>
     * null if no {@link User user} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    User GetUser(String username)throws IOException;

    /**
     * Retrieves all {@linkplain User user}
     * 
     * @return An array of {@link User user} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    User[] GetUsers()throws IOException;

    /**
     * Deletes a {@linkplain User user} with the given id
     * 
     * @param username The username of the {@link User user}
     * 
     * @return true if the {@link User user} was deleted
     * <br>
     * false if user with the given username does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean DeleteUser(String username)throws IOException;
}
