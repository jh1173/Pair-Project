package main;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Color;
import java.io.File;
import javax.swing.*;

public class playUno extends JFrame {
	
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
	/** The color choosen by he current player (human) */
	private Color colorChoice = Color.GRAY;
	/** whether the game is over */
	private boolean gameOver = false;
	/** time (in milliseconds) each computer player uses to move */
	private static final int COMPUTER_MOVE_TIME = 3000;
	int totalPlayer = 2;
	
	//Path to image files
	String path = "/Users/km/Desktop/Kalpak/Pair-Project-main/UnoGame/src/main/Uno_Cards/";
	//String path = "../../Uno_Cards/";
	//JLayeredPane playArea;
	JLayeredPane playArea = getLayeredPane( );
	int xPlayAreAdjustment = 0;
    int yPlayAreaAdjustment = 2;
	int currentPlayAreaStartPosX = 250;
	int currentPlayAreaStartPosY = 280;
	
	int xHandAreAdjustment = 5;
    int yHandAreaAdjustment = 5;
	int currentHandAreaStartPosX = 5;
	int currentHandAreaStartPosY = 5;
	
	
	Container parent;
	//JLabel label, label1,label2, label3, cardLabel;
	JLabel deckAreaFooter = new JLabel();
	
	//For Human Hand Area 
	int handAreaCounter = -1;
	JLabel handArealabel[] = new JLabel[108];
	private ArrayList<Card> handAreaCard = new ArrayList<Card>(108);
	
    int jlabelNumber = 0;
    Color color;
    JPanel handPanel, westPanel;
    Card cardStart;
    JFrame frame;
    
    JButton computerPlayer[] = new JButton[10];

	public static void main(String[] args) {
		SwingUtilities.invokeLater( () -> new playUno().startup());
	}

	public void startup() {
		
		//Creation & Initialization of Deck & Pile
		initDeckAndPile();
		
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
		
		westPanel = new JPanel(new BorderLayout(8,8));
	
		westPanel.setBackground(Color.PINK);
		westPanel.setOpaque(true);
		//eastPanel.setSize(50, 50);
		westPanel.add(deckAreaHeader, BorderLayout.NORTH);
		westPanel.add(decCardPanel, BorderLayout.CENTER);
		westPanel.add(deckAreaFooter, BorderLayout.SOUTH);
		
		//Main South Area holding your Hand
		handPanel = new JPanel(new FlowLayout());
		
		//Populate Initial Hand Area for Human Player (7 Cards)
		origin = new Point(5, 5);
		ArrayList<Card> c1;
		c1 = players[0].getHand().getCards();
		//c1.remove(0);
		//players[currentPlayerIndex].getHand().getCards().remove(0);
		for(int i = 0; i < c1.size(); i++) {  
			color = Color.GRAY;
		    handAreaPlaceCard(c1.get(i), color, origin, handPanel);
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
		
		Dimension boardSize = new Dimension(900,700);
		
		frame = new JFrame("UNO Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = frame.getContentPane( );
		frame.setContentPane(outerPanel);
		frame.setPreferredSize(boardSize);
		//frame.setSize(600,400);
		frame.setLocationByPlatform(true);
		//frame.setResizable( false );
        frame.pack();
        frame.setLocationRelativeTo( null );
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
			@Override
		    public void actionPerformed(ActionEvent e) { 
		    	
				/*final Timer t = new Timer(1000, new ActionListener() {
		            @Override
		            public void actionPerformed(ActionEvent evt) {
		            	startComputerTurn();
		            }
		        });
		        t.setRepeats(false);
		        t.start();*/
		    	
		    	startComputerTurn();
		    }
		}); 
		
		buttonPanel.add(computerPlayer[i]);
	}
	
	private void handAreaPlaceCard(Card card, Color color, Point origin, Container handPanel) {
		
		String imageFile = path + card.toString() + ".png";
		handArealabel[++handAreaCounter] = createCardImage(imageFile, origin);
		handArealabel[handAreaCounter].setPreferredSize(new Dimension(60, 80));
		handPanel.add(handArealabel[handAreaCounter]);
		handAreaCard.add(handAreaCounter, card);
		parent = handArealabel[handAreaCounter].getParent();
        handArealabel[handAreaCounter].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {     
            	System.out.println(e.getSource());
            	JLabel labelClicked = (JLabel) e.getSource();
            	Icon icon = labelClicked.getIcon();
            	System.out.println(labelClicked);
            	System.out.println(labelClicked.getName());
            	System.out.println(Integer.parseInt(labelClicked.getName()));
            	Card clickedCard = handAreaCard.get(Integer.parseInt(labelClicked.getName()));
            	        	   
            	if (startHumanTurn()) {     
	            		ArrayList<Card> matches = players[0].getHand().getMatches(pile.topCard());
	        			// draw card
	        			if (matches.size() == 0) {
	        				drawHuman();
	        			} else {      				
	        				// Move the clicked card to Play Area after checking validity of the move
	        				if (clickedCard.matches(pile.topCard()) || (clickedCard.hasRank(Rank.WILD_DRAW_FOUR))) {
	        					playHuman(clickedCard);	                
	        				} else {
	        						JOptionPane.showMessageDialog(null, icon, "Wrong Move ", JOptionPane.INFORMATION_MESSAGE);
	        						//System.out.println(labelClicked.getName());
	        					}
	        				} 	
	            } else {
	            	if (isAction) {
	            		int noOfCard = takeHumanAction();
	            		for(int i = 0; i < noOfCard; i++) {
	            			Card cardNew = drawCard(deck, pile);
		    			    handAreaPlaceCard(cardNew, color, origin, handPanel);
		    			    String footer = "Card Left: " + deck.numCards();
		    				message(footer);
		    			    parent.revalidate();
			                parent.repaint();
			                nextPlayer();
	            		}
	            		isAction = false;
	            	} else 
	            		//playCard(cardFace);
	               
		                //Remove Card from Hand Area after moved to Play Area
	            		removeCardFromHandArea(clickedCard);
	            		putColorChangeCard();  
	            		//nextPlayer();
	            	}     
            }	

			private void drawHuman() {
				JOptionPane.showMessageDialog(null, "<html>No valid card to move !!! <br> System is drawing card for you ...</html>");
				Card cardNew = drawCard(deck, pile);
			    handAreaPlaceCard(cardNew, color, origin, handPanel);
			    //parent = handArealabel[cardNumber].getParent();
			    String footer = "Card Left: " + deck.numCards();
				message(footer);
			    parent.revalidate();
                parent.repaint();
                nextPlayer();		
			}

			private void playHuman(Card clickedCard) {
				if (clickedCard.hasRank(Rank.WILD)) {
					playCard(clickedCard);
					pile.addCard(clickedCard);
	                //Remove Card from Hand Area after moved to Play Area
					removeCardFromHandArea(clickedCard);
			
	                //Remove Card from Human player Hand
	                players[0].getHand().removeCard(clickedCard);  
		        	getColorChoice();
		        	putColorChangeCard();
		        } 
				else if (clickedCard.hasRank(Rank.WILD_DRAW_FOUR)) {
					playCard(clickedCard);
	                pile.addCard(clickedCard);
	               
	                //Remove Card from Hand Area after moved to Play Area
	                removeCardFromHandArea(clickedCard);

	                //Remove Card from Human player Hand
	                players[0].getHand().removeCard(clickedCard);    
	                
	                if (currentPlayer().oneCardLeft()) {
	                	JOptionPane.showMessageDialog(null, "UNO !!!");
	    			}
	                             
	                getColorChoice();
	                putColorChangeCard();
	                nextPlayer();
			} else {
				playCard(clickedCard);
		        pile.addCard(clickedCard);
		        
		        //Remove Card from Hand Area Panel after moved to Play Area
		        removeCardFromHandArea(clickedCard);

		        //Remove Card from Human player Hand
		        players[currentPlayerIndex].getHand().removeCard(card);    
		        
		        if (currentPlayer().oneCardLeft()) {
		        	JOptionPane.showMessageDialog(null, "UNO !!!");
				}
		        
		        nextPlayer();
			}
		 }

			private void putColorChangeCard() {
				if (!colorChoice.equals(Color.GRAY))
    			{   
    		        addColorChangeCard(colorChoice);          		
    			}  				
			}					
        });
	}
	
	private void removeCardFromHandArea(Card clickedcard) {
		parent.remove(handArealabel[handAreaCard.indexOf(clickedcard)]);
        parent.revalidate();
        parent.repaint();
        playArea.repaint();
	}
	
	private void addColorChangeCard(Color colorChange) {
		
		Point origin = new Point(currentPlayAreaStartPosX, currentPlayAreaStartPosY); 
		JLabel cardLabel = new JLabel("<html><br>New<br>Color</html>",JLabel.CENTER);
		cardLabel.setVerticalAlignment(JLabel.CENTER);
		cardLabel.setHorizontalAlignment(JLabel.CENTER);
        cardLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
        cardLabel.setOpaque(true);
        cardLabel.setBackground(colorChange);
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
		playArea.revalidate();
		playArea.repaint();
	}	
	
	private void playCard(Card card) {
		
		currentPlayAreaStartPosX += xPlayAreAdjustment;
        currentPlayAreaStartPosY -= yPlayAreaAdjustment;
        Point origin = new Point(currentPlayAreaStartPosX, currentPlayAreaStartPosY); 
        
        String imageFile = path + card.toString() + ".png";
         
        JLabel label = createCardImage(imageFile, origin);
        
        // Place the buttons on Top.
        playArea.add(label, new Integer(++jlabelNumber));	
        playArea.revalidate();
		playArea.repaint();
	}
	
  //Create and set up a card image label.
    private JLabel createCardImage(String text, Point origin) {
    	String txt = "img_" + handAreaCounter;
    	JLabel label = new JLabel(txt);
    	label.setName(String.valueOf(handAreaCounter));
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
		
		playCard(card);
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
	 * @return the number of players
	 */
	public int computerPlayerIndex() {
		return currentPlayerIndex;
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
		//currentPlayerIndex = Math.floorMod(currentPlayerIndex + inc, numPlayers());
		if (inc > 0) {
			currentPlayerIndex++;
		} else {
			currentPlayerIndex--;
		}
		
		if (currentPlayerIndex == numPlayers()) {
			currentPlayerIndex = 0;
		}
		if (currentPlayerIndex < 0) {
			currentPlayerIndex = numPlayers() - 1;
		}
	}
	
	private boolean startHumanTurn() {
		Card topCard = pile.topCard();
		// if the top card is wild and not assigned a color (unlikely)
		if (topCard.hasColor(main.Color.NONE)) {
			getColorChoice();		
	        return false;
		}
		// action card
		if (topCard.isActive()) {
			isAction = true;
			return false;
		}
		return true;
	 }	
	
	/**
	* Start the computer player's turn by determining the action the player should take
	*/
	public void startComputerTurn() {
		
		//Start Computers turn
		Card topCard = pile.topCard();
		// if the top card is wild and not assigned a color (unlikely)
		if (topCard.hasColor(main.Color.NONE)) {
			setColor(((ComputerPlayer)currentPlayer()).chooseColor());
			// Find Card Color
			Card topCard1 = pile.topCard();
			switch (topCard1.getColor()) {
			    case RED : 
			    	color = Color.RED; 
			    	break;
			    	
			    case BLUE : 
			    	color = Color.BLUE; 
			    	break;
			    	
			    case GREEN : 
			    	color = Color.GREEN; 
			    	break;	
			    	
			    case YELLOW : 
			    	color = Color.YELLOW; 
			    	break;	
			    	
			    case NONE : 
			    	color = Color.GRAY; 
			    	break;	
			}
			addColorChangeCard(color);
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
				String footer = "Card Left: " + deck.numCards();
				message(footer);
				westPanel.revalidate();
                westPanel.repaint();
                nextPlayer();
			}
			// play card
			else {
				Card cardToPlay = ((ComputerPlayer)currentPlayer()).chooseCard(topCard);
				play(cardToPlay);
				nextPlayer();
			}
		}
	}

	/**
	 * The computer player plays the selected card to the pile
	 * @param card the card to be played
	 */
	public void play(Card card) {
		if (card != null) {
			playCard(card);
			currentPlayer().playCard(card, pile);
			String footer = "Card Left: " + deck.numCards();
			message(footer);
			//playCard(card.toString());
			westPanel.revalidate();
            westPanel.repaint();
            playArea.revalidate();
            playArea.repaint();
            
         // prompt wild card color selection if applicable
 			if (card.isWildCard()) {	
 				if(!currentPlayerIsHuman()) {
 					setColor(((ComputerPlayer)currentPlayer()).chooseColor());	
 					
 				// Find Card Color
 					Card topCard1 = pile.topCard();
 					switch (topCard1.getColor()) {
 					    case RED : 
 					    	color = Color.RED; 
 					    	break;
 					    	
 					    case BLUE : 
 					    	color = Color.BLUE; 
 					    	break;
 					    	
 					    case GREEN : 
 					    	color = Color.GREEN; 
 					    	break;	
 					    	
 					    case YELLOW : 
 					    	color = Color.YELLOW; 
 					    	break;	
 					    	
 					    case NONE : 
 					    	color = Color.GRAY; 
 					    	break;	
 					}
 					addColorChangeCard(color);	
 				}	
 			}
         			
			// say uno
			if (currentPlayer().oneCardLeft()) {
				String message = "Uno !!! from Computer Player " + computerPlayerIndex();
				JOptionPane.showMessageDialog(null,message);
			}
			
			//nextPlayer();					
		} 
		else {
				//nextPlayer();
			 }								
	}

	/**
	 * Transfer play to the next player
	 */
	public void nextPlayer() {
		Card topCard = pile.topCard();
		// if there is a winner
		if (currentPlayer().hasWon()) {
			// TODO handle win, including next player drawing cards if necessary and switching back to this player
			//if (topCard.isActive() && (topCard.hasRank(Rank.DRAW_TWO) || topCard.hasRank(Rank.WILD_DRAW_FOUR))) {
			//	incrementPlayerIndex(playOrder);
			//	drawAfterWin();
			//}
			int thisRoundPoints = 0;
			for (Player player: players) {
				thisRoundPoints += player.getHand().handValue();
			}
			points[currentPlayerIndex] += thisRoundPoints;
			if (currentPlayerIsHuman()) {
				String message = "CONGRATULATIONS YOU WON THIS ROUND !!!";
				JOptionPane.showMessageDialog(null,message);
			}
			else {
				String message = "CONGRATULATIONS Computer Player " + computerPlayerIndex() + " YOU WON THIS ROUND !!!";
				JOptionPane.showMessageDialog(null,message);
			}		
			//nextRound();
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
				//Enable Human hand to play
				for(Component component : getComponents(handPanel)) {
					component.setEnabled(true);
				}
			}
			else {
				//Disable Human hand while computer is playing
				for(Component component : getComponents(handPanel)) {
					component.setEnabled(false);
				}
				computerPlayer[computerPlayerIndex()].doClick();
			}
			
		}
	}
	
	/**
	* Get the color choice of the human player in case of a wild card to change color is played
	*/
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
        
        ButtonGroup group = new ButtonGroup();
        group.add(redButton);
        group.add(blueButton);
        group.add(yellowButton);
        group.add(greenButton);
        redButton.setSelected(true);

        JOptionPane.showMessageDialog(null, colorPanel,"Choose a Color", JOptionPane.INFORMATION_MESSAGE);
        if(redButton.isSelected()) {
        	setColor(main.Color.RED);
        	colorChoice = Color.RED;
        }	
        if(blueButton.isSelected()) {
        	setColor(main.Color.BLUE);
        	colorChoice = Color.BLUE;;
        }	
        if(yellowButton.isSelected()) {
        	setColor(main.Color.YELLOW);
        	colorChoice = Color.YELLOW;
        }	
        if(greenButton.isSelected()) {
        	setColor(main.Color.GREEN);
        	colorChoice = Color.GREEN;
        }	
	}
	
	/**
	 * The computer current player sets the color of a wild card that has not yet been assigned a color
	 */
	public void setColor(main.Color color) {
		pile.topCard().setColor(color);
	}

	/**
	 * The current player draws a card from the deck
	 */
	public void draw() {
		Card cardDrawn = currentPlayer().drawCard(deck, pile);
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
	 * The current player acts on an action card, the action card is deactivated
	 */
	public int takeHumanAction() {
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
			noOfCard = 0;
		}
		return noOfCard;
	}
	
	/**
	 * The current computer player acts on an action card, the action card is deactivated
	 */
	public void takeAction() {
		// get rank of card
		Card topCard = pile.topCard();
		Rank topRank = topCard.getRank();
		topCard.setActive(false);
		// draw two cards
		if (topRank.equals(Rank.DRAW_TWO)) {
			currentPlayer().drawCards(deck, pile, 2);
			String footer = "Card Left: " + deck.numCards();
			message(footer);
			westPanel.revalidate();
            westPanel.repaint();
		}
		// draw four cards
		else if (topRank.equals(Rank.WILD_DRAW_FOUR)) {
			currentPlayer().drawCards(deck, pile, 4);
			String footer = "Card Left: " + deck.numCards();
			message(footer);
			westPanel.revalidate();
            westPanel.repaint();
		}
		// skip turn
		else {
			// TODO handle skips (reverse acts like skip in 2 player game)
			nextPlayer();
		}
		nextPlayer();
	}
	
	/**
	 * Start a new round after a player wins, or end the game if a player has 500 points
	 */
	public void nextRound() {
		// check if someone won the game
		for (int i = 0; i < numPlayers(); i++) {
			if (points[i] >= 1 && !gameOver) {
				gameOver = true;
				/*
				// TODO do something for player that won
				//status.setText(String.format("Player %d wins the game", i));
				if (currentPlayerIsHuman()) {
					String message = "<html>CONGRATULATIONS YOU WON THE GAME !!!</html>";
					JOptionPane.showMessageDialog(null,message,"Congratulation",JOptionPane.INFORMATION_MESSAGE);
					frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				}
				else {
					String message1 = "<html>CONGRATULATIONS Computer Player ";
					String message2 = String.valueOf(computerPlayerIndex());
					String message3 = " - YOU WON THE GAME !!!";
					String message = message1 + message2 + message3 + "</html>";
					JOptionPane.showMessageDialog(null,message,"Congratulation",JOptionPane.INFORMATION_MESSAGE);
					frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
					JOptionPane.showMessageDialog(null,message);
					frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				}
				*/	
				// TODO display points on screen
				System.out.println(Arrays.toString(points));
			}
		}
		getPlayerPoints();
		
		// message for winner and set up prompt to next round
		// TODO message for winner
		//status.setText(String.format("Player %d wins the round", currentPlayerIndex));
		//continueButton.setActionCommand("next round");
		// TODO display points on screen
		System.out.println(Arrays.toString(points));
	}
	
	private Component[] getComponents(Component container) {
        ArrayList<Component> list = null;

        try {
            list = new ArrayList<Component>(Arrays.asList(
                  ((Container) container).getComponents()));
            for (int index = 0; index < list.size(); index++) {
                for (Component currentComponent : getComponents(list.get(index))) {
                    list.add(currentComponent);
                }
            }
        } catch (ClassCastException e) {
            list = new ArrayList<Component>();
        }

        return list.toArray(new Component[list.size()]);
      }
	
	/**
	* Get the total points of all the players when a player has won the game
	*/
	private void getPlayerPoints() {	
		
		JPanel pointsPanel = new JPanel();
		
		String message1 = "<html> Human Player Point Total: ";
		String message2 = String.valueOf(points[0]);
		String message = message1 + message2 + "</html>";
		JLabel player = new JLabel(message);
		pointsPanel.add(player);
		
		for (int i = 1; i < players.length; i++) {
			String message3 = "<html> Computer Player ";
			String message4 = Integer.toString(i);
			String message5 = " Point Total: ";
			String message6 = String.valueOf(points[i]);
			String message7 = message3 + message4 + message5 + message6 + "</html>";
			JLabel player1 = new JLabel(message7);
			pointsPanel.add(player1);
		}

        JOptionPane.showMessageDialog(null, pointsPanel,"Player Total Points", JOptionPane.INFORMATION_MESSAGE);
	}
}
