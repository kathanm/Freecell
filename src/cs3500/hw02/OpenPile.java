package cs3500.hw02;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an open pile in the game of Freecell.
 */
public class OpenPile extends AbstractPile<Card> {
  /**
   * Constructs an empty open pile.
   */
  public OpenPile() {
    super(new ArrayList<>());
  }

  /**
   * Constructs a cascade pile with the given list.
   *
   * @param pile the initial pile to set it to
   */
  public OpenPile(List<Card> pile) {
    super(pile);
  }

  @Override
  String getCardAsString(Card card) {
    return card.toString();
  }

  @Override
  public String getPileAsString(int index) {
    return "O" + index + ":" + getPileAsStringHelper();
  }

  @Override
  public boolean isValidDestination(Card card) {
    // If the card isn't null and the pile is empty it can accept a card
    return card != null && this.pile.size() == 0;
  }

  @Override
  public boolean isValidSource(int index) {
    // If the pile has a card in it and the index is correct then it can give a card
    return this.pile.size() == 1 && index == 0;
  }
}