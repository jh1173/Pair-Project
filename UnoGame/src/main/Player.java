package main;

/**
 * A player of the game
 */
public class Player {
	
	private Hand hand;
	
	/**
	 * Construct a player with a new Hand
	 * @param deck the deck that will be used to generate the hand
	 */
	public Player(Deck deck) {
		hand = new Hand(deck);
	}
	
	/**
	 * @return (a reference to) the player's Hand
	 */
	public Hand getHand() {
		return hand;
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
	 * @return the card that was drawn
	 */
	public Card drawCard(Deck deck) {
		Card card = deck.deal();
		hand.addCard(card);
		return card;
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
	 * Take a single turn of the game:
	 * <ol>
	 * <li>Take the action from an action card that was just played, ending the turn, OR:
	 * <li>Examine the player's hand and select a card for play OR draw a card
	 * <li>Play a selected card
	 * </ol>
	 * @param deck the deck from which the player will draw cards
	 * @param pile the pile onto which the player will play cards
	 */
	public void takeTurn(Deck deck, Pile pile) {
		Card topCard = pile.topCard();
		// action cards, end turn
		// select card OR draw from deck
		// play card if desired/possible
	}

}
