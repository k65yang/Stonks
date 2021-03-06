package persistence;

import exceptions.InvalidClassTypeException;
import exceptions.IncompatibleStockMarketException;
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
    public void testJsonReaderNonExistentFile() {
        JsonReader reader = new JsonReader("investor","./data/terEmtor.json");
        try {
            reader.read();
            fail("Something went wrong");
        } catch (IncompatibleStockMarketException e) {
            fail("Something went wrong");
        } catch (InvalidClassTypeException e) {
            fail("Something went wrong");
        } catch (IOException e) {
            // All good, passed test
        }
    }

    @Test
    public void testJsonReaderInvalidClassType() {
        JsonReader reader = new JsonReader("null","./data/testWriterEmptyInvestor.json");
        try {
            reader.read();
            fail("Something went wrong");
        } catch (IncompatibleStockMarketException e) {
            fail("Something went wrong");
        } catch (InvalidClassTypeException e) {
            // All good, passed test
        } catch (IOException e) {
            fail("Something went wrong");
        }
    }


    @Test
    public void testJsonReaderEmptyInvestor() {
        JsonReader reader = new JsonReader("investor","./data/testWriterEmptyInvestor.json");
        try {
            Investor i = (Investor) reader.read();
            assertEquals(i.getInvestorName(), "Empty");
            assertEquals(i.getInvestorFunds(), 10000);
        } catch (IncompatibleStockMarketException e) {
            fail("Something went wrong");
        } catch (InvalidClassTypeException e) {
            fail("Something went wrong");
        } catch (IOException e) {
            fail("Something went wrong");
        }
    }

    @Test
    public void testJsonReaderGeneralInvestor() {
        JsonReader reader = new JsonReader("investor","./data/testWriterGeneralInvestorFINAL.json");
        try {
            // check investor loaded properly
            Investor i = (Investor) reader.read();
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
            assertEquals(test1.getPortfolioNetWorth(), 1116.0431775124243);

            HashMap<String, Stock> test1StockMap = test1.getPortfolioMap();
            assertEquals(test1StockMap.size(), 2);
            assertTrue(test1StockMap.containsKey("AAPL"));
            assertTrue(test1StockMap.containsKey("GME"));

            Stock aapl = test1StockMap.get("AAPL");
            assertEquals(aapl.getStockName(), "AAPL");
            assertEquals(aapl.getQuantityOfStock(), 1);
            assertEquals(aapl.getTotalValue(), 155.6238899206936);

            HashMap<Integer, Double> aaplValueTracker = aapl.getValueTracker();
            assertEquals(aaplValueTracker.size(), 2);
            assertEquals(aaplValueTracker.get(0), 131.96);
            assertEquals(aaplValueTracker.get(3), 155.6238899206936);

            Stock gme = test1StockMap.get("GME");
            assertEquals(gme.getStockName(), "GME");
            assertEquals(gme.getQuantityOfStock(), 3);
            assertEquals(gme.getTotalValue(), 960.4192875917308);

            HashMap<Integer, Double> gmeValueTracker = gme.getValueTracker();
            assertEquals(gmeValueTracker.size(), 2);
            assertEquals(gmeValueTracker.get(0), 975);
            assertEquals(gmeValueTracker.get(3), 960.4192875917308);

            // Confirm specifics of the second portfolio, including stock information
            Portfolio test2 = investorPortfolios.get("Test2");
            assertEquals(test2.getPortfolioName(), "Test2");
            assertEquals(test2.getPortfolioFunds(), 4308.75);
            assertEquals(test2.getPortfolioNetWorth(), 711.3895462366418);

            HashMap<String, Stock> test2StockMap = test2.getPortfolioMap();
            assertEquals(test2StockMap.size(), 2);
            assertTrue(test2StockMap.containsKey("NVDA"));
            assertTrue(test2StockMap.containsKey("MMM"));

            Stock nvda = test2StockMap.get("NVDA");
            assertEquals(nvda.getStockName(), "NVDA");
            assertEquals(nvda.getQuantityOfStock(), 1);
            assertEquals(nvda.getTotalValue(), 554.4319597147563);

            HashMap<Integer, Double> nvdaValueTracker = nvda.getValueTracker();
            assertEquals(nvdaValueTracker.size(), 2);
            assertEquals(nvdaValueTracker.get(0), 515.59);
            assertEquals(nvdaValueTracker.get(3), 554.4319597147563);

            Stock mmm = test2StockMap.get("MMM");
            assertEquals(mmm.getStockName(), "MMM");
            assertEquals(mmm.getQuantityOfStock(), 1);
            assertEquals(mmm.getTotalValue(), 156.95758652188542);

            HashMap<Integer, Double> mmmValueTracker = mmm.getValueTracker();
            assertEquals(mmmValueTracker.size(), 2);
            assertEquals(mmmValueTracker.get(0), 175.66);
            assertEquals(mmmValueTracker.get(3), 156.95758652188542);

        } catch (IOException e) {
            fail("Something went wrong");
        } catch (InvalidClassTypeException e) {
            fail("Something went wrong");
        } catch (IncompatibleStockMarketException e) {
            fail("Something went wrong");
        }
    }

    @Test
    public void testReadStockMarketDayZero() {
        JsonReader reader = new JsonReader("stockmarket","./data/testWriterStockMarketDayZero.json");

        try {
            StockMarket sm = (StockMarket) reader.read();
            assertEquals(sm.getStockList().size(), 10);
            assertEquals(sm.getDay(), 0);
            for (String s : sm.getStockList()) {
                if (s.equals("Day")) {
                    continue;
                }
                assertEquals(sm.getAllStockHistory(s).size(), 1);
            }
        } catch (IncompatibleStockMarketException e) {
            fail("Something went wrong");
        } catch (IOException e) {
            fail("Something went wrong");
        } catch (InvalidClassTypeException e) {
            fail("Something went wrong");
        }
    }

    @Test
    public void testReadStockMarketDayThree() {
        JsonReader reader = new JsonReader("Stockmarket","./data/testWriterStockMarketDayThree.json");

        try {
            StockMarket sm = (StockMarket) reader.read();
            assertEquals(sm.getStockList().size(), 10);
            assertEquals(sm.getDay(), 3);
            for (String s : sm.getStockList()) {
                if (s.equals("Day")) {
                    continue;
                }
                assertEquals(sm.getAllStockHistory(s).size(), 4);
            }
        } catch (IncompatibleStockMarketException e) {
            fail("Something went wrong");
        } catch (IOException e) {
            fail("Something went wrong");
        } catch (InvalidClassTypeException e) {
            fail("Something went wrong");
        }
    }

    @Test
    public void testReadStockMarketFailExpectedIncompatible() {
        JsonReader reader = new JsonReader("stockmarket","./data/testWriterStockMarketIncompatible.json");

        try {
            reader.read();
            fail("Something went wrong");
        } catch (IOException e) {
            fail("Something went wrong");
            e.printStackTrace();
        } catch (IncompatibleStockMarketException e) {
            // All good, test pass
        } catch (InvalidClassTypeException e) {
            fail("Something went wrong");
        }
    }
}
