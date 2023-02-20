package com.estore.api.estoreapi.persistence;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.estore.api.estoreapi.users.User;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



public class UserFileDAO implements UserDAO {

    Map<String, User> users;   // Provides a local cache of the inventory objects
                             // so that we don't need to read from the file
                             // each time
    private ObjectMapper objectMapper;  // Provides conversion between Beef
                                        // objects and JSON text format written
                                        // to the file
    private String filename;    // Filename to read from and write to


    public UserFileDAO(@Value("${user.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the users from the file
    }

    
    /**
  * Saves the {@linkplain User user} from the map into the file as an array of JSON objects
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

    // Deserializes the JSON objects from the file into an array of beefInventory
    // readValue will throw an IOException if there's an issue with the file
    // or reading from the file
    User[] userArray = objectMapper.readValue(new File(filename),User[].class);

    // Adds each user to the map
    for (User user : userArray){
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
  * @return  The array of {@link Beef beef}, may be empty
  */
  private User[] getUserArray() {
    ArrayList<User> userArrayList = new ArrayList<>();

    for (User user : users.values()){
        userArrayList.add(user);
      
    }

    User[] userArray = new User[userArrayList.size()];
    userArrayList.toArray(userArray);
    return userArray;
  }
  
}
