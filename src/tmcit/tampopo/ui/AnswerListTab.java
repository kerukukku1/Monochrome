package tmcit.tampopo.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import tmcit.tampopo.util.Answer;

public class AnswerListTab extends DeletableTabbedPane implements ChangeListener,ActionListener{
	
	public HashMap<Component, Component> rootMemo;
	///ここに中身と親コンポーネントを紐づける、既に表示しているかどうか分かるようにする
	
	public AnswerListTab(int WIDTH,int HEIGHT){
		super();
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		this.rootMemo = new HashMap<Component,Component>();
		this.setListener(this);
		initTab();
	}
	
	public Component getTabFromComponent(Component rootComponent){
		///親コンポーネントからゲットする
		return rootMemo.get(rootComponent);
	}
	
	public void setView(Component rootComponent){
		Component target = rootMemo.get(rootComponent);
		this.setSelectedComponent(target);
	}
	
	public void removeRootMemo(Component child){
		Component target = null;
		for (Iterator<Map.Entry<Component, Component>> it = rootMemo.entrySet().iterator(); it.hasNext(); ) {
            // entryを取得
            Map.Entry<Component, Component> entry = it.next();
            // キーを取得
            Component rootComponent = entry.getKey();
            // 値を取得
            Component childComponent = entry.getValue();
            if(child.equals(childComponent)){
            	target = rootComponent;
            	break;
            }
        }
		if(target != null){
			rootMemo.put(target, null);
		}else{
			for(int i = 0;i < 10;i++)
				System.out.println("OKOKOKOKOKOKO");
		}
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
			deleteOpeningTab(false);
		}
	}
	
	public void addTabAndSelect(String title,Component component, Component rootComponent){
		///タブ追加してその画面を見る
		this.addTab(title, component);
		this.setSelectedComponent(component);
		rootMemo.put(rootComponent, component);
	}

	public void addTab(String title, Component component, Component rootComponent) {
		this.addTab(title, component);
		rootMemo.put(rootComponent, component);
	}
	
	public void deleteOpeningTab(boolean masterCheck){///masterCheck 1:消さない 0:消す
		///表示中のコンポーネントをタブから削除
		int nowTab = this.getSelectedIndex();
		Component child = this.getSelectedComponent();
		if(nowTab == -1)return;
		deleteTab(child,masterCheck);
	}
	
	public void deleteTab(Component child,boolean masterCheck){///masterCheck 1:消さない 0:消す
		///表示中のコンポーネントをタブから削除
//		int nowTab = this.get;
//		if(nowTab == -1)return;
		if(child instanceof MiniListPanel){
			MiniListPanel miniListPanel = (MiniListPanel) child;
			if(miniListPanel.detailPanel != null)
				miniListPanel.detailPanel.listOff();
		}
		if(child instanceof BigImagePanel){
			BigImagePanel bigImagePanel = (BigImagePanel) child;
			if(masterCheck && bigImagePanel.title.equals("Master"))return;
			if(bigImagePanel.detailPanel != null)
				bigImagePanel.detailPanel.listOff();
		}
		this.remove(child);
		this.removeRootMemo(child);
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

	@Override
	public void actionPerformed(ActionEvent event) {
		DeletableMyButton button = (DeletableMyButton) event.getSource();
		deleteTab(button.getSource(), true);
	}

}
