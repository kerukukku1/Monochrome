package tmcit.tampopo.geometry.util;

import java.util.ArrayList;
import java.util.List;

public class Piece {
	
	private int id;
	private List<Point> points;///座標セット
	private List<Double> angleSet = null;///角度セット
	
	private Piece(int id,List<Point> points){
		this.id = id;
		this.points = points;
	}
	
	public Piece getCopy(){
		PieceBuilder pieceBuilder = new PieceBuilder();
		pieceBuilder.setID(id);
		for(Point point : points){
			pieceBuilder.addPoint(point.x, point.y);
		}
		try {
			return pieceBuilder.build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public int getID(){
		return id;
	}
	
	public Point getPoint(int index){
		return points.get(index);
	}
	public List<Point> getPoints(){
		return this.points;
	}
	
	public int getPointSize(){
		return points.size();
	}
	
	public Segment getSegment(int index){
		int next = (index+1)%points.size();
		return new Segment(points.get(index), points.get(next));
	}
	
	public double getSegmentLength(int index){
		Segment ret = getSegment(index);
		return ret.length;
	}
	
	public double getAngle(int index){
		if(angleSet == null)calcAllAngle();
		return angleSet.get(index);
	}
	
	private void calcAllAngle(){
		AngleUtil angleUtil = new AngleUtil(points);
		try {
			angleSet = angleUtil.calc();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static class PieceBuilder{
		public int id;
		public List<Point> points;
		public PieceBuilder(){
			id = -1;
			points = new ArrayList<Point>();
		}
		
		public Piece build() throws Exception{
			if(id == -1 || points.size() <= 2)throw new Exception("PIECE DAME DESU.");
			return new Piece(id,points);
		}
		
		public void setID(int id){
			this.id = id;
		}
		
		public PieceBuilder addPoint(Point point){
			points.add(point);
			return this;
		}
		
		public PieceBuilder addPoint(double x,double y){
			return addPoint(new Point(x, y));
		}
		
	}

}
