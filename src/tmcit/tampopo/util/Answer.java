package tmcit.tampopo.util;

import java.util.ArrayList;
import java.util.List;

import tmcit.tampopo.edgeSolver.solver.util.ConvertToGrid;
import tmcit.tampopo.geometry.util.Piece;
import tmcit.tampopo.geometry.util.Piece.PieceBuilder;
import tmcit.tampopo.geometry.util.Point;

public class Answer {

	public List<Piece> frames,pieces;
	public double score = 0.0;
	public double area = 0.0;
	public int targetFrameIndex = 0;
	public int targetVertexIndex = 0;

	public Answer(List<Piece> frames,List<Piece> pieces){
		this.frames = frames;
		this.pieces = pieces;
	}
	public Answer(List<Piece> frames,List<Piece> pieces,double score,int targetFrameIndex,int targetVertexIndex){
		this.frames = frames;
		this.pieces = pieces;
		this.score = score;
		this.targetFrameIndex = targetFrameIndex;
		this.targetVertexIndex = targetVertexIndex;
	}
	public void setScore(double score){
		this.score = score;
	}
	public void setTargetFrameIndex(int targetFrameIndex){
		this.targetFrameIndex = targetFrameIndex;
	}
	public void setTargetVertexIndex(int targetVertexIndex){
		this.targetVertexIndex = targetVertexIndex;
	}
	public double getScore(){
		return this.score;
	}
	public void calcArea(){
		if(area != 0.0)return;
		for(Piece piece : pieces){
			area += piece.getPolygonArea();
		}
	}
	public double getArea(){
		if(area == 0.0)calcArea();
		return area;
	}

	public Answer convertToGrid(){
		List<Piece> gridPieces = new ArrayList<Piece>();
		List<Piece> gridFrames = new ArrayList<Piece>();

		for(Piece piece : pieces){
			tmcit.tampopo.edgeSolver.solver.util.Piece gp = new tmcit.tampopo.edgeSolver.solver.util.Piece();
			int n = piece.getPointSize();
			for(int i = 0;i < n;i++){
				double x = piece.getPoint(i).x;
				double y = piece.getPoint(i).y;
				tmcit.tampopo.edgeSolver.solver.util.Point point = new tmcit.tampopo.edgeSolver.solver.util.Point(x,y);
				gp.addVertex(point);
			}
			ConvertToGrid gridConverter = new ConvertToGrid(gp,5.0);
			gp = gridConverter.getGridPiece();
			PieceBuilder pieceBuilder = new PieceBuilder();
			pieceBuilder.setID(piece.getID());
			pieceBuilder.setReverse(false);
			for(int i = 0;i < n;i++){
				tmcit.tampopo.edgeSolver.solver.util.Point point = gp.getVertex(i);
				double x = point.x;
				double y = point.y;
				pieceBuilder.addPoint(x, y);
			}
			try {
				gridPieces.add(pieceBuilder.build());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for(Piece frame : frames){
			tmcit.tampopo.edgeSolver.solver.util.Piece gp = new tmcit.tampopo.edgeSolver.solver.util.Piece();
			int n = frame.getPointSize();
			for(int i = 0;i < n;i++){
				double x = frame.getPoint(i).x;
				double y = frame.getPoint(i).y;
				tmcit.tampopo.edgeSolver.solver.util.Point point = new tmcit.tampopo.edgeSolver.solver.util.Point(x,y);
				gp.addVertex(point);
			}
			ConvertToGrid gridConverter = new ConvertToGrid(gp,5.0);
			gp = gridConverter.getGridPiece();
			PieceBuilder pieceBuilder = new PieceBuilder();
			pieceBuilder.setID(frame.getID());
			pieceBuilder.setReverse(false);
			for(int i = 0;i < n;i++){
				tmcit.tampopo.edgeSolver.solver.util.Point point = gp.getVertex(i);
				double x = point.x;
				double y = point.y;
				pieceBuilder.addPoint(x, y);
			}
			try {
				gridFrames.add(pieceBuilder.build());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new Answer(gridPieces,gridFrames);

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
		return new Answer(newFrames, newPieces,score,targetFrameIndex,targetVertexIndex);
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

	public Answer getReversedAnswer() throws Exception{
		///左右反転
		double minx =  100000;
		double maxx = 0;
		for(Piece frame : frames){
			for(Point point : frame.getPoints()){
				minx = Math.min(minx, point.x);
				maxx = Math.max(maxx, point.x);
			}
		}
		List<Piece> revFrames = new ArrayList<Piece>();
		List<Piece> revPieces = new ArrayList<Piece>();
		for(Piece frame : frames){
			PieceBuilder pieceBuilder = new PieceBuilder();
			pieceBuilder.setID(frame.getID());
			pieceBuilder.setReverse(frame.isReverse());
			for(Point point : frame.getPoints()){
				double x = maxx-(point.x - minx);
				double y = point.y;
				pieceBuilder.addPoint(x, y);
			}
			revFrames.add(pieceBuilder.build());
		}
		for(Piece piece : pieces){
			PieceBuilder pieceBuilder = new PieceBuilder();
			pieceBuilder.setID(piece.getID());
			pieceBuilder.setReverse(piece.isReverse());
			for(Point point : piece.getPoints()){
				double x = maxx-(point.x - minx);
				double y = point.y;
				pieceBuilder.addPoint(x, y);
			}
			revPieces.add(pieceBuilder.build());
		}
		return new Answer(revFrames, revPieces, score,targetFrameIndex,targetVertexIndex);
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
