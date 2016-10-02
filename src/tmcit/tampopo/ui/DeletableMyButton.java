package tmcit.tampopo.ui;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;

public class DeletableMyButton extends JButton{
	
	private Component source;
	
	public DeletableMyButton(Icon icon,Component source){
		super(icon);
		this.source = source;
	}
	
	public Component getSource(){
		return source;
	}

}
