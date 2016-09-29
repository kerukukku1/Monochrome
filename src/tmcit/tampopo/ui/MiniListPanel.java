package tmcit.tampopo.ui;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.xml.soap.Detail;

import tmcit.tampopo.util.Answer;

public class MiniListPanel extends JPanel implements AdjustmentListener{
	
	public static final Color backGround = new Color(230, 230, 240);
	public static final int WIDTH = SolverPanel.CENTER_WIDTH - 4;
	public static final int HEIGHT = SolverPanel.CENTER_HEIGHT - 26;
	public static final int IMAGESIZE = SolverPanel.CENTER_MINIIMAGE_SIZE;
	
	public SolverPanel root;
	public DetailPanel detailPanel;
	public String title;
	public List<Answer> answers;
	public List<MiniImagePanel> miniPanels;
	public PanelListManager panelListManager;
	public int miniPanelNum;
	
	public MiniListPanel(String title,List<Answer> answers,SolverPanel root, DetailPanel detailPanel){
		this.root = root;
		this.detailPanel = detailPanel;
		///answerは必ずコピーする
		this.answers = new ArrayList<Answer>();
		this.miniPanels = new ArrayList<MiniImagePanel>();
		this.title = title;
		this.miniPanelNum = 0;
		initPanel();
		makePanel();
		for(Answer answer : answers){
			addAnswer(answer);
		}
		refreshAllMiniImage(true);
		
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
		miniPanels.add(miniImagePanel);
		miniPanelNum++;
	}
	
	public void makePanel(){
		panelListManager = new PanelListManager(WIDTH, HEIGHT,false,this);
		panelListManager.setBounds(0, 0, WIDTH, HEIGHT);
		panelListManager.setPanelBackgroundColor(backGround);
		panelListManager.getVerticalScrollBar().addAdjustmentListener(this);
		this.add(panelListManager);
	}
	
	public void initPanel(){
		this.setLayout(null);
	}
	
	private int adjustCounter = 0;
	
	public void refreshAllMiniImage(boolean isFirst){
		int c1 = 0;
		Rectangle viewRectangle = panelListManager.mainPanel.getVisibleRect();
		for(MiniImagePanel miniImagePanel : miniPanels){
			if(miniImagePanel.getBounds().intersects(viewRectangle)){
				///表示中なので画像も出す
				miniImagePanel.showImage();
			}else{
				miniImagePanel.hideImage();
			}
			if(isFirst && c1 < 30){
				miniImagePanel.showImage();
			}
			c1++;
		}
		panelListManager.resize();
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent event) {
		if(adjustCounter != 5){
			adjustCounter++;
			return;
		}
		refreshAllMiniImage(false);
		adjustCounter = 0;
		// TODO Auto-generated method stub
		
	}

}
