package Mahito6.UI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Mahito6.Main.Tuple2;

public class VisualizePiece extends JPanel{
	private BufferedImage gPiece;
	private List< List<Tuple2<Double, Double>> > vertex;
	private int nowPiece;
    private Polygon polygon;
    private JLabel earth;
    
    private static double scale = 1.0;
	public VisualizePiece(){
		nowPiece = 0;
		setUtil();
	}
	
	private void setUtil(){
		this.setLayout(null);
		this.setBackground(Color.gray.brighter().brighter());
		this.setBounds(0, 0, MainFrame.frame_width/4*3, MainFrame.frame_height-50);
		this.setOpaque(true);
		
		gPiece = new BufferedImage(MainFrame.frame_width, MainFrame.frame_height, BufferedImage.TYPE_INT_ARGB);
		earth = new JLabel(new ImageIcon(gPiece));
		earth.setBounds(0, 0, MainFrame.frame_width, MainFrame.frame_height);
		this.add(earth);
	}
	
	public void setVertex(List< List<Tuple2<Double, Double>> > vertex){
		this.vertex = vertex;
		paintPiece(nowPiece);
	}
	
	public void paintPiece(int index){
		if(vertex.isEmpty()){
			System.out.println("Vertex is not set.");
			return;
		}
		Graphics2D g = (Graphics2D)gPiece.getGraphics();

	    BasicStroke normalStroke = new BasicStroke(5.0f);
	    g.setStroke(normalStroke);
		g.setPaint(Color.BLACK);
		
	    List<Tuple2<Double,Double>> data = vertex.get(index);
	    int[] xpoints = new int[data.size()];
	    int[] ypoints = new int[data.size()];
	    for(int i = 0; i < data.size(); i++){
	    	Tuple2<Double, Double> d = data.get(i);
	    	double x = d.t1;
	    	double y = d.t2;
	    	xpoints[i] = (int)(x*scale);
	    	ypoints[i] = (int)(y*scale);
	    	System.out.println(xpoints[i] + "," + ypoints[i]);
	    }
	    
		g.setBackground(Color.gray.brighter().brighter());
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		polygon = new Polygon(xpoints, ypoints, xpoints.length);
		g.drawPolygon(polygon);
		g.setPaint(new Color(183,156,139));
		g.fillPolygon(polygon);
	    this.repaint();
		String nowState = "(" + String.valueOf(nowPiece+1) + "/" + String.valueOf(vertex.size()) + ")";
		MainFrame.changeTitle(nowState);
	}
	
	public void Back(){
		System.out.println("Back");
		if(vertex == null)return;
		nowPiece = (nowPiece + vertex.size() -1)%vertex.size();
		paintPiece(nowPiece);
	}
	
	public void Next(){
		System.out.println("Next");
		if(vertex == null)return;
		nowPiece = (nowPiece+1)%vertex.size();
		paintPiece(nowPiece);
	}
}