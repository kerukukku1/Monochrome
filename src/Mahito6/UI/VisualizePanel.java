package Mahito6.UI;

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

import Mahito6.Main.Tuple2;
import Mahito6.Solver.DiffPiece;

public class VisualizePanel extends JPanel implements MouseListener{
	
	private BufferedImage gPiece;
	private int nowPiece;
    private Polygon polygon;
    private JLabel earth;
    private List< List<Tuple2<Double, Double>> > vertex;
    private List<String> textData;
    private double scale = 1.0;
    private double[] scales;
    private DiffPiece diff;
    private CorrectDialog dia = null;
    
    public VisualizePanel(List< List<Tuple2<Double, Double>> > vertex){
    	this.vertex = vertex;
		nowPiece = 0;
		setUtil();
		paintPiece(nowPiece);
		this.setTransferHandler(new DropFileHandler());
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
		earth.addMouseListener(this);
		this.add(earth);
		
		scales = new double[51];
		for(int i = 0; i < 51; i++){
			scales[i] = -1.0;
		}
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
	    w -= 200;
	    h -= 200;
	    if(maxx > w){
	    	scales[index] = w/maxx;
	    	maxx = 0.0;
	    	maxy = 0.0;
	    	for(int i = 0 ; i < data.size(); i++){
		    	Tuple2<Double, Double> d = data.get(i);
		    	double x = d.t1*scales[nowPiece];
		    	double y = d.t2*scales[nowPiece];
		    	maxx = (x < maxx)?maxx:x;
		    	maxy = (y < maxy)?maxy:y;
		    	xpoints[i] = (int)x;
		    	ypoints[i] = (int)y;	    		
	    	}
	    }else if(maxy > h){
	    	scales[index] = h/maxy;
	    	maxx = 0.0;
	    	maxy = 0.0;
	    	for(int i = 0 ; i < data.size(); i++){
		    	Tuple2<Double, Double> d = data.get(i);
		    	double x = d.t1*scales[nowPiece];
		    	double y = d.t2*scales[nowPiece];
		    	maxx = (x < maxx)?maxx:x;
		    	maxy = (y < maxy)?maxy:y;
		    	xpoints[i] = (int)x;
		    	ypoints[i] = (int)y;	    		
	    	}
	    }else{
	    	scales[index] = 1.0;
	    }
	    
		this.setPreferredSize(new Dimension((int)(maxx+100), (int)(maxy+100)));
		g.setColor(Color.black);
		g.clearRect(0, 0, (int)(maxx+100), (int)(maxy+100));
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
		System.out.println(scales[nowPiece]);
		dia = new CorrectDialog((int)e.getX(), (int)e.getY(), 150, vertex.get(nowPiece), scales[nowPiece], this);
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
			g.fillOval(nx-3, ny-3, 6, 6);
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

}