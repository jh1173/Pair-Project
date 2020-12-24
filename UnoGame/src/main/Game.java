package main;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The Uno game, and the graphical user interface
 */
public class Game {

	// Set up Deck and Pile
	// Create Players
	// Pick a player to go first
	// The next player gets a turn until someone wins

	// Game elements
	private Deck deck;
	private Pile pile;
	private Player[] players;
	private int[] points;
	/** The direction of play (can be changed by reverse cards)*/
	private int playOrder = 1;
	/** the index of the current player */
	private int currentPlayerIndex;
	/** whether the current player has drawn a card from the deck already*/
	private boolean hasDrawnCard;
	/** whether the game is over */
	private boolean gameOver = false;

	// GUI elements

	public Game(int numPlayers) {
		deck = new Deck();
		pile = new Pile(deck);
		deck.addWildDraw4s();
		players = new Player[numPlayers];
		//players[0] = new Player(deck, pile);
		for (int i = 0; i < numPlayers; i++) {
			players[i] = new ComputerPlayer(deck, pile);
		}
		points = new int[numPlayers];
	}

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

	public Player currentPlayer() {
		return players[currentPlayerIndex];
	}

	public Hand currentPlayerHand() {
		return currentPlayer().getHand();
	}

	public boolean currentPlayerIsHuman() {
		//return currentPlayerIndex == 0;
		return false;
	}

	public void startTurn() {
		Card topCard = pile.topCard();
		if (topCard.isActive()) {
			// action card
			if (currentPlayerIsHuman()) {
				// prompt to takeAction()
			}
			else {
				try {
					Thread.sleep(1000);
				}
				catch (InterruptedException ex) {

				}
				takeAction();
			}

		}
		else {
			ArrayList<Card> matches = currentPlayerHand().getMatches(topCard);
			if (matches.size() == 0) {
				// draw card
				if (currentPlayerIsHuman()) {
					// prompt to drawCard()
				}
				else {
					try {
						Thread.sleep(1000);
					}
					catch (InterruptedException ex) {

					}
					draw();
				}
			}
			else {
				// play card
				if (currentPlayerIsHuman()) {
					// prompt to playCard()
				}
				else {
					try {
						Thread.sleep(1000);
					}
					catch (InterruptedException ex) {

					}
					Card cardToPlay = ((ComputerPlayer)currentPlayer()).chooseCard(topCard);
					play(cardToPlay);
				}
			}
		}
	}

	public void takeAction() {
		Card topCard = pile.topCard();
		if (topCard.isActive()) {
			Rank topRank = topCard.getRank();
			if (topRank.equals(Rank.DRAW_TWO)) {
				System.out.println("draw two");
				currentPlayer().drawCards(deck, pile, 2);
			}
			else if (topRank.equals(Rank.WILD_DRAW_FOUR)) {
				System.out.println("draw four");
				currentPlayer().drawCards(deck, pile, 4);
			}
			else if (topRank.equals(Rank.SKIP) || topRank.equals(Rank.REVERSE)) {
				System.out.println("skip");
				// handle these (reverse acts like skip in 2 player game)
			}
			topCard.setActive(false);
		}
	}

	public void draw() {
		System.out.println("draw");
		currentPlayer().drawCard(deck, pile);
		hasDrawnCard = true;
		// ask player whether to play card if playable
	}

	public void play(Card card) {
		System.out.println("play");
		currentPlayer().playCard(card, pile);
		// say uno
		// prompt wild card color selection if applicable
		if (card.isWildCard()) {
			if (currentPlayerIsHuman()) {

			}
			else {
				pile.topCard().setColor(((ComputerPlayer)currentPlayer()).chooseColor());
				System.out.println("set color");
			}
		}
	}

	/**
	 * 
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
			if (topCard.hasRank(Rank.REVERSE) && topCard.isActive() && players.length != 2) {
				System.out.println("reverse");
				playOrder *= -1;
				topCard.setActive(false);
			}
			currentPlayerIndex = Math.floorMod(currentPlayerIndex + playOrder, players.length);
			hasDrawnCard = false;
			System.out.println(currentPlayerIndex);
			startTurn();
		}
	}

	public void nextRound() {
		System.out.println(Arrays.toString(points));
		for (int i = 0; i < players.length; i++) {
			if (points[i] >= 500) {
				gameOver = true;
				// do something for player that won
				return;
			}
		}
		reset();
		setRandomPlayer();
	}

	public void startGame() {
		gameOver = false;
		Arrays.fill(points, 0);
		reset();
		setRandomPlayer();
		while (!gameOver) {
			nextPlayer();
		}
	}

	public void setRandomPlayer() {
		currentPlayerIndex = (int)(Math.random() * players.length);
	}

	public static void main(String[] args) {
		Game game = new Game(3);
		game.startGame();
	}

}
