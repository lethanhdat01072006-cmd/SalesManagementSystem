package com.fpt.swt301.salesmanagementsystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class SalesServiceTest {

    private SalesService service;

    @BeforeEach
    void setUp() {
        service = new SalesService();
    }

    // ========================================================
    // 1. calculateSubtotal() (Tối thiểu 3 test)
    // Formula: Subtotal = price * quantity
    // ========================================================
    @Test
    void testCalculateSubtotal_NormalOrder() {
        // Price = 500, Quantity = 3 -> Expected = 1500
        Product p = new Product("P01", "Ao thun", 500, 3);
        assertEquals(1500.0, service.calculateSubtotal(p), 0.001);
    }

    @Test
    void testCalculateSubtotal_SingleItem() {
        // Price = 250, Quantity = 1 -> Expected = 250
        Product p = new Product("P02", "Giay", 250, 1);
        assertEquals(250.0, service.calculateSubtotal(p), 0.001);
    }

    @Test
    void testCalculateSubtotal_NullProduct_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.calculateSubtotal(null));
    }

    // ========================================================
    // 2. calculateDiscount() & Task 3 Parameterized Test (Tối thiểu 6 test biên)
    // < 1000: 0% | 1000 - < 5000: 5% | 5000 - < 10000: 10% | >= 10000: 15%
    // Boundaries: 999.99, 1000, 4999.99, 5000, 9999.99, 10000
    // ========================================================
    @ParameterizedTest
    @CsvSource({
        "999.99, 0.0",         // < 1000 -> 0%
        "1000.0, 50.0",        // 1000 - < 5000 -> 5%
        "4999.99, 249.9995",   // Biên trên của 5%
        "5000.0, 500.0",       // 5000 - < 10000 -> 10%
        "9999.99, 999.999",    // Biên trên của 10%
        "10000.0, 1500.0"      // >= 10000 -> 15%
    })
    void testCalculateDiscount_BoundaryAndTiers(double subtotal, double expected) {
        assertEquals(expected, service.calculateDiscount(subtotal), 0.001);
    }

    @Test
    void testCalculateDiscount_NegativeSubtotal_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.calculateDiscount(-100));
    }

    // ========================================================
    // 3. calculateShippingFee() (Tối thiểu 3 test)
    // < 2000 -> 50 | >= 2000 -> 0
    // ========================================================
    @Test
    void testCalculateShippingFee_Below2000() {
        assertEquals(50.0, service.calculateShippingFee(1999.99), 0.001);
    }

    @Test
    void testCalculateShippingFee_Exactly2000_Boundary() {
        // Điểm biên 2000: quy tắc >= 2000 phí là 0
        assertEquals(0.0, service.calculateShippingFee(2000.0), 0.001);
    }

    @Test
    void testCalculateShippingFee_Above2000() {
        assertEquals(0.0, service.calculateShippingFee(3000.0), 0.001);
    }

    @Test
    void testCalculateShippingFee_NegativeSubtotal_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.calculateShippingFee(-10));
    }

    // ========================================================
    // 4. calculateTotal() (Tối thiểu 2 test)
    // Total = Subtotal - Discount + Shipping
    // ========================================================
    @Test
    void testCalculateTotal_WithDiscountAndShipping() {
        // Price = 500, Quantity = 3 -> Subtotal = 1500
        // Discount = 1500 * 5% = 75
        // Shipping = 50 (vì 1500 < 2000)
        // Total = 1500 - 75 + 50 = 1475
        Product p = new Product("P03", "Balo", 500, 3);
        assertEquals(1475.0, service.calculateTotal(p), 0.001);
    }

    @Test
    void testCalculateTotal_LargeOrder_FreeShipping() {
        // Price = 1000, Quantity = 10 -> Subtotal = 10000
        // Discount = 10000 * 15% = 1500
        // Shipping = 0 (vì 10000 >= 2000)
        // Total = 10000 - 1500 + 0 = 8500
        Product p = new Product("P04", "Laptop", 1000, 10);
        assertEquals(8500.0, service.calculateTotal(p), 0.001);
    }

    // ========================================================
    // 5. classifyCustomer() (Tối thiểu 4 test)
    // < 1000: REGULAR | 1000 - < 5000: SILVER | 5000 - < 10000: GOLD | >= 10000: VIP
    // ========================================================
    @Test
    void testClassifyCustomer_Regular() {
        assertEquals("REGULAR", service.classifyCustomer(999.99));
    }

    @Test
    void testClassifyCustomer_Silver() {
        assertEquals("SILVER", service.classifyCustomer(1000.0));
        assertEquals("SILVER", service.classifyCustomer(4999.99));
    }

    @Test
    void testClassifyCustomer_Gold() {
        assertEquals("GOLD", service.classifyCustomer(5000.0));
        assertEquals("GOLD", service.classifyCustomer(9999.99));
    }

    @Test
    void testClassifyCustomer_VIP_Boundary10000() {
        // Điểm biên 10000: quy tắc >= 10000 là VIP
        assertEquals("VIP", service.classifyCustomer(10000.0));
        assertEquals("VIP", service.classifyCustomer(15000.0));
    }
}
