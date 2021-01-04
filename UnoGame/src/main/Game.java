package main;

import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The Uno game, and the graphical user interface
 */
public class Game implements ActionListener{

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
	/** whether the human player is in the process of taking a turn */
	private boolean takingTurn = false;
	// GUI elements
	private JFrame frame;
	private JPanel contentPane;
	private JLabel welcome;
	private JButton howToPlayUno;
	private JButton gameRules;
	private JButton play;
	// TODO add GUI elements, player interface

	/**
	 * Create a game with the specified number of players
	 * @param numPlayers
	 */
	public Game(int numPlayers) {
		// initialize GUI
		frame = new JFrame("Welcome"); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel(); 
		contentPane.setLayout(new GridBagLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		welcome = new JLabel("Welcome");
		welcome.setHorizontalAlignment((int) JLabel.CENTER_ALIGNMENT);
		welcome.setVerticalAlignment((int) JLabel.CENTER_ALIGNMENT);
		howToPlayUno = new JButton("How to Play Uno");
		howToPlayUno.setHorizontalAlignment((int) JButton.CENTER_ALIGNMENT);
		howToPlayUno.setVerticalAlignment((int) JButton.CENTER_ALIGNMENT);
		gameRules = new JButton(" Game Rules");
		gameRules.setHorizontalAlignment((int) JButton.CENTER_ALIGNMENT);
		gameRules.setVerticalAlignment((int) JButton.CENTER_ALIGNMENT);
		play = new JButton("Play");
		play.setHorizontalAlignment((int) JButton.CENTER_ALIGNMENT);
		play.setVerticalAlignment((int) JButton.CENTER_ALIGNMENT);
		GridBagConstraints welcome1 = new GridBagConstraints();
		welcome1.fill = GridBagConstraints.HORIZONTAL;
		welcome1.gridwidth = 3;
		welcome1.gridx = 0;
		welcome1.gridy = 0;
		GridBagConstraints howToPlayUno1 = new GridBagConstraints();
		howToPlayUno1.fill = GridBagConstraints.HORIZONTAL;
		howToPlayUno1.gridx = 0;
		howToPlayUno1.gridy = 1;
		GridBagConstraints gameRules1 = new GridBagConstraints();
		gameRules1.fill = GridBagConstraints.HORIZONTAL;
		gameRules1.gridx = 1;
		gameRules1.gridy = 1;
		GridBagConstraints play1 = new GridBagConstraints();
		play1.fill = GridBagConstraints.HORIZONTAL;
		play1.gridx = 2;
		play1.gridy = 1;
		contentPane.add(welcome, welcome1);
        contentPane.add(howToPlayUno, howToPlayUno1);
        contentPane.add(gameRules, gameRules1);
        contentPane.add(play, play1);
		frame.setContentPane(contentPane);
		frame.setSize(700, 700);
		frame.setVisible(true);
		// initialize game
		// set up deck and pile
		deck = new Deck();
		pile = new Pile(deck);
		deck.addWildDraw4s();
		// set up players
		players = new Player[numPlayers];
		players[0] = new Player(deck, pile);
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
		return currentPlayerIndex == 0;
	}
	
	public void startHumanTurn() {
		Card topCard = pile.topCard();
		// if the top card is wild and not assigned a color (unlikely)
		if (topCard.hasColor(Color.NONE)) {
			// set up listener to execute chooseColor(color);
		}
		// action card
		if (topCard.isActive()) {
			// set up listener to execute takeAction();
		}
		else {
			ArrayList<Card> matches = currentPlayerHand().getMatches(topCard);
			// draw card
			if (matches.size() == 0) {
				// set up listener to execute draw();
			}
			// play card
			else {
				// set up listener to execute play(cardToPlay);
			}
		}
	}

	/**
	 * Start the current player's turn by determining the action the player should take
	 */
	public void startComputerTurn() {
		Card topCard = pile.topCard();
		// if the top card is wild and not assigned a color (unlikely)
		if (topCard.hasColor(Color.NONE)) {
			chooseColor(((ComputerPlayer)currentPlayer()).chooseColor());
		}
		// action card
		if (topCard.isActive()) {
			try {Thread.sleep(1000);}
			catch (InterruptedException ex) {}
			takeAction();
		}
		else {
			ArrayList<Card> matches = currentPlayerHand().getMatches(topCard);
			// draw card
			if (matches.size() == 0) {
				try {Thread.sleep(1000);}
				catch (InterruptedException ex) {}
				draw();
			}
			// play card
			else {
				try {Thread.sleep(1000);}
				catch (InterruptedException ex) {}
				Card cardToPlay = ((ComputerPlayer)currentPlayer()).chooseCard(topCard);
				play(cardToPlay);
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
			currentPlayer().drawCards(deck, pile, 2);
		}
		// draw four cards
		else if (topRank.equals(Rank.WILD_DRAW_FOUR)) {
			currentPlayer().drawCards(deck, pile, 4);
		}
		// skip turn
		else {
			// handle these (reverse acts like skip in 2 player game)
		}
		topCard.setActive(false);
		nextPlayer();
	}

	/**
	 * The current player draws a card from the deck
	 */
	public void draw() {
		Card cardDrawn = currentPlayer().drawCard(deck, pile);
		hasDrawnCard = true;
		// ask player whether to play card if playable
		ArrayList<Card> matches = currentPlayerHand().getMatches(pile.topCard());
		if (matches.contains(cardDrawn)) {
			if (currentPlayerIsHuman()) {
				// prompt to play(cardToPlay)
			}
			else {
				play(cardDrawn);
			}
		}
		nextPlayer();
	}

	/**
	 * The current player plays the selected card to the pile
	 * @param card the card to be played
	 */
	public void play(Card card) {
		if (card != null) {
			currentPlayer().playCard(card, pile);
			// say uno
			if (currentPlayer().oneCardLeft()) {

			}
			// prompt wild card color selection if applicable
			if (card.isWildCard()) {
				if (currentPlayerIsHuman()) {
					// prompt to chooseColor(color)
				}
				else {
					chooseColor(((ComputerPlayer)currentPlayer()).chooseColor());
				}
			}
		}
		nextPlayer();
	}
	
	/**
	 * The current player sets the color of a wild card that has not yet been assigned a color
	 */
	public void chooseColor(Color color) {
		pile.topCard().setColor(color);
		
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
				
				playOrder *= -1;
				topCard.setActive(false);
			}
			// switch to next player
			currentPlayerIndex = Math.floorMod(currentPlayerIndex + playOrder, players.length);
			hasDrawnCard = false;
			
			if (currentPlayerIsHuman()) {
				startHumanTurn();
			}
			else {
				startComputerTurn();
			}
		}
	}

	/**
	 * Start a new round after a player wins, or end the game if a player has 500 points
	 */
	public void nextRound() {
		
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
		nextPlayer();
	}

	/**
	 * Set a random player to be the current player
	 */
	public void setRandomPlayer() {
		currentPlayerIndex = (int)(Math.random() * players.length);
	}
	
	/**
	 * Perform actions
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
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
