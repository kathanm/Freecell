import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import cs3500.hw02.Card;
import cs3500.hw02.CardType;
import cs3500.hw02.CardValue;
import cs3500.hw02.FreecellModel;
import cs3500.hw02.PileType;
import cs3500.hw03.FreecellController;
import cs3500.hw03.IFreecellController;
import cs3500.hw04.MultiMoveFreecellModel;

import static org.junit.Assert.assertEquals;

/**
 * Test class for multi-move freecell model.
 */
public class MultiMoveFreecellModelTest {
  private MultiMoveFreecellModel model;

  private void setup() {
    this.model = new MultiMoveFreecellModel();
  }

  // Create a test deck, that when dealt over 4 cascade piles, will result in each pile sorted by
  // color and in descending order. This will make testing for certain operations simpler.
  // The order of the decks will be Spade, Club, Heart, Diamond
  private List<Card> generateTestDeck() {
    List<Card> deck = new ArrayList<>();
    for (int i = 12; i >= 0; i--) {
      CardValue value = CardValue.values()[i];
      for (int j = 0; j < 4; j++) {
        CardType type = CardType.values()[j];
        Card current = new Card(type, value);
        deck.add(current);
      }
    }
    return deck;
  }

  @Test
  public void testMultiMoveIllegalSubset() {
    boolean success = false;

    setup();
    model.startGame(generateTestDeck(), 4, 4, false);
    // Move A, 2, 3 to foundation piles from spades
    model.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);
    model.move(PileType.CASCADE, 0, 11, PileType.FOUNDATION, 0);
    model.move(PileType.CASCADE, 0, 10, PileType.FOUNDATION, 0);
    // Try to move A, 2, 3 of hearts below 4 of spades
    // 4 open piles so subset size can be max 5 (4 + 1 * 2^0)
    // thus only condition being broke is invalid subset
    // 1. Is it a valid build - FALSE
    // 2. Is the destination card correct - TRUE
    // 3. Are there enough empty open and cascades - TRUE
    try {
      model.move(PileType.CASCADE, 2, 10, PileType.CASCADE, 0);
    } catch (IllegalArgumentException e) {
      success = true;
    }

    assertEquals(true, success);
  }

  @Test
  public void testMultiMoveNotEnoughEmptyOpensAndCascades() {
    boolean success = false;

    setup();
    model.startGame(generateTestDeck(), 4, 2, false);
    // Move Ace of spades to O1
    model.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    // Move Ace of hearts to C1 below 2 of spades
    model.move(PileType.CASCADE, 2, 12, PileType.CASCADE, 0);
    // Move 2 of hearts to O2
    model.move(PileType.CASCADE, 2, 11, PileType.OPEN, 1);
    // 0 empty opens / cascades - (0 + 1 * 2^0) = 1 max subset size
    // Try to move 2 spades and A heart to 3 hearts - should fail
    // 1. Is it a valid build - TRUE
    // 2. Is the destination card correct - TRUE
    // 3. Are there enough empty open and cascades - FALSE
    try {
      model.move(PileType.CASCADE, 0, 11, PileType.CASCADE, 2);
    } catch (IllegalArgumentException e) {
      success = true;
    }

    assertEquals(true, success);
  }

  @Test
  public void testMultiMoveInvalidDestination() {
    boolean success = false;

    setup();
    model.startGame(generateTestDeck(), 4, 4, false);
    // Move Ace of spades to O1
    model.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    // Move Ace of hearts to C1 below 2 of spades
    model.move(PileType.CASCADE, 2, 12, PileType.CASCADE, 0);
    // Try to move 2 spades and A heart to 2 hearts - should fail
    // 1. Is it a valid build - TRUE
    // 2. Is the destination card correct - FALSE
    // 3. Are there enough empty open and cascades - TRUE
    try {
      model.move(PileType.CASCADE, 0, 11, PileType.CASCADE, 2);
    } catch (IllegalArgumentException e) {
      success = true;
    }

    assertEquals(true, success);
  }

  @Test
  public void testMultiMoveSuccessful() {
    setup();
    model.startGame(generateTestDeck(), 4, 4, false);
    // Move Ace of spades to O1
    model.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    // Move Ace of hearts to C1 below 2 of spades
    model.move(PileType.CASCADE, 2, 12, PileType.CASCADE, 0);
    // Move 2 of hearts to O2
    model.move(PileType.CASCADE, 2, 11, PileType.OPEN, 1);
    // Try to move 2 spades and A heart to 3 hearts - should pass
    // 1. Is it a valid build - TRUE
    // 2. Is the destination card correct - TRUE
    // 3. Are there enough empty open and cascades - TRUE
    model.move(PileType.CASCADE, 0, 11, PileType.CASCADE, 2);

    assertEquals(true, model.getGameState().contains("3♥, 2♠, A♥"));
    assertEquals(false, model.getGameState().contains("3♠, 2♠, A♥"));
  }

  @Test
  public void testMultiMoveSuccessfulWithEmptyCascades() {
    setup();
    model.startGame(generateTestDeck(), 52, 1, false);
    // Fill up all the open piles
    model.move(PileType.CASCADE, 0, 0, PileType.OPEN, 0);
    // Move Ace Spade to 2 Diamond
    model.move(PileType.CASCADE, 48, 0, PileType.CASCADE, 47);
    // move ace spade 2 diamond to 3 spade
    model.move(PileType.CASCADE, 47, 0, PileType.CASCADE, 40);

    assertEquals(true, model.getGameState().contains("C41: 3♠, 2♦, A♠\n"));
  }

  @Test
  public void testSingleMoveModelCantMakeMultiMoves() {
    boolean success = false;

    FreecellModel smodel = new FreecellModel();

    smodel.startGame(generateTestDeck(), 4, 4, false);
    // Move Ace of spades to O1
    smodel.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    // Move Ace of hearts to C1 below 2 of spades
    smodel.move(PileType.CASCADE, 2, 12, PileType.CASCADE, 0);
    // Move 2 of hearts to O2
    smodel.move(PileType.CASCADE, 2, 11, PileType.OPEN, 1);
    // Try to move 2 spades and A heart to 3 hearts - should pass
    try {
      smodel.move(PileType.CASCADE, 0, 11, PileType.CASCADE, 2);
    } catch (IllegalArgumentException e) {
      success = true;
    }

    assertEquals(true, success);
  }

  @Test
  public void testMultiMoveWorksWithController() {
    setup();
    StringBuilder winningGame = new StringBuilder();
    for (int i = 1; i <= 4; i++) {
      for (int j = 13; j >= 1; j--) {
        winningGame.append("C").append(i).append(" ").append(j).append(" F").append(i).append(" ");
      }
    }
    Readable rd = new StringReader(winningGame.toString());
    Appendable ap = new StringBuffer();
    IFreecellController<Card> controller = new FreecellController(rd, ap);
    try {
      controller.playGame(generateTestDeck(), model, 4, 4, false);
    } catch (IOException ignored) {
      // Ignore exception
    }
    assertEquals(true, ap.toString().contains("\nGame over."));
  }
}
