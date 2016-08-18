package Main.UI.Util;

import java.awt.Point;

public class JQueue{
	private int[] arrayx;
	private int[] arrayy;
	private int now;
	private int getCount;
	public JQueue(int size){
		arrayx = new int[size];
		arrayy = new int[size];
		now = 0;
		getCount = 0;
	}
	
	public void pop(){
		getCount++;
	}
	
	public void push(int x, int y){
		arrayx[now] = x;
		arrayy[now] = y;
		//System.out.println("push : " + arrayx[now] + ", " + arrayy[now]);
		now++;
	}
	
	public int frontx(){
		return arrayx[getCount];
	}
	
	public int fronty(){
		return arrayy[getCount];
	}
	
	public int size(){
		return now-getCount;
	}
	
	public void clear(){
		getCount = 0;
		now = 0;
	}
	
	public int getNow(){
		return now;
	}
	
}