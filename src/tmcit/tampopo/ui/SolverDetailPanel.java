package tmcit.tampopo.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.border.EtchedBorder;

public class SolverDetailPanel extends JTabbedPane{
	
	
	public SolverTree solverTree;///パラメータ管理用のツリー
	public int WIDTH,HEIGHT;
	
	public SolverDetailPanel(int WIDTH,int HEIGHT){
		this.HEIGHT = HEIGHT;
		this.WIDTH = WIDTH;
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		initPanel();
		addParameterPanel();
	}
	
	public void addParameterPanel(){
		solverTree = new SolverTree(this);
		solverTree.setBounds(0, 0, WIDTH, HEIGHT);
		this.addTab("Parameter",solverTree);
	}
	
	public void initPanel(){
		this.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
	}

}
