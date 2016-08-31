package Mahito6.UI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.ScrollPane;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Mahito6.Main.Constants;
import Mahito6.Main.Tuple2;
import Mahito6.Solver.Edge;
import Main.UI.Util.Coordinates;

public class VisualizeFrame extends JFrame implements KeyListener{
	private static VisualizeFrame mine;
	private VisualizePanel visPanel;
	private String title;
	private List<Tuple2<Double, Double>> vertex;
	private Coordinates coord;
	private List<Line2D> lines;
	private List<Edge> edges;
	private PieceViewPanel parent;
	
	public VisualizeFrame(List<Tuple2<Double, Double>> vertex, Coordinates coord, PieceViewPanel parent){
		this.vertex = vertex;
		this.coord = coord;
		this.parent = parent;
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
		setVisualizePanel();
		
		lines = new ArrayList<Line2D>();
		edges = new ArrayList<Edge>();
		//パネルサイズに合わせる
		pack();
	}
	
	private void setVisualizePanel(){
		visPanel = new VisualizePanel(vertex, coord);
		this.add(visPanel);
	}
	
	public static void changeTitle(String title){
		VisualizeFrame.mine.setTitle("Visualizer" + title);
	}
	
	public static void setVisibleFrame(boolean flag){
		VisualizeFrame.mine.setVisible(flag);
	}
	
	private Tuple2<Double, Double> calcHoughParam(Line2D source){
		double x1 = source.getX1();
		double y1 = source.getY1();
		double x2 = source.getX2();
		double y2 = source.getY2();
		double theta = 0.0;
		//too small case:
		if(y1-y2 < 0.000001){
			theta = Math.PI / 2;
		}else{
			theta = Math.atan((x2 - x1)/(y1 - y2));
		}
		double r = source.getX1()*Math.cos(theta) + source.getY1()*Math.sin(theta);
		return new Tuple2<Double, Double>(r, theta);
	}
	
	private Edge makeEdge(Tuple2<Double, Double> param, Line2D source){
		double r = param.t1;
		double theta = param.t2;
		double x1 = source.getX1();
		double y1 = source.getY1();
		double x2 = source.getX2();
		double y2 = source.getY2();
		return new Edge(r,theta,x1,y1,x2,y2);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S){
			lines = visPanel.getLines();
			
			for(int i = 0; i < lines.size(); i++){
				Line2D l = lines.get(i);
				edges.add(makeEdge(calcHoughParam(l), l));
			}
			//追加エッジを更新
			parent.updateEdges(edges);
			//追加エッジを考慮して頂点を検出し更新
			parent.updateVertex();
			parent.paintPiece();
			this.dispose();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}