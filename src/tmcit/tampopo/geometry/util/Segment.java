package tmcit.tampopo.geometry.util;

public class Segment {
	
	public Point p1,p2;
	public double length;
	
	public Segment(Point p1,Point p2){
		this.p1 = p1;
		this.p2 = p2;
		length = Math.sqrt(Math.pow(p1.x - p2.x, 2.0)+Math.pow(p1.y - p2.y, 2.0));
	}

}
