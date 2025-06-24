package gui;

import sound.Sound;
import model.Card;
import model.Deck;
import model.Hand;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class GameGUI extends JFrame implements ActionListener {
    private Deck deck;
    private Hand playerHand;
    private Hand dealerHand;
    private Sound cardSound;

    private JTextArea gameLog;
    private JLabel playerCardTotal, dealerCardTotal;
    private JButton hitBtn, standBtn, newGameBtn;
    private JPanel playerPanel, dealerPanel, centerWrapper, buttonPanel;
    private BackgroundImagePanel panel;

    public GameGUI() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        setTitle("BlackJack");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new BackgroundImagePanel("res/images/table/table.png");
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
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.white);
        buttonPanel.setOpaque(false);
        buttonPanel.add(hitBtn);
        buttonPanel.add(standBtn);
        buttonPanel.add(newGameBtn);
        buttonPanel.setPreferredSize(new Dimension(300, 50));
        centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        //panel.add(buttonPanel,  BorderLayout.CENTER);
        hitBtn.addActionListener(this);
        newGameBtn.addActionListener(this);
        standBtn.addActionListener(this);

        //Player
        playerPanel = new JPanel();
        playerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        playerPanel.setBackground(Color.white);
        playerPanel.setOpaque(false);
        playerPanel.setMaximumSize(new Dimension(getWidth(), 160));
        panel.add(playerPanel, BorderLayout.SOUTH);

        //Card text totals
        playerCardTotal = new JLabel();
        playerCardTotal.setFont(new Font("Monospaced", Font.PLAIN, 24));
        playerCardTotal.setForeground(Color.white);
        playerPanel.add(playerCardTotal);
        panel.add(playerCardTotal, BorderLayout.WEST);

        dealerCardTotal = new JLabel();
        dealerCardTotal.setFont(new Font("Monospaced", Font.PLAIN, 24));
        dealerCardTotal.setForeground(Color.white);
        dealerPanel.add(dealerCardTotal);
        panel.add(dealerCardTotal, BorderLayout.EAST);

        //Audio
        cardSound = new Sound("res/audio/card-sound.wav");

        setVisible(true);
        startNewGame();
    }

    private void addButton() {
        centerWrapper.add(buttonPanel);
        panel.add(centerWrapper, BorderLayout.CENTER);

        panel.revalidate();
        panel.repaint();
    }

    private void removeButton() {
        centerWrapper.remove(buttonPanel);
        panel.remove(centerWrapper);

        panel.revalidate();
        panel.repaint();
    }

    private int getPlayerCardAmount() {
        return playerHand.getTotal();
    }

    private int getDealerCardAmount() {
        return dealerHand.getTotal();
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
        String filename = "/images/cards/facedown.png";
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
        dealerCardTotal.setText("Dealer total: " + dealerHand.getTotal());
        dealerPanel.revalidate();
        dealerPanel.repaint();
    }

    private void showAllHands() {

        // Steps for showing each card with delay
        List<Runnable> steps = List.of(
                () -> {
                    playerPanel.add(createCardLabel(playerHand.getCards().get(0)));
                    cardSound.play();
                    playerPanel.revalidate();
                    playerPanel.repaint();
                },
                () -> {
                    dealerPanel.add(createCardLabel(dealerHand.getCards().get(0)));
                    cardSound.play();
                    dealerPanel.revalidate();
                    dealerPanel.repaint();
                },
                () -> {
                    playerPanel.add(createCardLabel(playerHand.getCards().get(1)));
                    cardSound.play();
                    playerPanel.revalidate();
                    playerPanel.repaint();
                },
                () -> {
                    dealerPanel.add(createHiddenCard());
                    cardSound.play();
                    dealerPanel.revalidate();
                    dealerPanel.repaint();
                }
        );

        final int delay = 500; // 500ms between steps
        final int[] step = {0};

        Timer timer = new Timer(delay, null);
        timer.addActionListener(e -> {
            if (step[0] < steps.size()) {
                steps.get(step[0]).run();
                step[0]++;
            } else {
                timer.stop(); // Stop when done
                addButton();
            }
        });
        timer.setInitialDelay(0);
        timer.start();
    }

    private void startNewGame() {
        deck.reset();
        playerHand.removeAllCards();
        dealerHand.removeAllCards();

        playerPanel.removeAll();
        dealerPanel.removeAll();
        playerPanel.revalidate();
        dealerPanel.revalidate();
        playerPanel.repaint();
        dealerPanel.repaint();

        removeButton();

        playerHand.addCard(deck.dealCard());
        dealerHand.addCard(deck.dealCard());
        playerHand.addCard(deck.dealCard());
        dealerHand.addCard(deck.dealCard());

        playerCardTotal.setText("Player Total: " + getPlayerCardAmount());
        playerCardTotal.revalidate();
        playerCardTotal.repaint();

        dealerCardTotal.setText("Dealer Total: ??");
        dealerCardTotal.revalidate();
        dealerCardTotal.repaint();

        showAllHands();
    }

    public void play() {
        playerTurn();
    }

    private void playerTurn() {
        while (true) {
            if (playerHand.getTotal() > 21) {
                System.out.println("You lost!");
                break;
            } else if (playerHand.getTotal() == 21) {
                System.out.println("You win!");
                break;
            }
        }
    }

    private void dealerTurn() {
        if (dealerHand.getTotal() < 17) {
            Card card = deck.dealCard();
            dealerHand.addCard(card);
            dealerPanel.add(createCardLabel(card));
            dealerCardTotal.setText("Dealer Total: " + getDealerCardAmount());
            dealerCardTotal.revalidate();
            dealerCardTotal.repaint();
            cardSound.play();
            dealerPanel.revalidate();
            dealerPanel.repaint();
        } else if (dealerHand.getTotal() < 21 && dealerHand.getTotal() >= 17) {
            findWinner();
        } else if (dealerHand.getTotal() > 21) {
            System.out.println("You win!");
        }
    }

    private void findWinner() {
        if (playerHand.getTotal() < 21 && playerHand.getTotal() > dealerHand.getTotal()) {
            System.out.println("You won!");
        } else if (playerHand.getTotal() == dealerHand.getTotal()) {
            System.out.println("It's a tie!");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == hitBtn) {
            System.out.println("You hit!");
            Card card = deck.dealCard();
            playerHand.addCard(card);
            playerPanel.add(createCardLabel(card));
            playerCardTotal.setText("Player Total: " + getPlayerCardAmount());
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
