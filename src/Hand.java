import java.util.ArrayList;

public class Hand {

    private ArrayList<Card> m_hand;

    public Hand() {
        m_hand = new ArrayList<>(2);
    }

    public int getSize() {
        return m_hand.size();
    }

    public void addCard(Card card) {
        m_hand.add(card);
    }

    public void removeAllCards() {
        m_hand.clear();
    }

    public void showPlayerHand() {
        System.out.println("Player Hand: ");
        for (int i = 0; i < m_hand.size(); i++) {
            System.out.println(i+1 + ": " + m_hand.get(i).toString());
        }
    }

    public void showDealerHand() {
        System.out.println("Dealer Hand: ");
        System.out.println(m_hand.getFirst().toString());
        System.out.println("Dealers second card is hidden");
    }

    public void showHiddenCard() {
        System.out.println("His hidden card was " + m_hand.getLast().toString());
    }

    public int getTotal() {
        int total = 0;
        for (Card card : m_hand) {
            total += card.getValue();
        }
        return total;
    }
}
