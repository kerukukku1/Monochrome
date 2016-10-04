package Main.UI.Util;

import java.util.ArrayList;

import Mahito6.Main.Constants;

public class Coordinates{
	public ArrayList<Integer> arx;
	public ArrayList<Integer> ary;
	public int maxx, maxy, minx, miny;
	private long funcCount;
	private boolean error = false;
	private boolean caution = false;
	
	public Coordinates(){
		arx = new ArrayList<Integer>();
		ary = new ArrayList<Integer>();
		maxx = 0;
		maxy = 0;
		minx = 100000;
		miny = 100000;
		funcCount = 0;
	}
	
	public Coordinates(ArrayList<Integer> arx, ArrayList<Integer> ary){
		this.arx = arx;
		this.ary = ary;
		this.calc();
	}
	
	public void clear(){
		arx = new ArrayList<Integer>();
		ary = new ArrayList<Integer>();
		maxx = 0;
		maxy = 0;
		minx = 100000;
		miny = 100000;
		funcCount = 0;
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


	public long getCount(){
		return funcCount;
	}
	
	public void setError(boolean error){
		this.error = error;
	}
	
	public void setCaution(boolean caution){
		this.caution  = caution;
	}
	
	public boolean isCaution(){
		return this.caution;
	}
	
	public boolean isError(){
		return error;
	}
	
	public int getVisX(int index){
		return arx.get(index) - minx;
	}
	
	public int getVisY(int index){
		return ary.get(index) - miny;
	}
}