import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import cs3500.hw02.Card;
import cs3500.hw02.CardType;
import cs3500.hw02.CardValue;
import cs3500.hw02.FreecellModel;
import cs3500.hw02.FreecellOperations;
import cs3500.hw03.FreecellController;
import cs3500.hw03.IFreecellController;

import static org.junit.Assert.assertEquals;

public class FreecellControllerTest {

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

  // Creating a controller with a null readable should throw an error
  @Test(expected = IllegalArgumentException.class)
  public void testCreateControllerNullReadable() {
    IFreecellController controller = new FreecellController(null, new StringBuffer());
  }

  // Creating a controller with a null appendable should throw an error
  @Test(expected = IllegalArgumentException.class)
  public void testCreateControllerNullAppendable() {
    IFreecellController controller = new FreecellController(new StringReader(""), null);
  }

  // Playing a game with a null deck should throw an error
  @Test(expected = IllegalArgumentException.class)
  public void testPlayGameNullDeck() {
    Readable rd = new StringReader("");
    Appendable ap = new StringBuffer();
    IFreecellController<Card> controller = new FreecellController(rd, ap);
    try {
      controller.playGame(null, new FreecellModel(), 8, 4, false);
    } catch (IOException ignored) {
      // Ignore exception
    }
  }

  // Playing a game with a null model should throw an error
  @Test(expected = IllegalArgumentException.class)
  public void testPlayGameNullModel() {
    Readable rd = new StringReader("");
    Appendable ap = new StringBuffer();
    IFreecellController<Card> controller = new FreecellController(rd, ap);
    try {
      controller.playGame(new ArrayList<>(), null, 8, 4, false);
    } catch (IOException ignored) {
      // Ignore exception
    }
  }

  // If there are an invalid number of cascades specified it should not start the game
  @Test
  public void testPlayGameInvalidCacades() {
    Readable rd = new StringReader("");
    Appendable ap = new StringBuffer();
    IFreecellController<Card> controller = new FreecellController(rd, ap);
    FreecellOperations<Card> model = new FreecellModel();
    try {
      controller.playGame(model.getDeck(), model, 3, 4, false);
    } catch (IOException ignored) {
      // Ignore exception
    }
    assertEquals("Could not start game.", ap.toString());
  }

  // If there are an invalid number of opens specified it should not start the game
  @Test
  public void testPlayGameInvalidOpens() {
    Readable rd = new StringReader("");
    Appendable ap = new StringBuffer();
    IFreecellController<Card> controller = new FreecellController(rd, ap);
    FreecellOperations<Card> model = new FreecellModel();
    try {
      controller.playGame(model.getDeck(), model, 8, 0, false);
    } catch (IOException ignored) {
      // Ignore exception
    }
    assertEquals("Could not start game.", ap.toString());
  }

  // It should re-prompt if the source pile has a bad letter given
  @Test
  public void testPlayGameInvalidSourcePileLetter() {
    Readable rd = new StringReader("R1 O1 q");
    Appendable ap = new StringBuffer();
    IFreecellController<Card> controller = new FreecellController(rd, ap);
    FreecellOperations<Card> model = new FreecellModel();
    try {
      controller.playGame(model.getDeck(), model, 8, 4, false);
    } catch (IOException ignored) {
      // Ignore exception
    }
    assertEquals(true, ap.toString().contains("Invalid source. Please input again."));
  }

  // It should re-prompt if the source pile has a bad index given
  @Test
  public void testPlayGameInvalidSourcePileNumber() {
    Readable rd = new StringReader("CC C1 q");
    Appendable ap = new StringBuffer();
    IFreecellController<Card> controller = new FreecellController(rd, ap);
    FreecellOperations<Card> model = new FreecellModel();
    try {
      controller.playGame(model.getDeck(), model, 8, 4, false);
    } catch (IOException ignored) {
      // Ignore exception
    }
    assertEquals(true, ap.toString().contains("Invalid source. Please input again."));
  }

  // It should re-prompt if a bad card index is given
  @Test
  public void testPlayGameInvalidCardIndex() {
    Readable rd = new StringReader("C1 aa 13 O1 q");
    Appendable ap = new StringBuffer();
    IFreecellController<Card> controller = new FreecellController(rd, ap);
    FreecellOperations<Card> model = new FreecellModel();
    try {
      controller.playGame(generateTestDeck(), model, 4, 4, false);
    } catch (IOException ignored) {
      // Ignore exception
    }
    assertEquals(true, ap.toString().contains("Invalid card index. Please input again."));
    assertEquals(true, ap.toString().contains("O1:\n"));
    assertEquals(true, ap.toString().contains("O1: A♠"));
  }

  // It should re-prompt if the destination pile has a bad letter given
  @Test
  public void testPlayGameInvalidDestPileLetter() {
    Readable rd = new StringReader("C1 13 R1 O1 q");
    Appendable ap = new StringBuffer();
    IFreecellController<Card> controller = new FreecellController(rd, ap);
    FreecellOperations<Card> model = new FreecellModel();
    try {
      controller.playGame(generateTestDeck(), model, 4, 4, false);
    } catch (IOException ignored) {
      // Ignore exception
    }
    assertEquals(true, ap.toString().contains("Invalid destination. Please input again."));
  }

  // It should re-prompt if the destination pile has a bad number given
  @Test
  public void testPlayGameInvalidDestPileNumber() {
    Readable rd = new StringReader("C1 13 OO O1 q");
    Appendable ap = new StringBuffer();
    IFreecellController<Card> controller = new FreecellController(rd, ap);
    FreecellOperations<Card> model = new FreecellModel();
    try {
      controller.playGame(generateTestDeck(), model, 4, 4, false);
    } catch (IOException ignored) {
      // Ignore exception
    }
    assertEquals(true, ap.toString().contains("Invalid destination. Please input again."));
    assertEquals(true, ap.toString().contains("O1:\n"));
    assertEquals(true, ap.toString().contains("O1: A♠"));
  }

  // It should end the game if a lower-case q is given for the source pile
  @Test
  public void testPlayGameSourcePileIsQuitLower() {
    Readable rd = new StringReader("q");
    Appendable ap = new StringBuffer();
    IFreecellController<Card> controller = new FreecellController(rd, ap);
    FreecellOperations<Card> model = new FreecellModel();
    try {
      controller.playGame(generateTestDeck(), model, 4, 4, false);
    } catch (IOException ignored) {
      // Ignore exception
    }
    assertEquals(true, ap.toString().contains("Game quit prematurely."));
  }

  // It should end the game if an upper-case q is given for the source pile
  @Test
  public void testPlayGameSourcePileIsQuitUpper() {
    Readable rd = new StringReader("Q");
    Appendable ap = new StringBuffer();
    IFreecellController<Card> controller = new FreecellController(rd, ap);
    FreecellOperations<Card> model = new FreecellModel();
    try {
      controller.playGame(generateTestDeck(), model, 4, 4, false);
    } catch (IOException ignored) {
      // Ignore exception
    }
    assertEquals(true, ap.toString().contains("Game quit prematurely."));
  }

  // It should end the game if a lower case q is given for the card index
  @Test
  public void testPlayGameCardIndexIsQuitLower() {
    Readable rd = new StringReader("C1 q");
    Appendable ap = new StringBuffer();
    IFreecellController<Card> controller = new FreecellController(rd, ap);
    FreecellOperations<Card> model = new FreecellModel();
    try {
      controller.playGame(generateTestDeck(), model, 4, 4, false);
    } catch (IOException ignored) {
      // Ignore exception
    }
    assertEquals(true, ap.toString().contains("Game quit prematurely."));
  }

  // It should end the game if an upper case q is given for the card index
  @Test
  public void testPlayGameCardIndexIsQuitUpper() {
    Readable rd = new StringReader("C1 Q");
    Appendable ap = new StringBuffer();
    IFreecellController<Card> controller = new FreecellController(rd, ap);
    FreecellOperations<Card> model = new FreecellModel();
    try {
      controller.playGame(generateTestDeck(), model, 4, 4, false);
    } catch (IOException ignored) {
      // Ignore exception
    }
    assertEquals(true, ap.toString().contains("Game quit prematurely."));
  }

  // It should end the game if a lower case q is given for the destination pile
  @Test
  public void testPlayGameDestPileIsQuitLower() {
    Readable rd = new StringReader("C1 13 q");
    Appendable ap = new StringBuffer();
    IFreecellController<Card> controller = new FreecellController(rd, ap);
    FreecellOperations<Card> model = new FreecellModel();
    try {
      controller.playGame(generateTestDeck(), model, 4, 4, false);
    } catch (IOException ignored) {
      // Ignore exception
    }
    assertEquals(true, ap.toString().contains("Game quit prematurely."));
  }

  // It should end the game if an upper case q is given for the destination pile
  @Test
  public void testPlayGameDestPileIsQuitUpper() {
    Readable rd = new StringReader("C1 13 Q");
    Appendable ap = new StringBuffer();
    IFreecellController<Card> controller = new FreecellController(rd, ap);
    FreecellOperations<Card> model = new FreecellModel();
    try {
      controller.playGame(generateTestDeck(), model, 4, 4, false);
    } catch (IOException ignored) {
      // Ignore exception
    }
    assertEquals(true, ap.toString().contains("Game quit prematurely."));
  }

  // It should re-prompt if the move cannot be made
  @Test
  public void testPlayGameInvalidMove() {
    Readable rd = new StringReader("C1 99 O1 q");
    Appendable ap = new StringBuffer();
    IFreecellController<Card> controller = new FreecellController(rd, ap);
    FreecellOperations<Card> model = new FreecellModel();
    try {
      controller.playGame(generateTestDeck(), model, 4, 4, false);
    } catch (IOException ignored) {
      // Ignore exception
    }
    assertEquals(true, ap.toString().contains("Invalid move. Try again."));
  }

  // It should print game over and return if the game has ended
  @Test
  public void testPlayGameGameOver() {
    StringBuilder winningGame = new StringBuilder();
    for (int i = 1; i <= 4; i++) {
      for (int j = 13; j >= 1; j--) {
        winningGame.append("C" + i + " " + j + " F" + i + " ");
      }
    }
    Readable rd = new StringReader(winningGame.toString());
    Appendable ap = new StringBuffer();
    IFreecellController<Card> controller = new FreecellController(rd, ap);
    FreecellOperations<Card> model = new FreecellModel();
    try {
      controller.playGame(generateTestDeck(), model, 4, 4, false);
    } catch (IOException ignored) {
      // Ignore exception
    }
    assertEquals(true, ap.toString().contains("\nGame over."));
  }
}
