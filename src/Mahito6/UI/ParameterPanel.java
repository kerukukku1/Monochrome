package Mahito6.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import Mahito6.Main.ProblemManager;
import Mahito6.Main.SolverConstants;

public class ParameterPanel extends JPanel{
	private VisualizeFrame parent;
	private JButton run, load;
	private JPanel paramField;
	private List<InputParamPanel> params;
	public ParameterPanel(VisualizeFrame parent){
		this.parent = parent;
		this.setLayout(null);
		this.setSize(300, VisualizeFrame.visualizeHeight-300);
		this.setBackground(new Color(51,149,230));
		launchItems();
	}
	
	private void launchParamField() throws IllegalArgumentException, IllegalAccessException{
		params = new ArrayList<>();
		paramField = new JPanel();
		paramField.setLayout(null);
		Dimension d = this.getSize();
		paramField.setBounds(5,5,d.width-10,d.height-35);
		SolverConstants consts = new SolverConstants();
		int count = 0;
        for (Field field : consts.getClass().getDeclaredFields()) {
        	String title = field.getName();
        	String type = field.getType().getName();
        	InputParamPanel ipp = new InputParamPanel(title, type, field.get(consts), paramField.getSize().width);
        	ipp.setBounds(0, count*30, d.width, 30);
        	//store to list
        	params.add(ipp);
        	//add component
        	paramField.add(ipp);
        	count++;
        }
		
		this.add(paramField);
	}
	
	private void launchItems() {
		run = new JButton("Run");
		load = new JButton("Load");
		Dimension d = this.getSize();
		load.setBounds(0,d.height-25,d.width/2, 20);
		run.setBounds(d.width/2, d.height-25, d.width/2, 20);
		load.addKeyListener(parent);
		run.addKeyListener(parent);
		this.add(load);
		this.add(run);
		try {
			launchParamField();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
