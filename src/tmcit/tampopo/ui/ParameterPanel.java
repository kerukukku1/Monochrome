package tmcit.tampopo.ui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tmcit.api.Parameter;
import tmcit.api.Parameter.ValueType;
import tmcit.tampopo.ui.util.ParameterNode;

public class ParameterPanel extends JPanel{
	
	public Parameter parameter;
	public JTextField valueField = null;
	public JCheckBox checkBox = null;
	
	public ParameterPanel(Parameter parameter){
		try {
			this.parameter = ParameterNode.getCopyParameter(parameter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		initPanel();
		makePanel();
	}
	
	public void updateParameter(){
		ValueType valueType = parameter.valueType;
		Object value = null;
		if(valueType == ValueType.Integer){
			value = Integer.parseInt(valueField.getText());
		}else if(valueType == ValueType.Double){
			value = Double.parseDouble(valueField.getText());
		}else if(valueType == ValueType.Boolean){
			value = checkBox.isSelected();
		}else if(valueType == ValueType.String){
			value = new String(valueField.getText());
		}
		parameter.value = value;
	}
	
	public Parameter getUpdatedParameter(){
		updateParameter();
		return parameter;
	}
	
	public void makePanel(){
		String parmName = parameter.name;
		ValueType valueType = parameter.valueType;
		Object value = parameter.value;
		String notes = parameter.notes;
		JLabel label = new JLabel(parmName);
		this.add(label);
		if(valueType == ValueType.Integer){
			valueField = new JTextField(""+(int)value);
			this.add(valueField);
		}else if(valueType == ValueType.Double){
			valueField = new JTextField(""+(double)value);
			this.add(valueField);
		}else if(valueType == ValueType.Boolean){
			checkBox = new JCheckBox("", (boolean)value);
			this.add(checkBox);
		}else if(valueType == ValueType.String){
			valueField = new JTextField(""+(String)value);
			this.add(valueField);
		}
		label.setToolTipText(notes);
	}
	
	public void initPanel(){
		this.setPreferredSize(new Dimension(ParameterDetailPanel.WIDTH - 14, ParameterDetailPanel.ONE_PARAMETER_HEIGHT));
		this.setLayout(new GridLayout());
	}

}
