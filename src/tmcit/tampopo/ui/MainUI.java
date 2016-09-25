package tmcit.tampopo.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainUI extends JFrame{
	
	public static final int MAINFRAME_WIDTH = 1000;
	public static final int MAINFRAME_HEIGHT = 750;
	
	public static final int MAINTABPANE_WIDTH = 980;
	public static final int MAINTABPANE_HEIGHT = 700;
	
	public SolverPanel solverPanel;
	
	public JPanel mainPanel;///ここに全体のタブ貼ったりする
	public MainTabPane tabPane;
	
	public MainUI(){
		initJFrame();
		makePanels();
	}
	
	public void initJFrame(){
		this.setSize(new Dimension(MAINFRAME_WIDTH,MAINFRAME_HEIGHT));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void makePanels(){
		mainPanel = new JPanel();
		mainPanel.setLayout(new FlowLayout());
		mainPanel.setBackground(Color.BLACK);
		
		tabPane = new MainTabPane(MAINTABPANE_WIDTH,MAINTABPANE_HEIGHT);
		
		solverPanel = new SolverPanel();
		
		tabPane.addTab("SOLVER", solverPanel);
		mainPanel.add(tabPane);
		this.add(mainPanel);
	}

}
