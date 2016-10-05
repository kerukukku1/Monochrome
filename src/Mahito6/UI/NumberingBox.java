package Mahito6.UI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import Mahito6.Main.Problem;
import Mahito6.Main.ProblemManager;
import Main.UI.Util.Status;

public class NumberingBox extends JPanel implements ActionListener{
	private JButton prev;
	public NumberingBox(){
	    try
	    {
	            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
	            UIManager.put("TabbedPane.selected",Color.GREEN);
	    } catch (Exception ee){
	        ee.printStackTrace();
	    }
//		this.setLayout(null);
	    prev = new JButton("Preview");
	    prev.addActionListener(this);
	    this.setLayout(null);
	    prev.setBounds(0, 0, 100, 50);
	    this.add(prev);
	}
	
	public void setImage(String path){
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("Preview")){
			Problem tmp = null;
			for(Problem p : ProblemManager.getProblems()){
				if(p.getType() == Status.Type.PIECE1){
					new NumberingFrame(p);
					tmp = p;
					break;
				}
			}
			if(tmp != null)return;
			JOptionPane.showMessageDialog(this, "Piece1 is not set");
		}
	}
}
