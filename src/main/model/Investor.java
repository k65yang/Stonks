package model;

import javax.sound.sampled.Port;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Represents an investor having a name and one or more portfolios
public class Investor {
    private String name;
    private double funds;
    private List<Portfolio> portfolioList;
    private HashMap<String, Portfolio> portfolioMap;
    private Portfolio activePortfolio;

    // REQUIRES: funds > 0
    // MODIFIES: this
    // EFFECTS: name on investor profile set to name and the available funds
    //          set to funds
    public Investor(String name, double funds) {
        this.name = name;
        this.funds = funds;
//        portfolioList = new ArrayList<>();
        portfolioMap = new HashMap<>();
    }

    // REQUIRES: name must be unique and not used for another portfolio
    //           cannot transfer more money to the portfolio than the person
    //           already has
    // MODIFIES: this
    // EFFECTS: adds a portfolio from the person
    public void addPortfolio(String name, double transferFunds) {
        Portfolio toAdd  = new Portfolio(name, transferFunds);
        portfolioMap.put(name, toAdd);
        funds -= transferFunds;
    }

    // REQUIRES: the portfolio must be empty (ie. have no stocks)
    // MODIFIES: this
    // EFFECTS: deletes specified portfolio from the investor and adds the portfolio
    //          funds back to the investor's total funds
    public void removePortfolio(String name) {
        funds += portfolioMap.get(name).getPortfolioFunds();
        portfolioMap.remove(name);
    }

    public void setActivePortfolio(String name) {
        activePortfolio = portfolioMap.get(name);
    }

    public void unsetActivePortfolio() {
        activePortfolio = null;
    }

    public void updateAllPortfolios(StockMarket sm) {
        for (Portfolio p : portfolioMap.values()) {
            p.updateStocks(sm);
        }
    }


    public String getInvestorName() {
        return name;
    }

    public double getInvestorFunds() {
        return funds;
    }

    public Portfolio getActivePortfolio() {
        return activePortfolio;
    }

    public HashMap<String, Portfolio> getPortfolioMap() {
        return portfolioMap;
    }
}
