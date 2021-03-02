package persistence;

import exceptions.IncompatiableStockMarketException;
import model.Investor;
import model.Portfolio;
import model.Stock;
import model.StockMarket;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {

    @Test
    public void testJsonReaderGeneralInvestor() {
        JsonReader reader = new JsonReader("./data/testWriterGeneralInvestor.json");
        try {
            // check investor loaded properly
            Investor i = reader.readInvestor();
            assertEquals(i.getInvestorName(), "Henry");
            assertEquals(i.getInvestorFunds(), 92000);

            // check portfolios loaded properly
            HashMap<String, Portfolio> investorPortfolios = i.getPortfolioMap();
            assertEquals(investorPortfolios.size(), 2);
            assertTrue(investorPortfolios.containsKey("Test1"));
            assertTrue(investorPortfolios.containsKey("Test2"));

            // confirm the specifics details of a portfolio, included stock information
            Portfolio test1 = investorPortfolios.get("Test1");
            assertEquals(test1.getPortfolioName(), "Test1");
            assertEquals(test1.getPortfolioFunds(), 1893.04);
            assertEquals(test1.getPortfolioNetWorth(), 1101.3881795664531);

            HashMap<String, Stock> test1StockMap = test1.getPortfolioMap();
            assertEquals(test1StockMap.size(), 2);
            assertTrue(test1StockMap.containsKey("AAPL"));
            assertTrue(test1StockMap.containsKey("GME"));

            Stock aapl = test1StockMap.get("AAPL");
            assertEquals(aapl.getStockName(), "AAPL");
            assertEquals(aapl.getQuantityOfStock(), 1);
            assertEquals(aapl.getTotalValue(), 131.20469039874652);

            HashMap<Integer, Double> aaplValueTracker = aapl.getValueTracker();
            assertEquals(aaplValueTracker.size(), 2);
            assertEquals(aaplValueTracker.get(0), 131.96);
            assertEquals(aaplValueTracker.get(3), 131.20469039874652);

            Stock gme = test1StockMap.get("GME");
            assertEquals(gme.getStockName(), "GME");
            assertEquals(gme.getQuantityOfStock(), 3);
            assertEquals(gme.getTotalValue(), 970.1834891677066);

            HashMap<Integer, Double> gmeValueTracker = gme.getValueTracker();
            assertEquals(gmeValueTracker.size(), 2);
            assertEquals(gmeValueTracker.get(0), 975);
            assertEquals(gmeValueTracker.get(3), 970.1834891677066);

            // Confirm specifics of the second portfolio, including stock information
            Portfolio test2 = investorPortfolios.get("Test2");
            assertEquals(test2.getPortfolioName(), "Test2");
            assertEquals(test2.getPortfolioFunds(), 4308.75);
            assertEquals(test2.getPortfolioNetWorth(), 726.5982898629086);

            HashMap<String, Stock> test2StockMap = test2.getPortfolioMap();
            assertEquals(test2StockMap.size(), 2);
            assertTrue(test2StockMap.containsKey("NVDA"));
            assertTrue(test2StockMap.containsKey("MMM"));

            Stock nvda = test2StockMap.get("NVDA");
            assertEquals(nvda.getStockName(), "NVDA");
            assertEquals(nvda.getQuantityOfStock(), 1);
            assertEquals(nvda.getTotalValue(), 535.0473522919377);

            HashMap<Integer, Double> nvdaValueTracker = nvda.getValueTracker();
            assertEquals(nvdaValueTracker.size(), 2);
            assertEquals(nvdaValueTracker.get(0), 515.59);
            assertEquals(nvdaValueTracker.get(3), 535.0473522919377);

            Stock mmm = test2StockMap.get("MMM");
            assertEquals(mmm.getStockName(), "MMM");
            assertEquals(mmm.getQuantityOfStock(), 1);
            assertEquals(mmm.getTotalValue(), 191.55093757097092);

            HashMap<Integer, Double> mmmValueTracker = mmm.getValueTracker();
            assertEquals(mmmValueTracker.size(), 2);
            assertEquals(mmmValueTracker.get(0), 175.66);
            assertEquals(mmmValueTracker.get(3), 191.55093757097092);

        } catch (IOException e) {
            fail("Something went wrong");
        }
    }

    @Test
    public void testReadStockMarketDayZero() {
        JsonReader reader = new JsonReader("./data/testWriterStockMarketDayZero.json");

        try {
            StockMarket sm = reader.readStockMarket();
            assertEquals(sm.getStockList().size(), 10);
            assertEquals(sm.getDay(), 0);
            for (String s : sm.getStockList()) {
                if (s.equals("Day")) {
                    continue;
                }
                assertEquals(sm.getAllStockHistory(s).size(), 1);
            }
        } catch (IncompatiableStockMarketException e) {
            fail("Something went wrong");
        } catch (IOException e) {
            fail("Something went wrong");
        }
    }

    @Test
    public void testReadStockMarketDayThree() {
        JsonReader reader = new JsonReader("./data/testWriterStockMarketDayThree.json");

        try {
            StockMarket sm = reader.readStockMarket();
            assertEquals(sm.getStockList().size(), 10);
            assertEquals(sm.getDay(), 3);
            for (String s : sm.getStockList()) {
                if (s.equals("Day")) {
                    continue;
                }
                assertEquals(sm.getAllStockHistory(s).size(), 4);
            }
        } catch (IncompatiableStockMarketException e) {
            fail("Something went wrong");
        } catch (IOException e) {
            fail("Something went wrong");
        }
    }

    @Test
    public void testReadStockMarketFailExpectIncompatible() {
        JsonReader reader = new JsonReader("./data/testWriterStockMarketIncompatible.json");

        try {
            reader.readStockMarket();
            fail("Something went wrong");
        } catch (IOException e) {
            fail("Something went wrong");
            e.printStackTrace();
        } catch (IncompatiableStockMarketException e) {
            // All good, test pass
        }
    }
}
