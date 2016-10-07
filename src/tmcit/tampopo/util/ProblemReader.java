package tmcit.tampopo.util;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import tmcit.api.AnswerChangeEvent;
import tmcit.api.ISolver;
import tmcit.api.Parameter;
import tmcit.api.SolverProgressEvent;
import tmcit.api.SolverProgressListener;
import tmcit.tampopo.geometry.util.Piece;
import tmcit.tampopo.geometry.util.Piece.PieceBuilder;
import tmcit.tampopo.geometry.util.Point;
import tmcit.tampopo.ui.BigImagePanel;
import tmcit.tampopo.ui.SolverPanel;
import tmcit.tampopo.ui.SuperPanel;
import tmcit.tampopo.ui.util.ExVertex;
import tmcit.tampopo.ui.util.ParameterNode;

public class ProblemReader {

	public File file,index;
	public static Problem problem = null;///問題データ
	public static Answer master = null;///マスター解
	public static SolverPanel solverPanel;///アクセスを簡易に

	public ProblemReader(File quest,File index){
		this.file = quest;
		this.index = index;
	}

	public static void stopAllSolver(){

	}

	public static Thread runSolver(ParameterNode parameterNode,ISolver solver,int solverID,List<Parameter> useParameters,PrintStream stream){
		String problemText = makeMixProblemText();
		ExVertex target = getTargetFrameAndVertex();
		if(problemText == null||target==null)return null;
		return ProblemReader.runSolver(parameterNode,problemText, solver, useParameters, solverID, target.frame, target.index,stream);
	}

	public static Thread runSolver(ParameterNode parameterNode
								  ,String problemText
								  ,ISolver solver
								  , List<Parameter> useParameters
								  ,int solverId
								  ,int targetFrame
								  ,int targetVertex
								  ,PrintStream stream){
		///ソルバーをThreadにして走らせる,Threadを返す
		for(Parameter parameter : useParameters){
			System.out.println(parameter.name+" : "+parameter.value);
		}
		solver.setPrintStream(stream);
		solver.setParameters(useParameters);
		solver.load(problemText,targetFrame,targetVertex);
		solver.init(solverId);

		SuperPanel superPanel = solverPanel.getSuperPanelFromParameterNode(parameterNode);
		superPanel.setAnswer(solverPanel.getViewingAnswer());
		solverPanel.setViewSuperPanel(superPanel);

		Thread solverThread = new Thread(solver);
		solverThread.start();
		AnswerChangeEvent.addListener(solverId, superPanel.right);
		SolverProgressEvent.addListener(solverId, new SolverProgressListener(){
			@Override
			public void solverProgressEvent(SolverProgressEvent event) {
				System.out.println("PROGRESS:"+event.percentage);
			}
		});
		System.out.println("SOLVER RUN!!");
		return solverThread;
	}

	public static void setSolverPanel(SolverPanel solverPanel){
		ProblemReader.solverPanel = solverPanel;
	}

	public static Answer convertSolvedAnswer(String answerText,double score){
		///ソルバーで帰ってきたStringをまたAnswerに直す
		List<Piece> frames = new ArrayList<Piece>();
		List<Piece> sourcePieces = new ArrayList<Piece>();
		for(Piece frame : problem.frames){
			frames.add(frame.getCopy());
		}

		List<Piece> pieces = new ArrayList<Piece>();
		Scanner scan = new Scanner(answerText);
		int N = scan.nextInt();
		for(int i = 0;i < N;i++){
			String idrstr = scan.next();
			String[] split = idrstr.split("_");
			boolean isReverse = false;
			int id = Integer.parseInt(split[0]);
			if(split.length == 2){
				isReverse = true;
			}
			int vertexNum = scan.nextInt();
			PieceBuilder pieceBuilder = new PieceBuilder();
			pieceBuilder.setID(id);
			pieceBuilder.setReverse(isReverse);
			for(int j = 0;j < vertexNum;j++){
				double x = scan.nextDouble();
				double y = scan.nextDouble();
				pieceBuilder.addPoint(x,y);
			}
			try {
				pieces.add(pieceBuilder.build());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		scan.close();
		Answer answer = new Answer(frames, pieces,score,0,0);
//		System.out.println(answerText);
		return answer;
	}

	public static ExVertex getTargetFrameAndVertex(){
		BigImagePanel bigImagePanel = solverPanel.getViewingBigImagePanel();
		if(bigImagePanel == null)return null;
		int frame = bigImagePanel.getSelectedFrameIndex();
		int vertex = bigImagePanel.getSelectedVertexIndex();
		return new ExVertex(frame, vertex, null, null);
	}

	public static String makeMixProblemText(){
		String ret = "";
		Answer mixAnswer = solverPanel.getViewingAnswer();
		if(problem == null || mixAnswer == null)return null;
		if(mixAnswer.frames.size() == 0)return null;///これピースだけだ?!
		List<Piece> frames = problem.frames;
		List<Piece> pieces = problem.pieces;
		List<Piece> mixPieces = mixAnswer.pieces;


		int frameNum = frames.size();
		ret += ""+frameNum+"\n";
		for(Piece frame : frames){
			int vertexNum = frame.getPointSize();
			ret += ""+vertexNum+"\n";
			for(int i = 0;i < vertexNum;i++){
				Point point = frame.getPoint(i);
				ret += ""+point.x+" "+point.y+"\n";
			}
		}
		int pieceNum = pieces.size();
		ret += ""+pieceNum+"\n";
		for(Piece piece : pieces){
			int vertexNum = piece.getPointSize();
			ret += ""+vertexNum+"\n";
			for(int i = 0;i < vertexNum;i++){
				Point point = piece.getPoint(i);
				ret += ""+point.x+" "+point.y+"\n";
			}
		}
		int mixNum = mixPieces.size();
		ret += ""+mixNum+"\n";
		for(Piece mixPiece : mixPieces){
			int pieceId = mixPiece.getID();
			int vertexNum = mixPiece.getPointSize();
			ret += ""+pieceId+"\n";
			ret += ""+vertexNum+"\n";
			for(int i = 0;i < vertexNum;i++){
				Point point = mixPiece.getPoint(i);
				ret += ""+point.x+" "+point.y+"\n";
			}
		}
		return ret;
	}

	public Problem load() throws Exception{
		Problem prob = loadQuest();
		if(index != null){
			loadIndex(prob);
		}
		return ProblemReader.problem = prob;
	}

	private void loadIndex(Problem prob) throws Exception{
		Scanner scan = new Scanner(index);
		String piece2ImageDir = scan.nextLine();
		List<Piece> realPieces = new ArrayList<Piece>();
		int N = scan.nextInt();
		if(prob.pieces.size() != N)return;///データが違う可能性高い
		for(int i = 0;i < N;i++){
			int V = scan.nextInt();
			if(V == 0)continue;
			PieceBuilder pieceBuilder = new PieceBuilder();
			pieceBuilder.setID(i);
			for(int j = 0;j < V;j++){
				double x = scan.nextDouble();
				double y = scan.nextDouble();
				pieceBuilder.addPoint(x, y);
			}
			realPieces.add(pieceBuilder.build());
		}
		scan.close();
		File imageFile = new File(piece2ImageDir);
		prob.setPiece2Image(imageFile);
		prob.setRealPieces(realPieces);
	}

	private Problem loadQuest() throws Exception{
		List<Piece> frames = new ArrayList<Piece>();
		List<Piece> pieces = new ArrayList<Piece>();
		Scanner scan = new Scanner(file);
		int frameNum = scan.nextInt();
		for(int i = 0;i < frameNum;i++){
			int vertexNum = scan.nextInt();
			PieceBuilder pieceBuilder = new PieceBuilder();
			pieceBuilder.setID(i);
			for(int j = 0;j < vertexNum;j++){
				double x = scan.nextDouble();
				double y = scan.nextDouble();
				pieceBuilder.addPoint(x, y);
			}
			Piece frame = pieceBuilder.build();
			frames.add(frame);
		}
		int pieceNum = scan.nextInt();
		for(int i = 0;i < pieceNum;i++){
			int vertexNum = scan.nextInt();
			PieceBuilder pieceBuilder = new PieceBuilder();
			pieceBuilder.setID(i);
			for(int j = 0;j < vertexNum;j++){
				double x = scan.nextDouble();
				double y = scan.nextDouble();
				pieceBuilder.addPoint(x, y);
			}
			Piece piece = pieceBuilder.build();
			pieces.add(piece);
		}
		scan.close();
		return ProblemReader.problem = new Problem(pieces, frames);
	}

}
