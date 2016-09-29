package tmcit.tampopo.main;

import java.awt.Color;

import javax.swing.UIManager;

import tmcit.tampopo.ui.MainUI;

public class Main {

	public Mahito6.Main.Main mahitoMain;
	public MainUI mainFrame;

	public Main(){
		mahitoMain = new Mahito6.Main.Main();
		new tmcit.tampopo.util.IconUtil("resources");
		initUI();
		mainFrame.setVisible(true);
	}

	public void initUI(){
	    try
	    {
	            UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
	            UIManager.put("TabbedPane.selected",Color.GREEN);
	    } catch (Exception ee){
	        ee.printStackTrace();
	    }
		this.mainFrame = new MainUI(mahitoMain);
	}

	public static void main(String[] args){
		Main main = new Main();
	}

}
