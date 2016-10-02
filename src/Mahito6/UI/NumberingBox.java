package Mahito6.UI;

import java.awt.Color;

import javax.swing.JTabbedPane;
import javax.swing.UIManager;

public class NumberingBox extends JTabbedPane{
	public NumberingBox(){
	    try
	    {
	            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
	            UIManager.put("TabbedPane.selected",Color.GREEN);
	    } catch (Exception ee){
	        ee.printStackTrace();
	    }
//		this.setLayout(null);
		this.addTab("test", null);
	}
	
	public void setImage(String path){
		
	}
}
