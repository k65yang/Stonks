package ui.gui;

import model.Investor;
import model.Portfolio;
import model.StockMarket;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;

public class InvestorPageGUI extends StonksGUI {
    private JScrollPane scrollPane;
    private JTextArea investorInfoTextArea;
    private JTextField submitText;
    private JLabel submitLabel;
    private JLabel errorLabel;
    private String currentAction;

    public InvestorPageGUI(Investor investor, StockMarket sm) {
        super();
//        this.panel = panel;
//        panel = new JPanel();
//        panel.setLayout(null);
        this.investor = investor;
        this.sm = sm;
        initializePageComponents();
        currentAction = null;
    }

    @Override
    public void initializePageComponents() {
        Border border = BorderFactory.createLineBorder(Color.BLUE, 1);

        JLabel homePageHeading1 = new JLabel("Welcome to to the investor homepage " + investor.getInvestorName() + "!");
        homePageHeading1.setBounds(10, 20, 530, 30);
        homePageHeading1.setFont(headingFont);
        homePageHeading1.setBorder(border);
        panel.add(homePageHeading1);

        loadInvestorInfoArea(false);

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

        JButton mergePortfolioButton = new JButton(new AbstractAction("Merge All Portfolios") {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitText.setText("Enter name of portfolio (must currently exist)");
                currentAction = "g";
            }
        });
        mergePortfolioButton.setBounds(290, 220, 250, 25);
        mergePortfolioButton.setFont(textFont);
        panel.add(mergePortfolioButton);

        JButton stockmarketButton = new JButton(new AbstractAction("Go To StockMarket") {
            @Override
            public void actionPerformed(ActionEvent e) {
                stonksGUIRunner.displayActivePage(3);
            }
        });
        stockmarketButton.setBounds(290, 255, 250, 25);
        stockmarketButton.setFont(textFont);
        panel.add(stockmarketButton);

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

        JButton submitButton = new JButton(new AbstractAction("Submit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fromSubmit = submitText.getText();
                if (currentAction == "a") { // add portfolio
                    String[] tokens = fromSubmit.split("/");
                    if (tokens.length == 2 && isDouble(tokens[1]) &&
                            Double.parseDouble(tokens[1]) <= investor.getInvestorFunds()) {
                        investor.addPortfolio(tokens[0], Double.parseDouble(tokens[1]));
                        loadInvestorInfoArea(true);
                    } else {
                        errorLabel.setText("Invalid entry. Double check format is correct.");
                    }
                } else if (currentAction == "m") { // add funds to portfolio
                    String[] tokens = fromSubmit.split("/");
                    if (tokens.length == 2 && isDouble(tokens[1]) &&
                            Double.parseDouble(tokens[1]) <= investor.getInvestorFunds() &&
                            investor.getPortfolioMap().containsKey(tokens[0])) {
                        investor.addFundsToPortfolio(tokens[0], Double.parseDouble(tokens[1]));
                        loadInvestorInfoArea(true);
                    } else {
                        errorLabel.setText("Invalid entry. Double check input.");
                    }
                } else if (currentAction == "r") { // remove portfolio
                    if (investor.getPortfolioMap().containsKey(fromSubmit)) {
                        investor.removePortfolio(fromSubmit);
                        loadInvestorInfoArea(true);
                    } else {
                        errorLabel.setText("Invalid entry. Double check spelling.");
                    }
                } else if (currentAction == "p") { // go to portfolio menu
                    if (investor.getPortfolioMap().containsKey(fromSubmit)) {
                        output = fromSubmit;
                        stonksGUIRunner.displayActivePage(2);
                    } else {
                        errorLabel.setText("Invalid entry. Double check spelling.");
                    }
                } else if (currentAction == "g") {
                    if (investor.getPortfolioMap().containsKey(fromSubmit)) {
                        Portfolio merged = investor.getPortfolioMap().get(fromSubmit);
                        // currently broken
                    }

                } else if (currentAction == "u") {
                    if (isInteger(fromSubmit)) {
                        sm.updateStockPrice(Integer.parseInt(fromSubmit));
                        investor.updateAllPortfolios(sm);
                        loadInvestorInfoArea(true);
                    } else {
                        errorLabel.setText("Invalid entry. Integer values only.");
                    }
                }
                submitText.setText(null);
            }
        });
        submitButton.setBounds(415, 400, 125, 25);
        submitButton.setFont(textFont);
        panel.add(submitButton);

        submitLabel = new JLabel("Select an option above");
        submitLabel.setBounds(10, 380, 530, 20);
        submitLabel.setFont(miniFont);
        panel.add(submitLabel);

        submitText = new JTextField();
        submitText.setBounds(10, 400, 405, 25);
        submitText.setFont(textFont);
        panel.add(submitText);

        errorLabel = new JLabel("");
        errorLabel.setBounds(10, 430, 530, 20);
        errorLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        errorLabel.setForeground(Color.RED);
        errorLabel.setBorder(border);
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
            scrollPane = new JScrollPane(investorInfoTextArea);
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
