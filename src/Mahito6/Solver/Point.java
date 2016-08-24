package Mahito6.Solver;

public class Point {
	public double x,y;
	public Point(Point p){
		this.x = p.x;
		this.y = p.y;
	}
	
	public Point(double x ,double y){
		this.x = x;
		this.y = y;
	}
	
	public Point(){
		this.x = 0;
		this.y = 0;
	}
	
	//�R�s�[
	public static Point getCopy(Point p){
		return new Point(p);
	}
	
	
}