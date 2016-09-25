package Mahito6.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Mahito6.Main.Constants;
import Mahito6.Main.Problem;
import Mahito6.Main.ProblemManager;
import Mahito6.Main.Tuple2;
import Mahito6.Solver.CrossAlgorithm;
import Mahito6.Solver.Edge;
import Mahito6.Solver.EdgeFinder;
import Mahito6.Solver.LeastSquareMethod;
import Main.UI.Util.Coordinates;

public class PieceViewPanel extends JPanel implements MouseListener{
	private int x, y, width, height, index;
	private JLabel paintArea, pieceIndex, pieceVertex, pieceType, errorType;
	private BufferedImage gPiece, image;
	private List<Tuple2<Double, Double>> vertex;
	private Polygon polygon;
	private Coordinates coord;
	private VisualizeFrame vis = null;
	private List<Edge> edges;
	private Problem myProblem;
	
	public PieceViewPanel(int x, int y, int width, int height, int index, Problem problem){
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.index = index;
		this.myProblem = problem;
		this.vertex = myProblem.getVertex(index);
		this.coord = myProblem.getCoord(index);
		this.edges = myProblem.getEdges(index);
		this.image = myProblem.getImage(index);
		setUtil();
		launchItems();
		paintPiece();
	}
	
	private void setUtil(){
		this.setLayout(null);
		this.setBackground(Color.WHITE);
		this.setBounds(x, y, width, height);
		this.setOpaque(true);
	}
	
	private void launchItems(){
		pieceIndex = new JLabel("index:"+String.valueOf(index+1));
		pieceVertex = new JLabel("Vertex:" + vertex.size());
		String type = (Constants.modeWaku)?"Frame":"Piece";
		if(type.equals("Frame"))this.setBackground(Color.GRAY.brighter());
		pieceType = new JLabel("Type:"+type);
		
		pieceIndex.setBounds(0,0,100,17);
		pieceVertex.setBounds(0,15,100,17);
		pieceType.setBounds(0,30,100,17);
		
		this.add(pieceIndex);
		this.add(pieceVertex);
		this.add(pieceType);
		
		
		gPiece = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		paintArea = new JLabel(new ImageIcon(gPiece));
		paintArea.setBounds(0, 50, 200, 200);
		paintArea.addMouseListener(this);
		this.add(paintArea);
		
		errorType = new JLabel();
		errorType.setOpaque(true);
		errorType.setBounds(150, 0, 50, 50);
		Color c = coord.isError() ? Color.RED : Color.GREEN;
		errorType.setBackground(c);
		this.add(errorType);
	}
	
	public void paintPiece(){
		Graphics2D g = (Graphics2D)gPiece.getGraphics();
		g.setColor(Constants.backgroundColor);
		g.clearRect(0, 0, 200, 200);		
	    List<Tuple2<Double,Double>> data = vertex;
	    double maxx = 0.0;
	    double maxy = 0.0;
	    int[] xpoints = new int[data.size()];
	    int[] ypoints = new int[data.size()];
	    for(int i = 0; i < data.size(); i++){
	    	Tuple2<Double, Double> d = data.get(i);
	    	double x = d.t1 - Constants.imagePositionOffset/4;
	    	double y = d.t2 - Constants.imagePositionOffset/4;;
	    	maxx = (x < maxx)?maxx:x;
	    	maxy = (y < maxy)?maxy:y;
	    	xpoints[i] = (int)x;
	    	ypoints[i] = (int)y;
	    }
	    
	    g.setColor(Constants.polyColor);
	    double scale = width/Math.max(maxx, maxy);
    	for(int i = 0 ; i < data.size(); i++){
	    	Tuple2<Double, Double> d1 = data.get(i);
	    	Tuple2<Double, Double> d2 = data.get((i+1)%data.size());
	    	double x1 = (d1.t1 - Constants.imagePositionOffset/3)*scale;
	    	double y1 = (d1.t2 - Constants.imagePositionOffset/3)*scale;
	    	double x2 = (d2.t1 - Constants.imagePositionOffset/3)*scale;
	    	double y2 = (d2.t2 - Constants.imagePositionOffset/3)*scale;
	    	g.draw(new Line2D.Double(x1, y1, x2, y2));
    	}

//		polygon = new Polygon(xpoints, ypoints, xpoints.length);
//		g.drawPolygon(polygon);
	    this.repaint();
	}
	
	public Problem getProblem(){
		return myProblem;
	}
	
	public int getIndex(){
		return index;
	}
	
	public void updateEdges(List<Edge> updateEdges){
//		System.out.println("before:" + edges.size());
		edges = new ArrayList<>(updateEdges);
		myProblem.setEdges(index, edges);
//		for(int i = 0; i < edges.size(); i++){
//			//エッジ伸ばす
//			edges.set(i, edges.get(i).getExtensionEdge(40));
//		}
//		System.out.println("after:" + this.edges.size());
//		System.out.println("distants:");
//		BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
//		Graphics2D g = (Graphics2D)output.createGraphics();
//		for(int i = 0 ; i< updateEdges.size(); i++){
//			Edge e = updateEdges.get(i);
//			System.out.println("Edge" + Integer.valueOf(i+1) + " : " + e.distance);
//			System.out.println(e.kx1 + "," + e.ky1 + " & " + e.kx2 + "," + e.ky2);
//			g.draw(new Line2D.Double(e.kx1, e.ky1, e.kx2, e.ky2));
//		}
//		g.drawImage(output, 0, 0, null);
//		try {
//			ImageIO.write(output, "png", new File("line.png"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	//頂点検出。humei 
	public void updateVertex(List<Tuple2<Double, Double>> list){
//		System.out.println(image.getWidth() + "x" + image.getHeight());
//		CrossAlgorithm solver2 = new CrossAlgorithm(edges,image.getWidth(),image.getHeight());
//		solver2.solve();
//		List<Tuple2<Double,Double>> ans = solver2.getAnswer();
//		System.out.println("--------------NO." +String.valueOf(index+1)+" answer updated--------------");
		this.vertex = new ArrayList<>(list);
//		for(Tuple2<Double,Double> t : ans){
//			System.out.println(t.t1+","+t.t2);
//		}
		myProblem.setVertex(index, vertex);
//		BufferedImage result3 = solver2.getAnswerImage();
//		File ans_save = new File("ans.png");
////		String pathStr = (getPath(String.valueOf(index)+"_ans_"));
////		System.out.println(pathStr);
//		try {
//			//ImageIO.write(result, "png", saveFile);
//			ImageIO.write(result3, "png", ans_save);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		errorType.setBackground(Color.ORANGE);
	}
	
	public List<Edge> getEdges(){
		return edges;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		vis = new VisualizeFrame(index, this);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}