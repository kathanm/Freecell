import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cs3500.hw02.Card;
import cs3500.hw02.CardType;
import cs3500.hw02.CardValue;
import cs3500.hw02.FreecellModel;
import cs3500.hw02.PileType;

import static org.junit.Assert.assertEquals;

// Using FreecellModel and not FreecellOperations interface for testing as this implementation of
// the model has some defined behaviors that are not specified in the interface. These tests will
// also be testing for those specific behaviors so this test class should not be used for other
// implementations of FreecellOperations
public class FreecellModelTest {
  FreecellModel model;

  // Setup method to initialize a clean model
  private void setup() {
    this.model = new FreecellModel();
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

  // Check that getDeck gives a valid deck
  @Test
  public void testGetDeck() {
    setup();
    List<Card> deck = model.getDeck();
    assertEquals(true, isValidDeck(deck));
  }

  // Starting the game with a null deck should throw an exception
  @Test(expected = IllegalArgumentException.class)
  public void testStartGameNullDeck() {
    setup();
    List<Card> deck = null;
    model.startGame(null, 8, 4, true);
  }

  // Starting the game with an invalid deck should throw an exception
  @Test(expected = IllegalArgumentException.class)
  public void testStartGameInvalidDeck() {
    setup();
    Card aceOfSpades = new Card(CardType.SPADE, CardValue.ACE);
    List<Card> deck = model.getDeck();
    deck.add(0, aceOfSpades);
    deck.add(1, aceOfSpades);
    model.startGame(deck, 8, 4, true);
  }

  // Starting the game with a deck that has a size other than 52 should throw an exception
  @Test(expected = IllegalArgumentException.class)
  public void testStartGameInvalidDeckSize() {
    setup();
    List<Card> deck = model.getDeck();
    deck.add(new Card(CardType.SPADE, CardValue.ACE));
    model.startGame(deck, 9, 4, true);
  }

  // Starting the game with the number of cascade piles less than 4 should throw an exception
  @Test(expected = IllegalArgumentException.class)
  public void testStartGameTooFewCascadePiles() {
    setup();
    model.startGame(model.getDeck(), 3, 4, true);
  }

  // Starting the game with the number of open piles less than 1 should throw an exception
  @Test(expected = IllegalArgumentException.class)
  public void testStartGameTooFewOpenPiles() {
    setup();
    model.startGame(model.getDeck(), 8, 0, true);
  }

  // Check that when the deck is not shuffled the game state is the same every time
  @Test
  public void testStartGameNoShuffle() {
    setup();
    List<Card> deck = model.getDeck();
    model.startGame(deck, 8, 4, false);
    String gameState1 = model.getGameState();
    model.startGame(deck, 8, 4, false);
    String gameState2 = model.getGameState();
    assertEquals(true, gameState1.equals(gameState2));
  }

  // Check that shuffling the deck creates a different game state than not shuffling
  @Test
  public void testStartGameShuffle() {
    setup();
    List<Card> deck = model.getDeck();
    model.startGame(deck, 8, 4, false);
    String gameState1 = model.getGameState();
    model.startGame(deck, 8, 4, true);
    String gameState2 = model.getGameState();
    assertEquals(false, gameState1.equals(gameState2));
  }

  // Check that calling start game resets the game state
  @Test
  public void testStartGameResetsGame() {
    setup();
    model.startGame(generateTestDeck(), 4, 4, false);
    String startState = model.getGameState();
    model.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);
    model.move(PileType.CASCADE, 0, 11, PileType.OPEN, 0);
    assertEquals(false, model.getGameState().equals(startState));
    model.startGame(generateTestDeck(), 4, 4, false);
    assertEquals(startState, model.getGameState());
  }

  // Making a move before the game has started should throw an exception
  @Test(expected = IllegalStateException.class)
  public void testMoveBeforeStartGame() {
    setup();
    model.move(PileType.CASCADE, 0, 0, PileType.CASCADE, 1);
  }

  // Moving a card to an open pile that already has a card should throw an exception
  @Test(expected = IllegalArgumentException.class)
  public void testMoveToFilledOpenPile() {
    setup();
    model.startGame(generateTestDeck(), 4, 4, false);
    model.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    model.move(PileType.CASCADE, 0, 11, PileType.OPEN, 0);
  }

  // Moving to a cascade pile with a card that is the correct color but wrong value should throw
  // an exception
  @Test(expected = IllegalArgumentException.class)
  public void testMoveToCascadeWrongValue() {
    setup();
    model.startGame(generateTestDeck(), 4, 4, false);
    model.move(PileType.CASCADE, 0, 12, PileType.CASCADE, 2);
  }

  // Moving to a cascade pile with a card that is the right value but wrong color should throw an
  // exception
  @Test(expected = IllegalArgumentException.class)
  public void testMoveToCascadeWrongColor() {
    setup();
    model.startGame(generateTestDeck(), 4, 4, false);
    model.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    model.move(PileType.CASCADE, 1, 12, PileType.CASCADE, 0);
  }

  // Moving to a foundation pile must match the color of the pile
  @Test(expected = IllegalArgumentException.class)
  public void testMoveToFoundationWrongColor() {
    setup();
    model.startGame(generateTestDeck(), 4, 4, false);
    model.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);
    model.move(PileType.CASCADE, 1, 12, PileType.FOUNDATION, 1);
    model.move(PileType.CASCADE, 0, 11, PileType.FOUNDATION, 1);
  }

  // Moving to a foundation pile with a card of wrong value throws exception
  @Test(expected = IllegalArgumentException.class)
  public void testMoveToFoundationWrongValue() {
    setup();
    model.startGame(generateTestDeck(), 4, 4, false);
    model.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);
    model.move(PileType.CASCADE, 0, 11, PileType.OPEN, 0);
    model.move(PileType.CASCADE, 0, 10, PileType.FOUNDATION, 0);
  }

  // The first card in an empty pile must be an ace
  @Test(expected = IllegalArgumentException.class)
  public void testMoveToFoundationFirstNotAce() {
    setup();
    model.startGame(generateTestDeck(), 4, 4, false);
    model.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    model.move(PileType.CASCADE, 0, 11, PileType.FOUNDATION, 0);
  }

  // Moving from an empty cascade pile should throw an error
  @Test(expected = IllegalArgumentException.class)
  public void testMoveFromCascadeEmptyPile() {
    setup();
    model.startGame(generateTestDeck(), 52, 4, false);
    model.move(PileType.CASCADE, 0, 0, PileType.OPEN, 0);
    model.move(PileType.CASCADE, 0, 0, PileType.OPEN, 1);
  }

  // Moving from the middle of a cascade pile should throw an error
  @Test(expected = IllegalArgumentException.class)
  public void testMoveFromCascadeMiddleOfPile() {
    setup();
    model.startGame(generateTestDeck(), 4, 4, false);
    model.move(PileType.CASCADE, 0, 6, PileType.OPEN, 0);
  }

  // Moving from an empty open pile should throw an error
  @Test(expected = IllegalArgumentException.class)
  public void testMoveFromOpenEmptyPile() {
    setup();
    model.startGame(generateTestDeck(), 4, 4, false);
    model.move(PileType.OPEN, 0, 0, PileType.OPEN, 1);
  }

  // Moving from a foundation pile should throw an error even if not empty
  @Test(expected = IllegalArgumentException.class)
  public void testMoveFromFoundation() {
    setup();
    model.startGame(generateTestDeck(), 4, 4, false);
    model.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);
    model.move(PileType.FOUNDATION, 0, 0, PileType.OPEN, 0);
  }

  // Test valid moves update the game state
  @Test
  public void testMove() {
    setup();
    model.startGame(generateTestDeck(), 4, 4, false);
    String initialGameState = model.getGameState();
    assertEquals(false, initialGameState.contains("F1: A♠"));
    assertEquals(false, initialGameState.contains("O1: 2♠"));
    assertEquals(true, initialGameState.contains("C1: K♠, Q♠, J♠, 10♠, 9♠, "
            + "8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠"));
    model.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);
    model.move(PileType.CASCADE, 0, 11, PileType.OPEN, 0);
    String gameState = model.getGameState();
    assertEquals(false, gameState.equals(initialGameState));
    assertEquals(true, gameState.contains("F1: A♠"));
    assertEquals(true, gameState.contains("O1: 2♠"));
    assertEquals(false, gameState.contains("C1: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠,"
            + " 5♠, 4♠, 3♠, 2♠, A♠"));
    assertEquals(true, gameState.contains("C1: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠"));
  }

  // The game should be over when all the foundation piles are full
  @Test
  public void testIsGameOver() {
    setup();
    assertEquals(false, model.isGameOver());
    model.startGame(generateTestDeck(), 4, 4, false);
    assertEquals(false, model.isGameOver());
    model.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);
    model.move(PileType.CASCADE, 1, 12, PileType.FOUNDATION, 1);
    model.move(PileType.CASCADE, 2, 12, PileType.FOUNDATION, 2);
    model.move(PileType.CASCADE, 3, 12, PileType.FOUNDATION, 3);
    assertEquals(false, model.isGameOver());
    for (int i = 0; i < 4; i++) {
      for (int j = 11; j >= 0; j--) {
        model.move(PileType.CASCADE, i, j, PileType.FOUNDATION, i);
      }
    }
    assertEquals(true, model.isGameOver());
  }

  // The game state should print out an empty string if game hasn't started
  @Test
  public void testGetGameStatePreGame() {
    setup();
    assertEquals("", model.getGameState());
  }

  // The game state should print out an empty string if it's called after startGame throws an
  // exception
  @Test
  public void testGetGameStateAfterStartGameThrowsException() {
    setup();
    try {
      model.startGame(generateTestDeck(), 1, 0, false);
    } catch (IllegalArgumentException ignored) {
      // Do nothing when exception is thrown
    }
    assertEquals("", model.getGameState());
  }

  // The game state should be formatted correctly after the game has begun
  @Test
  public void testGetGameState() {
    setup();
    model.startGame(generateTestDeck(), 4, 4, false);
    String expectedState = "F1:\n" +
            "F2:\n" +
            "F3:\n" +
            "F4:\n" +
            "O1:\n" +
            "O2:\n" +
            "O3:\n" +
            "O4:\n" +
            "C1: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "C2: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦";
    assertEquals(expectedState, model.getGameState());
  }
}
