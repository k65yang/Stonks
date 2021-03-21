package ui.GUI;

import model.Investor;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public abstract class StonksGUI {
    protected Investor investor;
    protected final Font titleFont = new Font("SansSerif", Font.BOLD, 36);
    protected final Font headingFont = new Font("SansSerif", Font.BOLD, 24);
    protected final Font textFont = new Font("SansSerif", Font.BOLD, 18);

    public StonksGUI() {
    }

    private void displayActivePage(int activePage) {
        // create the new frame
        JFrame frame = new JFrame();

        JButton createProfileButton = new JButton("Create investor profile");

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        panel.setLayout(new GridLayout(0,2));
        panel.add(createProfileButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        frame.setTitle("Stonks - A Stocks Investment Simulator");
        //frame.pack();
        frame.setVisible(true);

    }

//    private void displayActivePageTest(int activePage) {
//        // create the new frame
//        JFrame frame = new JFrame();
//
//        // set up contents plane
//        JPanel panel = new JPanel();
//        panel.setLayout(null);
//
//        StonksGUI homePageGUI = new HomePageGUI(panel);
//        panel = homePageGUI.getPanel();
//
//
//        // display the window
//        frame.add(panel);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(700,500);
//        frame.setTitle("Stonks - A Stocks Investment Simulator");
//        frame.setResizable(false);
//        frame.setVisible(true);
//    }

    public abstract JPanel initializePageComponents();

}
