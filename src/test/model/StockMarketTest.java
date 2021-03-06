package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StockMarketTest {
    private StockMarket sm;

    @BeforeEach
    public void runBefore() {
        sm = new StockMarket();
    }

    @Test
    public void testMarketInitialization() {
        assertTrue(sm.containsStock("MMM"));
        assertTrue(sm.containsStock("AMC"));
        assertTrue(sm.containsStock("GME"));
        assertTrue(sm.containsStock("NVDA"));
        assertTrue(sm.containsStock("GOOG"));
        assertTrue(sm.containsStock("AMZN"));
        assertTrue(sm.containsStock("AAPL"));
        assertTrue(sm.containsStock("TSLA"));
        assertTrue(sm.containsStock("PHUN"));

        assertEquals(sm.getStockValue("MMM"), 175.66);
        assertEquals(sm.getStockValue("AMC"), 13.26);
        assertEquals(sm.getStockValue("GME"), 325.00);
        assertEquals(sm.getStockValue("NVDA"), 515.59);
        assertEquals(sm.getStockValue("GOOG"), 1835.74);
        assertEquals(sm.getStockValue("AMZN"), 3206.20);
        assertEquals(sm.getStockValue("AAPL"), 131.96);
        assertEquals(sm.getStockValue("TSLA"), 793.53);
        assertEquals(sm.getStockValue("PHUN"), 2.08);
    }

    @Test
    public void testStockPriceTrackerInitialization() {
        assertEquals(sm.getAllStockHistory("GME").size(), 1);
        assertEquals(sm.getAllStockHistory("GME").get(0), 325.00);
    }

    @Test
    public void testGetStockList() {
        List<String> stockList = sm.getStockList();

        assertTrue(stockList.contains("MMM"));
        assertTrue(stockList.contains("AMC"));
        assertTrue(stockList.contains("GME"));
        assertTrue(stockList.contains("NVDA"));
        assertTrue(stockList.contains("GOOG"));
        assertTrue(stockList.contains("AMZN"));
        assertTrue(stockList.contains("AAPL"));
        assertTrue(stockList.contains("TSLA"));
        assertTrue(stockList.contains("PHUN"));
    }

    @Test
    public void testUpdateStockPrice() {
        Double gmeDay0 = sm.getStockValue("GME");
        sm.updateStockPrice(1);

        Double gmeDay1 = sm.getStockValue("GME");
        sm.updateStockPrice(1);

        Double gmeDay2 = sm.getStockValue("GME");
        sm.updateStockPrice(5);

        Double gmeDay7 = sm.getStockValue("GME");

        assertNotEquals(gmeDay1, gmeDay7);
        assertNotEquals(gmeDay2, gmeDay7);

        assertEquals(sm.getDay(), 7);

        List<Double> gmeHistory = sm.getAllStockHistory("GME");

        assertEquals(gmeHistory.size(), 8);
        assertEquals(gmeHistory.get(0), gmeDay0);
        assertEquals(gmeHistory.get(1), gmeDay1);
        assertEquals(gmeHistory.get(2), gmeDay2);
        assertEquals(gmeHistory.get(7), gmeDay7);

    }
}

