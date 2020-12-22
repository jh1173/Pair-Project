package main;

import java.util.ArrayList;

/**
 * The discard pile
 */
public class Pile {
	
	/** The pile of cards */
	private ArrayList<Card> pile;
	
	/**
	 * Create a Pile with one card from the deck
	 * @param deck
	 */
	public Pile(Deck deck) {
		pile = new ArrayList<>();
		pile.add(deck.deal());
	}
	
	/**
	 * @return the top card of the pile
	 */
	public Card topCard() {
		return pile.get(0);
	}
	
	/**
	 * Add a card to the top of the pile
	 * @param card the card to be added
	 */
	public void addCard(Card card) {
		pile.add(0, card);
	}
	
	/**
	 * Remove all cards from the pile, except for the top card
	 * @return a list containing the cards that were removed
	 */
	public ArrayList<Card> reset() {
		Card topCard = pile.remove(0);
		ArrayList<Card> newDeck = new ArrayList<>(pile);
		pile.clear();
		pile.add(topCard);
		return newDeck;
	}

}
