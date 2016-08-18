package Mahito6.Solver;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import Mahito6.Main.Tuple2;

public class LeastSquareMethod {
	
	private static final int MethodWidth = 5;
	private static final int hashkey = 100000;
	public static final File saveFile4 = new File("leastsquare.png");///デバッグ用
	
	private BufferedImage sourceImage;
	private BufferedImage tester = null;
	
	public LeastSquareMethod(BufferedImage sourceImage){
		this.sourceImage = sourceImage;
		this.tester = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), BufferedImage.TYPE_INT_BGR);
	}
	
	public void finish(){
		try {
			ImageIO.write(tester, "png", saveFile4);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("SAVE");
	}
	
	public double calcSumR(Tuple2<Double,Double> line,ArrayList<Tuple2<Integer,Integer>> target){
		double R = 0.0;
		double t = line.t1;
		double r = line.t2;
		double cost = Math.cos(t);
		double sint = Math.sin(t);
		for(Tuple2<Integer,Integer> tu2:target){
			double x = tu2.t1;
			double y = tu2.t2;
			
			double imy = (r - x * cost)/sint;
			double dif = y - imy;
			R += Math.pow(dif, 2.0);
		}
		return R;
	}
	
	public Tuple2<Double,Double> detectAndConvert(Edge preAns){///最小二乗法によって線を修正する。
		int w = sourceImage.getWidth();
		int h = sourceImage.getHeight();
        double sint = Math.sin(preAns.theta);
        double cost = Math.cos(preAns.theta);
        double r = preAns.r;
        Set<Long> set = new HashSet<Long>();
        if(sint != 0){
            for(int x = 0; x < w; x++){
                int y = (int)((r - x * cost) / sint);
                if(y < 0 || y >= h) continue;
                if(!preAns.onLine(x, y))continue;
                
            	for(int i = -MethodWidth;i <= MethodWidth;i++){
            		for(int j = -MethodWidth;j <= MethodWidth;j++){
            			int tx = x + j;
            			int ty = y + i;
            			if(tx<0||ty<0||tx>=w||ty>=h)continue;
            			if(sourceImage.getRGB(tx, ty) == -1){
            				long v = hashkey * ty + tx;
            				set.add(v);
            			}
            		}
            	}
            	
            }
        }
        if(cost != 0){
            for(int y = 0; y < h; y++){
                int x = (int)((r - y * sint) / cost);
                if(x < 0 || x >= w) continue;
                if(!preAns.onLine(x, y))continue;
                
            	for(int i = -MethodWidth;i <= MethodWidth;i++){
            		for(int j = -MethodWidth;j <= MethodWidth;j++){
            			int tx = x + j;
            			int ty = y + i;
            			if(tx<0||ty<0||tx>=w||ty>=h)continue;
            			if(sourceImage.getRGB(tx, ty) == -1){
            				long v = hashkey * ty + tx;
            				set.add(v);
            			}
            		}
            	}
            	
            }
        }
        ArrayList<Long> conv = new ArrayList<Long>(set);
        ArrayList<Tuple2<Integer,Integer>> next = new ArrayList<Tuple2<Integer,Integer>>();
        ArrayList<Tuple2<Integer,Integer>> nextRotated = new ArrayList<Tuple2<Integer,Integer>>();
        for(Long l:conv){
        	long x = l%hashkey;
        	long y = l/hashkey;
        	next.add(new Tuple2<Integer,Integer>((int)x,(int)y));
        	nextRotated.add(new Tuple2<Integer,Integer>((int)-y,(int)x));///90°rotation
        	
        	Graphics2D g2d = (Graphics2D) tester.getGraphics();
        	g2d.setColor(Color.RED);
        	g2d.fillRect((int)x, (int)y, 1, 1);
        	g2d.dispose();
//        	System.out.println(x+","+y);
        }
        Tuple2<Double,Double> ans1 = solve(next);
        Tuple2<Double,Double> ans2 = solve(nextRotated);
        double v1 = calcSumR(ans1,next);
        double v2 = calcSumR(ans2,nextRotated);
        System.out.println(v1+","+v2);
        if(v1 <= v2)return ans1;
        
        double t1 = ans2.t1,r1 = ans2.t2;
        double x1 = 0.0,x2 = sourceImage.getWidth();
        double y1 = (r1 - x1 * Math.cos(t1)) / Math.sin(t1);
        double y2 = (r1 - x2 * Math.cos(t1)) / Math.sin(t1);
        
        double tx1 = y1;
        double ty1 = -x1;
        double tx2 = y2;
        double ty2 = -x2;
        
        ans2 = convertLine(new Tuple2(tx1,ty1),new Tuple2(tx2,ty2));
        
        Graphics2D g2d = (Graphics2D) tester.getGraphics();
        g2d.setColor(Color.BLUE);
        g2d.drawLine((int)tx1, (int)ty1, (int)tx2, (int)ty2);
        g2d.dispose();
        
        return ans2;
	}
	
	public Tuple2<Double,Double> solve(List<Tuple2<Integer,Integer>> target){
		int n = target.size();
		double xbar = 0.0,ybar = 0.0;
		double upper_a = 0.0,lower_a = 0.0;

		for(int i = 0;i<n;i++){///xの平均とyの平均を求める
			xbar += target.get(i).t1;
			ybar += target.get(i).t2;
		}
		xbar /= (double)n;
		ybar /= (double)n;


		for(int i = 0;i<n;i++){
			double x = target.get(i).t1;
			double y = target.get(i).t2;
			upper_a += (y - ybar) * x;
			lower_a += Math.pow((x - xbar),2.0);
		}
		double fin_a, fin_b;
		if(lower_a == 0){
			fin_a = 0;
		}else{
			fin_a = upper_a / lower_a;	
		}
		fin_b = ybar - (fin_a * xbar);
		
		System.out.println("a = "+fin_a+", b = "+fin_b);
		
		double x1 = 0,x2 = sourceImage.getWidth();
		double y1 = fin_a * x1 + fin_b;
		double y2 = fin_a * x2 + fin_b;
		return convertLine(new Tuple2(x1,y1),new Tuple2(x2,y2));
	}
	
	public Tuple2<Double,Double> convertLine(Tuple2<Double,Double> co1,Tuple2<Double,Double> co2){
		double x1 = co1.t1 , x2 = co2.t1;
		double y1 = co1.t2 , y2 = co2.t2;
		double theta = Math.atan((x2 - x1)/(y1 - y2));
		double r = x1*Math.cos(theta) + y1*Math.sin(theta);
		return new Tuple2<Double,Double>(theta,r);
	}
	

}
