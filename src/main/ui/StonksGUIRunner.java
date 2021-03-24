package ui;

import model.Investor;
import ui.gui.HomePageGUI;
import ui.gui.InvestorPageGUI;
import ui.gui.StonksGUI;

import javax.swing.*;

public class StonksGUIRunner {

    //private int activePage = 0;
    private StonksGUI activePageGUI;
    private JFrame frame;
    private JPanel panel;
    private Investor investor;
    private boolean firstRun = true;

    public StonksGUIRunner() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700,500);
        frame.setTitle("Stonks - A Stocks Investment Simulator");
        frame.setResizable(false);

        panel = new JPanel();

        investor = new Investor("Henry", 11000);
        investor.addPortfolio("test1", 5000);
        investor.addPortfolio("test2", 5000);

        displayActivePage(1);
    }

    public void displayActivePage(int activePage) {
        panel.removeAll();
        panel.setLayout(null);
        //frame.removeAll();
        if (activePage == 0) {
            activePageGUI = new HomePageGUI(panel);
            activePageGUI.setStonksGUIRunner(this);
            panel = activePageGUI.getPanel();
            frame.add(panel);
            frame.setVisible(true);
//            runOnce = true;

        } else if (activePage == 1) {
            //investor = activePageGUI.getInvestor();
            //activePageGUI = new InvestorPageGUI(panel, investor);
            activePageGUI = new InvestorPageGUI(panel, investor);
            panel = activePageGUI.getPanel();
            frame.getContentPane().removeAll();
            frame.repaint();
            frame.add(panel);
            frame.validate();
            frame.setVisible(true);
        }
    }
}
