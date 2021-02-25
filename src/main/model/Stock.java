package model;

import org.json.JSONObject;

import java.util.HashMap;

// Represents a stock with a name, total value, quantity, and a value tracker to track the changes
// in the overall value of the stock
public class Stock {
    private String name;
    private double totalValue;
    private Integer quantity;
    private HashMap<Integer, Double> valueTracker;

    // REQUIRES: name is non-zero in length
    //           value > 0
    //           day >= 0
    //           quantity > 0
    // EFFECTS: the name of this stock is set to name; the value is set to value
    //          the quantity is set to quantity, the total value is set to
    //          quantity times the value, the day that this stock was bought and the
    //          total value is added to the value tracker
    public Stock(String name, Double value, Integer purchaseDay, Integer quantity) {
        this.name = name;
        this.totalValue = value * quantity;
        this.quantity = quantity;
        valueTracker = new HashMap<>();

        valueTracker.put(purchaseDay, this.totalValue);
    }

    // MODIFIES: this
    // EFFECTS: increases or decreases the quantity of the stock
    //          adjusts the total value of the stock accordingly
    public void adjustStockQuantity(int adjustment, StockMarket sm) {
        quantity += adjustment;
        totalValue = quantity * sm.getStockValue(name);
    }

    // MODIFIES: this
    // EFFECTS: updates the total value of the stock to the new values from the stock market
    //          updates the value tracker
    public void updatePrices(StockMarket sm) {
        double stockPricePerUnit = sm.getStockValue(this.name);
        totalValue = stockPricePerUnit * quantity;
        valueTracker.put(sm.getDay(), totalValue);
    }

    // EFFECTS: returns the name of this stock
    public String getStockName() {
        return name;
    }

    // EFFECTS: returns the total value of this stock
    public double getTotalValue() {
        return totalValue;
    }

    // EFFECTS: returns the total quantity of this stock
    public int getQuantityOfStock() {
        return quantity;
    }

    // EFFECTS: returns the value tracker for this stock
    public HashMap<Integer, Double> getValueTracker() {
        return valueTracker;
    }

    public void loadStockDataFromFile(JSONObject jsonStock) {
        name = jsonStock.getString("Stock Name");
        totalValue = jsonStock.getDouble("Total Value");
        quantity = jsonStock.getInt("Quantity");
        JSONObject valueTrackerObject = jsonStock.getJSONObject("Value Tracker");
        for (String s : valueTrackerObject.keySet()) {
            valueTracker.put(Integer.parseInt(s), valueTrackerObject.getDouble(s));
        }

    }

    public JSONObject toJson() {
        JSONObject jsonStock = new JSONObject();
        jsonStock.put("Stock Name", getStockName());
        jsonStock.put("Quantity", getQuantityOfStock());
        jsonStock.put("Total Value", getTotalValue());
        jsonStock.put("Value Tracker", valueTrackerMap());
        return jsonStock;
    }

    private JSONObject valueTrackerMap() {
        JSONObject jsonValueTracker = new JSONObject();
        for (Integer i : valueTracker.keySet()) {
            jsonValueTracker.put(i.toString(), valueTracker.get(i));
        }
        return jsonValueTracker;
    }

}
