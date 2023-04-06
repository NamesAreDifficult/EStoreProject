package com.estore.api.estoreapi.users;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.products.CartBeef;

@Tag("Model-tier")
public class ShoppingCartTests {

  ShoppingCart testCart;
  CartBeef[] cartBeefs;
  CartBeef[] clearList;

  @BeforeEach
  public void prep() {
    cartBeefs = new CartBeef[2];
    cartBeefs[0] = new CartBeef(1, 1);
    cartBeefs[1] = new CartBeef(2, 1);

    clearList = new CartBeef[0];

    testCart = new ShoppingCart(cartBeefs);

  }

  @Test
  public void testGetContents() {

    assertArrayEquals(cartBeefs, testCart.getContents());

  }

  @Test
  public void testGetCartBeef(){
    assertEquals(cartBeefs[0], testCart.getCartBeef(cartBeefs[0].getId()));
  }

  @Test
  public void testGetCartBeefNull(){
    assertNull(testCart.getCartBeef(99));
  }
  
  @Test
  public void testAddToCart() {

    ShoppingCart newTestCart = new ShoppingCart();

    // Asserts
    assertTrue(newTestCart.addToCart(new CartBeef(1, 1)), "Add to cart should be true");
    assertTrue(newTestCart.addToCart(new CartBeef(2, 1)), "Add to cart should be true");
    assertArrayEquals(cartBeefs, testCart.getContents());

  }

  @Test
  public void testFailAddToCart() {

    // Checks that you cannot add a duplicate beef
    assertFalse(testCart.addToCart(new CartBeef(1, 2)), "Add to cart should be true");

    assertArrayEquals(cartBeefs, testCart.getContents());

  }

  @Test
  public void testClearCart() {

    testCart.clearCart();

    assertArrayEquals(clearList, testCart.getContents());

  }

  @Test
  public void testRemoveFromCart() {

    // Asserts
    assertTrue(testCart.removeFromCart(1));

    assertTrue(testCart.removeFromCart(2));

    // Tests removing a CartBeef that cannot be found
    assertFalse(testCart.removeFromCart(1));

    // Tests that the shopping cart looks as it should
    assertArrayEquals(clearList, testCart.getContents());

  }

  @Test
  public void testCheckout() {
    // Tests checkout on a normal shopping cart
    assertTrue(testCart.Checkout());
    // Validates that the length is set to zero
    assertEquals(0, testCart.getContents().length);

    // Testing on empty shopping cart
    ShoppingCart newTestCart = new ShoppingCart();
    assertFalse(newTestCart.Checkout());
  }
}
