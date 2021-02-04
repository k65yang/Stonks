package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Represents an investment portfolio of an investor
public class Portfolio {
    private String name;                    // the name of the portfolio
    private HashMap<String, Stock> stockMap; // hashmap of all the stock in the portfolio
    private double funds;
    private double netWorth;

    // EFFECTS: name on portfolio set to name and the worth and change in value of
    //           this profile is initialized at zero
    public Portfolio(String name, double funds) {
        this.name = name;
        this.funds = funds;
        stockMap = new HashMap<>();
        netWorth = 0;
    }

    // REQUIRES: stock is an existing stock in the stock market
    //           quantity > 0
    //           must have enough funds in portfolio to afford the quantity of stock
    // MODIFIES: this
    // EFFECTS: adds stock to the portfolio
    public void buyStock(StockMarket sm, String stock, int quantityToBuy) {
        Stock toBuy;

        if (stockMap.containsKey(stock)) {
            toBuy = stockMap.get(stock);
            toBuy.adjustStockQuantity(quantityToBuy);
        } else {
            toBuy = new Stock(stock, sm.getStockValue(stock), sm.getDay(), quantityToBuy);
            stockMap.put(stock, toBuy);
        }

        double adjustment = sm.getStockValue(stock) * quantityToBuy;
        funds -= adjustment;
        netWorth += adjustment;
    }

    // REQUIRES: stock must be in this portfolio
    //           quantity > 0 and <= the quantity of the stock in this portfolio
    // MODIFIES: this
    // EFFECTS: sells the specified stock at market price and adds the amounts to the total funds
    public void sellStock(StockMarket sm, String stock, int quantityToSell) {
        Stock toSell = stockMap.get(stock);

        double stockCurrentValue = sm.getStockValue(stock);
        double adjustment = stockCurrentValue * quantityToSell;

        funds += adjustment;
        netWorth -= adjustment;

        if (toSell.getQuantityOfStock() == quantityToSell) {
            stockMap.remove(stock);
        } else {
            toSell.adjustStockQuantity(-quantityToSell);
//            stockMap.put(stock, toSell);
        }
    }

    // REQUIRES: stock must exist in the stock market
    //           quantityToAdd > 0
    // MODIFIES: this
    // EFFECTS: adds the stock to this portfolio, note that this is NOT the
    //          same as buying a stock because this costs no funds, however
    //          the new worth of the portfolio will still increase
    public void addStock(StockMarket sm, String stock, int quantityToAdd) {
        Stock toAdd;

        if (stockMap.containsKey(stock)) {
            toAdd = stockMap.get(stock);
            toAdd.adjustStockQuantity(quantityToAdd);
        } else {
            toAdd = new Stock(stock, sm.getStockValue(stock), sm.getDay(), quantityToAdd);
            stockMap.put(stock, toAdd);
        }

        netWorth += sm.getStockValue(stock) * quantityToAdd;
    }

    // REQUIRES: stock must exist in stock market and in current portfolio
    //           quantityToRemove > 0
    // MODIFIES: this
    // EFFECTS: removes the stock from this portfolio, note this is NOT the same
    //          as selling a stock, funds are not increase once removed, however
    //          the net worth will update appropriately
    public void removeStock(StockMarket sm, String stock, int quantityToRemove) {
        Stock toRemove = stockMap.get(stock);

        double stockCurrentValue = sm.getStockValue(stock);
        netWorth -= stockCurrentValue * quantityToRemove;

        if (toRemove.getQuantityOfStock() == quantityToRemove) {
            stockMap.remove(stock);
        } else {
            toRemove.adjustStockQuantity(-quantityToRemove);
        }
    }

    // REQUIRES: other portfolio must exist and have the stock to transfer
    //           quantity to transfer > 0
    // MODIFIES: this and otherPortfolio
    // EFFECTS: removes specified quantity of stock from other portfolio and
    //          adds it to this portfolio, recomputes the funds and net worth of the portfolio
    public void transferStock(StockMarket sm, Portfolio otherPortfolio, String toTransfer, int quantityToTransfer) {
        otherPortfolio.removeStock(sm, toTransfer, quantityToTransfer);
        addStock(sm, toTransfer, quantityToTransfer);
    }

    public String getPortfolioName() {
        return name;
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

    public double getPortfolioNetWorth() {
        return netWorth;
    }
}
