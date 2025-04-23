package main;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class RoundedButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RoundedButton(String text, Color bgColor) {
		super(text);
		setContentAreaFilled(false);
		setFocusPainted(false);
		setForeground(Color.WHITE);
		setBackground(bgColor);
		setFont(new Font("Segoe UI", Font.PLAIN, 13));
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(getBackground());
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

		super.paintComponent(g);
		g2.dispose();
	}

	@Override
	protected void paintBorder(Graphics g) {
		// Pas de bordure visible
	}
}
