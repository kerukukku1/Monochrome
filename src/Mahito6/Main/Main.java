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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;

import Mahito6.Solver.CrossAlgorithm;
import Mahito6.Solver.Edge;
import Mahito6.Solver.EdgeFinder;
import Mahito6.UI.MainFrame;
import Mahito6.UI.PieceListView;
import Mahito6.UI.VisualizeFrame;

public class Main {
	private static MainFrame mainFrame;
	
	public Main(){
		mainFrame = new MainFrame(Constants.uiTitle);
	}
	
	public static void main(String[] args){
		new Main();
//		debug();
	}
	
	private static File debugImage = new File("/Users/fujinomahito/Dropbox/PROCON2016/m-pc/test.JPG");
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
		BufferedImage ans1 = finder.getResult_line();
		try {
			ImageIO.write(ans1, "png", new File("ans.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		CrossAlgorithm crossAlgorithm = new CrossAlgorithm(finder.getResult_edge(), tarImage.getWidth(), tarImage.getHeight());
		crossAlgorithm.solve();
		if(crossAlgorithm.isErrorCross()){
			System.out.println("cross error!!");
		}
		
		List<Tuple2<Double,Double>> ans2 = crossAlgorithm.getAnswer();///�S�Ă̒��_���擾
		List<String> tmpOut = new ArrayList<String>();
		System.out.println(ans2.size());
		tmpOut.add(String.valueOf(ans2.size()));
		List<Tuple2<Double, Double> > tmplist = new ArrayList< Tuple2<Double, Double> >();
		for(Tuple2<Double,Double> t : ans2){
			System.out.println(t.t1+","+t.t2);
			tmplist.add(t);
		}
		List< List<Tuple2<Double, Double>> > vertex = new ArrayList<List<Tuple2<Double, Double>>>();
		vertex.add(tmplist);
		//VisualizeFrame vis = new VisualizeFrame(vertex, null);
		PieceListView view = new PieceListView(vertex, null);
	}
}
