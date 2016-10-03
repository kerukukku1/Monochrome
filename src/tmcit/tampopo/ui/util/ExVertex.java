package tmcit.tampopo.ui.util;

import tmcit.tampopo.geometry.util.Point;

public class ExVertex {
	
	public static final double EPS = 5;
	
	public int frame,index;
	public Point p1,p2;
	
	public ExVertex(int frame,int index,Point p1,Point p2){
		this.frame = frame;
		this.index = index;
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public boolean isOnLine(double x,double y){
		double d1 = p1.distance(x, y);
		double d2 = p2.distance(x, y);
		if((d1 + d2) < (p1.distance(p2)+EPS)){
			return true;
		}
		return false;
	}

}
