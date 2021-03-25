package ui.gui;

import model.Investor;
import model.Portfolio;
import model.Stock;
import model.StockMarket;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class PortfolioPageGUI extends StonksGUI {

    private Portfolio portfolio;
    private JTextArea portfolioInfoTextArea;
    private JScrollPane scrollPane;

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

        JLabel homePageHeading1 = new JLabel("Options for Portfolio " + portfolio.getPortfolioName() + "!");
        homePageHeading1.setBounds(10, 20, 530, 30);
        homePageHeading1.setFont(headingFont);
        homePageHeading1.setBorder(border);
        panel.add(homePageHeading1);

        loadPortfolioInfoArea(false);
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
            scrollPane.setBounds(10, 60, 530, 150);
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
            portfolioInfoTextArea.append("Total Worth of Stock: " + s.getTotalValue() + "\n");
            formatted = String.format("%.2f", sm.getAllStockHistory(s.getStockName()).get(0));
            portfolioInfoTextArea.append("Buy Price (Single): " + formatted + "\n");
            formatted = String.format("%.2f", sm.getAllStockHistory(s.getStockName()).get(sm.getDay()));
            portfolioInfoTextArea.append("Current Price (Single): " + formatted + "\n\n");
        }
    }
}
