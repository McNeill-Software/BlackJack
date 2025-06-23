import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class GameGUI extends JFrame {
    private Deck deck;
    private Hand playerHand;
    private Hand dealerHand;

    private JTextArea gameLog;
    private JLabel playerCardTotal, dealerCardTotal;
    private JButton hitBtn, standBtn, newGameBtn;
    private JPanel playerPanel, dealerPanel;

    public GameGUI() {
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
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(dealerPanel);


        //Button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(hitBtn);
        buttonPanel.add(standBtn);
        buttonPanel.add(newGameBtn);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buttonPanel);

        //Player
        playerPanel = new JPanel();
        playerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        playerPanel.setOpaque(false);
        playerPanel.setMaximumSize(new Dimension(getWidth(), 160));
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(playerPanel);

        //Card totals
        playerCardTotal = new JLabel();
        playerCardTotal.setText("adsd");
        playerCardTotal.setFont(new Font("Monospaced", Font.PLAIN, 24));
        add(playerCardTotal, BorderLayout.NORTH);

        dealerCardTotal = new JLabel();
        dealerCardTotal.setText("adsd");
        dealerCardTotal.setFont(new Font("Monospaced", Font.PLAIN, 24));
        dealerCardTotal.setHorizontalAlignment(JLabel.CENTER);
        add(dealerCardTotal, BorderLayout.EAST);

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
        for (Card card : dealerHand.getCards()) {
            dealerPanel.add(createCardLabel(card));
        }
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

    }

}
