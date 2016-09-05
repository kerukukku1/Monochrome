package Main.UI.Util;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;

import Mahito6.Main.Constants;
import Mahito6.Solver.BFS;


public class Act{
	public Mat[] divides;
	public List<Coordinates> coords;
	private int maxX, maxY;
	public boolean[][] memo;
	public boolean[][] state;
	private BufferedImage buf = null;
	private int threshold;
	//row = x, col = y
	//any[x][y]
	public Act(BufferedImage buf, int threshold){
		this.buf = buf;
		coords = new ArrayList<Coordinates>();
		maxX = buf.getWidth();
		maxY = buf.getHeight();
		BFS.initialize(maxX, maxY);
		this.threshold = threshold;
		memo = new boolean[maxX][maxY];
	}

    public static int rgbConverter(int c){
        return c>>8&0xff;
    }

	public List<Coordinates> divideImages(){
		System.out.println(maxX + "," + maxY);
		int rgb;
		Coordinates c = new Coordinates();
		for(int i = Constants.divideImageOffset; i < maxY-Constants.divideImageOffset; i++){
			for(int j = Constants.divideImageOffset; j < maxX-Constants.divideImageOffset; j++){
				//System.out.println(j + "," + i);
				if(memo[j][i] == true)continue;
				memo[j][i] = true;
				rgb = rgbConverter(buf.getRGB(j, i));
				//System.out.print(rgb);
				if(rgb == 0)continue;
				c.clear();
				c = BFS.findPointsForDivide(j, i, maxX, maxY, memo, buf);
				if(c.size() < threshold)continue;
				coords.add(new Coordinates(c.arx, c.ary));
			}
			//System.out.println();
		}
//
//		for(int i = 0; i < coords.size(); i++){
//			if(coords.get(i).size() < threshold){
//				coords.remove(i);
//				i--;
//			}
//		}
		return coords;
	}

}