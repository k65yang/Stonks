package ui.gui;

import model.Investor;
import model.Portfolio;
import model.Stock;
import model.StockMarket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

// A class representing the GUI investor page of the Stonks application, subtype of the StonksGUI class
public class PortfolioPageGUI extends StonksGUI {
    private final Portfolio portfolio;
    private JTextArea portfolioInfoTextArea;
    private JTextField submitText;
    private JLabel submitLabel;
    private JLabel errorLabel;
    private String currentAction;

    // MODIFIES: this
    // EFFECTS: constructs an portfolio menu page for the specified portfolio, portfolioName
    public PortfolioPageGUI(Investor investor, String portfolioName, StockMarket stockMarket) {
        super();
        this.investor = investor;
        this.sm = stockMarket;
        this.portfolio = this.investor.getPortfolioMap().get(portfolioName);
        currentAction = "x";
        initializePageComponents();
        playBackgroundMusic();
    }

    @Override
    // MODIFIES: this
    // EFFECTS: adds labels, text fields, buttons to the panels to initialize the portfolio page
    public void initializePageComponents() {
        loadLabels();
        loadPortfolioInfoArea(false);
        loadButtons();
        loadTextFields();
    }

    // MODIFIES: this
    // EFFECTS: adds the text fields to the portfolio page
    private void loadTextFields() {
        submitText = new JTextField();
        submitText.setBounds(10, 400, 405, 25);
        submitText.setFont(textFont);
        panel.add(submitText);
    }

    // MODIFIES: thie
    // EFFECTS: adds the buttons to the portfolio page
    private void loadButtons() {
        loadButtonBuyStock();
        loadButtonSellStock();
        loadButtonTransferStock();
        loadButtonUpdateDay();
        loadButtonInvestorPage();
        loadButtonStockMarket();
        loadButtonSubmit();
        loadButtonSound(true);
    }

    // MODIFIES: this, submitButton
    // EFFECTS: adds the submit button with an action listener to process the current action
    private void loadButtonSubmit() {
        JButton submitButton = new JButton(new AbstractAction("Submit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionProcedures();
            }
        });
        submitButton.setBounds(415, 400, 125, 25);
        submitButton.setFont(textFont);
        panel.add(submitButton);
    }

    // MODIFIES: this
    // EFFECTS: processes the current action when the submit button is pressed
    //          tells the user if the current action is not valid
    //          finally, deletes text in the submitText field
    private void actionProcedures() {
        String fromSubmit = submitText.getText();
        String[] tokens = fromSubmit.split("/");
        if (currentAction.equals("b")) { // buy stock
            actionBuyStock(tokens);
        } else if (currentAction.equals("s")) {
            actionSellStock(tokens);
        } else if (currentAction.equals("t")) {
            actionTransferStock(tokens);
        } else if (currentAction.equals("u")) {
            actionUpdateDay(fromSubmit);
        } else {
            errorLabel.setText("Select an appropriate option!");
        }
        submitText.setText(null);
    }

    // MODIFIES: this
    // EFFECTS: if input is an integer, will update the game day be the specified amount
    //          else, will notify the user of error through the errorLabel
    private void actionUpdateDay(String fromSubmit) {
        if (isInteger(fromSubmit)) {
            sm.updateStockPrice(Integer.parseInt(fromSubmit));
            investor.updateAllPortfolios(sm);
            loadPortfolioInfoArea(true);
        } else {
            errorLabel.setText("Invalid entry. Integer values only.");
        }
    }

    // MODIFIES: this
    // EFFECTS: if input is valid, will transfer the specified quantity of stock from the
    //          other specified portfolio to this portfolio
    //          else, will notify the uwer of error through the errorLabel
    private void actionTransferStock(String[] tokens) {
        if (validParse(tokens, "transfer")) {
            portfolio.transferStock(sm, investor.getPortfolioMap().get(tokens[2]),
                    tokens[0], Integer.parseInt(tokens[1]));
            loadPortfolioInfoArea(true);
        } else {
            errorLabel.setText("Entry error. Double check spelling. Entry is case-sensitive.");
        }
    }

    // MODIFIES: this
    // EFFECTS: is input is valid, will sell the specified quantity of stock
    //          else, will notify the uwer of error through the errorLabel
    private void actionSellStock(String[] tokens) {
        if (validParse(tokens, "sell")) {
            portfolio.sellStock(sm, tokens[0], Integer.parseInt(tokens[1]));
            loadPortfolioInfoArea(true);
        } else {
            errorLabel.setText("Invalid stock or quanity. Double check spelling. Entry is case-sensitive.");
        }
    }

    // MODIFIES: this
    // EFFECTS: if input is valid, will buy the specified quantity of stock
    //          else, will notify the uwer of error through the errorLabel
    private void actionBuyStock(String[] tokens) {
        if (validParse(tokens, "buy")) {
            portfolio.buyStock(sm, tokens[0], Integer.parseInt(tokens[1]));
            loadPortfolioInfoArea(true);
        } else {
            errorLabel.setText("Invalid stock or quanity. Double check spelling. Entry is case-sensitive.");
        }
    }

    // MODIFIES: this, stockMarketButton
    // EFFECTS: will bring the user to the stock market page
    private void loadButtonStockMarket() {
        JButton stockMarketButton = new JButton(new AbstractAction("Go To Stock Market") {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopBackgroundMusic();
                stonksAppRunner.displayActivePage(3);
            }
        });
        stockMarketButton.setBounds(290, 340, 250, 25);
        stockMarketButton.setFont(textFont);
        panel.add(stockMarketButton);
    }

    // MODIFIES: this, investorButton
    // EFFECTS: will bring the user to the investor page
    private void loadButtonInvestorPage() {
        JButton investorButton = new JButton(new AbstractAction("Return To Investor Menu") {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopBackgroundMusic();
                stonksAppRunner.displayActivePage(1);
            }
        });
        investorButton.setBounds(290, 305, 250, 25);
        investorButton.setFont(textFont);
        panel.add(investorButton);
    }

    // MODIFIES: this, updateDayButton
    // EFFECTS: adds a update day button to the panel with an action listener to set the submitLabel
    //          text and current action when pressed
    private void loadButtonUpdateDay() {
        JButton updateDayButton = new JButton(new AbstractAction("Update Game Day") {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitLabel.setText("Enter the number of days to update");
                currentAction = "u";
            }
        });
        updateDayButton.setBounds(290, 270, 250, 25);
        updateDayButton.setFont(textFont);
        panel.add(updateDayButton);
    }

    // MODIFIES: this, transferStockButton
    // EFFECTS: adds a transfer stock button to the panel with an action listener to set the submitLabel
    //          text and current action when pressed
    private void loadButtonTransferStock() {
        JButton transferStockButton = new JButton(new AbstractAction("Transfer Stock From") {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitLabel.setText("Enter stock, quantity, and transfer portfolio (STOCK/QUAN/PORT):");
                currentAction = "t";
            }
        });
        transferStockButton.setBounds(10, 340, 250, 25);
        transferStockButton.setFont(textFont);
        panel.add(transferStockButton);
    }

    // MODIFIES: this, sellStockButton
    // EFFECTS: adds a sell stock button to the panel with an action listener to set the submitLabel
    //          text and current action when pressed
    private void loadButtonSellStock() {
        JButton sellStockButton = new JButton(new AbstractAction("Sell Stock") {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitLabel.setText("Enter stock name and quantity you wish to sell (STOCK/QUANTITY):");
                currentAction = "s";
            }
        });
        sellStockButton.setBounds(10, 305, 250, 25);
        sellStockButton.setFont(textFont);
        panel.add(sellStockButton);
    }

    // MODIFIES: this, buyStockButton
    // EFFECTS: adds a buy stock day button to the panel with an action listener to set the submitLabel
    //          text and current action when pressed
    private void loadButtonBuyStock() {
        JButton buyStockButton = new JButton(new AbstractAction("Buy Stock") {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitLabel.setText("Enter stock name and quantity you wish to buy (STOCK/QUANTITY):");
                currentAction = "b";
            }
        });
        buyStockButton.setBounds(10, 270, 250, 25);
        buyStockButton.setFont(textFont);
        panel.add(buyStockButton);
    }

    // MODIFIES: this
    // EFFECTS: adds the labels onto the panel
    private void loadLabels() {
        JLabel homePageHeading1 = new JLabel("Options for Portfolio \"" + portfolio.getPortfolioName() + "\"!");
        homePageHeading1.setBounds(10, 20, 530, 30);
        homePageHeading1.setFont(headingFont);
        panel.add(homePageHeading1);

        submitLabel = new JLabel("Select an option above");
        submitLabel.setBounds(10, 380, 530, 20);
        submitLabel.setFont(miniFont);
        panel.add(submitLabel);

        errorLabel = new JLabel("");
        errorLabel.setBounds(10, 430, 530, 20);
        errorLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        errorLabel.setForeground(Color.RED);
        panel.add(errorLabel);
    }

    // MODIFIES: this
    // EFFECTS: if portfolioInfoTextArea has not been initialized, it will be initialized
    //          then adds portfolio information onto the portfolioInfoTextArea
    public void loadPortfolioInfoArea(boolean hasInitialized) {
        if (hasInitialized) {
            portfolioInfoTextArea.setText(null);
            printPortfolioInfo();
        } else {
            portfolioInfoTextArea = new JTextArea();
            portfolioInfoTextArea.setFont(miniPrintoutFont);
            portfolioInfoTextArea.setEditable(false);
            printPortfolioInfo();
            JScrollPane scrollPane = new JScrollPane(portfolioInfoTextArea);
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.setBounds(10, 60, 530, 200);
            panel.add(scrollPane);
        }
    }

    // MODIFIES: this
    // EFFECTS: adds portfolio information onto the portfolioInfoTextArea
    private void printPortfolioInfo() {
        portfolioInfoTextArea.append("---------------------- Portfolio Overview ----------------------\n");
        portfolioInfoTextArea.append("Name: " + portfolio.getPortfolioName() + "\n");
        String formatted = String.format("%.2f", portfolio.getPortfolioFunds());
        portfolioInfoTextArea.append("Available Funds: $" + formatted + "\n");
        formatted = String.format("%.2f", portfolio.getPortfolioNetWorth());
        portfolioInfoTextArea.append("Net Worth of Stocks: $" + formatted + "\n\n");
        portfolioInfoTextArea.append("------------------- Stocks In This Portfolio -------------------\n");
        for (Stock s : portfolio.getPortfolioMap().values()) {
            portfolioInfoTextArea.append("Stock Name: " + s.getStockName() + "\n");
            portfolioInfoTextArea.append("Quantity Owned: " + s.getQuantityOfStock() + "\n");
            formatted = String.format("%.2f", s.getTotalValue());
            portfolioInfoTextArea.append("Total Worth of Stock: $" + formatted + "\n");
            formatted = String.format("%.2f", sm.getAllStockHistory(s.getStockName()).get(sm.getDay()));
            portfolioInfoTextArea.append("Current Price (Single): $" + formatted + "\n\n");
        }
    }

    // EFFECTS: determines if the given tokens is valid with respect to the specified action
    private boolean validParse(String[] tokens, String action) {
        if (sm.containsStock(tokens[0]) && isInteger(tokens[1])) {
            // determines if input is valid for a buy action
            if (action.equals("buy") && tokens.length == 2) {
                double totalCost = Integer.parseInt(tokens[1]) * sm.getStockValue(tokens[0]);
                return totalCost <= portfolio.getPortfolioFunds();
            } else if (action.equals("sell") && tokens.length == 2) { // determines if input is valid for a sell action
                int quantityOfStock = portfolio.getPortfolioMap().get(tokens[0]).getQuantityOfStock();
                return Integer.parseInt(tokens[1]) <= quantityOfStock;
            } else if (action.equals("transfer") && tokens.length == 3) { // determines if input is valid for a transfer
                if (investor.getPortfolioMap().containsKey(tokens[2])) {
                    Portfolio otherPortfolio = investor.getPortfolioMap().get(tokens[2]);
                    if (otherPortfolio.isStockInPortfolio(tokens[0])) {
                        int quantityOfStock = otherPortfolio.getPortfolioMap().get(tokens[0]).getQuantityOfStock();
                        return Integer.parseInt(tokens[1]) <= quantityOfStock;
                    }
                }
            }
        }

        return false;
    }
}
