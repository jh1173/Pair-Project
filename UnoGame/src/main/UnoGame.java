package main;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.*;

/**
 * The Uno game, and the graphical user interface
 * The latest version
 */
public class UnoGame{

	@SuppressWarnings("unused")
	private static final long serialVersionUID = -6656762535162866431L;
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
	private static int computerMoveTime = 3000;
	//private String path = "Uno_Cards/";
	
	// GUI elements
	private JTextArea playerText = new JTextArea();
	
	//private JLayeredPane playArea = getLayeredPane();
	private JPanel playArea = new JPanel();
	
	/*
	private int xPlayAreAdjustment = 0;
	private int yPlayAreaAdjustment = 2;
	private int currentPlayAreaStartPosX = 230;
	private int currentPlayAreaStartPosY = 180;
	
	private int xHandAreAdjustment = 5;
	private int yHandAreaAdjustment = 5;
	private int currentHandAreaStartPosX = 5;
	private int currentHandAreaStartPosY = 5;
	
	private int handAreaCounter = 0;
	*/
	
	private JLabel deckAreaFooter = new JLabel();
	/** card panel for player's hand */
	private CardPanel handAreaPanel = new CardPanel(0.35);
	private JScrollPane handPane = new JScrollPane(handAreaPanel);
	/** card panel for pile */
	private CardPanel pilePanel = new CardPanel(0.6);
	//private JLabel handArealabel[] = new JLabel[200];
	//private int jlabelNumber = 0;
	private Color color;
    
	//private Card cardStart;
	
	/** button for continuing (or drawing cards) */
	private JButton continueButton = new JButton("Draw Card");
	private JLabel playInfo = new JLabel(" ");
	
	JFrame frame = new JFrame("UNO Game");
	JFrame newGameFrame;
	

	/**
	 * Create a game with the specified number of players
	 * @param numPlayers
	 */
	public UnoGame(int numPlayers, JFrame newGameFrame) {
		
		this.newGameFrame = newGameFrame;
		handAreaPanel.addMouseListener(handAreaPanel.new CardClickListener() {
			@Override
			public boolean clickIsEnabled() {
				return UnoGame.this.currentPlayerIsHuman();
			}
			@Override
			public boolean cardIsPlayable(Card card) {
				return UnoGame.this.cardIsPlayable(card);
			}
			@Override
			public void play(Card card) {
				UnoGame.this.play(card);				
			}
		});
		// set up deck and pile for games
		deck = new Deck();
		pile = new Pile(deck);
		deck.addWildDraw4s();
		
		// initialize GUI
		JPanel northPanel = new JPanel(new BorderLayout(8,8));
		northPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		
		JLabel gameHeader = new JLabel("Welcome to UNO Game !!!",JLabel.CENTER);
		gameHeader.setVerticalAlignment(JLabel.TOP);
		gameHeader.setFont(new Font("Verdana", Font.BOLD, 32));
		gameHeader.setForeground(Color.RED);
		northPanel.add(gameHeader, BorderLayout.CENTER);
		northPanel.setBackground(Color.CYAN);
		
		
		//Main West Panel Creating Deck Area (Header & Deck Cards)
		
		//Header Label
		
		JLabel deckAreaHeader = new JLabel("Deck Area",JLabel.CENTER);
		deckAreaHeader.setVerticalAlignment(JLabel.TOP);
		deckAreaHeader.setFont(new Font("Verdana", Font.BOLD, 14));
		deckAreaHeader.setPreferredSize(new Dimension(50, 20));
		deckAreaHeader.setForeground(Color.BLACK);
		
		//Deck cards
		JPanel decCardPanel = new JPanel(new FlowLayout());
		Point origin = new Point(5,5);
		color = Color.GRAY;
		//cardLabel = createCard("UNO",color,origin);
		JLabel cardLabel = new JLabel("<html>UNO<br><br>DECK<br>CARDS</html>");
		cardLabel.setVerticalAlignment(JLabel.CENTER);
		cardLabel.setHorizontalAlignment(JLabel.CENTER);
        cardLabel.setFont(new Font("Verdana", Font.BOLD, 18));
        cardLabel.setOpaque(true);
        cardLabel.setBackground(color);
        cardLabel.setForeground(Color.black);
        cardLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        cardLabel.setBounds(origin.x, origin.y, 150, 200);
		cardLabel.setPreferredSize(new Dimension(150, 200));
		decCardPanel.add(cardLabel);
		
		
		//Footer Label
		String footer = "Cards Left: " + deck.numCards();
		message(footer);
		//JLabel deckAreaFooter = new JLabel(footer,JLabel.CENTER);
		deckAreaFooter.setVerticalAlignment(JLabel.TOP);
		deckAreaFooter.setFont(new Font("Verdana", Font.BOLD, 14));
		deckAreaFooter.setPreferredSize(new Dimension(50, 20));
		deckAreaFooter.setForeground(Color.BLACK);
		
		JPanel westPanel = new JPanel(new BorderLayout(8,8));
	
		westPanel.setBackground(Color.PINK);
		westPanel.setOpaque(true);
		//eastPanel.setSize(50, 50);
		westPanel.add(deckAreaHeader, BorderLayout.NORTH);
		westPanel.add(decCardPanel, BorderLayout.CENTER);
		westPanel.add(deckAreaFooter, BorderLayout.SOUTH);
		//Main South Area holding your Hand
		JPanel handPanel = new JPanel(new FlowLayout());
		
		//Populate Initial Hand Area for Human Player (7 Cards)
		//origin = new Point(5, 5);
		
		
		//c1.remove(0);
		//players[currentPlayerIndex].getHand().getCards().remove(0);
		
		//Create Human Player Hand Header Area
		JPanel southPanel = new JPanel(new BorderLayout(8,8));
		southPanel.setBackground(Color.getHSBColor(0.3f, 0.6f, 0.8f));
		
		
		JLabel handAreaHeader = new JLabel("Your Hand",JLabel.CENTER);
		handAreaHeader.setVerticalAlignment(JLabel.CENTER);
		handAreaHeader.setFont(new Font("Verdana", Font.BOLD, 18));
		handAreaHeader.setPreferredSize(new Dimension(900, 22));
		handAreaHeader.setForeground(Color.RED);
		
		southPanel.add(handAreaHeader, BorderLayout.NORTH);
		southPanel.add(handPanel, BorderLayout.CENTER);
		handPanel.add(handPane, BorderLayout.CENTER);
		handPanel.add(continueButton, BorderLayout.EAST);
		southPanel.add(playInfo, BorderLayout.SOUTH);
		playInfo.setFont(new Font("Verdana", Font.PLAIN, 16));
		handPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		continueButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String ac = e.getActionCommand();
				//System.out.println(ac);
				if (ac.equals("action")) {
					takeAction();
				}
				else if (ac.equals("draw")) {
					draw();
				}
				else if (ac.equals("continue")) {
					nextPlayer();
				}
				else if (ac.equals("next round")) {
					reset();
					setRandomPlayer();
					nextPlayer();
				}
				else if (ac.equals("quit")) {
					newGameFrame.setVisible(true);
					frame.dispose();
				}
			}
		});
		
		//Main East Panel Creating Computer Player Area (Header & Players)
		
		//Header Label
		JLabel computerAreaHeader = new JLabel("Players",JLabel.CENTER);
		computerAreaHeader.setVerticalAlignment(JLabel.TOP);
		computerAreaHeader.setFont(new Font("Verdana", Font.BOLD, 14));
		computerAreaHeader.setPreferredSize(new Dimension(50, 20));
		computerAreaHeader.setForeground(Color.BLACK);
		
		//Player Buttons
		JPanel buttonPanel = new JPanel(new GridLayout(0,1,0,8));
		buttonPanel.setBackground(Color.BLUE);
		buttonPanel.add(playerText);
		playerText.setEditable(false);
		playerText.setFont(new Font("Consolas", Font.BOLD, 16));
		
		/*for (int i = 1; i < numPlayers; i++){
			color = Color.GRAY; 
			String cardValue = "C" + i;
		}*/
		
		JPanel eastPanel = new JPanel(new BorderLayout(8,8));
		eastPanel.setBackground(Color.PINK);
		eastPanel.setOpaque(true);
		eastPanel.add(computerAreaHeader, BorderLayout.NORTH);
		eastPanel.add(buttonPanel, BorderLayout.CENTER);
				
		//Create Main Center Play Area
		JPanel mainCentralArea = new JPanel(new BorderLayout(8,8));
		mainCentralArea.setBackground(Color.ORANGE);
		
		//Create Play Area Header
		JLabel playAreaHeader = new JLabel("Play Table",JLabel.CENTER);
		playAreaHeader.setVerticalAlignment(JLabel.TOP);
		playAreaHeader.setFont(new Font("Verdana", Font.BOLD, 24));
		playAreaHeader.setPreferredSize(new Dimension(800, 30));
		
		mainCentralArea.add(playAreaHeader, BorderLayout.NORTH);
		mainCentralArea.add(playArea, BorderLayout.CENTER);
		playArea.add(pilePanel);
		
		//Main Panel Holding Other Sub Panels
		JPanel outerPanel = new JPanel(new BorderLayout(8,8));
		outerPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		outerPanel.add(eastPanel, BorderLayout.EAST);
		outerPanel.add(northPanel, BorderLayout.NORTH);
		outerPanel.add(mainCentralArea, BorderLayout.CENTER);
		outerPanel.add(westPanel, BorderLayout.WEST);
		outerPanel.add(southPanel, BorderLayout.SOUTH);
		
		Dimension boardSize = new Dimension(900,680);
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//Container c = frame.getContentPane();
		frame.setContentPane(outerPanel);
		frame.setPreferredSize(boardSize);
		//frame.setSize(600,400);
		frame.setLocationByPlatform(true);
		//frame.setResizable( false );
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent e) {}
			public void windowClosing(WindowEvent e) {
				newGameFrame.setVisible(true);
			}
			public void windowClosed(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowActivated(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {}
		});
        
        startGame(numPlayers);
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
		currentPlayerIndex = Math.floorMod(currentPlayerIndex + inc, numPlayers());
	}
	
	/**
	 * determine whether the card is playable
	 * <p>
	 * The card must be:
	 * <li> in the current player's hand
	 * <li> matching the top card of the pile in number or color
	 * <li> if a card has been drawn during the turn: the card that was drawn
	 * </p>
	 * Additionally, the top card of the pile cannot be active.
	 * @param card
	 * @return whether the card is playable
	 */
	public boolean cardIsPlayable(Card card) {
		int cardIndex = currentPlayerHand().getCards().indexOf(card);
		if (!pile.topCard().isActive() && currentPlayerHand().getMatches(pile.topCard()).contains(card)) {
			if (hasDrawnCard) {
				if (cardIndex != currentPlayer().handSize() - 1) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public void startHumanTurn() {
		continueButton.setEnabled(true);
		Card topCard = pile.topCard();
		// if the top card is wild and not assigned a color (unlikely)
		if (topCard.hasColor(main.Color.NONE)) {
			updateDisplay();
			getColorChoice(true);
		}
		// action card
		else if (topCard.isActive()) {
			updateDisplay();
			playInfo.setText("Can't play during this turn");
			continueButton.setText("Take Action");
			continueButton.setActionCommand("action");
		}
		else {
			updateDisplay();
			ArrayList<Card> matches = currentPlayerHand().getMatches(topCard);
			// draw card
			if (matches.size() == 0) {
				playInfo.setText("No playable cards--draw a card");
			}
			else { // play card
				playInfo.setText("Click a playable card to play it");
			}
			continueButton.setText("Draw Card");
			continueButton.setActionCommand("draw");
		}
	}

	/**
	 * Start the current player's turn by determining the action the player should take
	 */
	public void startComputerTurn() {
		Card topCard = pile.topCard();
		// if the top card is wild and not assigned a color (unlikely)
		if (topCard.hasColor(main.Color.NONE)) {
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
				playInfo.setText("Click the last card to play it");
				continueButton.setText("Continue");
				continueButton.setActionCommand("continue");
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
			/*if (currentPlayer().oneCardLeft()) {
				JOptionPane.showMessageDialog(null, "UNO!!!");
			}*/
			// prompt wild card color selection if applicable
			if (card.isWildCard()) {
				if (currentPlayerIsHuman()) {
					updateDisplay();
					getColorChoice(false);
					nextPlayer();
				}
				else {
					main.Color computerColor = ((ComputerPlayer)currentPlayer()).chooseColor();
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
	public void chooseColor(main.Color color) {
		pile.topCard().setColor(color);
	}

	/**
	 * Transfer play to the next player
	 */
	public void nextPlayer() {
		playInfo.setText(" ");
		continueButton.setEnabled(false);
		Card topCard = pile.topCard();
		// if there is a winner
		if (currentPlayer().hasWon()) {
			// handle win, including next player drawing cards if necessary and switching back to this player
			if (topCard.isActive() && (topCard.hasRank(Rank.DRAW_TWO) || topCard.hasRank(Rank.WILD_DRAW_FOUR))) {
				incrementPlayerIndex(playOrder);
				drawAfterWin();
			}
			updateDisplay();
			int thisRoundPoints = 0;
			for (Player player: players) {
				thisRoundPoints += player.getHand().handValue();
			}
			points[currentPlayerIndex] += thisRoundPoints;
			// message for winner and set up prompt to next round
			if (currentPlayerIsHuman()) {
				JOptionPane.showMessageDialog(null, String.format("You won the round and earned %d points!", thisRoundPoints));
			}
			else {
				JOptionPane.showMessageDialog(null, String.format("Player %d won the round and earned %d points!", currentPlayerIndex, thisRoundPoints));
			}
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
				startHumanTurn();
			}
			else {
				updateDisplay();
				// make move after timer ends
				ActionListener al = new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						startComputerTurn();
					}
				};
				javax.swing.Timer timer = new javax.swing.Timer(computerMoveTime, al);
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
		continueButton.setEnabled(true);
		String playerInfo = "";
		for (int i = 0; i < players.length; i++) {
			if (i == currentPlayerIndex) {
				playerInfo += "*";
			}
			else {
				playerInfo += " ";
			}
			playerInfo += Integer.toString(i) + ": ";
			playerInfo += points[i];
			playerInfo += " points\n";
		}
		playerText.setText(playerInfo);
		for (int i = 0; i < numPlayers(); i++) {
			if (points[i] >= 500 && !gameOver) {
				gameOver = true;
				if (currentPlayerIsHuman()) {
					JOptionPane.showMessageDialog(null, "Congratulations! You won the game!");
				}
				else {
					JOptionPane.showMessageDialog(null, String.format("Player %d won the game!", currentPlayerIndex));
				}
				continueButton.setText("Quit");
				continueButton.setActionCommand("quit");
				return;
			}
		}
		continueButton.setText("Next Round");
		continueButton.setActionCommand("next round");
	}
	
	public void message(String msg) {
    	deckAreaFooter.setText(msg);
    	deckAreaFooter.setVerticalAlignment(JLabel.TOP);
		deckAreaFooter.setFont(new Font("Verdana", Font.BOLD, 14));
		deckAreaFooter.setPreferredSize(new Dimension(50, 20));
		deckAreaFooter.setForeground(Color.BLACK);
    }

	/**
	 * Reset the deck, pile, and all players' hands for a new round
	 */
	public void reset() {
		// revert play order
		playOrder = 1;
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
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// deck and pile labels
				String footer = "Cards Left: " + deck.numCards();
				message(footer);
				pilePanel.setCards(pile.topCard());
				// list of players
				String playerInfo = "";
				for (int i = 0; i < players.length; i++) {
					if (i == currentPlayerIndex) {
						playerInfo += "*";
					}
					else {
						playerInfo += " ";
					}
					if (i == 0) {
						playerInfo += "     You: ";
					}
					else {
						playerInfo += "Player " + Integer.toString(i) + ": ";
					}
					int handSize = players[i].handSize();
					
					if (handSize == 1) {
						playerInfo += "UNO!!!\n";
					}
					else {
						playerInfo += Integer.toString(handSize);
						playerInfo += " cards left\n";
					}
				}
				playerText.setText(playerInfo);
				// card panel
				ArrayList<Card> c1;
				c1 = players[0].getHand().getCards();
				Card[] cardArray = c1.toArray(new Card[0]);
				handAreaPanel.setCards(cardArray);
				handPane.repaint();
			}
		});
	}
	
	public void getColorChoice(boolean restartHumanTurn) {	
		JPanel colorPanel = new JPanel();
		ButtonGroup buttonGroup = new ButtonGroup();
        JRadioButton redButton = new JRadioButton("RED");
        JRadioButton blueButton = new JRadioButton("BLUE");
        JRadioButton yellowButton = new JRadioButton("YELLOW");
        JRadioButton greenButton = new JRadioButton("GREEN");
        buttonGroup.add(redButton);
        buttonGroup.add(blueButton);
        buttonGroup.add(yellowButton);
        buttonGroup.add(greenButton);
        colorPanel.add(redButton);
        colorPanel.add(blueButton);
        colorPanel.add(yellowButton);
        colorPanel.add(greenButton);

        JOptionPane.showMessageDialog(null, colorPanel,"Choose a Color", JOptionPane.QUESTION_MESSAGE);
        if(redButton.isSelected()) {
        	chooseColor(main.Color.RED);
        }	
        else if(blueButton.isSelected()) {
        	chooseColor(main.Color.BLUE);
        }	
        else if(yellowButton.isSelected()) {
        	chooseColor(main.Color.YELLOW);
        }	
        else { // green
        	chooseColor(main.Color.GREEN);
        }
        
        if (restartHumanTurn) {
        	startHumanTurn();
        }
	}

	/*
	 * Runs the game
	 * @param args not used
	 
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		UnoGame game = new UnoGame();
	}*/

}
// FIXME test to make sure everything works well