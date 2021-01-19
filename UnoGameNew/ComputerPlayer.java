package main;

import java.util.ArrayList;

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
		Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};
		return colors[(int)(Math.random() * 4)];
	}

}