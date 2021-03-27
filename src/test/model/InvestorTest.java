package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InvestorTest {
    private Investor i;
    private StockMarket sm;

    @BeforeEach
    public void runBefore() {
        i = new Investor("Henry", 10000);
        sm = new StockMarket();
    }

    @Test
    public void testInvestorInitialization() {
        assertEquals(i.getInvestorName(), "Henry");
        assertEquals(i.getInvestorFunds(), 10000);
        assertTrue(i.getPortfolioMap().isEmpty());
    }

    @Test
    public void testSetActivePortfolios() {
        i.addPortfolio("Stonks", 10000);
        i.addPortfolio("Moon", 1);

        Portfolio p = i.setActivePortfolio("Stonks");

        assertEquals(p.getPortfolioName(), "Stonks");
        assertEquals(p.getPortfolioFunds(), 10000);

        i.unsetActivePortfolio();

        p = i.setActivePortfolio("Moon");

        assertEquals(p.getPortfolioName(), "Moon");
        assertEquals(p.getPortfolioFunds(), 1);
    }

    @Test
    public void testAddAndRemovePortfolio() {
        i.addPortfolio("Long-term",5000);

        assertEquals(i.getInvestorFunds(), 5000);
        assertTrue(i.getPortfolioMap().containsKey("Long-term"));
        assertEquals(i.getPortfolioMap().size(), 1);

        i.removePortfolio("Long-term");

        assertEquals(i.getInvestorFunds(), 10000);
        assertTrue(i.getPortfolioMap().isEmpty());
    }

    @Test
    public void testAddFundsToExistingPorfolio() {
        i.addPortfolio("Long-term",5000);

        assertEquals(i.getInvestorFunds(), 5000);
        assertTrue(i.getPortfolioMap().containsKey("Long-term"));
        assertEquals(i.getPortfolioMap().size(), 1);

        i.addFundsToPortfolio("Long-term", 5);

        assertEquals(i.getPortfolioMap().get("Long-term").getPortfolioFunds(), 5005);
    }

    @Test
    public void testMergePortfolio() {
        i.addPortfolio("test1", 5000);
        i.addPortfolio("test2", 5000);

        assertEquals(i.getInvestorFunds(), 0);
        assertTrue(i.getPortfolioMap().containsKey("test1"));
        assertTrue(i.getPortfolioMap().containsKey("test2"));
        assertEquals(i.getPortfolioMap().size(), 2);

        Portfolio test1 = i.setActivePortfolio("test1");
        Portfolio test2 = i.setActivePortfolio("test2");

        // buyStock method throughly tested in PortfolioTest class
        test1.buyStock(sm, "GME", 5);
        test2.buyStock(sm, "AAPL", 4);
        double funds1 = test1.getPortfolioFunds();
        double funds2 = test2.getPortfolioFunds();

        i.mergeIntoOnePortfolio("test1", sm);
        assertEquals(i.getInvestorFunds(), 0);
        assertTrue(i.getPortfolioMap().containsKey("test1"));
        assertEquals(i.getPortfolioMap().size(), 1);

        assertEquals(test1.getPortfolioMap().size(), 2);
        assertTrue(test1.getPortfolioMap().containsKey("GME"));
        assertTrue(test1.getPortfolioMap().containsKey("AAPL"));
        assertEquals(test1.getPortfolioMap().get("GME").getQuantityOfStock(), 5);
        assertEquals(test1.getPortfolioMap().get("AAPL").getQuantityOfStock(), 4);
        assertEquals(test1.getPortfolioFunds(), funds1 + funds2);
        
    }
}
