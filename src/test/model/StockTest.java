package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StockTest {
    private Stock stock;

    @BeforeEach
    public void runBefore() {
        stock = new Stock("GME", 100.00, 5, 6);
    }

    @Test
    public void testInitialization() {
        assertEquals(stock.getStockName(), "GME");
        assertEquals(stock.getTotalValue(), 100.00 * 6);
        assertEquals(stock.getQuantityOfStock(), 6);

    }

    @Test
    public void testStockQuanityAdjustment() {
        stock.adjustStockQuantity(5);
        assertEquals(stock.getQuantityOfStock(), 11);

        stock.adjustStockQuantity(-10);
        assertEquals(stock.getQuantityOfStock(), 1);

    }
}
