package cs3500.hw04;

import cs3500.hw02.FreecellModel;
import cs3500.hw02.FreecellOperations;

/**
 * Factory class to create a model of Freecell that is either SINGLEMOVE or MULTIMOVE. Singlemove
 * models allow for only 1 card to be moved at a time, whereas multimove allows for multiple cards
 * to be moved in a single turn if the move is possible by performing a series of single card moves.
 */
public class FreecellModelCreator {
  /**
   * Enum representing single-move and multi-move Freecell models.
   */
  public enum GameType {
    SINGLEMOVE, MULTIMOVE
  }

  /**
   * Creates an instance of a Freecell model based on the given Gametype.
   *
   * @param type The type of Freecell game - either single move or multi-move
   * @return An instance of a model, a {@code FreecellModel} for single move, and a
   * {@code MultiMoveFreecellModel} if multi-move
   */
  public static FreecellOperations create(GameType type) {
    switch (type) {
      case SINGLEMOVE:
        return new FreecellModel();
      case MULTIMOVE:
        return new MultiMoveFreecellModel();
      default:
        return null;
    }
  }
}
