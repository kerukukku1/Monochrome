package Mahito6.UI;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class ParameterPanel extends JPanel{
	private VisualizeFrame parent;
	private JButton run, load;
	public ParameterPanel(VisualizeFrame parent){
		this.parent = parent;
		this.setLayout(null);
		this.setSize(300, VisualizeFrame.visualizeHeight-300);
		this.setBackground(new Color(51,149,230));
		launchItems();
	}
	private void launchItems() {
		run = new JButton("Run");
		load = new JButton("Load");
		Dimension d = this.getSize();
		load.setBounds(0,d.height-25,d.width/2, 20);
		run.setBounds(d.width/2, d.height-25, d.width/2, 20);
		this.add(load);
		this.add(run);
	}
}
