package ui.gui;

import model.Investor;
import model.StockMarket;
import persistence.JsonReader;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

public class HomePageGUI extends StonksGUI {
    private String name;

    private JTextField userText;
    private JTextField fundsText;
    private JTextField loadText;
    private JLabel creationErrorLabel;
    private JLabel loadErrorLabel;

    public HomePageGUI() {
        super();
        initializePageComponents();
        playBackgroundMusic();
    }


    public void initializePageComponents() {
        loadLabels();
        loadTextFields();
        loadButtons();
    }

    private void loadButtons() {
        loadButtonInvestor();
        loadButtonLoad();
        loadButtonSound(true);
    }

    private void loadButtonLoad() {
        JButton loadButton = new JButton(new AbstractAction("Load Profile!") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = loadText.getText();
                String investorDestination = "./data/Investor-" + fileName + ".json";
                String stockMarketDestination = "./data/StockMarket-" + fileName + ".json";

                try {
                    JsonReader readerInvestor = new JsonReader("investor", investorDestination);
                    JsonReader readerStockMarket = new JsonReader("stockmarket", stockMarketDestination);

                    investor = (Investor) readerInvestor.read();
                    sm = (StockMarket) readerStockMarket.read();
                    stonksAppRunner.displayActivePage(1);
                } catch (Exception exception) {
                    loadErrorLabel.setText("An error has occurred. Please retry.");
                }
            }
        });
        loadButton.setBounds(10, 355, 250, 25);
        loadButton.setFont(textFont);
        panel.add(loadButton);
    }

    private void loadButtonInvestor() {
        JButton createInvestorButton = new JButton(new AbstractAction("Create Investor!") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fundsAsString = fundsText.getText();
                if (!isDouble(fundsAsString)) {
                    creationErrorLabel.setText("ERROR: funds is not a double.");
                } else {
                    stopBackgroundMusic();
                    name = userText.getText();
                    investor = new Investor(name, Double.parseDouble(fundsAsString));
                    sm = new StockMarket();
                    stonksAppRunner.displayActivePage(1);
                }
            }
        });
        createInvestorButton.setBounds(10, 180, 250, 25);
        createInvestorButton.setFont(textFont);
        panel.add(createInvestorButton);
    }

    private void loadLabels() {
        loadLabelsHeadings();
        loadLabelsDescriptors();
        loadLabelsErrorLabels();
    }

    private void loadLabelsErrorLabels() {
        creationErrorLabel = new JLabel("");
        creationErrorLabel.setBounds(10, 210, 300, 20);
        creationErrorLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        creationErrorLabel.setForeground(Color.RED);
        panel.add(creationErrorLabel);

        loadErrorLabel = new JLabel("");
        loadErrorLabel.setBounds(10, 390, 300, 20);
        loadErrorLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        loadErrorLabel.setForeground(Color.RED);
        panel.add(loadErrorLabel);
    }

    private void loadLabelsDescriptors() {
        JLabel userLabel = new JLabel("Enter investor name:");
        userLabel.setBounds(10, 110, 190, 25);
        userLabel.setFont(textFont);
        panel.add(userLabel);

        JLabel fundsLabel = new JLabel("Enter starting funds:");
        fundsLabel.setBounds(10, 145, 185, 25);
        fundsLabel.setFont(textFont);
        panel.add(fundsLabel);

        JLabel loadLabel = new JLabel("Enter name of file to load:");
        loadLabel.setBounds(10, 320, 230, 25);
        loadLabel.setFont(textFont);
        panel.add(loadLabel);
    }

    private void loadLabelsHeadings() {
        JLabel title = new JLabel("STONKS!", SwingConstants.CENTER);
        title.setBounds(10, 20, 530, 40);
        title.setFont(titleFont);
        panel.add(title);

        JLabel homePageHeading1 = new JLabel("To begin, create a new investor profile");
        homePageHeading1.setBounds(10, 70, 450, 30);
        homePageHeading1.setFont(headingFont);
        panel.add(homePageHeading1);

        JLabel homePageHeading2 = new JLabel("Already have a profile?");
        homePageHeading2.setBounds(10, 280, 270, 30);
        homePageHeading2.setFont(headingFont);
        panel.add(homePageHeading2);
    }

    private void loadTextFields() {
        userText = new JTextField();
        userText.setBounds(210, 110, 165, 25);
        userText.setFont(textFont);
        panel.add(userText);

        fundsText = new JTextField();
        fundsText.setBounds(210, 145, 165, 25);
        fundsText.setFont(textFont);
        panel.add(fundsText);

        loadText = new JTextField();
        loadText.setBounds(250, 320, 165, 25);
        loadText.setFont(textFont);
        panel.add(loadText);
    }
}
