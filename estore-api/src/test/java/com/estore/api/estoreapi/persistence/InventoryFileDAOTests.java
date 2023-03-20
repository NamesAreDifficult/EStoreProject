package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.estore.api.estoreapi.products.Beef;
import com.fasterxml.jackson.databind.ObjectMapper;

@Tag("Persistence-tier")
@SpringBootTest
public class InventoryFileDAOTests {
  InventoryFileDAO inventoryFileDAO;
  Beef[] testBeefArray;
  ObjectMapper mockObjectMapper;

  /**
   * Before each test, we will create and inject a Mock Object Mapper to
   * isolate the tests from the underlying file
   * @throws IOException
   */
  @BeforeEach
  public void setupHeroFileDAO() throws IOException {
      mockObjectMapper = mock(ObjectMapper.class);
      testBeefArray = new Beef[3];
      testBeefArray[0] = new Beef(1, "Ribeye", (float)12.66, "A5", 299.99);
      testBeefArray[1] = new Beef(2,"Strip", (float)42.2, "C4", 13.99);
      testBeefArray[2] = new Beef(3, "Flank", (float)87, "C2", 9.99);

      // When the object mapper is supposed to read from the file
      // the mock object mapper will return the hero array above
      when(mockObjectMapper
        .readValue(new File("doesnt_matter.txt"),Beef[].class))
          .thenReturn(testBeefArray);  
      inventoryFileDAO = new InventoryFileDAO("doesnt_matter.txt",mockObjectMapper);
  }

  @Test
  public void testGetAllBeef(){
    Beef[] beef = inventoryFileDAO.getBeef();

    assertEquals(3, beef.length);

    for(int i = 0; i < beef.length; i++){
      assertEquals(testBeefArray[i], beef[i]);
    }
  }

  @Test
  public void testUpdateBeef(){
    Beef expectedBeef = new Beef(1, "Ribeye", (float)13.66, "A5", 49.99);
    Beef updatedBeef = new Beef(1, "Ribeye", (float)1, "A5", 49.99);
    Beef nonExistant = new Beef(99999, "Slim Jim", (float)30.1, "Z19", 999.99);
    
    assertDoesNotThrow(() -> {
      assertEquals(expectedBeef, inventoryFileDAO.updateBeef(updatedBeef));
    });

    assertDoesNotThrow(() -> {
      assertNull(inventoryFileDAO.updateBeef(nonExistant));
    });

    assertEquals(expectedBeef, inventoryFileDAO.getBeef(1));
  } 

  @Test
  public void testFindBeef(){
    Beef[] beefArray = inventoryFileDAO.findBeef("i");

    assertEquals(2, beefArray.length);
    assertTrue(Arrays.equals(beefArray, new Beef[] {new Beef(1, "Ribeye", (float)12.66, "A5", 299.99), new Beef(2,"Strip", (float)42.2, "C4", 13.99)}));
    assertTrue(Arrays.equals(inventoryFileDAO.findBeef("Chuck"), new Beef[] {}));
  }

  @Test
  public void testGetOneBeef(){
    Beef beef = inventoryFileDAO.getBeef(1);
    assertEquals(testBeefArray[0], beef);
    Beef nullBeef = inventoryFileDAO.getBeef(12);
    assertNull(nullBeef);
  }

  @Test
  public void testCreateBeef(){
    Beef beef = new Beef(4, "Skirt", (float)4.02, "C3", 8.99);
    assertDoesNotThrow(() ->{
      inventoryFileDAO.createBeef(beef);
    });

    assertEquals(beef, inventoryFileDAO.getBeef(4));
    Beef duplicateBeef = new Beef(1, "Ribeye", (float)12.66, "A5", 299.99);

    assertDoesNotThrow(() -> {
      assertNull(inventoryFileDAO.createBeef(duplicateBeef));
    });
  }

  @Test
  public void deleteBeef(){
    assertDoesNotThrow(() -> {
      assertTrue(inventoryFileDAO.deleteBeef(1));
    });

    assertDoesNotThrow(() -> {
      assertFalse(inventoryFileDAO.deleteBeef(10));
    });

    assertNull(inventoryFileDAO.getBeef(1));
  }
}
