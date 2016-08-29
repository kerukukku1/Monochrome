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

import Mahito6.Main.Tuple2;
import Main.UI.Util.Coordinates;

public class PieceListView extends JFrame{
	private VisualizePanel visPanel;
	private String title;
	private List< List<Tuple2<Double, Double>> > vertex;
	private JPanel paint;
	private JPanel earth;
	private List<PieceViewPanel> views;
	private JScrollPane sp;
	
	public PieceListView(List< List<Tuple2<Double, Double>> > vertex){
		this.vertex = vertex;
		title = "ListViewer";
		launchUI();
		this.requestFocusInWindow();
		this.setVisible(true);
	    test();
	}
	
	private void test(){
		views = new ArrayList<PieceViewPanel>();
		int size = 19;
		for(int i = 0; i < size; i++){
			PieceViewPanel p = new PieceViewPanel((i%4)*240+5,(i/4)*194+5,235,190);
			paint.add(p);
			views.add(p);
		}
		paint.setPreferredSize(new Dimension(960, 199+194*(size/4)));
		this.revalidate();
		this.repaint();
	}
	
	private void launchUI() {
		this.setTitle(title);
		this.setResizable(false);
		this.setSize(1000, 600);
		earth = new JPanel();
		paint = new JPanel();
		paint.setLayout(null);
	    JScrollPane scrollpane = new JScrollPane();
	    scrollpane.setPreferredSize(new Dimension(980, 490));
	    JViewport view = new JViewport();
	    view.setView(paint);
	    scrollpane.setViewport(view);
	    earth.add(scrollpane);
	    this.add(earth);
	}
}