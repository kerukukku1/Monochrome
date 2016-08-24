/**
 * 
 */
/**
 * @author fujinomahito
 *
 */
package Mahito6.Main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;

import Mahito6.Solver.CrossAlgorithm;
import Mahito6.Solver.Edge;
import Mahito6.Solver.EdgeFinder;
import Mahito6.UI.MainFrame;

public class Main {
	private static MainFrame mainFrame;
	
	public Main(){
		mainFrame = new MainFrame(Constants.uiTitle);
	}
	
	public static void main(String[] args){
//		new Main();
		debug();
	}
	
	private static File debugImage = new File("D:/dropbox/Dropbox/Program/PUROKON2016/t-pc/jikken/31_piece600dpi.JPG");
	public static void debug(){///EdgeFinderのデバッグ用
		BufferedImage tarImage = null;
		try {
			tarImage = ImageIO.read(debugImage);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		EdgeFinder finder = new EdgeFinder(tarImage);
		try {
			finder.edgeFind();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		BufferedImage ans = finder.getResult_line();
		try {
			ImageIO.write(ans, "png", new File("ans.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		CrossAlgorithm crossAlgorithm = new CrossAlgorithm(finder.getResult_edge(), tarImage.getWidth(), tarImage.getHeight());
		crossAlgorithm.solve();
		if(crossAlgorithm.isErrorCross()){
			System.out.println("cross error!!");
		}
	}
}
