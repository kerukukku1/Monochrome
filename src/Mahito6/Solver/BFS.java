package Mahito6.Solver;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Mahito6.Main.Constants;
import Main.UI.Util.Coordinates;
import Main.UI.Util.JQueue;

public class BFS{
	
	private static int[] dx;
	private static int[] dy;
	private static JQueue que;
	private static boolean[][] noizeMemo;
	private static boolean[][] noizeState;
	private static int latchRange = 0;
	public BFS(){}
	
	public static void initialize(int maxx, int maxy){
		noizeMemo = new boolean[maxx][maxy];
		noizeState = new boolean[maxx][maxy];
	}
	
	public static void initQueue(){
		que = new JQueue(Constants.queueSize);
	}
	
	public static void makeDXDY(int range){
		if(latchRange == range)return;
		latchRange = range;
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

	public static Coordinates findPointsForDivide(int x, int y, int maxx, int maxy, boolean[][] memo, byte[] rgbs){
		que.clear();
		que.push(x,y);
		BFS.makeDXDY(Constants.dividePixelLookingForDist);
		Coordinates tmpPoints = new Coordinates();
		tmpPoints.addCord(x, y);
		while(que.size() != 0){
			//if(que.size() % 10 == 0)System.out.println("size:" + que.size());
			int nowx = que.frontx();
			int nowy = que.fronty();
			que.pop();
			//System.out.println(nowx + "," + nowy);
			for(int k = 0; k < dx.length; k++){
				int nx = nowx + dx[k];
				int ny = nowy + dy[k];
				if(nx < 0 || ny < 0 || nx >= maxx || ny >= maxy)continue;
				if(memo[nx][ny] == true)continue;
				memo[nx][ny] = true;
//				System.out.println(nx + "," + ny);
				if(rgbs[nx+ny*maxx] == 0)continue;
				tmpPoints.addCord(nx, ny);
				que.push(nx, ny);
				//System.out.println("queque:" + que.getNow());
			}
		}
		return tmpPoints;
	}
	
	public static Coordinates findPointsForCoord(int x, int y, int maxx, int maxy, boolean[][] memo, boolean[][] state){
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
					//System.out.println("queue:" + que.getNow());
				}
			}
		}
		return tmpPoints;
	}
	

	public static void clearNoise(int threshold, Coordinates coord){
		coord.calc();
		int h = coord.maxy+1;
		int w = coord.maxx+1;
		BFS.makeDXDY(1);
		for(int i = 0; i < coord.size(); i++){
			if(coord.getX(i) >= w || coord.getY(i) >= h)System.out.println("Over");
			noizeState[coord.getX(i)][coord.getY(i)] = true;
		}
		ArrayList<Integer> arx2 = new ArrayList<Integer>();
		ArrayList<Integer> ary2 = new ArrayList<Integer>();
		for(int i = 0; i < coord.size(); i++){
			int x = coord.getX(i);
			int y = coord.getY(i);
			if(noizeMemo[x][y])continue;
			noizeMemo[x][y] = true;
			Coordinates tc = BFS.findPointsForCoord(x,y,coord.maxx,coord.maxy,noizeMemo,noizeState);
			if(tc.size() > threshold){
				for(int k = 0; k < tc.size(); k++){
					arx2.add(tc.getX(k));
					ary2.add(tc.getY(k));
				}
			}
		}
		coord.arx = arx2;
		coord.ary = ary2;
		coord.calc();
	}
	
    public static int rgbConverter(int c){
        return c>>8&0xff;
    }
}