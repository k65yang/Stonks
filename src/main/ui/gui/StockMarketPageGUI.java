package ui.gui;

import model.StockMarket;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class StockMarketPageGUI extends StonksGUI {

    public StockMarketPageGUI(StockMarket stockMarket) {
        super();
        this.sm = stockMarket;
        initializePageComponents();
    }

    @Override
    public void initializePageComponents() {
        Border border = BorderFactory.createLineBorder(Color.BLUE, 1);

        JLabel homePageHeading1 = new JLabel("Welcome to the Stock Market!");
        homePageHeading1.setBounds(10, 20, 530, 30);
        homePageHeading1.setFont(headingFont);
        homePageHeading1.setBorder(border);
        panel.add(homePageHeading1);
    }
}
