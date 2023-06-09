package com.estore.api.estoreapi.products;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Class for representing beef in a customer's shopping cart
 * 
 * @author Brendan Battisti
 */
public class CartBeef {

  // JSON properties
  @JsonProperty("id")
  private final int id;
  @JsonProperty("weight")
  private BigDecimal weight;

  /**
  * Creates a represntation of beef to add to a shopping cart
  * 
  * @param id     The id of the cut
  * @param weight the weight of the cut
  * 
  *               {@literal @}JsonProperty is used in serialization and
  *               deserialization
  *               of the JSON object to the Java object in mapping the fields. If
  *               a field
  *               is not provided in the JSON object, the Java field gets the
  *               default Java
  *               value, i.e. 0 for int
  */
  public CartBeef(@JsonProperty("id") int id, @JsonProperty("weight") BigDecimal weight) {
    this.id = id;
    this.weight = weight;
  }

  /**
  * Constsructor that uses a {@linkplain Beef beef} object and the weight the
  * user wants
  * 
  * @param beef - {@linkplain Beef beef} to represent in the shopping cart
  * 
  * @param weight - weight of the {@linkplain Beef beef} to be bought
  * 
  */
  public CartBeef(Beef beef, BigDecimal weight) {
    this.id = beef.getId();
    this.weight = weight;
  }

  /**
  * Gets the id of the {@linkplain CartBeef cartBeef}
  * 
  * @return - id of the {@linkplain CartBeef cartBeef}
  */
  public int getId() {
    return this.id;
  }

  /**
  * Gets the weight of the {@linkplain CartBeef cartBeef}
  * 
  * @return - weight of the {@linkplain CartBeef cartBeef}
  */
  public BigDecimal getWeight() {
    return this.weight;
  }

  /**
  * Adds new weight to an existing CartBeef object
  * 
  * @param newWeight: Weight to be added to the CartBeef object
  */
  public void addWeight(BigDecimal newWeight){
    this.weight = this.weight.add(newWeight);
  }

  /**
  * Checks if another object is an equal{@linkplain CartBeef cartBeef}
  * 
  * @return - if the other object is an equal {@linkplain CartBeef cartBeef}
  */
  @Override
  public boolean equals(Object other) {
    if (other instanceof CartBeef) {
      CartBeef otherCartBeef = (CartBeef) other;
      if (otherCartBeef.getId() == this.getId()){
        return true;
      }
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode(){
    return String.format("%d%f", this.getId(), this.getWeight()).hashCode();
  }
}