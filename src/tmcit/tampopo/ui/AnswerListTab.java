package tmcit.tampopo.ui;

import java.awt.Component;
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
	
	public void addTabAndSelect(String title,Component component){
		///タブ追加してその画面を見る
		this.addTab(title, component);
		this.setSelectedComponent(component);
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
