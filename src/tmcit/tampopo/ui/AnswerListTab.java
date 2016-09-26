package tmcit.tampopo.ui;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AnswerListTab extends JTabbedPane{
	
	public AnswerListTab(int WIDTH,int HEIGHT){
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		initTab();
	}
	
	public void deleteOpeningTab(){
		///表示中のコンポーネントをタブから削除
		int nowTab = this.getSelectedIndex();
		if(nowTab == -1)return;
		this.remove(nowTab);
	}
	
	public void initTab(){
	}
}
