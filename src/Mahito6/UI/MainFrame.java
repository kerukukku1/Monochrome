/**
 * 
 */
/**
 * @author fujinomahito
 *
 */
package Mahito6.UI;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import Main.UI.Util.MyKeyListener;

public class MainFrame extends JFrame{
	public static final int frame_height = 600;
	public static final int frame_width = 700;
	
	private MainFrame mine;
	private MainPanel mainPanel;
	private String title;
	
	public MainFrame(String title){
		this.mine = this;
		this.title = title;
		launchUI();
		this.setLocationRelativeTo(null);
		this.requestFocusInWindow();
		this.setVisible(true);
		this.addKeyListener(new MyKeyListener());
	}
	
	private void launchUI() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle(title);
		this.setSize(MainFrame.frame_width, MainFrame.frame_height);
		this.setResizable(false);
		setMainPanel();	
	}
	
	
	private void setMainPanel(){
		mainPanel = new MainPanel();
		this.add(mainPanel);
	}

}