package Mahito6.UI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class UtilityPanel extends JPanel implements ActionListener{
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
}