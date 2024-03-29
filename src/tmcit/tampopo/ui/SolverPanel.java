package tmcit.tampopo.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicCheckBoxMenuItemUI;

import tmcit.tampopo.util.Answer;

public class SolverPanel extends JPanel implements MouseListener{
	
	public static final int LEFT_WIDTH = 210;
	public static final int LEFT_HEIGHT = 664;
	public static final int CENTER_WIDTH = 540;
	public static final int CENTER_HEIGHT = 664;
	public static final int RIGHT_WIDTH = 210;
	public static final int RIGHT_HEIGHT = 664;
	
	public static final int LEFT_IMAGE_SIZE = 192;///左側のリストの画像サイズ
	public static final int CENTER_BIGIMAGE_SIZE = 530;///真ん中のでっかい画像サイズ
	public static final int CENTER_MINIIMAGE_SIZE = 128;///リストの小さい画像
	
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
		
		this.add(right);
		this.add(center);
		this.add(left);
	}
	
	public void tabClose(){
		///今開いてるタブを閉じる
		center.deleteOpeningTab();
	}
	
	private void leftDetailClick(DetailPanel detailPanel,int button){
		String title = detailPanel.title;
		List<Answer> answers = detailPanel.answers;
		if(button == 1 || button == 2){
			///解が1個ならビッグ、複数ならリスト表示
			JPanel newPanel;
			if(detailPanel.answers.size() == 1){
				newPanel = new BigImagePanel(title,answers.get(0));
			}else{
				MiniListPanel newMiniListPanel = new MiniListPanel(title, answers,this);
				title = newMiniListPanel.getTabTitle();
				newPanel = newMiniListPanel;
			}
			if(button == 1){
				center.addTabAndSelect(title, newPanel);
			}else if(button == 2){
				center.addTab(title, newPanel);
			}
		}else{
			///右クリックなら要素削除
		}
	}
	private void centerMiniImageClick(MiniImagePanel miniImagePanel,int button){
		String title = miniImagePanel.getTabTitle();
		Answer answer = miniImagePanel.answer;
		if(button == 1 || button == 2){
			///解が1個ならビッグ、複数ならリスト表示
			BigImagePanel newPanel = new BigImagePanel(title,answer);
			if(button == 1){
				center.addTabAndSelect(title,newPanel);
			}else if(button == 2){
				center.addTab(title, newPanel);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int button = e.getButton();
		System.out.println(button);
		if(e.getSource() instanceof DetailPanel){
			leftDetailClick((DetailPanel)e.getSource(), button);
		}else if(e.getSource() instanceof MiniImagePanel){
			centerMiniImageClick((MiniImagePanel)e.getSource(), button);
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
