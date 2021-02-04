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
