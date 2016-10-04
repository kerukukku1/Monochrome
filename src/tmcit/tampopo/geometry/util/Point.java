package tmcit.tampopo.geometry.util;

public class Point {
	
	public double x,y;
	
	public Point(double x,double y){
		this.x = x;
		this.y = y;
	}
	
	public double distance(double tx,double ty){
		return Math.sqrt(Math.pow(tx - x, 2.0) + Math.pow(ty - y, 2.0));
	}
	
	public double distance(Point tp){
		return distance(tp.x, tp.y);
	}

}
