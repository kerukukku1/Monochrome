package tmcit.tampopo.ui;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

public class SolverDetailPanel extends JPanel{
	
	public SolverDetailPanel(int WIDTH,int HEIGHT){
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		initPanel();
	}
	
	public void initPanel(){
		this.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
	}

}
