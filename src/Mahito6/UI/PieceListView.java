package Mahito6.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;
import javax.swing.UIManager;

import Mahito6.Main.Problem;
import Mahito6.Main.ProblemManager;
import Mahito6.Main.Tuple2;

public class PieceListView extends JPanel{
	private String title;
	private JPanel paint;
	private JPanel earth;
	private HandlSummaryPanel viewer;
	private JTabbedPane summary;
	private NumberingBox numBox;

	public static List<PieceViewPanel> pieceViews;
	public static int tabWidth = 980;
	public static int tabHeight = 700;
	
	private int padding = 5;
	private int line = 3;

	public PieceListView(){
		title = "ListViewer";
	    try
	    {
	            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
	            UIManager.put("TabbedPane.selected",Color.GREEN);
	    } catch (Exception ee){
	        ee.printStackTrace();
	    }
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
		int border_offset = 35;
		List< List<Tuple2<Double, Double>> > vertex;
		int count = 0;
		int ypos = 0;
		paint.removeAll();
		for(int loop = 0; loop < problems.size(); loop++){
			Problem problem = problems.get(loop);
			if(!problem.isWaku())continue;
			vertex = problem.getVertex();
			PieceBox pb = new PieceBox(problem);
			int pcount = 0;
			for(int i = 0; i < vertex.size(); i++){
//				System.out.println(String.valueOf((pcount%line)*(padding + PieceViewPanel.Width)+10)+"," + String.valueOf(((pcount)/line)*(padding + PieceViewPanel.Height)+ border_offset) + ", " + PieceViewPanel.Width + ", " + PieceViewPanel.Height);
				PieceViewPanel p = new PieceViewPanel((pcount%line)*(padding + PieceViewPanel.Width)+10,((pcount)/line)*(padding + PieceViewPanel.Height)+ border_offset, i, problem);
				pieceViews.add(p);
				pb.add(p);
//				paint.add(p);
				++count;pcount++;
			}
			if(count%line != 0)count += (line - count%line);
//			JLabel label = new JLabel();
//			label.setBounds(0,((count)/line)*(padding + PieceViewPanel.Height)+2, 1000, 3);
//			label.setBackground(Color.BLACK);
//			label.setOpaque(true);
			pb.setBounds(0, ypos, (padding + PieceViewPanel.Width)*line + 25, (count / line)*(padding + PieceViewPanel.Height) + border_offset);
			ypos = ((count / line)*(padding + PieceViewPanel.Height) + border_offset);
			paint.add(pb);
		}
		for(int loop = 0; loop < problems.size(); loop++){
			Problem problem = problems.get(loop);
			if(problem.isWaku())continue;
			vertex = problem.getVertex();
			PieceBox pb = new PieceBox(problem);
			int pcount = 0;
			for(int i = 0; i < vertex.size(); i++){
				PieceViewPanel p = new PieceViewPanel((pcount%line)*(padding + PieceViewPanel.Width)+10,((pcount)/line)*(padding + PieceViewPanel.Height)+ border_offset, i, problem);
				pieceViews.add(p);
				pb.add(p);
//				paint.add(p);
				++count;pcount++;
			}
			if(count%line != 0)count += (line - count%line);
//			JLabel label = new JLabel();
//			label.setBounds(0,((count)/line)*(padding + PieceViewPanel.Height)+2, 1000, 3);
//			label.setBackground(Color.BLACK);
//			label.setOpaque(true);
			pb.setBounds(0, ypos, (padding + PieceViewPanel.Width)*line + 25, (count / line)*(padding + PieceViewPanel.Height) + border_offset);
			ypos = ((count / line)*(padding + PieceViewPanel.Height) + border_offset);
			paint.add(pb);
		}		
		//追加されたピースは改行されて表示されるようにする。
		paint.setPreferredSize(new Dimension((padding + PieceViewPanel.Width)*line + 25, (padding + PieceViewPanel.Height)*(count/line)));
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

		
		JScrollPane scrollpane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    scrollpane.setPreferredSize(new Dimension((padding + PieceViewPanel.Width)*line + 25, tabHeight-80));
	    JViewport view = new JViewport();
	    view.setView(paint);
	    scrollpane.setViewport(view);
	    scrollpane.getVerticalScrollBar().setUnitIncrement(10);
	    paint.setPreferredSize(new Dimension((padding + PieceViewPanel.Width)*line + 25, tabHeight-80));
//	    scrollpane.setBounds(0, 0, tabWidth, tabHeight-50);
	    JPanel p = new JPanel();
	    p.setBounds(0, 0, (padding + PieceViewPanel.Width)*line + 25, tabHeight-80);
	    p.add(scrollpane);
	    earth.add(p);
	    earth.setBounds(0, 0, tabWidth, tabHeight);
	    earth.setBackground(Color.BLACK);
	    
	    numBox = new NumberingBox();
	    numBox.setBackground(Color.BLUE);
	    numBox.setBounds((padding + PieceViewPanel.Width)*line + 25, 0, tabWidth - ((padding + PieceViewPanel.Width)*line + 25), (int)(Math.sqrt(2.0) * (tabWidth - ((padding + PieceViewPanel.Width)*line + 25))));
	    earth.add(numBox);

	    InputPanel inputPanel = new InputPanel(0, tabHeight-80, tabWidth, 30);
	    earth.add(inputPanel);

	    summary = new JTabbedPane();
	    summary.addTab("List", earth);
	    summary.addTab("Viewer", new JPanel());
	    summary.setPreferredSize(new Dimension(tabWidth, tabHeight));
	    summary.setBounds(0, 0, tabWidth, tabHeight);
	    this.add(summary);

	    pieceViews = new ArrayList<PieceViewPanel>();
	    this.revalidate();
	    this.repaint();
	}

	public void initializePanel(){
		summary.removeAll();
		this.remove(summary);
		launchUI();
	}
}