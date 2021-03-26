package ui;

import model.Investor;
import model.StockMarket;
import ui.gui.*;

import javax.swing.*;

public class StonksGUIRunner {

    //private int activePage = 0;
    private StonksGUI activePageGUI;
    private JFrame frame;
    private JPanel panel;
    private Investor investor;
    private StockMarket sm;
    private boolean firstRun = true;

    public StonksGUIRunner() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(560,500);
        frame.setTitle("Stonks - A Stocks Investment Simulator");
        frame.setResizable(false);

        panel = new JPanel();

        investor = new Investor("Henry", 11000);
        investor.addPortfolio("test1", 5000);
        investor.addPortfolio("test2", 5000);

        sm = new StockMarket();

        investor.getPortfolioMap().get("test1").buyStock(sm, "GME", 5);
        investor.getPortfolioMap().get("test2").buyStock(sm, "AAPL", 2);

        displayActivePage(3);
    }

    public void displayActivePage(int activePage) {
//        panel.removeAll();
//        panel.setLayout(null);
        //frame.removeAll();
        if (activePage == 0) {
            activePageGUI = new HomePageGUI();
            activePageGUI.setStonksGUIRunner(this);
            refreshScreen();

        } else if (activePage == 1) {
            //investor = activePageGUI.getInvestor();
//            sm = activePageGUI.getStockMarket();
            //activePageGUI = new InvestorPageGUI(panel, investor);
            activePageGUI = new InvestorPageGUI(investor, sm);
            activePageGUI.setStonksGUIRunner(this);
            refreshScreen();
        } else if (activePage == 2) {
            //investor = activePageGUI.getInvestor();
            //sm = activePageGUI.getStockMarket();
            //String portfolioName = activePageGUI.getOutput();
            String portfolioName = "test1";
            activePageGUI = new PortfolioPageGUI(investor, portfolioName, sm);
            activePageGUI.setStonksGUIRunner(this);
            refreshScreen();
        } else if (activePage == 3) {
            //investor = activePageGUI.getInvestor();
            //sm = activePageGUI.getStockMarket();
            sm.updateStockPrice(4);
            activePageGUI = new StockMarketPageGUI(sm);
            activePageGUI.setStonksGUIRunner(this);
            refreshScreen();
        }
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
