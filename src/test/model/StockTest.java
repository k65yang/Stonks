package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StockTest {
    private Stock stock;
    private StockMarket sm;

    @BeforeEach
    public void runBefore() {
        stock = new Stock("TEST", 100.00, 5, 6);
        sm = new StockMarket();
    }

    @Test
    public void testInitialization() {
        assertEquals(stock.getStockName(), "TEST");
        assertEquals(stock.getTotalValue(), 100.00 * 6);
        assertEquals(stock.getQuantityOfStock(), 6);

    }

    @Test
    public void testStockQuanityAdjustment() {
        stock.adjustStockQuantity(5, sm);
        assertEquals(stock.getQuantityOfStock(), 11);
        assertEquals(stock.getTotalValue(), 1100);

        stock.adjustStockQuantity(-10, sm);
        assertEquals(stock.getQuantityOfStock(), 1);
        assertEquals(stock.getTotalValue(), 100);

    }
}
