package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
//import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class FinalGame implements ActionListener {
	JFrame frame;
	JSpinner selectPlayers;
	

	/**
	 * Create a game with the specified number of players
	 * @param numPlayers
	 */
	public FinalGame() {
		
		//The northPanel containing the gameHeader label.
		JPanel northPanel = new JPanel(new BorderLayout(8,8));
		northPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		JLabel gameHeader = new JLabel("Welcome to UNO Game !!!", JLabel.CENTER);
		gameHeader.setVerticalAlignment(JLabel.TOP);
		gameHeader.setFont(new Font("Verdana", Font.BOLD, 32));
		gameHeader.setForeground(new Color(38, 70, 83));
		northPanel.add(gameHeader, BorderLayout.CENTER);
		northPanel.setBackground(new Color(255, 173, 173));
		
		//The centerNorthPanel containing the gameRulesHeader label.
		JPanel centerNorthPanel = new JPanel(new BorderLayout(8,8));
		centerNorthPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		JLabel gameRulesHeader = new JLabel("How to Play UNO.", JLabel.CENTER);
		gameRulesHeader.setVerticalAlignment(JLabel.TOP);
		gameRulesHeader.setFont(new Font("Verdana", Font.BOLD, 32));
		gameRulesHeader.setForeground(new Color(253, 255, 182));
		centerNorthPanel.add(gameRulesHeader, BorderLayout.CENTER);
		centerNorthPanel.setBackground(new Color(220, 47, 2));
		
		//The centerCenterPanel containing the gameRules textarea.
		JPanel centerCenterPanel = new JPanel(new BorderLayout(8,8));
		centerCenterPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		JTextArea gameRules = new JTextArea("Official rules\n" + 
				"The aim of the game is to be the first player to score 500 points, achieved (usually over several rounds of play) by being the first to play all of one's own cards and scoring points for the cards still held by the other players.\n" + 
				"The deck consists of 108 cards: four each of \"Wild\" and \"Wild Draw Four,\" and 25 each of four different colors (red, yellow, green, blue). Each color consists of one zero, two each of 1 through 9, and two each of \"Skip,\" \"Draw Two,\" and \"Reverse.\" These last three types are known as \"action cards.\"\n" + 
				"To start a hand, seven cards are dealt to each player, and the top card of the remaining deck is flipped over and set aside to begin the discard pile. The player to the dealer's left plays first unless the first card on the discard pile is an action or Wild card (see below). On a player's turn, they must do one of the following:\n" + 
				"- play one card matching the discard in color, number, or symbol\n" + 
				"- play a Wild card, or a playable Wild Draw Four card (see restriction below)\n" + 
				"- draw the top card from the deck, then play it if possible\n" + 
				"Cards are played by laying them face-up on top of the discard pile. Play proceeds clockwise around the table.\n" + 
				"\n" + 
				"Action or Wild cards have the following effects:\n" +
				"- Skip:  Next player in sequence misses a turn\n" + 
				"- Reverse:  Order of play switches directions\n" + 
				"- Draw Two (+2):  Next player draws two cards and misses a turn\n" + 
				"- Wild:  Player declares the next color to be matched (current color may be chosen as the next to be matched)\n" + 
				"- Wild Draw Four (+4 & wild):  Player declares the next color to be matched; next player draws four cards and misses a turn. May be played only if the player has no cards of the current color.\n" + 
				"\n" + 
				"- A player who draws from the deck must either play or keep that card and may play no other card from their hand on that turn.\n" + 
				"- A player may play a Wild card at any time, even if that player has other playable cards.\n" + 
				"- A player may play a Wild Draw Four card only if that player has no cards matching the current color. The player may have cards of a different color matching the current number or symbol or a Wild card and still play the Wild Draw Four card.\n" + 
				"- If the entire deck is used during play, the top discard is set aside and the rest of the pile is shuffled to create a new deck. Play then proceeds normally.\n" + 
				"- It is illegal to trade cards of any sort with another player.\n" + 
				"\n" + 
				"The first player to get rid of their last card (\"going out\") wins the hand and scores points for the cards held by the other players. Number cards count their face value, all action cards count 20, and Wild and Wild Draw Four cards count 50. If a Draw Two or Wild Draw Four card is played to go out, the next player in the sequence must draw the appropriate number of cards before the score is tallied.\n" + 
				"The first player to score 500 points wins the game.\n" + 
				"\n" + 
				"Two-player game\n" + 
				"In a two-player game, the Reverse card acts like a Skip card; when played, the other player misses a turn.\n" + 
				"	");
		gameRules.setLineWrap(true);
		gameRules.setWrapStyleWord(true);
		gameRules.setEditable(false);
		gameRules.setFont(new Font("Verdana", Font.PLAIN, 12));
		gameRules.setForeground(new Color(11, 3, 45));
		gameRules.setBackground(new Color(222, 217, 226));
		JScrollPane scrollingArea = new JScrollPane(gameRules);
		centerCenterPanel.add(scrollingArea, BorderLayout.CENTER);
		centerCenterPanel.setBackground(new Color(226, 175, 255));
		
		//The centerPanel containing other subPanels.
		JPanel centerPanel = new JPanel(new BorderLayout(8,8));
		centerPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		centerPanel.add(centerNorthPanel, BorderLayout.NORTH);
		centerPanel.setBackground(new Color(9, 215, 95));
		centerPanel.add(centerCenterPanel, BorderLayout.CENTER);
		
		//The southWestEastPanel containing the selectPlayers spinner.
		JPanel southWestEastPanel = new JPanel(new BorderLayout(8,8));
		southWestEastPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		SpinnerModel spinnerModel = new SpinnerNumberModel(2, 2, 10, 1);
		selectPlayers = new JSpinner(spinnerModel);
		southWestEastPanel.add(selectPlayers, BorderLayout.EAST);
		southWestEastPanel.setBackground(new Color(0, 245, 212));
		
		//The southWestWestPanel containing the players label.
		JPanel southWestWestPanel = new JPanel(new BorderLayout(8,8));
		southWestWestPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		JLabel players = new JLabel("Number of Players:", JLabel.CENTER);
		players.setVerticalAlignment(JLabel.TOP);
		players.setFont(new Font("Verdana", Font.BOLD, 32));
		players.setForeground(new Color(253, 231, 76));
		southWestWestPanel.add(players, BorderLayout.WEST);
		southWestWestPanel.setBackground(new Color(102, 46, 155));
		
		//The southEastPanel containing the play button.
		JPanel southEastPanel = new JPanel(new BorderLayout(8,8));
		southEastPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		JButton play = new JButton("Play");
		play.setVerticalAlignment(JLabel.CENTER);
		play.addActionListener(this);
		play.setFont(new Font("Verdana", Font.BOLD, 32));
		play.setForeground(new Color(255, 32, 110));
		play.setBackground(new Color(0, 0, 0));
		southEastPanel.add(play, BorderLayout.CENTER);
		southEastPanel.setBackground(new Color(214, 234, 223));
		
		//The southWestPanel containing other subPanels.
		JPanel southWestPanel = new JPanel(new BorderLayout(8,8));
		southWestPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		southWestPanel.add(southWestEastPanel, BorderLayout.EAST);
		southWestPanel.setBackground(new Color(255, 155, 113));
		southWestPanel.add(southWestWestPanel, BorderLayout.WEST);
		
		//The southPanel containing other subPanels.
		JPanel southPanel = new JPanel(new BorderLayout(8,8));
		southPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		southPanel.add(southEastPanel, BorderLayout.EAST);
		southPanel.setBackground(new Color(0, 150, 199));
		southPanel.add(southWestPanel, BorderLayout.WEST);
		
		//The outerPanel containing other subPanels.
		JPanel outerPanel = new JPanel(new BorderLayout(8,8));
		outerPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		outerPanel.add(northPanel, BorderLayout.NORTH);
		outerPanel.add(centerPanel, BorderLayout.CENTER);
		outerPanel.add(southPanel, BorderLayout.SOUTH);
		outerPanel.setBackground(new Color(234, 115, 23));
		
		//The frame containing the outerPanel.
		frame = new JFrame("Welcome"); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(outerPanel);
		frame.setPreferredSize(new Dimension(1200, 900));
		//frame.setLocationByPlatform(true);
		//frame.setResizable(false);
        frame.pack();
        //frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		frame.setVisible(false);
		new UnoGame((Integer)selectPlayers.getValue(), frame);
	}
	
	/**
	 * Runs the game
	 * @param args not used
	 */
	public static void main(String[] args) {
		new FinalGame();
	}
	
}
