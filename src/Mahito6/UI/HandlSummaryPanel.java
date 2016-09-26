package Mahito6.UI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Mahito6.Main.Main;
import Mahito6.Main.Constants;
import Mahito6.Main.Problem;
import Mahito6.Main.ProblemManager;
import Mahito6.Main.Tuple2;
import Mahito6.Solver.CrossAlgorithm;
import Mahito6.Solver.Edge;
import Mahito6.Solver.EdgeFinder;
import Mahito6.Solver.LeastSquareMethod;
import Main.UI.Util.Coordinates;

public class HandlSummaryPanel extends JPanel implements KeyListener{
	private static HandlSummaryPanel mine;
	private VisualizePanel visPanel;
	private String title;
	private List<Tuple2<Double, Double>> vertex;
	private Coordinates coord;
	private List<Line2D> lines;
	private List<Edge> edges;
	private PieceViewPanel parent;
	private RealTimeDialog realtimeDialog;
	private ParameterPanel paramPanel;
	private int index;
	public static final int visualizeWidth = Main.pieceView.getTabSize().width - 330;
	public static final int visualizeHeight = Main.pieceView.getTabSize().height - 50;
	private Problem myProblem;
	
	public HandlSummaryPanel(int index, PieceViewPanel parent){
		init(index, parent);
		HandlSummaryPanel.mine = this;
//		title = "Visualize";
		launchUI();
		this.requestFocusInWindow();
		this.setVisible(true);
		this.addKeyListener(this);
	}
	
	private void init(int index, PieceViewPanel parent){
		this.index = index;
		this.myProblem = parent.getProblem();
		this.vertex = myProblem.getVertex(index);
		this.coord = myProblem.getCoord(index);
		this.parent = parent;
	}
	
	private void launchUI() {
//		this.setTitle(title);
		this.setSize(HandlSummaryPanel.visualizeWidth + 300+15, HandlSummaryPanel.visualizeHeight+35);
//		this.setResizable(false);
		this.setLayout(null);
//	    this.getContentPane().setBackground(Color.BLUE.darker().darker());
		this.setBackground(Color.BLUE.darker().darker());
		lines = makeLine2D(this.vertex);
		edges = new ArrayList<Edge>();
		
		setVisualizePanel();
		setParameterPanel();
	}
	
	private void setParameterPanel() {
		// TODO Auto-generated method stub
		paramPanel = new ParameterPanel(this);
		paramPanel.setBounds(visualizeWidth+10, 300+10, 300, visualizeHeight-300-5);
		this.add(paramPanel);
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
		visPanel.setBounds(5, 5, HandlSummaryPanel.visualizeWidth, HandlSummaryPanel.visualizeHeight);
		this.add(visPanel);
	}
	
	public void setRealTimeDialog(RealTimeDialog realtimeDialog){
		this.realtimeDialog = realtimeDialog;
		this.realtimeDialog.setBounds(HandlSummaryPanel.visualizeWidth+10, 5, realtimeDialog.range*2, realtimeDialog.range*2);
		this.add(realtimeDialog);
	}
	
	public void removeRealTimeDialog(){
		this.remove(realtimeDialog);
	}
	
	public static void changeTitle(String title){
//		VisualizeFrame.mine.setTitle("Visualizer" + title);
	}
	
	public static void setVisibleFrame(boolean flag){
		HandlSummaryPanel.mine.setVisible(flag);
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
	
	public Problem getProblem(){
		return myProblem;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void saveData(){
		System.out.println("Save");
		lines = new ArrayList<>(visPanel.getLines());
		for(int i = 0; i < lines.size(); i++){
			Line2D l = lines.get(i);
			edges.add(makeEdge(calcHoughParam(l), l));
		}
		System.out.println("edges:" + edges.size());
		
		//エッジを更新
		parent.updateEdges(edges);
		//エッジを考慮して頂点を検出し更新
		parent.updateVertex(visPanel.getVertex());
		parent.paintPiece();
	}
	
	public void calcLeastSquare(Line2D line, int _index){
		BufferedImage image = this.getImage();
		EdgeFinder ef = new EdgeFinder(image, true, paramPanel.getConstants());
		LeastSquareMethod lsm = new LeastSquareMethod(image, paramPanel.getConstants());
		Edge e = makeEdge(calcHoughParam(line), line);
		Tuple2<Double,Double> ansConverted = lsm.detectAndConvert(e);///preAnsを最小二乗法によって精度上げる
		if(ansConverted == null){
			///最小二乗法 or split失敗により強制終了、無限ループ回避用
//			this.setTitle("LeastSquare Error!");
			return;
		}
		Edge ans = ef.split(image,ansConverted.t1,ansConverted.t2);///正しい長さにスプリットする
		BufferedImage result3 = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_BGR);
		Graphics2D g = (Graphics2D)result3.getGraphics();
		lines = visPanel.getLines();
		for(int i = 0; i < lines.size(); i++){
			if(_index == i){
				g.draw(line);
				edges.add(ans);
			}else{
				Line2D l = visPanel.expandLine(lines.get(i), 10);
				g.draw(l);
				edges.add(makeEdge(calcHoughParam(l), l));	
			}
		}
		CrossAlgorithm solver2 = new CrossAlgorithm(edges,image.getWidth(),image.getHeight());
		solver2.solve();
		List<Tuple2<Double,Double>> answer = solver2.getAnswer();
		relaunch(answer, false);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_W){
//			this.dispose();
		}else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S){
			saveData();
		}else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			next();
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT){
			back();
		}
	}
	
	public void next(){
		System.out.println("RIGHT");
		int nowAllIndex = PieceListView.pieceViews.indexOf(parent);
		int next = (nowAllIndex+1)%PieceListView.pieceViews.size();
		PieceViewPanel pvp = PieceListView.pieceViews.get(next);
		init(pvp.getIndex(), pvp);
		lines = makeLine2D(vertex);
		visPanel.relaunchPanel(vertex, coord, lines, this);
	}
	
	public void back(){
		System.out.println("LEFT");
		int nowAllIndex = PieceListView.pieceViews.indexOf(parent);
		int back = ((nowAllIndex-1)+PieceListView.pieceViews.size())%PieceListView.pieceViews.size();
		PieceViewPanel pvp = PieceListView.pieceViews.get(back);
		init(pvp.getIndex(), pvp);
		lines = makeLine2D(vertex);
		visPanel.relaunchPanel(vertex, coord, lines, this);
	}
	
	public BufferedImage getImage(){
		return myProblem.getImage(index);
	}
	
	//頂点を更新して再描画させる
	public void relaunch(List<Tuple2<Double, Double>> vertex, boolean isInit){
		this.vertex = new ArrayList<>(vertex);
		lines = makeLine2D(vertex);
		edges = new ArrayList<Edge>();
		visPanel.launchPanel(vertex, coord, lines, isInit);
		System.out.println(vertex.size());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public RealTimeDialog getRealTimeDialog() {
		return realtimeDialog;
	}
}