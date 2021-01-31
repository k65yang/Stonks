package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Represents an investment portfolio of an investor
public class Portfolio {
    private String name;                    // the name of the portfolio
    private List<Stock> stockList;          // the list of names of stocks in the portfolio
    private HashMap<String, Stock> stockMap; // hashmap of all the stock in the portfolio
    private double funds;

    // EFFECTS: name on portfolio set to name and the worth and change in value of
    //           this profile is initialized at zero
    public Portfolio(String name, double funds) {
        this.name = name;
        this.funds = funds;
        stockList = new ArrayList<>();
        stockMap = new HashMap<>();
    }

    // REQUIRES: stock is an existing stock in the stock market
    //           quantity > 0
    //           must have enough funds in portfolio to afford the quantity of stock
    // MODIFIES: this
    // EFFECTS: adds stock to the portfolio
    public void addStock(StockMarket sm, String stock, int quantity) {
        Stock toAdd;
        if (stockMap.containsKey(stock)) {
            toAdd = stockMap.get(stock);
            toAdd.adjustStockQuantity(quantity);
        } else {
            toAdd = new Stock(stock, sm.getStockValue(stock), sm.getDay(), quantity);
            stockMap.put(stock, toAdd);
        }

        funds -= sm.getStockValue(stock) * quantity;
    }

    // REQUIRES: stock must be in this portfolio
    //           quantity > 0 and <= the quantity of the stock in this portfolio
    // MODIFIES: this
    // EFFECTS: sells the specified stock at market price and adds the amounts to the total funds
    public void sellStock(StockMarket sm, String stock, int quantityToSell) {
        Stock stockToSell = stockMap.get(name);
        double stockCurrentValue = sm.getStockValue(stock);
        funds += stockCurrentValue * quantityToSell;
        if (stockToSell.getQuantityOfStock() == quantityToSell) {
            stockMap.remove(stock);
        } else {
            stockToSell.adjustStockQuantity(-quantityToSell);
            stockMap.put(stock, stockToSell);
        }
    }

    public String getPorfolioName() {
        return name;
    }

    public List<Stock> getPortfolioStockList() {
        return stockList;
    }

    public HashMap<String, Stock> getPortfolioMap() {
        return stockMap;
    }

    public Stock getStockInPortfolio(String stock) {
        return stockMap.get(stock);
    }

    public double getPortfolioFunds() {
        return funds;
    }
}
