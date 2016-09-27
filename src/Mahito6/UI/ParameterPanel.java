package Mahito6.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import Mahito6.Main.ProblemManager;
import Mahito6.Main.SolverConstants;
import Mahito6.Main.Tuple2;
import Mahito6.Solver.CrossAlgorithm;
import Mahito6.Solver.EdgeFinder;
import Main.UI.Util.FolderManager;
import Main.UI.Util.PresetReader;

public class ParameterPanel extends JPanel implements ActionListener, ItemListener{
	private HandlSummaryPanel parent;
	private JButton run, save, next, back;
	private JPanel paramField;
	private List<InputParamPanel> params;
	private SolverConstants consts;
	private JComboBox pulldown;
	public ParameterPanel(HandlSummaryPanel parent){
		this.parent = parent;
		this.setLayout(null);
		this.setSize(300, HandlSummaryPanel.visualizeHeight-300);
		this.setBackground(new Color(51,149,230));
		launchItems();
	}
	
	private void launchParamField() throws IllegalArgumentException, IllegalAccessException{
		params = new ArrayList<>();
		paramField = new JPanel();
		paramField.setLayout(null);
		Dimension d = this.getSize();
		paramField.setBounds(5,5,d.width-10,d.height-55);
		consts = new SolverConstants();
		setPulldown();
		int count = 1;
        for (Field field : consts.getClass().getDeclaredFields()) {
        	String title = field.getName();
        	String type = field.getType().getName();
        	InputParamPanel ipp = new InputParamPanel(title, type, field.get(consts), paramField.getSize().width);
        	//値を参照するために渡す
        	ipp.setField(field);
        	ipp.setBounds(0, count*30, d.width, 30);
        	//store to list
        	params.add(ipp);
        	//add component
        	paramField.add(ipp);
        	count++;
        }
		
		this.add(paramField);
	}
	
	private void setPulldown() {
		Vector<String> presetList = new Vector<String>();
		List<String> names = FolderManager.getPresetNames();
		for(String name : names){
			presetList.add(name);
		}
		pulldown = new JComboBox(presetList);
		Dimension d = paramField.getSize();
		pulldown.setBounds(0, 0, d.width, 30);
		pulldown.addItemListener(this);
		paramField.add(pulldown);
	}

	private void launchItems() {
		run = new JButton("Run");
		save = new JButton("Store");
		next = new JButton(">");
		back = new JButton("<");
		Dimension d = this.getSize();
		save.setBounds(0,d.height-25,d.width/2, 20);
		run.setBounds(d.width/2, d.height-25, d.width/2, 20);
		back.setBounds(0,d.height-45,d.width/2, 20);
		next.setBounds(d.width/2, d.height-45, d.width/2, 20);
		run.addActionListener(this);
		save.addActionListener(this);
		back.addActionListener(this);
		next.addActionListener(this);
		this.add(save);
		this.add(run);
		this.add(back);
		this.add(next);
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
	
	private void reloadConstants(){
		for(int i = 0; i < params.size(); i++){
			InputParamPanel ipp = params.get(i);
			Field field = ipp.getField();
			try {
				field.setAccessible(true);
				field.set(consts, ipp.getValue());
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	public SolverConstants getConstants(){
		reloadConstants();
		return consts;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String cmd = e.getActionCommand();
		if(cmd.equals("<")){
			parent.back();
		}else if(cmd.equals(">")){
			parent.next();
		}else if(cmd.equals("Store")){
			parent.saveData();
//			parent.setTitle("Saved Data! [id]" + new Random().nextDouble());
		}else if(cmd.equals("Run")){
			reloadConstants();
			BufferedImage tarImage = parent.getImage();
			EdgeFinder finder = new EdgeFinder(parent.getImage(), false, consts);
			try {
				finder.edgeFind();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			CrossAlgorithm crossAlgorithm = new CrossAlgorithm(finder.getResult_edge(), tarImage.getWidth(), tarImage.getHeight());
			crossAlgorithm.solve();
			if(crossAlgorithm.isErrorCross()){
				System.out.println("cross error!!");
			}
			parent.relaunch(crossAlgorithm.getAnswer(), false);
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		String filename = (String)pulldown.getSelectedItem();
		PresetReader reader = new PresetReader(FolderManager.getPresetFile(filename));
		try {
			consts = reader.getSolverConsts();
			for(InputParamPanel ipp : params){
				Object obj = reader.getValue(ipp.title);
				ipp.reloadTextField(obj);
			}
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
