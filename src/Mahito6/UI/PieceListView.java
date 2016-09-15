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
	public static List<PieceViewPanel> pieceViews;
	
	public PieceListView(){
		title = "ListViewer";
		launchUI();
		this.requestFocusInWindow();
		this.setVisible(true);
	}
	
	public void launchPiecePanel(){
		paintPiecePanel(ProblemManager.getProblems());
	}
	
	private void paintPiecePanel(List<Problem> problems){
		List< List<Tuple2<Double, Double>> > vertex;
		int count = 0;
		for(int loop = 0; loop < problems.size(); loop++){
			Problem problem = problems.get(loop);
			vertex = problem.getVertex();
			for(int i = 0; i < vertex.size(); i++){
				//まだ変更してないよ〜〜〜〜バグるよ！！！！！！！！！！！！！！！！！！！！！！！！！
				PieceViewPanel p = new PieceViewPanel((count%4)*205+5,(count/4)*255+5,200,250, i, problem);
				pieceViews.add(p);
				paint.add(p);
				++count;
			}
			//追加されたピースは改行されて表示されるようにする。
			count += (4 - count%4);
		}
		paint.setPreferredSize(new Dimension(830, 295+255*(count/4)));
		this.revalidate();
		this.repaint();
	}
	
	private void launchUI() {
		this.setTitle(title);
		this.setResizable(false);
		this.setSize(850, 570);
		earth = new JPanel();
		paint = new JPanel();
		this.setLayout(null);
		paint.setLayout(null);
	    JScrollPane scrollpane = new JScrollPane();
	    scrollpane.setPreferredSize(new Dimension(850, 520));
	    JViewport view = new JViewport();
	    view.setView(paint);
	    scrollpane.setViewport(view);
	    scrollpane.getVerticalScrollBar().setUnitIncrement(10);
	    earth.add(scrollpane);
	    earth.setBounds(0, 0, 850, 520);
	    this.add(earth);
	    
	    InputPanel inputPanel = new InputPanel(0, 520, 850, 30);
	    this.add(inputPanel);
	    
	    pieceViews = new ArrayList<PieceViewPanel>();
	}
}