package tmcit.tampopo.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import tmcit.tampopo.util.Answer;

public class AnswerListPanel extends JPanel{
	
	public PanelListManager panelListManager;
	public int WIDTH,HEIGHT;
	
	public AnswerListPanel(int WIDTH,int HEIGHT){
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		initPanel();
		makePanel();
		
		panelListManager.addPanel(new DetailPanel("test", Answer.makeDebugAnswer()));
		List<Answer> ansList = new ArrayList<Answer>();
		for(int i = 0;i < 100;i++)
			ansList.add(Answer.makeDebugAnswer());
		panelListManager.addPanel(new DetailPanel("any", ansList));
	}
	
	public void makePanel(){
		panelListManager = new PanelListManager(WIDTH, HEIGHT);
		panelListManager.setBounds(0, 0, WIDTH, HEIGHT);
		this.add(panelListManager);
	}
	
	public void initPanel(){
		this.setLayout(null);
		this.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.white, Color.black));
	}

}
