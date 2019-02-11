package cs3500.hw02;

import java.util.List;

/**
 * Abstract base class for implementations of Pile parametrized over Card class K.
 *
 * @param <K> class of the implementation of a card
 */
abstract class AbstractPile<K> implements Pile<K> {
  // Changed from default to protected so subclasses from other packages can use attribute
  protected List<K> pile;

  /**
   * Constructs a pile with the given list.
   *
   * @param pile the initial pile to set it to
   */
  AbstractPile(List<K> pile) {
    this.pile = pile;
  }

  // Formats the pile as a string for the getGameState in model ignoring the beginning text
  public String getPileAsStringHelper() {
    if (pile.size() == 0) {
      return "";
    }
    StringBuilder bldr = new StringBuilder(" ");
    for (int i = 0; i < pile.size() - 1; i++) {
      K current = pile.get(i);
      bldr.append(getCardAsString(current));
      bldr.append(", ");
    }
    K last = pile.get(pile.size() - 1);
    bldr.append(getCardAsString(last));
    return bldr.toString();
  }

  // Returns the card as a string value using its toString() method
  abstract String getCardAsString(K card);

  @Override
  public List<K> getPile() {
    return pile;
  }

  @Override
  public void addCard(K card) throws IllegalArgumentException {
    if (!this.isValidDestination(card)) {
      throw new IllegalArgumentException("Cannot add card to this pile. Not a valid destination.");
    } else {
      pile.add(card);
    }
  }

  @Override
  public void removeCard(int index) throws IllegalArgumentException {
    if (!this.isValidSource(index)) {
      throw new IllegalArgumentException("Cannot remove card(s) from pile for given index.");
    } else {
      pile = pile.subList(0, index);
    }
  }
}