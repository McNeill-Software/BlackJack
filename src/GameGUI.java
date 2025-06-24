import sound.Sound;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;

public class GameGUI extends JFrame implements ActionListener {
    private Deck deck;
    private Hand playerHand;
    private Hand dealerHand;
    private Sound cardSound;

    private JTextArea gameLog;
    private JLabel playerCardTotal, dealerCardTotal;
    private JButton hitBtn, standBtn, newGameBtn;
    private JPanel playerPanel, dealerPanel;

    public GameGUI() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        setTitle("BlackJack");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BackgroundImagePanel panel = new BackgroundImagePanel("src/res/images/table/table.png");
        panel.setLayout(new BorderLayout());
        setContentPane(panel);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);
        panel.add(mainPanel, BorderLayout.CENTER);

        deck = new Deck();
        playerHand = new Hand();
        dealerHand = new Hand();

        gameLog = new JTextArea();
        gameLog.setEditable(false);
        gameLog.setFont(new Font("Monospaced", Font.PLAIN, 14));

        //Buttons
        hitBtn = new JButton("Hit");
        standBtn = new JButton("Stand");
        newGameBtn = new JButton("New Game");

        //Dealer
        dealerPanel = new JPanel();
        dealerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        dealerPanel.setOpaque(false);
        dealerPanel.setMaximumSize(new Dimension(getWidth(), 160));
        panel.add(dealerPanel, BorderLayout.NORTH);

        //Button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.white);
        buttonPanel.setOpaque(false);
        buttonPanel.add(hitBtn);
        buttonPanel.add(standBtn);
        buttonPanel.add(newGameBtn);
        buttonPanel.setPreferredSize(new Dimension(300, 50));
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(buttonPanel); // This centers it
        panel.add(centerWrapper, BorderLayout.CENTER);

        //panel.add(buttonPanel,  BorderLayout.CENTER);
        hitBtn.addActionListener(this);
        newGameBtn.addActionListener(this);
        standBtn.addActionListener(this);

        //Player
        playerPanel = new JPanel();
        playerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        playerPanel.setBackground(Color.white);
        //playerPanel.setOpaque(false);
        playerPanel.setMaximumSize(new Dimension(getWidth(), 160));
        panel.add(playerPanel, BorderLayout.SOUTH);

        //Card text totals
        playerCardTotal = new JLabel();
        playerCardTotal.setText("adsd");
        playerCardTotal.setFont(new Font("Monospaced", Font.PLAIN, 24));
        playerCardTotal.setBounds(0, 500, 100, 500);
        //panel.add(playerCardTotal);

        dealerCardTotal = new JLabel();
        dealerCardTotal.setText("adsd");
        dealerCardTotal.setFont(new Font("Monospaced", Font.PLAIN, 24));
        dealerCardTotal.setHorizontalAlignment(JLabel.CENTER);
        //add(dealerCardTotal, BorderLayout.EAST);

        //Audio
        cardSound = new Sound("src/res/audio/card-sound.wav");

        setVisible(true);
        startNewGame();
    }

    private JLabel createCardLabel(Card card) {
        String filename = card.getImageFileName();
        System.out.println("Loading image: " + filename);
        URL resource = getClass().getResource(filename);
        if (resource == null) {
            System.err.println("ERROR: Image not found at " + filename);
            return new JLabel("Image not found"); // fallback
        }
        ImageIcon icon = new ImageIcon(resource);
        Image scaled = icon.getImage().getScaledInstance(80, 120, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(scaled));
    }

    private JLabel createHiddenCard() {
        String filename = "res/images/cards/facedown.png";
        System.out.println("Loading image: " + filename);
        URL resource = getClass().getResource(filename);
        if (resource == null) {
            System.err.println("ERROR: Image not found at " + filename);
            return new JLabel("Image not found");
        }
        ImageIcon icon = new ImageIcon(resource);
        Image scaled = icon.getImage().getScaledInstance(80, 120, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(scaled));
    }

    private void revealHiddenCard() {
        dealerPanel.remove(1);
        Card card = dealerHand.getCards().getLast();
        dealerPanel.add(createCardLabel(card));
        dealerPanel.revalidate();
        dealerPanel.repaint();
    }

    private void showPlayerHand() {
        playerPanel.removeAll();
        for (Card card : playerHand.getCards()) {
            playerPanel.add(createCardLabel(card));
        }
        playerPanel.revalidate();
        playerPanel.repaint();
    }

    private void showDealerHand() {
        dealerPanel.removeAll();
        Card card = dealerHand.getCards().getFirst();
        dealerPanel.add(createCardLabel(card));
        dealerPanel.add(createHiddenCard());
        dealerPanel.revalidate();
        dealerPanel.repaint();
    }

    private void startNewGame() {
        deck.shuffle();
        playerHand.removeAllCards();
        dealerHand.removeAllCards();

        playerHand.addCard(deck.dealCard());
        dealerHand.addCard(deck.dealCard());
        playerHand.addCard(deck.dealCard());
        dealerHand.addCard(deck.dealCard());
        showPlayerHand();
        showDealerHand();
    }

    public void play() {
        while (true) {
            if (playerHand.getTotal() > 21) {
                System.out.println("You lost!");
                break;
            }
        }
    }

    private void dealerTurn() {
        Card card = deck.dealCard();
        dealerHand.addCard(card);
        dealerPanel.add(createCardLabel(card));
        cardSound.play();
        dealerPanel.revalidate();
        dealerPanel.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == hitBtn) {
            System.out.println("You hit!");
            Card card = deck.dealCard();
            playerHand.addCard(card);
            playerPanel.add(createCardLabel(card));
            cardSound.play();
            playerPanel.revalidate();
            playerPanel.repaint();
        }
        if (e.getSource() == newGameBtn) {
            startNewGame();
        }
        if (e.getSource() == standBtn) {
            revealHiddenCard();
            dealerTurn();
        }
    }
}
