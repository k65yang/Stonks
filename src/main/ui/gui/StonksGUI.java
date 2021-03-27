package ui.gui;

import model.Investor;
import model.StockMarket;
import persistence.JsonWriter;
import ui.StonksAppRunner;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.FileNotFoundException;

public abstract class StonksGUI {
    protected String output;
    protected JPanel panel;
    protected Investor investor;
    protected StockMarket sm;
    protected StonksAppRunner stonksAppRunner;
    protected final Font titleFont = new Font("SansSerif", Font.BOLD, 36);
    protected final Font headingFont = new Font("SansSerif", Font.BOLD, 24);
    protected final Font textFont = new Font("SansSerif", Font.BOLD, 18);
    protected final Font miniFont = new Font("SansSerif", Font.BOLD, 16);
    protected final Font miniPrintoutFont = new Font("DialogInput", Font.BOLD, 14);
    protected final Border border = BorderFactory.createLineBorder(Color.BLUE, 1);

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

    public void setStonksGUIRunner(StonksAppRunner toSet) {
        this.stonksAppRunner = toSet;
    }

    public void setInvestor(Investor investor) {
        this.investor = investor;
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

    protected void saveFile(String saveName) throws FileNotFoundException {
        String saveInvestorName = "Investor-" + saveName;
        String saveStockMarketName = "StockMarket-" + saveName;

        String investorDestination = "./data/" + saveInvestorName + ".json";
        String stockMarketDestination = "./data/" + saveStockMarketName + ".json";

        JsonWriter writerInvestor = new JsonWriter(investorDestination);
        JsonWriter writerStockMarket = new JsonWriter(stockMarketDestination);

        writerInvestor.open();
        writerInvestor.write(investor);
        writerInvestor.close();

        writerStockMarket.open();
        writerStockMarket.write(sm);
        writerStockMarket.close();
    }

    public abstract void initializePageComponents();

}
