package com.fpt.swt301.salesmanagementsystem;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    @Test
    void testCreateValidProduct() {
        Product product = new Product("P01", "Ao khoac", 100.0, 2);
        assertEquals("P01", product.getProductId());
        assertEquals("Ao khoac", product.getProductName());
        assertEquals(100.0, product.getPrice(), 0.001);
        assertEquals(2, product.getQuantity());
    }

    @Test
    void testInvalidProductId_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Product(null, "Ao", 100, 1));
        assertThrows(IllegalArgumentException.class, () -> new Product("", "Ao", 100, 1));
    }

    @Test
    void testInvalidProductName_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Product("P01", null, 100, 1));
        assertThrows(IllegalArgumentException.class, () -> new Product("P01", "", 100, 1));
    }

    @Test
    void testInvalidPrice_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Product("P01", "Ao", 0, 1));
        assertThrows(IllegalArgumentException.class, () -> new Product("P01", "Ao", -10, 1));
    }

    @Test
    void testInvalidQuantity_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Product("P01", "Ao", 100, 0));
        assertThrows(IllegalArgumentException.class, () -> new Product("P01", "Ao", 100, -5));
    }
}
