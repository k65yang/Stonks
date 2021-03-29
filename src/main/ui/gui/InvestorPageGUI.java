package ui.gui;

import model.Investor;
import model.Portfolio;
import model.StockMarket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;

// A class representing the GUI investor page of the Stonks application, subtype of the StonksGUI class
public class InvestorPageGUI extends StonksGUI {
    private JTextArea investorInfoTextArea;
    private JTextField submitText;
    private JLabel submitLabel;
    private JLabel errorLabel;
    private String currentAction;

    // MODIFIES: this
    // EFFECTS: constructs an investor menu page
    public InvestorPageGUI(Investor investor, StockMarket sm) {
        super();
        this.investor = investor;
        this.sm = sm;
        currentAction = null;
        initializePageComponents();
        playBackgroundMusic();
    }

    @Override
    // MODIFIES: this
    // EFFECTS: adds labels, text fields, buttons to the panels to initialize the investor page
    public void initializePageComponents() {
        loadLabels();
        loadInvestorInfoArea(false);
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

    // MODIFIES: this
    // EFFECTS: attempts to save the current state of the game, if exception is thrown,
    //          the game will notify the user through the errorLabel
    private void actionSaveToFile(String fromSubmit) {
        try {
            saveFile(fromSubmit);
        } catch (FileNotFoundException exception) {
            errorLabel.setText("An error has occurred, please try again");
        }
    }

    // MODIFIES: this
    // EFFECTS: if input is an integer, will update the game day be the specified amount
    //          else, will notify the user of error through the errorLabel
    private void actionUpdateDay(String fromSubmit) {
        if (isInteger(fromSubmit)) {
            sm.updateStockPrice(Integer.parseInt(fromSubmit));
            investor.updateAllPortfolios(sm);
            loadInvestorInfoArea(true);
        } else {
            errorLabel.setText("Invalid entry. Integer values only.");
        }
    }

    // MODIFIES: this
    // EFFECTS: if input is a valid portfolio name, will merge all other portfolios into
    //          the specified portfolio, else will notify user of an error through the
    //          errorLabel
    private void actionMergePortfolios(String fromSubmit) {
        if (investor.getPortfolioMap().containsKey(fromSubmit)) {
            investor.mergeIntoOnePortfolio(fromSubmit, sm);
            loadInvestorInfoArea(true);
        }
    }

    // MODIFIES: this
    // EFFECTS: if input is a valid portfolio name, will go to the specified portfolio page
    //          else, will notify user of an error through the errorLabel
    private void actionPortfolioMenu(String fromSubmit) {
        if (investor.getPortfolioMap().containsKey(fromSubmit)) {
            stopBackgroundMusic();
            output = fromSubmit;
            stonksAppRunner.displayActivePage(2);
        } else {
            errorLabel.setText("Invalid entry. Double check spelling.");
        }
    }

    // MODIFIES: this
    // EFFECTS: if input is a valid portfolio name, will remove the specified portfolio
    //          else, will notify user of an error through the errorLabel
    private void actionRemovePortfolio(String fromSubmit) {
        if (investor.getPortfolioMap().containsKey(fromSubmit)) {
            investor.removePortfolio(fromSubmit);
            loadInvestorInfoArea(true);
        } else {
            errorLabel.setText("Invalid entry. Double check spelling.");
        }
    }

    // MODIFIES: this
    // EFFECTS: if input is in valid format, and there is enough funds and the specified
    //          portfolio exists, then the specified funds will be added to the specified
    //          portfolio, else, the user will be notified of error through the errorLabel
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

    // MODIFIES: this
    // EFFECTS: if the input is in valid format, and there is enough funds and the portfolio
    //          name is available, the specified portfolio will be created, else the user
    //          will be notified of error through the errorLabel
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

    // MODIFIES: this, saveButton
    // EFFECTS: adds a load button to the panel with an action listener to set the submitLabel
    //          text and current action when pressed
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

    // MODIFIES: this, updateDayButton
    // EFFECTS: adds a update day button to the panel with an action listener to set the submitLabel
    //          text and current action when pressed
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

    // MODIFIES: this, stockmarketButton
    // EFFECTS: adds a stock market button to the panel with an action listener to set the submitLabel
    //          text and current action when pressed
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

    // MODIFIES: this, mergePortfolioButton
    // EFFECTS: adds a merge button to the panel with an action listener to set the submitLabel
    //          text and current action when pressed
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

    // MODIFIES: this, viewPortfolioButton
    // EFFECTS: adds a view portfolio button to the panel with an action listener to set the submitLabel
    //          text and current action when pressed
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

    // MODIFIES: this, removePortfolioButton
    // EFFECTS: adds a remove portfolio button to the panel with an action listener to set the submitLabel
    //          text and current action when pressed
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

    // MODIFIES: this, addFundsToPorfolioButton
    // EFFECTS: adds a add funds to portfolio button to the panel with an action listener
    //          to set the submitLabel text and current action when pressed
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

    // MODIFIES: this, addPortfolioButton
    // EFFECTS: adds a add portfolio button to the panel with an action listener to set the submitLabel
    //          text and current action when pressed
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

    // MODIFIES: this
    // EFFECTS: adds the labels onto the panel
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

    // MODIFIES: this
    // EFFECTS: if investorInfoTextArea has not been initialized, it will be initialized
    //          then adds investor information onto the investorInfoTextArea
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

    // MODIFIES: this
    // EFFECTS: adds investor information onto the investorInfoTextArea
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
