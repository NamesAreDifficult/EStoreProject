package com.estore.api.estoreapi.users;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Represents a Credit Card
 *  
 * @author John West
 */

public class CreditCard {

  @JsonProperty("number")
  private String number;

  @JsonProperty("expiration")
  private String expiration;

  @JsonProperty("cvv")
  private String cvv;

  /*
   * Constructs a credit card using the card number, expiration date, and cvv
   */
  public CreditCard(@JsonProperty("number") String number,
                    @JsonProperty("expiration") String expiration,
                    @JsonProperty("cvv") String cvv){
  
    this.number = number;
    this.expiration = expiration;
    this.cvv = cvv;
  }

  /*
   * Retrieves the card number of a given credit card
   * 
   * @return Card number, as a string
   *
   */
  public String getNumber(){
    return this.number;
  }

  /*
   * Retrieves the expiration date of a given credit card
   * 
   * @return Expiration date, as a string
   *
   */
  public String getExpiration(){
    return this.expiration;
  }

  /*
   * Retrieves the CVV of a given credit card
   * 
   * @return CVV, as a string
   *
   */
  public String getCVV(){
    return this.cvv;
  }

  /*
   * Checks if another object is an equal{@linkplain CreditCard creditCard}
   * 
   * @return - if the other object is an equal {@linkplain CreditCard creditCard}
   */
  @Override
  public boolean equals(Object other) {
    if (other instanceof CreditCard) {
      CreditCard otherCard = (CreditCard) other;

      if (otherCard.getNumber().equals(this.getNumber())){
        return true;
      }
    }
    return false;
  }

  @Override
  public int hashCode(){
    return String.format("%s%s%s",this.getNumber(), this.getExpiration(), this.getCVV()).hashCode();
  }
}
