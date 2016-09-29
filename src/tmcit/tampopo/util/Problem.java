package tmcit.tampopo.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import tmcit.tampopo.geometry.util.Piece;

public class Problem {
	
	public List<Piece> frames;
	public List<Piece> pieces;
	
	public BufferedImage piece2Image;///クソでかの可能性
	public List<Piece> realPieces;///現画像上での座標が格納される
	
	public Problem(List<Piece> pieces,List<Piece> frames){
		this.frames = frames;
		this.pieces = pieces;
	}
	
	public void setPiece2Image(File file){
		try {
			this.piece2Image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void setRealPieces(List<Piece> realPieces){
		this.realPieces = realPieces;
	}
	
	public Answer getEmptyAnswer(){
		List<Piece> newFrames = new ArrayList<Piece>();
		for(Piece frame : frames)newFrames.add(frame.getCopy());
		return new Answer(newFrames, new ArrayList<Piece>());
	}
	
	public List<Answer> convertAllAnswerList(){
		List<Answer> ret = new ArrayList<Answer>();
		for(Piece frame : frames){
			List<Piece> tmPieces = new ArrayList<Piece>();
			tmPieces.add(frame.getCopy());
			ret.add(new Answer(tmPieces,new ArrayList<Piece>()));
		}
		for(Piece piece : pieces){
			List<Piece> tmPieces = new ArrayList<Piece>();
			tmPieces.add(piece.getCopy());
			ret.add(new Answer(new ArrayList<Piece>(), tmPieces));
		}
		return ret;
	}

}
