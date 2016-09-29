package Mahito6.Main;

import java.util.ArrayList;
import java.util.List;

import Mahito6.UI.InputPanel;
import Mahito6.UI.MainFrame;
import Mahito6.UI.MainPanel;
import Mahito6.UI.UtilityPanel;
import Main.UI.Util.ImageManager;

public class ProblemManager {
//	public static MainFrame mainFrame;
	public static UtilityPanel utilPanel;
	public static InputPanel inputPanel;
	public static ImageManager imageManager;
	public static Problem problem;
	public static List<Problem> problems;
	public static SolverConstants consts;
	public static double dpi = 0;
	
	//0 : frame   1 : piece1   2: piece2
	public static int switch_index = 0;
	public ProblemManager(){
//		ProblemManager.mainFrame = mainFrame;
		ProblemManager.imageManager = new ImageManager();
		problems = new ArrayList<Problem>();
		consts = new SolverConstants();
	}
	
	public static SolverConstants getConstants(){
		return consts;
	}
	
	public static void generatePieceDatas(){
		imageManager.getPieces();
	}
	
	public static void resetImageManager(){
		imageManager.clearAll();
	}
	
	public static void setSwitchType(int type){
		switch_index = type;
	}
	
	public static void addProblem(Problem problem){
		problem.setType(switch_index);
		ProblemManager.problem = problem;
		problems.add(problem);
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
