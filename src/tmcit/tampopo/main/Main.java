package tmcit.tampopo.main;

import java.awt.Color;

import javax.swing.UIManager;

import tmcit.tampopo.geometry.network.IPManager;
import tmcit.tampopo.ui.MainUI;

public class Main {

	public static final String saveDir = System.getProperty("user.home")+"/procon27";
	public static final String questDir = System.getProperty("user.home")+"/procon27/quest.txt";
	public static final String indexDir = System.getProperty("user.home")+"/procon27/index.txt";

	public Mahito6.Main.Main mahitoMain;
	public MainUI mainFrame;

	public Main(){
		IPManager ipManager = new IPManager();
		ipManager.exec();
		mahitoMain = new Mahito6.Main.Main();
		new tmcit.tampopo.util.IconUtil("resources");
		initUI();
		mainFrame.setVisible(true);
	}

	public void initUI(){
	    try
	    {
//	            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
	            UIManager.put("TabbedPane.selected",new Color(220,220,240));
	    } catch (Exception ee){
	        ee.printStackTrace();
	    }
		this.mainFrame = new MainUI(mahitoMain);
	}

	public static void main(String[] args){
		Main main = new Main();
	}

}
