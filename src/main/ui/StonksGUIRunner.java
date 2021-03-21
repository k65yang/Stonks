package ui;

import ui.GUI.HomePageGUI;
import ui.GUI.StonksGUI;

import javax.swing.*;

public class StonksGUIRunner {
    private int activePage = 0;

    public StonksGUIRunner() {
        displayActivePage();
    }

    private void displayActivePage() {
        // create the new frame
        JFrame frame = new JFrame();

        // set up contents plane
        JPanel panel = new JPanel();
        panel.setLayout(null);

        HomePageGUI homePageGUI = new HomePageGUI(panel);
        panel = homePageGUI.getPanel();


        // display the window
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700,500);
        frame.setTitle("Stonks - A Stocks Investment Simulator");
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
