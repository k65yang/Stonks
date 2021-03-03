package model;

import exceptions.IncompatiableStockMarketException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.*;

// Represents a stock market with various stocks that fluctuate randomly in price
public class StockMarket implements Writable {
    private Integer dayInGameTime;                                 // the day in game time,
    private HashMap<String, Double> stockMarket;                   // hashmap represents the stock and its values
    private HashMap<String, ArrayList<Double>> stockPriceTracker;  // keeps track of the daily prices of each stock

    // EFFECTS: constructs a stock market
    public StockMarket() {
        dayInGameTime = 0;
        stockMarket = new HashMap<>();
        stockPriceTracker = new HashMap<>();

        initializeMarket();
        initializeStockPriceTracker();
    }

    // MODIFIES: this
    // EFFECTS: adds ten stocks to the stock market
    private void initializeMarket() {
        stockMarket.put("MMM", 175.66);
        stockMarket.put("AMC", 13.26);
        stockMarket.put("GME", 325.00);
        stockMarket.put("NVDA", 515.59);
        stockMarket.put("GOOG", 1835.74);
        stockMarket.put("AMZN", 3206.20);
        stockMarket.put("AAPL", 131.96);
        stockMarket.put("TSLA", 793.53);
        stockMarket.put("PHUN", 2.08);
        stockMarket.put("TEST", 100.00);
    }

    // MODIFIES: this
    // EFFECTS: initializes empty array lists for each stock in the stock market to
    //          store the prices for each stock and adds the initial price to each stock
    private void initializeStockPriceTracker() {
        for (String stock : stockMarket.keySet()) {
            ArrayList<Double> toAdd = new ArrayList<>();
            toAdd.add(stockMarket.get(stock));
            stockPriceTracker.put(stock, toAdd);
        }
    }

    // REQUIRES: days > 0
    // MODIFIES: this
    // EFFECTS: simulates daily market changes in the stock prices; the stock price is
    //          randomly increased or decrease [-5, 10] (%)
    public void updateStockPrice(int days) {
        dayInGameTime += days;
        while (days > 0) {
            for (String stock : stockMarket.keySet()) {
                double multiplier = (Math.random() * 0.15) + 0.95;
                Double newStockVal = stockMarket.get(stock) * multiplier;
                stockMarket.put(stock, newStockVal);

                ArrayList<Double> priceTrackOfStock = stockPriceTracker.get(stock);
                priceTrackOfStock.add(newStockVal);
                stockPriceTracker.put(stock, priceTrackOfStock);
            }

            days--;
        }

    }

    // EFFECTS: returns the a list of all the stocks in the market, sorted alphabetically
    public List<String> getStockList() {
        List<String> stockList = new ArrayList<>(stockMarket.keySet());
        Collections.sort(stockList);
        return stockList;
    }

    // EFFECTS: returns true if the stock is in the stock market
    public boolean containsStock(String stock) {
        return stockMarket.containsKey(stock);
    }

    // REQUIRES: an existing stock ticker in the stock market
    // EFFECTS: returns the current value of the stock
    public Double getStockValue(String stock) {
        return stockMarket.get(stock);
    }

    // REQUIRES: an existing stock ticker in the stock market
    // EFFECTS: returns the price history of the stock
    public ArrayList<Double> getAllStockHistory(String stock) {
        return stockPriceTracker.get(stock);
    }

    // EFFECTS: returns the day in the stock market
    public int getDay() {
        return dayInGameTime;
    }

    @Override
    // EFFECTS: returns all this stock market's data as a JSONObject
    public JSONObject toJson() {
        JSONObject jsonStockMarket = new JSONObject();
        jsonStockMarket.put("Day", dayInGameTime);
        for (String s : stockMarket.keySet()) {
            ArrayList<Double> priceList = stockPriceTracker.get(s);
            JSONArray stockPriceTracker = new JSONArray(priceList);
            jsonStockMarket.put(s, stockPriceTracker);
        }
        return jsonStockMarket;
    }

    // EFFECTS: loads the stock market data from the JSONObject
    public void loadFromFile(JSONObject jsonStockMarket) throws IncompatiableStockMarketException {
        dayInGameTime = jsonStockMarket.getInt("Day");
        for (String s : jsonStockMarket.keySet()) {
            if (s.equals("Day")) {
                continue;
            } else if (!containsStock(s)) {
                throw new IncompatiableStockMarketException();
            } else {
                List<Double> priceTrackerList = new ArrayList<>();
                JSONArray priceTrackerJson = jsonStockMarket.getJSONArray(s);
                for (int i = 0; i < priceTrackerJson.length(); i++) {
                    priceTrackerList.add(priceTrackerJson.getDouble(i));
                }
                stockPriceTracker.put(s, (ArrayList)priceTrackerList);
                stockMarket.put(s, priceTrackerList.get(priceTrackerList.size() - 1));
            }

        }
    }
}
