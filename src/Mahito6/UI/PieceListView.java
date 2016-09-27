package Mahito6.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;

import Mahito6.Main.Problem;
import Mahito6.Main.ProblemManager;
import Mahito6.Main.Tuple2;
import Mahito6.Solver.Edge;
import Main.UI.Util.Coordinates;
import Main.UI.Util.ImageManager;

public class PieceListView extends JFrame{
	private String title;
	private JPanel paint;
	private JPanel earth;
	private HandlSummaryPanel viewer;
	private JTabbedPane summary;
	public static List<PieceViewPanel> pieceViews;
	private int line = 4;
	
	public PieceListView(){
		title = "ListViewer";
		launchUI();
		this.requestFocusInWindow();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
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
		paint.setPreferredSize(new Dimension(830, 295+255*(count/4)));
		this.revalidate();
		this.repaint();
	}
	
	public Dimension getTabSize(){
		return summary.getSize();
	}
	
	private void launchUI() {
		this.setTitle(title);
		this.setResizable(false);
		this.setSize(950, 800);
		earth = new JPanel();
		paint = new JPanel();
		viewer = null;
		this.setLayout(null);
		paint.setLayout(null);
	    JScrollPane scrollpane = new JScrollPane();
	    scrollpane.setPreferredSize(new Dimension(950, 750));
	    JViewport view = new JViewport();
	    view.setView(paint);
	    scrollpane.setViewport(view);
	    scrollpane.getVerticalScrollBar().setUnitIncrement(10);
	    earth.add(scrollpane);
	    earth.setBounds(0, 0, 950, 750);
	    
	    summary = new JTabbedPane();
	    summary.addTab("List", earth);
	    summary.addTab("Viewer", new JPanel());
	    summary.setPreferredSize(new Dimension(950, 750));
	    summary.setBounds(0, 0, 950, 750);
	    this.add(summary);
	    
	    InputPanel inputPanel = new InputPanel(0, 750, 950, 30);
	    this.add(inputPanel);
	    
	    pieceViews = new ArrayList<PieceViewPanel>();
	}
}