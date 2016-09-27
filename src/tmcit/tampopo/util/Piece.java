package tmcit.tampopo.util;

import java.util.ArrayList;
import java.util.List;

public class Piece {
	
	private List<Point> points;///座標セット
	private List<Double> angleSet = null;///角度セット
	
	private Piece(List<Point> points){
		this.points = points;
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
		public List<Point> points;
		public PieceBuilder(){
			points = new ArrayList<Point>();
		}
		
		public Piece build(){
			return new Piece(points);
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
