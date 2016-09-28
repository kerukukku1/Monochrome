package tmcit.tampopo.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import tmcit.api.AnswerChangeEvent;
import tmcit.api.AnswerChangeListener;
import tmcit.tampopo.util.Answer;

public class AnswerListPanel extends JPanel{
	public SolverPanel source;
	public PanelListManager panelListManager;
	public int WIDTH,HEIGHT;
	
	public AnswerListPanel(int WIDTH,int HEIGHT,SolverPanel source){
		this.source = source;
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		initPanel();
		makePanel();
	}
	
	public void clearPanels(){
		///クリアする
		panelListManager.allClear();
	}
	
	public DetailPanel addAnswer(String title,Answer answer){
		DetailPanel detailPanel = new DetailPanel(title, answer, source);
		panelListManager.addPanel(detailPanel);
		return detailPanel;
	}
	
	public DetailPanel addAnswer(String title,List<Answer> answers){
		DetailPanel detailPanel = new DetailPanel(title, answers, source);
		panelListManager.addPanel(detailPanel);
		return detailPanel;
	}
	public DetailPanel addAnswer(String title, Answer answer, int index) {
		///割り込み
		DetailPanel detailPanel = new DetailPanel(title, answer, source);
		panelListManager.addPanel(detailPanel,0);
		return detailPanel;
	}

	
	public void removeAnswerPanel(DetailPanel component){
		panelListManager.removePanel(component);
	}
	
	public void makePanel(){
		panelListManager = new PanelListManager(WIDTH, HEIGHT,false,this);
		panelListManager.setBounds(0, 0, WIDTH, HEIGHT);
		this.add(panelListManager);
	}
	
	public void initPanel(){
		this.setLayout(null);
		this.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.white, Color.black));
	}


}
