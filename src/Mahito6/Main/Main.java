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
import javax.swing.JFrame;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import Mahito6.Solver.BFS;
import Mahito6.Solver.CrossAlgorithm;
import Mahito6.Solver.Edge;
import Mahito6.Solver.EdgeFinder;
import Mahito6.UI.MainFrame;
import Mahito6.UI.PieceListView;
import Mahito6.UI.HandlSummaryPanel;
import Main.UI.Util.FolderManager;

public class Main {
	private static MainFrame mainFrame;
	public static PieceListView pieceView;
	public Main(){
		//mainFrame = new MainFrame(Constants.uiTitle);
		//queue先に確保
		System.out.println(System.getProperty("java.library.path"));
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		BFS.initQueue();

		new ProblemManager();
		new FolderManager().buildDirectory();
		pieceView = new PieceListView();
	}
	
	public static void main(String[] args){
//		new Main();
//		JFrame debug = new JFrame("debug");
//		debug.add(pieceView);
//		debug.pack();
//		debug.setVisible(true);
//		debug.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		debug();
		outputImages();
	}
	
	private static File debugImage = new File("/Users/fujinomahito/Desktop/1試合目/Piece1_300.png");
	public static void debug(){///EdgeFinderのデバッグ用
		BufferedImage tarImage = null;
		new ProblemManager();
		try {
			tarImage = ImageIO.read(debugImage);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		EdgeFinder finder = new EdgeFinder(tarImage,false, ProblemManager.getConstants());
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
//		List< List<Tuple2<Double, Double>> > vertex = new ArrayList<List<Tuple2<Double, Double>>>();
//		vertex.add(tmplist);
//		//VisualizeFrame vis = new VisualizeFrame(vertex, null);
//		PieceListView view = new PieceListView(vertex, null, null);
	}
	
	private static String testImagePath = "/Users/fujinomahito/Desktop/1試合目/Piece1_300.png";
	private static void outputImages(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat source = Highgui.imread(testImagePath, 0);
		for(int i = 0; i < 3; i++){
			Imgproc.resize(source, source, new Size(), 0.50, 0.50, Imgproc.INTER_LINEAR);
			Imgproc.resize(source, source, new Size(), 2.0, 2.0, Imgproc.INTER_LINEAR);
			Imgproc.resize(source, source, new Size(), 2.0, 2.0, Imgproc.INTER_LINEAR);
			Imgproc.resize(source, source, new Size(), 0.50, 0.50, Imgproc.INTER_LINEAR);
		}
        double span = 0.5;
        for(int i = 0; i < 10; i++){
        	int blockSize = 21 + (i*2);
        	double c = blockSize - 25.0;
        	while(true){
        		if((int)c > blockSize + 35)break;
        		Mat binImage = source.clone();
            	Imgproc.adaptiveThreshold(binImage, binImage, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, blockSize, c);
            	String head = String.valueOf(blockSize) + "_" + String.valueOf(c);
            	Highgui.imwrite(getPath(head), binImage);
            	c+=span;
        		System.out.println("c:" + c + ", block:" + blockSize);
        	}
        }
	}
	
	private static String getPath(String head){
        File f = new File(testImagePath);
        String filename = f.getName();
        System.out.println(filename);
        String newname = head + filename;
        String newPath = f.getParent() + File.separator + newname;
        return newPath;
	}
}
