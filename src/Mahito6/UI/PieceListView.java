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
	private JScrollPane sp;
	private List<Coordinates> coords;
	
	public PieceListView(List< List<Tuple2<Double, Double>> > vertex, List<Coordinates> coords){
		this.coords = coords;
		this.vertex = vertex;
		title = "ListViewer";
		launchUI();
		this.requestFocusInWindow();
		this.setVisible(true);
		paintPiecePanel();
	}
	
	private void paintPiecePanel(){
		for(int i = 0; i < vertex.size(); i++){
			PieceViewPanel p = new PieceViewPanel((i%4)*205+5,(i/4)*255+5,200,250, i, vertex.get(i), coords.get(i));
			paint.add(p);
		}
		paint.setPreferredSize(new Dimension(830, 275+255*(vertex.size()/4)));
		this.revalidate();
		this.repaint();
	}
	
	private void launchUI() {
		this.setTitle(title);
		this.setResizable(false);
		this.setSize(850, 520);
		earth = new JPanel();
		paint = new JPanel();
		paint.setLayout(null);
	    JScrollPane scrollpane = new JScrollPane();
	    scrollpane.setPreferredSize(new Dimension(850, 520));
	    JViewport view = new JViewport();
	    view.setView(paint);
	    scrollpane.setViewport(view);
	    earth.add(scrollpane);
	    this.add(earth);
	}
}