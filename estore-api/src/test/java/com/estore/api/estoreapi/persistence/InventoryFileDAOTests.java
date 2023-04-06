package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.estore.api.estoreapi.products.Beef;
import com.fasterxml.jackson.databind.ObjectMapper;

@Tag("Persistence-tier")
@SpringBootTest
class InventoryFileDAOTests {
  InventoryFileDAO inventoryFileDAO;
  Beef[] testBeefArray;
  ObjectMapper mockObjectMapper;

  /**
   * Before each test, we will create and inject a Mock Object Mapper to
   * isolate the tests from the underlying file
   * @throws IOException
   */
  @BeforeEach
  void setupInventoryFileDAO() throws IOException {
      mockObjectMapper = mock(ObjectMapper.class);
      testBeefArray = new Beef[3];
      testBeefArray[0] = new Beef(1, "Ribeye", (float)12.66, "A5", 299.99, "https://www.youtube.com/watch?v=dQw4w9WgXcQ");
      testBeefArray[1] = new Beef(2,"Strip", (float)42.2, "C4", 13.99, "https://www.youtube.com/watch?v=dQw4w9WgXcQ");
      testBeefArray[2] = new Beef(3, "Flank", (float)87, "C2", 9.99, "https://www.youtube.com/watch?v=dQw4w9WgXcQ");

      // When the object mapper is supposed to read from the file
      // the mock object mapper will return the beef array above
      when(mockObjectMapper
        .readValue(new File("doesnt_matter.txt"),Beef[].class))
          .thenReturn(testBeefArray);  
      inventoryFileDAO = new InventoryFileDAO("doesnt_matter.txt",mockObjectMapper);
  }

  @AfterEach
  void cleanupInventoryFileDAO() {
    File file = new File("doesnt_matter.txt");
    if (file.exists()){
      file.delete();
    }
  }

  @Test
  void testUpdateBeef() throws IOException{
    Beef updatedBeef = new Beef(1, "Ribeye", (float)1.34, "A5", 49.99, "https://www.youtube.com/watch?v=dQw4w9WgXcQ");
    Beef expectedBeef = new Beef(1, "Ribeye", 14, "A5", 49.99, "https://www.youtube.com/watch?v=dQw4w9WgXcQ");

    assertDoesNotThrow(() -> {
      assertEquals(expectedBeef, inventoryFileDAO.updateBeef(updatedBeef));
    });

    doThrow(new IOException("Failed to write"))
      .when(mockObjectMapper)
      .writeValue(any(File.class), any(Beef[].class));

    assertThrows(IOException.class, 
      () -> inventoryFileDAO.updateBeef(updatedBeef),
      "IOException ont thrown");
  }

  @Test
  void testGetAllBeef(){
    Beef[] beef = inventoryFileDAO.getBeef();

    assertEquals(3, beef.length);
    assertTrue(Arrays.equals(testBeefArray, beef));
  }

  @Test
  void testFindBeef(){
    Beef[] beefArray = inventoryFileDAO.findBeef("i");

    assertEquals(2, beefArray.length);
    assertTrue(Arrays.equals(beefArray, new Beef[] {new Beef(1, "Ribeye", (float)12.66, "A5", 299.99, "https://www.youtube.com/watch?v=dQw4w9WgXcQ"), new Beef(2,"Strip", (float)42.2, "C4", 13.99, "https://www.youtube.com/watch?v=dQw4w9WgXcQ")}));
    assertTrue(Arrays.equals(inventoryFileDAO.findBeef("Chuck"), new Beef[] {}));
  }

  @Test
  void testGetOneBeef(){
    Beef beef = inventoryFileDAO.getBeef(1);
    assertEquals(testBeefArray[0], beef);
    Beef nullBeef = inventoryFileDAO.getBeef(12);
    assertNull(nullBeef);
  }

  @Test
  void testCreateBeef(){
    Beef beef = new Beef(4, "Skirt", (float)4.02, "C3", 8.99, "https://www.youtube.com/watch?v=dQw4w9WgXcQ");
    assertDoesNotThrow(() -> {
      inventoryFileDAO.createBeef(beef);
    });

    assertEquals(beef, inventoryFileDAO.getBeef(4));
    Beef duplicateBeef = new Beef(1, "Ribeye", (float)12.66, "A5", 299.99, "https://www.youtube.com/watch?v=dQw4w9WgXcQ");

    assertDoesNotThrow(() -> {
      assertNull(inventoryFileDAO.createBeef(duplicateBeef));
    });
  }

  @Test
  void deleteBeef(){
    assertDoesNotThrow(() -> {
      assertTrue(inventoryFileDAO.deleteBeef(1));
    });

    assertDoesNotThrow(()->{
      assertFalse(inventoryFileDAO.deleteBeef(10));
    });

    assertNull(inventoryFileDAO.getBeef(1));
  }
}
