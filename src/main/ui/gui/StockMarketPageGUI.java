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

// A class representing the GUI Stock Market page of the Stonks application, subtype of the StonksGUI class
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

    // MODIFIES: this
    // EFFECTS constructs a stock market menu page
    public StockMarketPageGUI(Investor investor, StockMarket stockMarket) {
        super();
        this.sm = stockMarket;
        this.investor = investor;
        initializePageComponents();
        playBackgroundMusic();
    }

    @Override
    // MODIFIES: this
    // EFFECTS: adds labels, text fields, buttons to the panels to initialize the investor page
    public void initializePageComponents() {
        loadLabels();
        loadStockPriceTable(false);
        loadButtons();
        loadTextFields();
    }

    // MODIFIES: this
    // EFFECTS: adds the text fields to the investor page
    private void loadTextFields() {
        submitText = new JTextField();
        submitText.setBounds(10, 400, 405, 25);
        submitText.setFont(textFont);
        panel.add(submitText);
    }

    // MODIFIES: this
    // EFFECTS: adds the buttons to the investor page
    private void loadButtons() {
        loadButtonStockPlot();
        loadButtonUpdateDay();
        loadButtonInvestorPage();
        loadButtonSave();
        loadButtonSubmit();
        loadButtonSound(true);
    }

    // MODIFIES: this, submitButton
    // EFFECTS: adds the submit button with an action listener to process the current action
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

    // MODIFIES: this
    // EFFECTS: processes the current action when the submit button is pressed
    //          tells the user if the current action is not valid
    //          finally, deletes text in the submitText field
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

    // MODIFIES: this
    // EFFECTS: attempts to save the current state of the game, if exception is thrown,
    //          the game will notify the user through the errorLabel
    private void actionSave(String fromSubmit) {
        try {
            saveFile(fromSubmit);
        } catch (FileNotFoundException exception) {
            errorLabel.setText("Error occurred during save, please try again");
        }
    }

    // MODIFIES: this
    // EFFECTS: if input is an integer, will update the game day be the specified amount
    //          else, will notify the user of error through the errorLabel
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

    // MODIFIES: this, saveButton
    // EFFECTS: adds a load button to the panel with an action listener to set the submitLabel
    //          text and current action when pressed
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

    // MODIFIES: this, investorButton
    // EFFECTS: adds a investor button to the panel with an action listener to set the submitLabel
    //          text and current action when pressed
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

    // MODIFIES: this, updateDayButton
    // EFFECTS: adds a update day button to the panel with an action listener to set the submitLabel
    //          text and current action when pressed
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

    // MODIFIES: this, graphButton
    // EFFECTS: adds a graph button to the panel with an action listener to create a plot of the
    //          stock history of all stocks on a new popup window
    private void loadButtonStockPlot() {
        JButton graphButton = new JButton(new AbstractAction("See Plot of Stocks") {
            @Override
            public void actionPerformed(ActionEvent e) {
                createDataSets();

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

    // MODIFIES: this
    // EFFECTS: adds labels onto the panel
    private void loadLabels() {
        loadLabelsHeadings();
        loadLabelDayCounter();
        loadLabelSubmit();
        loadLabelError();
    }

    // MODIFIES: this
    // EFFECTS: adds the error label to the panel
    private void loadLabelError() {
        errorLabel = new JLabel("");
        errorLabel.setBounds(10, 430, 530, 20);
        errorLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        errorLabel.setForeground(Color.RED);
        panel.add(errorLabel);
    }

    // MODIFIES: this
    // EFFECTS: adds the submit label to the panel
    private void loadLabelSubmit() {
        submitLabel = new JLabel("Select an option above");
        submitLabel.setBounds(10, 380, 530, 20);
        submitLabel.setFont(miniFont);
        panel.add(submitLabel);
    }

    // MODIFIES: this
    // EFFECTS: adds the day label to the panel
    private void loadLabelDayCounter() {
        dayLabel = new JLabel("Current Day: " + sm.getDay());
        dayLabel.setBounds(270, 70, 250, 28);
        dayLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        panel.add(dayLabel);
    }

    // MODIFIES: this
    // EFFECTS; adds the heading labels to the panel
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

    // MODIFIES: this
    // EFFECTS: creates and customizes the chart component
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

    // EFFECTS: returns a renderer specifying the symbology on the chart plot
    private XYLineAndShapeRenderer createRenderer() {
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        int numStocks = sm.getStockList().size();
        for (int i = 0; i < numStocks; i++) {
            renderer.setSeriesShape(i, new Rectangle2D.Float(-2,-2,4,4));
        }

        return renderer;
    }

    // MODIFIES: this
    // EFFECTS: adds a chart to show the current price of the all stocks
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

    // MOFIDIES: this
    // EFFECTS: generates the stock market dataset to plot
    public void createDataSets() {
        stockDataSet = new XYSeriesCollection();
        createXYSeries();
        addStockHistoryToDataSet();

    }

    // MODIFIES: this
    // EFFECTS: creates individual series to store each of the stocks data points
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

    // MODIFIES: this
    // EFFECTS: adds the values related to the stock price for each stock in the stock market
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
