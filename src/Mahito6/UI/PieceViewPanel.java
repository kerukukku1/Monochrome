package Mahito6.UI;

import java.awt.Color;

import javax.swing.JPanel;

public class PieceViewPanel extends JPanel{
	public PieceViewPanel(int x, int y, int width, int height){
		this.setLayout(null);
		this.setBackground(Color.gray.darker().darker().darker());
		this.setBounds(x, y, width, height);
		this.setOpaque(true);	
	}
}