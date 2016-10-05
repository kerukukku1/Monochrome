package tmcit.tampopo.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicCheckBoxMenuItemUI;

import tmcit.api.AnswerChangeEvent;
import tmcit.api.AnswerChangeListener;
import tmcit.api.Parameter;
import tmcit.procon27.main.Solver;
import tmcit.tampopo.ui.util.DropFileHandler;
import tmcit.tampopo.ui.util.ExVertex;
import tmcit.tampopo.ui.util.ParameterNode;
import tmcit.tampopo.util.Answer;
import tmcit.tampopo.util.Problem;
import tmcit.tampopo.util.ProblemReader;

public class SolverPanel extends JPanel{
	
	public static final int RIGHT_WIDTH = 280;
	public static final int RIGHT_HEIGHT = 664;
	public static final int TAB_WIDTH = 520 + 150 + 10;
	public static final int TAB_HEIGHT = 664;
	
	public SolverPanel own;
	
	public SolverDetailPanel right;
	public SuperPanelTab superPanelTab;
	
	public SolverPanel(){
		this.own = this;
		ProblemReader.setSolverPanel(this);
		initPanel();
		makePanels();
		superPanelTab.addSuperPanel("Master",null);
	}
	
	public void setViewSuperPanel(SuperPanel superPanel){
		///superPanelのタブに移動
		superPanelTab.setViewSuperPanel(superPanel);
	}
	
	public Answer getViewingAnswer(){
		BigImagePanel bigImagePanel = getViewingBigImagePanel();
		if(bigImagePanel == null)return null;
		return bigImagePanel.answer;
	}
	
	public BigImagePanel getViewingBigImagePanel(){
		SuperPanel superPanel = getViewingSuperPanel();
		if(superPanel == null)return null;
		return superPanel.getViewingBigImagePanel();
	}
	
	public SuperPanel getViewingSuperPanel(){
		return superPanelTab.getViewingSuperPanel();
	}
	
	public SuperPanel getMasterSuperPanel(){
		return getSuperPanelFromParameterNode(null);
	}
	
	public SuperPanel getSuperPanelFromParameterNode(ParameterNode parameterNode){
		SuperPanel ret = superPanelTab.getSuperPanel(parameterNode);
		if(ret == null){
			ret = superPanelTab.addSuperPanel(parameterNode.getTitle(), parameterNode);
			ret.setProblem(ProblemReader.problem);
		}
		return ret;
	}
	
	public void setProblem(Problem problem){
		superPanelTab.allClear();
		superPanelTab.addSuperPanel("Master",null);
		superPanelTab.getSuperPanel(null).setProblem(problem);
	}
	private void initPanel() {
		this.setLayout(new FlowLayout());
		this.setTransferHandler(new DropFileHandler(this));
	}
	
	private void makePanels(){
		superPanelTab = new SuperPanelTab(TAB_WIDTH,TAB_HEIGHT,this);
		right = new SolverDetailPanel(RIGHT_WIDTH,RIGHT_HEIGHT);
		this.add(right);
		this.add(superPanelTab);
	}
}
