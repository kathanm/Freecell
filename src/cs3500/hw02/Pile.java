package cs3500.hw02;

import java.util.List;

/**
 * This is a public interface for the pile type. It represents a generic pile in a game of Freecell
 * and specifies methods which all of cascade, foundation, and open piles must have. It is
 * parametrized over the card type.
 */
public interface Pile<K> {
  /**
   * Gives the pile as a list of cards, if the pile is empty it gives an empty list.
   *
   * @return the pile as a list of cards
   */
  List<K> getPile();

  /**
   * Adds a given card to the end of the pile.
   *
   * @param card the card to add to the pile
   * @throws IllegalArgumentException if the card cannot be added to the pile or is null
   */
  void addCard(K card) throws IllegalArgumentException;

  /**
   * Removes a given card(s) from the end of the pile.
   *
   * @param index the starting index of the card(s) to remove, inclusive of the index
   * @throws IllegalArgumentException if the index is not a valid index for the given implementation
   */
  void removeCard(int index) throws IllegalArgumentException;

  /**
   * Determines if a given card is able to be added to this pile.
   *
   * @param card the card to add to the pile
   * @return true if it can, false if it cannot or if the card is null
   */
  boolean isValidDestination(K card);

  /**
   * Determines if the card at the given index can be moved to another pile.
   *
   * @param index the index of the card in the pile to be moved
   * @return true if the card can be moved, false otherwise
   */
  boolean isValidSource(int index);

  /**
   * Formats the pile as a string.
   * e.g. An empty open pile when called with index = 1 will return "O1:"
   * A cascade pile with ace of spade called with index = 4 will return "C$: A♠"
   *
   * @param index represents the index of the pile in relation to the other piles
   * @return A string listing out the pile as a game state
   */
  String getPileAsString(int index);
}
