package Mahito6.Solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Mahito6.Main.Tuple2;
import Main.UI.Util.AngleUtil;
import Main.UI.Util.ImageManager;

public class DiffPiece{
	private List<List<Double>> realAngle;
	private List<List<Double>> realDist;
	private List<List<Double>> simuAngle;
	private List<List<Double>> simuDist;
	private List<List<Tuple2<Double, Double>>> realCross;
	private List<List<Tuple2<Double, Double>>> simuCross;
	private List<String> realData;
	private List<String> simuData;
	
	public DiffPiece(List<String> data){
		realData = ImageManager.data;
		realCross = new ArrayList<List<Tuple2<Double, Double>>>();
		simuCross = new ArrayList<List<Tuple2<Double, Double>>>();
		this.simuData = data;

		realCross = makeData(realData);
		realDist = calcDist(realCross);
		realAngle = calcAngle(realCross);

		simuCross = makeData(simuData);
		simuDist = calcDist(simuCross);
		simuAngle = calcAngle(simuCross);
		
		System.out.println("Distance:");
		compareError(realDist, simuDist, true);
		System.out.println("Angles:");
		compareError(realAngle, simuAngle, false);
	}
	
	private List<List<Tuple2<Double, Double>>> makeData(List<String> data){
		List<List<Tuple2<Double, Double>>> crossPoints = new ArrayList<List<Tuple2<Double, Double>>>();
		int frameNum = Integer.valueOf(data.get(0));
		int next = 0;
		for(int i = 0; i < frameNum; i++){
			next++;
			int count = Integer.valueOf(data.get(next));
			next += count;
		}
		next++;
		int pieceNum = Integer.valueOf(data.get(next));
		for(int i = 0; i < pieceNum; i++){
			next++;
			int count = Integer.valueOf(data.get(next));
			System.out.println(count);
			List<Tuple2<Double,Double>> tmp = new ArrayList<Tuple2<Double, Double>>();
			for(int j = 1; j <= count; j++){
				int now = j + next;
				String line = data.get(now);
				String[] spl = line.split(" ");
				System.out.println(spl[0] + " " + spl[1]);
				Tuple2<Double, Double> pairs = new Tuple2<Double, Double>(Double.valueOf(spl[0]),Double.valueOf(spl[1]));
				tmp.add(pairs);
			}
			crossPoints.add(tmp);
			next += count;
		}
		return crossPoints;
	}
	
	public List<List<Double>> calcDist(List<List<Tuple2<Double, Double>>> Block){
		List<List<Double>> list = new ArrayList<List<Double>>();
		for(int i = 0; i < Block.size(); i++){
			List<Double> dists = new ArrayList<Double>();
			List<Tuple2<Double, Double>> points = Block.get(i);
			for(int j = 0; j < points.size(); j++){
				Tuple2<Double, Double> c1 = points.get(j);
				Tuple2<Double, Double> c2 = points.get((j+1)%points.size());
				double dist = Edge.distance(c1.t1, c1.t2, c2.t1, c2.t2);
				dists.add(dist);
			}
			Collections.sort(dists, new MyComparator());
			list.add(dists);
		}
		return list;
	}
	
	public List<List<Double>> calcAngle(List<List<Tuple2<Double, Double>>> Block){
		List<List<Double>> list = new ArrayList<List<Double>>();
		for(int i = 0; i < Block.size(); i++){
			List<Tuple2<Double, Double>> points = Block.get(i);
			int flag = this.checkRotationDirection(points);
			if(flag==-1){
				//反時計回りを逆回転
				Collections.reverse(points);
			}
			AngleUtil util = new AngleUtil(points);
			List<Double> angles = null;
			try {
				angles = util.calc();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Collections.sort(angles, new MyComparator());
			list.add(angles);
		}
		return list;

	}
	
	public void compareError(List<List<Double>> real, List<List<Double>> simu, boolean isDist){
		List<Double> Errors = new ArrayList<Double>();
		List<Double> minErrors = new ArrayList<Double>();
		List<Double> maxErrors = new ArrayList<Double>();
		List<Double> avgErrors = new ArrayList<Double>();
		List<Integer> perm = new ArrayList<Integer>();
		double allDiff = 10000000.0;
		int errorIndex = 0;
		for(int i = 0; i < simu.size(); i++){
			List<Double> nows = simu.get(i);
			double errorMax = 10000000.0;
			double avgAll = 0.0;
			double minDiffAll = 10000000.0;
			double maxDiffAll = -1.0;
			int select = 0;
			for(int j = 0; j < real.size(); j++){
				List<Double> nowr = real.get(j);
				if(nowr.size() != nows.size())continue;
				double error = 0.0;
				double tmpDiff = 0;
				double avg = 0.0;
				double minDiff = 10000000.0;
				double maxDiff = -1.0;
				for(int k = 0; k < nowr.size(); k++){
					double distr = nowr.get(k);
					double dists = nows.get(k);
					if(isDist){
						distr *= 0.0423333333;
					}
					double dif = Math.abs(dists-distr);
					minDiff = Math.min(minDiff, dif);
					maxDiff = Math.max(maxDiff, dif);
					tmpDiff = Math.max(dif, tmpDiff);
					avg += dif;
					error += (dif/dists)*100;
				}
				avg /= nowr.size();
				error /= nowr.size();
				if(errorMax > error){
					errorMax = error;
					avgAll = avg;
					minDiffAll = minDiff;
					maxDiffAll = maxDiff;
					select = j;
					if(allDiff > tmpDiff){
						allDiff = tmpDiff;
						errorIndex = i;
					}
				}
			}
			Errors.add(errorMax);
			minErrors.add(minDiffAll);
			maxErrors.add(maxDiffAll);
			avgErrors.add(avgAll);
			perm.add(select);
		}
		
		for(int i = 0; i < Errors.size(); i++){
			System.out.println("No." + perm.get(i));
			System.out.println("Percentage: " + Errors.get(i)+"%");
			System.out.println("Mini: " + minErrors.get(i));
			System.out.println("Maxi: " + maxErrors.get(i));
			System.out.println("Average: " + avgErrors.get(i));
		}
		
		System.out.println("Bad Piece is " + errorIndex);
	}
	
	private int checkRotationDirection(List<Tuple2<Double, Double>> points){
		double s = 0;
		for(int i = 0; i < points.size(); i++){
			Tuple2<Double, Double> c1 = points.get(i);
			Tuple2<Double, Double> c2 = points.get((i+1)%points.size());
			s += (c1.t1 * c2.t2 - c2.t1 * c1.t2);
		}
		return (s<0.0)?-1:1;
	}
	
	private class MyComparator implements Comparator<Double> {

		@Override
		public int compare(Double a, Double b) {
			// TODO Auto-generated method stub
			if(a==b){
				return 0;
			}else if(a > b){
				return 1;
			}else{
				return -1;
			}
		}
	}
}