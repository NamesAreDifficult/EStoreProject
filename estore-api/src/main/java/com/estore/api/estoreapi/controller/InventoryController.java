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

import com.estore.api.estoreapi.persistence.InventoryDAO;
import com.estore.api.estoreapi.products.Beef;

/**
 * Handles the REST API requests for the Beef resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author NamesAreDifficult
 */

@RestController
@RequestMapping("inventory")
public class InventoryController {
  private static final Logger LOG = Logger.getLogger(InventoryController.class.getName());
    private InventoryDAO inventoryDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param inventoryDao The {@link InventoryDAO Inventory Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public InventoryController(InventoryDAO inventoryDao) {
        this.inventoryDao = inventoryDao;
    }

  /**
  * Responds to the GET request for a {@linkplain Beef } for the given id
  * 
  * @param id The id used to locate the {@link Beef beef}
  * 
  * @return ResponseEntity with {@link Beef beef} object and HTTP status of OK if found<br>
  * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
  * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
  */
  @GetMapping("/products/{id}")
  public ResponseEntity<Beef> getBeef(@PathVariable int id) {
    try{

      Beef retrievedBeef = inventoryDao.getBeef(id);

      if (retrievedBeef != null) {
        return new ResponseEntity<Beef>(retrievedBeef, HttpStatus.OK);
      }
      else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      
    }catch(IOException e){
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
    

  /**
  * Responds to the GET request for all {@linkplain Beef beef}
  * 
  * @return ResponseEntity with array of {@link Beef beef} objects (may be empty) and
  * HTTP status of OK<br>
  * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
  */
  @GetMapping("/products")
  public ResponseEntity<Beef[]> getBeef() {

    try{

      Beef[] retrievedBeef = inventoryDao.getBeef();

      if (retrievedBeef != null) {
        return new ResponseEntity<Beef[]>(retrievedBeef, HttpStatus.OK);
      }
      else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      
    }catch(IOException e){
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
  * Responds to the GET request for all {@linkplain Beef beef} whose grade and cut match the
  * provided search parameters
  * 
  * @param grade The grade parameter which contains the grade used to find the {@link Beef beef}
  * @param cut The cut parameter which contains teh name of the cut used to find the {@link Beef beef}
  *
  * @return ResponseEntity with array of {@link Beef beef} objects (may be empty) and
  * HTTP status of OK<br>
  * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
  * <p>
  * Example: Find all beef that are ribeyes
  * GET http://localhost:8080/products/?cut=ribeye
  */
  @GetMapping("/products/")
  public ResponseEntity<Beef[]> searchBeef(@RequestParam String name) {
    String message = String.format("GET /products/?name=%s", name);
    LOG.info(message);
    
    try{
      Beef[] retrievedBeef = inventoryDao.findBeef(name);

      if (retrievedBeef.length != 0) {
        return new ResponseEntity<Beef[]>(retrievedBeef, HttpStatus.OK);
      }      else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      
    }catch(IOException e){
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
  * Creates a {@linkplain Beef beef} with the provided beef object
  * 
  * @param beef - The {@link Beef beef} to create
  * 
  * @return ResponseEntity with created {@link Beef beef} object and HTTP status of CREATED<br>
  * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
  */
  @PostMapping("/products")
  public ResponseEntity<Beef> createBeef(@RequestBody Beef beef) {
    String message = String.format("POST /inventory/products %s", beef.toString());
    LOG.info(message);

    try{
      if(beef.getCut() == null || beef.getGrade() == null || beef.getWeight() < 0 || beef.getPrice() < 0){
        String warning = String.format("Failed to create %s, invalid attributes", beef.toString());
        LOG.warning(warning);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }

      Beef newBeef = inventoryDao.createBeef(beef);

      // newBeef will be null if a beef with the same cut and grade already exist
      // In this instance it should be updated with the weight of the newbeef
      if(newBeef == null) { 
        String warning = String.format("Failed to create %s, already exists.", beef.toString());
        LOG.warning(warning);
        return new ResponseEntity<>(HttpStatus.CONFLICT); 
      }
      String createMessage = String.format("Created %s", newBeef.toString());
      LOG.info(createMessage);
      return new ResponseEntity<Beef>(newBeef, HttpStatus.OK);
    }catch(IOException e){
      LOG.log(Level.SEVERE, e.getLocalizedMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
  * Updates the {@linkplain Beef beef} with the provided {@linkplain Beef beef} object, if it exists
  * 
  * @param beef The {@link Beef beef} to update
  * 
  * @return ResponseEntity with updated {@link Beef beef} object and HTTP status of OK if updated<br>
  * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
  * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
  */
  @PutMapping("/products")
  public ResponseEntity<Beef> updateBeef(@RequestBody Beef beef) {
    String message = String.format("PUT /inventory/products %s", beef.toString());
    LOG.info(message);
    try{
      Beef currentBeef = inventoryDao.getBeef(beef.getId());
      if (currentBeef == null){
        String doesNotExistWarning = String.format("Failed to update %s, beef does not exist", beef.toString());
        LOG.warning(doesNotExistWarning);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      if(currentBeef.getWeight() + beef.getWeight() < 0 || beef.getPrice() < 0){
        String invalidAttributeWarning = String.format("Failed to update %s, invalid attributes", beef.toString());
        LOG.warning(invalidAttributeWarning);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
      else{
        Beef updatedBeef = inventoryDao.updateBeef(beef);
        String updateMessage = String.format("Updated weight of %s", updatedBeef.toString());
        LOG.info(updateMessage);
        return new ResponseEntity<Beef>(updatedBeef, HttpStatus.OK);
      }
    } catch(IOException e) {
      LOG.log(Level.SEVERE, e.getLocalizedMessage());
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
  * Deletes a {@linkplain Beef beef} with the given id
  * 
  * @param id The id of the {@link Beef beef} to deleted
  * 
  * @return ResponseEntity HTTP status of OK if deleted<br>
  * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
  * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
  */
  @DeleteMapping("/products/{id}")
  public ResponseEntity<Beef> deleteBeef(@PathVariable int id) {
    try {
      String message = String.format("DELETE /inventory/products/%d", id);
      LOG.info(message);

      Beef beef = inventoryDao.getBeef(id);

      if(beef == null){
        String doesNotExistWarning = String.format("Failed to delete ID: %d, id does not exist.", id);
        LOG.warning(doesNotExistWarning);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      inventoryDao.deleteBeef(id);
      String deleteMessage = String.format("Deleted %s", beef.toString());
      LOG.info(deleteMessage);

      return new ResponseEntity<Beef>(beef, HttpStatus.OK);
      }catch(IOException e) {
        LOG.log(Level.SEVERE, e.getLocalizedMessage());
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }
}
