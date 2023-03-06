package com.estore.api.estoreapi.products;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Tag("Model-tier")
@SpringBootTest
public class CartBeefTests {
    
    @Test
    public void testCtor(){
        int expectedId = 2;
        float expectedWeight = (float)10.1;

        CartBeef testCartBeef = new CartBeef(expectedId, expectedWeight);

        assertEquals(expectedId, testCartBeef.getId());
        assertEquals(expectedWeight, testCartBeef.getWeight());
    }


}
