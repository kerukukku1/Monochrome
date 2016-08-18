package Mahito6.Solver;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import Mahito6.Main.Tuple2;

public class CrossAlgorithm {///エッジから交点抽出するアルゴリズム
	
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
		
		for(int i = 0;i < n;i++){
			if(memo[i].size() <= 1){///交点1個以下
				memo[i].clear();
				continue;
			}else if(memo[i].size() == 2)continue;
			///交点が3個以上あるから2個にする
			
			List<Tuple2<Double,Double>> newList = null;
			List<Tuple2<Double,Double>> target = memo[i];
			double dis_max = 0.0;
			for(int j = 0;j < target.size();j++)
			for(int k = j+1;k < target.size();k++){
				Tuple2<Double,Double> c1 = target.get(j);
				Tuple2<Double,Double> c2 = target.get(k);
				double dis = Edge.distance(c1.t1, c1.t2, c2.t1, c2.t2);
				if(dis_max < dis){///解を更新
					dis_max = dis;
					newList = new ArrayList<Tuple2<Double,Double>>();
					newList.add(c1);
					newList.add(c2);
				}
			}
			
			memo[i] = newList;
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
			int x = (int)answer.get(i).t1.doubleValue();
			int y = (int)answer.get(i).t2.doubleValue();
			g2d.setColor(Color.YELLOW);
			g2d.fillOval(x-2, y-2, 4, 4);
			int nx = (int)answer.get(next).t1.doubleValue();
			int ny = (int)answer.get(next).t2.doubleValue();
			g2d.setColor(Color.WHITE);
			g2d.drawLine(x,y,nx,ny);
		}
		g2d.dispose();
	}
	
	private int saiki(int index,List<Tuple2<Double,Double>> tmp){
		if(memo[index].size() == 0)return 0;
		Tuple2<Double,Double> c1 = memo[index].get(0);
		memo[index].remove(c1);///削除
		int next = -1;
		for(int i = 0;i < n;i++){
			for(Tuple2<Double,Double> t : memo[i]){
				if(t == c1){
					next = i;
					memo[i].remove(c1);///移動先からも削除
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

}
