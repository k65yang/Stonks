package model;

import java.lang.reflect.Array;
import java.util.*;

public class StockMarket {
    private Integer dayInGameTime;
    private HashMap<String, Double> stockMarket; // hashmap represents the stock and its values
    private HashMap<String, ArrayList<Double>> stockPriceTracker;

    public StockMarket() {
        dayInGameTime = 0;
        stockMarket = new HashMap<>();
        stockPriceTracker = new HashMap<>();

        initializeMarket();
        initializeStockPriceTracker();
    }

    private void initializeMarket() {

        // stockMarket automatically initialized with a set number of stocks
        stockMarket.put("MMM", 175.66);
        stockMarket.put("AMC", 13.26);
        stockMarket.put("GME", 325.00);
        stockMarket.put("NVDA", 515.59);
        stockMarket.put("GOOG", 1835.74);
        stockMarket.put("AMZN", 3206.20);
        stockMarket.put("AAPL", 131.96);
        stockMarket.put("TSLA", 793.53);
        stockMarket.put("PHUN", 2.08);
    }

    private void initializeStockPriceTracker() {

        // stockPriceTracker initialized with individual lists for each of the
        // existing stock, which has the initial prices for each stock
        for (String stock : stockMarket.keySet()) {
            ArrayList<Double> toAdd = new ArrayList<>();
            toAdd.add(stockMarket.get(stock));
            stockPriceTracker.put(stock, toAdd);
        }
    }

    // REQUIRES: days > 0
    // MODIFIES: this
    // EFFECTS: simulates daily market changes in the stock prices
    public void updateStockPrice(int days) {
        // stock price simulated by a random increase or decrease [-5, 5](%)
        // of existing stock price
        dayInGameTime += days;
        while (days > 0) {
            for (String stock : stockMarket.keySet()) {
                double multiplier = (Math.random() * 0.1) + 0.95;
                Double newStockVal = stockMarket.get(stock) * multiplier;
                stockMarket.put(stock, newStockVal);

                ArrayList<Double> priceTrackOfStock = stockPriceTracker.get(stock);
                priceTrackOfStock.add(newStockVal);
                stockPriceTracker.put(stock, priceTrackOfStock);
            }

            days--;
        }

    }

    public List<String> getStockList() {
        List<String> stockList = new ArrayList<>(stockMarket.keySet());
        Collections.sort(stockList);
        return stockList;
    }

    public boolean containsStock(String stock) {
        return stockMarket.containsKey(stock);
    }

    // REQUIRES: an existing stock ticker in the stock market
    public Double getStockValue(String stock) {
        return stockMarket.get(stock);
    }

    public ArrayList<Double> getAllStockHistory(String stock) {
        return stockPriceTracker.get(stock);
    }

    public int getDay() {
        return dayInGameTime;
    }

}
