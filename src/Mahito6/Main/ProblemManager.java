package Mahito6.Main;

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
	public ProblemManager(MainFrame mainFrame){
//		ProblemManager.mainFrame = mainFrame;
		ProblemManager.imageManager = new ImageManager();
	}
	
	public static void generatePieceDatas(){
		imageManager.getPieces();
	}
	
	public static void setProblem(Problem problem){
		ProblemManager.problem = problem;
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
	
//	public static MainPanel getMainPanel(){
//		return ProblemManager.mainFrame.getMainPanel();
//	}
}
