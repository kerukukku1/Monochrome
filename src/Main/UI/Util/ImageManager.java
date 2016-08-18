package Main.UI.Util;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import Mahito6.Main.Tuple2;
import Mahito6.Main.Main;
import Mahito6.Solver.CrossAlgorithm;
import Mahito6.Solver.Edge;
import Mahito6.Solver.EdgeFinder;
import Mahito6.UI.MainPanel;

public class ImageManager{
	
	private String path;
	private Mat binImage;
	private List<Coordinates> coords;
	private List<BufferedImage> bufImages;
	private List< List<Tuple2<Double, Double>> > vertex;
	
	private boolean modeWaku = false;
	
	public ImageManager(){
		coords = new ArrayList<Coordinates>();
		bufImages = new ArrayList<BufferedImage>();
		vertex = new ArrayList<List <Tuple2<Double, Double> > >();
	}
	
	private void runAdaptiveThreshold(){
		System.out.println(System.getProperty("java.library.path"));
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat src = Highgui.imread(path, 0);
        //枠のときの速度上げ専
        
        if(modeWaku)Imgproc.resize(src, src, new Size(), 0.50, 0.50, Imgproc.INTER_LINEAR);
        
        //微妙?
        //Imgproc.medianBlur(src, src, 1);
        
        if(!modeWaku)Imgproc.GaussianBlur(src, src, new Size(), 0.025);
        binImage = src.clone();
        
        //61 14 太いけど確実param　GAUSSIAN
        //Imgproc.adaptiveThreshold(binaryAdaptive, binaryAdaptive, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 61, 14);
        //Imgproc.adaptiveThreshold(binaryAdaptive, binaryAdaptive, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 71, 21);
        //有力
        if(!modeWaku)Imgproc.adaptiveThreshold(binImage, binImage, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 15, 8);
        //Imgproc.adaptiveThreshold(binaryAdaptive, binaryAdaptive, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 31, 8);
        
        //枠専用
        if(modeWaku){
        	Imgproc.adaptiveThreshold(binImage, binImage, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 21, 14);
        }
        //Imgproc.resize(binaryAdaptive, binaryAdaptive, new Size(), 0.25, 0.25, Imgproc.INTER_LINEAR);
        //Imgproc.resize(binaryAdaptive, binaryAdaptive, new Size(), 4.0, 4.0, Imgproc.INTER_LINEAR);
	}
	
	private String getPath(String head){
        File f = new File(path);
        String filename = f.getName();
        System.out.println(filename);
        String newname = head + filename;
        String newPath = f.getParent() + File.separator + newname;
        return newPath;
	}
	
	public boolean setPath(String path){
		if(isVerify(path)){
			this.path = path;
			return true;
		}else{
			return false;
		}
	}
	
	public void getPieces(){
		long start = 0;
		long end = 0;
		System.out.println("Get Piece");
		System.out.println("runAdaptiveThreshold");
		start = System.nanoTime();
		runAdaptiveThreshold();
		end = System.nanoTime();
		System.out.println((end - start) / 1000000f + "ms");
		
		System.out.println("Divide Images");
		start = System.nanoTime();
		Act act = new Act(binImage, 20, 3000);
		coords = act.divideImages();
		end = System.nanoTime();
		System.out.println((end - start) / 1000000f + "ms");
		
		System.out.println("All Noise Clear");
		start = System.nanoTime();
		clearAllNoise();
		end = System.nanoTime();
		System.out.println((end - start) / 1000000f + "ms");
		
		System.out.println("Find Edge");
		start = System.nanoTime();
		for(int i = 0; i < bufImages.size(); i++){
			pieceSolve(bufImages.get(i), i);
		}
		end = System.nanoTime();
		System.out.println((end - start) / 1000000f + "ms");
		
		outputData();

		System.out.println("end get piece");
		
		MainPanel.vPiece.setVertex(vertex);
	}
	
	public void pieceSolve(BufferedImage image, int index){
		EdgeFinder solver = new EdgeFinder(image);///�G�b�W���o�p�\���o�\
		try {
			solver.edgeFind();
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}
		List<Edge> edges = solver.getResult_edge();
		CrossAlgorithm solver2 = new CrossAlgorithm(edges,image.getWidth(),image.getHeight());///���o�����S�ẴG�b�W�����_�����߂�\���o�\
		solver2.solve();
		List<Tuple2<Double,Double>> ans = solver2.getAnswer();///�S�Ă̒��_���擾
		System.out.println("--------------answer--------------");
		List<String> tmpOut = new ArrayList<String>();
		System.out.println(ans.size());
		tmpOut.add(String.valueOf(ans.size()));
		List<Tuple2<Double, Double> > tmplist = new ArrayList< Tuple2<Double, Double> >();
		for(Tuple2<Double,Double> t : ans){
			System.out.println(t.t1+","+t.t2);
			tmplist.add(t);
		}
		vertex.add(tmplist);
		//BufferedImage result = solver.getResult();
		BufferedImage result2 = solver.getResult_line();
		BufferedImage result3 = solver2.getAnswerImage();
		File line_save = new File(getPath(String.valueOf(index)+"_ans_"));
		File ans_save = new File(getPath(String.valueOf(index)+"_line_"));
		try {
			//ImageIO.write(result, "png", saveFile);
			ImageIO.write(result2, "png", line_save);
			ImageIO.write(result3, "png", ans_save);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void clearAllNoise(){
		int minx,miny,maxx,maxy,mat_h,mat_w,offset;
		for(int i = 0; i < coords.size(); i++){
			Coordinates now = coords.get(i);
			now.clearNoise(200);
			System.out.println(now.getCount());
			minx = now.minx;
			miny = now.miny;
			maxx = now.maxx;
			maxy = now.maxy;
			mat_h = maxy - miny;
			mat_w = maxx - minx;
			if(mat_h < 0 || mat_w < 0)continue;
			offset = 50;
			System.out.println(mat_w + "x" + mat_h);
			BufferedImage bim = new BufferedImage(mat_w+offset, mat_h+offset, BufferedImage.TYPE_INT_RGB);
//			Mat img = new Mat(mat_h+offset, mat_w+offset, CvType.CV_8UC1);
//			img = Mat.zeros(mat_h+offset, mat_w+offset, CvType.CV_8UC1);
			for(int j = 0; j < now.size(); j++){
				int nowx = now.getX(j);
				int nowy = now.getY(j);
				//offset/2は画像を中央に入れるため。四方に黒い空間を少しつくってる。
				int nx = nowx - minx + offset/2;
				int ny = nowy - miny + offset/2;
				//img.put(ny, nx, WHITE);
				bim.setRGB(nx, ny, 0xff000000 | 255 <<16 | 255 <<8 | 255);
			}
			bufImages.add(bim);
			File saves = new File(getPath(String.valueOf(i)+"_"));
			try {
				ImageIO.write(bim, "png", saves);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Noise cleared");
	}

    public static BufferedImage MatToBufferedImageBGR(Mat mat) {
        int dataSize = mat.cols() * mat.rows() * (int) mat.elemSize();
        byte data[] = new byte[dataSize];
        mat.get(0, 0, data);
        int type;
        if (mat.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else {
            type = BufferedImage.TYPE_3BYTE_BGR;
            for (int i = 0; i < dataSize; i += 3) {
                byte blue = data[i + 0];
                data[i + 0] = data[i + 2];
                data[i + 2] = blue;
            }
        }

        BufferedImage img = new BufferedImage(mat.cols(), mat.rows(), type);
        img.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);
        return img;
    }
	
	private boolean isVerify(String path) throws StringIndexOutOfBoundsException{
		if(path.length()==0)return false;
	    int idx = path.lastIndexOf(".");
	    String spieces = path.substring(idx).substring(1);
	    return spieces.equals("png") ||	spieces.equals("jpg") || spieces.equals("jpeg");
	}
	
	private void outputData(){
		System.out.println(vertex.size());
		for(int i = 0; i < vertex.size(); i++){
			List< Tuple2<Double, Double> > now = vertex.get(i);
			System.out.println(now.size());
			for(int j = 0; j < now.size(); j++){
				System.out.println(now.get(j).t1 + " " + now.get(j).t2);
			}
		}
	}
}