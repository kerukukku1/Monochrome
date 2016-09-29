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

import Mahito6.Main.Main;
import Mahito6.UI.PieceListView;
import tmcit.tampopo.util.Problem;
import tmcit.tampopo.util.ProblemReader;

public class MainUI extends JFrame implements ActionListener{

	public static final int MAINFRAME_WIDTH = 1000;
	public static final int MAINFRAME_HEIGHT = 770;

	public static final int MAINTABPANE_WIDTH = 980;
	public static final int MAINTABPANE_HEIGHT = 700;

	public Mahito6.Main.Main mahitoMain;

	public PieceListView pieceListView;
	public SolverPanel solverPanel;

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
		JMenuItem menuitem2 = new JMenuItem("Refresh");
		menuitem2.setAccelerator(KeyStroke.getKeyStroke(
				  KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
		JMenuItem menuitem3 = new JMenuItem("Tab_Close");
		menuitem3.setAccelerator(KeyStroke.getKeyStroke(
				  KeyEvent.VK_D, 0));
		JMenuItem menuitem4 = new JMenuItem("MERGE(TEST)");
		menuitem4.setAccelerator(KeyStroke.getKeyStroke(
				  KeyEvent.VK_M, 0));
		JMenuItem menuitem5 = new JMenuItem("OVERWRITE(TEST)");
		menuitem5.setAccelerator(KeyStroke.getKeyStroke(
				  KeyEvent.VK_O, 0));
		JMenuItem menuitem6 = new JMenuItem("load_quest");
		JMenuItem menuitem1 = new JMenuItem("Exit");
		menuitem1.addActionListener(this);
		menuitem2.addActionListener(this);
		menuitem3.addActionListener(this);
		menuitem4.addActionListener(this);
		menuitem5.addActionListener(this);
		menuitem6.addActionListener(this);
		menu1.add(menuitem2);
		menu1.add(menuitem1);
		menu2.add(menuitem6);
		menu2.add(menuitem3);
		menu2.add(menuitem4);
		menu2.add(menuitem5);
		menubar.add(menu1);
		menubar.add(menu2);
		this.setJMenuBar(menubar);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String value = ((JMenuItem)event.getSource()).getText();
		if(value.equalsIgnoreCase("Refresh")){
			this.repaint();
		}else if(value.equalsIgnoreCase("Tab_Close")){
			solverPanel.tabClose();
		}else if(value.equalsIgnoreCase("Exit")){
			System.exit(0);
		}else if(value.equalsIgnoreCase("MERGE(TEST)")){
			solverPanel.doMergeForMaster();
		}else if(value.equalsIgnoreCase("OVERWRITE(TEST)")){
			solverPanel.doOverwriteForMaster();
		}else if(value.equalsIgnoreCase("load_quest")){
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
		}
	}

}
