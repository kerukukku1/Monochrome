package tmcit.tampopo.util;

import java.util.ArrayList;
import java.util.List;

public class AngleUtil {
	
	private List<Point> target;
	
	public AngleUtil(List<Point> target){
		this.target = target;
	}
	
	public List<Double> calc() throws Exception{
		ArrayList<Double> ret = new ArrayList<Double>(target.size());
		for(int i = 0;i < target.size();i++){
			ret.add(0.0);
		}
		List<Point> tar = target;///�S���W�擾
		double degsum = 0.0;
		//double ccw0 = ccw(tar.get(0),tar.get(1),tar.get(2));
		int n = tar.size();
		for(int i = 0;i < n;i++){
			///�p�x
			int next = (i+1)%n;
			int next2 = (i+2)%n;///�z�����邽�߂�MOD�Ƃ�
			double ccwn = ccw(tar.get(i),tar.get(next),tar.get(next2));
			if(ccwn == 0.0){
				System.out.println(i + ":" + next + ":" + next2);
				System.out.println(tar.get(i).x + "," + tar.get(i).y);
				System.out.println(tar.get(next).x + "," + tar.get(next).y);
				System.out.println(tar.get(next2).x + "," + tar.get(next2).y);
				throw new Exception("Angle calc error! " + target.size());
			}
			double result = angle(tar.get(i),tar.get(next),tar.get(next2));
			if(ccwn < 0.0){
				///�݊p
				result = 360.0 - result;
			}
			degsum += result;
			ret.set(next, result);
		}
		///System.out.println(degsum+"��");
		return ret;
	}
	
	private double norm(double x,double y){
		return Math.sqrt(x*x+y*y);
	}

	private double angle(double a,double b,double x,double y,double o,double p){
		double vx1 = a-x;
		double vy1 = b-y;
		double vx2 = o-x;
		double vy2 = p-y;
		double up = vx1*vx2 + vy1*vy2;
		double down = norm(vx1,vy1) * norm(vx2,vy2);
		double theta = Math.acos(up/down);
		double deg = Math.toDegrees(theta);//theta * 180.0 / 3.1415926535;
		return deg;
	}

	private double angle(Point v1,Point v2,Point v3){
		return angle(v1.x,v1.y,v2.x,v2.y,v3.x,v3.y);
	}


	private double cross(double x1,double y1,double x2,double y2){
		return x1 * y2 - x2 * y1;
	}

	private double ccw(double x1,double y1,double x2,double y2,double x3,double y3){
		return cross(x2 - x1, y2 - y1, x3 - x2, y3 - y2);
	}

	private double ccw(Point v1,Point v2,Point v3){
		return ccw(v1.x,v1.y,v2.x,v2.y,v3.x,v3.y);
	}

}
