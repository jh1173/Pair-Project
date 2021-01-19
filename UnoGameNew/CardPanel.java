package main;

import javax.swing.*;
import java.awt.*;

public class CardPanel extends JPanel {
	
	private static final long serialVersionUID = 1l;
	private double scale = 1.0;
	
	public CardPanel() {
		super();
		setLayout(new GridBagLayout());
	}
	
	public CardPanel(double scale) {
		super();
		this.scale = scale;
		setLayout(new GridBagLayout());
	}
	
	public void setCards(Card card) {
		Card[] cards = {card};
		setCards(cards);
	}
	
	public void setCards(Card[] cards) {
		removeAll();
		int x = 0;
		GridBagConstraints c = new GridBagConstraints();
		for (Card card: cards) {
			c.gridx = x;
			this.add(new JLabel(getCardImage(card)));
			x++;
		}
	}
	
	public ImageIcon getCardImage(Card card) {
		ImageIcon originalIcon = new ImageIcon("Uno_Cards/" + card.toString() + ".png");
		int scaledWidth = (int)(originalIcon.getIconWidth() * scale);
		int scaledHeight = (int)(originalIcon.getIconHeight() * scale);
		return new ImageIcon(originalIcon.getImage().getScaledInstance(scaledWidth, scaledHeight, java.awt.Image.SCALE_SMOOTH));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
