package tmcit.tampopo.ui;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JPanel;

public class SolverPanel extends JPanel{
	
	public static final int LEFT_WIDTH = 210;
	public static final int LEFT_HEIGHT = 664;
	public static final int CENTER_WIDTH = 540;
	public static final int CENTER_HEIGHT = 664;
	public static final int RIGHT_WIDTH = 210;
	public static final int RIGHT_HEIGHT = 664;
	
	public static final int LEFT_IMAGE_SIZE = 192;///左側のリストの画像サイズ
	
	public AnswerListPanel left;
	public AnswerListTab center;
	public SolverDetailPanel right;
	
	public SolverPanel(){
		initPanel();
		makePanels();
	}

	private void initPanel() {
		this.setLayout(new FlowLayout());
	}
	
	private void makePanels(){
		left = new AnswerListPanel(LEFT_WIDTH,LEFT_HEIGHT);
		center = new AnswerListTab(CENTER_WIDTH,CENTER_HEIGHT);
		right = new SolverDetailPanel(RIGHT_WIDTH,RIGHT_HEIGHT);
		
		this.add(left);
		this.add(center);
		this.add(right);
	}

}
