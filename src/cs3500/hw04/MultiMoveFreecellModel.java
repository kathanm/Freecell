package cs3500.hw04;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs3500.hw02.Card;
import cs3500.hw02.FreecellModel;
import cs3500.hw02.Pile;
import cs3500.hw02.PileType;

/**
 * Represents a model of the Freecell game. This version of the model allows for multi-card
 * moves if there are enough empty open and cascade piles for the move to be made by performing
 * a series of single-card moves.
 */
public class MultiMoveFreecellModel extends FreecellModel {
  /**
   * Constructs an empty instance of the multi-move Freecell model.
   */
  public MultiMoveFreecellModel() {
    super();
  }

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

    if (source == PileType.CASCADE && destination == PileType.CASCADE) {
      Pile sourceCas = pileMap.get(source).get(pileNumber);
      Pile destCas = pileMap.get(destination).get(destPileNumber);
      moveCascadeToCascade(sourceCas, destCas, cardIndex);
      return;
    }

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

  private void moveCascadeToCascade(Pile sourceCas, Pile destCas, int cardIndex) {
    if (cardIndex >= sourceCas.getPile().size()) {
      throw new IllegalArgumentException("Card index too high.");
    }

    if (sourceCas.getPile().size() == 0) {
      throw new IllegalArgumentException("Source pile is empty. Cannot move from there.");
    }

    // Since source pile is not empty and card index is less than pile size, guaranteed to have
    // at least one card in this list
    // Create new list as sublist function retruns only a view of original list - causes issues
    // when trying to use removeAll method
    List<Card> cardsToMove =
            new ArrayList<>(sourceCas.getPile().subList(cardIndex, sourceCas.getPile().size()));
    if (!isValidSubset(cardsToMove)) {
      throw new IllegalArgumentException("Cannot move that set of cards. Not a valid sequence.");
    }

    Card topCard = cardsToMove.get(0);
    if (!destCas.isValidDestination(topCard)) {
      throw new IllegalArgumentException("Not a valid destination.");
    }

    int numEmptyCascades = 0;
    int numEmptyOpens = 0;

    for (Pile<Card> p : cascades) {
      if (p.getPile().size() == 0) {
        numEmptyCascades++;
      }
    }

    for (Pile<Card> p : opens) {
      if (p.getPile().size() == 0) {
        numEmptyOpens++;
      }
    }

    int maxSubsetSize = (numEmptyOpens + 1) * (int) Math.pow(2, numEmptyCascades);
    if (cardsToMove.size() > maxSubsetSize) {
      throw new IllegalArgumentException("Illegal move. Card to move size too large.");
    }

    // If it passes all 3 conditions move the cards
    destCas.getPile().addAll(cardsToMove);
    sourceCas.getPile().removeAll(cardsToMove);

  }

  private boolean isValidSubset(List<Card> subset) {
    if (subset.size() == 0) {
      return false;
    }

    if (subset.size() == 1) {
      return true;
    }

    // For each card that has a next card, if the next cards color is the same or if the value
    // is not 1 less than the current card, then it is not a valid subset of cards.
    for (int i = 0; i < subset.size() - 1; i++) {
      Card current = subset.get(i);
      Card next = subset.get(i + 1);
      if (current.getCardType().getColor().equals(next.getCardType().getColor())
              || current.getCardValue().getValue() - 1 != next.getCardValue().getValue()) {
        return false;
      }
    }

    return true;
  }
}
