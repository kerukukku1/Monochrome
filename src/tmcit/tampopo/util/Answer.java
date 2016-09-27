package tmcit.tampopo.util;

import java.util.ArrayList;
import java.util.List;

import tmcit.tampopo.util.Piece.PieceBuilder;

public class Answer {
	
	public List<Piece> frames,pieces;
	
	public Answer(List<Piece> frames,List<Piece> pieces){
		this.frames = frames;
		this.pieces = pieces;
	}
	
	public static Answer makeDebugAnswer(){
		List<Piece> rf = new ArrayList<Piece>();
		List<Piece> rp = new ArrayList<Piece>();
		Piece testP = new PieceBuilder()
						.addPoint(100, 100)
						.addPoint(200, 100)
						.addPoint(150, 200)
						.build();
		rf.add(testP);
		return new Answer(rf, rp);
	}

}
