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

    public StonksGUI() {
        panel = new JPanel();
        panel.setLayout(null);

        soundButton.setBounds(520, 430, 20, 20);
        panel.add(soundButton);
    }

    public String getOutput() {
        return output;
    }

    public JPanel getPanel() {
        return panel;
    }

    public Investor getInvestor() {
        return investor;
    }

    public StockMarket getStockMarket() {
        return sm;
    }

    public void setStonksAppRunner(StonksAppRunner toSet) {
        this.stonksAppRunner = toSet;
    }

    public void setInvestor(Investor investor) {
        this.investor = investor;
    }

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

    protected void stopBackgroundMusic() {
        clip.stop();
    }

    // code from https://stackoverflow.com/questions/4801386/how-do-i-add-an-image-to-a-jbutton
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

    protected boolean isDouble(String toCheck) {
        try {
            Double.parseDouble(toCheck);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isInteger(String toCheck) {
        try {
            Integer.parseInt(toCheck);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

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

    public abstract void initializePageComponents();

}
