public class Card {

    private final String m_rank;
    private final String m_suit;
    private final int m_value;

    public Card(String rank, String suit, int value) {
        m_rank = rank;
        m_suit = suit;
        m_value = value;
    }

    public String getRank() {
        return m_rank;
    }

    public String getSuit() {
        return m_suit;
    }

    public int getValue() {
        return m_value;
    }

    @Override
    public String toString() {
        return m_rank + " of " +  m_suit;
    }
}
