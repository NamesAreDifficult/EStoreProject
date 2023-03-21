package com.estore.api.estoreapi.products;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Tag("Model-tier")
@SpringBootTest
public class BeefTests {
  @Test
  public void testCtor() {
    int expectedId = 3;
    float expectedWeight = (float) 12.3;
    double expectedPrice = 12.99;
    String expectedCut = "Ribeye";
    String expectedGrade = "A5";

    Beef testBeef = new Beef(expectedId, expectedCut, expectedWeight, expectedGrade, expectedPrice);

    assertNotNull(testBeef, () -> "The Beef should not be null");
    assertEquals(expectedId, testBeef.getId());
    assertEquals(expectedWeight, testBeef.getWeight());
    assertEquals(expectedPrice, testBeef.getPrice());
    assertEquals(expectedCut, testBeef.getCut());
    assertEquals(expectedGrade, testBeef.getGrade());
  }

  @Test
  public void testPrice() {
    double expectedPrice = 15.32;

    Beef testBeef = new Beef(1, "Strip", (float) 42.3, "C3", 999.99);

    testBeef.setPrice(expectedPrice);

    assertEquals(expectedPrice, testBeef.getPrice());
  }

  @Test
  public void testName() {
    String cut = "Ribeye";
    String grade = "A5";
    String expectedName = String.format("%s %s", grade, cut);

    Beef testBeef = new Beef(1, cut, (float) 5, grade, 23.44);

    assertEquals(expectedName, testBeef.getName());
  }

  @Test
  public void testWeight() {
    float startWeight = (float) 15.00;
    float increase = (float) 12.50;
    float decrease = (float) -4.30;
    float expectedWeightIncrease = startWeight + increase;
    float expectedWeightDecrease = expectedWeightIncrease + decrease;

    Beef testBeef = new Beef(1, "ribeye", startWeight, "B4", 12.50);

    testBeef.addWeight(increase);

    assertEquals(expectedWeightIncrease, testBeef.getWeight());

    testBeef.addWeight(decrease);

    assertEquals(expectedWeightDecrease, testBeef.getWeight());

    testBeef.setWeight(startWeight);

    assertEquals(startWeight, testBeef.getWeight());
  }

  @Test
  public void testToString() {
    int id = 3;
    float weight = (float) 12.3;
    double price = 12.99;
    String cut = "Ribeye";
    String grade = "A5";
    String expectedString = String.format("Beef [id=%s cut=%s grade=%s weight=%s price=%s]", id, cut, grade, weight,
        price);

    Beef testBeef = new Beef(id, cut, weight, grade, price);
    assertEquals(expectedString, testBeef.toString());
  }

}
