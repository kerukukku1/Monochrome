package Main.UI.Util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import Mahito6.Main.Constants;
import Mahito6.Main.Problem;
import Mahito6.Main.ProblemManager;
import Mahito6.Main.Tuple2;

public class FolderManager {
	public static final String currentPath = System.getProperty("user.home")+File.separator+"procon27" + File.separator;
	public static final String imagePath = currentPath + "images" + File.separator;
	public static final String presetsPath = currentPath + "presets" + File.separator;
	public FolderManager(){}

	public void buildDirectory(){
		buildCurrent();
		buildPresets();
		buildImages();
	}
	private void buildImages() {
		if(!new File(imagePath).exists()){
			if(new File(imagePath).mkdir()){
				System.out.println("mkdir " + imagePath);
			}else{
				System.out.println("permission denied");
			}
		}
	}
	
	private void buildPresets() {
		if(!new File(presetsPath).exists()){
			if(new File(presetsPath).mkdir()){
				System.out.println("mkdir " + presetsPath);
			}else{
				System.out.println("permission denied");
			}
		}
	}

	private void buildCurrent(){
		if(!new File(currentPath).exists()){
			if(new File(currentPath).mkdir()){
				System.out.println("mkdir " + currentPath);
			}else{
				System.out.println("permission denied");
			}
		}
	}

	public static void questSave(){
		List<Problem> problems = ProblemManager.getProblems();
		if(problems.size() == 0){
			System.out.println("No problem. Save Faild");
			return;
		}
		List<String> ansFrame = new ArrayList<String>();
		ansFrame.add("dummy");
		List<String> ansPiece = new ArrayList<String>();
		ansPiece.add("dummy");

		int frameSize = 0;
		int pieceSize = 0;

		for(int i = 0; i < problems.size(); i++){
			Problem p = problems.get(i);
			if(p.isWaku()){
				List<List<Tuple2<Double, Double>>> v = p.getVertex();
				for(int j = 0; j < v.size(); j++){
					List<Tuple2<Double, Double>> v2 = v.get(j);
					if(v2.size() < 3){
						//図形が構成されていない場合は除去
						continue;
					}
					Coordinates c = p.getCoord(j);
					ansFrame.add(String.valueOf(v2.size()));
					for(int k = 0; k < v2.size(); k++){
						double x = (v2.get(k).t1+c.minx - Constants.imagePositionOffset/2) * (Constants.unitInch / ProblemManager.dpi);
						double y = (v2.get(k).t2+c.miny - Constants.imagePositionOffset/2) * (Constants.unitInch / ProblemManager.dpi);
						ansFrame.add(String.valueOf(x) + " " + String.valueOf(y));
					}
					frameSize++;
				}
			}else{
				List<List<Tuple2<Double, Double>>> v = p.getVertex();
				for(int j = 0; j < v.size(); j++){
					List<Tuple2<Double, Double>> v2 = v.get(j);
					if(v2.size() < 3){
						//図形が構成されていない場合は除去
						continue;
					}
					ansPiece.add(String.valueOf(v2.size()));
					for(int k = 0; k < v2.size(); k++){
						double x = (v2.get(k).t1 - Constants.imagePositionOffset/2) * (Constants.unitInch / ProblemManager.dpi);
						double y = (v2.get(k).t2 - Constants.imagePositionOffset/2) * (Constants.unitInch / ProblemManager.dpi);
						ansPiece.add(String.valueOf(x) + " " + String.valueOf(y));
					}
					pieceSize++;
				}
			}
		}
		//dummyを個数に置き換え
		ansFrame.set(0, String.valueOf(frameSize));
		ansPiece.set(0, String.valueOf(pieceSize));
		try {
			File quest = new File(currentPath+"quest.txt");
			if(quest.exists())quest.delete();
			quest.createNewFile();
            FileWriter fw = new FileWriter(currentPath+"quest.txt", false);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

			for(int i = 0; i < ansFrame.size(); i++)pw.println(ansFrame.get(i));
			for(int i = 0; i < ansPiece.size(); i++)pw.println(ansPiece.get(i));

            pw.close();
            System.out.println("File Saved");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

	}

	public static File getPresetFile(String filename){
		String preset = presetsPath + filename;
		return new File(preset);
	}

	public static List<String> getPresetNames(){
		File dir = new File(presetsPath);
		List<String> ret = new ArrayList<String>();
	    File[] files = dir.listFiles();
	    for (int i = 0; i < files.length; i++) {
	    	if(!files[i].getName().matches(".*" + ".procon27" + ".*"))continue;
	        ret.add(files[i].getName());
	    }
	    return ret;
	}

	public static void indexSave() {
		// TODO Auto-generated method stub
		List<Problem> problems = ProblemManager.getProblems();
		if(problems.size() == 0){
			System.out.println("No problem. Save Faild");
			return;
		}
		List<String> ansPiece = new ArrayList<String>();
		ansPiece.add("dummy");
		String piece2_dir = "";
		int pieceSize = 0;
		int pieceCount = 0;

		for(int i = 0; i < problems.size(); i++){
			Problem p = problems.get(i);
			if(p.isWaku())continue;
			pieceCount++;
		}

		for(int i = 0; i < problems.size(); i++){
			Problem p = problems.get(i);
			if(p.isWaku())continue;
			//piece2のディレクトリ取得
			if(pieceCount == 1){
				piece2_dir = p.getPath();
			}else{
				if(p.getType() == 1)piece2_dir = p.getPath();
			}
			List<List<Tuple2<Double, Double>>> v = p.getVertex();
			for(int j = 0; j < v.size(); j++){
				List<Tuple2<Double, Double>> v2 = v.get(j);
				if(v2.size() < 3){
					//図形が構成されていない場合は除去
					continue;
				}
				if(p.getType() == 0 && pieceCount != 1){
					ansPiece.add(String.valueOf(0));
				}else{
					ansPiece.add(String.valueOf(v2.size()));
					for(int k = 0; k < v2.size(); k++){
						double x = (v2.get(k).t1+p.getCoord(j).minx - Constants.imagePositionOffset/2);
						double y = (v2.get(k).t2+p.getCoord(j).miny - Constants.imagePositionOffset/2);
						ansPiece.add(String.valueOf(x) + " " + String.valueOf(y));
					}	
				}
				pieceSize++;
			}
		}

		//dummyを個数に置き換え
		ansPiece.set(0, String.valueOf(pieceSize));
		try {
			File tar = new File(currentPath+"index.txt");
			if(tar.exists())tar.delete();
			tar.createNewFile();
            FileWriter fw = new FileWriter(currentPath+"index.txt", false);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            pw.println(piece2_dir);
			for(int i = 0; i < ansPiece.size(); i++)pw.println(ansPiece.get(i));

            pw.close();
            System.out.println("File Saved");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}
}
