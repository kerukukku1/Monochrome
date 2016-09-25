package Mahito6.Thread;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import Mahito6.Main.ProblemManager;
import Mahito6.Main.Tuple2;
import Mahito6.Solver.CrossAlgorithm;
import Mahito6.Solver.Edge;
import Mahito6.Solver.EdgeFinder;

public class SolverThreadingAgent {

	private int threadNum;
	private int pieceNum;
	private List<BufferedImage> sourceImages;
	private List<List<Tuple2<Double,Double>>> allAnswer;
	private List<Boolean> isErrorList;
	private List<List<Edge>> allEdges;

	/*
	 * threadNum:スレッドの数
	 * sourceImages:2値化された画像
	 */
	public SolverThreadingAgent(List<BufferedImage> sourceImages,int threadNum){
		this.threadNum = threadNum;
		this.sourceImages = sourceImages;
		this.pieceNum = sourceImages.size();
		System.out.println(pieceNum);
		this.allAnswer = new ArrayList<List<Tuple2<Double,Double>>>(pieceNum);
		this.allEdges = new ArrayList<List<Edge>>(pieceNum);
		this.isErrorList = new ArrayList<Boolean>(pieceNum);
		for(int i = 0; i < pieceNum; i++){
			allAnswer.add(null);
			allEdges.add(null);
			isErrorList.add(null);
		}
	}

	public List<Tuple2<Double, Double>> getCrossAnswer(int id){
		return allAnswer.get(id);
	}
	public boolean isError(int id){
		return isErrorList.get(id);
	}
	public List<List<Edge>> getAllEdges(){
		return allEdges;
	}

	private synchronized void endMethod(int id,List<Tuple2<Double, Double>> answerEdges,
			   boolean isError){
		///解をぶちこむためのメソッド、衝突防止
		isErrorList.set(id, isError);
		allAnswer.set(id, answerEdges);

		List<Edge> edges = new ArrayList<Edge>();
		for(int i = 0; i < answerEdges.size(); i++){
			Tuple2<Double, Double> e1 = answerEdges.get(i);
			Tuple2<Double, Double> e2 = answerEdges.get((i+1)%answerEdges.size());
			edges.add(new Edge(e1.t1,e1.t2,e2.t1,e2.t2));
		}
		allEdges.set(id, edges);
	}

	public void run() throws Exception{///並列化のすべてを投げる
		int split;///各スレッドの担当数
		if(threadNum <= pieceNum){
			split = pieceNum/threadNum;
		}else{
			split = 1;
		}
		int imageCount = 0;
//		List<Thread> runningThread = new ArrayList<Thread>();
		for(int i = 0;i < threadNum;i++){
			final List<BufferedImage> target = new ArrayList<BufferedImage>();
			final List<Integer> ids = new ArrayList<Integer>();
			final int threadId = i;
			while(imageCount != pieceNum){
				if(i!=(threadNum - 1) && target.size() == split)break;
				target.add(sourceImages.get(imageCount));
				ids.add(imageCount);
				imageCount++;
			}
			if(target.size() == 0)break;

			Thread newThread = new Thread(new Runnable(){
				///中身
				public void run() {
					for(int j = 0;j < target.size();j++){
						BufferedImage image = target.get(j);
						int id = ids.get(j);
						int w = image.getWidth(),h = image.getHeight();
						EdgeFinder edgeFinder = new EdgeFinder(image, false, ProblemManager.getConstants());
						try {
							edgeFinder.edgeFind();
						} catch (Exception e) {
							e.printStackTrace();
						}
						List<Edge> edges = edgeFinder.getResult_edge();
						CrossAlgorithm crossAlgorithm = new CrossAlgorithm(edges, w, h);
						crossAlgorithm.solve();
						List<Tuple2<Double, Double>> answerEdges = crossAlgorithm.getAnswer();
						boolean isError = crossAlgorithm.isErrorCross();
						endMethod(id,answerEdges,isError);
					}
//					target.clear();
				}
			});
			newThread.start();
			newThread.join();
//			runningThread.add(newThread);
		}

//		for(int i = 0;i < runningThread.size();i++){
//			runningThread.get(i).join();
//		}
	}
}