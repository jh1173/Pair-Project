package main;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The deck of cards
 */
public class Deck {
	
	/** The deck of cards */
	private ArrayList<Card> cardList;
	
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
		cardList = new ArrayList<>();
		addNonWildDraw4s();
	}
	
	/**
	 * Add all the non-wild draw 4 cards (specified in the constructor)
	 */
	public void addNonWildDraw4s() {
		for (Color color: Color.values()) {
			// don't add NONE
			if (!color.equals(Color.NONE)) {
				// add one zero
				cardList.add(new Card(Rank.NUM0, color));
				// add two of each of the other number and action cards
				for (Rank rank: Rank.values()) {
					if (!rank.equals(Rank.NUM0) && !Card.isWildRank(rank)) {
						cardList.add(new Card(rank, color));
						cardList.add(new Card(rank, color));
					}
				}
			}
		}
		// add wild cards
		for (int i = 0; i < 4; i++) {
			cardList.add(new Card(Rank.WILD, Color.NONE));
		}
		Collections.shuffle(cardList);
	}
	
	/**
	 * Add wild draw 4 cards to the deck, shuffle<br>
	 * This is called after creating the pile, in order to avoid putting a Wild Draw 4 on the top of the discard pile
	 */
	public void addWildDraw4s() {
		for (int i = 0; i < 4; i++) {
			cardList.add(new Card(Rank.WILD_DRAW_FOUR, Color.NONE));
		}
		Collections.shuffle(cardList);
	}
	
	/**
	 * @return whether the deck is empty
	 */
	public boolean isEmpty() {
		return cardList.isEmpty();
	}
	
	/**
	 * deal a card from the top of the deck, removing it from the deck
	 * @return the card dealt
	 */
	public Card deal() {
		return cardList.remove(0);
	}
	
	/**
	 * put all the cards back in the deck
	 * @param pile the discard pile with the deck's cards
	 */
	public void reset(Pile pile) {
		cardList = pile.reset();
		Collections.shuffle(cardList);
		for (Card card: cardList) {
			if (card.isActionCard()) {
				card.setActive(true);
			}
			if (card.isWildCard()) {
				card.setColor(Color.NONE);
			}
		}
	}
	
	/**
	 * Clear the deck and add back all the cards except for wild draw 4s<br>
	 * Be sure to add the wild draw 4s after setting the first card of the deck
	 */
	public void hardReset() {
		cardList.clear();
		addNonWildDraw4s();
	}
	
	/**
	 * @return the number of cards in the deck
	 */
	public int numCards() {
		return cardList.size();
	}

}