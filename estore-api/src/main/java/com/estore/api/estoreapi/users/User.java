package com.estore.api.estoreapi.users;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Represents a User
 * 
 * @author Brendan Battisti
 */
public abstract class User implements Comparable<User> {

  @JsonProperty("username")
  private final String username;
  private String password;

  /**
   * Create a user with a username
   * 
   * @param username The username of the user
   * 
   *                 {@literal @}JsonProperty is used in serialization and
   *                 deserialization
   *                 of the JSON object to the Java object in mapping the fields.
   *                 If a field
   *                 is not provided in the JSON object, the Java field gets the
   *                 default Java
   *                 value, i.e. 0 for int
   */
  public User(@JsonProperty("username") String username,
              @JsonProperty("password") String password) {
    this.username = username;
    this.password = password;
  }

  /*
   * Gets the username of the user
   * 
   * @return username of the user
   */
  public String getUsername() {
    return this.username;
  }


    /*
     * Get the password of the user
     * 
     * @return password of the user
     */
    public String getPassword(){
      return this.password;
    }
    
    /*
     * Set the user password
     */
    public void setPassword(String newPassword){
      this.password = newPassword;
    }
    /*

     * Returns if the user is an admin
     * 
     * @return true if user is an admin
     */
    @JsonProperty("admin")
    public abstract boolean isAdmin();

  /*
   * Compares the current user to another user
   * 
   * @param user other user to compare to
   * 
   * @return an integer depending on whether the current user is greater than,
   * less than, or equal to the other user
   */
  @Override
  public int compareTo(User user) {
    return this.username.compareTo(user.getUsername());
  }

  /*
   * Checks if two users are the same
   * 
   * @param other user to compare to
   * 
   * @return true if users are the same, else false
   */
  @Override
  public boolean equals(Object other) {
    if (other instanceof User) {
      User otherUser = (User) other;

      if (otherUser.getUsername() == this.getUsername()){
        return otherUser.isAdmin() == this.isAdmin();
      }
    }
    return false;
  }

  @Override
  public int hashCode(){
    return String.format("%s%s", this.getUsername(), this.getPassword()).hashCode();
  }
}