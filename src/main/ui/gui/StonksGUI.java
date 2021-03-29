package ui.gui;

import model.Investor;
import model.StockMarket;
import persistence.JsonWriter;
import ui.StonksAppRunner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

// Abstract superclass for all the pages (different layouts) used by the Stonks GUI
public abstract class StonksGUI {
    protected String output;
    protected JPanel panel;
    protected JButton soundButton = new JButton();
    protected Investor investor;
    protected StockMarket sm;
    protected StonksAppRunner stonksAppRunner;
    protected Clip clip;
    protected final Font titleFont = new Font("SansSerif", Font.BOLD, 36);
    protected final Font headingFont = new Font("SansSerif", Font.BOLD, 24);
    protected final Font textFont = new Font("SansSerif", Font.BOLD, 18);
    protected final Font miniFont = new Font("SansSerif", Font.BOLD, 16);
    protected final Font miniPrintoutFont = new Font("DialogInput", Font.BOLD, 14);
    protected final Border border = BorderFactory.createLineBorder(Color.BLACK, 1);

    // MODIFIES: this
    // EFFECTS: Creates a StonksGUI object
    public StonksGUI() {
        panel = new JPanel();
        panel.setLayout(null);

        soundButton.setBounds(520, 430, 20, 20);
        panel.add(soundButton);
    }

    // EFFECTS: returns the output field as a string
    public String getOutput() {
        return output;
    }

    // EFFECTS: returns the JPanel object, panel
    public JPanel getPanel() {
        return panel;
    }

    // EFFECTS: returns the stored investor object
    public Investor getInvestor() {
        return investor;
    }

    // EFFECTS: returns the stored stock market object
    public StockMarket getStockMarket() {
        return sm;
    }

    // REQUIRES: toSet cannot be null
    // MODIFIES: this
    // EFFECTS: sets the StonksAppRunner field to the given StonksAppRunner toSet
    public void setStonksAppRunner(StonksAppRunner toSet) {
        this.stonksAppRunner = toSet;
    }

    // REQUIRES: investor cannot be null
    // MODIFIES: this
    // EFFECTS: sets the investor field to the given investor
    public void setInvestor(Investor investor) {
        this.investor = investor;
    }

    // EFFECTS: starts playing background music on a loop
    protected void playBackgroundMusic() {
        // Music from https://www.storyblocks.com/audio/stock/vintage-background-jazz-atmosphere-sb3gvmdkvkchko6xk.html
        // Code from https://stackoverflow.com/tags/javasound/info
        try {
            String musicFile = "data/music/background_music.wav";
            clip = AudioSystem.getClip();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(musicFile));
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception exception) {
            System.out.println("No music");
            exception.printStackTrace();
        }

    }

    // EFFECTS: stops playing background music
    protected void stopBackgroundMusic() {
        clip.stop();
    }

    // MODIFIES: this
    // EFFECTS: adds a sound button on the lower right corner of the screen
    //          if background music is playing, the image will be a mute icon and upon click
    //          will stop the background music from playing, then change the icon to a volume icon
    //          if background music is not playing, the image will be a volume button and upon click
    //          will start playing the background music, then change the icon to a mute icon
    protected void loadButtonSound(boolean showMute) {
        for (ActionListener al : soundButton.getActionListeners()) {
            soundButton.removeActionListener(al);
        }

        if (showMute) {
            soundButton.setIcon(new ImageIcon("data/images/mute_resized.png"));
            soundButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    stopBackgroundMusic();
                    loadButtonSound(false);
                }
            });
        } else {
            soundButton.setIcon(new ImageIcon("data/images/volume_resized.png"));
            soundButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    playBackgroundMusic();
                    loadButtonSound(true);
                }
            });
        }

    }

    // EFFECTS: returns a boolean indicating if toCheck can be represented as a double
    protected boolean isDouble(String toCheck) {
        try {
            Double.parseDouble(toCheck);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // EFFECTS: returns a boolean indicated in toCheck can be represented as an integer
    protected boolean isInteger(String toCheck) {
        try {
            Integer.parseInt(toCheck);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // EFFECTS: saves the current state of investor and stock market to individual json files
    protected void saveFile(String saveName) throws FileNotFoundException {
        String saveInvestorName = "Investor-" + saveName;
        String saveStockMarketName = "StockMarket-" + saveName;

        String investorDestination = "./data/" + saveInvestorName + ".json";
        String stockMarketDestination = "./data/" + saveStockMarketName + ".json";

        JsonWriter writerInvestor = new JsonWriter(investorDestination);
        JsonWriter writerStockMarket = new JsonWriter(stockMarketDestination);

        writerInvestor.open();
        writerInvestor.write(investor);
        writerInvestor.close();

        writerStockMarket.open();
        writerStockMarket.write(sm);
        writerStockMarket.close();
    }

    // MODIFIES: this
    // EFFECTS: loads the page components onto the JPanel
    public abstract void initializePageComponents();

}
