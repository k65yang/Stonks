package model;

import java.util.HashMap;


public class Stock {
    private String name;
    private double value;
    private Integer quantity;
    private HashMap<Integer, Double> valueTracker;

    public Stock(String name, Double value, Integer day, Integer quantity) {
        this.name = name;
        this.value = value * quantity;
        this.quantity = quantity;
        valueTracker = new HashMap<>();

        valueTracker.put(day, this.value);
    }

    public void adjustStockQuantity(int adjustment) {
        quantity += adjustment;
    }

    public String getStockName() {
        return name;
    }

    public double getStockValue() {
        return value;
    }

    public int getQuantityOfStock() {
        return quantity;
    }

}
