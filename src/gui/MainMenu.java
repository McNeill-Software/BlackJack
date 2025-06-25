package gui;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainMenu implements ActionListener {

    JFrame frame = new JFrame();
    JLabel title = new JLabel();
    JButton playBtn = new JButton();

    public MainMenu() {

        //Buttons
        playBtn.setText("Play");
        playBtn.setBounds(500, 300, 200, 40);
        playBtn.setFocusable(false);
        playBtn.addActionListener(this);

        //Title
        title.setText("BlackJack");
        title.setBounds(550, 0, 200, 40);

        //Window
        frame.setTitle("Main Menu");
        frame.add(playBtn);
        frame.add(title);
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playBtn) {
            try {
                GameGUI gui =  new GameGUI();
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void main(String[] args) {
        MainMenu mainMenu = new MainMenu();
    }
}
