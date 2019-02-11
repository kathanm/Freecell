package cs3500.hw02;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a foundation pile in the game of Freecell.
 */
public class FoundationPile extends AbstractPile<Card> {
  /**
   * Constructs an empty foundation pile.
   */
  public FoundationPile() {
    super(new ArrayList<>());
  }

  /**
   * Constructs a foundation pile with the given list.
   *
   * @param pile the initial pile to set it to
   */
  public FoundationPile(List<Card> pile) {
    super(pile);
  }

  @Override
  String getCardAsString(Card card) {
    return card.toString();
  }

  @Override
  public String getPileAsString(int index) {
    return "F" + index + ":" + getPileAsStringHelper();
  }

  @Override
  public boolean isValidDestination(Card card) {
    // If the card is null return false
    if (card == null) {
      return false;
    }
    // If the pile is empty check that the incoming card is an Ace
    else if (pile.size() == 0) {
      return card.getCardValue().getValue() == 1;
    }
    // Otherwise check that the card is the same color and one greater value than the last one
    else {
      Card lastInPile = pile.get(pile.size() - 1);
      return lastInPile.getCardValue().getValue() + 1 == card.getCardValue().getValue()
              && lastInPile.getCardType().equals(card.getCardType());
    }
  }

  @Override
  public boolean isValidSource(int index) {
    // Cards cannot be removed from the foundation pile
    return false;
  }
}
