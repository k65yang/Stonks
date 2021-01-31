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
//        portfolioList.add(toAdd);
        funds -= transferFunds;
    }

    // REQUIRES: the portfolio must be empty (ie. have no stocks)
    // MODIFIES: this
    // EFFECTS: deletes specified portfolio from the investor and adds the portfolio
    //          funds back to the investor's total funds
    public void removePortfolio(String name) {
        portfolioMap.remove(name);
//        for (Portfolio p : portfolioList) {
//            if (p.getPorfolioName() == name) {
//                funds += p.getPortfolioFunds();
//                portfolioList.remove(p);
//                break;
//            }
//        }
    }

//    public void updatePortfolio(StockMarket sm) {
//        for
//    }

    public String getInvestorname() {
        return name;
    }

    public double getInvestorFunds() {
        return funds;
    }

    public void getAllPortfolios() {
        for (String p : portfolioMap.keySet()) {
            System.out.println("Portfolio Name: " + p);
        }
    }
}
