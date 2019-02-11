package cs3500.hw02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a model of the Freecell game. This class performs operations such as starting a game,
 * returning the game state as a string, and making a move.
 */
public class FreecellModel implements FreecellOperations<Card> {
  protected final List<Pile<Card>> cascades;
  protected final List<Pile<Card>> opens;
  protected final List<Pile<Card>> foundations;

  /**
   * Constructor for the Freecell model.
   */
  public FreecellModel() {
    this.cascades = new ArrayList<>();
    this.opens = new ArrayList<>();
    this.foundations = new ArrayList<>();
  }

  @Override
  public List<Card> getDeck() {
    List<Card> deck = new ArrayList<>();
    for (int i = 0; i < 13; i++) {
      CardValue value = CardValue.values()[i];
      for (int j = 0; j < 4; j++) {
        CardType type = CardType.values()[j];
        deck.add(new Card(type, value));
      }
    }
    return deck;
  }

  private boolean isValidDeck(List<Card> deck) {
    // Check the deck is not null
    if (deck == null) {
      return false;
    }
    // Check that the deck has exactly 52 cards
    if (deck.size() != 52) {
      return false;
    }
    // Check that there are 52 distinct and valid cards in the deck
    for (int i = 0; i < 4; i++) {
      CardType type = CardType.values()[i];
      for (int j = 0; j < 13; j++) {
        CardValue value = CardValue.values()[j];
        Card current = new Card(type, value);
        if (!deck.contains(current)) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public void startGame(List<Card> deck, int numCascadePiles, int numOpenPiles, boolean shuffle)
          throws IllegalArgumentException {
    if (!isValidDeck(deck)) {
      throw new IllegalArgumentException("Deck is not valid. Cannot start game.");
    }
    if (numCascadePiles < 4) {
      throw new IllegalArgumentException("There must be at least 4 cascade piles.");
    }
    if (numOpenPiles < 1) {
      throw new IllegalArgumentException("There must be at least 1 open pile.");
    }
    foundations.clear();
    cascades.clear();
    opens.clear();
    for (int i = 0; i < 4; i++) {
      foundations.add(new FoundationPile());
    }
    for (int i = 0; i < numCascadePiles; i++) {
      cascades.add(new CascadePile());
    }
    for (int i = 0; i < numOpenPiles; i++) {
      opens.add(new OpenPile());
    }
    if (shuffle) {
      Collections.shuffle(deck);
    }
    int counter = 0;
    for (Card c : deck) {
      cascades.get(counter % cascades.size()).getPile().add(c);
      counter++;
    }
  }

  /**
   * In this implementation of Freecell, moving a card from a foundation pile will throw an error.
   *
   * @param source         the type of the source pile see @link{PileType}
   * @param pileNumber     the pile number of the given type, starting at 0
   * @param cardIndex      the index of the card to be moved from the source
   *                       pile, starting at 0
   * @param destination    the type of the destination pile (see
   * @param destPileNumber the pile number of the given type, starting at 0
   * @throws IllegalArgumentException if move is not possible
   * @throws IllegalStateException    if game has not started yet
   */
  @Override
  public void move(PileType source, int pileNumber, int cardIndex, PileType destination,
                   int destPileNumber) throws IllegalArgumentException, IllegalStateException {
    if (foundations.size() != 4) {
      throw new IllegalStateException("Cannot make a move until game has started");
    }
    Map<PileType, List<Pile<Card>>> pileMap = new HashMap<>();
    pileMap.put(PileType.CASCADE, cascades);
    pileMap.put(PileType.FOUNDATION, foundations);
    pileMap.put(PileType.OPEN, opens);

    if (pileMap.get(source).get(pileNumber).isValidSource(cardIndex)) {
      Card current = pileMap.get(source).get(pileNumber).getPile().get(cardIndex);
      if (pileMap.get(destination).get(destPileNumber).isValidDestination(current)) {
        pileMap.get(source).get(pileNumber).removeCard(cardIndex);
        pileMap.get(destination).get(destPileNumber).addCard(current);
      } else {
        throw new IllegalArgumentException("Card cannot be moved to that destination");
      }
    } else {
      throw new IllegalArgumentException("Card cannot be moved from that source");
    }
  }

  @Override
  public boolean isGameOver() {
    return foundations.size() == 4
            && foundations.stream().allMatch(t -> (t.getPile().size()) == 13);
  }

  @Override
  public String getGameState() {
    if (foundations.size() != 4) {
      return "";
    }

    StringBuilder bldr = new StringBuilder();

    for (int i = 1; i <= foundations.size(); i++) {
      bldr.append(foundations.get(i - 1).getPileAsString(i));
      bldr.append("\n");
    }

    for (int i = 1; i <= opens.size(); i++) {
      bldr.append(opens.get(i - 1).getPileAsString(i));
      bldr.append("\n");
    }

    for (int i = 1; i < cascades.size(); i++) {
      bldr.append(cascades.get(i - 1).getPileAsString(i));
      bldr.append("\n");
    }

    bldr.append(cascades.get(cascades.size() - 1).getPileAsString(cascades.size()));

    return bldr.toString();
  }
}
