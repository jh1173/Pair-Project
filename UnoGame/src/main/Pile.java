package main;

import java.util.ArrayList;

/**
 * The discard pile
 */
public class Pile {
	
	/** The pile of cards */
	private ArrayList<Card> cardList;
	// can also use an ArrayDeque<Card>
	
	/**
	 * Create a Pile with one card from the deck
	 * @param deck
	 */
	public Pile(Deck deck) {
		cardList = new ArrayList<>();
		cardList.add(deck.deal());
	}
	
	/**
	 * @return the top card of the pile
	 */
	public Card topCard() {
		return cardList.get(0);
	}
	
	/**
	 * Add a card to the top of the pile
	 * @param card the card to be added
	 */
	public void addCard(Card card) {
		cardList.add(0, card);
	}
	
	/**
	 * Remove all cards from the pile, except for the top card
	 * @return a list containing the cards that were removed
	 */
	public ArrayList<Card> reset() {
		Card topCard = cardList.remove(0);
		ArrayList<Card> newDeck = new ArrayList<>(cardList);
		cardList.clear();
		cardList.add(topCard);
		return newDeck;
	}
	
	/**
	 * Remove all cards from the pile and add a card from the deck
	 * @param deck the deck
	 */
	public void hardReset(Deck deck) {
		cardList.clear();
		cardList.add(deck.deal());
	}

}
