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
		VisualizeFrame.mine = this;
		title = "Visualize";
		launchUI();
		this.requestFocusInWindow();
		this.setVisible(true);
		this.addKeyListener(this);
	}
	
	private void launchUI() {
		this.setTitle(title);
		this.setResizable(false);
		lines = makeLine2D(this.vertex);
		edges = new ArrayList<Edge>();
		
		setVisualizePanel();
		//パネルサイズに合わせる
		pack();
	}
	
	private List<Line2D> makeLine2D(List<Tuple2<Double, Double>> source){
		List<Line2D> ret = new ArrayList<Line2D>();
		for(int i = 0; i < source.size(); i++){
			Tuple2<Double, Double> nt1 = source.get(i);
			Tuple2<Double, Double> nt2 = source.get((i+1)%source.size());
			ret.add(new Line2D.Double(nt1.t1, nt1.t2, nt2.t1, nt2.t2));
		}
		return ret;
	}
	
	private void setVisualizePanel(){
		visPanel = new VisualizePanel(vertex, coord, lines, this);
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
		if(Math.abs(y1-y2) < 0.000001){
			theta = Math.PI / 2;
		}else{
			theta = Math.atan((x2 - x1)/(y1 - y2));
		}
		double r = x1*Math.cos(theta) + y1*Math.sin(theta);
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
		if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Q){
			System.out.println("Save");
			lines = visPanel.getLines();
			for(int i = 0; i < lines.size(); i++){
				lines.set(i, visPanel.expandLine(lines.get(i)));
			}
			for(int i = 0; i < lines.size(); i++){
				Line2D l = lines.get(i);
				edges.add(makeEdge(calcHoughParam(l), l));
			}
			System.out.println("edges:" + edges.size());
			
			//エッジを更新
			parent.updateEdges(edges);
			//エッジを考慮して頂点を検出し更新
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