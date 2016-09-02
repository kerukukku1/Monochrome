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
    //スケールを合わせたプロット
    private List<Tuple2<Integer, Integer>> scalePlots;
    //合わせてないプロット
    private List<Tuple2<Double, Double>> plots;
    private double maxx, maxy;
    private boolean isSelect = false;
    private Line2D scaleLine;
    private List<Line2D> scaleLines;
    private List<Line2D> lines;
    private Tuple2<Integer, Integer> clickP = null;
    private int range = 150;
    private HashMap<Tuple2<Tuple2<Integer,Integer>, Tuple2<Integer,Integer>>, Boolean> lineMap;
    private BasicStroke miniStroke;
    private BasicStroke maxiStroke;
    private Tuple2<Tuple2<Integer, Integer>, Tuple2<Integer, Integer>> _hash;
    
    public VisualizePanel(List<Tuple2<Double, Double>> vertex, Coordinates coord, List<Line2D> lines){
    	this.vertex = vertex;
    	this.coord = coord;
    	this.lines = new ArrayList<Line2D>();
    	for(int i = 0 ; i < lines.size(); i++){
    		this.lines.add(this.expandLine(lines.get(i)));
    	}
		setUtil();
		//スケールの計算。これしないとバグる
		calcScale();
		//二値化されたピースをペイント
		paintPiece();
		
		//頂点データをスケールに合わせてプロットに変換。このときint型に丸め込まれる
		scalePlots = this.convertVertexToScalePlots(vertex);
		//線をスケールを合わせて描画。これはdoubleのまま保持される。
		scaleLines = this.convertScaleLines(this.lines);
		//スケールを合わせていないプロット
		plots = this.convertVertexToPlots(vertex);
		scaleLine = new Line2D.Double();
    	isSelect = false;
	    drawLines();
	    paintPlots();
    }
	
	private void setUtil(){
		this.setLayout(null);
		this.setBackground(Color.gray.brighter().brighter());
		this.setPreferredSize(new Dimension(300, 300));
		this.setOpaque(true);
		this.setTransferHandler(new DropFileHandler());

		//スクリーンサイズ以上のピースは存在しないようにスケールを合わせてある
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		gPiece = new BufferedImage(screenSize.width, screenSize.height, BufferedImage.TYPE_INT_ARGB);
		paint = new BufferedImage(screenSize.width, screenSize.height, BufferedImage.TYPE_INT_ARGB);
		earth = new JLabel(new ImageIcon(paint));
		earth.setBounds(0, 0, screenSize.width, screenSize.height);
		earth.addMouseListener(this);
		earth.addMouseMotionListener(this);
		this.add(earth);
		
		//線の太さ初期化
    	miniStroke = new BasicStroke(1.0f);
        maxiStroke = new BasicStroke(3.0f);
        
        //既存の線か判定するテーブル(要書き換え)
        lineMap = new HashMap<Tuple2<Tuple2<Integer,Integer>, Tuple2<Integer,Integer>>, Boolean>();
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
		
		g.setColor(Constants.backgroundColor);
		g.clearRect(0, 0, (int)(maxx+100), (int)(maxy+100));
		
//	    g.setStroke(maxiStroke);
//		polygon = new Polygon(xpoints, ypoints, xpoints.length);
//		g.setColor(Constants.polyColor.brighter());
//		g.drawPolygon(polygon);
//	    g.setStroke(miniStroke);

//        AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f);
//        // アルファ値をセット（以後の描画は半透明になる）
//        g.setComposite(composite);
	    g.setColor(Constants.coordColor);
	    for(int i = 0; i < coord.size(); i++){
	    	double x = (coord.getVisX(i)+Constants.imagePositionOffset/2)*scale;
	    	double y = (coord.getVisY(i)+Constants.imagePositionOffset/2)*scale;
	    	g.fillRect((int)x, (int)y, 1, 1);
	    }
	    
	    //画像として自身に保存し高速化	    
	    g.drawImage(gPiece, null, 0, 0);
	    //背景として保存したgPieceを描画する。
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
		for(int i = 0; i < scalePlots.size(); i++){
			Tuple2<Integer, Integer> now = scalePlots.get(i);
			int nx = now.t1;
			int ny = now.t2;
			g.fillOval(nx-Constants.plotOvalRadius, ny-Constants.plotOvalRadius, Constants.plotOvalRadius*2, Constants.plotOvalRadius*2);
		}
		this.repaint();
	}
	
	public void setPlots(List<Tuple2<Double, Double>> plots){
		this.plots = plots;
		this.scalePlots = this.encodeToScalePlot(plots);
		paintPlots();
	}
	
	private List<Tuple2<Integer, Integer>> encodeToScalePlot(List<Tuple2<Double, Double>> plots){
		List<Tuple2<Integer, Integer>> ret = new ArrayList<Tuple2<Integer, Integer>>();
		for(Tuple2<Double, Double> d : plots){
			double nowx = d.t1 * scale;
			double nowy = d.t2 * scale;
			ret.add(new Tuple2<Integer, Integer>((int)nowx, (int)nowy));
		}
		return ret;
	}
	
	private void drawLines(){
		Graphics2D g = (Graphics2D)paint.getGraphics();
		g.setStroke(maxiStroke);
		g.setColor(Constants.newLineColor);
		for(int i = 0; i < scaleLines.size(); i++){
			g.draw(scaleLines.get(i));
		}
	}
	
	private void paintLine(int nowx, int nowy){
		Graphics2D g = (Graphics2D)paint.getGraphics();
		g.setStroke(maxiStroke);
		g.setColor(Constants.newLineColor);
		drawLines();
		//点選択中
		if(isSelect){
			int cx = clickP.t1;
			int cy = clickP.t2;
			scaleLine.setLine(nowx, nowy, cx, cy);
			g.draw(scaleLine);
		}
	}
	
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
	
	private List<Tuple2<Integer, Integer>> convertVertexToScalePlots(List<Tuple2<Double, Double>> vertex){
		List<Tuple2<Integer, Integer>> ret = new ArrayList<Tuple2<Integer, Integer>>();
		for(Tuple2<Double, Double> v : vertex){
			double nowx = v.t1 * scale;
			double nowy = v.t2 * scale;
			ret.add(new Tuple2<Integer, Integer>((int)nowx, (int)nowy));
		}
		return ret;
	}
	
	private List<Tuple2<Double, Double>> convertVertexToPlots(List<Tuple2<Double, Double>> vertex){
		List<Tuple2<Double, Double>> ret = new ArrayList<Tuple2<Double, Double>>();
		for(Tuple2<Double, Double> v : vertex){
			double nowx = v.t1;
			double nowy = v.t2;
			ret.add(new Tuple2<Double, Double>(nowx, nowy));
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
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    double w = (double)screenSize.width;
	    double h = (double)screenSize.height;
	    //System.out.println("w:" + w + " h:" + h);
	    w -= 200;
	    h -= 200;
	    maxx = coord.maxx - coord.minx + Constants.imagePositionOffset/2;
	    maxy = coord.maxy - coord.miny + Constants.imagePositionOffset/2;
		this.setPreferredSize(new Dimension((int)(maxx+100), (int)(maxy+100)));
	    System.out.println(maxx + "," + maxy);
	    if(maxy < maxx && maxx > w){
	    	scale = w/maxx;
	    	if(coord.isError())return;
	    	maxx = 0.0;
	    	maxy = 0.0;
	    	for(int i = 0 ; i < data.size(); i++){
		    	Tuple2<Double, Double> d = data.get(i);
		    	double x = d.t1*scale;
		    	double y = d.t2*scale;
		    	maxx = (x < maxx)?maxx:x;
		    	maxy = (y < maxy)?maxy:y;   		
	    	}
	    }else if(maxx < maxy && maxy > h){
	    	scale = h/maxy;
	    	if(coord.isError())return;
	    	maxx = 0.0;
	    	maxy = 0.0;
	    	for(int i = 0 ; i < data.size(); i++){
		    	Tuple2<Double, Double> d = data.get(i);
		    	double x = d.t1*scale;
		    	double y = d.t2*scale;
		    	maxx = (x < maxx)?maxx:x;
		    	maxy = (y < maxy)?maxy:y;	
	    	}
	    }else{
	    	scale = 1.0;
	    }
	    
		this.setPreferredSize(new Dimension((int)(maxx+100), (int)(maxy+100)));
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
	
	public List<Tuple2<Integer, Integer>> getScalePlots(){
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
	
	private Line2D expandLine(Line2D line){
		double x1 = line.getX1();
		double y1 = line.getY1();
		double x2 = line.getX2();
		double y2 = line.getY2();
		double dist = Edge.distance(x1, y1, x2, y2);

		//各軸の単位増加にオフセット分を掛けて追加分の長さを求める
		double xInc = Math.abs((x1 - x2)/dist)*Constants.expandOffset;
		double yInc = Math.abs((y1 - y2)/dist)*Constants.expandOffset;
		
		x1 += (x1>x2)?xInc:-xInc;
		x2 += (x2>x1)?xInc:-xInc;
		y1 += (y1>y2)?yInc:-yInc;
		y2 += (y2>y1)?yInc:-yInc;
		
		return new Line2D.Double(x1,y1,x2,y2);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		Tuple2<Integer, Integer> nowOn = null;
		int nowx = e.getX();
		int nowy = e.getY();
		boolean isOn = false;
		for(int i = 0; i < scalePlots.size(); i++){
			Tuple2<Integer, Integer> t = scalePlots.get(i);
			int vx = t.t1;
			int vy = t.t2;
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
			}else{
				for(int i = 0; i < scaleLines.size(); i++){
				}
			}
		//Left Click
		}else{
			//点選択時
			if(isSelect){
				if(!isOn)return;
				int cx = clickP.t1;
				int cy = clickP.t2;
				int nowOnX = nowOn.t1;
				int nowOnY = nowOn.t2;
				
				//有効なラインか確認(重複確認含)
				if(!isVerifyLine(nowOnX,nowOnY,cx,cy))return;
				
				Graphics2D g = (Graphics2D)paint.getGraphics();
				g.setColor(Constants.newLineColor);
				Line2D line = new Line2D.Double(nowOnX, nowOnY, cx, cy);
				//点の上の時
				clickP = null;
				isSelect = false;
				//オフセットだけ伸ばす
				lines.add(this.expandLine(converToLine(line)));
				scaleLines.add(this.expandLine(line));
				paintLine(nowx, nowy);
			}else{
				//点の上の時
				if(isOn){
					isSelect = true;
					int nowOnX = nowOn.t1;
					int nowOnY = nowOn.t2;
					clickP = new Tuple2<Integer, Integer>(nowOnX, nowOnY);
				}else{
					System.out.println(e.getX()+","+e.getY());
					dia = new CorrectDialog(nowx, nowy, range, this);
					VisualizeFrame.setVisibleFrame(false);	
				}	
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
		int nowx = e.getX();
		int nowy = e.getY();
		paintLine(nowx, nowy);
		boolean isOn = false;
		for(int i = 0; i < scalePlots.size(); i++){
			Tuple2<Integer, Integer> t = scalePlots.get(i);
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