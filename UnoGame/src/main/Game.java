package main;

import java.util.*;
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
	/** time (in milliseconds) each computer player uses to move */
	private static final int COMPUTER_MOVE_TIME = 3000;
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
	private CardPanel pileImage = new CardPanel(0.5);
	private JTextArea playerText = new JTextArea();
	private JTextArea humanPlayerCards = new JTextArea();
	private CardPanel humanCardImages = new CardPanel(0.3);
	private JButton continueButton = new JButton("Continue");
	private JTextField inputField = new JTextField(8);
	private JLabel status = new JLabel();
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
		// game window
		gameWindow.setLayout(new GridBagLayout());
		continueButton.addActionListener(this);
		playerText.setEditable(false);
		humanPlayerCards.setEditable(false);
		c.fill = GridBagConstraints.VERTICAL; c.gridx = 0; c.gridy = 0;
		gameWindow.add(deckLabel, c);
		c.gridy = 1;
		gameWindow.add(pileLabel, c);
		c.gridx = 1;
		gameWindow.add(pileImage, c);
		c.gridx = 0; c.gridy = 2;
		gameWindow.add(playerText, c);
		c.gridy = 3;
		gameWindow.add(humanPlayerCards, c);
		c.gridy = 4;
		gameWindow.add(inputField, c);
		c.gridx = 1;
		gameWindow.add(continueButton, c);
		c.gridx = 0; c.gridy = 5;
		gameWindow.add(status, c);
		c.gridy = 6;
		gameWindow.add(humanCardImages, c);
		// set up deck and pile for games
		deck = new Deck();
		pile = new Pile(deck);
		deck.addWildDraw4s();
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

	/**
	 * @return the number of players
	 */
	public int numPlayers() {
		return players.length;
	}

	/**
	 * Set a random player to be the current player
	 */
	public void setRandomPlayer() {
		currentPlayerIndex = (int)(Math.random() * numPlayers());
	}

	/**
	 * Increment currentPlayerIndex by inc, wrapping around after last player
	 * @param inc the increment of the player index
	 */
	public void incrementPlayerIndex(int inc) {
		// TODO see if argument is necessary
		currentPlayerIndex = Math.floorMod(currentPlayerIndex + inc, numPlayers());
	}

	public void startHumanTurn() {
		inputField.setText("");
		Card topCard = pile.topCard();
		// if the top card is wild and not assigned a color (unlikely)
		if (topCard.hasColor(Color.NONE)) {
			inputField.setEnabled(true);
			updateDisplay();
			// TODO set up listener to execute chooseColor(color);
			status.setText("pick color");
			continueButton.setActionCommand("color continue");
		}
		// action card
		else if (topCard.isActive()) {
			inputField.setEnabled(false);
			updateDisplay();
			// TODO set up listener to execute takeAction();
			status.setText("take action");
			continueButton.setActionCommand("action");
		}
		else {
			inputField.setEnabled(true);
			updateDisplay();
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
			takeAction();
		}
		else {
			ArrayList<Card> matches = currentPlayerHand().getMatches(topCard);
			// draw card
			if (matches.size() == 0) {
				draw();
			}
			// play card
			else {
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
		topCard.setActive(false);
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
		nextPlayer();
	}

	public void drawAfterWin() {
		// get rank of card
		Card topCard = pile.topCard();
		Rank topRank = topCard.getRank();
		topCard.setActive(false);
		// draw two cards
		if (topRank.equals(Rank.DRAW_TWO)) {
			currentPlayer().drawCards(deck, pile, 2);
		}
		// draw four cards (wild draw four)
		else {
			currentPlayer().drawCards(deck, pile, 4);
		}
		// go back 1 player
		incrementPlayerIndex(-1 * playOrder);
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
				updateDisplay();
				// TODO prompt to play(cardToPlay) for the card drawn
				status.setText("play card or continue");
				continueButton.setActionCommand("play/draw");
			}
			else {
				play(cardDrawn);
			}
		}
		else {
			nextPlayer();
		}
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
				// TODO say uno
			}
			// prompt wild card color selection if applicable
			if (card.isWildCard()) {
				if (currentPlayerIsHuman()) {
					updateDisplay();
					// TODO prompt to chooseColor(color)
					status.setText("pick color");
					continueButton.setActionCommand("color next");
				}
				else {
					Color computerColor = ((ComputerPlayer)currentPlayer()).chooseColor();
					chooseColor(computerColor);
					nextPlayer();
				}
			}
			else {
				nextPlayer();
			}
		}

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
		// if there is a winner
		if (currentPlayer().hasWon()) {
			// TODO handle win, including next player drawing cards if necessary and switching back to this player
			if (topCard.isActive() && (topCard.hasRank(Rank.DRAW_TWO) || topCard.hasRank(Rank.WILD_DRAW_FOUR))) {
				incrementPlayerIndex(playOrder);
				drawAfterWin();
			}
			updateDisplay();
			inputField.setEnabled(false);
			continueButton.setEnabled(true);
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
			incrementPlayerIndex(playOrder);
			hasDrawnCard = false;
			if (currentPlayerIsHuman()) {
				continueButton.setEnabled(true);
				startHumanTurn();
			}
			else {
				continueButton.setEnabled(false);
				updateDisplay();
				// make move after timer ends
				ActionListener al = new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						startComputerTurn();
					}
				};
				javax.swing.Timer timer = new javax.swing.Timer(COMPUTER_MOVE_TIME, al);
				timer.setRepeats(false);
				timer.start();
			}
		}
	}

	/**
	 * Start a new round after a player wins, or end the game if a player has 500 points
	 */
	public void nextRound() {
		// check if someone won the game
		for (int i = 0; i < numPlayers(); i++) {
			if (points[i] >= 500 && !gameOver) {
				gameOver = true;
				// TODO do something for player that won
				status.setText(String.format("Player %d wins the game", i));
				continueButton.setEnabled(false);
				// TODO display points on screen
				System.out.println(Arrays.toString(points));
				return;
			}
		}
		// message for winner and set up prompt to next round
		// TODO message for winner
		status.setText(String.format("Player %d wins the round", currentPlayerIndex));
		continueButton.setActionCommand("next round");
		// TODO display points on screen
		System.out.println(Arrays.toString(points));
	}

	/**
	 * Reset the deck, pile, and all players' hands for a new round
	 */
	public void reset() {
		// make deck and pile go back to the way they were before
		deck.hardReset();
		pile.hardReset(deck);
		deck.addWildDraw4s();
		// and fill player hands
		for (Player player: players) {
			if (player != null) {
				player.fillHand(deck, pile);
			}
		}
		// revert play order
		playOrder = 1;
	}

	/**
	 * Start the game with all players at 0 points
	 */
	public void startGame(int numPlayers) {
		// set up players
		players = new Player[numPlayers];
		players[0] = new Player();
		for (int i = 1; i < numPlayers; i++) {
			players[i] = new ComputerPlayer();
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
	 * Update the display to reflect the current state of the game
	 */
	public void updateDisplay() {
		// TODO this is what will change when graphics are updated
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// deck and pile labels
				deckLabel.setText("Deck: " + Integer.toString(deck.numCards()));
				pileLabel.setText("Pile: " + pile.topCard().toString());
				pileImage.setCards(pile.topCard());
				// list of players
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
				// list of cards
				String humanCards = "";
				int cardNum = 1;
				Hand playerHand = players[0].getHand();
				for (int i = 0; i < playerHand.getCards().size(); i++) {
					Card card = playerHand.getCards().get(i);
					if (currentPlayerIsHuman() && inputField.isEnabled() 
							&& !status.getText().contains("color")
							&& playerHand.getMatches(pile.topCard()).contains(card) 
							&& (!hasDrawnCard || i == playerHand.getCards().size() - 1)) {
						humanCards += "->";
					}
					humanCards += Integer.toString(cardNum) + ": ";
					humanCards += card.toString();
					humanCards += "\n";
					cardNum++;
				}
				humanPlayerCards.setText(humanCards);
				Card[] humanCardArray = new Card[playerHand.getCards().size()];
				humanCardImages.setCards(playerHand.getCards().toArray(humanCardArray));
			}
		});
	}

	/**
	 * choose the color of the top card, based on input text
	 * @param inputText
	 * @return whether the color was set successfully
	 */
	public boolean chooseColorFromText(String inputText) {
		inputText = inputText.toLowerCase();
		if (inputText.equals("red")) {
			chooseColor(Color.RED);
		}
		else if (inputText.equals("green")) {
			chooseColor(Color.GREEN);
		}
		else if (inputText.equals("blue")) {
			chooseColor(Color.BLUE);
		}
		else if (inputText.equals("yellow")) {
			chooseColor(Color.YELLOW);
		}
		else {
			return false;
		}
		return true;
	}

	/**
	 * Perform actions
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String ac = e.getActionCommand();
		if (ac.equals("Game Rules")) {

		}
		// start playing (from main screen)
		else if (ac.equals("Play")) {
			frame.setContentPane(gameWindow);
			continueButton.setEnabled(true);
			startGame((Integer)selectPlayers.getValue());
		}
		// execute action
		else if (ac.equals("action")) {
			inputField.setEnabled(true);
			takeAction();
		}
		// set color and continue turn
		else if (ac.equals("color continue")) {
			String inputText = inputField.getText();
			inputField.setText("");
			if (chooseColorFromText(inputText)) {
				startHumanTurn();
			}
		}
		// set color and end turn
		else if (ac.equals("color next")) {
			String inputText = inputField.getText();
			inputField.setText("");
			if (chooseColorFromText(inputText)) {
				nextPlayer();
			}
		}
		// play or draw card
		else if (ac.equals("play/draw")) {
			String inputText = inputField.getText();
			inputField.setText("");
			if (hasDrawnCard && inputText.equals("")) {
				// end turn
				nextPlayer();
			}
			else if (!hasDrawnCard && inputText.equals("")) {
				// draw card
				draw();
			}
			else {
				// play card at inputed index
				int cardIndex;
				// check formatting of integer
				try {
					cardIndex = Integer.parseInt(inputText) - 1;
				} catch (NumberFormatException ex) {return;}
				Card selectedCard;
				if (cardIndex >= 0 && cardIndex < currentPlayerHand().getCards().size()) {
					selectedCard = currentPlayerHand().getCards().get(cardIndex);
				}
				else {return;}
				// only play card if it is allowed
				// (a card that matches the top card, must be the card that was drawn if a card has been drawn)
				if ((hasDrawnCard && cardIndex == currentPlayer().handSize() - 1)
						|| currentPlayerHand().getMatches(pile.topCard()).contains(selectedCard)) {
					play(selectedCard);
				}
			}
		}
		// start next round
		else if (ac.equals("next round")) {
			// set up next round
			reset();
			setRandomPlayer();
			inputField.setEnabled(true);
			nextPlayer();
		}
	}

	/**
	 * Runs the game
	 * @param args not used
	 */
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Game game = new Game();
	}

}
// FIXME better graphics
// FIXME test to make sure everything works well