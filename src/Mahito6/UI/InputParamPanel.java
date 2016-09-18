package Mahito6.UI;

import java.awt.Color;
import java.lang.reflect.Field;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InputParamPanel extends JPanel{
	private String type, title;
	private JTextField tf;
	private JLabel label;
	private Object value;
	private Field mine;
	private int parent_width;
	public InputParamPanel(String title, String type, Object value, int parent_width){
		this.title = title;
		this.type = type;
		this.value = value;
		this.parent_width = parent_width;
		this.setLayout(null);
		this.setBackground(Color.CYAN);
		launchItems();
	}
	
	private void launchItems(){
		setLabel();
		setTextField();
	}
	
	private void setTextField() {
		tf = new JTextField();
		tf.setText(String.valueOf(value));
		tf.setBounds(parent_width/2, 0, parent_width/2 - 5, 30);
		this.add(tf);
	}

	private void setLabel() {
		label = new JLabel(title);
		label.setBounds(5, 0, parent_width/2 - 5, 30);
		this.add(label);
	}

	public void setValue(Object value){
		if(type.equals("int")){
			setValue((int)value);
		}else{
			setValue((double)value);
		}
	}

	private void setValue(int value){
		System.out.println(title + ":" + "integer");
	}
	
	private void setValue(double value){
		System.out.println(title + ":" + "double");
	}
	
	public Object getValue(){
		if(type.equals("int")){
			value = (Object)Integer.valueOf(tf.getText());			
		}else{
			value = (Object)Double.valueOf(tf.getText());
		}
		return value;
	}

	public void setField(Field field) {
		// TODO Auto-generated method stub
		this.mine = field;
	}
	
	public Field getField(){
		return mine;
	}
}
