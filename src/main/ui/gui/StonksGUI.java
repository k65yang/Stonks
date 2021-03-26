package ui.gui;

import model.Investor;
import model.StockMarket;
import ui.StonksGUIRunner;

import javax.swing.*;
import java.awt.*;

public abstract class StonksGUI {
    protected String output;
    protected JPanel panel;
    protected Investor investor;
    protected StockMarket sm;
    protected StonksGUIRunner stonksGUIRunner;
    protected final Font titleFont = new Font("SansSerif", Font.BOLD, 36);
    protected final Font headingFont = new Font("SansSerif", Font.BOLD, 24);
    protected final Font textFont = new Font("SansSerif", Font.BOLD, 18);
    protected final Font miniFont = new Font("SansSerif", Font.BOLD, 16);
    protected final Font miniPrintoutFont = new Font("DialogInput", Font.BOLD, 14);

    public StonksGUI() {
        panel = new JPanel();
        panel.setLayout(null);
    }

    public String getOutput() {
        return output;
    }

    public JPanel getPanel() {
        return panel;
    }

    public Investor getInvestor() {
        return investor;
    }

    public StockMarket getStockMarket() {
        return sm;
    }

    public void setStonksGUIRunner(StonksGUIRunner toSet) {
        this.stonksGUIRunner = toSet;
    }

    public void setInvestor(Investor investor) {
        this.investor = investor;
    }

    public void setOutput(String toSet) {
        output = toSet;
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

    protected boolean isInteger(String toCheck) {
        try {
            Integer.parseInt(toCheck);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public abstract void initializePageComponents();

}
