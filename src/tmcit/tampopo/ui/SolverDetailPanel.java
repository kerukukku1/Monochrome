package tmcit.tampopo.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.border.EtchedBorder;

public class SolverDetailPanel extends DeletableTabbedPane implements ActionListener{
	
	
	public SolverTree solverTree;///パラメータ管理用のツリー
	public int WIDTH,HEIGHT;
	
	public SolverDetailPanel(int WIDTH,int HEIGHT){
		this.HEIGHT = HEIGHT;
		this.WIDTH = WIDTH;
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		this.setListener(this);
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
		this.setTabPlacement(JTabbedPane.TOP);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		DeletableMyButton button = (DeletableMyButton) e.getSource();
		Component target = button.getSource();
		if(target instanceof SolverTree)return;
		ParameterDetailPanel detailPanel = (ParameterDetailPanel) target;
		if(detailPanel.isSolverRunning)return;
		detailPanel.sourceNode.hideDetail();
	}

}
