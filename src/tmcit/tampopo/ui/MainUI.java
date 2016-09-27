package tmcit.tampopo.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import Mahito6.UI.PieceListView;

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
		JMenuItem menuitem2 = new JMenuItem("Refresh");
		menuitem2.setAccelerator(KeyStroke.getKeyStroke(
				  KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
		JMenuItem menuitem3 = new JMenuItem("Tab_Close");
		menuitem3.setAccelerator(KeyStroke.getKeyStroke(
				  KeyEvent.VK_D, 0));
		JMenuItem menuitem1 = new JMenuItem("Exit");
		menuitem1.addActionListener(this);
		menuitem2.addActionListener(this);
		menuitem3.addActionListener(this);
		menu1.add(menuitem2);
		menu1.add(menuitem3);
		menu1.add(menuitem1);
		menubar.add(menu1);
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
		}
	}

}
