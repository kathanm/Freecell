package cs3500.hw02;

/**
 * Type for the four different types of cards in a standard deck.
 */
public enum CardType {
  SPADE("B", "♠"), CLUB("B", "♣"), HEART("R", "♥"), DIAMOND("R", "♦");

  private String color;
  private String icon;

  /**
   * Gets the color of the card type.
   *
   * @return the color of the card type as a one character string that is either "B" or "R"
   */
  public String getColor() {
    return this.color;
  }

  /**
   * Gets the icon of the card type.
   *
   * @return Returns the unicode character that represents the card type icon - one of ♠,♣,♥,♦
   */
  public String getIcon() {
    return this.icon;
  }

  private CardType(String color, String icon) {
    this.color = color;
    this.icon = icon;
  }
}
