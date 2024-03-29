package Mahito6.Solver;

import Mahito6.Main.Constants;
import Mahito6.Main.Tuple2;

public class Edge {

	public double kx1,ky1,kx2,ky2;///直線の端点
	public double r,theta;///直線のパラメータ(r = x*cosθ + y*sinθ)
	public double distance;///端点間の距離
	public static final double gosa = 10.0;///線上にあるか判定するアルゴリズムの許容誤差
	
	public Edge(double r,double theta,double kx1,double ky1,double kx2,double ky2){
		this.r = r;
		this.theta = theta;
		this.kx1 = kx1;
		this.ky1 = ky1;
		this.kx2 = kx2;
		this.ky2 = ky2;
		if(kx1 > kx2){
			///入れ替える
			this.kx1 = kx2;
			this.ky1 = ky2;
			this.kx2 = kx1;
			this.ky2 = ky1;
		}
		this.distance = distance(kx1,ky1,kx2,ky2);
	}
	
	public Edge(double kx1,double ky1,double kx2,double ky2){
		this.kx1 = kx1;
		this.ky1 = ky1;
		this.kx2 = kx2;
		this.ky2 = ky2;
		if(kx1 > kx2){
			///入れ替える
			this.kx1 = kx2;
			this.ky1 = ky2;
			this.kx2 = kx1;
			this.ky2 = ky1;
		}
		this.theta = calcTheta(kx1,ky1,kx2,ky2);
		this.r = calcRadius(kx1,ky1,theta);
		this.distance = distance(kx1,ky1,kx2,ky2);
	}
	
	public Edge getExtensionEdge(double value){///線分の端点をvalueだけ伸ばしたエッジを返す
		LinerMethod lm = new LinerMethod(new Point(kx1,ky1),new Point(kx2,ky2),1);
		Point newL = lm.getPointFromLength(-value);
		Point newR = lm.getPointFromLength(distance + value);
		return new Edge(r,theta,newL.x,newL.y,newR.x,newR.y);
	}

	public boolean onLine(double x,double y){///(x,y)がこのエッジ上に存在するか判定
		double sum = distance(x,y,kx1,ky1) + distance(x,y,kx2,ky2);
		double sabun = Math.abs(distance - sum);
		if(sabun < gosa)return true;///線上にあるのでOK
		return false;
	}
	public boolean onLine(Tuple2<Double,Double> vertex){///オーバーロード
		return onLine(vertex.t1,vertex.t2);
	}

	public double getA(){///計算用
		double calc = -1/Math.tan(theta);
		return calc;
	}
	public double getB(){
		double calc = r/Math.sin(theta);
		return calc;
	}

	public static Tuple2<Double,Double> getCross(Edge e1,Edge e2){///2本のエッジの交点を出す、
		double a1 = e1.getA();
		double b1 = e1.getB();
		double a2 = e2.getA();
		double b2 = e2.getB();

		double x = (b2-b1)/(a1-a2);
		double y = a1 * x + b1;
		return new Tuple2<Double,Double>(x,y);
	}

	public static double distance(double x1,double y1,double x2,double y2){///2点間の距離を返す
		return Math.sqrt(Math.pow(x1-x2, 2.0)+Math.pow(y1-y2,2.0));
	}
	
	public double calcTheta(double x1,double y1,double x2,double y2){
		return (Math.abs(y1-y2) < 0.000001)?Math.PI / 2 : Math.atan((x2 - x1)/(y1 - y2));
	}
	public double calcRadius(double x, double y, double theta){
		return x*Math.cos(theta) + y*Math.sin(theta);	
	}
}
