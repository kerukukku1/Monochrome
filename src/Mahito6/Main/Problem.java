package Mahito6.Main;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
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
	public List<BufferedImage> div;
	private List<Coordinates> coords;
	public Problem(Mat source){
		this.source = source;
		div = new ArrayList<BufferedImage>();
	}
	
	public void runAdaptiveThreshold(){
        binImage = source.clone();
        binImage2 = source.clone();
        if(!Constants.modeWaku){
//        	Imgproc.adaptiveThreshold(binImage, binImage, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 61, 14);
//        	Imgproc.adaptiveThreshold(binImage, binImage, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 17, 8);
        	Imgproc.adaptiveThreshold(binImage, binImage, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 15, 8);
        	//nico nico
        	Imgproc.adaptiveThreshold(binImage2, binImage2, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 61, 14);	
//          Imgproc.adaptiveThreshold(binImage, binImage, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 31, 8);
        }
        Core.bitwise_and(binImage, binImage2, binImage);
        //枠専用
        if(Constants.modeWaku){
        	Imgproc.adaptiveThreshold(binImage, binImage, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 21, 14);
        }
        bufBinImage = ImageManager.MatToBufferedImageBGR(binImage);
        
//        黄色プロット確認
//        if(Constants.isOutputDebugOval)confirm = MatToBufferedImageBGR(src);
	}
	
	public void setCoordinates(List<Coordinates> coords){
		this.coords = coords;
	}

	public BufferedImage getBinaryBufferedImage(){
		return bufBinImage;
	}
}