package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.HashMap;

// Represents an investor having a name and one or more portfolios
public class Investor implements Writable {
    private String name;
    private double funds;
    private HashMap<String, Portfolio> portfolioMap;
    private Portfolio activePortfolio;

    // REQUIRES: funds > 0
    // MODIFIES: this
    // EFFECTS: name on investor profile set to name and the available funds
    //          set to funds
    public Investor(String name, double funds) {
        this.name = name;
        this.funds = funds;
        portfolioMap = new HashMap<>();
    }

    // REQUIRES: name must be unique and not used for another portfolio
    //           addFunds < funds
    // MODIFIES: this
    // EFFECTS: adds a portfolio from the person
    public void addPortfolio(String name, double addFunds) {
        Portfolio toAdd  = new Portfolio(name, addFunds);
        portfolioMap.put(name, toAdd);
        funds -= addFunds;
    }

    // REQUIRES: name must be a portfolio that exists
    //           amount > 0
    // MODIFIES: this
    // EFFECTS: increases the funds to the specified portfolio by a amount
    public void addFundsToPortfolio(String name, double amount) {
        Portfolio toAdd = portfolioMap.get(name);
        toAdd.addFunds(amount);
    }

    // REQUIRES: the portfolio must be empty (ie. have no stocks)
    // MODIFIES: this
    // EFFECTS: deletes specified portfolio from the investor and adds the portfolio
    //          funds back to the investor's total funds
    public void removePortfolio(String name) {
        funds += portfolioMap.get(name).getPortfolioFunds();
        portfolioMap.remove(name);
    }

    // MODIFIES: portfolio p
    // EFFECTS: updates the portfolio using the new prices from the stock market
    public void updateAllPortfolios(StockMarket sm) {
        for (Portfolio p : portfolioMap.values()) {
            p.updateStocks(sm);
        }
    }

    // REQUIRES: name must be a name of a portfolio that exists
    // MODIFIES: this
    // EFFECTS: contents of all portfolios will be transferred into the specified portfolio
    public void mergeIntoOnePortfolio(String name, StockMarket sm) {
        Portfolio merged = portfolioMap.get(name);
        for (Portfolio p : portfolioMap.values()) {
            if (p.getPortfolioName() != merged.getPortfolioName()) {
                for (Stock s : p.getPortfolioMap().values()) {
                    merged.transferStock(sm, p, s.getStockName(), s.getQuantityOfStock());
                }
                merged.addFunds(p.getPortfolioFunds());
            }
        }
        portfolioMap.keySet().removeIf(k -> !(k.equals(name)));
    }


    // MODIFIES: this
    // EFFECTS: sets the active portfolio to the specified portfolio, and returns it
    public Portfolio setActivePortfolio(String name) {
        activePortfolio = portfolioMap.get(name);
        return activePortfolio;
    }

    // MODIFIES: this
    // EFFECTS: deselects the active portfolio
    public void unsetActivePortfolio() {
        activePortfolio = null;
    }

    // MODIFIES: this
    // EFFECTS: updates all the investor portfolios to match the information from the stock market

    // EFFECTS: returns the name of the investor
    public String getInvestorName() {
        return name;
    }

    // EFFECTS: returns the available funds that this investor has
    public double getInvestorFunds() {
        return funds;
    }

    // EFFECTS: returns the hashmap containing all the portfolios
    public HashMap<String, Portfolio> getPortfolioMap() {
        return portfolioMap;
    }


    @Override
    // EFFECTS: returns all the investor data as a JSONObject
    public JSONObject toJson() {
        JSONObject jsonInvestor = new JSONObject();
        jsonInvestor.put("Investor", name);
        jsonInvestor.put("Total funds", funds);
        jsonInvestor.put("Portfolios", portfolioToJson());
        return jsonInvestor;
    }

    // EFFECTS: returns all the portfolios of the investor as a single JSONObject
    private JSONObject portfolioToJson() {
        JSONObject jsonPortfolio = new JSONObject();
        for (String p : portfolioMap.keySet()) {
            jsonPortfolio.put(p, portfolioMap.get(p).toJson());
        }
        return jsonPortfolio;
    }

    // MODIFIES: this
    // EFFECTS: loads the investor data from the JSONObject
    public void loadInvestorFromFile(JSONObject json) {
        JSONObject jsonPortfolio = json.getJSONObject("Portfolios");
        for (String key : jsonPortfolio.keySet()) {
            Portfolio p = loadPortfolioFromFile(key, 0.0);
            p.loadPortfolioFromFile(jsonPortfolio.getJSONObject(key));
        }
    }

    // Special method to be used when loading from file
    // MODIFIES: this
    // EFFECTS: creates a new portfolio with name and funds
    public Portfolio loadPortfolioFromFile(String name, double funds) {
        Portfolio toAdd  = new Portfolio(name, funds);
        portfolioMap.put(name, toAdd);
        return toAdd;
    }
}
