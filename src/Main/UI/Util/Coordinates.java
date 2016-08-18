package Main.UI.Util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Coordinates{
	public ArrayList<Integer> arx;
	public ArrayList<Integer> ary;
	public ArrayList<Double> ansx;
	public ArrayList<Double> ansy;
	public int now, maxx, maxy, minx, miny;
	private int[] dx;
	private int[] dy;
	private boolean[][] memo;
	private boolean[][] state;
	private JQueue que;
	private long funcCount = 0;
	public Coordinates(){
		arx = new ArrayList<Integer>();
		ary = new ArrayList<Integer>();
		now = 0;
		maxx = 0;
		maxy = 0;
		minx = 100000;
		miny = 100000;
		que = new JQueue(200000);
	}
	
	public void addCord(int x, int y){
		arx.add(x);
		ary.add(y);
	}
	
	public int getX(int index){
		return arx.get(index);
	}
	
	public int getY(int index){
		return ary.get(index);
	}
	
	public void calc(){
		maxx = 0;
		maxy = 0;
		minx = 100000;
		miny = 100000;
		for(int i = 0; i < this.size(); i++){
			maxx = Math.max(maxx, arx.get(i));
			maxy = Math.max(maxy, ary.get(i));
			minx = Math.min(minx, arx.get(i));
			miny = Math.min(miny, ary.get(i));
		}
	}
	
	public void clearNoise(int threshold){
		this.calc();
		int h = maxy+1;
		int w = maxx+1;
		memo = new boolean[w][h];
		state = new boolean[w][h];
		this.makeDXDY(1);
		for(int i = 0; i < size(); i++){
			if(getX(i) >= w || getY(i) >= h)System.out.println("Over");
			state[getX(i)][getY(i)] = true;
		}
		ArrayList<Integer> arx2 = new ArrayList<Integer>();
		ArrayList<Integer> ary2 = new ArrayList<Integer>();
		for(int i = 0; i < size(); i++){
			int x = arx.get(i);
			int y = ary.get(i);
			if(memo[x][y])continue;
			memo[x][y] = true;
			if(state[x][y]){
				Coordinates tc = findPoints(x,y);
				if(tc.size() > threshold){
					for(int k = 0; k < tc.size(); k++){
						arx2.add(tc.getX(k));
						ary2.add(tc.getY(k));
					}
				}
			}
		}
		arx = arx2;
		ary = ary2;
		this.calc();
	}
	
	private void makeDXDY(int range){
		dx = new int[(range*2+1)*(range*2+1)-1];
		dy = new int[(range*2+1)*(range*2+1)-1];
		int count = 0;
		for(int i = -range; i <= range; i++){
			for(int j = -range; j <= range; j++){
				if(i == 0 && j == 0)continue;
				dx[count] = i;
				dy[count] = j;
				count++;
			}
		}
	}
	
	public Coordinates findPoints(int x, int y){
		que.clear();
		que.push(x,y);
		Coordinates tmpPoints = new Coordinates();
		tmpPoints.addCord(x, y);
		memo[x][y] = true;
		while(que.size() != 0){
			//if(que.size() % 10 == 0)System.out.println("size:" + que.size());
			int nowx = que.frontx();
			int nowy = que.fronty();
			que.pop();
			//System.out.println(nowx + "," + nowy);
			for(int k = 0; k < dx.length; k++){
				int nx = nowx + dx[k];
				int ny = nowy + dy[k];
				if(nx < 0 || ny < 0 || nx >= maxx+1 || ny >= maxy+1)continue;
				if(memo[nx][ny] == true)continue;
				memo[nx][ny] = true;
//				System.out.println(nx + "," + ny);
				if(state[nx][ny] == true){
					tmpPoints.addCord(nx, ny);
					que.push(nx, ny);
					funcCount++;
					//System.out.println("queue:" + que.getNow());
				}
			}
		}
		return tmpPoints;
	}
	
	
	public int size(){
		return arx.size();
	}
	
	public void clear(){
		now = 0;
	}
	
	public long getCount(){
		return funcCount;
	}
	
}