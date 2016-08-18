package Main.UI.Util;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.opencv.core.Mat;

public class Act{
	public Mat earth;
	public Mat[] divides;
	public List<Coordinates> coords;
	private int maxX, maxY;
	public boolean[][] memo;
	public boolean[][] state;
	private int[] dx;
	private int[] dy;
	private BufferedImage buf = null;
	private int range;
	private int threshold;
	private JQueue que;
	//row = x, col = y
	//any[x][y]
	public Act(Mat earth, int range, int threshold){
		this.earth = earth;
		buf = ImageManager.MatToBufferedImageBGR(earth);
		coords = new ArrayList<Coordinates>();
		maxX = earth.cols();
		maxY = earth.rows();
		this.range = range;
		this.threshold = threshold;
		memo = new boolean[maxX][maxY];
		makeDXDY(range);
		que = new JQueue(10000000);
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
		while(que.size() != 0){
			//if(que.size() % 10 == 0)System.out.println("size:" + que.size());
			int nowx = que.frontx();
			int nowy = que.fronty();
			que.pop();
			//System.out.println(nowx + "," + nowy);
			for(int k = 0; k < dx.length; k++){
				int nx = nowx + dx[k];
				int ny = nowy + dy[k];
				if(nx < 0 || ny < 0 || nx >= maxX || ny >= maxY)continue;
				if(memo[nx][ny] == true)continue;
				memo[nx][ny] = true;
//				System.out.println(nx + "," + ny);
				if(rgbConverter(buf.getRGB(nx, ny)) == 255){
					tmpPoints.addCord(nx, ny);
					que.push(nx, ny);
					//System.out.println("queque:" + que.getNow());
				}
			}
		}
		return tmpPoints;
	}
	
    public static int rgbConverter(int c){
        return c>>8&0xff;
    }
	
	public List<Coordinates> divideImages(){
		int offset = 100; //スキャン端の大きな影を省く為のオフセット
		System.out.println(maxX + "," + maxY);
		int rgb;
		for(int i = offset; i < maxY-offset; i++){
			for(int j = offset; j < maxX-offset; j++){
				//System.out.println(j + "," + i);
				if(memo[j][i] == true)continue;
				memo[j][i] = true;
				rgb = rgbConverter(buf.getRGB(j, i));
				//System.out.print(rgb);
				if(rgb == 0)continue;
				coords.add(findPoints(j, i));
			}
			//System.out.println();
		}
		
		for(int i = 0; i < coords.size(); i++){
			if(coords.get(i).size() < threshold){
				coords.remove(i);
				i--;
			}
		}

//		for(int i = 0; i < coords.size(); i++){
//			System.out.println(coords.get(i).size());
//		}
		
		return coords;
	}
	
}