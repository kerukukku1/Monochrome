package tmcit.tampopo.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import tmcit.api.ISolver;
import tmcit.api.Parameter;
import tmcit.api.Parameter.ValueType;
import tmcit.api.SolverProgressEvent;
import tmcit.tampopo.ui.util.JTextAreaStream;
import tmcit.tampopo.ui.util.ParameterNode;
import tmcit.tampopo.util.Problem;
import tmcit.tampopo.util.ProblemReader;

public class ParameterDetailPanel extends JPanel implements ActionListener{
	
	public static final int WIDTH = SolverPanel.RIGHT_WIDTH - 10;
	public static final int HEIGHT = SolverPanel.RIGHT_HEIGHT - 30;
	public static final int PARAMETER_AREA_HEIGHT = 280;
	public static final int CONSOLE_AREA_HEIGHT = 180;
	public static final int BUTTON_WIDTH = 190,BUTTON_HEIGHT = 44;
	public static final int ONE_PARAMETER_HEIGHT = 20;

	public ParameterNode sourceNode;
	public String title;
	public ISolver solver;
	public List<Parameter> parameters;
	public List<ParameterPanel> parameterPanels;
	public int solverId;

	public PanelListManager panelListManager;
	public JTextArea console;
	public PrintStream stream;
	public JButton stopButton,runButton,saveButton;
	
	public boolean isSolverRunning = false;
	public Thread solverThread;
	public int solverID = -1;

	public ParameterDetailPanel(String title,ISolver solver,List<Parameter> parameters,ParameterNode sourceNode){
		this.sourceNode = sourceNode;
		this.title = title;
		this.solver = solver;
		this.parameters = parameters;
		this.parameterPanels = new ArrayList<ParameterPanel>();
		initPanel();
		makePanel();
		for(Parameter parm:parameters){
			addParameter(parm);
		}
	}

	public void runSolver(){
		///ソルバーを実行する、いろいろやる
		if(isSolverRunning)return;
		List<Parameter> useParameters = new ArrayList<Parameter>();
		for(ParameterPanel parameterPanel : parameterPanels){
			Parameter into = parameterPanel.getUpdatedParameter();
			if(into == null)return;
			useParameters.add(into);
		}
		int solverID = new Random().nextInt();
		solverThread = ProblemReader.runSolver(solver,solverID,useParameters,stream);
		if(solverThread == null)return;
		isSolverRunning = true;
		this.solverId = solverID;
		SolverProgressEvent.addListener(solverID, sourceNode);
		runButton.setEnabled(false);
	}
	
	public void stopSolver(){
		solverThread.stop();
		isSolverRunning = false;
		runButton.setEnabled(true);
	}
	
	public boolean saveParameter(){
		///パラメータを全部保存する、ただし入力ミスってる可能性もあるのでチェックを入れる
		List<Parameter> tmp = new ArrayList<Parameter>();
		for(ParameterPanel parameterPanel : parameterPanels){
			Parameter into = parameterPanel.getUpdatedParameter();
			if(into == null)return false;
			try {
				tmp.add(ParameterNode.getCopyParameter(into));
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		this.parameters.clear();
		for(Parameter parameter : tmp){
			this.parameters.add(parameter);
		}
		System.out.println("RENRAKU");
		return sourceNode.save();
	}

	public void addParameter(Parameter parameter){
		ParameterPanel panel = new ParameterPanel(parameter);
		panelListManager.addPanel(panel);
		parameterPanels.add(panel);
	}

	public void makePanel(){
		panelListManager = new PanelListManager(WIDTH, PARAMETER_AREA_HEIGHT, false,this);
		panelListManager.setPreferredSize(new Dimension(WIDTH, PARAMETER_AREA_HEIGHT));
		
		console = new JTextArea();
		JScrollPane conscroll = new JScrollPane(console);
		conscroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		conscroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		conscroll.getVerticalScrollBar().setUnitIncrement(10);
		console.setFont(new Font("Arial", Font.PLAIN, 11));
		console.setLineWrap(true);
		conscroll.setPreferredSize(new Dimension(WIDTH, CONSOLE_AREA_HEIGHT));
		this.stream = new PrintStream(new JTextAreaStream(console), true);
		
		stopButton = new JButton("STOP");
		runButton = new JButton("RUN");
		saveButton = new JButton("SAVE");
		stopButton.addActionListener(this);
		runButton.addActionListener(this);
		saveButton.addActionListener(this);
		stopButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		runButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		saveButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		
		this.add(panelListManager);
		this.add(conscroll);
		this.add(saveButton);
		this.add(stopButton);
		this.add(runButton);
	}

	public void initPanel(){
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setLayout(new FlowLayout());
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource().equals(runButton)){
			runSolver();
		}else if(event.getSource().equals(stopButton)){
			stopSolver();
		}else if(event.getSource().equals(saveButton)){
			boolean result = saveParameter();
			if(result){
				saveButton.setForeground(Color.GREEN);
			}else{
				saveButton.setForeground(Color.RED);
			}
		}
	}

}
