package cs3500.hw03;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import cs3500.hw02.Card;
import cs3500.hw02.FreecellOperations;
import cs3500.hw02.PileType;

/**
 * Concrete implementation of the Freecell controller interface. Represents game logic for Freecell.
 */
public class FreecellController implements IFreecellController<Card> {
  private final Readable rd;
  private final Appendable ap;

  /**
   * Constructs a Freecell controller.
   *
   * @param rd {@code Readable} object to receive input
   * @param ap {@code Appendable} object to deliver output
   */
  public FreecellController(Readable rd, Appendable ap) throws IllegalArgumentException {
    if (rd == null || ap == null) {
      throw new IllegalArgumentException("Readable / appendable objects cannot be null");
    }
    this.rd = rd;
    this.ap = ap;
  }

  @Override
  public void playGame(List<Card> deck, FreecellOperations<Card> model, int numCascades,
                       int numOpens, boolean shuffle) throws IllegalArgumentException, IOException {
    // Throw an exception if the deck or model are null
    if (deck == null || model == null) {
      throw new IllegalArgumentException("Cannot start game with empty model or deck");
    }
    // Scanner to read input.
    Scanner sc = new Scanner(this.rd);
    // Start the game with the given parameters, return if the parameters are bad.
    try {
      model.startGame(deck, numCascades, numOpens, shuffle);
    } catch (IllegalArgumentException e) {
      ap.append("Could not start game.");
      return;
    }
    while (!model.isGameOver()) {
      // initialize variables for move
      PileType source;
      int sourcePileNumber;
      int cardIndex;
      PileType destination;
      int destPileNumber;

      // Print the current game state
      ap.append(model.getGameState());
      ap.append("\n");

      // get the source pile as a string
      String sourceString = sc.next();

      // Check if the source pile given is valid. If not, keep re-prompting until a valid one is
      // entered
      while (!isValidPile(sourceString)) {
        ap.append("\nInvalid source. Please input again.");
        sourceString = sc.next();
      }

      // Quit the game if the value is Q
      if (isPileStringQuitSignal(sourceString)) {
        ap.append("\nGame quit prematurely.");
        return;
      }

      // Set the piletype and pilenumber variables
      source = getPileType(sourceString);
      sourcePileNumber = getPileNumber(sourceString) - 1;

      // Similar logic for card index
      String cardIndexString = sc.next();

      while (!isValidCardIndex(cardIndexString)) {
        ap.append("\nInvalid card index. Please input again.");
        cardIndexString = sc.next();
      }

      if (cardIndexString.equals("q") || cardIndexString.equals("Q")) {
        ap.append("\nGame quit prematurely.");
        return;
      }

      cardIndex = Integer.valueOf(cardIndexString) - 1;

      // Similar logic for destination pile
      String destString = sc.next();

      while (!isValidPile(destString)) {
        ap.append("\nInvalid destination. Please input again.");
        destString = sc.next();
      }

      if (isPileStringQuitSignal(destString)) {
        ap.append("\nGame quit prematurely.");
        return;
      }

      destination = getPileType(destString);
      destPileNumber = getPileNumber(destString) - 1;

      // Try to make the move with the given parameters. If not possible, prompt user to try again
      try {
        model.move(source, sourcePileNumber, cardIndex, destination, destPileNumber);
      } catch (Exception e) {
        ap.append("\nInvalid move. Try again. " + e.getMessage());
      }
    }

    // If game is over, print game state and message and return
    ap.append(model.getGameState());
    ap.append("\nGame over.");
  }

  // Checks if a given card index is valid
  private boolean isValidCardIndex(String cardIndexString) {
    return cardIndexString.equalsIgnoreCase("q") || isStringAnInt(cardIndexString);
  }

  // Given a valid source pile string, extracts the pile number
  private int getPileNumber(String sourceString) {
    String pileNumberString = sourceString.substring(1);
    return Integer.valueOf(pileNumberString);
  }

  // Given a valid source pile string, extracts the pile type
  private PileType getPileType(String sourceString) {
    String pileTyprString = sourceString.substring(0, 1);
    switch (pileTyprString) {
      case "O":
        return PileType.OPEN;
      case "F":
        return PileType.FOUNDATION;
      case "C":
        return PileType.CASCADE;
      default:
        // Should never return null - only called with valid strings
        return null;
    }
  }

  // Checks if a pile string indicates game should be quit
  private boolean isPileStringQuitSignal(String sourceString) {
    return sourceString.equalsIgnoreCase("q");
  }

  // Checks if a given pile is a valid pile input
  private boolean isValidPile(String source) {
    // If it is to quit game then it is valid
    if (source.equals("q") || source.equals("Q")) {
      return true;
    }
    // If the first character is not q then the string should be at least 2 characters
    if (source.length() < 2) {
      return false;
    }
    String pile = source.substring(0, 1);
    String pileIndex = source.substring(1);

    // The pile should be a valid letter
    if (!(pile.equals("C") || pile.equals("O") || pile.equals("F"))) {
      return false;
    }

    // The pile index should be able to be converted to an int
    return isStringAnInt(pileIndex);
  }

  // Check if a string can be parsed for an int
  private boolean isStringAnInt(String s) {
    // false if string is null
    if (s == null) {
      return false;
    }
    // if scanner get parse string for int then return true
    try {
      Scanner sc = new Scanner(s);
      int num = sc.nextInt();
    }
    // return false if it throws an exception
    catch (Exception e) {
      return false;
    }
    return true;
  }
}