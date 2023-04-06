package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.estore.api.estoreapi.persistence.InventoryDAO;
import com.estore.api.estoreapi.products.Beef;

@Tag("Controller-Tier")
@SpringBootTest
public class InventoryControllerTests {
  private InventoryController inventoryController;
  private InventoryDAO mockInventoryDAO;
  Beef testBeef;


  /* Setup environment before each etest
   * Mock out the InventoryDAO and inject it into inventoryController
   * Create a testBeef class
   */
  @BeforeEach
  public void setupInventoryController(){
    mockInventoryDAO = mock(InventoryDAO.class);
    inventoryController = new InventoryController(mockInventoryDAO); 
    testBeef = new Beef(1, "Ribeye", 11.2, "A5", 19.99, "https://www.youtube.com/watch?v=dQw4w9WgXcQ");
    
  }

  @Test
  public void testGetBeef() throws IOException{
    when(mockInventoryDAO.getBeef(anyInt())).thenReturn(testBeef);
    
    ResponseEntity<Beef> response = inventoryController.getBeef(1);
    
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(testBeef, response.getBody());
  }

  @Test
  public void testFailedGetBeef() throws IOException{
    when(mockInventoryDAO.getBeef(anyInt())).thenReturn(null);
    ResponseEntity<Beef> response = inventoryController.getBeef(1);

    assertNull(response.getBody());
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void testErrorGetBeef() throws IOException{
    doThrow(new IOException("Failed to read from file"))
      .when(mockInventoryDAO)
      .getBeef(anyInt());
    ResponseEntity<Beef> response = inventoryController.getBeef(1);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  public void testGetAllBeef() throws IOException{
    Beef[] expectedBeef = new Beef[1];
    expectedBeef[0] = testBeef;

    when(mockInventoryDAO.getBeef()).thenReturn(expectedBeef);
    ResponseEntity<Beef[]> response = inventoryController.getBeef();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedBeef, response.getBody());
  }

  @Test
  public void testNotFoundGetAllBeef() throws IOException{
    when(mockInventoryDAO.getBeef()).thenReturn(null);
    ResponseEntity<Beef[]> response = inventoryController.getBeef();

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void testErrorGetAllBeef() throws IOException{
    doThrow(new IOException("Failed to read from file"))
      .when(mockInventoryDAO)
      .getBeef();
    ResponseEntity<Beef[]> response = inventoryController.getBeef();

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  public void testSearchBeef() throws IOException {
    Beef[] expectedBeef = new Beef[1];
    expectedBeef[0] = testBeef;

    when(mockInventoryDAO.findBeef("Ribeye")).thenReturn(expectedBeef);
    ResponseEntity<Beef[]> response = inventoryController.searchBeef("Ribeye");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedBeef, response.getBody());
  }

  @Test
  public void testNotFoundSearchBeef() throws IOException{
    when(mockInventoryDAO.findBeef(anyString())).thenReturn(new Beef[0]);
    ResponseEntity<Beef[]> response = inventoryController.searchBeef("Strip");
    
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void testErrorSearchBeef() throws IOException{
    doThrow(new IOException("Failed to read from file"))
      .when(mockInventoryDAO)
      .findBeef(anyString());
    ResponseEntity<Beef[]> response = inventoryController.searchBeef("Strip");

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  public void testCreateBeef()  throws IOException{
    when(mockInventoryDAO.createBeef(testBeef)).thenReturn(testBeef);
    ResponseEntity<Beef> response = inventoryController.createBeef(testBeef);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(testBeef, response.getBody());
  }

  @Test
  public void testConflictCreateBeef() throws IOException{
    when(mockInventoryDAO.getBeef(1)).thenReturn(testBeef);
    ResponseEntity<Beef> response =inventoryController.createBeef(testBeef);

    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
  }

  @Test
  public void testBadRequestCreateBeef() throws IOException{
    ResponseEntity<Beef> response = inventoryController.createBeef(new Beef(2, "Ribeye", 2, "A5", -4.23, "https://www.youtube.com/watch?v=dQw4w9WgXcQ"));

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void testErrorCreateBeef() throws IOException{
    doThrow(new IOException("Failed to read from file"))
      .when(mockInventoryDAO)
      .createBeef(any(Beef.class));
    ResponseEntity<Beef> response = inventoryController.createBeef(testBeef);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  public void testUpdateBeef() throws IOException{
    Beef updateBeef = new Beef(1, "Ribeye", 1, "A5", 14.99, "https://www.youtube.com/watch?v=dQw4w9WgXcQ");
    Beef expectedBeef = new Beef(1, "Ribeye", 12.2, "A5", 14.99, "https://www.youtube.com/watch?v=dQw4w9WgXcQ");
    
    when(mockInventoryDAO.updateBeef(updateBeef)).thenReturn(expectedBeef);
    when(mockInventoryDAO.getBeef(1)).thenReturn(testBeef);
    ResponseEntity<Beef> response = inventoryController.updateBeef(updateBeef);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedBeef, response.getBody());
  }

  @Test 
  public void testNotFoundUpdateBeef() throws IOException{
    Beef updateBeef = new Beef(4, "Ribeye", 1, "A5", 14.99, "https://www.youtube.com/watch?v=dQw4w9WgXcQ");
    
    when(mockInventoryDAO.updateBeef(updateBeef)).thenReturn(null);
    ResponseEntity<Beef> response = inventoryController.updateBeef(updateBeef);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test 
  void testBadRequestUpdateBeef() throws IOException{
    Beef updateBeef = new Beef(1, "Ribeye", 1, "A5", -14.99, "https://www.youtube.com/watch?v=dQw4w9WgXcQ");

    when(mockInventoryDAO.getBeef(1)).thenReturn(updateBeef);
    ResponseEntity<Beef> response = inventoryController.updateBeef(updateBeef);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void testErrorUpdateBeef() throws IOException{
    doThrow(new IOException("Failed to read from file"))
      .when(mockInventoryDAO)
      .getBeef(anyInt());
    ResponseEntity<Beef> response = inventoryController.updateBeef(testBeef);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  public void testDeleteBeef() throws IOException {
    when(mockInventoryDAO.deleteBeef(1)).thenReturn(true);
    when(mockInventoryDAO.getBeef(1)).thenReturn(testBeef);
    ResponseEntity<Beef> response = inventoryController.deleteBeef(1);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(testBeef, response.getBody());

  }

  @Test
  public void testNotFoundDeleteBeef() throws IOException{
    when(mockInventoryDAO.getBeef(anyInt())).thenReturn(null);
    ResponseEntity<Beef> response = inventoryController.deleteBeef(1);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void testErroDeleteBeef() throws IOException{
    doThrow(new IOException("Failed to read from file"))
      .when(mockInventoryDAO)
      .getBeef(anyInt());
    ResponseEntity<Beef> response = inventoryController.deleteBeef(1);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }
}
