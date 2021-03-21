package ui.GUI;

import model.Investor;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;

public class HomePageGUI extends StonksGUI {
    private String name;

    private JPanel panel;
    private JTextField userText;
    private JTextField fundsText;
    private JTextField loadText;
    private JLabel creationErrorLabel;
    private JLabel loadErrorLabel;

    public HomePageGUI(JPanel panel) {
        this.panel = panel;
        initializePageComponents();
    }

    public JPanel getPanel() {
        return panel;
    }

    public JPanel initializePageComponents() {
        Border border = BorderFactory.createLineBorder(Color.BLUE, 1);

        JLabel homePageHeading1 = new JLabel("To begin, create a new investor profile");
        homePageHeading1.setBounds(10, 20, 450, 30);
        homePageHeading1.setFont(headingFont);
        homePageHeading1.setBorder(border);
        panel.add(homePageHeading1);

        JLabel userLabel = new JLabel("Enter investor name:");
        userLabel.setBounds(10, 60, 190, 25);
        userLabel.setFont(textFont);
        userLabel.setBorder(border);
        panel.add(userLabel);

        userText = new JTextField();
        userText.setBounds(210, 60, 165, 25);
        userText.setBorder(border);
        name = userText.getText();
        panel.add(userText);

        JLabel fundsLabel = new JLabel("Enter starting funds:");
        fundsLabel.setBounds(10, 95, 185, 25);
        fundsLabel.setFont(textFont);
        fundsLabel.setBorder(border);
        panel.add(fundsLabel);

        fundsText = new JTextField();
        fundsText.setBounds(210, 95, 165, 25);
        fundsText.setBorder(border);
        String fundsAsString = fundsText.getText();
        panel.add(fundsText);

        JButton createInvestorButton = new JButton(new AbstractAction("Create Investor!") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("here");
                if (!isDouble(fundsAsString)) {
                    creationErrorLabel.setText("ERROR: funds is not a string");
                } else {
                    investor = new Investor(name, Double.parseDouble(fundsAsString));
                }
            }
        });
        createInvestorButton.setBounds(10, 125, 250, 25);
        createInvestorButton.setFont(textFont);
        panel.add(createInvestorButton);

        creationErrorLabel = new JLabel("");
        creationErrorLabel.setBounds(10, 160, 300, 25);
        creationErrorLabel.setFont(textFont);
        creationErrorLabel.setBorder(border);
        panel.add(creationErrorLabel);

        JLabel homePageHeading2 = new JLabel("Already have a profile?");
        homePageHeading2.setBounds(10, 230, 270, 30);
        homePageHeading2.setFont(headingFont);
        homePageHeading2.setBorder(border);
        panel.add(homePageHeading2);

        JLabel loadLabel = new JLabel("Enter name of file to load:");
        loadLabel.setBounds(10, 270, 230, 25);
        loadLabel.setFont(textFont);
        loadLabel.setBorder(border);
        panel.add(loadLabel);

        loadText = new JTextField();
        loadText.setBounds(250, 270, 165, 25);
        loadText.setBorder(border);
        panel.add(loadText);

        JButton loadButton = new JButton("Load profile!");
        loadButton.setBounds(10, 305, 250, 25);
        loadButton.setFont(textFont);
        panel.add(loadButton);

        loadErrorLabel = new JLabel("");
        loadErrorLabel.setBounds(10, 340, 300, 25);
        loadErrorLabel.setFont(textFont);
        loadErrorLabel.setBorder(border);
        panel.add(loadErrorLabel);

        return panel;
    }

    private boolean isDouble(String toCheck) {
        try {
            Double.parseDouble(toCheck);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
