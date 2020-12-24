package main;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The Uno game, and the graphical user interface
 */
public class Game {

	// Game elements
	/** the game's deck */
	private Deck deck;
	/** the game's pile */
	private Pile pile;
	/** the list of players */
	private Player[] players;
	/** the points accumulated by all the players */
	private int[] points;
	/** The direction of play (can be changed by reverse cards) */
	private int playOrder = 1;
	/** the index of the current player */
	private int currentPlayerIndex;
	/** whether the current player has already drawn a card from the deck */
	private boolean hasDrawnCard;
	/** whether the game is over */
	private boolean gameOver = false;
	// GUI elements
	// TODO add GUI elements, player interface (will likely require wait-notify)
	// TODO get rid of print statements

	/**
	 * Create a game with the specified number of players
	 * @param numPlayers
	 */
	public Game(int numPlayers) {
		// set up deck and pile
		deck = new Deck();
		pile = new Pile(deck);
		deck.addWildDraw4s();
		// set up players
		players = new Player[numPlayers];
		//players[0] = new Player(deck, pile);
		players[0] = new ComputerPlayer(deck, pile);
		for (int i = 1; i < numPlayers; i++) {
			players[i] = new ComputerPlayer(deck, pile);
		}
		points = new int[numPlayers];
	}

	/**
	 * Reset the deck, pile, and all players' hands for a new round
	 */
	public void reset() {
		deck.hardReset();
		pile.hardReset(deck);
		deck.addWildDraw4s();
		for (Player player: players) {
			if (player != null) {
				player.fillHand(deck, pile);
			}
		}
	}

	/**
	 * @return the current player
	 */
	public Player currentPlayer() {
		return players[currentPlayerIndex];
	}

	/**
	 * @return the current player's hand
	 */
	public Hand currentPlayerHand() {
		return currentPlayer().getHand();
	}

	/**
	 * @return whether the current player is a human player
	 */
	public boolean currentPlayerIsHuman() {
		return !(currentPlayer() instanceof ComputerPlayer);
	}

	/**
	 * Start the current player's turn by determining the action the player should take
	 */
	public void startTurn() {
		Card topCard = pile.topCard();
		// if the top card is wild and not assigned a color (unlikely)
		if (topCard.hasColor(Color.NONE)) {
			chooseColor();
		}
		// action card
		if (topCard.isActive()) {
			if (currentPlayerIsHuman()) {
				// prompt to takeAction()
			}
			else {
				try {Thread.sleep(1000);}
				catch (InterruptedException ex) {}
				takeAction();
			}

		}
		else {
			ArrayList<Card> matches = currentPlayerHand().getMatches(topCard);
			// draw card
			if (matches.size() == 0) {
				if (currentPlayerIsHuman()) {
					// prompt to drawCard()
				}
				else {
					try {Thread.sleep(1000);}
					catch (InterruptedException ex) {}
					draw();
				}
			}
			// play card
			else {
				if (currentPlayerIsHuman()) {
					// prompt to playCard()
				}
				else {
					try {Thread.sleep(1000);}
					catch (InterruptedException ex) {}
					Card cardToPlay = ((ComputerPlayer)currentPlayer()).chooseCard(topCard);
					play(cardToPlay);
				}
			}
		}
	}

	/**
	 * The current player acts on an action card, the action card is deactivated
	 */
	public void takeAction() {
		// get rank of card
		Card topCard = pile.topCard();
		Rank topRank = topCard.getRank();
		// draw two cards
		if (topRank.equals(Rank.DRAW_TWO)) {
			System.out.println("draw two");
			currentPlayer().drawCards(deck, pile, 2);
		}
		// draw four cards
		else if (topRank.equals(Rank.WILD_DRAW_FOUR)) {
			System.out.println("draw four");
			currentPlayer().drawCards(deck, pile, 4);
		}
		// skip turn
		else {
			System.out.println("skip");
			// handle these (reverse acts like skip in 2 player game)
		}
		topCard.setActive(false);
	}

	/**
	 * The current player draws a card from the deck
	 */
	public void draw() {
		System.out.println("draw");
		Card cardDrawn = currentPlayer().drawCard(deck, pile);
		hasDrawnCard = true;
		// ask player whether to play card if playable
		ArrayList<Card> matches = currentPlayerHand().getMatches(pile.topCard());
		if (matches.contains(cardDrawn)) {
			if (currentPlayerIsHuman()) {
				// prompt to play card
			}
			else {
				play(cardDrawn);
			}
		}
	}

	/**
	 * The current player plays the selected card to the pile
	 * @param card the card to be played
	 */
	public void play(Card card) {
		System.out.println("play");
		currentPlayer().playCard(card, pile);
		// say uno
		if (currentPlayer().oneCardLeft()) {
			System.out.println("Uno");
		}
		// prompt wild card color selection if applicable
		if (card.isWildCard()) {
			chooseColor();
		}
	}
	
	/**
	 * The current player sets the color of a wild card that has not yet been assigned a color
	 */
	public void chooseColor() {
		if (currentPlayerIsHuman()) {

		}
		else {
			pile.topCard().setColor(((ComputerPlayer)currentPlayer()).chooseColor());
			System.out.println("set color");
		}
	}

	/**
	 * Transfer play to the next player
	 */
	public void nextPlayer() {
		Card topCard = pile.topCard();
		if (currentPlayer().hasWon()) {
			// handle win, including next player drawing cards if necessary and switching back to this player
			int thisRoundPoints = 0;
			for (Player player: players) {
				thisRoundPoints += player.getHand().handValue();
			}
			points[currentPlayerIndex] += thisRoundPoints;
			nextRound();
		}
		else {
			// reverse card
			if (topCard.hasRank(Rank.REVERSE) && topCard.isActive() && players.length != 2) {
				System.out.println("reverse");
				playOrder *= -1;
				topCard.setActive(false);
			}
			// switch to next player
			currentPlayerIndex = Math.floorMod(currentPlayerIndex + playOrder, players.length);
			hasDrawnCard = false;
			System.out.println(currentPlayerIndex);
			startTurn();
		}
	}

	/**
	 * Start a new round after a player wins, or end the game if a player has 500 points
	 */
	public void nextRound() {
		System.out.println(Arrays.toString(points));
		// check if someone won the game
		for (int i = 0; i < players.length; i++) {
			if (points[i] >= 500) {
				gameOver = true;
				// do something for player that won
				return;
			}
		}
		// set up next round
		reset();
		setRandomPlayer();
	}

	/**
	 * Start the game with all players at 0 points
	 */
	public void startGame() {
		// initialize game data
		gameOver = false;
		Arrays.fill(points, 0);
		reset();
		setRandomPlayer();
		// take turns until the game is over
		while (!gameOver) {
			nextPlayer();
		}
	}

	/**
	 * Set a random player to be the current player
	 */
	public void setRandomPlayer() {
		currentPlayerIndex = (int)(Math.random() * players.length);
	}

	/**
	 * Runs the game
	 * @param args not used
	 */
	public static void main(String[] args) {
		Game game = new Game(3);
		game.startGame();
	}

}
