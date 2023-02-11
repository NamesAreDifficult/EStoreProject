package com.estore.api.estoreapi.products;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Beef entity
 * 
 * @author NamesAreDifficult
 */
public class Beef {
 // Map Json properties to class internal attributes
  @JsonProperty("id") private final int id;
  @JsonProperty("cut") private final String cut;
  @JsonProperty("weight") private final float weight;
  @JsonProperty("grade") private final String grade;

  // Format strings
  static final String NAME_FORMAT = "%s %s";
  static final String STRING_FORMAT = "Beef [id=%s cut=%s grade=%s weight=%s]";
  
  /**
   * Create a cut of Beef with the supplied paramaters
   * @param id The id of the cut
   * @param cut The type of cut
   * @param weight the weight of the cut
   * @param grade the beef marbling score of the cut of beef
   * 
   * {@literal @}JsonProperty is used in serialization and deserialization
   * of the JSON object to the Java object in mapping the fields.  If a field
   * is not provided in the JSON object, the Java field gets the default Java
   * value, i.e. 0 for int
   */
  //TODO: break up constructor to less than 100 chars per line per google java styling
  public Beef(@JsonProperty("id") int id, @JsonProperty("cut") String cut, @JsonProperty("weight") float weight, @JsonProperty("grade") String grade) {
    this.id = id;
    this.cut = cut;
    this.weight = weight;
    this.grade = grade;
  }

  /**
   * Retrieves the id of the beef
   * @return The id of the beef
   */
  public int getId() {return id;}

  /**
   * Retrieves the name of the beef
   * @return The name of the beef "{Grade} {Cut}"
   */
  public String getName() { return String.format("%s %s", grade, cut); }

  public float getWeight() { return this.weight; }
  /**
  * {@inheritDoc}
  */
  @Override
  public String toString() {
    return String.format(STRING_FORMAT, id, cut, grade, weight);
  }
}