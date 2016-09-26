package tmcit.tampopo.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicCheckBoxMenuItemUI;

public class SolverPanel extends JPanel implements MouseListener{
	
	public static final int LEFT_WIDTH = 210;
	public static final int LEFT_HEIGHT = 664;
	public static final int CENTER_WIDTH = 540;
	public static final int CENTER_HEIGHT = 664;
	public static final int RIGHT_WIDTH = 210;
	public static final int RIGHT_HEIGHT = 664;
	
	public static final int LEFT_IMAGE_SIZE = 192;///左側のリストの画像サイズ
	public static final int CENTER_BIGIMAGE_SIZE = 530;///真ん中のでっかい画像サイズ
	
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
		left = new AnswerListPanel(LEFT_WIDTH,LEFT_HEIGHT,this);
		center = new AnswerListTab(CENTER_WIDTH,CENTER_HEIGHT);
		right = new SolverDetailPanel(RIGHT_WIDTH,RIGHT_HEIGHT);
		
		this.add(left);
		this.add(center);
		this.add(right);
	}
	
	public void tabClose(){
		///今開いてるタブを閉じる
		center.deleteOpeningTab();
	}
	
	private void leftDetailClick(DetailPanel detailPanel,int button){
		if(button == 1){
			///解が1個ならビッグ、複数ならリスト表示
			if(detailPanel.answers.size() == 1){
				BigImagePanel newPanel = new BigImagePanel(detailPanel.title, detailPanel.answers.get(0));
				center.addTab(detailPanel.title,newPanel);
			}else{
				
			}
		}else{
			///右クリックなら要素削除
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int button = e.getButton();
		System.out.println(button);
		if(e.getSource() instanceof DetailPanel){
			leftDetailClick((DetailPanel)e.getSource(), button);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
