package ui.gui;

import model.Stock;
import model.StockMarket;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.font.TextAttribute;
import java.util.*;
import java.util.List;

public class StockMarketPageGUI extends StonksGUI {
    private XYSeriesCollection stockDataSet;

    public StockMarketPageGUI(StockMarket stockMarket) {
        super();
        this.sm = stockMarket;
        createDataSets(false);
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

        JLabel subHeadingLable = new JLabel("Current Stock Tickers");
        subHeadingLable.setBounds(10, 70, 300, 28);
        Map<TextAttribute, Integer> fontAttributes = new HashMap<TextAttribute, Integer>();
        fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        subHeadingLable.setFont(new Font("SansSerif", Font.BOLD, 20).deriveFont(fontAttributes));
        panel.add(subHeadingLable);

        JButton testButton = new JButton(new AbstractAction("See Plot of Stocks") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame popup = new JFrame();
                popup.setSize(560,500);

                JPanel popupPanel = new JPanel();

                JFreeChart chart = ChartFactory.createXYLineChart("Title", "X", "Y", stockDataSet);
                ChartPanel chartPanel = new ChartPanel(chart);
                chartPanel.setBounds(10, 60, 530, 250);
                chartPanel.setBorder(border);
                popupPanel.add(chartPanel);

                popup.add(popupPanel);
                popup.validate();
                popup.setVisible(true);
            }
        });
        testButton.setBounds(10, 400, 250, 25);
        testButton.setFont(textFont);
        testButton.setBorder(border);
        panel.add(testButton);


    }

    public void createDataSets(boolean hasInitialized) {
        if (hasInitialized) {
            System.out.println("wrong");
        } else {
            stockDataSet = new XYSeriesCollection();
            createXYSeries();
            addStockHistoryToDataSet();
        }

    }

    private void createXYSeries() {
        stockDataSet.addSeries(new XYSeries("MMM"));
        stockDataSet.addSeries(new XYSeries("AMC"));
        stockDataSet.addSeries(new XYSeries("GME"));
        stockDataSet.addSeries(new XYSeries("NVDA"));
        stockDataSet.addSeries(new XYSeries("GOOG"));
        stockDataSet.addSeries(new XYSeries("AMZN"));
        stockDataSet.addSeries(new XYSeries("AAPL"));
        stockDataSet.addSeries(new XYSeries("TSLA"));
        stockDataSet.addSeries(new XYSeries("PHUN"));

    }

    private void addStockHistoryToDataSet() {
        List<Double> mmmHistory = sm.getAllStockHistory("MMM");
        List<Double> amcHistory = sm.getAllStockHistory("AMC");
        List<Double> gmeHistory = sm.getAllStockHistory("GME");
        List<Double> nvdaHistory = sm.getAllStockHistory("NVDA");
        List<Double> googHistory = sm.getAllStockHistory("GOOG");
        List<Double> amznHistory = sm.getAllStockHistory("AMZN");
        List<Double> aaplHistory = sm.getAllStockHistory("AAPL");
        List<Double> tslaHistory = sm.getAllStockHistory("TSLA");
        List<Double> phunHistory = sm.getAllStockHistory("PHUN");

        int totalDays = sm.getDay();

        for (int i = 0; i <= totalDays; i++) {
            stockDataSet.getSeries("MMM").add(i, mmmHistory.get(i));
            stockDataSet.getSeries("AMC").add(i, amcHistory.get(i));
            stockDataSet.getSeries("GME").add(i, gmeHistory.get(i));
            stockDataSet.getSeries("NVDA").add(i, nvdaHistory.get(i));
            stockDataSet.getSeries("GOOG").add(i, googHistory.get(i));
            stockDataSet.getSeries("AMZN").add(i, amznHistory.get(i));
            stockDataSet.getSeries("AAPL").add(i, aaplHistory.get(i));
            stockDataSet.getSeries("TSLA").add(i, tslaHistory.get(i));
            stockDataSet.getSeries("PHUN").add(i, phunHistory.get(i));
        }

    }
}
