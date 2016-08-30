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
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    private double maxx, maxy;
    private int[] xpoints;
    private int[] ypoints;
    private int range = 150;
    
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
		g.setColor(Color.black);
		g.clearRect(0, 0, (int)(maxx+100), (int)(maxy+100));
	    BasicStroke normalStroke = new BasicStroke(3.0f);
	    g.setStroke(normalStroke);
		polygon = new Polygon(xpoints, ypoints, xpoints.length);
		g.setColor(Color.YELLOW);
		g.drawPolygon(polygon);
		
        AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f);
        // アルファ値をセット（以後の描画は半透明になる）
	    g.setColor(Color.WHITE);
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
	
	public static List<String> readTxt(File file) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(file));
		List<String> in = new ArrayList<String>();
		String str = br.readLine();
		while(str != null){in.add(str);str = br.readLine();}
		br.close();
		return in;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println(e.getX()+","+e.getY());
		dia = new CorrectDialog((int)e.getX(), (int)e.getY(), range, vertex, coord, scale, this);
		VisualizeFrame.setVisibleFrame(false);
	}
	
	public void paintPlots(List<Tuple2<Integer, Integer>> plots){
		Graphics2D g = (Graphics2D)gPiece.getGraphics();
		for(int i = 0; i < plots.size(); i++){
			Tuple2<Integer, Integer> now = plots.get(i);
			int nx = now.t1;
			int ny = now.t2;
			System.out.println(nx + "," + ny);
			g.setColor(Color.RED);
			g.fillOval(nx-4, ny-4, 8, 8);
		}
		this.repaint();
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
		g.setColor(Color.GREEN.brighter());
		int x = e.getX();
		int y = e.getY();
		g.drawRect(x-range, y-range, range*2, range*2);
//		g.drawLine(x-range, y-range, x-range, y+range);
//		g.drawLine(x-range, y-range, x+range, y-range);
//		g.drawLine(x+range, y+range, x+range, y-range);
//		g.drawLine(x+range, y+range, x-range, y+range);
		this.repaint();
	}

}