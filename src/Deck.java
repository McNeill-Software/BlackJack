import java.util.ArrayList;
import java.util.Collections;

public class Deck {

    private ArrayList<Card> m_deck;

    public Deck() {
        initialiseDeck();
    }

    private void initialiseDeck() {
        String[] ranks = {"2",  "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
        String[] suits = {"Clubs", "Diamonds", "Hearts", "Spades"};
        int[] values = {2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11};

        int size = ranks.length * suits.length;
        m_deck = new ArrayList<>(size);

        for (int i = 0; i < ranks.length; i++) {
            for (String suit : suits) {
                m_deck.add(new Card(ranks[i], suit, values[i]));
            }
        }
        shuffle();
    }

    public void reset() {
        initialiseDeck();
    }

    public void shuffle() {
        Collections.shuffle(m_deck);
    }

    public void showDeck() {
        for (Card card : m_deck) {
            System.out.println(card);
        }
    }

    public Card dealCard() {
        return m_deck.removeFirst();
    }
}
