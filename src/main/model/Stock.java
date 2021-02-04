package model;

import java.util.HashMap;


public class Stock {
    private String name;
    private double totalValue;
    private Integer quantity;
    private HashMap<Integer, Double> valueTracker;

    public Stock(String name, Double value, Integer day, Integer quantity) {
        this.name = name;
        this.totalValue = value * quantity;
        this.quantity = quantity;
        valueTracker = new HashMap<>();

        valueTracker.put(day, this.totalValue);
    }

    public void adjustStockQuantity(int adjustment) {
        quantity += adjustment;
    }

    public String getStockName() {
        return name;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public int getQuantityOfStock() {
        return quantity;
    }

}
