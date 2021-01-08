package main;

/**
 * One card in the Uno Game 
 */
public class Card {
	
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
	 * @return the card can be played on the other card (by matching in color or rank)
	 * <p>Note: Wild cards (but not wild draw fours) will always match the other card (not necessarily symmetric)
	 */
	public boolean matches(Card other) {
		return matchesColor(other) || matchesRank(other) || hasRank(Rank.WILD);
	}
	
	/**
	 * @param rank a rank
	 * @return the card has the given rank
	 */
	public boolean hasRank(Rank rank) {
		return this.rank.equals(rank);
	}
	
	/**
	 * @param other the other card
	 * @return the card has the same rank as the other card
	 */
	public boolean matchesRank(Card other) {
		return hasRank(other.getRank());
	}
	
	/**
	 * @param color a color
	 * @return the card has the given color
	 */
	public boolean hasColor(Color color) {
		return this.color.equals(color);
	}
	
	/**
	 * @param other the other card
	 * @return the card has the same color as the other card
	 */
	public boolean matchesColor(Card other) {
		return hasColor(other.getColor());
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
	 * @return whether the rank represents an action card (draw two, reverse, skip, wild draw four)
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
	
	/**
	 * string representation of the card
	 */
	@Override
	public String toString() {
		return (color.toString() + "_" + rank.toString());
	}

}
