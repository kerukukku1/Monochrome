package Mahito6.UI;
import java.awt.Color;

import javax.swing.JPanel;

import Mahito6.Main.ProblemManager;
import Main.UI.Util.MyKeyListener;

public class MainPanel extends JPanel{
	public InputPanel inputPanel;
	public UtilityPanel uPanel; 
	
	public MainPanel(){
		this.setLayout(null);
		launchItems();
		this.addKeyListener(new MyKeyListener());
	}
	
	private void launchItems(){
		inputPanel = new InputPanel(0,MainFrame.frame_height-50);
		this.add(inputPanel);
		this.setBackground(Color.gray.darker());
		
		ProblemManager.setInputPanel(inputPanel);
		
		uPanel = new UtilityPanel();
		this.add(uPanel);
		
		ProblemManager.setUtilityPanel(uPanel);
		
//		imagePanel = new MainImagePanel();
//		this.add(imagePanel);
	}
	
	
}