package Mahito6.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Mahito6.Main.Constants;
import Mahito6.Main.Tuple2;
import Main.UI.Util.Coordinates;

public class PieceViewPanel extends JPanel implements MouseListener{
	private int x, y, width, height, index;
	private JLabel paintArea, pieceIndex, pieceVertex, pieceType, errorType;
	private BufferedImage image;
	private List<Tuple2<Double, Double>> vertex;
	private Polygon polygon;
	private Coordinates coord;
	public PieceViewPanel(int x, int y, int width, int height, int index, List<Tuple2<Double, Double>> vertex, Coordinates coord){
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.index = index;
		this.vertex = vertex;
		this.coord = coord;
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
		pieceType = new JLabel("Type:"+type);
		
		pieceIndex.setBounds(0,0,100,17);
		pieceVertex.setBounds(0,15,100,17);
		pieceType.setBounds(0,30,100,17);
		
		this.add(pieceIndex);
		this.add(pieceVertex);
		this.add(pieceType);
		
		
		image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		paintArea = new JLabel(new ImageIcon(image));
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
	
	private void paintPiece(){
		Graphics2D g = (Graphics2D)image.getGraphics();
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
	    
	    double scale = width/Math.max(maxx, maxy);
    	maxx = 0.0;
    	maxy = 0.0;
    	for(int i = 0 ; i < data.size(); i++){
	    	Tuple2<Double, Double> d = data.get(i);
	    	double x = (d.t1 - Constants.imagePositionOffset/3)*scale;
	    	double y = (d.t2 - Constants.imagePositionOffset/3)*scale;
	    	maxx = (x < maxx)?maxx:x;
	    	maxy = (y < maxy)?maxy:y;
	    	xpoints[i] = (int)x;
	    	ypoints[i] = (int)y;	    		
    	}

		polygon = new Polygon(xpoints, ypoints, xpoints.length);
		g.setColor(Constants.polyColor);
		g.drawPolygon(polygon);
	    this.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		VisualizeFrame vis = new VisualizeFrame(vertex, coord);
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