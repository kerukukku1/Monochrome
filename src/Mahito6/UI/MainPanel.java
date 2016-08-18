package Mahito6.UI;
import java.awt.Color;

import javax.swing.JPanel;

import Main.UI.Util.MyKeyListener;

public class MainPanel extends JPanel{
	public InputPanel inputPanel;
	public static VisualizePiece vPiece;
	public UtilityPanel uPanel; 
	
	public MainPanel(){
		this.setLayout(null);
		launchItems();
		this.addKeyListener(new MyKeyListener());
	}
	
	private void launchItems(){
		vPiece = new VisualizePiece();
		this.add(vPiece);
		
		inputPanel = new InputPanel(0,MainFrame.frame_height-50);
		this.add(inputPanel);
		this.setBackground(Color.gray.darker());
		
		uPanel = new UtilityPanel();
		this.add(uPanel);
		
//		imagePanel = new MainImagePanel();
//		this.add(imagePanel);
	}
	
	
}