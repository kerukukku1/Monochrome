package Mahito6.Main;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import Main.UI.Util.Coordinates;
import Main.UI.Util.ImageManager;

public class Problem {
	public Mat source;
	public Mat binImage, binImage2;
	public BufferedImage bufBinImage;
	public BufferedImage[] div;
	private List<Coordinates> coords;
	public Problem(Mat source){
		this.source = source;
		div = new BufferedImage[100];
		for(int i = 0; i < 100; i++){
			div[i] = null;
		}
	}
	
	public void runAdaptiveThreshold(){
        //枠のときの速度上げ専
        //if(Constants.modeWaku)Imgproc.resize(src, src, new Size(), 0.50, 0.50, Imgproc.INTER_LINEAR);
        //微妙?
        //Imgproc.medianBlur(src, src, 1);
        //if(!modeWaku)Imgproc.GaussianBlur(src, src, new Size(), 0.025);
        binImage = source.clone();
        binImage2 = source.clone();
        //61 14 太いけど確実param　GAUSSIAN
        //Imgproc.adaptiveThreshold(binaryAdaptive, binaryAdaptive, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 61, 14);
        //Imgproc.adaptiveThreshold(binaryAdaptive, binaryAdaptive, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 71, 21);
        //有力
        if(!Constants.modeWaku){
//        	Imgproc.adaptiveThreshold(binImage, binImage, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 61, 14);
//        	Imgproc.adaptiveThreshold(binImage, binImage, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 17, 8);
        	Imgproc.adaptiveThreshold(binImage, binImage, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 15, 8);
        	//nico nico
        	Imgproc.adaptiveThreshold(binImage2, binImage2, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 61, 14);	
//            Imgproc.adaptiveThreshold(binImage, binImage, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 31, 8);
        }
        Mat dst = source.clone();
        Core.bitwise_and(binImage, binImage2, dst);
        Highgui.imwrite("and_image.png", dst);
        //枠専用
        if(Constants.modeWaku){
        	Imgproc.adaptiveThreshold(binImage, binImage, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 21, 14);
        }
        bufBinImage = ImageManager.MatToBufferedImageBGR(dst);
        //Imgproc.resize(binaryAdaptive, binaryAdaptive, new Size(), 0.25, 0.25, Imgproc.INTER_LINEAR);
        //Imgproc.resize(binaryAdaptive, binaryAdaptive, new Size(), 4.0, 4.0, Imgproc.INTER_LINEAR);
        //黄色いプロット確認
//        if(Constants.isOutputDebugOval)confirm = MatToBufferedImageBGR(src);
	}
	
	public void setCoordinates(List<Coordinates> coords){
		this.coords = coords;
	}

	public BufferedImage getBinaryBufferedImage(){
		return bufBinImage;
	}
}