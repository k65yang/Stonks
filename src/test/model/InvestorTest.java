package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InvestorTest {
    private Investor i;

    @BeforeEach
    public void runBefore() {
        i = new Investor("Henry", 10000);
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

        i.setActivePortfolio("Stonks");
        Portfolio p = i.getActivePortfolio();

        assertEquals(p.getPortfolioName(), "Stonks");
        assertEquals(p.getPortfolioFunds(), 10000);

        i.unsetActivePortfolio();
        assertNull(i.getActivePortfolio());

        i.setActivePortfolio("Moon");
        p = i.getActivePortfolio();

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
}
