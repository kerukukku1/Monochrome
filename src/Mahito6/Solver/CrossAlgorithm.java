package Mahito6.Solver;



import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import Mahito6.Main.Tuple2;

public class CrossAlgorithm {///�G�b�W�����_���o����A���S���Y��

	/*
	 * ���F
	 *	������_��2���邪����1���{���̌�_�łȂ��ꍇ���������Ȃ�
	 *
	 *
	 */

	private static final double perm1 = 10.0;///���������̕����덷�̋��e�l(���̒l��蕽��������������Γ��������Ƃ݂Ȃ�)

	private int w,h;
	private List<Edge> edges;
	private List<Tuple2<Double,Double>> answer;
	private BufferedImage ansImage;

	public CrossAlgorithm(List<Edge> input,int w,int h){
		this.edges = input;
		this.w = w;
		this.h = h;
	}

	public List<Tuple2<Double,Double>> getAnswer(){
		return answer;
	}
	public BufferedImage getAnswerImage(){
		return ansImage;
	}

	private List<Tuple2<Double,Double>>[] memo;
	private int n;
	public void solve(){
		answer = new ArrayList<Tuple2<Double,Double>>();
		ansImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_BGR);
		n = edges.size();
		memo = new ArrayList[n];
		for(int i = 0;i < n;i++)memo[i] = new ArrayList<Tuple2<Double,Double>>();

		for(int i = 0;i < n;i++)
		for(int j = i+1;j < n;j++){
			Edge e1 = edges.get(i);
			Edge e2 = edges.get(j);
			Tuple2<Double,Double> cp = Edge.getCross(e1, e2);
			if(!e1.onLine(cp) || !e2.onLine(cp))continue;
			memo[i].add(cp);
			memo[j].add(cp);
		}
		List<Integer> ones = new ArrayList<Integer>();///1������_�����Ȃ��G�b�W
		for(int i = 0;i < n;i++){
			if(memo[i].size() <= 1){///��_1�ȉ�
				if(memo[i].size() == 1){ones.add(i);continue;}
				memo[i].clear();
				continue;
			}else if(memo[i].size() == 2)continue;
			///��_��3�ȏ゠�邩��2�ɂ���

			List<Tuple2<Double,Double>> newList = null;
			List<Tuple2<Double,Double>> target = memo[i];
			double dis_max = 0.0;
			for(int j = 0;j < target.size();j++)
			for(int k = j+1;k < target.size();k++){
				Tuple2<Double,Double> c1 = target.get(j);
				Tuple2<Double,Double> c2 = target.get(k);
				double dis = Edge.distance(c1.t1, c1.t2, c2.t1, c2.t2);
				if(dis_max < dis){///�����X�V
					dis_max = dis;
					newList = new ArrayList<Tuple2<Double,Double>>();
					newList.add(c1);
					newList.add(c2);
				}
			}

			memo[i] = newList;
		}
		for(int i = 0;i < ones.size();i++)
		for(int j = i + 1;j < ones.size();j++){
			int t1 = ones.get(i);
			int t2 = ones.get(j);
			if(memo[t1].size() != 1 || memo[t2].size() != 1)continue;
			Edge e1 = edges.get(t1);
			Edge e2 = edges.get(t2);
			double rs = e1.r - e2.r;
			double ts = e1.theta - e2.theta;
			double square = Math.sqrt(Math.pow(rs, 2.0) + Math.pow(ts, 2.0));
			System.out.println(square);
			if(square > perm1)continue;
			Tuple2<Double,Double> c2 = memo[t2].get(0);///�Е��̌�_�������Е��ɂԂ�����
			memo[t1].add(c2);
			memo[t2].clear();///�Е��͏���
		}
		int maxi = 0;
		for(int i = 0;i < n;i++){
			List<Tuple2<Double,Double>> tmp = new ArrayList<Tuple2<Double,Double>>();
			int len = saiki(i,tmp);
			if(maxi < len){
				maxi = len;
				answer = tmp;
			}
		}
		Graphics2D g2d = (Graphics2D) ansImage.getGraphics();
		for(int i = 0;i < answer.size();i++){
			int next = (i + 1)%answer.size();
			int nnext = (i + 2)%answer.size();
			int x = (int)answer.get(i).t1.doubleValue();
			int y = (int)answer.get(i).t2.doubleValue();
			g2d.setColor(Color.YELLOW);
			g2d.fillOval(x-2, y-2, 4, 4);
			int nx = (int)answer.get(next).t1.doubleValue();
			int ny = (int)answer.get(next).t2.doubleValue();
			g2d.setColor(Color.WHITE);
			g2d.drawLine(x,y,nx,ny);
		}
		double ccw0 = 0.0;
		for(int i = 0;i < answer.size();i++){
			int next = (i + 1)%answer.size();
			int nnext = (i + 2)%answer.size();
			double x = answer.get(i).t1.doubleValue();
			double y = answer.get(i).t2.doubleValue();
			double nx = answer.get(next).t1.doubleValue();
			double ny = answer.get(next).t2.doubleValue();
			double nnx = answer.get(nnext).t1.doubleValue();
			double nny = answer.get(nnext).t2.doubleValue();
			if(i == 0){
				ccw0 = ccw(x,y,nx,ny,nnx,nny);
			}
			double ccwn = ccw(x,y,nx,ny,nnx,nny);
			double result = angle(x,y,nx,ny,nnx,nny);
			if(ccw0 * ccwn <= 0.0){
				///�݊p
				result = 360.0 - result;
			}
			System.out.println(result);
			result *= 100;
			result = (int)result;
			result /= 100;
			g2d.setColor(Color.WHITE);
			g2d.drawString(String.valueOf(result), (int)nx, (int)ny);
		}
		g2d.dispose();
	}

	private int saiki(int index,List<Tuple2<Double,Double>> tmp){
		if(memo[index].size() == 0)return 0;
		Tuple2<Double,Double> c1 = memo[index].get(0);
		memo[index].remove(c1);///�폜
		int next = -1;
		for(int i = 0;i < n;i++){
			for(Tuple2<Double,Double> t : memo[i]){
				if(t == c1){
					next = i;
					memo[i].remove(c1);///�ړ��悩����폜
					break;
				}
			}
		}
		int sum = 0;
		if(next != -1){
			tmp.add(c1);
			sum += saiki(next,tmp) + 1;
		}
		return sum;
	}

	private double ccw(double x1,double y1,double x2,double y2,double x3,double y3){
		return cross(x2 - x1, y2 - y1, x3 - x2, y3 - y2);
	}

	private double cross(double x1,double y1,double x2,double y2){
		return x1 * y2 - x2 * y1;
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
		double deg = theta * 180.0 / 3.1415926535;
		return deg;
	}


}
