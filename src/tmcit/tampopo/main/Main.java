package tmcit.tampopo.main;


import tmcit.tampopo.ui.MainUI;

public class Main {
	
	public MainUI mainFrame;
	
	public Main(){
		initUI();
	}
	
	public void initUI(){
		this.mainFrame = new MainUI();
	}
	
	public static void main(String[] args){
		Main main = new Main();
	}

}
