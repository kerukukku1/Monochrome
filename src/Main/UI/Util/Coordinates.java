package Main.UI.Util;

import java.util.ArrayList;

import Mahito6.Main.Constants;

public class Coordinates{
	public ArrayList<Integer> arx;
	public ArrayList<Integer> ary;
	public ArrayList<Double> ansx;
	public ArrayList<Double> ansy;
	public int now, maxx, maxy, minx, miny;
	private long funcCount = 0;
	public Coordinates(){
		arx = new ArrayList<Integer>();
		ary = new ArrayList<Integer>();
		now = 0;
		maxx = 0;
		maxy = 0;
		minx = 100000;
		miny = 100000;
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