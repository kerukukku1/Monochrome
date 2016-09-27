package tmcit.tampopo.util;

import java.util.List;
import java.util.Random;

import javax.swing.tree.DefaultMutableTreeNode;

import tmcit.api.ISolver;
import tmcit.api.Parameter;
import tmcit.tampopo.ui.ParameterDetailFrame;

public class ParameterNode extends DefaultMutableTreeNode{
	
	private String title;
	private ISolver solver;
	private List<Parameter> parameters;
	private ParameterDetailFrame frame = null;
	
	public ParameterNode(String title,ISolver solver){
		super(title);
		this.title = title;
		this.solver = solver;
		this.parameters = solver.getParameters();
	}
	
	public void showDetail(){
		if(frame == null)frame = new ParameterDetailFrame(title,solver,parameters);
		frame.setVisible(true);
	}

}
