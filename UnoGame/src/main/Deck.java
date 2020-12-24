package main;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The deck of cards
 */
public class Deck {
	
	/** The deck of cards */
	private ArrayList<Card> deck;
	
	/**
	 * Construct a new Deck with the standard Uno Cards<br>
	 * <p>108 cards as follows:
	 * <li>19 Blue cards - 0 to 9 (one 0, two of each of 1-9)
	 * <li>19 Green cards - 0 to 9 (one 0, two of each of 1-9)
	 * <li>19 Red cards - 0 to 9 (one 0, two of each of 1-9)
	 * <li>19 Yellow cards - 0 to 9 (one 0, two of each of 1-9)
	 * <li>8 Draw Two cards - 2 each in blue, green, red and yellow
	 * <li>8 Reverse cards - 2 each in blue, green, red and yellow
	 * <li>8 Skip cards - 2 each in blue, green, red and yellow
	 * <li>4 Wild cards
	 * <li><s>4 Wild Draw Four cards</s> These will be added via {@link addWildDraw4s()}
	 */
	public Deck() {
		deck = new ArrayList<>();
		addNonWildDraw4s();
	}
	
	public void addNonWildDraw4s() {
		for (Color color: Color.values()) {
			// don't add NONE
			if (!color.equals(Color.NONE)) {
				// add one zero
				deck.add(new Card(Rank.NUM0, color));
				// add two of each of the other number and action cards
				for (Rank rank: Rank.values()) {
					if (!rank.equals(Rank.NUM0) && !Card.isWildRank(rank)) {
						deck.add(new Card(rank, color));
					}
				}
			}
		}
		// add wild cards
		for (int i = 0; i < 4; i++) {
			deck.add(new Card(Rank.WILD, Color.NONE));
		}
		Collections.shuffle(deck);
	}
	
	/**
	 * Add wild draw 4 cards to the deck, shuffle<br>
	 * This is called after creating the pile, in order to avoid putting a Wild Draw 4 on the top of the discard pile
	 */
	public void addWildDraw4s() {
		for (int i = 0; i < 4; i++) {
			deck.add(new Card(Rank.WILD_DRAW_FOUR, Color.NONE));
		}
		Collections.shuffle(deck);
	}
	
	/**
	 * @return whether the deck is empty
	 */
	public boolean isEmpty() {
		return deck.isEmpty();
	}
	
	/**
	 * deal a card from the top of the deck, removing it from the deck
	 * @return the card dealt
	 */
	public Card deal() {
		return deck.remove(0);
	}
	
	/**
	 * put all the cards back in the deck
	 * @param pile the discard pile with the deck's cards
	 */
	public void reset(Pile pile) {
		deck = pile.reset();
		Collections.shuffle(deck);
		for (Card card: deck) {
			if (card.isActionCard()) {
				card.setActive(true);
			}
			if (card.isWildCard()) {
				card.setColor(Color.NONE);
			}
		}
	}
	
	public void hardReset() {
		deck.clear();
		addNonWildDraw4s();
	}

}
