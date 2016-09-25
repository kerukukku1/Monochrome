package tmcit.tampopo.ui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

public class PanelListManager extends JScrollPane{
	
	public static final int SCROLLBARINCREMENT = 15;
	
	private PanelListManager own;
	private int WIDTH,HEIGHT;
	public JPanel mainPanel;
	public List<JPanel> jPanels;
	public int heightSum;
	
	public PanelListManager(int WIDTH,int HEIGHT){
		this.own = this;
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
		this.jPanels = new ArrayList<JPanel>();
		this.heightSum = 0;
		this.mainPanel = new JPanel(null);
		this.setViewportView(mainPanel);
		this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.getVerticalScrollBar().setUnitIncrement(SCROLLBARINCREMENT);
		resize();
	}
	
	public void resize(){
		Dimension newSize = new Dimension(WIDTH, Math.max(HEIGHT, heightSum));
		mainPanel.setPreferredSize(newSize);
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				own.revalidate();
				own.repaint();
			}
		});
	}
	
	public void addPanel(JPanel panel){
		jPanels.add(panel);
		int height = panel.getPreferredSize().height;
		panel.setBounds(0, heightSum, WIDTH, height);
		mainPanel.add(panel);
		heightSum += height;
		resize();
	}
	
	public void removePanel(JPanel panel){
		resize();
	}

}
