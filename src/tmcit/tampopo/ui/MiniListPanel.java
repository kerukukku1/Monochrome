package tmcit.tampopo.ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import tmcit.tampopo.util.Answer;

public class MiniListPanel extends JPanel{
	
	public static final Color backGround = new Color(230, 230, 240);
	public static final int WIDTH = SolverPanel.CENTER_WIDTH - 4;
	public static final int HEIGHT = SolverPanel.CENTER_HEIGHT - 26;
	public static final int IMAGESIZE = SolverPanel.CENTER_MINIIMAGE_SIZE;
	
	public SolverPanel root;
	public String title;
	public List<Answer> answers;
	public PanelListManager panelListManager;
	public int miniPanelNum;
	
	public MiniListPanel(String title,List<Answer> answers,SolverPanel root){
		this.root = root;
		///answerは必ずコピーする
		this.answers = new ArrayList<Answer>();
		this.title = title;
		this.miniPanelNum = 0;
		initPanel();
		makePanel();
		for(Answer answer : answers){
			addAnswer(answer);
		}
	}
	
	public String getTabTitle(){
		String ret = title + "[" + miniPanelNum + "]";
		return ret;
	}
	
	public void addAnswer(Answer answer){
		///1個画像を追加する
		this.answers.add(answer);
		MiniImagePanel miniImagePanel = new MiniImagePanel(answer,miniPanelNum,IMAGESIZE,this);
		miniImagePanel.addMouseListener(root);
		panelListManager.addPanel(miniImagePanel);
		miniPanelNum++;
	}
	
	public void makePanel(){
		panelListManager = new PanelListManager(WIDTH, HEIGHT,false);
		panelListManager.setBounds(0, 0, WIDTH, HEIGHT);
		panelListManager.setPanelBackgroundColor(backGround);
		this.add(panelListManager);
	}
	
	public void initPanel(){
		this.setLayout(null);
	}

}
