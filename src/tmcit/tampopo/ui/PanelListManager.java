package tmcit.tampopo.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

public class PanelListManager extends JScrollPane{

	public static final int SCROLLBARINCREMENT = 15;

	private Component root;
	private PanelListManager own;
	private int WIDTH,HEIGHT;
	public JPanel mainPanel;
	public int heightSum;
	public int widthSum;
	public int heightMax;

	public PanelListManager(int WIDTH,int HEIGHT,boolean VERTICAL_NEVER,Component root){
		this.root = root;
		this.own = this;
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
		this.heightSum = 0;
		this.widthSum = 0;
		this.heightMax = 0;
		this.mainPanel = new JPanel(null);
		this.setViewportView(mainPanel);
		this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		if(VERTICAL_NEVER)
			this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		else{
			this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		}
		this.getVerticalScrollBar().setUnitIncrement(SCROLLBARINCREMENT);
		resize();
	}

	public void allClear(){
		mainPanel.removeAll();
		widthSum = 0;
		heightSum = 0;
		heightMax = 0;
		resize();
	}

	public void resize(){
		int newheight = Math.max(HEIGHT, heightSum + heightMax);
		Dimension newSize = new Dimension(WIDTH, newheight);
//		System.out.println("RESIZE:"+WIDTH+","+newheight);
		mainPanel.setPreferredSize(newSize);
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				root.revalidate();
				root.repaint();
			}
		});
	}

	public void setPanelBackgroundColor(Color backGround){
		mainPanel.setBackground(backGround);
	}

	public void addPanel(Component panel){
		int height = panel.getPreferredSize().height;
		int width = panel.getPreferredSize().width;
//		System.out.println(width+","+height);
		heightMax = Math.max(heightMax, height);
		if((widthSum + width) > WIDTH){
			///改行
			widthSum = 0;
			heightSum += heightMax;
			heightMax = height;
		}
		panel.setBounds(widthSum, heightSum, width, height);
		mainPanel.add(panel);
		widthSum += width;
		resize();
	}

	public void removePanel(Component panel){
		List<Component> components = new ArrayList<Component>();
		for(Component comp : mainPanel.getComponents()){
			if(comp.equals(panel))continue;///同じのは入れない
			components.add(comp);
		}
		allClear();
		for(Component comp : components){
			addPanel(comp);
		}
		resize();
	}

	public void addPanel(DetailPanel detailPanel, int index) {
		List<Component> components = new ArrayList<Component>();
		int count = 0;
		for(Component comp : mainPanel.getComponents()){
			if(count == index){
				components.add(detailPanel);
			}
			components.add(comp);
			count++;
		}
		allClear();
		for(Component comp : components){
			addPanel(comp);
		}
		resize();
	}

}
