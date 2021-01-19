package main;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Color;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class playUno extends JFrame {
	
	private static final long serialVersionUID = 7974747661368373107L;
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
	private int currentPlayerIndex = 0;
	/** whether the current player has already drawn a card from the deck */
	private boolean hasDrawnCard;
	/** whether the current player (human) need to take an action */
	private boolean isAction;
	/** The color chosen by the current player (human) */
	private Color colorChoice = Color.GRAY;
	/** whether the game is over */
	private boolean gameOver = false;
	/** time (in milliseconds) each computer player uses to move */
	private static final int COMPUTER_MOVE_TIME = 3000;
	
	//Path to image files
	String path = "Uno_Cards/";
	
	//JLayeredPane playArea;
	JLayeredPane playArea = getLayeredPane();
	int xPlayAreAdjustment = 0;
    int yPlayAreaAdjustment = 2;
	int currentPlayAreaStartPosX = 230;
	int currentPlayAreaStartPosY = 180;
	
	int xHandAreAdjustment = 5;
    int yHandAreaAdjustment = 5;
	int currentHandAreaStartPosX = 5;
	int currentHandAreaStartPosY = 5;
	
	int handAreaCounter = 0;
	
	//JLabel label, label1,label2, label3, cardLabel;
	JLabel deckAreaFooter = new JLabel();
	CardPanel handAreaPanel = new CardPanel(0.25);
	JScrollPane handPane = new JScrollPane(handAreaPanel);
	JLabel handArealabel[] = new JLabel[200];
    int jlabelNumber = 0;
    Color color;
    
    //Card cardStart;
    
    int totalPlayer = 10;
    JButton computerPlayer[] = new JButton[20];

	public static void main(String[] args) {
		SwingUtilities.invokeLater( () -> new playUno().startup());
	}

	public void startup() {
		
		//Creation & Initialization of Deck & Pile
		initDeckAndPile();
		
		/////////////
		/**
		 * Start the game with all players at 0 points
		 */
		//public void startGame(int numPlayers) {
			// set up players
			players = new Player[totalPlayer];
			players[0] = new Player();
			for (int i = 1; i < totalPlayer; i++) {
				players[i] = new ComputerPlayer();
			}
			points = new int[totalPlayer];
			// initialize game data
			gameOver = false;
			Arrays.fill(points, 0);
			reset();
			//setRandomPlayer();
			// take turns until the game is over
			//nextPlayer();
		//}
		/////////////
		/**********  Create the Game Board  ****************/
		//Main North/Top Panel Creating Game Header Area

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
		String footer = "Card Left: " + deck.numCards();
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
		origin = new Point(5, 5);
		ArrayList<Card> c1;
		c1 = players[0].getHand().getCards();
		//c1.remove(0);
		//players[currentPlayerIndex].getHand().getCards().remove(0);
		for(int i = 0; i < c1.size(); i++) {  
			color = Color.GRAY;
		    handAreaPlaceCard(i, c1.get(i), c1.get(i).toString(), color, origin, handPanel);
		}	
        
		//Create Human Player Hand Header Area
		JPanel southPanel = new JPanel(new BorderLayout(8,8));
		southPanel.setBackground(Color.GREEN);
		
		JLabel handAreaHeader = new JLabel("Your Hand",JLabel.CENTER);
		handAreaHeader.setVerticalAlignment(JLabel.CENTER);
		handAreaHeader.setFont(new Font("Verdana", Font.BOLD, 18));
		handAreaHeader.setPreferredSize(new Dimension(900, 22));
		handAreaHeader.setForeground(Color.RED);
		
		southPanel.add(handAreaHeader, BorderLayout.NORTH);
		southPanel.add(handPanel, BorderLayout.CENTER);
	
		
		//Main East Panel Creating Computer Player Area (Header & Players)
		
		//Header Label
		JLabel computerAreaHeader = new JLabel("Computer Players",JLabel.CENTER);
		computerAreaHeader.setVerticalAlignment(JLabel.TOP);
		computerAreaHeader.setFont(new Font("Verdana", Font.BOLD, 14));
		computerAreaHeader.setPreferredSize(new Dimension(50, 20));
		computerAreaHeader.setForeground(Color.BLACK);
		
		//Player Buttons
		JPanel buttonPanel = new JPanel(new GridLayout(0,1,0,8));
		buttonPanel.setBackground(Color.BLUE);
		
		for (int i = 1; i< totalPlayer; i++){
			color = Color.GRAY; 
			String cardValue = "C" + i;
			computerAreaCreatePlayer(i, cardValue, color, origin, buttonPanel);
		}
		
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
		
		//Main Panel Holding Other Sub Panels
		JPanel outerPanel = new JPanel(new BorderLayout(8,8));
		outerPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		outerPanel.add(eastPanel, BorderLayout.EAST);
		outerPanel.add(northPanel, BorderLayout.NORTH);
		outerPanel.add(mainCentralArea, BorderLayout.CENTER);
		outerPanel.add(westPanel, BorderLayout.WEST);
		outerPanel.add(southPanel, BorderLayout.SOUTH);
		
		Dimension boardSize = new Dimension(900,680);
		
		JFrame frame = new JFrame("UNO Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = frame.getContentPane( );
		frame.setContentPane(outerPanel);
		frame.setPreferredSize(boardSize);
		//frame.setSize(600,400);
		frame.setLocationByPlatform(true);
		//frame.setResizable( false );
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
	}
	
	private void initDeckAndPile() {
		// Create up deck and pile for games
		// Move first card from deck to pile
		
		deck = new Deck();
		pile = new Pile(deck);
		deck.addWildDraw4s();
	}

	private void computerAreaCreatePlayer(int i, String string, Color color, Point origin, JPanel buttonPanel) {
		
		computerPlayer[i] = new JButton("Computer Player " + i);
		
		computerPlayer[i].addActionListener(new ActionListener() {          
		    public void actionPerformed(ActionEvent e) {   
		    	
		    	// Check for valid move of the card
		    	
		    	
		    	
		    	// Move the top card to Play Area for that player
		    	Card card = players[i].getHand().getCards().get(0);       
                playCard(card.toString());
                pile.addCard(card);
                System.out.println("computer " + card.toString());
                
              //Remove Card from Computer player Hand
                players[i].getHand().removeCard(card);
                
                if (players[i].handSize() == 0) {
                	String msg = "Congratulations!!! " +  computerPlayer[i].getText() + " won the Game";
                 JOptionPane.showMessageDialog(null, msg);
               } 
		    }
		}); 
		
		buttonPanel.add(computerPlayer[i]);
		
	}
	
	private void handAreaPlaceCard(int cardNumber, Card card, String cardFace, Color color, Point origin, Container handPanel) {
		
		String imageFile = path + cardFace + ".png";
		handArealabel[cardNumber] = createCardImage(imageFile, origin);
		handArealabel[cardNumber].setPreferredSize(new Dimension(60, 80));
		handPanel.add(handArealabel[cardNumber]);
		//System.out.println("hand Area ... " +  pile.numCards() + "  " + pile.topCard().toString());        
        handArealabel[cardNumber].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	if (validateHumanTurn()) {        
            			//System.out.println("Play/Draw ... " +  pile.numCards() + "  " + pile.topCard().getRank());
	            		ArrayList<Card> matches = currentPlayerHand().getMatches(pile.topCard());
	        			// draw card
	        			if (matches.size() == 0) {
	        				JOptionPane.showMessageDialog(null, "<html>No valid card to move !!! <br> System is drawing card for you ...</html>");
	        				Card cardNew = drawCard(deck, pile);
	        				int counter = players[0].handSize();
		    			    handAreaPlaceCard(counter, cardNew, cardNew.toString(), color, origin, handPanel);
		    			    Container parent = handArealabel[counter].getParent();
		    			    String footer = "Card Left: " + deck.numCards();
		    				message(footer);
		    			    parent.validate();
			                parent.repaint();
			                //hasDrawnCard = true;
	        				//hasDrawnCard = false;
	        			} else {      				
	        				// Move the clicked card to Play Area after checking validity of the move
	        				if (card.matches(pile.topCard())) {
	        					playCard(cardFace);
				                pile.addCard(card);
				              //Remove Card from Hand Area Panel after moved to Play Area
				                Container parent = handArealabel[cardNumber].getParent();
				                parent.remove(handArealabel[cardNumber]);
				                parent.validate();
				                parent.repaint();

				                if (card.hasRank(Rank.WILD)) {
				                	getColorChoice();
				                	putColorChangeCard();
				                }
				                //System.out.println("human move "  +  pile.numCards() + "  " + card.toString());
				                //Remove Card from Human player Hand
				                players[currentPlayerIndex].getHand().removeCard(card);    
				                
				                if (currentPlayer().oneCardLeft()) {
				                	JOptionPane.showMessageDialog(null, "UNO!!!");
				    			}
				                
				                if (players[currentPlayerIndex].handSize() == 0) {
				                	JOptionPane.showMessageDialog(null, "Congratulations!!! You won the game!");
				                }
				                
	        				} else {
	        					// Move the wild_four card to Play Area only possible.
	        					if (card.hasRank(Rank.WILD_DRAW_FOUR)) {
    	        					playCard(cardFace);
    				                pile.addCard(card);
    				              //Remove Card from Hand Area after moved to Play Area
    				                Container parent = handArealabel[cardNumber].getParent();
    				                parent.remove(handArealabel[cardNumber]);
    				                parent.validate();
    				                parent.repaint();
    				                //Change Color
    				                getColorChoice();
    				                putColorChangeCard();
    				                //System.out.println("human move "  +  pile.numCards() + "  " + card.toString());
    				                //Remove Card from Human player Hand
    				                players[currentPlayerIndex].getHand().removeCard(card);    
    				                
    				                if (currentPlayer().oneCardLeft()) {
    				                	JOptionPane.showMessageDialog(null, "UNO!!!");
    				    			}
    				                
    				                if (players[currentPlayerIndex].handSize() == 0) {
    				                	JOptionPane.showMessageDialog(null, "Congratulations!!! You won the game!");
    				                }
	        					} else {
	        						JOptionPane.showMessageDialog(null, "<html>You cannot play that card!<br>Try Again</html>");
	        					}
	        				}
	        			}  	
	            } else {
	            	if (isAction) {
	            		int noOfCard = takeAction();
	            		for(int i = 0; i < noOfCard; i++) {
	            			Card cardNew = drawCard(deck, pile);
		    			    handAreaPlaceCard(players[currentPlayerIndex].handSize(), cardNew, cardNew.toString(), color, origin, handPanel);
		    			    Container parent = handArealabel[cardNumber].getParent();
		    			    String footer = "Card Left: " + deck.numCards();
		    				message(footer);
		    			    parent.validate();
			                parent.repaint();
	            		}
	            		isAction = false;
	            	} else 
	            		putColorChangeCard();          	
	            	}      	
            }

			private void putColorChangeCard() {
				if (!colorChoice.equals(Color.GRAY))
    			{   
    		        Point origin = new Point(currentPlayAreaStartPosX, currentPlayAreaStartPosY); 
            		JLabel cardLabel = new JLabel("<html><br>New<br>Color</html>",JLabel.CENTER);
            		cardLabel.setVerticalAlignment(JLabel.CENTER);
            		cardLabel.setHorizontalAlignment(JLabel.CENTER);
                    cardLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
                    cardLabel.setOpaque(true);
                    cardLabel.setBackground(colorChoice);
                    cardLabel.setForeground(Color.black);
                    cardLabel.setBorder(BorderFactory.createLineBorder(Color.black));
                    cardLabel.setBounds(origin.x, origin.y, 60, 80);
            		cardLabel.setPreferredSize(new Dimension(60, 80));
            		
            		currentPlayAreaStartPosX += xPlayAreAdjustment;
                    currentPlayAreaStartPosY -= yPlayAreaAdjustment;
                    origin = new Point(currentPlayAreaStartPosX, currentPlayAreaStartPosY); 
                    
                    // Place the card on Top.
                    playArea.add(cardLabel, new Integer(++jlabelNumber));
    				colorChoice = Color.GRAY;            		
    			}  				
			}		
        });		
	}

	private void playCard(String cardFace) {
		
		currentPlayAreaStartPosX += xPlayAreAdjustment;
        currentPlayAreaStartPosY -= yPlayAreaAdjustment;
        Point origin = new Point(currentPlayAreaStartPosX, currentPlayAreaStartPosY); 
        
        String imageFile = path + cardFace + ".png";
        JLabel label = createCardImage(imageFile, origin);
        
        // Place the buttons on Top.
        playArea.add(label, new Integer(++jlabelNumber));		
	}
	
  //Create and set up a card image label.
    private JLabel createCardImage(String text, Point origin) {
    	String txt = "img - " + ++handAreaCounter;
    	JLabel label = new JLabel(txt);
    	label.setForeground(color);
		label.setBounds(origin.x, origin.y, 60, 80);
		ImageIcon imageIcon = new ImageIcon(new ImageIcon(text).getImage().getScaledInstance(60, 80, Image.SCALE_DEFAULT));
		label.setIcon(imageIcon);

        return label;
    }
    
    void message(String msg) {
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
		Card card = pile.topCard();
		//System.out.println("Init "  +  pile.numCards() + "  " + card.toString());
		//System.out.println(card.getPointValue());
		//System.out.println(card.getRank());
		//System.out.println(card.getRankIndex());
		//System.out.println(card.getColor());
		//System.out.println(card.toString());
		
		playCard(card.toString());
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
	
	private boolean validateHumanTurn() {
		Card topCard = pile.topCard();
		//System.out.println("inside validate method " +  pile.numCards() + "  " + topCard.toString());
		// if the top card is wild and not assigned a color (unlikely)
		if (topCard.hasColor(main.Color.NONE)) {
			getColorChoice();
			
	        return false;
		}
		// action card
		else if (topCard.isActive()) {

			isAction = true;
			return false;
		}
		else {
				return true;
		}
	 }	
	
	private void getColorChoice() {	
		JPanel colorPanel = new JPanel();
        JRadioButton redButton = new JRadioButton("RED");
        JRadioButton blueButton = new JRadioButton("BLUE");
        JRadioButton yellowButton = new JRadioButton("YELLOW");
        JRadioButton greenButton = new JRadioButton("GREEN");
        colorPanel.add(redButton);
        colorPanel.add(blueButton);
        colorPanel.add(yellowButton);
        colorPanel.add(greenButton);

        JOptionPane.showMessageDialog(null, colorPanel,"Choose a Color", JOptionPane.INFORMATION_MESSAGE);
        if(redButton.isSelected()) {
        	chooseColor(main.Color.RED);
        	colorChoice = Color.RED;
        }	
        if(blueButton.isSelected()) {
        	chooseColor(main.Color.BLUE);
        	colorChoice = Color.BLUE;
        }	
        if(yellowButton.isSelected()) {
        	chooseColor(main.Color.YELLOW);
        	colorChoice = Color.YELLOW;
        }	
        if(greenButton.isSelected()) {
        	chooseColor(main.Color.GREEN);
        	colorChoice = Color.GREEN;
        }	
	}

	/**
	 * The current player draws a card from the deck
	 */
	public void draw() {
		Card cardDrawn = currentPlayer().drawCard(deck, pile);
		//hasDrawnCard = true;
		// ask player whether to play card if playable
		/*ArrayList<Card> matches = currentPlayerHand().getMatches(pile.topCard());
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
		}*/
	}
	
	/**
	 * draw a card from the deck and add it to the hand
	 * @param deck the deck from which the card was drawn
	 * @param pile the pile onto which cards will be played (in case the deck needs to reset)
	 * @return the card that was drawn
	 */
	 public Card drawCard(Deck deck, Pile pile) {
		Card card = deck.deal();
		players[currentPlayerIndex].getHand().addCard(card);
		if (deck.isEmpty()) {
			deck.reset(pile);
		}
		hasDrawnCard = true;
		return card;
	 }
	
	 /**
	 * The current player sets the color of a wild card that has not yet been assigned a color
	 */
	public void chooseColor(main.Color color) {
		pile.topCard().setColor(color);
	}
	
	/**
	 * The current player acts on an action card, the action card is deactivated
	 */
	public int takeAction() {
		int noOfCard;
		// get rank of card
		Card topCard = pile.topCard();
		Rank topRank = topCard.getRank();
		topCard.setActive(false);
		// draw two cards
		if (topRank.equals(Rank.DRAW_TWO)) {
			//currentPlayer().drawCards(deck, pile, 2);
			noOfCard = 2;
		}
		// draw four cards
		else if (topRank.equals(Rank.WILD_DRAW_FOUR)) {
			//currentPlayer().drawCards(deck, pile, 4);
			noOfCard = 4;
		}
		// skip turn
		else {
			// TODO handle skips (reverse acts like skip in 2 player game)
			noOfCard = 0;
		}
		//nextPlayer();
		return noOfCard;
	}
    ////////////////////

}
