package ui.gui;

import model.Investor;
import model.StockMarket;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

public class StockMarketPageGUI extends StonksGUI {
    private JTable stockTable;
    private JFreeChart chart;
    private ChartPanel chartPanel;
    private XYSeriesCollection stockDataSet;
    private JTextField submitText;
    private JLabel submitLabel;
    private JLabel errorLabel;
    private JLabel dayLabel;
    private String currentAction;

    public StockMarketPageGUI(Investor investor, StockMarket stockMarket) {
        super();
        this.sm = stockMarket;
        this.investor = investor;
        initializePageComponents();
        playBackgroundMusic();
    }

    @Override
    public void initializePageComponents() {
        loadLabels();
        loadStockPriceTable(false);
        loadButtons();
        loadTextFields();
    }

    private void loadTextFields() {
        submitText = new JTextField();
        submitText.setBounds(10, 400, 405, 25);
        submitText.setFont(textFont);
        panel.add(submitText);
    }

    private void loadButtons() {
        loadButtonStockPlot();
        loadButtonUpdateDay();
        loadButtonInvestorPage();
        loadButtonSave();
        loadButtonSubmit();
        loadButtonSound(true);
    }

    private void loadButtonSubmit() {
        JButton submitButton = new JButton(new AbstractAction("Submit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionProcedures();
            }
        });
        submitButton.setBounds(415, 400, 125, 25);
        submitButton.setFont(textFont);
        panel.add(submitButton);
    }

    private void actionProcedures() {
        String fromSubmit = submitText.getText();
        if (currentAction.equals("u")) {
            actionUpdateDay(fromSubmit);
        } else if (currentAction.equals("f")) {
            actionSave(fromSubmit);
        } else {
            errorLabel.setText("Select an appropriate option!");
        }
        submitText.setText(null);
    }

    private void actionSave(String fromSubmit) {
        try {
            saveFile(fromSubmit);
        } catch (FileNotFoundException exception) {
            errorLabel.setText("Error occurred during save, please try again");
        }
    }

    private void actionUpdateDay(String fromSubmit) {
        if (isInteger(fromSubmit)) {
            sm.updateStockPrice(Integer.parseInt(fromSubmit));
            investor.updateAllPortfolios(sm);
            loadStockPriceTable(true);
            dayLabel.setText("Current Day: " + sm.getDay());
        } else {
            errorLabel.setText("Invalid entry. Integer values only.");
        }
    }

    private void loadButtonSave() {
        JButton saveButton = new JButton(new AbstractAction("Save Game") {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitLabel.setText("Enter name of save:");
                currentAction = "f";
            }
        });
        saveButton.setBounds(270, 215, 250, 25);
        saveButton.setFont(textFont);
        panel.add(saveButton);
    }

    private void loadButtonInvestorPage() {
        JButton investorButton = new JButton(new AbstractAction("Return To Investor Menu") {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopBackgroundMusic();
                stonksAppRunner.displayActivePage(1);
            }
        });
        investorButton.setBounds(270, 180, 250, 25);
        investorButton.setFont(textFont);
        panel.add(investorButton);
    }

    private void loadButtonUpdateDay() {
        JButton updateDayButton = new JButton(new AbstractAction("Update Game Day") {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitLabel.setText("Enter number of days to update:");
                currentAction = "u";
            }
        });
        updateDayButton.setBounds(270, 110, 250, 25);
        updateDayButton.setFont(textFont);
        panel.add(updateDayButton);
    }

    private void loadButtonStockPlot() {
        JButton graphButton = new JButton(new AbstractAction("See Plot of Stocks") {
            @Override
            public void actionPerformed(ActionEvent e) {
                createDataSets(false);

                JFrame popup = new JFrame();
                popup.setSize(910,610);
                popup.setTitle("Stonks Stock Market");

                JPanel popupPanel = new JPanel();
                popupPanel.setLayout(null);

                chart = ChartFactory.createXYLineChart("Stock Market Price Graph", "Day", "Stock Price", stockDataSet);
                customizeChart();

                popupPanel.add(chartPanel);
                popup.add(popupPanel);
                popup.validate();
                popup.setResizable(false);
                popup.setVisible(true);
            }
        });
        graphButton.setBounds(270, 145, 250, 25);
        graphButton.setFont(textFont);
        panel.add(graphButton);
    }

    private void loadLabels() {
        loadLabelsHeadings();
        loadLabelDayCounter();
        loadLabelSubmit();
        loadLabelError();
    }

    private void loadLabelError() {
        errorLabel = new JLabel("");
        errorLabel.setBounds(10, 430, 530, 20);
        errorLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        errorLabel.setForeground(Color.RED);
        panel.add(errorLabel);
    }

    private void loadLabelSubmit() {
        submitLabel = new JLabel("Select an option above");
        submitLabel.setBounds(10, 380, 530, 20);
        submitLabel.setFont(miniFont);
        panel.add(submitLabel);
    }

    private void loadLabelDayCounter() {
        dayLabel = new JLabel("Current Day: " + sm.getDay());
        dayLabel.setBounds(270, 70, 250, 28);
        dayLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        panel.add(dayLabel);
    }

    private void loadLabelsHeadings() {
        JLabel homePageHeading1 = new JLabel("Welcome to the Stock Market!");
        homePageHeading1.setBounds(10, 20, 530, 30);
        homePageHeading1.setFont(headingFont);
        panel.add(homePageHeading1);

        JLabel subHeadingLabel = new JLabel("Current Stock Tickers");
        subHeadingLabel.setBounds(10, 70, 300, 28);
        Map<TextAttribute, Integer> fontAttributes = new HashMap<>();
        fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        subHeadingLabel.setFont(new Font("SansSerif", Font.BOLD, 20).deriveFont(fontAttributes));
        panel.add(subHeadingLabel);
    }

    private void customizeChart() {
        chartPanel = new ChartPanel(chart);
        chartPanel.setBounds(10, 10, 880, 560);
        chartPanel.setBorder(border);

        XYPlot plot = chart.getXYPlot();
        plot.setRenderer(createRenderer());
        plot.setBackgroundPaint(Color.DARK_GRAY);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);
    }

    private XYLineAndShapeRenderer createRenderer() {
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        int numStocks = sm.getStockList().size();
        for (int i = 0; i < numStocks; i++) {
            renderer.setSeriesShape(i, new Rectangle2D.Float(-2,-2,4,4));
        }

        return renderer;
    }

    private void loadStockPriceTable(boolean hasInitialized) {
        List<String> stockList = sm.getStockList();
        String[][] stockData = new String[stockList.size()][2];

        for (int i = 0; i < stockList.size(); i++) {
            stockData[i][0] = stockList.get(i);
            stockData[i][1] = String.format("%.2f", sm.getStockValue(stockList.get(i)));
        }

        if (hasInitialized) {
            panel.remove(stockTable);
            panel.repaint();
        }

        stockTable = new JTable(stockData, new String[] {"Stock Name", "Current Price"});
        stockTable.setBounds(10, 110, 250, 250);
        stockTable.setFont(textFont);
        stockTable.setRowHeight(25);
        stockTable.setRowMargin(10);
        stockTable.getColumnModel().setColumnMargin(2);
        stockTable.setBorder(border);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        stockTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        stockTable.getColumnModel().getColumn(0).setMinWidth(125);
        stockTable.getColumnModel().getColumn(1).setMinWidth(125);
        panel.add(stockTable);
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
