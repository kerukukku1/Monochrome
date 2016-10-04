package tmcit.tampopo.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import tmcit.tampopo.geometry.util.Piece;
import tmcit.tampopo.geometry.util.Point;
import tmcit.tampopo.geometry.util.Segment;
import tmcit.tampopo.ui.util.ExVertex;

public class PuzzleImage {
	
	private static Color[] colorDef = {	//�s�[�X�̐F�̎��
			new Color(0,240,0),
			new Color(0,0,240),
			new Color(240,240,0),
			new Color(0,240,240),
			new Color(240,0,240),
			new Color(190,0,240),
			new Color(240,120,120),
			new Color(120,240,120),
			new Color(120,120,240),
			new Color(240,180,40),
			new Color(64,0,178),
			new Color(98,161,83),
			new Color(165,165,165),
			new Color(240,240,240)
	};
	
	private static final int WIDTH = 600;///WIDTH��HEIGHT�͓����ɂ��Ăق���
	private static final int offset = 5;
	private static final int hvalue = 1;///�L������
	
	private BufferedImage image = null;
	private List<Piece> pieces = null;
	private List<Piece> frames = null;
	private int width,height;
	private double expansion;
	private int slidex,slidey;///�ŏ��ɍ��W���炱�̕�����
	private Polygon[] polygons;
	private List<ExVertex> edges;
	
	
	public PuzzleImage(List<Piece> frames,List<Piece> pieces){
		this.frames = frames;
		this.pieces = pieces;
		init();
	}
	
	public BufferedImage getImage(){
		return image;
	}
	
	private void init(){
		int lx = 100000;
		int dy = -100000;
		int rx = -100000;
		int uy = 100000;
		if(!frames.isEmpty()){
			for(Piece frame : frames){
				for(Point vertex : frame.getPoints()){
					int x = (int)vertex.x;
					int y = (int)vertex.y;
					lx = Math.min(lx, x);
					uy = Math.min(uy, y);
					rx = Math.max(rx, x);
					dy = Math.max(dy, y);
				}
			}
		}else{
			for(Piece piece : pieces){
				for(Point vertex : piece.getPoints()){
					int x = (int)vertex.x;
					int y = (int)vertex.y;
					lx = Math.min(lx, x);
					uy = Math.min(uy, y);
					rx = Math.max(rx, x);
					dy = Math.max(dy, y);
				}
			}
		}
		width = rx - lx + offset;
		height = dy - uy + offset;
		expansion = ((double)WIDTH)/((double)Math.max(width, height));
		slidex = lx - offset;
		slidey = uy - offset;
	}
	
	public Piece getPieceFromPoint(double x,double y){
		for(int id = 0;id < polygons.length;id++){
			if(polygons[id].contains(x,y))return pieces.get(id);
		}
		return null;
	}
	
	public ExVertex getFrameAndVertexFromPoint(double x,double y){
		for(ExVertex exVertex : edges){
			if(exVertex.isOnLine(x,y))return exVertex;
		}
		return null;
	}
	
	public void paint(int width
					 ,boolean frameDegree
					 ,boolean frameLength
					 ,boolean pieceDegree
					 ,boolean pieceLength
					 ,boolean frameIndex
					 ,boolean pieceIndex
					 ,int targetFrameIndex
					 ,int targetVertexIndex){///percent:�g��̊���
		double percent = ((double)width)/((double)WIDTH);
		double realper = percent * expansion * 0.9;
		image = new BufferedImage(width,width,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) image.getGraphics();
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, width - 1, width - 1);
		


		///�s�[�X�̕`��
		polygons = new Polygon[pieces.size()];
		edges = new ArrayList<ExVertex>();
		int id = 0;
		for(Piece piece : pieces){
			int pieceID = piece.getID();
			if(piece.getPieceColor() != null){
				g2d.setColor(piece.getPieceColor());
			}else{
				g2d.setColor(colorDef[pieceID%colorDef.length]);
			}
			Polygon fill = new Polygon();
			for(int i = 0;i < piece.getPointSize();i++){
				Segment edge = piece.getSegment(i);
				double x = (edge.p1.x - slidex) * realper;
				double y = (edge.p1.y - slidey) * realper;
				fill.addPoint((int)x, (int)y);
			}
			g2d.fillPolygon(fill);
			for(int i = 0;i < piece.getPointSize();i++){
				g2d.setColor(Color.WHITE);
				Segment edge = piece.getSegment(i);
				double x = (edge.p1.x - slidex) * realper;
				double y = (edge.p1.y - slidey) * realper;
				double tx = (edge.p2.x - slidex) * realper;
				double ty = (edge.p2.y - slidey) * realper;
				g2d.drawLine((int)x, (int)y, (int)tx, (int)ty);
				if(pieceDegree){
					g2d.drawString(getShortDouble(piece.getAngle(i),hvalue)+"d", (int)x, (int)y);
				}
				if(pieceLength){
					double hx = (x + tx)/2.0;
					double hy = (y + ty)/2.0;
					g2d.drawString(getShortDouble(piece.getSegmentLength(i),hvalue), (int)hx, (int)hy);
				}
				if(pieceIndex){
					g2d.setColor(Color.RED);
					g2d.drawString(""+i, (int)x - 8, (int)y - 3);
				}
			}
			polygons[id] = fill;
			id++;
		}
		///�t���[���̕`��
		for(int frameidx = 0;frameidx < frames.size();frameidx++){
			Piece frame = frames.get(frameidx);
			for(int i = 0;i < frame.getPointSize();i++){
				
				if(targetFrameIndex == frameidx && targetVertexIndex == i){
					g2d.setColor(Color.RED);
				}else{
					g2d.setColor(Color.YELLOW);
				}
				Segment edge = frame.getSegment(i);
				double x = (edge.p1.x - slidex) * realper;
				double y = (edge.p1.y - slidey) * realper;
				double tx = (edge.p2.x - slidex) * realper;
				double ty = (edge.p2.y - slidey) * realper;
				g2d.drawLine((int)x, (int)y, (int)tx, (int)ty);
				edges.add(new ExVertex(frameidx, i, new Point(x, y), new Point(tx, ty)));
				if(frameDegree){
					g2d.drawString(getShortDouble(frame.getAngle(i),hvalue)+"d", (int)x, (int)y);
				}
				if(frameLength){
					double hx = (x + tx)/2.0;
					double hy = (y + ty)/2.0;
					g2d.drawString(getShortDouble(frame.getSegmentLength(i),hvalue), (int)hx, (int)hy);
				}
				if(frameIndex){
					g2d.setColor(Color.RED);
					g2d.drawString(""+i, (int)x - 8, (int)y - 3);
				}
			}
		}
		
		g2d.dispose();
	}
	
	public static String getShortDouble(double v,int h){///h:�L����������
		double ret = v * Math.pow(10.0, h);
		ret = (int)ret;
		ret /= Math.pow(10.0, h);
		return String.valueOf(ret);
	}

}
