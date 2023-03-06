package com.estore.api.estoreapi.users;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Represents an Admin User
 * 
 * @author Brendan Battisti
 */
public class Admin extends User {

    /**
   * Create an admin with a username
   * @param username The username of the admin
   * 
   * {@literal @}JsonProperty is used in serialization and deserialization
   * of the JSON object to the Java object in mapping the fields.  If a field
   * is not provided in the JSON object, the Java field gets the default Java
   * value, i.e. 0 for int
   */
    public Admin(@JsonProperty("username") String username) {
        super(username);
    }

   
    /**
     ** {@inheritDoc}
    */
    public boolean isAdmin() {
        return true;
    }

    @Override
    public boolean equals(Object other){
        if (other instanceof Customer) {
            Customer otherCustomer = (Customer) other;
            if (otherCustomer.getUsername().equals(this.getUsername())){
                return true;
            }
        }
        return false;
    }

}
