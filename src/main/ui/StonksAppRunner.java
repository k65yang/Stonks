package ui;

import model.Investor;
import model.StockMarket;
import ui.gui.*;

import javax.swing.*;

public class StonksAppRunner {
    private StonksGUI activePageGUI;
    private JFrame frame;
    private JPanel panel;
    private Investor investor;
    private StockMarket sm;

    public StonksAppRunner() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(560,500);
        frame.setTitle("Stonks - A Stocks Investment Simulator");
        frame.setResizable(false);

        displayActivePage(0);
    }

    public void displayActivePage(int activePage) {
        if (activePage == 0) {
            activePageGUI = new HomePageGUI();
            activePageGUI.setStonksAppRunner(this);
            refreshScreen();
        } else if (activePage == 1) {
            updateFields();
            activePageGUI = new InvestorPageGUI(investor, sm);
            activePageGUI.setStonksAppRunner(this);
            refreshScreen();
        } else if (activePage == 2) {
            String portfolioName = updateFields();
            activePageGUI = new PortfolioPageGUI(investor, portfolioName, sm);
            activePageGUI.setStonksAppRunner(this);
            refreshScreen();
        } else if (activePage == 3) {
            updateFields();
            activePageGUI = new StockMarketPageGUI(investor, sm);
            activePageGUI.setStonksAppRunner(this);
            refreshScreen();
        }
    }

    private String updateFields() {
        investor = activePageGUI.getInvestor();
        sm = activePageGUI.getStockMarket();
        return activePageGUI.getOutput();
    }

    private void refreshScreen() {
        panel = activePageGUI.getPanel();
        frame.getContentPane().removeAll();
        frame.repaint();
        frame.add(panel);
        frame.validate();
        frame.setVisible(true);
    }
}
