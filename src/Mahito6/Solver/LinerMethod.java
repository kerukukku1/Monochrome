package Mahito6.Solver;

public class LinerMethod {
	
	private int type;
	private double r,theta;
	private double kx,ky;
	private double rx,ry;
	private double cost,sint;
	private Point p1,p2;
	
	private boolean isSP = false;///�������̗�O����
	
	public LinerMethod(Point p1,Point p2,int type){///type 1:p1���� 2:p2����X�^�[�g
		this.type = type;
		this.p1 = p1;
		this.p2 = p2;
		init();
	}
	
	private void init(){
		if(type == 1){
			kx = p1.x;ky = p1.y;
			rx = p2.x;ry = p2.y;
		}else{
			kx = p2.x;ky = p2.y;
			rx = p1.x;ry = p1.y;
		}
		double x1 = p1.x;
		double y1 = p1.y;
		double x2 = p2.x;
		double y2 = p2.y;
		double ysa = (y2 - y1);
		if(ysa == 0.0){
			this.theta = Math.PI/2.0;
			isSP = true;
		}else{
			this.theta = Math.atan((x1-x2)/(y2-y1));
		}
		this.cost = Math.cos(theta);
		this.sint = Math.sin(theta);
		this.r = x1*cost + y1*sint;
	}
	
	public Point getPointFromLength(double length){
		double y2 = ky + length * Math.sqrt(1.0 / (Math.tan(theta)*Math.tan(theta) + 1));
		double x2 = (r - y2*sint)/cost;
		double y3 = ky - length * Math.sqrt(1.0 / (Math.tan(theta)*Math.tan(theta) + 1));
		double x3 = (r - y3*sint)/cost;
		if(isSP){
			x2 = kx + length;
			x3 = kx - length;
		}
		
		double dis1 = Math.pow(x2-rx, 2.0) + Math.pow(y2-ry, 2.0);
		double dis2 = Math.pow(x3-rx, 2.0) + Math.pow(y3-ry, 2.0);
		if(dis1 < dis2){
			return new Point(x2,y2);
		}else{
			return new Point(x3,y3);
		}
	}

}
