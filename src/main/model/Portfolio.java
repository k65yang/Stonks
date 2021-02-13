package model;

import java.util.HashMap;

// Represents an investment portfolio of an investor containing funds and various stock
public class Portfolio {
    private String name;                     // the name of the portfolio
    private HashMap<String, Stock> stockMap; // hashmap of all the stock in the portfolio
    private double funds;                    // the available funds in this portfolio to buy stock
    private double netWorth;                 // the total worth of all the stock in thie portfolio

    // REQUIRES: name is non-zero in length
    //           funds > 0
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
            toBuy.adjustStockQuantity(quantityToBuy, sm);
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
            toSell.adjustStockQuantity(-quantityToSell, sm);
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
            toAdd.adjustStockQuantity(quantityToAdd, sm);
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
            toRemove.adjustStockQuantity(-quantityToRemove, sm);
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

    // MODIFIES: this
    // EFFECTS: updates all the stock in this portfolio to reflect the most up to date prices
    //          updates the net worth of all stock accordingly
    public void updateStocks(StockMarket sm) {
        for (Stock s : stockMap.values()) {
            s.updatePrices(sm);
        }

        netWorth = 0;
        for (Stock s : getPortfolioMap().values()) {
            double stockValue = s.getTotalValue();
            netWorth += stockValue;
        }
    }

    // EFFECTS: returns true if the given stock is in this portfolio, false otherwise
    public boolean isStockInPortfolio(String stock) {
        return stockMap.containsKey(stock);
    }

    // MODIFIES: this
    // EFFECTS: increases the funds by the amount
    public void addFunds(double amount) {
        funds += amount;
    }

    // EFFECTS: returns the name of this portfolio as a String
    public String getPortfolioName() {
        return name;
    }

    // EFFECTS: returns the hashmap representation of all the stocks in the portfolio
    public HashMap<String, Stock> getPortfolioMap() {
        return stockMap;
    }

    // REQUIRES: the stock must be in this portfolio
    // EFFECTS: returns a Stock in the stock map
    public Stock getStockInPortfolio(String stock) {
        return stockMap.get(stock);
    }

    // EFFECTS: returns the available funds in the portfolio
    public double getPortfolioFunds() {
        return funds;
    }

    // EFFECTS: returns the net worth of the stocks in the portfolio
    public double getPortfolioNetWorth() {
        return netWorth;
    }
}
