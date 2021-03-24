package ui.gui;

import model.Investor;
import ui.StonksGUIRunner;

import javax.swing.*;
import java.awt.*;

public abstract class StonksGUI {
    protected JPanel panel;
    protected Investor investor;
    protected StonksGUIRunner stonksGUIRunner;
    protected final Font titleFont = new Font("SansSerif", Font.BOLD, 36);
    protected final Font headingFont = new Font("SansSerif", Font.BOLD, 24);
    protected final Font textFont = new Font("SansSerif", Font.BOLD, 18);
    protected final Font miniFont = new Font("SansSerif", Font.BOLD, 16);
    protected final Font miniPrintoutFont = new Font("DialogInput", Font.BOLD, 14);

    public StonksGUI() {
    }

    private void displayActivePage(int activePage) {
        // create the new frame
        JFrame frame = new JFrame();

        JButton createProfileButton = new JButton("Create investor profile");

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        panel.setLayout(new GridLayout(0,2));
        panel.add(createProfileButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        frame.setTitle("Stonks - A Stocks Investment Simulator");
        //frame.pack();
        frame.setVisible(true);

    }

//    private void displayActivePageTest(int activePage) {
//        // create the new frame
//        JFrame frame = new JFrame();
//
//        // set up contents plane
//        JPanel panel = new JPanel();
//        panel.setLayout(null);
//
//        StonksGUI homePageGUI = new HomePageGUI(panel);
//        panel = homePageGUI.getPanel();
//
//
//        // display the window
//        frame.add(panel);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(700,500);
//        frame.setTitle("Stonks - A Stocks Investment Simulator");
//        frame.setResizable(false);
//        frame.setVisible(true);
//    }

    public JPanel getPanel() {
        return panel;
    }

    public Investor getInvestor() {
        return investor;
    }

    public void setStonksGUIRunner(StonksGUIRunner toSet) {
        this.stonksGUIRunner = toSet;
    }

    public void setInvestor(Investor investor) {
        this.investor = investor;
    }

    public void clearPanel() {
        panel.removeAll();
    }

    protected boolean isDouble(String toCheck) {
        try {
            Double.parseDouble(toCheck);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public abstract void initializePageComponents();

}
