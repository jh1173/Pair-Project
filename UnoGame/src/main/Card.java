package main;

/**
 * One card in the Uno Game 
 */
public class Card {
	
	/**
	 * Card Color constants<br>
	 * Card.Color.NONE represents the color of unassigned wild cards
	 */
	public static enum Color {
		RED, GREEN, BLUE, YELLOW, NONE
	}
	/**
	 * Card Rank constants
	 */
	public static enum Rank {
		NUM0, NUM1, NUM2, NUM3, NUM4, NUM5, NUM6, NUM7, NUM8, NUM9, DRAW_TWO, REVERSE, SKIP, WILD, WILD_DRAW_FOUR
	}
	/** Point values of Cards */
	private static int[] points = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 20, 20, 20, 50, 50};
	/** Card rank */
	private Rank rank;
	/** Card color */
	private Color color;
	/** Active state of action cards */
	private boolean isActive = false;
	
	/**
	 * Construct a Card object with the given rank and color
	 * @param rank
	 * @param color
	 */
	public Card(Rank rank, Color color) {
		this.rank = rank;
		this.color = color;
		if (isActionCard()) {
			isActive = true;
		}
	}

	/**
	 * @return isActive (state of action cards)
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the points
	 */
	public int getPointValue() {
		return points[getRankIndex()];
	}

	/**
	 * @return the rank
	 */
	public Rank getRank() {
		return rank;
	}
	
	/**
	 * @return the rank index
	 */
	public int getRankIndex() {
		return rank.ordinal();
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	 * @param other the other card for comparison
	 * @return the card matches the other card in color or rank
	 * <p>Note: Wild cards (not wild draw fours) will always match the other card (not necessarily symmetric)
	 */
	public boolean matches(Card other) {
		return color.equals(other.getColor()) || rank.equals(other.getRank()) || rank.equals(Rank.WILD);
	}
	
	/**
	 * @return whether the card is an action card
	 */
	public boolean isActionCard() {
		return isActionRank(rank);
	}
	
	/**
	 * @return whether the card is a wild card or wild draw four
	 */
	public boolean isWildCard() {
		return isWildRank(rank);
	}
	
	/**
	 * @param rank the rank
	 * @return whether the rank represents an action card
	 */
	public static boolean isActionRank(Rank rank) {
		int rankIndex = rank.ordinal();
		return rankIndex >= 9 && !rank.equals(Rank.WILD);
	}
	
	/**
	 * @param rank the rank
	 * @return whether the rank represents a wild card or wild draw four
	 */
	public static boolean isWildRank(Rank rank) {
		return rank.equals(Rank.WILD) || rank.equals(Rank.WILD_DRAW_FOUR);
	}

}
