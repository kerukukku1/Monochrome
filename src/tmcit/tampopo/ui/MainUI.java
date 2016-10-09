package tmcit.tampopo.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import tmcit.tampopo.util.Answer;
import tmcit.tampopo.util.Problem;
import tmcit.tampopo.util.ProblemReader;
import Mahito6.UI.PieceListView;

public class MainUI extends JFrame implements ActionListener{

	public static final int MAINFRAME_WIDTH = 1000;
	public static final int MAINFRAME_HEIGHT = 770;

	public static final int MAINTABPANE_WIDTH = 980;
	public static final int MAINTABPANE_HEIGHT = 700;

	public Mahito6.Main.Main mahitoMain;

	public PieceListView pieceListView;
	public SolverPanel solverPanel;
	public ToAnswerPanel toAnswerPanel;

	public JPanel mainPanel;///ここに全体のタブ貼ったりする
	public MainTabPane tabPane;

	public MainUI(Mahito6.Main.Main mahitoMain){
		this.mahitoMain = mahitoMain;
		initJFrame();
		setMenuBar();
		makePanels();
	}

	public void initJFrame(){
		this.setSize(new Dimension(MAINFRAME_WIDTH,MAINFRAME_HEIGHT));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void makePanels(){
		mainPanel = new JPanel();
		mainPanel.setLayout(new FlowLayout());
		mainPanel.setBackground(Color.BLACK);

		tabPane = new MainTabPane(MAINTABPANE_WIDTH,MAINTABPANE_HEIGHT);

		pieceListView = mahitoMain.pieceView;
		solverPanel = new SolverPanel();

		tabPane.addTab("Scanner", pieceListView);
		tabPane.addTab("SOLVER", solverPanel);
		mainPanel.add(tabPane);
		this.add(mainPanel);
	}

	private void setMenuBar() {
		JMenuBar menubar = new JMenuBar();
		JMenu menu1 = new JMenu("File");
		JMenu menu2 = new JMenu("Solver");
		JMenu menu3 = new JMenu("Mono");
		JMenuItem menuitem2 = new JMenuItem("Refresh");
		menuitem2.setAccelerator(KeyStroke.getKeyStroke(
				  KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
		JMenuItem menuitem3 = new JMenuItem("Tab_Close");
		menuitem3.setAccelerator(KeyStroke.getKeyStroke(
				  KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK));
		JMenuItem menuitem4 = new JMenuItem("MOVE_MASTER");
		menuitem4.setAccelerator(KeyStroke.getKeyStroke(
				  KeyEvent.VK_M, InputEvent.CTRL_DOWN_MASK));
		JMenuItem menuitem5 = new JMenuItem("OVERWRITE(TEST)");
		menuitem5.setAccelerator(KeyStroke.getKeyStroke(
				  KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		JMenuItem menuitem10 = new JMenuItem("GRID");
		menuitem10.setAccelerator(KeyStroke.getKeyStroke(
				  KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK));
		JMenuItem menuitem6 = new JMenuItem("Load_quest");
		JMenuItem menuitem7 = new JMenuItem("Send_answer");
		JMenuItem menuitem1 = new JMenuItem("Exit");

		JMenuItem menuitem8 = new JMenuItem("Store");
		menuitem8.setAccelerator(KeyStroke.getKeyStroke(
				  KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		JMenuItem menuitem9 = new JMenuItem("EdgeFinder");
		menuitem9.setAccelerator(KeyStroke.getKeyStroke(
				  KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));

		menuitem1.addActionListener(this);
		menuitem2.addActionListener(this);
		menuitem3.addActionListener(this);
		menuitem4.addActionListener(this);
		menuitem5.addActionListener(this);
		menuitem6.addActionListener(this);
		menuitem7.addActionListener(this);
		menuitem8.addActionListener(this);
		menuitem9.addActionListener(this);
		menuitem10.addActionListener(this);
		menu1.add(menuitem2);
		menu1.add(menuitem1);
		menu2.add(menuitem6);
		menu2.add(menuitem3);
		menu2.add(menuitem4);
		menu2.add(menuitem5);
		menu2.add(menuitem10);
		menu2.add(menuitem7);
		menu3.add(menuitem8);
		menu3.add(menuitem9);
		menubar.add(menu1);
		menubar.add(menu2);
		menubar.add(menu3);
		this.setJMenuBar(menubar);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String value = ((JMenuItem)event.getSource()).getText();
		if(value.equalsIgnoreCase("Refresh")){
			this.repaint();
		}else if(value.equalsIgnoreCase("Tab_Close")){
			SuperPanel superPanel = solverPanel.getViewingSuperPanel();
			if(superPanel == null)return;
			superPanel.removeViewingBigImageOrMiniList();
		}else if(value.equalsIgnoreCase("Exit")){
			System.exit(0);
		}else if(value.equalsIgnoreCase("MOVE_MASTER")){
			Answer answer = solverPanel.getViewingAnswer();
			SuperPanel superPanel = solverPanel.getMasterSuperPanel();
			if(superPanel == null || answer == null)return;
			superPanel.addAnswer(answer.getCopy());
		}else if(value.equalsIgnoreCase("OVERWRITE(TEST)")){
			Answer answer = solverPanel.getViewingAnswer();
			SuperPanel superPanel = solverPanel.getViewingSuperPanel();
			if(answer == null || superPanel == null)return;
			superPanel.overwriteAnswerToMaster(answer.getCopy());
		}else if(value.equalsIgnoreCase("Load_quest")){
			File quest = new File(tmcit.tampopo.main.Main.questDir);
			File index = new File(tmcit.tampopo.main.Main.indexDir);
			ProblemReader problemReader = new ProblemReader(quest,index);
			try {
				Problem problem = problemReader.load();
				solverPanel.setProblem(problem);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}else if(value.equalsIgnoreCase("Send_answer")){
			Problem problem = ProblemReader.problem;
			Answer answer = solverPanel.getViewingAnswer();
			if(problem == null || answer == null || problem.piece2Image == null)return;
			if(toAnswerPanel != null){
				tabPane.remove(toAnswerPanel);
				toAnswerPanel = null;
			}
			toAnswerPanel = new ToAnswerPanel(problem, answer.getCopy());
			tabPane.addTab("ANSWER", toAnswerPanel);
		}else if(value.equalsIgnoreCase("Store")){
			Mahito6.Main.Main.pieceView.callStore();
		}else if(value.equalsIgnoreCase("EdgeFinder")){
			Mahito6.Main.Main.pieceView.callRun();
		}else if(value.equalsIgnoreCase("GRID")){
			Problem problem = ProblemReader.problem;
			Answer source = new Answer(problem.frames,problem.pieces);
			source = source.convertToGrid();
			Problem newprob = new Problem(source.frames,source.pieces);
			newprob.setPiece2Image(problem.piece2Image);
			newprob.setRealPieces(problem.realPieces);
			ProblemReader.problem = newprob;
			solverPanel.setProblem(newprob);
		}
	}
}
