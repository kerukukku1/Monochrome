package Mahito6.UI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import Mahito6.Main.Constants;
import Mahito6.Main.Tuple2;
import Main.UI.Util.Coordinates;

public class CorrectDialog extends JDialog implements MouseListener, KeyListener, MouseMotionListener{
	private int x, y, range;
	private static int[] dx;
	private static int[] dy;
	private List<Tuple2<Integer, Integer>> plots;
	private List<Tuple2<Integer, Integer>> viewPlots;
	private BufferedImage img, paint;
	private JLabel earth;
    private List<Tuple2<Double, Double>> vertex;
    private Coordinates coord;
    private double scale;
    private VisualizePanel parent = null;
    private int[] ypoints;
    private int[] xpoints;
    
	public CorrectDialog(int x, int y, int range, List<Tuple2<Double, Double>> vertex, Coordinates coord, double scale, VisualizePanel parent){
		this.x = x;
		this.y = y;
		this.parent = parent;
		this.vertex = vertex;
		this.coord = coord;
		this.range = range;
		this.scale = scale;
		plots = new ArrayList<Tuple2<Integer, Integer>>();
		viewPlots = new ArrayList<Tuple2<Integer, Integer>>();
		setUtil();
		makeDXDY(this.range);
		paintBackground();
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}
	
	private void setUtil(){
		this.setTitle("TEST");
		this.setVisible(true);
		this.setLayout(null);
		this.setSize(this.range*2,this.range*2);
		img = new BufferedImage(range*2, range*2, BufferedImage.TYPE_INT_ARGB);
		paint = new BufferedImage(range*2, range*2, BufferedImage.TYPE_INT_ARGB);
		earth = new JLabel(new ImageIcon(paint));
		earth.setBounds(0, 0, range*2, range*2);
		earth.addMouseListener(this);
		earth.addMouseMotionListener(this);
		this.add(earth);
		this.addKeyListener(this);
		
		BufferedImage image = new BufferedImage(16,16,BufferedImage.TYPE_4BYTE_ABGR);  
		Graphics2D g2 = image.createGraphics();  
		//黒で透明 black & transparency  
		g2.setColor(new Color(0,0,0,0));     
		g2.fillRect(0,0,16,16);  
		g2.dispose();  
		earth.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0,0), "null_cursor"));
		
		this.setResizable(false);
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
	    xpoints = new int[data.size()];
	    ypoints = new int[data.size()];
	    for(int i = 0; i < data.size(); i++){
	    	Tuple2<Double, Double> d = data.get(i);
	    	double tx = d.t1 + (range) - this.x/scale;
	    	double ty = d.t2 + (range) - this.y/scale;
	    	//System.out.println(tx + "," + ty);
	    	maxx = (tx < maxx)?maxx:tx;
	    	maxy = (ty < maxy)?maxy:ty;
	    	xpoints[i] = (int)tx;
	    	ypoints[i] = (int)ty;
	    	//System.out.println(xpoints[i] + "," + ypoints[i]);
	    }
	    
	    Polygon polygon = new Polygon(xpoints, ypoints, xpoints.length);
		g.setColor(Color.YELLOW);
		BasicStroke wideStroke = new BasicStroke(4.0f);
		g.setStroke(wideStroke);
		g.drawPolygon(polygon);
		
		g.setColor(Color.WHITE);
	    for(int i = 0; i < coord.size(); i++){
	    	double nowx = (coord.getVisX(i) + Constants.imagePositionOffset/2) +range - this.x/scale;
	    	double nowy = (coord.getVisY(i) + Constants.imagePositionOffset/2) +range - this.y/scale;
	    	if(nowx < 0 || nowy < 0 || nowx >= range*2+1 || nowy >=range*2+1)continue;
	    	g.fillRect((int)nowx, (int)nowy, 1, 1);
	    }
	    
		g.drawImage(img, null, 0, 0);
		drawBackground();
	}
	
	private void drawBackground(){
		Graphics2D g = (Graphics2D)paint.getGraphics();
		g.drawImage(img, 0, 0, earth);
		this.repaint();
	}
	
	public List<Tuple2<Integer, Integer>> getPlots(){
		return plots;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		viewPlots.add(new Tuple2<Integer, Integer>((int)e.getX(), (int)e.getY()));
		int nx = (int)e.getX() - range + (int)((double)this.x/scale);
		int ny = (int)e.getY() - range + (int)((double)this.y/scale);
		plots.add(new Tuple2<Integer, Integer>((int)((double)nx*scale), (int)((double)ny*scale)));
		
		Graphics2D g = (Graphics2D)img.getGraphics();
		g.setColor(Color.red);
		for(int i = 0; i < plots.size(); i++){
			g.fillOval(viewPlots.get(i).t1 - 4, viewPlots.get(i).t2 - 4, 8, 8);
		}
		g.drawImage(img, null, 0, 0);
		drawBackground();
		this.repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			parent.paintPlots(plots);
			this.dispose();
			VisualizeFrame.setVisibleFrame(true);
		}else{
			
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void mouseDragged(MouseEvent e) {}
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		//背景を描画
		int x = e.getX();
		int y = e.getY();
		drawBackground();
		Graphics2D g = (Graphics2D)paint.getGraphics();
		g.setColor(Color.RED);
		g.drawOval(x-4, y-4, 8, 8);
		g.drawOval(x-8, y-8, 16, 16);
	    g.drawLine(0, y, range*2, y);
	    g.drawLine(x, 0, x, range*2);
	    this.repaint();
	}
}