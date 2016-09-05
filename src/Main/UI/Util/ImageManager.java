package Main.UI.Util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import Mahito6.Main.Constants;
import Mahito6.Main.Problem;
import Mahito6.Main.Tuple2;
import Mahito6.Solver.BFS;
import Mahito6.Solver.CrossAlgorithm;
import Mahito6.Solver.Edge;
import Mahito6.Solver.EdgeFinder;
import Mahito6.Thread.SolverThreadingAgent;
import Mahito6.UI.MainPanel;
import Mahito6.UI.PieceListView;
import Mahito6.UI.VisualizeFrame;

public class ImageManager{

	private String path;
	private Mat binImage;
	private List<Coordinates> coords;
	private List<BufferedImage> bufImages;
	private List< List<Tuple2<Double, Double>> > vertex;
	private BufferedImage confirm, bufBinImage;
	public static List<String> data;
	public List<List<Edge>> allEdges;
	public Problem problem;

	public ImageManager(){
		coords = new ArrayList<Coordinates>();
		bufImages = new ArrayList<BufferedImage>();
		vertex = new ArrayList<List <Tuple2<Double, Double> > >();
		data = new ArrayList<String>();
		allEdges = new ArrayList<List<Edge>>();
	}

	private void runAdaptiveThreshold(){
        Mat src = Highgui.imread(path, 0);
        //枠のときの速度上げ専

        //if(Constants.modeWaku)Imgproc.resize(src, src, new Size(), 0.50, 0.50, Imgproc.INTER_LINEAR);

        //微妙?
        //Imgproc.medianBlur(src, src, 1);

        //if(!modeWaku)Imgproc.GaussianBlur(src, src, new Size(), 0.025);
        binImage = src.clone();

        //61 14 太いけど確実param　GAUSSIAN
        //Imgproc.adaptiveThreshold(binaryAdaptive, binaryAdaptive, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 61, 14);
        //Imgproc.adaptiveThreshold(binaryAdaptive, binaryAdaptive, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 71, 21);
        //有力
        if(!Constants.modeWaku){
//        	Imgproc.adaptiveThreshold(binImage, binImage, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 61, 14);
//        	Imgproc.adaptiveThreshold(binImage, binImage, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 17, 8);
        	
//        	Imgproc.adaptiveThreshold(binImage, binImage, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 15, 8);
        	
        	//nico nico
        	Imgproc.adaptiveThreshold(binImage, binImage, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 25, 25.25);
        	
        	
//            Imgproc.adaptiveThreshold(binImage, binImage, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 31, 8);
        }

        //枠専用
        if(Constants.modeWaku){
        	Imgproc.adaptiveThreshold(binImage, binImage, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 21, 14);
        }
        
        
        bufBinImage = ImageManager.MatToBufferedImageBGR(binImage);
        //Imgproc.resize(binaryAdaptive, binaryAdaptive, new Size(), 0.25, 0.25, Imgproc.INTER_LINEAR);
        //Imgproc.resize(binaryAdaptive, binaryAdaptive, new Size(), 4.0, 4.0, Imgproc.INTER_LINEAR);
        //黄色いプロット確認
//        if(Constants.isOutputDebugOval)confirm = MatToBufferedImageBGR(src);
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
		System.out.println("K=" + Constants.dividePixelLookingForDist);
		System.out.println("Get Piece");
		problem = new Problem(Highgui.imread(path, 0));
		System.out.println("runAdaptiveThreshold");
		MeasureTimer.start();
		problem.runAdaptiveThreshold();
		if(Constants.isOutputDebugOval)try {confirm = ImageIO.read(new File(path));} catch (IOException e1) {e1.printStackTrace();}
		//runAdaptiveThreshold();
		MeasureTimer.end();
		MeasureTimer.call();

		System.out.println("Divide Images");
		MeasureTimer.start();
		Act act = new Act(problem.getBinaryBufferedImage(), Constants.divideImageGarbageThreshold);
		coords = act.divideImages();
		MeasureTimer.end();
		MeasureTimer.call();

		System.out.println("All Noise Clear");
		MeasureTimer.start();
		clearAllNoise();
		MeasureTimer.end();
		MeasureTimer.call();

		System.out.println("Find Edge");
		MeasureTimer.start();
		try {
			runSolveThread();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		outputData();
		MeasureTimer.end();
		MeasureTimer.call();
		
		System.out.println("end get piece");
		
		File yellowP = new File(getPath(String.valueOf(0)+"yellow"));
		try {
			if(Constants.isOutputDebugOval)ImageIO.write(confirm, "png", yellowP);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//VisualizeFrame visualizer = new VisualizeFrame(vertex, coords);
		PieceListView view = new PieceListView(this);
	}
	
	public void runSolveThread() throws Exception{
		int threadNum = Constants.solveThread;
		SolverThreadingAgent agent = new SolverThreadingAgent(bufImages, threadNum);
		agent.run();
		allEdges = agent.getAllEdges();
		for(int i = 0; i < bufImages.size(); i++){
			vertex.add(agent.getCrossAnswer(i));
			coords.get(i).setError(agent.isError(i));			
		}
	}

	public void clearAllNoise(){
		int minx,miny,maxx,maxy,mat_h,mat_w,offset;
		for(int i = 0; i < coords.size(); i++){
			Coordinates now = coords.get(i);
			BFS.clearNoise(Constants.clearNoiseThreshold, now);
			//now.clearNoise(Constants.clearNoiseThreshold);
			System.out.println(now.getCount());
			minx = now.minx;
			miny = now.miny;
			maxx = now.maxx;
			maxy = now.maxy;
			mat_h = maxy - miny;
			mat_w = maxx - minx;
			if(mat_h < 0 || mat_w < 0)continue;
			System.out.println(mat_w + "x" + mat_h);
			BufferedImage bim = new BufferedImage(mat_w+Constants.imagePositionOffset, mat_h+Constants.imagePositionOffset, BufferedImage.TYPE_INT_RGB);
//			Mat img = new Mat(mat_h+offset, mat_w+offset, CvType.CV_8UC1);
//			img = Mat.zeros(mat_h+offset, mat_w+offset, CvType.CV_8UC1);
			for(int j = 0, loop = now.size(); j < loop; j++){
				int nowx = now.getX(j);
				int nowy = now.getY(j);
				//offset/2は画像を中央に入れるため。四方に黒い空間を少しつくってる。
				int nx = nowx - minx + Constants.imagePositionOffset/2;
				int ny = nowy - miny + Constants.imagePositionOffset/2;
				//img.put(ny, nx, WHITE);
				bim.setRGB(nx, ny, 0xff000000 | 255 <<16 | 255 <<8 | 255);
			}
			bufImages.add(bim);
			File saves = new File(getPath(String.valueOf(i)+"_"));
			try {
				if(Constants.debugImage)ImageIO.write(bim, "png", saves);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
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
		if(path.length()==0 || path.equals("Illegal Path"))return false;
	    int idx = path.lastIndexOf(".");
	    String spieces = path.substring(idx).substring(1);
	    return spieces.equals("png") ||	spieces.equals("jpg") || spieces.equals("jpeg") || spieces.equals("JPG");
	}

	private void outputData(){
		data.add("0");
		System.out.println(vertex.size());
		data.add(String.valueOf(vertex.size()));
		for(int i = 0; i < vertex.size(); i++){
			List< Tuple2<Double, Double> > now = vertex.get(i);
			System.out.println(now.size());
			data.add(String.valueOf(now.size()));
			for(int j = 0; j < now.size(); j++){
				System.out.println(now.get(j).t1 + " " + now.get(j).t2);
				String x = String.valueOf(now.get(j).t1);
				String y = String.valueOf(now.get(j).t2);
				data.add(x+" "+y);
			}
		}
	}
	
	public List< List<Tuple2<Double, Double>> > getVertex(){
		return vertex;
	}
	
	public List< List<Edge>> getEdges(){
		return allEdges;
	}
	
	public List<Coordinates> getCoord(){
		return coords;
	}
	
	public List<BufferedImage> getImages(){
		return bufImages;
	}
	
}