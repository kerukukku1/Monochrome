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
import javax.swing.JDialog;
import javax.swing.JLabel;

import Mahito6.Main.Tuple2;

public class CorrectDialog extends JDialog implements MouseListener{
	private int x, y, range;
	private static int[] dx;
	private static int[] dy;
	private BufferedImage img;
	private JLabel earth;
    private List<Tuple2<Double, Double>> vertex;
    private double scale;
    
	public CorrectDialog(int x, int y, int range, List<Tuple2<Double, Double>> vertex, double scale){
		this.x = x;
		this.y = y;
		this.vertex = vertex;
		this.range = range;
		this.scale = scale;
		setUtil();
		makeDXDY(this.range);
		paintBackground();
		this.addMouseListener(this);
	}
	
	private void setUtil(){
		this.setTitle("TEST");
		this.setVisible(true);
		this.setLayout(null);
		this.setSize(this.range*2,this.range*2);
		img = new BufferedImage(range*2, range*2, BufferedImage.TYPE_INT_ARGB);
		earth = new JLabel(new ImageIcon(img));
		earth.setBounds(0, 0, range*2, range*2);
		this.add(earth);
	}
	
	public static void makeDXDY(int range){
		dx = new int[(range*2+1)*(range*2+1)-1];
		dy = new int[(range*2+1)*(range*2+1)-1];
		int count = 0;
		for(int i = -range; i <= range; i++){
			for(int j = -range; j <= range; j++){
				if(i == 0 && j == 0)continue;
				dx[count] = i;
				dy[count] = j;
				count++;
			}
		}
	}
	
	private void paintBackground(){
		Graphics2D g = (Graphics2D)img.getGraphics();
		g.setColor(Color.black);
		g.clearRect(0, 0, range*2, range*2);
		
	    List<Tuple2<Double,Double>> data = this.vertex;
	    double maxx = 0.0;
	    double maxy = 0.0;
	    int[] xpoints = new int[data.size()];
	    int[] ypoints = new int[data.size()];
	    for(int i = 0; i < data.size(); i++){
	    	Tuple2<Double, Double> d = data.get(i);
	    	double tx = d.t1 + (range) - this.x/scale;
	    	double ty = d.t2 + (range) - this.y/scale;
	    	System.out.println(tx + "," + ty);
	    	maxx = (tx < maxx)?maxx:tx;
	    	maxy = (ty < maxy)?maxy:ty;
	    	xpoints[i] = (int)tx;
	    	ypoints[i] = (int)ty;
	    	//System.out.println(xpoints[i] + "," + ypoints[i]);
	    }
	    
	    Polygon polygon = new Polygon(xpoints, ypoints, xpoints.length);
		g.setColor(Color.YELLOW);
		g.drawPolygon(polygon);
	    this.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		Graphics2D g = (Graphics2D)img.getGraphics();
		g.setColor(Color.red);
		g.fillOval((int)e.getX()-2 - 4, (int)e.getY()-27 - 4, 8, 8);
		this.repaint();
		System.out.println("Clicked");
		System.out.println(e.getX() + "," + e.getY());
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