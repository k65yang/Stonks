package ui.gui;

import model.Investor;
import model.Portfolio;
import model.StockMarket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;

public class InvestorPageGUI extends StonksGUI {
    private JTextArea investorInfoTextArea;
    private JTextField submitText;
    private JLabel submitLabel;
    private JLabel errorLabel;
    private String currentAction;

    public InvestorPageGUI(Investor investor, StockMarket sm) {
        super();
        this.investor = investor;
        this.sm = sm;
        currentAction = null;
        initializePageComponents();
        playBackgroundMusic();
    }

    @Override
    public void initializePageComponents() {
        loadLabels();
        loadInvestorInfoArea(false);
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
        loadButtonAddPortfolio();
        loadButtonAddFundsToPortfolio();
        loadButtonRemovePortfolio();
        loadButtonViewPortfolio();
        loadButtonMergePortfolio();
        loadButtonStockMarket();
        loadButtonUpdateDay();
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
        if (currentAction.equals("a")) { // add portfolio
            actionAddPortfolio(fromSubmit);
        } else if (currentAction.equals("m")) { // add funds to portfolio
            actionAddFundsToPortfolio(fromSubmit);
        } else if (currentAction.equals("r")) { // remove portfolio
            actionRemovePortfolio(fromSubmit);
        } else if (currentAction.equals("p")) { // go to portfolio menu
            actionPortfolioMenu(fromSubmit);
        } else if (currentAction.equals("g")) {
            actionMergePortfolios(fromSubmit);
        } else if (currentAction.equals("u")) { // update day
            actionUpdateDay(fromSubmit);
        } else if (currentAction.equals("f")) {
            actionSaveToFile(fromSubmit);
        } else {
            errorLabel.setText("Select an appropriate option!");
        }
        submitText.setText(null);
    }

    private void actionSaveToFile(String fromSubmit) {
        try {
            saveFile(fromSubmit);
        } catch (FileNotFoundException exception) {
            errorLabel.setText("An error has occurred, please try again");
        }
    }

    private void actionUpdateDay(String fromSubmit) {
        if (isInteger(fromSubmit)) {
            sm.updateStockPrice(Integer.parseInt(fromSubmit));
            investor.updateAllPortfolios(sm);
            loadInvestorInfoArea(true);
        } else {
            errorLabel.setText("Invalid entry. Integer values only.");
        }
    }

    private void actionMergePortfolios(String fromSubmit) {
        if (investor.getPortfolioMap().containsKey(fromSubmit)) {
            investor.mergeIntoOnePortfolio(fromSubmit, sm);
            loadInvestorInfoArea(true);
        }
    }

    private void actionPortfolioMenu(String fromSubmit) {
        if (investor.getPortfolioMap().containsKey(fromSubmit)) {
            stopBackgroundMusic();
            output = fromSubmit;
            stonksAppRunner.displayActivePage(2);
        } else {
            errorLabel.setText("Invalid entry. Double check spelling.");
        }
    }

    private void actionRemovePortfolio(String fromSubmit) {
        if (investor.getPortfolioMap().containsKey(fromSubmit)) {
            investor.removePortfolio(fromSubmit);
            loadInvestorInfoArea(true);
        } else {
            errorLabel.setText("Invalid entry. Double check spelling.");
        }
    }

    private void actionAddFundsToPortfolio(String fromSubmit) {
        String[] tokens = fromSubmit.split("/");
        if (tokens.length == 2 && isDouble(tokens[1])
                && Double.parseDouble(tokens[1]) <= investor.getInvestorFunds()
                && investor.getPortfolioMap().containsKey(tokens[0])) {
            investor.addFundsToPortfolio(tokens[0], Double.parseDouble(tokens[1]));
            loadInvestorInfoArea(true);
        } else {
            errorLabel.setText("Invalid entry. Double check input.");
        }
    }

    private void actionAddPortfolio(String fromSubmit) {
        String[] tokens = fromSubmit.split("/");
        if (tokens.length == 2 && isDouble(tokens[1])
                && Double.parseDouble(tokens[1]) <= investor.getInvestorFunds()
                && !investor.getPortfolioMap().containsKey(tokens[0])) {
            investor.addPortfolio(tokens[0], Double.parseDouble(tokens[1]));
            loadInvestorInfoArea(true);
        } else {
            errorLabel.setText("Invalid entry. Double check format is correct.");
        }
    }

    private void loadButtonSave() {
        JButton saveButton = new JButton(new AbstractAction("Save Game") {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitLabel.setText("Enter the name of this save");
                currentAction = "f";
            }
        });
        saveButton.setBounds(290, 325, 250, 25);
        saveButton.setFont(textFont);
        panel.add(saveButton);
    }

    private void loadButtonUpdateDay() {
        JButton updateDayButton = new JButton(new AbstractAction("Update Game Day") {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitLabel.setText("Enter the number of days to update");
                currentAction = "u";
            }
        });
        updateDayButton.setBounds(290, 290, 250, 25);
        updateDayButton.setFont(textFont);
        panel.add(updateDayButton);
    }

    private void loadButtonStockMarket() {
        JButton stockmarketButton = new JButton(new AbstractAction("Go To StockMarket") {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopBackgroundMusic();
                stonksAppRunner.displayActivePage(3);
            }
        });
        stockmarketButton.setBounds(290, 255, 250, 25);
        stockmarketButton.setFont(textFont);
        panel.add(stockmarketButton);
    }

    private void loadButtonMergePortfolio() {
        JButton mergePortfolioButton = new JButton(new AbstractAction("Merge All Portfolios") {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitLabel.setText("Enter name of portfolio (must currently exist)");
                currentAction = "g";
            }
        });
        mergePortfolioButton.setBounds(290, 220, 250, 25);
        mergePortfolioButton.setFont(textFont);
        panel.add(mergePortfolioButton);
    }

    private void loadButtonViewPortfolio() {
        JButton viewPortfolioButton = new JButton(new AbstractAction("View Existing Portfolio") {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitLabel.setText("Enter portfolio name to view");
                currentAction = "p";
            }
        });
        viewPortfolioButton.setBounds(10, 325, 250, 25);
        viewPortfolioButton.setFont(textFont);
        panel.add(viewPortfolioButton);
    }

    private void loadButtonRemovePortfolio() {
        JButton removePortfolioButton = new JButton(new AbstractAction("Remove Portfolio") {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitLabel.setText("Enter portfolio name to remove");
                currentAction = "r";
            }
        });
        removePortfolioButton.setBounds(10, 290, 250, 25);
        removePortfolioButton.setFont(textFont);
        panel.add(removePortfolioButton);
    }

    private void loadButtonAddFundsToPortfolio() {
        JButton addFundsToPorfolioButton = new JButton(new AbstractAction("Add Funds To Portfolio") {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitLabel.setText("Enter portfolio name and funds to add in the format NAME/FUNDS");
                currentAction = "m";
            }
        });
        addFundsToPorfolioButton.setBounds(10, 255, 250, 25);
        addFundsToPorfolioButton.setFont(textFont);
        panel.add(addFundsToPorfolioButton);
    }

    private void loadButtonAddPortfolio() {
        JButton addPortfolioButton = new JButton(new AbstractAction("Add New Portfolio") {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitLabel.setText("Enter name of new portfolio and funds in the format NAME/FUNDS");
                currentAction = "a";
            }
        });
        addPortfolioButton.setBounds(10, 220, 250, 25);
        addPortfolioButton.setFont(textFont);
        panel.add(addPortfolioButton);
    }

    private void loadLabels() {
        JLabel homePageHeading1 = new JLabel("Welcome to to the investor homepage " + investor.getInvestorName() + "!");
        homePageHeading1.setBounds(10, 20, 530, 30);
        homePageHeading1.setFont(headingFont);
        panel.add(homePageHeading1);

        submitLabel = new JLabel("Select an option above");
        submitLabel.setBounds(10, 380, 530, 20);
        submitLabel.setFont(miniFont);
        panel.add(submitLabel);

        errorLabel = new JLabel("");
        errorLabel.setBounds(10, 430, 530, 20);
        errorLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        errorLabel.setForeground(Color.RED);
        panel.add(errorLabel);
    }

    private void loadInvestorInfoArea(boolean hasInitialized) {
        if (hasInitialized) {
            investorInfoTextArea.setText(null);
            printInvestorInfo();
        } else {
            investorInfoTextArea = new JTextArea();
            investorInfoTextArea.setFont(miniPrintoutFont);
            investorInfoTextArea.setEditable(false);
            printInvestorInfo();
            JScrollPane scrollPane = new JScrollPane(investorInfoTextArea);
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.setBounds(10, 60, 530, 150);
            panel.add(scrollPane);
        }

    }

    private void printInvestorInfo() {
        investorInfoTextArea.append("--------------------- Investor Overview ----------------------\n");
        investorInfoTextArea.append("Name: " + investor.getInvestorName() + "\n");
        String formatted = String.format("%.2f", investor.getInvestorFunds());
        investorInfoTextArea.append("Available Funds: " + formatted + "\n\n");
        investorInfoTextArea.append("--------------------- Portfolio Overview ---------------------\n");
        for (Portfolio p : investor.getPortfolioMap().values()) {
            investorInfoTextArea.append("Portfolio: " + p.getPortfolioName() + "\n");
            formatted = String.format("%.2f", p.getPortfolioFunds());
            investorInfoTextArea.append("Available Funds in Portfolio: $" + formatted + "\n");
            formatted = String.format("%.2f", p.getPortfolioNetWorth());
            investorInfoTextArea.append("Net Worth of Stocks: $" + formatted + "\n");
            investorInfoTextArea.append("\n\n");
        }
    }
}
