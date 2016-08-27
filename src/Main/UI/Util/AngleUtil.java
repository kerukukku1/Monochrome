package Main.UI.Util;

import java.util.ArrayList;
import java.util.List;

import Mahito6.Main.Tuple2;
public class AngleUtil {
	
	private List<Tuple2<Double, Double>> target;
	
	public AngleUtil(List<Tuple2<Double, Double>> target){
		this.target = target;
	}
	
	public ArrayList<Double> calc() throws Exception{
		ArrayList<Double> ret = new ArrayList<Double>(target.size());
		for(int i = 0;i < target.size();i++){
			ret.add(0.0);
		}
		List<Tuple2<Double, Double>> tar = target;///�S���W�擾
		double degsum = 0.0;
		//double ccw0 = ccw(tar.get(0),tar.get(1),tar.get(2));
		int n = tar.size();
		for(int i = 0;i < n;i++){
			int next = (i+1)%n;
			int next2 = (i+2)%n;
			double ccwn = ccw(tar.get(i),tar.get(next),tar.get(next2));
			if(ccwn == 0.0){
				throw new Exception("Angle calc error! " + target.size());
			}
			double result = angle(tar.get(i),tar.get(next),tar.get(next2));
			if(ccwn < 0.0){
				result = 360.0 - result;
			}
			degsum += result;
			ret.set(next, result);
		}
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

	private double angle(Tuple2<Double, Double> v1,Tuple2<Double, Double> v2,Tuple2<Double, Double> v3){
		return angle(v1.t1,v1.t2,v2.t1,v2.t2,v3.t1,v3.t2);
	}


	private double cross(double x1,double y1,double x2,double y2){
		return x1 * y2 - x2 * y1;
	}

	private double ccw(double x1,double y1,double x2,double y2,double x3,double y3){
		return cross(x2 - x1, y2 - y1, x3 - x2, y3 - y2);
	}

	private double ccw(Tuple2<Double, Double> v1,Tuple2<Double, Double> v2,Tuple2<Double, Double> v3){
		return ccw(v1.t1,v1.t2,v2.t1,v2.t2,v3.t1,v3.t2);
	}

}
