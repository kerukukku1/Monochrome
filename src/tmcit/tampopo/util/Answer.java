package tmcit.tampopo.util;

import java.util.ArrayList;
import java.util.List;

import tmcit.tampopo.geometry.util.Piece;
import tmcit.tampopo.geometry.util.Piece.PieceBuilder;

public class Answer {
	
	public List<Piece> frames,pieces;
	
	public Answer(List<Piece> frames,List<Piece> pieces){
		this.frames = frames;
		this.pieces = pieces;
	}
	
	public Answer getCopy(){
		List<Piece> newFrames = new ArrayList<Piece>();
		List<Piece> newPieces = new ArrayList<Piece>();
		for(Piece frame : this.frames){
			newFrames.add(frame.getCopy());
		}
		for(Piece piece : this.pieces){
			newPieces.add(piece.getCopy());
		}
		return new Answer(newFrames, newPieces);
	}
	
	public boolean mergeAnotherAnswer(Answer source){
		System.out.println("------Merge Tool------");
		System.out.println("TO    :"+frames.size()+"frames , "+pieces.size()+"pieces.");
		
		boolean[] isUse = new boolean[50];
		for(int i = 0;i < 50;i++)isUse[i] = false;
		for(Piece piece:pieces){
			isUse[piece.getID()] = true;
			System.out.print(piece.getID()+" ");
		}
		System.out.println("");
		System.out.println("SOURCE:"+source.frames.size()+"frames , "+source.pieces.size()+"pieces.");
		for(Piece piece:source.pieces){
			if(isUse[piece.getID()]){
				System.out.println("ID:"+piece.getID()+" OUT!!");
				return false;///使われとる！
			}
			System.out.print(piece.getID()+" ");
		}
		System.out.println("");
		for(Piece piece : source.pieces){
			pieces.add(piece.getCopy());
		}
		return true;
	}
	
//	public static Answer makeDebugAnswer(){
//		List<Piece> rf = new ArrayList<Piece>();
//		List<Piece> rp = new ArrayList<Piece>();
//		Piece testP = new PieceBuilder()
//						.addPoint(100, 100)
//						.addPoint(200, 100)
//						.addPoint(150, 200)
//						.build();
//		rf.add(testP);
//		return new Answer(rf, rp);
//	}

}
