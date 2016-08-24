package Mahito6.UI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Mahito6.Main.Tuple2;

public class VisualizePanel extends JPanel{
	
	private BufferedImage gPiece;
	private int nowPiece;
    private Polygon polygon;
    private JLabel earth;
    private List< List<Tuple2<Double, Double>> > vertex;
    private double scale = 1.0;
    
    public VisualizePanel(List< List<Tuple2<Double, Double>> > vertex){
    	this.vertex = vertex;
		nowPiece = 0;
		setUtil();
		paintPiece(nowPiece);
    }
	
	private void setUtil(){
		this.setLayout(null);
		this.setBackground(Color.gray.brighter().brighter());
		this.setPreferredSize(new Dimension(300, 300));
		this.setOpaque(true);

	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		gPiece = new BufferedImage(screenSize.width, screenSize.height, BufferedImage.TYPE_INT_ARGB);
		earth = new JLabel(new ImageIcon(gPiece));
		earth.setBounds(0, 0, screenSize.width, screenSize.height);
		this.add(earth);
	
		ScrollPane Sc = new ScrollPane(ScrollPane.SCROLLBARS_ALWAYS);
		this.add(Sc);
	}
	
	public void paintPiece(int index){
		if(vertex.isEmpty()){
			System.out.println("Vertex is not set.");
			return;
		}
		Graphics2D g = (Graphics2D)gPiece.getGraphics();
//
//	    BasicStroke normalStroke = new BasicStroke(5.0f);
//	    g.setStroke(normalStroke);
//		g.setPaint(Color.BLACK);
		
	    List<Tuple2<Double,Double>> data = vertex.get(index);
	    double maxx = 0.0;
	    double maxy = 0.0;
	    int[] xpoints = new int[data.size()];
	    int[] ypoints = new int[data.size()];
	    for(int i = 0; i < data.size(); i++){
	    	Tuple2<Double, Double> d = data.get(i);
	    	double x = d.t1;
	    	double y = d.t2;
	    	maxx = (x < maxx)?maxx:x;
	    	maxy = (y < maxy)?maxy:y;
	    	xpoints[i] = (int)(x*scale);
	    	ypoints[i] = (int)(y*scale);
	    	System.out.println(xpoints[i] + "," + ypoints[i]);
	    }
	    
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    double w = (double)screenSize.width;
	    double h = (double)screenSize.height;
	    //System.out.println("w:" + w + " h:" + h);
	    if(maxx > w){
	    	w -= 100;
	    	double wscale = w/maxx;
	    	maxx = 0.0;
	    	maxy = 0.0;
	    	for(int i = 0 ; i < data.size(); i++){
		    	Tuple2<Double, Double> d = data.get(i);
		    	double x = d.t1*wscale;
		    	double y = d.t2*wscale;
		    	maxx = (x < maxx)?maxx:x;
		    	maxy = (y < maxy)?maxy:y;
		    	xpoints[i] = (int)x;
		    	ypoints[i] = (int)y;	    		
	    	}
	    }else if(maxy > h){
	    	h -= 100;
	    	double hscale = h/maxy;
	    	maxx = 0.0;
	    	maxy = 0.0;
	    	for(int i = 0 ; i < data.size(); i++){
		    	Tuple2<Double, Double> d = data.get(i);
		    	double x = d.t1*hscale;
		    	double y = d.t2*hscale;
		    	maxx = (x < maxx)?maxx:x;
		    	maxy = (y < maxy)?maxy:y;
		    	xpoints[i] = (int)x;
		    	ypoints[i] = (int)y;	    		
	    	}
	    }
	    
		this.setPreferredSize(new Dimension((int)(maxx+20), (int)(maxy+20)));
		g.setColor(Color.black);
		g.clearRect(0, 0, (int)(maxx+20), (int)(maxy+20));
		polygon = new Polygon(xpoints, ypoints, xpoints.length);
		g.setColor(Color.YELLOW);
		g.drawPolygon(polygon);
		//g.setPaint(new Color(183,156,139));
		//g.fillPolygon(polygon);
	    this.repaint();
		String nowState = "(" + String.valueOf(nowPiece+1) + "/" + String.valueOf(vertex.size()) + ")";
		VisualizeFrame.changeTitle(nowState);
	}
	
	public void Back(){
		System.out.println("Back");
		nowPiece = (nowPiece + vertex.size() -1)%vertex.size();
		paintPiece(nowPiece);
	}
	
	public void Next(){
		System.out.println("Next");
		nowPiece = (nowPiece+1)%vertex.size();
		paintPiece(nowPiece);
	}

}