package cs3500.hw02;

/**
 * Represents a standard playing card.
 */
public class Card {
  private CardType type;
  private CardValue value;

  /**
   * Constructs a {@code Card} object.
   *
   * @param type  The suite of the card
   * @param value The value of the card
   */
  public Card(CardType type, CardValue value) {
    this.type = type;
    this.value = value;
  }

  /**
   * Get the suite of the card.
   *
   * @return A CardType enum value denoting the type of the card
   */
  public CardType getCardType() {
    return this.type;
  }

  /**
   * Get the value of the card.
   *
   * @return A CardValue enum value denoting the value of the card
   */
  public CardValue getCardValue() {
    return this.value;
  }

  /**
   * Checks if another card is the same as this card.
   *
   * @param o The object to compare with
   * @return True if the object is a card and has the same suite
   *         and value as this card, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Card)) {
      return false;
    } else {
      Card other = (Card) o;
      return this.getCardType() == other.getCardType()
              && this.getCardValue() == other.getCardValue();
    }
  }

  /**
   * Override the hash code function to ensure two equal cards have the same code.
   *
   * @return an integer hashcode
   */
  @Override
  public int hashCode() {
    return (type.toString() + value.toString()).hashCode();
  }

  @Override
  public String toString() {
    return value.getName() + type.getIcon();
  }
}
