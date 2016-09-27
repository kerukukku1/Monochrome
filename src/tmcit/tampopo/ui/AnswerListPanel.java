package tmcit.tampopo.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

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
		
		panelListManager.addPanel(new DetailPanel("test", Answer.makeDebugAnswer(),source));
		List<Answer> ansList = new ArrayList<Answer>();
		for(int i = 0;i < 5;i++)
			ansList.add(Answer.makeDebugAnswer());
		panelListManager.addPanel(new DetailPanel("any", ansList,source));
	}
	
	public void makePanel(){
		panelListManager = new PanelListManager(WIDTH, HEIGHT,false);
		panelListManager.setBounds(0, 0, WIDTH, HEIGHT);
		this.add(panelListManager);
	}
	
	public void initPanel(){
		this.setLayout(null);
		this.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.white, Color.black));
	}

}
