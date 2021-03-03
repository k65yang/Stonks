package persistence;

import model.Investor;
import model.Portfolio;
import model.StockMarket;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest {

    @Test
    public void testWriteIncorrectFileName() {
        try {
            JsonWriter writer = new JsonWriter("./data/\ntestWriterGeneralInvestorFINAL.json");
        } catch (Exception e) {
            // All good, test passed
        }
    }

    @Test
    public void testWriteEmptyInvestor() {
        try {
            Investor i = new Investor("Empty", 10000);
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyInvestor.json");
            writer.open();
            writer.write(i);
            writer.close();
        } catch (Exception e) {
            fail("Something went wrong!");
        }
    }

    @Test
    public void testWriteGeneralInvestor() {
        try {
            StockMarket sm = new StockMarket();

            Investor i = new Investor("Henry", 100000.00);
            i.addPortfolio("Test1", 3000);
            i.addPortfolio("Test2", 5000);
            Portfolio active = i.setActivePortfolio("Test1");

            active.buyStock(sm, "GME", 3);
            active.buyStock(sm, "AAPL", 1);

            active = i.setActivePortfolio("Test2");
            active.buyStock(sm, "MMM", 1);
            active.buyStock(sm, "NVDA", 1);

            sm.updateStockPrice(3);
            i.updateAllPortfolios(sm);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralInvestor.json");
            writer.open();
            writer.write(i);
            writer.close();


        } catch (IOException e) {
            fail("Something went wrong!");
        }
    }

    @Test
    public void testWriteStockMarketDayZero() {
        StockMarket sm = new StockMarket();

        try {
            JsonWriter writer = new JsonWriter("./data/testWriterStockMarketDayZero.json");
            writer.open();
            writer.write(sm);
            writer.close();
        } catch (FileNotFoundException e) {
            fail("Something went wrong!");
        }
    }

    @Test
    public void testWriteStockMarketDayThree() {
        StockMarket sm = new StockMarket();
        sm.updateStockPrice(3);

        try {
            JsonWriter writer = new JsonWriter("./data/testWriterStockMarketDayThree.json");
            writer.open();
            writer.write(sm);
            writer.close();
        } catch (FileNotFoundException e) {
            fail("Something went wrong!");
        }
    }
}
