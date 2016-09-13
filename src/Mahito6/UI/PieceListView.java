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
	private List< List<Tuple2<Double, Double>> > vertex;
	private JPanel paint;
	private JPanel earth;
	public static List<PieceViewPanel> pieceViews;
	private List<List<Edge>> allEdges;
	
	public PieceListView(){
		title = "ListViewer";
		launchUI();
		this.requestFocusInWindow();
		this.setVisible(true);
	}
	
	public void launchPiecePanel(Problem p){
		this.vertex = p.getVertex();
		this.allEdges = p.getAllEdges();
		paintPiecePanel();
	}
	
	private void paintPiecePanel(){
		for(int i = 0; i < vertex.size(); i++){
			PieceViewPanel p = new PieceViewPanel((i%4)*205+5,(i/4)*255+5,200,250, i);
			pieceViews.add(p);
			paint.add(p);
		}
		paint.setPreferredSize(new Dimension(830, 295+255*(vertex.size()/4)));
		this.revalidate();
		this.repaint();
	}
	
	public List<Edge> getEdges(int index){
		return allEdges.get(index);
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