package tmcit.tampopo.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import tmcit.tampopo.util.Answer;

public class AnswerListTab extends JTabbedPane implements ChangeListener{
	
	public AnswerListTab(int WIDTH,int HEIGHT){
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		initTab();
	}
	
	public Answer getViewingAnswer(){
		if(!(this.getSelectedComponent() instanceof BigImagePanel)){
			System.out.println("表示して！");
			return null;
		}
		BigImagePanel bigImagePanel = (BigImagePanel) this.getSelectedComponent();
		return bigImagePanel.answer;
	}
	
	public void clearPanels(){
		///クリアする
		while(this.getTabCount() != 0){
			deleteOpeningTab();
		}
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
		if(this.getSelectedComponent() instanceof MiniListPanel){
			MiniListPanel miniListPanel = (MiniListPanel) this.getSelectedComponent();
			if(miniListPanel.detailPanel != null)
				miniListPanel.detailPanel.listOff();
		}
		if(this.getSelectedComponent() instanceof BigImagePanel){
			BigImagePanel bigImagePanel = (BigImagePanel) this.getSelectedComponent();
			if(bigImagePanel.detailPanel != null)
				bigImagePanel.detailPanel.listOff();
		}
		this.remove(nowTab);
	}
	
	public void initTab(){
		this.addChangeListener(this);
	}

	@Override
	public void stateChanged(ChangeEvent event) {
		Component comp = this.getSelectedComponent();
		if(comp instanceof BigImagePanel){
			BigImagePanel bigImagePanel = (BigImagePanel) comp;
			bigImagePanel.imageReload();
		}
	}
}
