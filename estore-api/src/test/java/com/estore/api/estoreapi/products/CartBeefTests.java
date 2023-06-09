package com.estore.api.estoreapi.products;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;

@Tag("Model-tier")
//@SpringBootTest
class CartBeefTests {
    
  @Test
  void testCtor(){
    //setup
    int expectedId = 2;
    BigDecimal expectedWeight = BigDecimal.valueOf(10.1);

    CartBeef testCartBeef = new CartBeef(expectedId, expectedWeight);

    //tests
    assertNotNull(testCartBeef, () -> "The cart should not be empty");
    assertEquals(expectedId, testCartBeef.getId());
    assertEquals(expectedWeight, testCartBeef.getWeight());
  }

  @Test
  void testGetID(){
    int expectedId = 2;
    BigDecimal expectedWeight = BigDecimal.valueOf(10.1);

    CartBeef testCartBeef = new CartBeef(expectedId, expectedWeight);
    assertEquals(2, testCartBeef.getId());
  }

  @Test 
  void testOverloadCtor() {
    //setup
    int expectedId = 2;
    BigDecimal expectedWeight = BigDecimal.valueOf(10.1);
    Beef expectedBeef = new Beef(2, "Ribeye", BigDecimal.valueOf(12.2), "A5", 13.00, "https://www.youtube.com/watch?v=dQw4w9WgXcQ");

    CartBeef testCartBeef = new CartBeef(expectedBeef, expectedWeight);

    //tests
    assertNotNull(testCartBeef, () -> "The cart should not be empty");
    assertEquals(expectedId, testCartBeef.getId());
    assertEquals(expectedWeight, testCartBeef.getWeight());
  }

    

  @Test
  void testEqual(){
     //Setup
    CartBeef testCartBeef1 = new CartBeef( 2, BigDecimal.valueOf(10.1));
    CartBeef testCartBeef2 = new CartBeef(2, BigDecimal.valueOf(12.1));

    //testing to see if equals correctly returns true
    boolean equal = testCartBeef1.equals(testCartBeef2);
    assertTrue(equal);

    CartBeef testCartBeef3 = new CartBeef(1, BigDecimal.valueOf(10.1));
        
    //testing to see if equals correctly returns false
    equal= testCartBeef1.equals(testCartBeef3);
    assertFalse(equal);
  }
}
