package Mahito6.UI;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;

import Mahito6.Main.Constants;
import Mahito6.Main.Tuple2;
import Mahito6.Solver.DiffPiece;
import Mahito6.Solver.Edge;
import Main.UI.Util.Coordinates;

public class VisualizePanel extends JPanel implements MouseListener, MouseMotionListener{
	
	private BufferedImage gPiece;
	private BufferedImage paint;
    private Polygon polygon;
    private JLabel earth;
    private List<Tuple2<Double, Double>> vertex;
    private List<String> textData;
    private double scale = 1.0;
    private DiffPiece diff;
    private CorrectDialog dia = null;
    private Coordinates coord;
    private List<Tuple2<Integer, Integer>> plots;
    private List<Tuple2<Integer, Integer>> viewPlots;
    private double maxx, maxy;
    private boolean isSelect = false;
    private int[] xpoints;
    private int[] ypoints;
    private List<Line2D> lines;
    private Line2D viewLine;
    private Tuple2<Integer, Integer> clickP = null;
    private int range = 150;
<<<<<<< HEAD
    private HashMap<Tuple2<Tuple2<Integer,Integer>, Tuple2<Integer,Integer>>, Boolean> lineMap;
    private BasicStroke miniStroke;
    private BasicStroke maxiStroke;
    private Tuple2<Tuple2<Integer, Integer>, Tuple2<Integer, Integer>> _hash;
=======
    private BasicStroke miniStroke;
    private BasicStroke maxiStroke;
>>>>>>> 5844b332e3beecbe6b9e5c7d63ef7ade94f8032a
    
    
    public VisualizePanel(List<Tuple2<Double, Double>> vertex, Coordinates coord){
    	this.vertex = vertex;
    	this.coord = coord;
		setUtil();
		paintPiece();
		this.setTransferHandler(new DropFileHandler());
    }
	
	private void setUtil(){
		this.setLayout(null);
		this.setBackground(Color.gray.brighter().brighter());
		this.setPreferredSize(new Dimension(300, 300));
		this.setOpaque(true);

	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		gPiece = new BufferedImage(screenSize.width, screenSize.height, BufferedImage.TYPE_INT_ARGB);
		paint = new BufferedImage(screenSize.width, screenSize.height, BufferedImage.TYPE_INT_ARGB);
		earth = new JLabel(new ImageIcon(paint));
		earth.setBounds(0, 0, screenSize.width, screenSize.height);
		earth.addMouseListener(this);
		earth.addMouseMotionListener(this);
		this.add(earth);
		
		plots = new ArrayList<Tuple2<Integer, Integer>>();
		viewPlots = new ArrayList<Tuple2<Integer, Integer>>();
		lines = new ArrayList<Line2D>();
		viewLine = new Line2D.Double();
    	isSelect = false;
    	miniStroke = new BasicStroke(1.0f);
        maxiStroke = new BasicStroke(3.0f);
<<<<<<< HEAD
        
        lineMap = new HashMap<Tuple2<Tuple2<Integer,Integer>, Tuple2<Integer,Integer>>, Boolean>();
=======
>>>>>>> 5844b332e3beecbe6b9e5c7d63ef7ade94f8032a
	}
	
	public void paintPiece(){
		if(vertex.isEmpty()){
			System.out.println("Vertex is not set.");
			return;
		}
		Graphics2D g = (Graphics2D)gPiece.getGraphics();
//
//	    BasicStroke normalStroke = new BasicStroke(5.0f);
//	    g.setStroke(normalStroke);
//		g.setPaint(Color.BLACK);
		
	    List<Tuple2<Double,Double>> data = vertex;
	    xpoints = new int[data.size()];
	    ypoints = new int[data.size()];
	    for(int i = 0; i < data.size(); i++){
	    	Tuple2<Double, Double> d = data.get(i);
	    	double x = d.t1;
	    	double y = d.t2;
	    	xpoints[i] = (int)x;
	    	ypoints[i] = (int)y;
	    	System.out.println(xpoints[i] + "," + ypoints[i]);
	    }
	    
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    double w = (double)screenSize.width;
	    double h = (double)screenSize.height;
	    //System.out.println("w:" + w + " h:" + h);
	    w -= 200;
	    h -= 200;
	    maxx = coord.maxx - coord.minx + Constants.imagePositionOffset/2;
	    maxy = coord.maxy - coord.miny + Constants.imagePositionOffset/2;
	    if(maxy < maxx && maxx > w){
	    	scale = w/maxx;
	    	maxx = 0.0;
	    	maxy = 0.0;
	    	for(int i = 0 ; i < data.size(); i++){
		    	Tuple2<Double, Double> d = data.get(i);
		    	double x = d.t1*scale;
		    	double y = d.t2*scale;
		    	maxx = (x < maxx)?maxx:x;
		    	maxy = (y < maxy)?maxy:y;
		    	xpoints[i] = (int)x;
		    	ypoints[i] = (int)y;	    		
	    	}
	    }else if(maxx < maxy && maxy > h){
	    	scale = h/maxy;
	    	maxx = 0.0;
	    	maxy = 0.0;
	    	for(int i = 0 ; i < data.size(); i++){
		    	Tuple2<Double, Double> d = data.get(i);
		    	double x = d.t1*scale;
		    	double y = d.t2*scale;
		    	maxx = (x < maxx)?maxx:x;
		    	maxy = (y < maxy)?maxy:y;
		    	xpoints[i] = (int)x;
		    	ypoints[i] = (int)y;	    		
	    	}
	    }else{
	    	scale = 1.0;
	    }

		this.setPreferredSize(new Dimension((int)(maxx+100), (int)(maxy+100)));
		g.setColor(Constants.backgroundColor);
		g.clearRect(0, 0, (int)(maxx+100), (int)(maxy+100));
	    g.setStroke(maxiStroke);
		polygon = new Polygon(xpoints, ypoints, xpoints.length);
		g.setColor(Constants.polyColor);
		g.drawPolygon(polygon);
	    g.setStroke(miniStroke);

        AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f);
        // アルファ値をセット（以後の描画は半透明になる）
	    g.setColor(Constants.coordColor);
        g.setComposite(composite);
	    for(int i = 0; i < coord.size(); i++){
	    	double x = (coord.getVisX(i)+Constants.imagePositionOffset/2)*scale;
	    	double y = (coord.getVisY(i)+Constants.imagePositionOffset/2)*scale;
	    	g.fillRect((int)x, (int)y, 1, 1);
	    }
	    g.drawImage(gPiece, null, 0, 0);
	    drawBackground();
	}
	
	private void drawBackground(){
		Graphics2D g = (Graphics2D)paint.getGraphics();
		g.drawImage(gPiece, 0, 0, earth);
		this.repaint();
	}
	
	public static List<String> readTxt(File file) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(file));
		List<String> in = new ArrayList<String>();
		String str = br.readLine();
		while(str != null){in.add(str);str = br.readLine();}
		br.close();
		return in;
	}
	
	public void paintPlots(){
		Graphics2D g = (Graphics2D)paint.getGraphics();
		g.setColor(Constants.plotColor);
		for(int i = 0; i < plots.size(); i++){
			Tuple2<Integer, Integer> now = plots.get(i);
			int nx = now.t1;
			int ny = now.t2;
			g.fillOval(nx-Constants.plotOvalRadius, ny-Constants.plotOvalRadius, Constants.plotOvalRadius*2, Constants.plotOvalRadius*2);
		}
		this.repaint();
	}
	
	public void setPlots(List<Tuple2<Integer, Integer>> plots){
		this.plots = plots;
		paintPlots();
	}
	
	public void saveViewPlots(List<Tuple2<Integer, Integer>> viewPlots){
		this.viewPlots = viewPlots;
	}
	
	private void paintLines(int nowx, int nowy){
		Graphics2D g = (Graphics2D)paint.getGraphics();
		g.setStroke(maxiStroke);
		g.setColor(Constants.newLineColor);
		for(int i = 0; i < lines.size(); i++){
			g.draw(lines.get(i));
		}
		//点選択中
		if(isSelect){
			int cx = clickP.t1;
			int cy = clickP.t2;
			viewLine.setLine(nowx, nowy, cx, cy);
			g.draw(viewLine);
		}
	    g.setStroke(miniStroke);
	}
	
<<<<<<< HEAD
	private boolean isVerifyLine(int nx, int ny, int cx, int cy){
		if(nx==cx && ny==cy)return false;
		Tuple2<Integer, Integer> nt = new Tuple2<Integer, Integer>(nx,ny);
		Tuple2<Integer, Integer> ct = new Tuple2<Integer, Integer>(cx,cy);
		_hash = new Tuple2<Tuple2<Integer, Integer>, Tuple2<Integer, Integer>>(nt, ct);
		if(lineMap.containsKey(_hash))return false;
		lineMap.put(_hash, true);
		_hash = new Tuple2<Tuple2<Integer, Integer>, Tuple2<Integer, Integer>>(ct, nt);
		if(lineMap.containsKey(_hash))return false;
		lineMap.put(_hash, true);
		return true;
	}
	
=======
>>>>>>> 5844b332e3beecbe6b9e5c7d63ef7ade94f8032a
	public List<Tuple2<Double, Double>> getVertex(){
		return vertex;
	}
	
	public Coordinates getCoordinates(){
		return coord;
	}
	
	public double getScale(){
		return scale;
	}
	
	public List<Tuple2<Integer, Integer>> getPlots(){
		return plots;
	}
	
	public List<Tuple2<Integer, Integer>> getViewPlots(){
		return viewPlots;
	}
	
<<<<<<< HEAD
	public List<Line2D> getLines(){
		List<Line2D> ret = new ArrayList<Line2D>();
		for(int i = 0; i < lines.size(); i++){
			Line2D line = lines.get(i);
			double x1 = line.getX1() / scale;
			double y1 = line.getY1() / scale;
			double x2 = line.getX2() / scale;
			double y2 = line.getY2() / scale;
			ret.add(new Line2D.Double(x1,y1,x2,y2));
		}
		return ret;
	}
	
=======
>>>>>>> 5844b332e3beecbe6b9e5c7d63ef7ade94f8032a
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		Tuple2<Integer, Integer> nowOn = null;
		int nowx = e.getX();
		int nowy = e.getY();
		boolean isOn = false;
		for(int i = 0; i < plots.size(); i++){
			Tuple2<Integer, Integer> t = plots.get(i);
			int vx = t.t1;
			int vy = t.t2;
			double dist = Edge.distance(nowx, nowy, vx, vy);
			if(dist <= 5.0){
				isOn = true;
				nowOn = t;
				break;
			}
		}
		//点選択時
		if(isSelect){
			if(!isOn)return;
			int nx = nowOn.t1;
			int ny = nowOn.t2;
			int cx = clickP.t1;
			int cy = clickP.t2;
<<<<<<< HEAD
			
			//有効なラインか確認(重複確認含)
			if(!isVerifyLine(nx,ny,cx,cy))return;
			
=======
			//同一点なら無視
			if(nx==cx && ny==cy)return;
>>>>>>> 5844b332e3beecbe6b9e5c7d63ef7ade94f8032a
			Graphics2D g = (Graphics2D)paint.getGraphics();
			g.setColor(Constants.newLineColor);
			Line2D line = new Line2D.Double(nx, ny, cx, cy);
			//点の上の時
			clickP = null;
			isSelect = false;
			lines.add(line);
			paintLines(nowx, nowy);
		}else{
			//点の上の時
			if(isOn){
				isSelect = true;
				clickP = new Tuple2<Integer, Integer>(nowx, nowy);
			}else{
				System.out.println(e.getX()+","+e.getY());
				dia = new CorrectDialog(nowx, nowy, range, this);
				VisualizeFrame.setVisibleFrame(false);	
			}	
		}
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

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		drawBackground();
		Graphics2D g = (Graphics2D)paint.getGraphics();
		g.setColor(Constants.rangeRectColor);
		int nowx = e.getX();
		int nowy = e.getY();
		boolean isOn = false;
		for(int i = 0; i < plots.size(); i++){
			Tuple2<Integer, Integer> t = plots.get(i);
			int vx = t.t1;
			int vy = t.t2;
			double dist = Edge.distance(nowx, nowy, vx, vy);
			g.setColor(Constants.plotColor);
			if(dist <= 5.0){
				g.setColor(Constants.onPlotColor);
				isOn = true;
			}
			g.fillOval(vx - Constants.plotOvalRadius, vy - Constants.plotOvalRadius, Constants.plotOvalRadius*2, Constants.plotOvalRadius*2);
		}
		
		paintLines(nowx, nowy);
		
		//点に乗っている場合には範囲方形は描画しない。
		if(isOn){
			this.repaint();
			return;
		}
		
		int scaleRange = (int)((double)range*scale);
		g.setColor(Constants.rangeRectColor);
		g.drawRect(nowx-scaleRange, nowy-scaleRange, scaleRange*2, scaleRange*2);

		this.repaint();
	}
	
	private class DropFileHandler extends TransferHandler {
		@Override
		public boolean canImport(TransferSupport support) {
			if (!support.isDrop()) {
		        return false;
		    }
 
			if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
		        return false;
		    }
 
			return true;
		}
 
		@Override
		public boolean importData(TransferSupport support) {
			if (!canImport(support)) {
		        return false;
		    }
 
			Transferable t = support.getTransferable();
			try {
				List<File> files = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
				File f = files.get(0);
				try {
					textData = readTxt(f);
					diff = new DiffPiece(textData);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (UnsupportedFlavorException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
	}

	
}