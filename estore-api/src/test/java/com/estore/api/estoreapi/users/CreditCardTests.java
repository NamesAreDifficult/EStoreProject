package com.estore.api.estoreapi.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
public class CreditCardTests {
    
    @Test
    public void testCtor(){
        CreditCard creditCard = new CreditCard("1234567890123456", "4/20", "420");
        assertNotNull(creditCard);
    }

    @Test
    public void testGetNumber(){
        CreditCard creditCard = new CreditCard("1234567890123456", "4/20", "420");
        assertEquals("1234567890123456", creditCard.getNumber());
    }

    @Test
    public void testGetExpiration(){
        CreditCard creditCard = new CreditCard("1234567890123456", "4/20", "420");
        assertEquals("4/20", creditCard.getExpiration());
    }

    @Test
    public void testGetCVV(){
        CreditCard creditCard = new CreditCard("1234567890123456", "4/20", "420");
        assertEquals("420", creditCard.getCVV());
    }

    @Test
    public void testEqualsTrue(){
        CreditCard creditCard = new CreditCard("1234567890123456", "4/20", "420");
        CreditCard sameCard = new CreditCard("1234567890123456", "4/20", "420");
        assertTrue(creditCard.equals(sameCard));
    }

    @Test
    public void testEqualsFalse(){
        CreditCard creditCard = new CreditCard("1234567890123456", "4/20", "420");
        CreditCard difCard = new CreditCard("1234567890123457", "4/20", "420");
        assertFalse(creditCard.equals(difCard));
    }
}
