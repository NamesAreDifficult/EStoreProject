package com.estore.api.estoreapi.persistence;

import java.io.IOException;

import com.estore.api.estoreapi.products.Beef;

/**
 * Defines the interface for Beef object persistence
 * 
 * @author NamesAreDifficult
 */
public interface InventoryDAO {
    /**
     * Retrieves all {@linkplain Beef beef}
     * 
     * @return An array of {@link Beef beef} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Beef[] getBeef() throws IOException;

        /**
     * Retrieves a {@linkplain Beef beef} with the given id
     * 
     * @param id The id of the {@link Beef beef} to get
     * 
     * @return a {@link Beef beef} object with the matching id
     * <br>
     * null if no {@link Beef beef} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Beef getBeef(int id) throws IOException;

    /**
     * Finds all {@linkplain Beef beef} that match the supplied parameters
     * 
     * @param cut the text to search cuts for
     * @param grade the text to search grades for
     * 
     * @return An array of {@link Beef beef} who match the given search critera
     * 
     * @throws IOException if an issue with underlying storage
     */
    Beef[] findBeef(String cut, String grade) throws IOException;

    /**
     * Creates and saves a {@linkplain Beef beef}
     * 
     * @param beef {@linkplain Beef beef} object to be created and saved
     * <br>
     * The id of the beef object is ignored and a new uniqe id is assigned
     *
     * @return new {@link Beef beef} if successful, null otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    Beef createBeef(Beef beef) throws IOException;

    /**
     * Deletes a {@linkplain Beef beef} with the given id
     * 
     * @param id The id of the {@link Beef beef}
     * 
     * @return true if the {@link Beef beef} was deleted
     * <br>
     * false if beef with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteBeef(int id) throws IOException;
    
    /**
     * Updates and saves a {@linkplain Beef beef}
     *
     * @param {@link Beef beef} object to be updated and saved
     *
     * @return updated {@link Beef beef} if successful, null if
     * {@link Beef beef} could not be found
     *
     * @throws IOException if underlying storage cannot be accessed
     */

    Beef updateBeef(Beef beef) throws IOException;

}
