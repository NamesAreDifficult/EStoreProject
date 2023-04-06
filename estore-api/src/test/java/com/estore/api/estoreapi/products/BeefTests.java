package com.estore.api.estoreapi.products;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Tag("Model-tier")
@SpringBootTest
class BeefTests {
  @Test
  void testCtor() {
    int expectedId = 3;
    double expectedWeight =  12.3;
    double expectedPrice = 12.99;
    String expectedCut = "Ribeye";
    String expectedGrade = "A5";
    String expectedImageUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";

    Beef testBeef = new Beef(expectedId, expectedCut, expectedWeight, expectedGrade, expectedPrice, expectedImageUrl);

    assertNotNull(testBeef, () -> "The Beef should not be null");
    assertEquals(expectedId, testBeef.getId());
    assertEquals(expectedWeight, testBeef.getWeight());
    assertEquals(expectedPrice, testBeef.getPrice());
    assertEquals(expectedCut, testBeef.getCut());
    assertEquals(expectedGrade, testBeef.getGrade());
  }

  @Test
  void testPrice() {
    double expectedPrice = 15.32;

    Beef testBeef = new Beef(1, "Strip", 42.3, "C3", 999.99, "https://www.youtube.com/watch?v=dQw4w9WgXcQ");

    testBeef.setPrice(expectedPrice);

    assertEquals(expectedPrice, testBeef.getPrice());
  }

  @Test
  void testName() {
    String cut = "Ribeye";
    String grade = "A5";
    String expectedName = String.format("%s %s", grade, cut);

    Beef testBeef = new Beef(1, cut, 5, grade, 23.44, "https://www.youtube.com/watch?v=dQw4w9WgXcQ");

    assertEquals(expectedName, testBeef.getName());
  }

  @Test
  void testWeight() {
    double startWeight = 15.00;
    double increase =  12.50;
    double decrease =  -4.30;
    double expectedWeightIncrease = startWeight + increase;
    double expectedWeightDecrease = expectedWeightIncrease + decrease;

    Beef testBeef = new Beef(1, "ribeye", startWeight, "B4", 12.50, "https://www.youtube.com/watch?v=dQw4w9WgXcQ");

    testBeef.addWeight(increase);

    assertEquals(expectedWeightIncrease, testBeef.getWeight());

    testBeef.addWeight(decrease);

    assertEquals(expectedWeightDecrease, testBeef.getWeight());

    testBeef.setWeight(startWeight);

    assertEquals(startWeight, testBeef.getWeight());
  }

  @Test
  void testToString() {
    int id = 3;
    double weight = 12.3;
    double price = 12.99;
    String cut = "Ribeye";
    String grade = "A5";
    String expectedString = String.format("Beef [id=%s cut=%s grade=%s weight=%s price=%s]", id, cut, grade, weight,
        price);

    Beef testBeef = new Beef(id, cut, weight, grade, price, "https://www.youtube.com/watch?v=dQw4w9WgXcQ");
    assertEquals(expectedString, testBeef.toString());
  }

}
