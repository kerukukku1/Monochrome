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
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import Mahito6.Main.Constants;
import Mahito6.Main.Tuple2;
import Mahito6.Solver.Edge;
import Main.UI.Util.Coordinates;

public class CorrectDialog extends JDialog implements MouseListener, KeyListener, MouseMotionListener{
	private int x, y, range;
	private static int[] dx;
	private static int[] dy;
	private List<Tuple2<Double, Double>> plots;
	private List<Tuple2<Integer, Integer>> focusPlots;
	private List<Line2D> lines;
	private List<Line2D> focusLines;
	private int dragPlotIndex;
	private List<Integer> dragLineIndexes;
	private BufferedImage img, paint;
	private boolean isDrag = false;
	private JLabel earth;
    private List<Tuple2<Double, Double>> vertex;
    private Coordinates coord;
    private double scale;
    private VisualizePanel parent = null;
    private int[] ypoints;
    private int[] xpoints;
    private VisualizeFrame owner;
    
	public CorrectDialog(int x, int y, int range, VisualizePanel parent, VisualizeFrame owner){
        super(owner, "look", false);
        setBounds(parent.getWidth(), 0, range*2, range*2);
		this.x = x;
		this.y = y;
		this.owner = owner;
		this.parent = parent;
		this.vertex = parent.getVertex();
		this.coord = parent.getCoordinates();
		this.range = range;
		this.scale = parent.getScale();
		this.lines = parent.getLines();
		System.out.println("Correct line size:" + lines.size());
		plots = parent.getPlots();
		focusPlots = this.convertScaleToFocusPlots(parent.getScalePlots());
		focusLines = this.convertToFocusLines(lines);
		setUtil();
		paintBackground();
		
		//閉じるで閉められない
//		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}
	
	public void reloadPoints(int x, int y){
		this.x = x;
		this.y = y;
		drawBackground();
	}
	
	private void setUtil(){
		this.setVisible(true);
		this.setLayout(null);
		this.setSize(this.range*2,this.range*2);
		img = new BufferedImage(parent.getWidth()+range*2, parent.getHeight()+range*2, BufferedImage.TYPE_INT_ARGB);
		paint = new BufferedImage(parent.getWidth()+range*2, parent.getHeight()+range*2, BufferedImage.TYPE_INT_ARGB);
		earth = new JLabel(new ImageIcon(paint));
		earth.setBounds(0, 0, parent.getWidth()+range*2, parent.getHeight()+range*2);
		earth.addMouseMotionListener(this);
		earth.addMouseListener(this);
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
	
	private void paintBackground(){
		Graphics2D g = (Graphics2D)img.getGraphics();
		g.setColor(Constants.backgroundColor);
		g.clearRect(0, 0, coord.maxx+range*2, coord.maxy+range*2);
		
//	    List<Tuple2<Double,Double>> data = this.vertex;
//	    double maxx = 0.0;
//	    double maxy = 0.0;
//	    xpoints = new int[data.size()];
//	    ypoints = new int[data.size()];
//	    for(int i = 0; i < data.size(); i++){
//	    	Tuple2<Double, Double> d = data.get(i);
//	    	double tx = d.t1 + (range) - this.x/scale;
//	    	double ty = d.t2 + (range) - this.y/scale;
//	    	//System.out.println(tx + "," + ty);
//	    	maxx = (tx < maxx)?maxx:tx;
//	    	maxy = (ty < maxy)?maxy:ty;
//	    	xpoints[i] = (int)tx;
//	    	ypoints[i] = (int)ty;
//	    	//System.out.println(xpoints[i] + "," + ypoints[i]);
//	    }
	    
//	    Polygon polygon = new Polygon(xpoints, ypoints, xpoints.length);
//		g.setColor(Constants.polyColor);
//		BasicStroke wideStroke = new BasicStroke(4.0f);
//		g.setStroke(wideStroke);
//		g.drawPolygon(polygon);
		
		g.setColor(Constants.coordColor);
	    for(int i = 0; i < coord.size(); i++){
//	    	double nowx = (coord.getVisX(i) + Constants.imagePositionOffset/2) +range - this.x/scale;
//	    	double nowy = (coord.getVisY(i) + Constants.imagePositionOffset/2) +range - this.y/scale;
	    	double nowx = (coord.getVisX(i) + Constants.imagePositionOffset/2);
	    	double nowy = (coord.getVisY(i) + Constants.imagePositionOffset/2);
//	    	if(nowx < 0 || nowy < 0 || nowx >= range*2+1 || nowy >=range*2+1)continue;
	    	g.fillRect((int)nowx, (int)nowy, 1, 1);
	    }
		g.drawImage(img, null, 0, 0);
		drawBackground();
	}
	
	private void drawBackground(){
		Graphics2D g = (Graphics2D)paint.getGraphics();
		int w = (int)((double)this.x/scale)-range;
		int h = (int)((double)this.y/scale)-range;
		BufferedImage output = img.getSubimage(Math.max(0, w), Math.max(0, h), range*2, range*2);
		g.drawImage(output, 0, 0, earth);
		this.repaint();
	}
	
	public List<Tuple2<Double, Double>> getPlos(){
		return plots;
	}
	
	private List<Tuple2<Integer, Integer>> convertFocusToScalePlots(List<Tuple2<Integer, Integer>> tmp){
		List<Tuple2<Integer, Integer>> enc = new ArrayList<Tuple2<Integer, Integer>>();
		for(int i = 0; i < tmp.size(); i++){
			Tuple2<Integer, Integer> t = tmp.get(i);
			enc.add(new Tuple2<Integer, Integer>(t.t1+(int)((double)this.x/scale), t.t2+(int)((double)this.y/scale)));
		}
		return enc;
	}
	
	private List<Tuple2<Integer, Integer>> convertScaleToFocusPlots(List<Tuple2<Integer, Integer>> source){
		List<Tuple2<Integer, Integer>> ret = new ArrayList<Tuple2<Integer, Integer>>();
		for(int i = 0; i < source.size(); i++){
			Tuple2<Integer, Integer> t = source.get(i);
			ret.add(new Tuple2<Integer, Integer>((int)((double)t.t1/scale)-(int)((double)this.x/scale)+range, (int)((double)t.t2/scale)-(int)((double)this.y/scale)+range));
		}
		return ret;
	}
	
	private List<Line2D> convertFocusToLines(List<Line2D> source){
		List<Line2D> ret = new ArrayList<Line2D>();
		for(int i = 0; i < source.size(); i++){
			Line2D line = source.get(i);
			double x1 = line.getX1() + (double)this.x/scale - range;
			double y1 = line.getY1() + (double)this.y/scale - range;
			double x2 = line.getX2() + (double)this.x/scale - range;
			double y2 = line.getY2() + (double)this.y/scale - range;
			ret.add(new Line2D.Double(x1, y1, x2, y2));
		}
		return ret;
	}
	
	private List<Line2D> convertToFocusLines(List<Line2D> source){
		List<Line2D> ret = new ArrayList<Line2D>();
		for(int i = 0; i < source.size(); i++){
			Line2D line = source.get(i);
			double x1 = line.getX1() - this.x/scale + range;
			double y1 = line.getY1() - this.y/scale + range;
			double x2 = line.getX2() - this.x/scale + range;
			double y2 = line.getY2() - this.y/scale + range;
			ret.add(new Line2D.Double(x1, y1, x2, y2));
		}
		return ret;
	}

	public boolean onLine(double x,double y, Line2D line){///(x,y)がこのエッジ上に存在するか判定
		double kx1 = line.getX1();
		double ky1 = line.getY1();
		double kx2 = line.getX2();
		double ky2 = line.getY2();
		double dist = Edge.distance(kx1, ky1, kx2, ky2);
		double sum = Edge.distance(x,y,kx1,ky1) + Edge.distance(x,y,kx2,ky2);
		double sabun = Math.abs(dist - sum);
		if(sabun < 4.0)return true;///線上にあるのでOK
		return false;
	}
		
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		boolean isOn = false;
		int rmIndex = 0;
		int nowx = e.getX();
		int nowy = e.getY();
		Tuple2<Double, Double> nowOn = null;
		for(int i = 0; i < focusPlots.size(); i++){
			Tuple2<Integer, Integer> t = focusPlots.get(i);
			int vx = t.t1;
			int vy = t.t2;
			double dist = Edge.distance(nowx, nowy, vx, vy);
			if(dist <= 5.0){
				isOn = true;
				rmIndex = i;
				nowOn = new Tuple2<Double, Double>((double)nowx, (double)nowy);
				break;
			}
		}
		//Right Click
		if(e.getButton() == MouseEvent.BUTTON3){
			if(!isOn)return;
			focusPlots.remove(rmIndex);
			plots.remove(rmIndex);
			drawBackground();
			Graphics2D g = (Graphics2D)paint.getGraphics();
			for(int i = 0; i < focusPlots.size(); i++){
				Tuple2<Integer, Integer> t = focusPlots.get(i);
				int vx = t.t1;
				int vy = t.t2;
				double dist = Edge.distance(nowx, nowy, vx, vy);
				g.setColor(Constants.plotColor);
				if(dist <= 5.0)g.setColor(Constants.onPlotColor);
				g.fillOval(vx - Constants.plotOvalRadius, vy - Constants.plotOvalRadius, Constants.plotOvalRadius*2, Constants.plotOvalRadius*2);
			}
		//Left Click
		}else{
			if(isOn){

			}else{
				focusPlots.add(new Tuple2<Integer, Integer>((int)e.getX(), (int)e.getY()));
				double nx = (double)e.getX() - range + (double)this.x/scale;
				double ny = (double)e.getY() - range + (double)this.y/scale;
				plots.add(new Tuple2<Double, Double>((double)nx, (double)ny));
				drawBackground();
				Graphics2D g = (Graphics2D)paint.getGraphics();
				g.setColor(Constants.plotColor);
				for(int i = 0; i < focusPlots.size(); i++){
					g.fillOval(focusPlots.get(i).t1 - 4, focusPlots.get(i).t2 - 4, 8, 8);
				}	
			}
		}
		this.repaint();
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S){
			this.dispose();
			parent.setPlots(plots);
			VisualizeFrame.setVisibleFrame(true);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		//背景を描画
		int x = e.getX();
		int y = e.getY();
		drawBackground();
		Graphics2D g = (Graphics2D)paint.getGraphics();
		
		//線を描画
		BasicStroke maxiStroke = new BasicStroke(4.0f);
		BasicStroke miniStroke = new BasicStroke(1.0f);
		g.setStroke(maxiStroke);
		g.setColor(Constants.newLineColor);
		for(int i = 0; i < focusLines.size(); i++){
			g.draw(focusLines.get(i));
		}
		g.setStroke(miniStroke);
		
		//点を描画
		int nowx = e.getX();
		int nowy = e.getY();
		for(int i = 0; i < focusPlots.size(); i++){
			Tuple2<Integer, Integer> t = focusPlots.get(i);
			int vx = t.t1;
			int vy = t.t2;
			double dist = Edge.distance(nowx, nowy, vx, vy);
			g.setColor(Constants.plotColor);
			if(dist <= 5.0)g.setColor(Constants.onPlotColor);
			g.fillOval(vx - Constants.plotOvalRadius, vy - Constants.plotOvalRadius, Constants.plotOvalRadius*2, Constants.plotOvalRadius*2);
		}
		
		//カーソル円を描画
		g.setColor(Color.BLUE);
		g.drawOval(x-Constants.targetOvalRadius, y-Constants.targetOvalRadius, Constants.targetOvalRadius*2, Constants.targetOvalRadius*2);
		g.drawOval(x-Constants.targetOvalRadius*2+1, y-Constants.targetOvalRadius*2+1, (Constants.targetOvalRadius*2-1)*2, (Constants.targetOvalRadius*2-1)*2);
		//カーソル線を描画
	    g.drawLine(0, y, x-Constants.targetOvalRadius, y);
	    g.drawLine(x+Constants.targetOvalRadius, y, range*2, y);
	    g.drawLine(x, 0, x, y-Constants.targetOvalRadius);
	    g.drawLine(x, y+Constants.targetOvalRadius, x, range*2);
		
	    this.repaint();
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		boolean isOn = false;
		int rmIndex = 0;
		int nowx = e.getX();
		int nowy = e.getY();
		for(int i = 0; i < focusPlots.size(); i++){
			Tuple2<Integer, Integer> t = focusPlots.get(i);
			int vx = t.t1;
			int vy = t.t2;
			double dist = Edge.distance(nowx, nowy, vx, vy);
			if(dist <= 5.0){
				isOn = true;
				rmIndex = i;
				break;
			}
		}
		isDrag = isOn;
		if(isDrag){
			dragLineIndexes = new ArrayList<Integer>();
			dragPlotIndex = rmIndex;
			for(int i = 0; i < focusLines.size(); i++){
				Line2D line = focusLines.get(i);
				if(onLine(nowx, nowy, line)){
					dragLineIndexes.add(i);
				}
			}			
		}

	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(!isDrag)return;
		boolean isOk = true;
		int nowx = e.getX();
		int nowy = e.getY();
		for(int i = 0; i < focusPlots.size(); i++){
			Tuple2<Integer, Integer> t = focusPlots.get(i);
			int vx = t.t1;
			int vy = t.t2;
			double dist = Edge.distance(nowx, nowy, vx, vy);
			if(dist <= 5.0){
				isOk = false;
				break;
			}
		}
		
		for(int i = 0; i < focusLines.size(); i++){
			if(onLine(nowx, nowy, focusLines.get(i))){
				isOk = false;
				break;
			}
		}
		
		//点上なら更新しない
		if(!isOk)return;
		for(int i = 0; i < dragLineIndexes.size(); i++){
			int index = dragLineIndexes.get(i);
			System.out.println("index:"+index);
			int x1 = focusPlots.get(dragPlotIndex).t1;
			int y1 = focusPlots.get(dragPlotIndex).t2;
			Line2D line = focusLines.get(index);
			if(Math.abs(line.getX1() - x1) < 2.0 && Math.abs(line.getY1() - y1) < 2.0){
				focusLines.set(index, new Line2D.Double(nowx, nowy, line.getX2(), line.getY2()));
			}else{
				focusLines.set(index, new Line2D.Double(line.getX1(), line.getY1(), nowx, nowy));
			}
		}
		focusPlots.set(dragPlotIndex, new Tuple2<Integer, Integer>(nowx, nowy));
		mouseMoved(e);
	}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void mouseDragged(MouseEvent e) {
		if(!isDrag){
			mouseMoved(e);
		}
		drawBackground();
		Graphics2D g = (Graphics2D)paint.getGraphics();
		int nowx = e.getX();
		int nowy = e.getY();

		for(int i = 0; i < focusPlots.size(); i++){
			Tuple2<Integer, Integer> t = focusPlots.get(i);
			int vx = t.t1;
			int vy = t.t2;
			if(i == dragPlotIndex)continue;
			g.setColor(Constants.plotColor);
			g.fillOval(vx - Constants.plotOvalRadius, vy - Constants.plotOvalRadius, Constants.plotOvalRadius*2, Constants.plotOvalRadius*2);
		}
		g.setColor(Color.orange);
		g.fillOval(nowx - Constants.plotOvalRadius, nowy - Constants.plotOvalRadius, Constants.plotOvalRadius*2, Constants.plotOvalRadius*2);
		
		//カーソル円を描画
		g.setColor(Color.BLUE);
		g.drawOval(nowx-Constants.targetOvalRadius, nowy-Constants.targetOvalRadius, Constants.targetOvalRadius*2, Constants.targetOvalRadius*2);
		g.drawOval(nowx-Constants.targetOvalRadius*2+1, nowy-Constants.targetOvalRadius*2+1, (Constants.targetOvalRadius*2-1)*2, (Constants.targetOvalRadius*2-1)*2);
		//カーソル線を描画
	    g.drawLine(0, nowy, nowx-Constants.targetOvalRadius, nowy);
	    g.drawLine(nowx+Constants.targetOvalRadius, nowy, range*2, nowy);
	    g.drawLine(nowx, 0, nowx, nowy-Constants.targetOvalRadius);
	    g.drawLine(nowx, nowy+Constants.targetOvalRadius, nowx, range*2);
	    
	    this.repaint();
	    
	}
}