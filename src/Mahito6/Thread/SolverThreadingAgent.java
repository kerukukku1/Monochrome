package Mahito6.Thread;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import Mahito6.Main.Tuple2;
import Mahito6.Solver.CrossAlgorithm;
import Mahito6.Solver.Edge;
import Mahito6.Solver.EdgeFinder;

public class SolverThreadingAgent {
	
	private int threadNum;
	private int pieceNum;
	private List<BufferedImage> sourceImages;
	private List<List<Tuple2<Double,Double>>> allAnswer;
	private List<BufferedImage> allAnswerImage;
	private List<Boolean> isErrorList;
	
	/*
	 * threadNum:スレッドの数
	 * sourceImages:2値化された画像
	 */
	public SolverThreadingAgent(List<BufferedImage> sourceImages,int threadNum){
		this.threadNum = threadNum;
		this.sourceImages = sourceImages;
		this.pieceNum = sourceImages.size();
		this.allAnswer = new ArrayList<List<Tuple2<Double,Double>>>(pieceNum);
		this.allAnswerImage = new ArrayList<BufferedImage>(pieceNum);
		this.isErrorList = new ArrayList<Boolean>(pieceNum);
	}
	
	public List<Tuple2<Double, Double>> getEdgeAnswer(int id){
		return allAnswer.get(id);
	}
	public BufferedImage getAnswerImage(int id){
		return allAnswerImage.get(id);
	}
	public boolean isError(int id){
		return isErrorList.get(id);
	}
	
	private synchronized void endMethod(int id,List<Tuple2<Double, Double>> answerEdges,
									   BufferedImage image,boolean isError){
		///解をぶちこむためのメソッド、衝突防止
		allAnswer.set(id, answerEdges);
		allAnswerImage.set(id, image);
		isErrorList.set(id, isError);
	}
	
	public void run() throws Exception{///並列化のすべてを投げる
		int split;///各スレッドの担当数
		if(threadNum <= pieceNum){
			split = pieceNum/threadNum;
		}else{
			split = 1;
		}
		int imageCount = 0;
		List<Thread> runningThread = new ArrayList<Thread>();
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
						EdgeFinder edgeFinder = new EdgeFinder(image, false);
						try {
							edgeFinder.edgeFind();
						} catch (Exception e) {
							e.printStackTrace();
						}
						List<Edge> edges = edgeFinder.getResult_edge();
						CrossAlgorithm crossAlgorithm = new CrossAlgorithm(edges, w, h);
						crossAlgorithm.solve();
						List<Tuple2<Double, Double>> answerEdges = crossAlgorithm.getAnswer();
						BufferedImage answerImage = crossAlgorithm.getAnswerImage();
						boolean isError = crossAlgorithm.isErrorCross();
						endMethod(id,answerEdges,answerImage,isError);
					}
				}
			});
			newThread.start();
			runningThread.add(newThread);
		}
		
		while(true){///全てのスレッドが終了するまで待つ
			for(int i = 0;i < runningThread.size();i++){
				Thread th = runningThread.get(i);
				if(th.isAlive()){
					///まだ終わってないスレッドあり
					i = -1;
					Thread.sleep(100L);
				}
			}
			break;
		}
	}

}
