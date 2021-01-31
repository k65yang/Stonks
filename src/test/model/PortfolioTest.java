package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PortfolioTest {
    private StockMarket sm;
    private Portfolio p;

    @BeforeEach
    public void runBefore() {
        p = new Portfolio("Henry", 10000);
        sm = new StockMarket();
    }

    @Test
    public void testAddStock() {
        p.addStock(sm, "GME", 3);
        assertEquals(p.getPortfolioMap().size(), 1);
        assertTrue(p.getPortfolioMap().containsKey("GME"));

        Stock gme = p.getStockInPortfolio("GME");
        assertEquals(gme.getQuantityOfStock(), 3);

    }

    @Test
    public void testAddSameStockTwice() {
        p.addStock(sm, "GME", 3);
        assertEquals(p.getPortfolioMap().size(), 1);
        assertTrue(p.getPortfolioMap().containsKey("GME"));

        p.addStock(sm, "GME", 1);
        assertEquals(p.getPortfolioMap().size(), 1);
        assertTrue(p.getPortfolioMap().containsKey("GME"));

        Stock gme = p.getStockInPortfolio("GME");
        assertEquals(gme.getQuantityOfStock(), 4);
    }
}
