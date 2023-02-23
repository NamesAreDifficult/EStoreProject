package com.estore.api.estoreapi.products;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Class for representing beef in a customer's shopping cart
 */
public class CartBeef {
    @JsonProperty("id")
    private final int id;
    @JsonProperty("weight")
    private float weight;

    /*
     * Constructor using JSON properties
     */
    public CartBeef(@JsonProperty("id") int id, @JsonProperty("weight") float weight) {
        this.id = id;
        this.weight = weight;
    }

    /*
     * Constsructor that uses a beef object and the weight the user wants
     */
    public CartBeef(Beef beef, float weight) {
        this.id = beef.getId();
        this.weight = weight;
    }

    public int getId() {
        return this.id;
    }

    public float getWeight() {
        return this.weight;
    }

}