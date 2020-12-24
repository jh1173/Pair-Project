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
	public ComputerPlayer(Deck deck, Pile pile) {
		super(deck, pile);
	}
	
	public Card chooseCard(Card cardToMatch) {
		ArrayList<Card> allMatches = getHand().getMatches(cardToMatch);
		return allMatches.get((int) (allMatches.size() * Math.random()));
	}
	
	public Color chooseColor() {
		Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};
		return colors[(int)(Math.random() * 4)];
	}

}
