import java.util.Objects;
import java.util.Scanner;

public class Game {

    private Deck m_deck;
    private Hand m_playerHand;
    private Hand m_computerHand;

    public Game() {
        m_deck = new Deck();
        m_playerHand = new Hand();
        m_computerHand = new Hand();
    }

    public void dealHands() {
        if (m_playerHand.getSize() > 0 && m_computerHand.getSize() > 0) {
            m_playerHand.removeAllCards();
            m_computerHand.removeAllCards();
        }
        m_playerHand.addCard(m_deck.dealCard());
        m_computerHand.addCard(m_deck.dealCard());
        m_playerHand.addCard(m_deck.dealCard());
        m_computerHand.addCard(m_deck.dealCard());
    }

    public void determineWinner() {
        System.out.println("Dealer total is " + m_computerHand.getTotal());
        System.out.println("Player total is " + m_playerHand.getTotal());
        System.out.println();

        if (m_playerHand.getTotal() > m_computerHand.getTotal()) {
            System.out.println("YOU WIN!");
        } else if (m_playerHand.getTotal() == m_computerHand.getTotal()) {
            System.out.println("IT'S A TIE!");
        } else {
            System.out.println("YOU LOSE!");
        }
    }

    public void dealerTurn() {
        System.out.println("Okay, dealer's turn");
        m_computerHand.showHiddenCard();
        System.out.println("His total was " + m_computerHand.getTotal() + "\n");

        boolean is_Running = true;

        while (is_Running) {
           if (m_computerHand.getTotal() > 21) {
               System.out.println("YOU WIN!");
               playAgain();
               is_Running = false;
           }
           if (m_computerHand.getTotal() == 21) {
               System.out.println("YOU LOSE!");
               playAgain();
               is_Running = false;
           }

           if (m_computerHand.getTotal() < 17) {
               System.out.println("Dealer chooses to hit.");
               Card card = m_deck.dealCard();
               System.out.println("He draws a " + card.toString());
               m_computerHand.addCard(card);
               System.out.println("His total is " + m_computerHand.getTotal() + "\n");
           }
           if (m_computerHand.getTotal() >= 17 && m_computerHand.getTotal() < 21) {
               System.out.println("Dealer stays\n");
               determineWinner();
               is_Running = false;
           }
        }
    }

    public void play() {
        System.out.println("Welcome to BlackJack!");
        dealHands();
        m_playerHand.showPlayerHand();
        System.out.println("Your total is " + m_playerHand.getTotal());
        System.out.println();
        m_computerHand.showDealerHand();
        boolean is_Running = true;
        Scanner scanner = new Scanner(System.in);
        String answer;
        while (is_Running) {
            if (m_playerHand.getTotal() > 21) {
                System.out.println("YOU LOSE!");
                playAgain();
                is_Running = false;
            } else if (m_playerHand.getTotal() == 21) {
                System.out.println("YOU WIN!");
                playAgain();
                is_Running = false;
            }
            System.out.println("Would you like to Hit or Stand? (Q to quit): ");
            answer = scanner.next();
            if (answer.equalsIgnoreCase("Q")) {
                System.out.println("Thanks for playing!");
                is_Running = false;
            }
            if (Objects.equals(answer, "Hit")) {
                Card card = m_deck.dealCard();
                System.out.println("You drew a " + card.toString());
                m_playerHand.addCard(card);
                System.out.println("Your total is " + m_playerHand.getTotal());
            }
            if (Objects.equals(answer, "Stand")) {
                dealerTurn();
                is_Running = false;
            }
        }
    }

    public void playAgain() {
        Scanner scanner = new Scanner(System.in);
        String answer;

        System.out.println("Would you like to play again?: ");
        answer = scanner.next();

        if (Objects.equals(answer, "Yes")) {
            m_deck.reset();
            play();
        } else if  (Objects.equals(answer, "No")) {
            System.out.println("Thanks for playing!");
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }
}
