package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class PortfolioTest {
    private StockMarket sm;
    private Portfolio p;

    @BeforeEach
    public void runBefore() {
        p = new Portfolio("Stock", 10000);
        sm = new StockMarket();
    }

    @Test
    public void testPortfolioInitialization() {
        assertEquals(p.getPortfolioName(), "Stock");
        assertEquals(p.getPortfolioFunds(), 10000);
        assertTrue(p.getPortfolioMap().isEmpty());
        assertEquals(p.getPortfolioNetWorth(), 0);
    }

    @Test
    public void testBuyStock() {
        p.buyStock(sm, "GME", 3);
        assertEquals(p.getPortfolioMap().size(), 1);
        assertTrue(p.getPortfolioMap().containsKey("GME"));

        double gmeCost = sm.getStockValue("GME") * 3;
        assertEquals(p.getPortfolioFunds(), 10000-gmeCost);


        Stock gme = p.getStockInPortfolio("GME");
        assertEquals(gme.getQuantityOfStock(), 3);

    }

    @Test
    public void testContainsStock() {
        p.buyStock(sm, "GME", 3);
        assertTrue(p.isStockInPortfolio("GME"));
        assertFalse(p.isStockInPortfolio("asfd"));
    }

    @Test
    public void testBuySameStockTwice() {
        p.buyStock(sm, "GME", 3);
        assertEquals(p.getPortfolioMap().size(), 1);
        assertTrue(p.getPortfolioMap().containsKey("GME"));

        p.buyStock(sm, "GME", 1);
        assertEquals(p.getPortfolioMap().size(), 1);
        assertTrue(p.getPortfolioMap().containsKey("GME"));

        double gmeCost = sm.getStockValue("GME") * 4;
        assertEquals(p.getPortfolioFunds(), 10000-gmeCost);

        Stock gme = p.getStockInPortfolio("GME");
        assertEquals(gme.getQuantityOfStock(), 4);
    }

    @Test
    public void testBuyTwoDifferentStock() {
        p.buyStock(sm, "GME", 3);
        assertEquals(p.getPortfolioMap().size(), 1);
        assertTrue(p.getPortfolioMap().containsKey("GME"));

        double gmeCost = sm.getStockValue("GME") * 3;
        assertEquals(p.getPortfolioFunds(), 10000-gmeCost);

        p.buyStock(sm, "AAPL", 1);
        assertEquals(p.getPortfolioMap().size(), 2);
        assertTrue(p.getPortfolioMap().containsKey("AAPL"));

        double aaplCost = sm.getStockValue("AAPL");
        assertEquals(p.getPortfolioFunds(), 10000-gmeCost-aaplCost);
    }

    @Test
    public void testSellAllStock() {
        p.buyStock(sm, "GME", 3);
        p.sellStock(sm, "GME", 3);

        assertEquals(p.getPortfolioMap().size(), 0);
        assertEquals(p.getPortfolioFunds(), 10000);
    }

    @Test
    public void testSellSomeStock() {
        p.buyStock(sm, "GME", 3);
        p.sellStock(sm, "GME", 2);

        assertEquals(p.getPortfolioMap().size(), 1);
        assertTrue(p.getPortfolioMap().containsKey("GME"));

        double gmeCost = sm.getStockValue("GME") * 1;
        assertEquals(p.getPortfolioFunds(), 10000 - gmeCost);
    }

    @Test
    public void testAddNewStock() {
        p.addStock(sm, "GME", 3);

        assertEquals(p.getPortfolioMap().size(), 1);
        assertTrue(p.getPortfolioMap().containsKey("GME"));

        Stock gme = p.getStockInPortfolio("GME");
        assertEquals(gme.getQuantityOfStock(), 3);

        double gmeCost = sm.getStockValue("GME") * 3;
        assertEquals(p.getPortfolioFunds(), 10000);
        assertEquals(p.getPortfolioNetWorth(), gmeCost);
    }

    @Test
    public void testAddNewStockToExisting() {
        p.buyStock(sm, "GME", 3);

        assertEquals(p.getPortfolioMap().size(), 1);
        assertTrue(p.getPortfolioMap().containsKey("GME"));

        Stock gme = p.getStockInPortfolio("GME");
        assertEquals(gme.getQuantityOfStock(), 3);

        p.addStock(sm, "GME", 3);
        assertEquals(gme.getQuantityOfStock(), 6);

        double gmeCost = sm.getStockValue("GME");
        assertEquals(p.getPortfolioFunds(), 10000 - gmeCost * 3);
        assertEquals(p.getPortfolioNetWorth(), gmeCost * 6);
    }

    @Test
    public void testRemoveAllStock() {
        p.buyStock(sm, "GME", 3);

        Stock gme = p.getStockInPortfolio("GME");
        assertEquals(gme.getQuantityOfStock(), 3);

        p.removeStock(sm, "GME", 3);

        assertEquals(p.getPortfolioMap().size(), 0);

        double gmeCost = sm.getStockValue("GME") * 3;
        assertEquals(p.getPortfolioFunds(), 10000 - gmeCost);
        assertEquals(p.getPortfolioNetWorth(), 0);
    }

    @Test
    public void testRemoveSomeStock() {
        p.buyStock(sm, "GME", 3);

        Stock gme = p.getStockInPortfolio("GME");
        assertEquals(gme.getQuantityOfStock(), 3);

        p.removeStock(sm, "GME", 2);

        assertEquals(p.getPortfolioMap().size(), 1);
        assertTrue(p.getPortfolioMap().containsKey("GME"));
        assertEquals(gme.getQuantityOfStock(), 1);

        double gmeCost = sm.getStockValue("GME");
        assertEquals(p.getPortfolioFunds(), 10000 - gmeCost * 3);
        assertEquals(p.getPortfolioNetWorth(), gmeCost);
    }

    @Test
    public void testTransferAllStockToAnother() {
        Portfolio x = new Portfolio("other", 10000);
        x.buyStock(sm, "NVDA", 4);

        p.transferStock(sm, x, "NVDA", 4);

        assertEquals(x.getPortfolioMap().size(), 0);
        assertEquals(p.getPortfolioMap().size(), 1);
        assertTrue(p.getPortfolioMap().containsKey("NVDA"));
    }
}
