package Mahito6.UI;

import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Polygon;
import java.awt.Robot;
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
import javax.swing.border.LineBorder;

import Mahito6.Main.Constants;
import Mahito6.Main.Tuple2;
import Mahito6.Solver.DiffPiece;
import Mahito6.Solver.Edge;
import Main.UI.Util.Coordinates;
import Main.UI.Util.MeasureTimer;

public class VisualizePanel extends JPanel implements MouseListener, MouseMotionListener{
	
	private BufferedImage gPiece;
	private BufferedImage paint;
    private JLabel earth;
    private List<Tuple2<Double, Double>> vertex;
    private List<String> textData;
    private double scale = 1.0;
    private RealTimeDialog realtimeDialog = null;
    private Coordinates coord;
    //スケールを合わせたプロット
    private List<Tuple2<Double, Double>> scalePlots;
    //合わせてないプロット
    private List<Tuple2<Double, Double>> plots;
    private DiffPiece diff;
    private double maxx, maxy;
    private boolean isSelect = false;
    private boolean isLine = false;
    private Line2D scaleLine;
    private List<Line2D> scaleLines;
    private List<Line2D> lines;
    private Tuple2<Double, Double> clickP = null;
    private int range = 150;
    private BasicStroke miniStroke;
    private BasicStroke maxiStroke;
    private HandlSummaryPanel parent;
    private int mouseCounter = 0;
    
    public VisualizePanel(List<Tuple2<Double, Double>> vertex, Coordinates coord, List<Line2D> lines, HandlSummaryPanel parent){
    	this.parent = parent;
    	launchPanel(vertex, coord, lines, true);
    }
    
    public void relaunchPanel(List<Tuple2<Double, Double>> vertex, Coordinates coord, List<Line2D> lines, HandlSummaryPanel parent){
    	this.parent = parent;
    	this.vertex = vertex;
    	this.coord = coord;
    	this.lines = lines;
		//スケールの計算。これしないとバグる
		calcScale();
		//二値化されたピースをペイント
		paintPiece();
		
		//頂点データをスケールに合わせてプロットに変換。このときint型に丸め込まれる
		scalePlots = this.convertVertexToScalePlots(vertex);
		//線をスケールを合わせて描画。これはdoubleのまま保持される。
		scaleLines = this.convertScaleLines(this.lines);
		//スケールを合わせていないプロット
		plots = vertex;
		System.out.println("relaunch : " + vertex.size());
		scaleLine = new Line2D.Double();
    	isSelect = false;
	    drawLines(false);
	    drawPlots(false);
	    
	    //一端パネルを削除
	    parent.removeRealTimeDialog();
	    realtimeDialog = new RealTimeDialog(0, 0, range, this, parent);
	    parent.setRealTimeDialog(realtimeDialog);
    }
    
    public void launchPanel(List<Tuple2<Double, Double>> vertex, Coordinates coord, List<Line2D> lines, boolean isInit){
    	this.vertex = vertex;
    	this.coord = coord;
    	this.lines = lines;
//    	for(int i = 0 ; i < lines.size(); i++){
//    		this.lines.add(this.expandLine(lines.get(i)));
//    	}
		if(isInit)setUtil();
		//スケールの計算。これしないとバグる
		if(isInit)calcScale();
		//二値化されたピースをペイント
		paintPiece();
		
		//頂点データをスケールに合わせてプロットに変換。このときint型に丸め込まれる
		scalePlots = this.convertVertexToScalePlots(vertex);
		//線をスケールを合わせて描画。これはdoubleのまま保持される。
		scaleLines = this.convertScaleLines(this.lines);
		//スケールを合わせていないプロット
		plots = vertex;
		System.out.println("launch: "+plots.size());
		scaleLine = new Line2D.Double();
    	isSelect = false;
	    drawLines(false);
	    drawPlots(false);
	    
	    if(isInit){
	    	System.out.println("launch realtime");
	    	realtimeDialog = new RealTimeDialog(0, 0, range, this, parent);
	    	parent.setRealTimeDialog(realtimeDialog);
	    }else{
	    	realtimeDialog.setPlots(plots);
	    }
	    parent.repaint();
    }
	
	private void setUtil(){
		this.setLayout(null);
		this.setBackground(Color.gray.brighter().brighter());
		this.setOpaque(true);
		this.setTransferHandler(new DropFileHandler());

		//スクリーンサイズ以上のピースは存在しないようにスケールを合わせる
		gPiece = new BufferedImage(HandlSummaryPanel.visualizeWidth, HandlSummaryPanel.visualizeHeight, BufferedImage.TYPE_INT_ARGB);
		paint = new BufferedImage(HandlSummaryPanel.visualizeWidth, HandlSummaryPanel.visualizeHeight, BufferedImage.TYPE_INT_ARGB);
		earth = new JLabel(new ImageIcon(paint));
		earth.setBounds(0, 0, HandlSummaryPanel.visualizeWidth, HandlSummaryPanel.visualizeHeight);
		earth.addMouseListener(this);
		earth.addMouseMotionListener(this);
		this.add(earth);
		
		//線の太さ初期化
    	miniStroke = new BasicStroke(1.0f);
        maxiStroke = new BasicStroke(2.0f);
        
        //既存の線か判定するテーブル(要書き換え)
//        lineMap = new HashMap<Tuple2<Tuple2<Integer,Integer>, Tuple2<Integer,Integer>>, Boolean>();
	}
	
	public void paintPiece(){
		if(vertex.isEmpty())System.out.println("Vertex is not set.");
		Graphics2D g = (Graphics2D) gPiece.getGraphics();
//
//	    BasicStroke normalStroke = new BasicStroke(5.0f);
//	    g.setStroke(normalStroke);
//		g.setPaint(Color.BLACK);
		
		g.setColor(Constants.backgroundColor);
		g.clearRect(0, 0, (int)(HandlSummaryPanel.visualizeWidth), (int)(HandlSummaryPanel.visualizeHeight));
		
//	    g.setStroke(maxiStroke);
//		polygon = new Polygon(xpoints, ypoints, xpoints.length);
//		g.setColor(Constants.polyColor.brighter());
//		g.drawPolygon(polygon);
//	    g.setStroke(miniStroke);

//        AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f);
//        // アルファ値をセット（以後の描画は半透明になる）
//        g.setComposite(composite);
	    g.setColor(Constants.coordColor);
	    boolean[][] memo = new boolean[HandlSummaryPanel.visualizeWidth][HandlSummaryPanel.visualizeHeight];
	    for(int i = 0; i < coord.size(); i++){
	    	int x = (int)((coord.getVisX(i)+Constants.imagePositionOffset/2)*scale);
	    	int y = (int)((coord.getVisY(i)+Constants.imagePositionOffset/2)*scale);
	    	if(x < 0 || y < 0 || x >= HandlSummaryPanel.visualizeWidth || y >= HandlSummaryPanel.visualizeHeight)continue;
	    	if(memo[x][y])continue;
	    	memo[x][y] = true;
	    	g.fillRect(x, y, 1, 1);
	    }
	    
	    //画像として自身に保存し高速化	    
	    g.drawImage(gPiece, null, 0, 0);
	    g.dispose();
	    //背景として保存したgPieceを描画する。
	    drawBackground(false);
	}
	
	private void drawBackground(boolean isRepaint){
		Graphics2D g = paint.createGraphics();
		g.drawImage(gPiece, 0, 0, earth);
		g.dispose();
		if(isRepaint)parent.repaint();
	}
	
	public static List<String> readTxt(File file) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(file));
		List<String> in = new ArrayList<String>();
		String str = br.readLine();
		while(str != null){in.add(str);str = br.readLine();}
		br.close();
		return in;
	}
	
	public void drawPlots(boolean isRepaint){
		Graphics2D g = (Graphics2D)paint.getGraphics();
		g.setColor(Constants.plotColor);
		for(int i = 0; i < scalePlots.size(); i++){
			Tuple2<Double, Double> now = scalePlots.get(i);
			double nx = now.t1;
			double ny = now.t2;
			g.fillOval((int)nx-Constants.plotOvalRadius, 
					(int)ny-Constants.plotOvalRadius, 
					Constants.plotOvalRadius*2, 
					Constants.plotOvalRadius*2);
		}
		g.dispose();
		if(isRepaint)parent.repaint();
	}
	
	public void setData(List<Tuple2<Double, Double>> plots, List<Line2D> lines){
		this.plots = plots;
		this.scalePlots = this.encodeToScalePlot(plots);
		this.lines = lines;
		this.scaleLines = this.encodeToScaleLines(lines);
		drawBackground(false);
		drawLines(false);
		drawPlots(true);
	}
	private List<Line2D> encodeToScaleLines(List<Line2D> source){
		List<Line2D> ret = new ArrayList<Line2D>();
		for(Line2D line : source){
			double nowx1 = line.getX1() * scale;
			double nowy1 = line.getY1() * scale;
			double nowx2 = line.getX2() * scale;
			double nowy2 = line.getY2() * scale;
			ret.add(new Line2D.Double(nowx1, nowy1, nowx2, nowy2));
		}
		return ret;
	}
		
	private List<Tuple2<Double, Double>> encodeToScalePlot(List<Tuple2<Double, Double>> source){
		List<Tuple2<Double, Double>> ret = new ArrayList<Tuple2<Double, Double>>();
		for(Tuple2<Double, Double> d : source){
			double nowx = d.t1 * scale;
			double nowy = d.t2 * scale;
			ret.add(new Tuple2<Double, Double>(nowx, nowy));
		}
		return ret;
	}
	
	private void drawLines(boolean isRepaint){
		Graphics2D g = (Graphics2D)paint.getGraphics();
		g.setStroke(maxiStroke);
		g.setColor(Constants.newLineColor);
		for(int i = 0; i < scaleLines.size(); i++){
			g.draw(scaleLines.get(i));
		}
		g.dispose();
		if(isRepaint)parent.repaint();
	}
	
	private void paintLine(int nowx, int nowy){
		Graphics2D g = (Graphics2D)paint.getGraphics();
		g.setStroke(maxiStroke);
		g.setColor(Constants.newLineColor);
		//点選択中
		if(isSelect){
			double cx = clickP.t1;
			double cy = clickP.t2;
			scaleLine.setLine(nowx, nowy, cx, cy);
			g.draw(scaleLine);
		}
		g.dispose();
	}
	
	private void drawDialog(int x, int y){
		realtimeDialog.reloadPoints(x, y);
	}
	
//	private boolean isVerifyLine(int nx, int ny, int cx, int cy){
//		if(nx==cx && ny==cy)return false;
//		Tuple2<Integer, Integer> nt = new Tuple2<Integer, Integer>(nx,ny);
//		Tuple2<Integer, Integer> ct = new Tuple2<Integer, Integer>(cx,cy);
//		_hash = new Tuple2<Tuple2<Integer, Integer>, Tuple2<Integer, Integer>>(nt, ct);
//		if(lineMap.containsKey(_hash))return false;
//		lineMap.put(_hash, true);
//		_hash = new Tuple2<Tuple2<Integer, Integer>, Tuple2<Integer, Integer>>(ct, nt);
//		if(lineMap.containsKey(_hash))return false;
//		lineMap.put(_hash, true);
//		return true;
//	}
	
	private List<Tuple2<Double, Double>> convertVertexToScalePlots(List<Tuple2<Double, Double>> vertex){
		List<Tuple2<Double, Double>> ret = new ArrayList<Tuple2<Double, Double>>();
		for(Tuple2<Double, Double> v : vertex){
			double nowx = v.t1 * scale;
			double nowy = v.t2 * scale;
			ret.add(new Tuple2<Double, Double>(nowx,nowy));
		}
		return ret;
	}
	
	private List<Line2D> convertScaleLines(List<Line2D> source){
		List<Line2D> ret = new ArrayList<Line2D>();
		for(Line2D line : source){
			double nx1 = line.getX1() * scale;
			double ny1 = line.getY1() * scale;
			double nx2 = line.getX2() * scale;
			double ny2 = line.getY2() * scale;
			ret.add(new Line2D.Double(nx1,ny1,nx2,ny2));
		}
		return ret;
	}
	
	private Line2D convertScaleLine(Line2D line){
		double nx1 = line.getX1() * scale;
		double ny1 = line.getY1() * scale;
		double nx2 = line.getX2() * scale;
		double ny2 = line.getY2() * scale;
		return new Line2D.Double(nx1,ny1,nx2,ny2);
	}
	
	private void calcScale(){
	    List<Tuple2<Double,Double>> data = vertex;
	    
//	    for(int i = 0; i < data.size(); i++){
//	    	Tuple2<Double, Double> d = data.get(i);
//	    	double x = d.t1;
//	    	double y = d.t2;
//	    }
//	    
	    double w = HandlSummaryPanel.visualizeWidth - Constants.imagePositionOffset/4;
	    double h = HandlSummaryPanel.visualizeHeight - Constants.imagePositionOffset/4;
	    //System.out.println("w:" + w + " h:" + h);
	    coord.calc();
	    maxx = coord.maxx - coord.minx + Constants.imagePositionOffset/2;
	    maxy = coord.maxy - coord.miny + Constants.imagePositionOffset/2;
	    System.out.println(maxx + "," + maxy);
	    scale = Math.min(w/maxx, Math.min(h/maxy, 1.0));
//	    if(maxy < maxx && maxx > w){
//	    	scale = w/maxx;
//////	    	if(coord.isError())return;
////	    	maxx = 0.0;
////	    	maxy = 0.0;
////	    	for(int i = 0 ; i < data.size(); i++){
////		    	Tuple2<Double, Double> d = data.get(i);
////		    	double x = d.t1;
////		    	double y = d.t2;
////		    	maxx = (x < maxx)?maxx:x;
////		    	maxy = (y < maxy)?maxy:y;   		
////	    	}
////	    	scale = Math.min(w/maxx, scale);
//	    }else if(maxx < maxy && maxy > h){
//	    	scale = h/maxy;
//////	    	if(coord.isError())return;
////	    	maxx = 0.0;
////	    	maxy = 0.0;
////	    	for(int i = 0 ; i < data.size(); i++){
////		    	Tuple2<Double, Double> d = data.get(i);
////		    	double x = d.t1;
////		    	double y = d.t2;
////		    	maxx = (x < maxx)?maxx:x;
////		    	maxy = (y < maxy)?maxy:y;	
////	    	}
////	    	scale = Math.min(h/maxy, scale);
//	    }else{
//	    	scale = 1.0;
//	    }
//	    
//		this.setPreferredSize(new Dimension((int)(maxx*scale+50), (int)(maxy*scale+50)));
	}
	
	public List<Tuple2<Double, Double>> getVertex(){
		return vertex;
	}
	
	public Coordinates getCoordinates(){
		return coord;
	}
	
	public double getScale(){
		return scale;
	}
	
	public List<Tuple2<Double, Double>> getPlots(){
		return plots;
	}
	
	public List<Tuple2<Double, Double>> getScalePlots(){
		return scalePlots;
	}
	
	//正規のスケールに直す
	public List<Line2D> getLines(){
		return lines;
	}
	
	private Line2D converToLine(Line2D line){
		//自分で引いた線をスケールを直して追加
		double x1 = line.getX1() / scale;
		double y1 = line.getY1() / scale;
		double x2 = line.getX2() / scale;
		double y2 = line.getY2() / scale;
		return new Line2D.Double(x1,y1,x2,y2);
	}
	
	public Line2D expandLine(Line2D line, int sign){
		double x1 = line.getX1();
		double y1 = line.getY1();
		double x2 = line.getX2();
		double y2 = line.getY2();
		double dist = Edge.distance(x1, y1, x2, y2);

		//各軸の単位増加にオフセット分を掛けて追加分の長さを求める
		double xInc = Math.abs((x1 - x2)/dist)*Constants.expandOffset * (double)sign;
		double yInc = Math.abs((y1 - y2)/dist)*Constants.expandOffset * (double)sign;
		x1 += (x1>x2)?xInc:-xInc;
		x2 += (x2>x1)?xInc:-xInc;
		y1 += (y1>y2)?yInc:-yInc;
		y2 += (y2>y1)?yInc:-yInc;
		
		return new Line2D.Double(x1,y1,x2,y2);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		Tuple2<Double, Double> nowOn = null;
		int nowx = e.getX();
		int nowy = e.getY();
		boolean isOn = false;
		for(int i = 0; i < scalePlots.size(); i++){
			Tuple2<Double, Double> t = scalePlots.get(i);
			double vx = t.t1;
			double vy = t.t2;
			double dist = Edge.distance(nowx, nowy, vx, vy);
			if(dist <= 5.0){
				isOn = true;
				nowOn = t;
				break;
			}
		}
		//Right Click
		if(e.getButton() == MouseEvent.BUTTON3){
			if(isSelect){
				clickP = null;
				isSelect = false;
			}else if(isLine){
				//乗ってる線削除
				for(int i = 0; i < scaleLines.size(); i++){
					if(onLine(nowx, nowy, scaleLines.get(i))){
						lines.remove(i);
						scaleLines.remove(i);
						break;
					}
				}
				drawBackground(false);
				drawLines(false);
				drawPlots(true);
			}
		//Left Click
		}else{
			if(isLine){
				Line2D l = null;
				int _index;
				for(_index = 0; _index < scaleLines.size(); _index++){
					if(onLine(nowx, nowy, scaleLines.get(_index))){
						l = this.expandLine(lines.get(_index), -10);
						break;
					}
				}
				parent.calcLeastSquare(l, _index);
				
//				drawBackground(false);
//				drawLines(false);
//				drawPlots(true);				
			}else if(isSelect){				//点選択時
				if(!isOn)return;
				double cx = clickP.t1;
				double cy = clickP.t2;
				double nowOnX = nowOn.t1;
				double nowOnY = nowOn.t2;
				
				//有効なラインか確認(重複確認含)
//				if(!isVerifyLine(nowOnX,nowOnY,cx,cy))return;
				
				Line2D line = new Line2D.Double(nowOnX, nowOnY, cx, cy);
				//点の上の時
				clickP = null;
				isSelect = false;
				lines.add(converToLine(line));
				scaleLines.add(line);
				paintLine(nowx, nowy);
			}else{
				//点の上の時
				if(isOn){
					isSelect = true;
					double nowOnX = nowOn.t1;
					double nowOnY = nowOn.t2;
					clickP = new Tuple2<Double, Double>(nowOnX, nowOnY);
				}else{
					System.out.println(e.getX()+","+e.getY());
					try {
						Robot r = new Robot();
						Point p = MouseInfo.getPointerInfo().getLocation();
						r.mouseMove(p.x-e.getX()+(int)((double)HandlSummaryPanel.visualizeWidth)+range, p.y-e.getY()+range);
					} catch (AWTException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					realtimeDialog.paintMoveScreen(range, range);
					//フォーカスをダイアログへ
//					realtimeDialog.requestFocusInWindow();
//					Point p = e.getLocationOnScreen();
//					p.x -= range/2;
//					if(p.x < 0)p.x = 0;
//					p.y -= range/2;
//					if(p.y < 0)p.y = 0;
//					dia.setLocation(p);
//					VisualizeFrame.setVisibleFrame(false);	
				}	
			}
			
		}
	}
	
	public boolean onLine(double x,double y, Line2D line){///(x,y)がこのエッジ上に存在するか判定
		double kx1 = line.getX1();
		double ky1 = line.getY1();
		double kx2 = line.getX2();
		double ky2 = line.getY2();
		double dist = Edge.distance(kx1, ky1, kx2, ky2);
		double sum = Edge.distance(x,y,kx1,ky1) + Edge.distance(x,y,kx2,ky2);
		double sabun = Math.abs(dist - sum);
		if(sabun < 1)return true;///線上にあるのでOK
		return false;
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
		int nowx = e.getX();
		int nowy = e.getY();
		drawDialog(nowx, nowy);
		//drawBackground(false);
		Graphics2D g = (Graphics2D)paint.getGraphics();
		g.setStroke(maxiStroke);
		isLine = false;
		for(int i = 0; i < scaleLines.size(); i++){
			Line2D line = scaleLines.get(i);
			g.setColor(Constants.newLineColor);
			if(onLine(nowx, nowy, line)){
				g.setColor(Color.BLUE);
				isLine = true;
			}
			g.draw(scaleLines.get(i));
		}
		g.setStroke(miniStroke);
		//drawLines();
		
		//paintLine(nowx, nowy);
		boolean isOn = false;
		for(int i = 0; i < scalePlots.size(); i++){
			Tuple2<Double, Double> t = scalePlots.get(i);
			double vx = t.t1;
			double vy = t.t2;
			double dist = Edge.distance(nowx, nowy, vx, vy);
			g.setColor(Constants.plotColor);
			if(dist <= 5.0){
				g.setColor(Constants.onPlotColor);
				isOn = true;
			}
			if(clickP!=null && Edge.distance(clickP.t1, clickP.t2, vx, vy) < 5.0){
				g.setColor(Color.yellow);
			}
			g.fillOval((int)vx - Constants.plotOvalRadius, 
					(int)vy - Constants.plotOvalRadius, 
					Constants.plotOvalRadius*2, 
					Constants.plotOvalRadius*2);
		}

		
		//点に乗っている場合には範囲方形は描画しない。
		g.dispose();
		parent.repaint();
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