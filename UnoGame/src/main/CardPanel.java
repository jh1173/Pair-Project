package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * JPanel for showing Cards
 */
public class CardPanel extends JPanel {
	
	private static final long serialVersionUID = 1l;
	private int numWide = 7;
	private int cardWidth;
	private int cardHeight;
	private Card[] cards;
	
	public CardPanel() {
		this(1.0);
	}
	
	/**
	 * Play cards in the panel by clicking on them
	 * @implements MouseListener
	 */
	public abstract class CardClickListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (clickIsEnabled()) {
				int x = e.getX();
				int y = e.getY();
				int cardIndex = (int)(x / cardWidth) + numWide * (int)(y / cardHeight);
				if (cardIndex >= 0 && cardIndex < cards.length) {
					Card selectedCard = cards[cardIndex];
					if (cardIsPlayable(selectedCard)) {
						play(selectedCard);
					}
				}

			}
		}
		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
		/**
		 * @return whether a click is permitted on this panel in order to play a card
		 */
		public abstract boolean clickIsEnabled();
		/**
		 * @param card the card to be played
		 * @return whether the card is playable
		 */
		public abstract boolean cardIsPlayable(Card card);
		/**
		 * play the card
		 * @param card
		 */
		public abstract void play(Card card);
	}
	
	public CardPanel(double scale) {
		super();
		setLayout(new GridBagLayout());
		// standardize width and height of all cards based on sample card (red 0 is used)
		ImageIcon originalIcon = new ImageIcon("Uno_Cards/RED_NUM0.png");
		int scaledWidth = (int)(originalIcon.getIconWidth() * scale);
		this.cardWidth = (int)((scaledWidth + 2) / 4) * 4;
		int scaledHeight = (int)(originalIcon.getIconHeight() * scale);
		this.cardHeight = (int)((scaledHeight + 2) / 4) * 4;
	}
	
	public void setCards(Card card) {
		Card[] cards = {card};
		setCards(cards);
	}
	
	public void setCards(Card[] cards) {
		removeAll();
		this.cards = cards;
		int cardNum = 0;
		GridBagConstraints c = new GridBagConstraints();
		for (Card card: cards) {
			c.gridx = cardNum % numWide;
			c.gridy = (int)(cardNum / numWide);
			this.add(new JLabel(getCardImage(card)), c);
			cardNum++;
		}
	}
	
	public ImageIcon getCardImage(Card card) {
		ImageIcon originalIcon = new ImageIcon("Uno_Cards/" + card.toString() + ".png");
		return new ImageIcon(originalIcon.getImage().getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
	}
	
	public int getMaxWidth() {
		return numWide * cardWidth;
	}
	
	public int getMaxHeight() {
		return cardHeight;
	}

}
