package Main.UI.Util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import Mahito6.Main.Constants;
import Mahito6.Main.Main;
import Mahito6.Main.Problem;
import Mahito6.Main.ProblemManager;
import Mahito6.Main.Tuple2;
import Mahito6.Solver.BFS;
import Mahito6.Solver.Edge;
import Mahito6.Thread.SolverThreadingAgent;

public class ImageManager{

	private String path;
	private List<Coordinates> coords;
	private List<BufferedImage> bufImages;
	private List< List<Tuple2<Double, Double>> > vertex;
	private BufferedImage confirm, bufBinImage, originalImage;
	public static List<String> data;
	public List<List<Edge>> allEdges;
	public Problem problem;
	private Mat source;
	public static int scan_stringOffset = 59;
//	private static int piece_load_index = 0;

	public ImageManager(){
		coords = new ArrayList<Coordinates>();
		bufImages = new ArrayList<BufferedImage>();
		vertex = new ArrayList<List <Tuple2<Double, Double> > >();
		data = new ArrayList<String>();
		allEdges = new ArrayList<List<Edge>>();
	}

	public void clear(){
		coords.clear();
		vertex.clear();
		data.clear();
		allEdges.clear();
		for(;bufImages.size() != 0;){
			Object obj = bufImages.remove(0);
			obj = null;
		}
		System.gc();
	}

	public void clearAll(){
		clear();
		problem = null;
		source = null;
//		ImageManager.piece_load_index = 0;
	}

	public void runAdaptiveThreshold(){
		this.source = Highgui.imread(path);
		if(problem.getType() == Status.Type.FRAME)source = new Mat(source, new Rect(10, 10, source.cols()-30, source.cols()-30));
		Mat _source = source.clone();
		if(!Constants.modeWaku)Imgproc.erode(_source, _source, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(4,4)));
		Imgproc.cvtColor(_source, _source, Imgproc.COLOR_RGB2GRAY);
		originalImage = this.MatToBufferedImageBGR(source);
        //枠のときの速度上げ専
        //if(Constants.modeWaku)Imgproc.resize(src, src, new Size(), 0.50, 0.50, Imgproc.INTER_LINEAR);
        //微妙?
        //Imgproc.medianBlur(src, src, 1);
        //if(!modeWaku)Imgproc.GaussianBlur(src, src, new Size(), 0.025);
		if(!Constants.modeWaku){
			for(int i = 0; i < Constants.Nameraka; i++){
				Imgproc.resize(source, source, new Size(), 0.50, 0.50, Imgproc.INTER_LINEAR);
				Imgproc.resize(source, source, new Size(), 2.0, 2.0, Imgproc.INTER_LINEAR);
				Imgproc.resize(source, source, new Size(), 2.0, 2.0, Imgproc.INTER_LINEAR);
				Imgproc.resize(source, source, new Size(), 0.50, 0.50, Imgproc.INTER_LINEAR);
			}
		}

//		if(problem.getType() == Status.Type.FRAME)source = new Mat(source, new Rect(10, 10, source.cols()-100, source.rows()-100));
		Mat binImage = _source.clone();
//		Mat binImage2 = _source.clone();
        //61 14 太いけど確実param　GAUSSIAN
        //Imgproc.adaptiveThreshold(binaryAdaptive, binaryAdaptive, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 61, 14);
        //Imgproc.adaptiveThreshold(binaryAdaptive, binaryAdaptive, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 71, 21);

		if(binImage.rows() + binImage.cols() < 10000){
			ProblemManager.dpi = 300;
		}else{
			ProblemManager.dpi = 600;
		}
		System.out.println(ProblemManager.dpi);

        //有力
        if(!Constants.modeWaku){
//        	Imgproc.adaptiveThreshold(binImage, binImage, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 61, 14);
//        	Imgproc.adaptiveThreshold(binImage, binImage, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 17, 8);
        	Imgproc.adaptiveThreshold(binImage, binImage, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 15, 8);
        	//nico nico
//        	Imgproc.adaptiveThreshold(binImage2, binImage2, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 61, 14);
//            Imgproc.adaptiveThreshold(binImage, binImage, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 31, 8);
        }

//andとってゴミ消す
//        Core.bitwise_and(binImage, binImage2, binImage);
//        Highgui.imwrite("and_image.png", dst);

        //枠専用
        if(Constants.modeWaku){
//        	Imgproc.adaptiveThreshold(binImage, binImage, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 21, 14);
        	Imgproc.adaptiveThreshold(binImage, binImage, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 15, 8);
        }

        bufBinImage = ImageManager.MatToBufferedImageBGR(binImage);
        binImage = null;
        //Imgproc.resize(binaryAdaptive, binaryAdaptive, new Size(), 0.25, 0.25, Imgproc.INTER_LINEAR);
        //Imgproc.resize(binaryAdaptive, binaryAdaptive, new Size(), 4.0, 4.0, Imgproc.INTER_LINEAR);
        //黄色いプロット確認
        if(Constants.isOutputDebugOval)confirm = MatToBufferedImageBGR(source);
	}

    private Mat pieceColorDetect(Mat mat) {
        Mat mat1 = new Mat();
        Core.inRange(mat, new Scalar(0, 0, 0), new Scalar(100, 240, 240), mat1);
        return mat1;
    }

    private Mat distTransform(Mat mat) {
        Mat mat1 = new Mat(mat.cols(), mat.rows(), CvType.CV_8UC1);
        Mat mat2 = new Mat();
        Imgproc.distanceTransform(mat, mat2, Imgproc.CV_DIST_L2, 3);
        Core.convertScaleAbs(mat2, mat1);
        Core.normalize(mat1, mat1, 0.0, 255.0, Core.NORM_MINMAX);
        return mat1;
    }

	private void testSalesio(Mat source){
		Mat grayScale = source.clone();

        Mat hsv = new Mat();//HSV変換画像
        Imgproc.cvtColor(source, hsv, Imgproc.COLOR_BGR2HSV);
//        Imgproc.medianBlur(hsv, hsv, 3);
        Mat skin = pieceColorDetect(hsv);
        Mat skinDist = distTransform(skin);

        Mat bin = new Mat();
        Imgproc.threshold(skinDist, bin, 0, 255, Imgproc.THRESH_BINARY_INV
                | Imgproc.THRESH_OTSU);
        Highgui.imwrite("./hsv.png", bin);
        System.exit(0);
		Imgproc.cvtColor(source, grayScale, Imgproc.COLOR_BGR2GRAY);
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat binImage = grayScale.clone();
        Imgproc.adaptiveThreshold(binImage, binImage, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 61, 14);
		Imgproc.findContours(binImage, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
//		Core.polylines(subSource, contours, true, new Scalar(0, 255, 0), 2);
		for(int i = 0; i < contours.size(); i++){
			MatOfPoint contour = contours.get(i);
			MatOfPoint2f approxCurve = new MatOfPoint2f();
		    List<Point> approxPoints = new ArrayList<Point>();
		    Imgproc.approxPolyDP(new MatOfPoint2f(contour.toArray()), approxCurve, 0.0001, true);
		    System.out.println(approxCurve.size());
		    Converters.Mat_to_vector_Point(approxCurve, approxPoints);
		    double area = Imgproc.contourArea(contour);
		    if(area > 1000){
		    	System.out.println("area:" + area);
//				Core.line(source, approxPoints, true, new Scalar(0, 255, 0), 2);
//	            MatOfInt hull = new MatOfInt();
//	            Imgproc.convexHull(contour, hull);

//	            Point data[] = contour.toArray();
//	            int v = -1;
//	            for (int j : hull.toArray()) {
//	                if (v == -1) {
//	                    v = j;
//	                } else {
//	                    Core.line(source, data[j], data[v], new Scalar(0, 255, 0));
//	                    v = j;
//	                }
//	            }
//	            convexityDefects(source, contour,hull);
	            Imgproc.drawContours(binImage, contours, i, new Scalar(255, 0, 0));
		    }
//			Imgproc.approxPolyDP((MatOfPoint2f) contour, approx, 2, true);
		}
		Highgui.imwrite("./result.png", binImage);
		MeasureTimer.end();
		MeasureTimer.call();
		System.exit(0);
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

	public void getPieces(Status.Type type){
		System.out.println("K=" + Constants.dividePixelLookingForDist);
		System.out.println("Get Piece");
		problem = new Problem(type);
		System.out.println("runAdaptiveThreshold");
		MeasureTimer.start();
		runAdaptiveThreshold();
//		this.testSalesio(Highgui.imread(path));
		if(Constants.isOutputDebugOval)try {confirm = ImageIO.read(new File(path));} catch (IOException e1) {e1.printStackTrace();}
		MeasureTimer.end();
		MeasureTimer.call();

		System.out.println("Divide Images");
		MeasureTimer.start();
		Act act = new Act(bufBinImage, Constants.divideImageGarbageThreshold);
		coords = act.divideImages();
		problem.setBufferedImage(originalImage);
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
		MeasureTimer.end();
		MeasureTimer.call();

		System.out.println("end get piece");

		if(Constants.isOutputDebugOval){
			try {
					File yellowP = new File(getPath(String.valueOf(0)+"yellow"));
					Graphics2D g = confirm.createGraphics();
					for(int i = 0; i < vertex.size(); i++){
						List<Tuple2<Double, Double>> v = vertex.get(i);
						Coordinates c = coords.get(i);
						c.calc();
						for(int j = 0; j < v.size(); j++){
							Tuple2<Double, Double> tmp = v.get(j);
							double x = tmp.t1;
							double y = tmp.t2;
							g.setColor(Color.yellow);
							g.drawOval((int)x-2+c.minx-Constants.imagePositionOffset/2, (int)y-2+c.miny-Constants.imagePositionOffset/2, 4, 4);
							System.out.println(x + "," + y);
						}
					}
					ImageIO.write(confirm, "png", yellowP);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		problem.setData(allEdges, vertex, coords);
		if(this.problem.isWaku()){
	        Date date = new Date();
	        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	        String nowtime = sdf1.format(date).toString();
			problem.setPath(path);
//			problem.setPath(path);
		}
//		}else{
//	        Date date = new Date();
//	        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
//	        String nowtime = sdf1.format(date).toString();
//			problem.setPath(ProblemManager.getQuestPath()  + "flip_" + nowtime + problem.getType().toString() + ".png");
//		}
//		problem.setBufferedImages(bufImages);
		ProblemManager.addProblem(problem);

		//Problemにデータを引き継いでいるので無駄なデータは削除。addはこれしないと無理
		this.clear();

		//VisualizeFrame visualizer = new VisualizeFrame(vertex, coords);
		Main.pieceView.launchPiecePanel();
	}

	public void runSolveThread() throws Exception{
		int threadNum = Constants.solveThread;
		SolverThreadingAgent agent = new SolverThreadingAgent(bufImages, threadNum);
		agent.run();
		allEdges = agent.getAllEdges();
		for(int i = 0; i < bufImages.size(); i++){
			vertex.add(agent.getCrossAnswer(i));
			coords.get(i).setError(agent.isError(i));
			coords.get(i).setCaution(agent.isCaution(i));
		}
	}

	public void clearAllNoise(){
//		ImageManager.piece_load_index++;
		int minx,miny,maxx,maxy,mat_h,mat_w;
		Mat flip_image = source.clone();
		Core.flip(flip_image, flip_image, 1);
		if(!this.problem.isWaku()){
	        Date date = new Date();
	        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	        String nowtime = sdf1.format(date).toString();
			Highgui.imwrite(ProblemManager.getQuestPath()  + "flip_" + nowtime + problem.getType().toString() + ".png", flip_image);
			problem.setPath(ProblemManager.getQuestPath()  + "flip_" + nowtime + problem.getType().toString() + ".png");
		}

		//y軸回転
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
//			File saves = new File(getPath(String.valueOf(i)+"_"));
//			try {
//				if(Constants.debugImage)ImageIO.write(bim, "png", saves);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				//e.printStackTrace();
//			}
		}
		if(!Constants.modeWaku){
//			Imgproc.resize(numbering, numbering, new Size(), 0.50, 0.50, Imgproc.INTER_LINEAR);
//			Highgui.imwrite(FolderManager.imagePath + "numbering_" + problem.getType().toString() + ".png", numbering);
//			problem.setNumberingPath(FolderManager.imagePath + "numbering_" + problem.getType().toString() + ".png");
//			if(problem.getType() == Status.Type.PIECE1){
//				new Thread(new Runnable(){
//					@Override
//					public void run(){
//						new NumberingFrame(FolderManager.imagePath + "numbering_" + String.valueOf(piece_load_index) + ".png");
//					}
//				}).start();
//			}
//			numbering = null;
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

	public List< List<Tuple2<Double, Double>> > getVertex(){
		return vertex;
	}

	public List< List<Edge>> getEdges(){
		return allEdges;
	}

	public List<Coordinates> getCoord(){
		return coords;
	}

	public static BufferedImage booleanToBufferedImage(boolean[][] state){
		return null;
	}

	public static BufferedImage rescaleImage(double scale, BufferedImage image){
		int dw = (int)(image.getWidth() * scale);
		int dh = (int)(image.getHeight() * scale);
		BufferedImage thumb = new BufferedImage(dw, dh, BufferedImage.TYPE_INT_BGR);
		thumb.getGraphics().drawImage(image.getScaledInstance(dw, dh, image.SCALE_AREA_AVERAGING), 0, 0, dw, dh, null);
		return thumb;
	}


}