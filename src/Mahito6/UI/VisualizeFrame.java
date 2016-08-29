package Mahito6.UI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.ScrollPane;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Mahito6.Main.Constants;
import Mahito6.Main.Tuple2;
import Main.UI.Util.Coordinates;

public class VisualizeFrame extends JFrame{
	private static VisualizeFrame mine;
	private VisualizePanel visPanel;
	private String title;
	private List<Tuple2<Double, Double>> vertex;
	private Coordinates coord;
	
	public VisualizeFrame(List<Tuple2<Double, Double>> vertex, Coordinates coord){
		this.vertex = vertex;
		this.coord = coord;
		this.mine = this;
		title = "Visualize";
		launchUI();
		this.requestFocusInWindow();
		this.setVisible(true);
	}
	
	private void launchUI() {
		this.setTitle(title);
		this.setResizable(false);
		setVisualizePanel();
		//パネルサイズに合わせる
		pack();
	}
	
	private void setVisualizePanel(){
		visPanel = new VisualizePanel(vertex);
		this.add(visPanel);
	}
	
	public static void changeTitle(String title){
		VisualizeFrame.mine.setTitle("Visualizer" + title);
	}
	
	public static void setVisibleFrame(boolean flag){
		VisualizeFrame.mine.setVisible(flag);
	}
}