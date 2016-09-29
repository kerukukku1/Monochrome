package Mahito6.Main;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import Mahito6.Solver.Edge;
import Main.UI.Util.Coordinates;

public class Problem {
	private List<Coordinates> coords;
	private List<List<Edge>> allEdges;
	private List<List<Tuple2<Double, Double>>> vertex;
	private BufferedImage binImage;
	private String image_path;
	private int type;
	private boolean modeWaku;
	public Problem(boolean modeWaku){
		this.modeWaku = modeWaku;
	}

	public boolean isWaku(){
		return modeWaku;
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

	public Coordinates getCoord(int index){
		return coords.get(index);
	}

	public List<Edge> getEdges(int index){
		return allEdges.get(index);
	}

	public BufferedImage getImage(int index){
		Coordinates c = coords.get(index);
		c.calc();
		int mat_h = c.maxy - c.miny;
		int mat_w = c.maxx - c.minx;
		BufferedImage bim = new BufferedImage(mat_w+Constants.imagePositionOffset, mat_h+Constants.imagePositionOffset, BufferedImage.TYPE_INT_RGB);
//		Mat img = new Mat(mat_h+offset, mat_w+offset, CvType.CV_8UC1);
//		img = Mat.zeros(mat_h+offset, mat_w+offset, CvType.CV_8UC1);
		for(int j = 0, loop = c.size(); j < loop; j++){
			bim.setRGB(c.getVisX(j) + Constants.imagePositionOffset/2, c.getVisY(j) + Constants.imagePositionOffset/2, 0xff000000 | 255 <<16 | 255 <<8 | 255);
		}
		return bim;
	}

	public void setBufferedImage(BufferedImage bufBinImage) {
		// TODO Auto-generated method stub
		this.binImage = bufBinImage;
	}

	public BufferedImage getSubImage(int x, int y, int range){
		return binImage.getSubimage(Math.max(0, x-range), Math.max(0, y-range), range*2, range*2);
	}

	public BufferedImage getGrayImage(int index){
		Coordinates c = coords.get(index);
		return binImage.getSubimage(c.minx, c.miny, c.maxx - c.minx, c.maxy - c.miny);
	}

	public BufferedImage rescaleImage(double scale, BufferedImage image){
		int dw = (int)(image.getWidth() * scale);
		int dh = (int)(image.getHeight() * scale);
		BufferedImage thumb = new BufferedImage(dw, dh, BufferedImage.TYPE_INT_BGR);
		thumb.getGraphics().drawImage(image.getScaledInstance(dw, dh, image.SCALE_AREA_AVERAGING), 0, 0, dw, dh, null);
		return thumb;
	}

	public void setType(int type) {
		// TODO Auto-generated method stub
		this.type = type;
	}
	
	public int getType(){
		return type;
	}
	
	public void setPath(String path){
		this.image_path = path;
	}
	
	public String getPath(){
		return image_path;
	}
}
