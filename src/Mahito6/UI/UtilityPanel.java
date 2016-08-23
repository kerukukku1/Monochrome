package Mahito6.UI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Mahito6.Main.Constants;

public class UtilityPanel extends JPanel implements ActionListener, ChangeListener{
	public ButtonGroup switType = new ButtonGroup();
	private JRadioButton piece;
	private JRadioButton frame;
	public UtilityPanel(){
		setUtil();
		launchItems();
	}
	
	private void setUtil(){
		this.setLayout(null);
		this.setBackground(Color.gray.darker().darker().darker());
		this.setBounds(MainFrame.frame_width/4*3, 0, MainFrame.frame_width/4, MainFrame.frame_height-50);
		this.setOpaque(true);
	}
	
	private void launchItems(){
		JButton next = new JButton("Next");
		JButton back = new JButton("Back");
		
		next.setBounds(10, MainFrame.frame_height-90, 80, 30);
		back.setBounds(90, MainFrame.frame_height-90, 80, 30);
		
		next.addActionListener(this);
		back.addActionListener(this);
		
		this.add(next);
		this.add(back);
		
		piece = new JRadioButton("Piece");
		frame = new JRadioButton("Frame");

		piece.setBounds(0, MainFrame.frame_height/2, 80, 30);
		frame.setBounds(80, MainFrame.frame_height/2, 80, 30);
		
		piece.setForeground(Color.red);
		frame.setForeground(Color.red);
		
		piece.setSelected(true);
		
		switType.add(piece);
		switType.add(frame);
		
		this.add(piece);
		this.add(frame);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand() == "Next"){
			MainPanel.vPiece.Next();
		}else if(e.getActionCommand() == "Back"){
			MainPanel.vPiece.Back();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		if(piece.isSelected()){
			Constants.clearNoiseThreshold = 200;
			Constants.edgeWidth = 6;
			Constants.lrAddition = 15;
		}else{
			Constants.clearNoiseThreshold = 1200;
			Constants.edgeWidth = 12;
			Constants.lrAddition = 50;
		}
	}
}