package Mahito6.Main;

import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import Mahito6.Solver.Edge;
import Main.UI.Util.Coordinates;
import Main.UI.Util.ImageManager;

public class Problem {
	public Mat source;
	public List<BufferedImage> div;
	private List<Coordinates> coords;
	private List<List<Edge>> allEdges;
	private List<List<Tuple2<Double, Double>>> vertex;
	private boolean modeWaku;
	public Problem(Mat source, boolean modeWaku){
		this.source = source;
		this.modeWaku = modeWaku;
		div = new ArrayList<BufferedImage>();
	}
	
	public int getSize(){
		return allEdges.size();
	}
	
	public void setEdges(int index, List<Edge> edges){
		allEdges.set(index, edges);
	}
	
	public void setVertex(int index, List<Tuple2<Double, Double>> v){
		vertex.set(index, v);
	}
	
	public void setCoordinates(List<Coordinates> coords){
		this.coords = coords;
	}
	
	public void setBufferedImages(List<BufferedImage> images){
		this.div = new ArrayList<BufferedImage>(images);
	}
	
	public void setData(List<List<Edge>> allEdges, List<List<Tuple2<Double, Double>>> vertex, List<Coordinates> coords){
		this.allEdges = new ArrayList<List<Edge>>(allEdges);
		this.vertex = new ArrayList<List<Tuple2<Double, Double>>>(vertex);
		this.coords = new ArrayList<Coordinates>(coords);
	}
	
	public List<List<Edge>> getAllEdges(){
		return allEdges;
	}
	
	public List<Coordinates> getCoords(){
		return coords;
	}
	
	public List<List<Tuple2<Double, Double>>> getVertex(){
		return vertex;
	}
	
	public List<Tuple2<Double, Double>> getVertex(int index){
		return vertex.get(index);
	}
	
	public Mat getBinaryMatImage(){
		return source;
	}
	
	public Coordinates getCoord(int index){
		return coords.get(index);
	}
	
	public List<Edge> getEdges(int index){
		return allEdges.get(index);
	}
	
	public BufferedImage getImage(int index){
		return div.get(index);
	}
}