package tmcit.tampopo.main;


import tmcit.tampopo.ui.MainUI;

public class Main {

	public Mahito6.Main.Main mahitoMain;
	public MainUI mainFrame;

	public Main(){
		mahitoMain = new Mahito6.Main.Main();
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
