package main;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A computer player: contains extra methods to decide what to do during turns
 */
public class ComputerPlayer extends Player {
	
	/**
	 * Construct a Computer Player with a hand from the deck
	 * @param deck the deck
	 */
	public ComputerPlayer() {
		super();
	}
	
	// TODO make better methods (these are actually already pretty good)
	
	/**
	 * Chooses a legal card to play
	 * @param cardToMatch the card that needs to be matched
	 * @return the card to play
	 */
	public Card chooseCard(Card cardToMatch) {
		ArrayList<Card> allMatches = getHand().getMatches(cardToMatch);
		return allMatches.get((int) (allMatches.size() * Math.random()));
	}
	
	/**
	 * Chooses a color to make a wild or wild draw 4 card that was just played
	 * @return the color to make the wild/wild draw 4 card
	 */
	public Color chooseColor() {
		HashMap<Color, Integer> colorCount = new HashMap<Color, Integer>();
		colorCount.put(Color.RED, 0);
		colorCount.put(Color.GREEN, 0);
		colorCount.put(Color.BLUE, 0);
		colorCount.put(Color.YELLOW, 0);
		for (Card card: getHand().getCards()) {
			Color currentColor = card.getColor();
			if (!currentColor.equals(Color.NONE)) {
				Integer currentCount = colorCount.get(currentColor);
				colorCount.replace(currentColor, currentCount + 1);
			}
		}
		int maxCount = 0;
		ArrayList<Color> maxColors = new ArrayList<Color>();
		Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};
		for (Color color: colors) {
			int thisColorCount = colorCount.get(color);
			if (thisColorCount > maxCount) {
				maxCount = thisColorCount;
				maxColors.clear();
				maxColors.add(color);
			}
			else if (thisColorCount == maxCount) {
				maxColors.add(color);
			}
		}
		return maxColors.get((int)(Math.random() * maxColors.size()));
	}

}
