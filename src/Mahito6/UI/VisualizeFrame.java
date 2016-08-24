package Mahito6.UI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.ScrollPane;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Mahito6.Main.Constants;
import Mahito6.Main.Tuple2;

public class VisualizeFrame extends JFrame implements KeyListener{
	private static VisualizeFrame mine;
	private VisualizePanel visPanel;
	private String title;
	private List< List<Tuple2<Double, Double>> > vertex;
	
	public VisualizeFrame(List< List<Tuple2<Double, Double>> > vertex){
		this.vertex = vertex;
		this.mine = this;
		title = "Visualize";
		launchUI();
		this.requestFocusInWindow();
		this.setVisible(true);
		this.addKeyListener(this);
	}
	
	private void launchUI() {
		this.setTitle(title);
		this.setResizable(false);
		this.setSize(600, 600);
		setVisualizePanel();
		//パネルサイズに合わせる
		//pack();
	}
	
	private void setVisualizePanel(){
		visPanel = new VisualizePanel(vertex);
		this.add(visPanel);
	}
	
	public static void changeTitle(String title){
		VisualizeFrame.mine.setTitle("Visualizer" + title);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			System.out.println("Right");
			visPanel.Next();
			//pack();
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT){
			System.out.println("LEFT");
			visPanel.Back();
			//pack();
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}