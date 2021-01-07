package main;

import java.util.ArrayList;

/**
 * A player of the game
 */
public class Player {
	
	/** The player's hand */
	private Hand hand;
	
	/**
	 * Construct a player with a new Hand
	 * @param deck the deck that will be used to generate the hand
	 * @param pile the pile where cards will go when played
	 */
	public Player(Deck deck, Pile pile) {
		hand = new Hand();
		//fillHand(deck, pile);
	}
	
	/**
	 * @return (a reference to) the player's Hand
	 */
	public Hand getHand() {
		return hand;
	}
	
	/**
	 * Clear the player's hand and fill it with seven cards from the deck
	 * @param deck the deck from which the player draws cards
	 * @param pile the pile onto which the player plays cards
	 */
	public void fillHand(Deck deck, Pile pile) {
		hand.getCards().clear();
		for (int i = 0; i < 7; i++) {
			drawCard(deck, pile);
		}
	}
	
	/**
	 * @return the player has exactly one card left in the Hand
	 */
	public boolean oneCardLeft() {
		return hand.getCards().size() == 1;
	}
	
	/**
	 * @return the player has no cards left in the Hand
	 */
	public boolean hasWon() {
		return hand.getCards().isEmpty();
	}
	
	/**
	 * draw a card from the deck and add it to the hand
	 * @param deck the deck from which the card was drawn
	 * @param pile the pile onto which cards will be played (in case the deck needs to reset)
	 * @return the card that was drawn
	 */
	public Card drawCard(Deck deck, Pile pile) {
		Card card = deck.deal();
		hand.addCard(card);
		if (deck.isEmpty()) {
			deck.reset(pile);
		}
		return card;
	}
	
	/**
	 * draw multiple cards from the deck and add them to the hand
	 * @param deck the deck from which the card was drawn
	 * @param pile the pile onto which cards will be played (in case the deck needs to reset)
	 * @param numCards the number of cards to draw
	 * @return the list of cards drawn
	 */
	public ArrayList<Card> drawCards(Deck deck, Pile pile, int numCards) {
		ArrayList<Card> cardsDrawn = new ArrayList<>();
		for (int i = 0; i < numCards; i++) {
			cardsDrawn.add(drawCard(deck, pile));
		}
		return cardsDrawn;
	}
	
	/**
	 * Play the card onto the pile<br>
	 * <b>Important: Does not check whether the play is valid</b>
	 * @param card the card to be played
	 * @param pile the pile on which the card will be played
	 */
	public void playCard(Card card, Pile pile) {
		hand.removeCard(card);
		pile.addCard(card);
	}
	
	/**
	 * Set the top card of pile to the designated color
	 * Precondition: the top card of pile is a wild card with Color NONE
	 * @param pile the pile
	 * @param color a Color that is not NONE
	 */
	public void setColor(Pile pile, Color color) {
		pile.topCard().setColor(color);
	}
	
	public int handSize() {
		return getHand().getCards().size();
	}

}
