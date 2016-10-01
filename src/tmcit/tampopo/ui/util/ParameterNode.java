package tmcit.tampopo.ui.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.tree.DefaultMutableTreeNode;

import tmcit.api.ISolver;
import tmcit.api.Parameter;
import tmcit.api.Parameter.ValueType;
import tmcit.tampopo.ui.ParameterDetailPanel;
import tmcit.tampopo.ui.SolverDetailPanel;
import tmcit.tampopo.util.IconUtil;
import tmcit.tampopo.util.ParameterLoader;

public class ParameterNode extends DefaultMutableTreeNode{
	
	private String title;
	private ISolver solver;
	private List<Parameter> parameters;
	private ParameterDetailPanel panel = null;
	
	public ParameterNode(String title,ISolver solver){
		super(title);
		this.title = title;
		this.solver = solver;
		this.parameters = getParametersCopy(solver.getParameters());
//		showDetail();
	}
	
	public ParameterNode(String title,ISolver solver,List<Parameter> parameters){
		///こっちはすでに保存したパラメータから読み込む場合
		super(title);
		this.title = title;
		this.solver = solver;
		this.parameters = getParametersCopy(parameters);
	}
	
	public boolean save(){
		///現在のパラメータをすべてテキストに書き出す
		return ParameterLoader.saveParameter(this);
	}
	
	public String getTitle(){
		return title;
	}
	public ISolver getSolver(){
		return solver;
	}
	public List<Parameter> getParameters(){
		return parameters;
	}
	
	private List<Parameter> getParametersCopy(List<Parameter> source){
		try{
		List<Parameter> ret = new ArrayList<Parameter>();
		for(Parameter parm : source){
			ret.add(getCopyParameter(parm));
		}
		return ret;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static Parameter getCopyParameter(Parameter parm) throws Exception{
		Object copyValue = getCopy(parm.valueType, parm.value);
		Parameter copy = new tmcit.api.Parameter.ParameterBuilder()
				.setName(parm.name)///readonly
				.setDefaultValue(copyValue)///コピーしないとまずい
				.setValueType(parm.valueType)///readonly
				.setNotes(parm.notes)///readonly
				.build();
		return copy;
	}
	
	private static Object getCopy(ValueType valueType,Object value){
		Object ret = null;
		if(valueType == ValueType.Integer){
			ret = new Integer((int)value).intValue();
		}else if(valueType == ValueType.Double){
			ret = new Double((double)value).doubleValue();
		}else if(valueType == ValueType.Boolean){
			ret = new Boolean((boolean)value).booleanValue();
		}else if(valueType == ValueType.String){
			ret = new String((String)value);
		}
		return ret;
	}
	
	public void showDetail(SolverDetailPanel tabPane){
		if(panel == null)panel = new ParameterDetailPanel(title,solver,parameters,this);
		if(tabPane.indexOfComponent(panel) != -1){
			///既に表示されているので移動
			tabPane.setSelectedComponent(panel);
			return;
		}
		tabPane.addTab(title,IconUtil.getSolverIcon(solver),panel);
		tabPane.setSelectedComponent(panel);
	}

}
