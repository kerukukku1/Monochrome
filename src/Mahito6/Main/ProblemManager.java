package Mahito6.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Mahito6.UI.InputPanel;
import Mahito6.UI.MainFrame;
import Mahito6.UI.MainPanel;
import Mahito6.UI.UtilityPanel;
import Main.UI.Util.ImageManager;
import Main.UI.Util.Status;

public class ProblemManager {
//	public static MainFrame mainFrame;
	public static UtilityPanel utilPanel;
	public static InputPanel inputPanel;
	public static ImageManager imageManager;
	public static Problem problem;
	public static List<Problem> problems;
	public static SolverConstants consts;
	public static double dpi = 0;
	public static HashMap<Status.Type, Boolean> loadMap;
	public static Status.Type[] types = {Status.Type.PIECE1, Status.Type.PIECE2, Status.Type.FRAME};
	public static String currentQuestPath;
	
	//0 : frame   1 : piece1   2: piece2
	public ProblemManager(){
//		ProblemManager.mainFrame = mainFrame;
		ProblemManager.imageManager = new ImageManager();
		problems = new ArrayList<Problem>();
		consts = new SolverConstants();
		loadMap = new HashMap<Status.Type, Boolean>();
		for(Status.Type type : types){
			loadMap.put(type, false);
		}
	}
	
	public static void setQuestPath(String path){
		ProblemManager.currentQuestPath = path;
	}
	
	public static String getQuestPath(){
		return ProblemManager.currentQuestPath;
	}
	
	public static SolverConstants getConstants(){
		return consts;
	}
	
	public static void generatePieceDatas(Status.Type type){
		imageManager.getPieces(type);
	}
	
	public static void resetImageManager(){
		problems.clear();
		consts = new SolverConstants();
		loadMap.clear();
		for(Status.Type type : types){
			loadMap.put(type, false);
		}
		imageManager.clearAll();
	}
	
	public static void removeImageManager(Status.Type type){
		for(Problem p : problems){
			if(p.getType() == type){
				problems.remove(p);
				break;
			}
		}
		loadMap.put(type, false);
		imageManager.clearAll();
	}
	
	public static void addProblem(Problem problem){
		ProblemManager.problem = problem;
		problems.add(problem);
		loadMap.put(problem.getType(), true);
	}
	
	
	public static void setInputPanel(InputPanel inputPanel){
		ProblemManager.inputPanel = inputPanel;
	}
	
	public static void setUtilityPanel(UtilityPanel utilPanel){
		ProblemManager.utilPanel = utilPanel;
	}
	
	public static Problem getProblem(){
		return ProblemManager.problem;
	}
	
	public static List<Problem> getProblems(){
		return ProblemManager.problems;
	}
	
//	public static MainPanel getMainPanel(){
//		return ProblemManager.mainFrame.getMainPanel();
//	}
}
