package main;

import java.util.ArrayList;

/**
 * One hand of Uno cards 
 */
public class Hand {
	
	/** A list containing the Cards in the hand */
	private ArrayList<Card> cardList;
	
	/**
	 * Create a hand using cards from deck
	 */
	public Hand() {
		cardList = new ArrayList<>();
	}
	
	/**
	 * returns a reference to the list that stores all the cards
	 * @return the cards in the hand
	 */
	public ArrayList<Card> getCards(){
		return cardList;
	}
	
	/**
	 * Add a card to the hand
	 * @param card the card to be added
	 */
	public void addCard(Card card) {
		cardList.add(card);
	}
	
	/**
	 * Remove a card from the hand
	 * @param card the card to be removed
	 * @return whether the card was removed successfully
	 */
	public boolean removeCard(Card card) {
		return cardList.remove(card);
	}
	
	/**
	 * @return the sum of the point values of the cards in the hand
	 */
	public int handValue() {
		int totalValue = 0;
		for (Card card: cardList) {
			totalValue += card.getPointValue();
		}
		return totalValue;
	}
	
	/**
	 * Get all cards in the hand that match cardToMatch
	 * @param cardToMatch the card to match
	 * @return a list of all cards that match cardToMatch
	 * <p>Note: Wild draw fours are only added if no other cards in the match list match cardToMatch by color
	 */
	public ArrayList<Card> getMatches(Card cardToMatch) {
		ArrayList<Card> matches = new ArrayList<>();
		ArrayList<Card> wd4s = new ArrayList<>();
		// look for cards that match
		for (Card card: cardList) {
			if (card.hasRank(Rank.WILD_DRAW_FOUR)) {
				wd4s.add(card);
			}
			else if (card.matches(cardToMatch)) {
				matches.add(card);
			}
		}
		// add wild draw fours if they can be played
		boolean wd4Valid = true;
		for (Card match: matches) {
			if (match.matchesColor(cardToMatch)) {
				wd4Valid = false;
			}
		}
		if (wd4Valid) {
			for (Card card: wd4s) {
				matches.add(card);
			}
		}
		return matches;
	}

}