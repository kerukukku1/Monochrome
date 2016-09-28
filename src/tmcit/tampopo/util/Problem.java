package tmcit.tampopo.util;

import java.util.ArrayList;
import java.util.List;

public class Problem {
	
	public List<Piece> frames;
	public List<Piece> pieces;
	
	public Problem(List<Piece> pieces,List<Piece> frames){
		this.frames = frames;
		this.pieces = pieces;
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
