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
	// GUI elements
	private JFrame frame;
	private JPanel contentPane;
	private JLabel welcome;
	private JButton howToPlayUno;
	private JButton gameRules;
	private JButton play;
	private JSpinner selectPlayers;
	// TODO game window: text-only prototype
	private JPanel gameWindow = new JPanel();
	private JLabel deckLabel = new JLabel();
	private JLabel pileLabel = new JLabel();
	private JTextArea playerText = new JTextArea();
	private JTextArea humanPlayerCards = new JTextArea();
	private JButton continueButton = new JButton("Continue");
	private JTextField inputField = new JTextField(8);
	private JLabel status = new JLabel();
	private JButton quitButton = new JButton("Quit");
	// TODO add GUI elements, player interface

	/**
	 * Create a game with the specified number of players
	 * @param numPlayers
	 */
	public Game() {
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
		gameRules = new JButton("Game Rules");
		gameRules.setHorizontalAlignment((int) JButton.CENTER_ALIGNMENT);
		gameRules.setVerticalAlignment((int) JButton.CENTER_ALIGNMENT);
		gameRules.addActionListener(this);
		gameRules.setActionCommand("Game Rules");
		play = new JButton("Play");
		play.setHorizontalAlignment((int) JButton.CENTER_ALIGNMENT);
		play.setVerticalAlignment((int) JButton.CENTER_ALIGNMENT);
		play.addActionListener(this);
		play.setActionCommand("Play");
		SpinnerModel spinnerModel = new SpinnerNumberModel(2, 2, 10, 1);
		selectPlayers = new JSpinner(spinnerModel);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 3; c.gridx = 0; c.gridy = 0;
		contentPane.add(welcome, c);
		c.gridwidth = 1; c.gridy = 1;
		contentPane.add(howToPlayUno, c);
		c.gridx = 1;
		contentPane.add(gameRules, c);
		c.gridx = 2;
        contentPane.add(play, c);
        c.gridy = 2;
        contentPane.add(selectPlayers, c);
		frame.setContentPane(contentPane);
		frame.setSize(700, 700);
		frame.setVisible(true);
		continueButton.addActionListener(this);
		c.fill = GridBagConstraints.VERTICAL; c.gridx = 0; c.gridy = 0;
		gameWindow.add(deckLabel, c);
		c.gridy = 1;
		gameWindow.add(pileLabel, c);
		c.gridy = 2;
		gameWindow.add(playerText, c);
		c.gridy = 3;
		gameWindow.add(humanPlayerCards, c);
		c.gridy = 4;
		gameWindow.add(inputField);
		c.gridx = 1;
		gameWindow.add(continueButton);
		c.gridx = 0; c.gridy = 5;
		gameWindow.add(status);
		// set up deck and pile for games
		deck = new Deck();
		pile = new Pile(deck);
		deck.addWildDraw4s();
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
	
	public int numPlayers() {
		return players.length;
	}
	
	public void startHumanTurn() {
		Card topCard = pile.topCard();
		// if the top card is wild and not assigned a color (unlikely)
		if (topCard.hasColor(Color.NONE)) {
			// TODO set up listener to execute chooseColor(color);
			status.setText("pick color");
			continueButton.setActionCommand("color");
		}
		// action card
		if (topCard.isActive()) {
			// TODO set up listener to execute takeAction();
			status.setText("take action");
			continueButton.setActionCommand("action");
		}
		else {
			ArrayList<Card> matches = currentPlayerHand().getMatches(topCard);
			// draw card
			if (matches.size() == 0) {
				// TODO set up listener to execute draw();
				status.setText("draw card");
				continueButton.setActionCommand("play/draw");
			}
			// play card
			else {
				// TODO set up listener to execute play(cardToPlay);
				status.setText("play card");
				continueButton.setActionCommand("play/draw");
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
			//try {Thread.sleep(1000);}
			//catch (InterruptedException ex) {}
			takeAction();
		}
		else {
			ArrayList<Card> matches = currentPlayerHand().getMatches(topCard);
			// draw card
			if (matches.size() == 0) {
				//try {Thread.sleep(1000);}
				//catch (InterruptedException ex) {}
				draw();
			}
			// play card
			else {
				//try {Thread.sleep(1000);}
				//catch (InterruptedException ex) {}
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
			// TODO handle skips (reverse acts like skip in 2 player game)
		}
		topCard.setActive(false);
		nextPlayer();
	}

	/**
	 * The current player draws a card from the deck
	 */
	public void draw() {
		currentPlayer().drawCard(deck, pile);
		ArrayList<Card> playerCards = currentPlayer().getHand().getCards();
		Card cardDrawn = playerCards.get(playerCards.size() - 1);
		hasDrawnCard = true;
		updateDisplay();
		// ask player whether to play card if playable
		ArrayList<Card> matches = currentPlayerHand().getMatches(pile.topCard());
		if (matches.contains(cardDrawn)) {
			if (currentPlayerIsHuman()) {
				// TODO prompt to play(cardToPlay) for the card drawn
				status.setText("play card or continue");
				continueButton.setActionCommand("play/draw");
				return;
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
			updateDisplay();
			if (currentPlayer().oneCardLeft()) {
				// TODO say uno
			}
			// prompt wild card color selection if applicable
			if (card.isWildCard()) {
				if (currentPlayerIsHuman()) {
					// TODO prompt to chooseColor(color)
					status.setText("pick color");
					continueButton.setActionCommand("color next");
					return;
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
		updateDisplay();
		Card topCard = pile.topCard();
		if (currentPlayer().hasWon()) {
			// handle win, including next player drawing cards if necessary and switching back to this player
			// TODO message for winner
			int thisRoundPoints = 0;
			for (Player player: players) {
				thisRoundPoints += player.getHand().handValue();
			}
			points[currentPlayerIndex] += thisRoundPoints;
			nextRound();
		}
		else {
			// reverse card
			if (topCard.hasRank(Rank.REVERSE) && topCard.isActive() && numPlayers() != 2) {
				playOrder *= -1;
				topCard.setActive(false);
			}
			// switch to next player
			currentPlayerIndex = Math.floorMod(currentPlayerIndex + playOrder, numPlayers());
			hasDrawnCard = false;
			updateDisplay();
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
		for (int i = 0; i < numPlayers(); i++) {
			if (points[i] >= 500) {
				gameOver = true;
				// TODO do something for player that won
				return;
			}
		}
		// set up next round
		reset();
		setRandomPlayer();
		// FIXME replace with setup to event listener (avoid stack overflow)
		nextPlayer();
	}

	/**
	 * Start the game with all players at 0 points
	 */
	public void startGame(int numPlayers) {
		// set up players
		players = new Player[numPlayers];
		players[0] = new Player(deck, pile);
		for (int i = 1; i < numPlayers; i++) {
			players[i] = new ComputerPlayer(deck, pile);
		}
		points = new int[numPlayers];
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
		currentPlayerIndex = (int)(Math.random() * numPlayers());
	}
	
	/**
	 * Update the display to reflect the current state of the game
	 */
	public void updateDisplay() {
		deckLabel.setText("Deck: " + Integer.toString(deck.numCards()));
		pileLabel.setText("Pile: " + pile.topCard().toString());
		String playerInfo = "";
		for (int i = 0; i < players.length; i++) {
			if (i == currentPlayerIndex) {
				playerInfo += "*";
			}
			playerInfo += Integer.toString(i) + ": ";
			playerInfo += players[i].handSize();
			playerInfo += " cards left\n";
		}
		playerText.setText(playerInfo);
		String humanCards = "";
		int cardNum = 1;
		Hand playerHand = players[0].getHand();
		for (int i = 0; i < playerHand.getCards().size(); i++) {
			Card card = playerHand.getCards().get(i);
			if (playerHand.getMatches(pile.topCard()).contains(card)) {
				if (!hasDrawnCard || i == playerHand.getCards().size() - 1) {
					humanCards += "->";
				}
			}
			humanCards += Integer.toString(cardNum) + ": ";
			humanCards += card.toString();
			humanCards += "\n";
			cardNum++;
		}
		humanPlayerCards.setText(humanCards);
	}
	
	/**
	 * Perform actions
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String ac = e.getActionCommand();
		if (ac.equals("Game Rules")) {
			
		}
		else if (ac.equals("Play")) {
			frame.setContentPane(gameWindow);
			startGame((Integer)selectPlayers.getValue());
		}
		else if (ac.equals("action")) {
			takeAction();
		}
		else if (ac.equals("color")) {
			String inputText = inputField.getText();
			inputField.setText("");
			if (inputText.toLowerCase().equals("red")) {
				chooseColor(Color.RED);
			}
			else if (inputText.toLowerCase().equals("green")) {
				chooseColor(Color.GREEN);
			}
			else if (inputText.toLowerCase().equals("blue")) {
				chooseColor(Color.BLUE);
			}
			else if (inputText.toLowerCase().equals("yellow")) {
				chooseColor(Color.YELLOW);
			}
		}
		else if (ac.equals("color next")) {
			String inputText = inputField.getText();
			inputField.setText("");
			if (inputText.toLowerCase().equals("red")) {
				chooseColor(Color.RED);
			}
			else if (inputText.toLowerCase().equals("green")) {
				chooseColor(Color.GREEN);
			}
			else if (inputText.toLowerCase().equals("blue")) {
				chooseColor(Color.BLUE);
			}
			else if (inputText.toLowerCase().equals("yellow")) {
				chooseColor(Color.YELLOW);
			}
			else {
				return;
			}
			nextPlayer();
		}
		else if (ac.equals("play/draw")) {
			String inputText = inputField.getText();
			inputField.setText("");
			if (hasDrawnCard && inputText.equals("")) {
				nextPlayer();
			}
			else if (!hasDrawnCard && inputText.equals("")) {
				draw();
			}
			else {
				int cardIndex = -1;
				try {
					cardIndex = Integer.parseInt(inputText) - 1;
				} catch (NumberFormatException ex) {return;}
				Card selectedCard = currentPlayerHand().getCards().get(cardIndex);
				if (hasDrawnCard && cardIndex == currentPlayer().handSize() - 1) {
					play(selectedCard);
				}
				else if (currentPlayerHand().getMatches(pile.topCard()).contains(selectedCard)) {
					play(selectedCard);
				}
			}
		}
	}

	/**
	 * Runs the game
	 * @param args not used
	 */
	public static void main(String[] args) {
		Game game = new Game();
	}

}
// FIXME better graphics
// FIXME see what computer players are doing