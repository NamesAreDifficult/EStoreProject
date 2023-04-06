package com.estore.api.estoreapi.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
class CreditCardTests {
    
    @Test
    void testCtor(){
        CreditCard creditCard = new CreditCard("1234567890123456", "4/20", "420");
        assertNotNull(creditCard);
    }

    @Test
    void testGetNumber(){
        CreditCard creditCard = new CreditCard("1234567890123456", "4/20", "420");
        assertEquals("1234567890123456", creditCard.getNumber());
    }

    @Test
    void testGetExpiration(){
        CreditCard creditCard = new CreditCard("1234567890123456", "4/20", "420");
        assertEquals("4/20", creditCard.getExpiration());
    }

    @Test
    void testGetCVV(){
        CreditCard creditCard = new CreditCard("1234567890123456", "4/20", "420");
        assertEquals("420", creditCard.getCVV());
    }

    @Test
    void testEqualsTrue(){
        CreditCard creditCard = new CreditCard("1234567890123456", "4/20", "420");
        CreditCard sameCard = new CreditCard("1234567890123456", "4/20", "420");
        assertTrue(creditCard.equals(sameCard));
    }

    @Test
    void testEqualsFalse(){
        CreditCard creditCard = new CreditCard("1234567890123456", "4/20", "420");
        CreditCard difCard = new CreditCard("1234567890123457", "4/20", "420");
        assertFalse(creditCard.equals(difCard));
    }

    @Test
    void testHashCode(){
      CreditCard testCard = new CreditCard("1234567890123456", "4/20", "420"); 
      assertEquals(965964379, testCard.hashCode());
    }
}
