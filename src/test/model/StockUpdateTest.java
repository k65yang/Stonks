package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class StockUpdateTest {
    private StockMarket sm;
    private Investor investor;

    @BeforeEach
    public void runBefore() {
        sm = new StockMarket();
        investor = new Investor("Henry", 100000000);

        investor.addPortfolio("Stonk", 100000);
        investor.setActivePortfolio("Stonk");
        Portfolio stonk = investor.getActivePortfolio();
        stonk.buyStock(sm, "PHUN", 1);
        stonk.buyStock(sm, "GOOG", 1);

        assertEquals(stonk.getPortfolioMap().size(), 2);
        assertTrue(stonk.isStockInPortfolio("PHUN"));
        assertTrue(stonk.isStockInPortfolio("GOOG"));

        investor.unsetActivePortfolio();

        investor.addPortfolio("Tank", 500000);
        investor.setActivePortfolio("Tank");
        Portfolio tank = investor.getActivePortfolio();
        tank.buyStock(sm, "GME", 1);

        assertEquals(tank.getPortfolioMap().size(), 1);
        assertTrue(tank.isStockInPortfolio("GME"));
    }

    @Test
    public void testUpdateOneDay() {
        sm.updateStockPrice(1);
        investor.updateAllPortfolios(sm);

        investor.setActivePortfolio("Stonk");
        Portfolio stonk = investor.getActivePortfolio();
        investor.unsetActivePortfolio();

        Stock stonkPHUN = stonk.getStockInPortfolio("PHUN");
        Stock stonkGOOG = stonk.getStockInPortfolio("GOOG");

        assertEquals(stonkPHUN.getTotalValue(), sm.getStockValue("PHUN"));
        assertEquals(stonkGOOG.getTotalValue(), sm.getStockValue("GOOG"));

        investor.setActivePortfolio("Tank");
        Portfolio tank = investor.getActivePortfolio();
        investor.unsetActivePortfolio();

        Stock tankGME = tank.getStockInPortfolio("GME");
        HashMap<Integer, Double> gmeValueTracker = tankGME.getValueTracker();

        assertEquals(tankGME.getTotalValue(), sm.getStockValue("GME"));

        assertEquals(gmeValueTracker.size(), 2);
        assertEquals(gmeValueTracker.get(sm.getDay()), sm.getStockValue("GME"));
    }

    @Test
    public void testUpdateMultipleTimes() {
        sm.updateStockPrice(1);
        investor.updateAllPortfolios(sm);

        investor.setActivePortfolio("Tank");
        Portfolio tank = investor.getActivePortfolio();
        investor.unsetActivePortfolio();

        Stock tankGME = tank.getStockInPortfolio("GME");
        HashMap<Integer, Double> gmeValueTracker = tankGME.getValueTracker();

        assertEquals(tankGME.getTotalValue(), sm.getStockValue("GME"));
        assertEquals(gmeValueTracker.size(), 2);
        assertEquals(gmeValueTracker.get(sm.getDay()), sm.getStockValue("GME"));

        tank.buyStock(sm, "NVDA", 1);
        Stock tankNVDA = tank.getStockInPortfolio("NVDA");
        HashMap<Integer, Double> nvdaValueTracker = tankNVDA.getValueTracker();

        assertEquals(tankNVDA.getTotalValue(), sm.getStockValue("NVDA"));
        assertEquals(nvdaValueTracker.size(), 1);
        assertEquals(nvdaValueTracker.get(sm.getDay()), sm.getStockValue("NVDA"));

        sm.updateStockPrice(5);
        investor.updateAllPortfolios(sm);

        assertEquals(tankGME.getTotalValue(), sm.getStockValue("GME"));
        assertEquals(gmeValueTracker.size(), 3);
        assertEquals(gmeValueTracker.get(sm.getDay()), sm.getStockValue("GME"));

        assertEquals(tankNVDA.getTotalValue(), sm.getStockValue("NVDA"));
        assertEquals(nvdaValueTracker.size(), 2);
        assertEquals(nvdaValueTracker.get(sm.getDay()), sm.getStockValue("NVDA"));
    }
}
