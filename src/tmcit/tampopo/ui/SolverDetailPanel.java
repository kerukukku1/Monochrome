package tmcit.tampopo.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.border.EtchedBorder;

public class SolverDetailPanel extends JPanel{
	
	
	public SolverTree solverTree;///パラメータ管理用のツリー
	int WIDTH,HEIGHT;
	
	public SolverDetailPanel(int WIDTH,int HEIGHT){
		this.HEIGHT = HEIGHT;
		this.WIDTH = WIDTH;
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		initPanel();
		makePanel();
	}
	
	public void makePanel(){
		solverTree = new SolverTree();
		solverTree.setBounds(0, 0, WIDTH, HEIGHT);
		this.add(solverTree);
	}
	
	public void initPanel(){
		this.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		this.setLayout(null);
	}

}
