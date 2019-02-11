package cs3500.hw02;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a cascade pile in the game of Freecell.
 */
public class CascadePile extends AbstractPile<Card> {
  /**
   * Constructs an empty cascade pile.
   */
  public CascadePile() {
    super(new ArrayList<>());
  }

  /**
   * Constructs a cascade pile with the given list.
   *
   * @param pile the initial pile to set it to
   */
  CascadePile(List<Card> pile) {
    super(pile);
  }

  @Override
  String getCardAsString(Card card) {
    return card.toString();
  }

  @Override
  public String getPileAsString(int index) {
    return "C" + index + ":" + getPileAsStringHelper();
  }

  @Override
  public boolean isValidDestination(Card card) {
    // If the card is null return false
    if (card == null) {
      return false;
    }
    // If the pile is empty any card can be accepted
    else if (this.pile.size() == 0) {
      return true;
    }
    // otherwise check that the given card is 1 smaller and a different color than the last card
    // in the pile
    else {
      Card lastInPile = pile.get(pile.size() - 1);
      return !lastInPile.getCardType().getColor().equals(card.getCardType().getColor())
              && lastInPile.getCardValue().getValue() - 1 == card.getCardValue().getValue();
    }
  }

  @Override
  public boolean isValidSource(int index) {
    return index >= 0 && this.pile.size() - 1 == index;
  }
}
