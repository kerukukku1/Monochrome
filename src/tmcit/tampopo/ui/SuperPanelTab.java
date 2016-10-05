package tmcit.tampopo.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTabbedPane;

import tmcit.tampopo.ui.util.ParameterNode;

public class SuperPanelTab extends JTabbedPane{
	
	public SolverPanel source;
	public int WIDTH,HEIGHT;
	public List<SuperPanel> superPanels;
	
	public SuperPanelTab(int WIDTH,int HEIGHT,SolverPanel source){
		this.source = source;
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		superPanels = new ArrayList<SuperPanel>();
	}
	
	public void setViewSuperPanel(SuperPanel superPanel){
		this.setSelectedComponent(superPanel);
	}
	
	public SuperPanel getViewingSuperPanel(){
		Component component = this.getSelectedComponent();
		if(component == null)return null;
		return (SuperPanel) component;
	}
	
	public void allClear(){
		for(SuperPanel superPanel : superPanels){
			this.remove(superPanel);
		}
		superPanels.clear();
	}
	
	public SuperPanel addSuperPanel(String tabName,ParameterNode parameterNode){
		SuperPanel superPanel = new SuperPanel(parameterNode,source);
		superPanels.add(superPanel);
		this.addTab(tabName, superPanel);
		return superPanel;
	}
	
	public SuperPanel getSuperPanel(ParameterNode parameterNode){
		///線形探索
		for(SuperPanel superPanel : superPanels){
			if(superPanel.getParameterNode() == parameterNode){
				return superPanel;
			}
		}
		return null;
	}

}
