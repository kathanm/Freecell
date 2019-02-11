package cs3500.hw02;

/**
 * Possible values for a card in a standard deck of cards. Contains numerical and string
 * representations of the value.
 */
public enum CardValue {
  ACE(1, "A"),
  TWO(2, "2"),
  THREE(3, "3"),
  FOUR(4, "4"),
  FIVE(5, "5"),
  SIX(6, "6"),
  SEVEN(7, "7"),
  EIGHT(8, "8"),
  NINE(9, "9"),
  TEN(10, "10"),
  JACK(11, "J"),
  QUEEN(12, "Q"),
  KING(13, "K");

  private int value;
  private String name;

  /**
   * Get the numerical value as an integer.
   *
   * @return An integer from 1 - 13 representing the value
   */
  public int getValue() {
    return this.value;
  }

  /**
   * Get the text value as a string.
   *
   * @return A string from 'A' to 'K' representing the textual value
   */
  public String getName() {
    return this.name;
  }

  private CardValue(int value, String name) {
    this.value = value;
    this.name = name;
  }
}
