package com.estore.api.estoreapi.products;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

import org.apache.el.lang.ELArithmetic.BigDecimalDelegate;
/**
 * Represents a Beef entity
 * 
 * @author NamesAreDifficult
 */
public class Beef {
  // Map Json properties to class internal attributes
  @JsonProperty("id")
  private final int id;
  @JsonProperty("cut")
  private final String cut;
  @JsonProperty("grade")
  private final String grade;
  @JsonProperty("weight")
  private BigDecimal weight;
  @JsonProperty("price")
  private double price;
  @JsonProperty("imageUrl")
  private String imageUrl;

  // Format strings
  static final String NAME_FORMAT = "%s %s";
  static final String STRING_FORMAT = "Beef [id=%s cut=%s grade=%s weight=%s price=%s]";

  /**
   * Create a cut of Beef with the supplied paramaters
   * 
   * @param id     The id of the cut
   * @param cut    The type of cut
   * @param weight the weight of the cut
   * @param grade  the grade of beef as represented on the japanese beef grading
   *               scale
   * @param price  the price per lb of beef
   * 
   *               {@literal @}JsonProperty is used in serialization and
   *               deserialization
   *               of the JSON object to the Java object in mapping the fields. If
   *               a field
   *               is not provided in the JSON object, the Java field gets the
   *               default Java
   *               value, i.e. 0 for int
   */
  public Beef(@JsonProperty("id") int id,
      @JsonProperty("cut") String cut,
      @JsonProperty("weight") BigDecimal weight,
      @JsonProperty("grade") String grade,
      @JsonProperty("price") double price,
      @JsonProperty("imageUrl") String imageUrl) {
    this.id = id;
    this.cut = cut;
    this.weight = weight;
    this.grade = grade;
    this.price = Math.round(price * 100.0) / 100.0;
    this.imageUrl = imageUrl;
  }

  /**
   * Retrieves the id of the beef
   * 
   * @return The id of the beef
   */
  public int getId() {
    return id;
  }

  /**
   *  Retrieves the image url
   * 
   * @return the imageUrl for the beef object
   */
  public String getImageUrl(){
    return this.imageUrl;
  }

  /**
   * Sets the image url
   * 
   * @param url the String to set the URL to
   */
  public void setImageUrl(String newUrl){
    this.imageUrl = newUrl;
  }
  /**
   * Retrieves the name and grade of the beef
   * 
   * @return The name and grade of the beef "{Grade} {Cut}"
   */
  public String getName() {
    return String.format(NAME_FORMAT, grade, cut);
  }

  /**
   * Retrieves the weight of the piece of beef
   * 
   * @return double representing the weight in pounds of the beef
   */
  public BigDecimal getWeight() {
    return this.weight;
  }

  /**
   * Sets the weight of the piece of beef
   */
  public void setWeight(BigDecimal newWeight) {
    this.weight = newWeight;
  }

  /**
   * Adds the provided amount of weight to the current stored weight
   * 
   * @param extraWeight amount of weight to add to the object
   * 
   * @return the new total weight of the object
   */
  public BigDecimal addWeight(BigDecimal extraWeight) {
    this.weight = this.weight.add(extraWeight);
    return this.weight;
  }

  /**
   * Retrieves the grade of the piece of beef
   * 
   * @return 2 character string representing grade of beef
   */
  public String getGrade() {
    return this.grade;
  }

  /**
   * Retrieves the cut of the piece of beef
   * 
   * @return String containing the name of the cut of beef
   */
  public String getCut() {
    return this.cut;
  }

  /**
   * Retrieves the price of the beef
   * 
   * @return The price of the beef
   */
  public double getPrice() {
    return this.price;
  }

  /**
   * Changes the price of the beef
   */
  public void setPrice(double newPrice) {
    this.price = newPrice;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return String.format(STRING_FORMAT, id, cut, grade, weight, price);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object object) {
    if (object instanceof Beef) {
      Beef beef = (Beef) object;
      return this.getId() == beef.getId() && this.getCut().equals(beef.getCut())
          && this.getGrade().equals(beef.getGrade()) && this.getWeight().compareTo(beef.getWeight()) == 0
          && this.getPrice() == beef.getPrice();
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  public int hashCode(){
    return this.toString().hashCode();
  }
}