package ui.gui;

import model.Investor;
import model.Portfolio;
import model.Stock;
import model.StockMarket;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PortfolioPageGUI extends StonksGUI {

    private Portfolio portfolio;
    private JTextArea portfolioInfoTextArea;
    private JScrollPane scrollPane;
    private JTextField submitText;
    private JLabel submitLabel;
    private JLabel errorLabel;
    private String currentAction;

    public PortfolioPageGUI(Investor investor, String portfolioName, StockMarket stockMarket) {
        super();
        this.investor = investor;
        this.sm = stockMarket;
        this.portfolio = this.investor.getPortfolioMap().get(portfolioName);
        initializePageComponents();
    }

    @Override
    public void initializePageComponents() {
        Border border = BorderFactory.createLineBorder(Color.BLUE, 1);

        JLabel homePageHeading1 = new JLabel("Options for Portfolio \"" + portfolio.getPortfolioName() + "\"!");
        homePageHeading1.setBounds(10, 20, 530, 30);
        homePageHeading1.setFont(headingFont);
        homePageHeading1.setBorder(border);
        panel.add(homePageHeading1);

        loadPortfolioInfoArea(false);

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

        JButton investorButton = new JButton(new AbstractAction("Return To Investor Menu") {
            @Override
            public void actionPerformed(ActionEvent e) {
                stonksGUIRunner.displayActivePage(1);
            }
        });
        investorButton.setBounds(290, 305, 250, 25);
        investorButton.setFont(textFont);
        panel.add(investorButton);

        JButton stockMarketButton = new JButton(new AbstractAction("Go To Stock Market") {
            @Override
            public void actionPerformed(ActionEvent e) {
                stonksGUIRunner.displayActivePage(3);
            }
        });
        stockMarketButton.setBounds(290, 340, 250, 25);
        stockMarketButton.setFont(textFont);
        panel.add(stockMarketButton);

        JButton submitButton = new JButton(new AbstractAction("Submit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fromSubmit = submitText.getText();
                String[] tokens = fromSubmit.split("/");
                if (currentAction == "b") { // buy stock
                    if (validParse(tokens, "buy")) {
                        portfolio.buyStock(sm, tokens[0], Integer.parseInt(tokens[1]));
                        loadPortfolioInfoArea(true);
                    } else {
                        errorLabel.setText("Invalid stock or quanity. Double check spelling. Entry is case-sensitive.");
                    }
                } else if (currentAction == "s") {
                    if (validParse(tokens, "sell")) {
                        portfolio.sellStock(sm, tokens[0], Integer.parseInt(tokens[1]));
                        loadPortfolioInfoArea(true);
                    } else {
                        errorLabel.setText("Invalid stock or quanity. Double check spelling. Entry is case-sensitive.");
                    }
                } else if (currentAction == "t") {
                    if (validParse(tokens, "transfer")) {
                        portfolio.transferStock(sm, investor.getPortfolioMap().get(tokens[2]),
                                tokens[0], Integer.parseInt(tokens[1]));
                        loadPortfolioInfoArea(true);
                    } else {
                        errorLabel.setText("Entry error. Double check spelling. Entry is case-sensitive.");
                    }
                } else if (currentAction == "u") {
                    if (isInteger(fromSubmit)) {
                        sm.updateStockPrice(Integer.parseInt(fromSubmit));
                        investor.updateAllPortfolios(sm);
                        loadPortfolioInfoArea(true);
                    } else {
                        errorLabel.setText("Invalid entry. Integer values only.");
                    }
                }
                submitText.setText(null);
            }
        });
        submitButton.setBounds(415, 400, 125, 25);
        submitButton.setFont(textFont);
        panel.add(submitButton);

        submitLabel = new JLabel("Select an option above");
        submitLabel.setBounds(10, 380, 530, 20);
        submitLabel.setFont(miniFont);
        panel.add(submitLabel);

        submitText = new JTextField();
        submitText.setBounds(10, 400, 405, 25);
        submitText.setFont(textFont);
        panel.add(submitText);

        errorLabel = new JLabel("");
        errorLabel.setBounds(10, 430, 530, 20);
        errorLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        errorLabel.setForeground(Color.RED);
        errorLabel.setBorder(border);
        panel.add(errorLabel);
    }

    public void loadPortfolioInfoArea(boolean hasInitialized) {
        if (hasInitialized) {
            portfolioInfoTextArea.setText(null);
            printPortfolioInfo();
        } else {
            portfolioInfoTextArea = new JTextArea();
            portfolioInfoTextArea.setFont(miniPrintoutFont);
            portfolioInfoTextArea.setEditable(false);
            printPortfolioInfo();
            scrollPane = new JScrollPane(portfolioInfoTextArea);
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.setBounds(10, 60, 530, 200);
            panel.add(scrollPane);
        }
    }

    private void printPortfolioInfo() {
        portfolioInfoTextArea.append("---------------------- Portfolio Overview ----------------------\n");
        portfolioInfoTextArea.append("Name: " + portfolio.getPortfolioName() + "\n");
        String formatted = String.format("%.2f", portfolio.getPortfolioFunds());
        portfolioInfoTextArea.append("Available Funds: " + formatted + "\n");
        formatted = String.format("%.2f", portfolio.getPortfolioNetWorth());
        portfolioInfoTextArea.append("Net Worth of Stocks: $" + formatted + "\n\n");
        portfolioInfoTextArea.append("------------------- Stocks In This Portfolio -------------------\n");
        for (Stock s : portfolio.getPortfolioMap().values()) {
            portfolioInfoTextArea.append("Stock Name: " + s.getStockName() + "\n");
            portfolioInfoTextArea.append("Quantity Owned: " + s.getQuantityOfStock() + "\n");
            formatted = String.format("%.2f", s.getTotalValue());
            portfolioInfoTextArea.append("Total Worth of Stock: " + formatted + "\n");
            formatted = String.format("%.2f", sm.getAllStockHistory(s.getStockName()).get(0));
            portfolioInfoTextArea.append("Buy Price (Single): " + formatted + "\n");
            formatted = String.format("%.2f", sm.getAllStockHistory(s.getStockName()).get(sm.getDay()));
            portfolioInfoTextArea.append("Current Price (Single): " + formatted + "\n\n");
        }
    }

    private boolean validParse(String[] tokens, String action) {

        if (sm.containsStock(tokens[0]) && isInteger(tokens[1])) {
            if (action == "buy" && tokens.length == 2) {
                double totalCost = Integer.parseInt(tokens[1]) * sm.getStockValue(tokens[0]);
                if (totalCost <= portfolio.getPortfolioFunds()) {
                    return true;
                }
            } else if (action == "sell" && tokens.length == 2) {
                int quantityOfStock = portfolio.getPortfolioMap().get(tokens[0]).getQuantityOfStock();
                if (Integer.parseInt(tokens[1]) <= quantityOfStock) {
                    return true;
                }
            } else if (action == "transfer" && tokens.length == 3) {
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
