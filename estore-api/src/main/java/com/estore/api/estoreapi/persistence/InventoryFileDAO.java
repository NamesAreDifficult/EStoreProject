package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.estore.api.estoreapi.products.Beef;

/**
 * Implements the functionality for JSON file-based peristance for Beef
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author NamesAreDifficult
 */
@Component
public class InventoryFileDAO implements InventoryDAO {
  Map<Integer, Beef> inventory;   // Provides a local cache of the inventory objects
                             // so that we don't need to read from the file
                             // each time
  private ObjectMapper objectMapper;  // Provides conversion between Beef
                                      // objects and JSON text format written
                                      // to the file
  private static int nextId;  // The next Id to assign to a new Beef
  private String filename;    // Filename to read from and write to

  /**
  * Creates a Beef File Data Access Object
  * 
  * @param filename Filename to read from and write to
  * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
  * 
  * @throws IOException when file cannot be accessed or read from
  */
  public InventoryFileDAO(@Value("${inventory.file}") String filename, ObjectMapper objectMapper) throws IOException {
      this.filename = filename;
      this.objectMapper = objectMapper;
      load();  // load the beef from the file
  }

  /**
  * Generates the next id for a new {@linkplain Beef beef}
  * 
  * @return The next id
  */
  private synchronized static int nextId() {
    int id = nextId;
    ++nextId;
    return id;
  }

  /**
  * Generates an array of {@linkplain Beef beef} from the tree map
  * 
  * @return  The array of {@link Beef beef}, may be empty
  */
  private Beef[] getBeefArray() {
    return getBeefArray(null, null);
  }

  /**
  * Generates an array of {@linkplain Beef beef} from the tree map for any
  * {@linkplain Beef beef} that contains the text specified by containsText
  * <br>
  * If containsText is null, the array contains all of the {@linkplain Beef beef}
  * in the tree map
  * 
  * @return  The array of {@link Beef beef}, may be empty
  */
  private Beef[] getBeefArray(String grade, String cut) { // if containsText == null, no filter
    ArrayList<Beef> beefArrayList = new ArrayList<>();

    for (Beef beef : inventory.values()){
      if ((grade == null && cut == null) || 
          (beef.getCut().contains(cut) && beef.getGrade().contains(grade)) ||
          (beef.getCut().contains(cut) && grade == null) ||
          (beef.getGrade().contains(grade) && cut == null) ) { 
        beefArrayList.add(beef);
      }
    }

    Beef[] beefArray = new Beef[beefArrayList.size()];
    beefArrayList.toArray(beefArray);
    return beefArray;
  }

  /**
  * Saves the {@linkplain Beef beef} from the map into the file as an array of JSON objects
  * 
  * @return true if the {@link Beef beef} were written successfully
  * 
  * @throws IOException when file cannot be accessed or written to
  */
  private boolean save() throws IOException {
    Beef[] beefArray = getBeefArray();

    // Serializes the Java Objects to JSON objects into the file
    // writeValue will thrown an IOException if there is an issue
    // with the file or reading from the file
    objectMapper.writeValue(new File(filename), beefArray);
    return true;
  }

  /**
  * Loads {@linkplain Beef beef} from the JSON file into the map
  * <br>
  * Also sets next id to one more than the greatest id found in the file
  * 
  * @return true if the file was read successfully
  * 
  * @throws IOException when file cannot be accessed or read from
  */
  private boolean load() throws IOException {
    inventory = new TreeMap<>();
    nextId = 0;

    // Deserializes the JSON objects from the file into an array of beefInventory
    // readValue will throw an IOException if there's an issue with the file
    // or reading from the file
    Beef[] beefArray = objectMapper.readValue(new File(filename),Beef[].class);

    // Add each beef to the tree map and keep track of the greatest id
    for (Beef beef : beefArray){
      inventory.put(beef.getId(), beef);
      if(beef.getId() > nextId) { 
         nextId = beef.getId();
      }
    }
    // Make the next id one greater than the maximum from the file
    ++nextId;
    return true;
  }

  /**
  ** {@inheritDoc}
  */
  @Override
  public Beef[] getBeef() {
    synchronized(inventory) {
      return getBeefArray();
    }
  }

  /**
  ** {@inheritDoc}
  */
  @Override
  public Beef[] findBeef(String cut, String grade) {
    synchronized(inventory) {
      return getBeefArray(cut, grade);
    }
  }

  /**
  ** {@inheritDoc}
  */
  @Override
  public Beef getBeef(int id) {
    synchronized(inventory) {
      if (inventory.containsKey(id)){
        return inventory.get(id);
      } else {
        return null;
      }
    }
  }

  /**
  ** {@inheritDoc}
  */
  @Override
  public Beef createBeef(Beef beef) throws IOException {
    synchronized(inventory) {
      // We create a new beef object because the id field is immutable
      // and we need to assign the next unique id
      for(Beef existingBeef : inventory.values()){

        if(existingBeef.getName().equals(beef.getName()) && existingBeef.getGrade().equals(beef.getGrade())){
          return null;
        }
      }
      Beef newBeef = new Beef(nextId(),beef.getCut(), beef.getWeight(), beef.getGrade());
      inventory.put(newBeef.getId(), newBeef);
      save(); // may throw an IOException
      return newBeef;
    }
  }

  /**
  ** {@inheritDoc}
  */
  @Override
  public boolean deleteBeef(int id) throws IOException {
    synchronized(inventory) {
      if (inventory.containsKey(id)) {
        inventory.remove(id);
        return save();
      }else{
        return false;
      }
    }
  }
}
