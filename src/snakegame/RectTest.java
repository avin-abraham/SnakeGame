//WE NEED TO CLEAN THE CODE UP AND MAKE IT MORE READABLE!!!!!

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.*;
import static java.awt.event.KeyEvent.*;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author 11768
 */
public class RectTest {
    
    static Font customFont;
    
        public static void fontLoader(){
            try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("customFont.ttf")).deriveFont(25f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("customFont.ttf")));
        } catch (IOException | FontFormatException e) {
            //Handle exception
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        JFrame frame = new JFrame("rektangles");
        RectPanel rectPanel = new RectPanel();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(rectPanel);

        JLabel statusLabel = new JLabel("BERNIE: " + Integer.toString((int) RectPanel.bernie.getSSize()));
        fontLoader();
        statusLabel.setFont(customFont);
        statusLabel.setForeground(Color.white);
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        rectPanel.add(statusLabel);

        frame.pack();

        frame.setVisible(true);
        
        int score = 0;
        int score1 = 0;
        int score2 = 0;

        // NEW IN ANIMATION VERSION:
        while (true) {
            rectPanel.update();
            if(!RectPanel.bernie.isLiving()){
                 statusLabel.setText("BERNIE: " + Integer.toString(score) + " BERNINATOR: " + Integer.toString(score1) + " ROBOBERNIE: " + Integer.toString(score2));
            }
            else{
                score = (int)RectPanel.bernie.getSSize()-1;
                 statusLabel.setText("BERNIE: " + Integer.toString(score) + " BERNINATOR: " + Integer.toString(score1) + " ROBOBERNIE: " + Integer.toString(score2));
            }
            
            if(!RectPanel.berninator.isLiving()){
                 statusLabel.setText("BERNIE: " + Integer.toString(score) + " BERNINATOR: " + Integer.toString(score1) + " ROBOBERNIE: " + Integer.toString(score2));
            }
            else{
                score1 = (int)RectPanel.berninator.getSSize()-1;
                 statusLabel.setText("BERNIE: " + Integer.toString(score) + " BERNINATOR: " + Integer.toString(score1) + " ROBOBERNIE: " + Integer.toString(score2));
            }
            
            if(!RectPanel.robobernie.isLiving()){
                statusLabel.setText("BERNIE: " + Integer.toString(score) + " BERNINATOR: " + Integer.toString(score1) + " ROBOBERNIE: " + Integer.toString(score2));
            }
            else{
                score2 = (int)RectPanel.robobernie.getSSize()-1;
                 statusLabel.setText("BERNIE: " + Integer.toString(score) + " BERNINATOR: " + Integer.toString(score1) + " ROBOBERNIE: " + Integer.toString(score2));
            }
            
            try {
                Thread.sleep(35);
            } catch (Exception e) {
                e.printStackTrace();
            }
            frame.repaint();
        }

    }

    public static int random_number(int low, int high) {
        double rand = Math.random(); //generates a random number
        int rand2 = (int) (rand * 100000); //casts the random number as int
        int interval = high - low;//interval in which to put the number ie 1-100
        rand2 = rand2 % interval;//puts the number into the interval
        rand2 = rand2 + low;//acertains that the number is above the minimum
        int randNum = rand2;//assigns the random number's value
        return randNum;//returns the random number's value
    }

}
