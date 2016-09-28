package tmcit.tampopo.ui;

import java.awt.Dimension;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import tmcit.api.ISolver;
import tmcit.api.Parameter;

public class ParameterDetailFrame extends JFrame{

	public static final int WIDTH = 300;
	public static final int HEIGHT = 400;

	public String title;
	public ISolver solver;
	public List<Parameter> parameters;
	public int solverId;

	public JPanel mainPanel;
	public PanelListManager panelListManager;

	public ParameterDetailFrame(String title,ISolver solver,List<Parameter> parameters){
		this.title = title;
		this.solver = solver;
		this.parameters = parameters;
		initFrame();
		makePanel();
		for(Parameter parm:parameters){
			System.out.println(parm.name + "" + parm.defaultValue);
		}
	}

	public void runSolver(){
		this.solverId = new Random().nextInt();
		solver.init(solverId);
//		solver.load(in);
		solver.run();
	}

	public void addParameter(Parameter parameter){

	}

	public void makePanel(){
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		panelListManager = new PanelListManager(WIDTH - 16, HEIGHT - 60, false);
		panelListManager.setBounds(0, 0, WIDTH - 16, HEIGHT - 60);

		mainPanel.add(panelListManager);
		this.add(mainPanel);
	}

	public void initFrame(){
		this.setTitle(title);
		this.setSize(new Dimension(300, 400));
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}

}
