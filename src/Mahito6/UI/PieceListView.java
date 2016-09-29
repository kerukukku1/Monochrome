package Mahito6.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;

import Mahito6.Main.Problem;
import Mahito6.Main.ProblemManager;
import Mahito6.Main.Tuple2;

public class PieceListView extends JPanel{
	private String title;
	private JPanel paint;
	private JPanel earth;
	private HandlSummaryPanel viewer;
	private JTabbedPane summary;
	public static List<PieceViewPanel> pieceViews;
	public static int tabWidth = 980;
	public static int tabHeight = 700;
	private int line = 4;

	public PieceListView(){
		title = "ListViewer";
		launchUI();
		this.requestFocusInWindow();
//		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public void launchPiecePanel(){
		paintPiecePanel(ProblemManager.getProblems());
	}

	public void setVisualizePanel(HandlSummaryPanel vf){
		viewer = vf;
		summary.setComponentAt(1, viewer);
		summary.setSelectedIndex(1);
		summary.revalidate();
	}

	private void paintPiecePanel(List<Problem> problems){
		List< List<Tuple2<Double, Double>> > vertex;
		int count = 4;
		for(int loop = 0; loop < problems.size(); loop++){
			int bias = 1;
			Problem problem = problems.get(loop);
			vertex = problem.getVertex();
			if(problem.isWaku())bias = 0;
			for(int i = 0; i < vertex.size(); i++){
				PieceViewPanel p = new PieceViewPanel((count%line)*205+10,((bias*count)/line)*255+5,200,250, i, problem);
				pieceViews.add(p);
				paint.add(p);
				++count;
			}
			//追加されたピースは改行されて表示されるようにする。
			if(count%line != 0)count += (line - count%line);
		}
		paint.setPreferredSize(new Dimension(tabWidth, 295+255*(count/4)));
		this.revalidate();
		this.repaint();
	}

	public Dimension getTabSize(){
		return summary.getSize();
	}

	private void launchUI() {
//		this.setTitle(title);
//		this.setResizable(false);
		this.setPreferredSize(new Dimension(tabWidth, tabHeight));
		earth = new JPanel();
		paint = new JPanel();
		viewer = null;
		this.setLayout(null);
		paint.setLayout(null);
		earth.setLayout(null);
	    JScrollPane scrollpane = new JScrollPane();
	    scrollpane.setPreferredSize(new Dimension(tabWidth, tabHeight-50));
	    JViewport view = new JViewport();
	    view.setView(paint);
	    scrollpane.setViewport(view);
	    scrollpane.getVerticalScrollBar().setUnitIncrement(10);
	    paint.setPreferredSize(new Dimension(tabWidth-50, tabHeight));
//	    scrollpane.setBounds(0, 0, tabWidth, tabHeight-50);
	    JPanel p = new JPanel();
	    p.setBounds(0, 0, tabWidth, tabHeight-60);
	    p.add(scrollpane);
	    earth.add(p);
	    earth.setBounds(0, 0, tabWidth, tabHeight);
	    earth.setBackground(Color.BLACK);

	    InputPanel inputPanel = new InputPanel(0, tabHeight-60, tabWidth, 30);
	    earth.add(inputPanel);

	    summary = new JTabbedPane();
	    summary.addTab("List", earth);
	    summary.addTab("Viewer", new JPanel());
	    summary.setPreferredSize(new Dimension(tabWidth, tabHeight));
	    summary.setBounds(0, 0, tabWidth, tabHeight);
	    this.add(summary);

	    pieceViews = new ArrayList<PieceViewPanel>();
	    this.repaint();
	}

	public void initializePanel(){
		paint.removeAll();
		viewer.removeAll();
		launchUI();
	}
}