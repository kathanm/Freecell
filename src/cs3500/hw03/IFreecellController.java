package cs3500.hw03;

import java.io.IOException;
import java.util.List;

import cs3500.hw02.FreecellOperations;

/**
 * This is an interface of the Freecell controller.
 *
 * @param <K> The card type to be used for the game
 */
public interface IFreecellController<K> {
  /**
   * Starts a new game of Freecell.
   *
   * @param deck        The deck of cards to be used for the game
   * @param model       The model to be used for the game
   * @param numCascades The number of cascade piles to create
   * @param numOpens    The number of open piles to create
   * @param shuffle     Determines if the deck should be shuffled
   * @throws IllegalArgumentException if the model or deck are null
   * @throws IOException              if the controller is unable to successfully
   *                                  receive input or output
   */
  void playGame(List<K> deck, FreecellOperations<K> model, int numCascades,
                int numOpens, boolean shuffle) throws IllegalArgumentException, IOException;
}
