package tmcit.tampopo.main;

import java.io.File;

import tmcit.tampopo.ui.MainUI;

public class Main {
	
	public static final String saveDir = System.getProperty("user.home")+"/procon27";

	public Mahito6.Main.Main mahitoMain;
	public MainUI mainFrame;

	public Main(){
		mahitoMain = new Mahito6.Main.Main();
		new tmcit.tampopo.util.IconUtil("resources");
		initUI();
		mainFrame.setVisible(true);
	}

	public void initUI(){
		this.mainFrame = new MainUI(mahitoMain);
	}

	public static void main(String[] args){
		Main main = new Main();
	}

}
