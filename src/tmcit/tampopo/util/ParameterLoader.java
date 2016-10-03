package tmcit.tampopo.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import Mahito6.Main.Main;
import tmcit.api.ISolver;
import tmcit.api.Parameter;
import tmcit.api.Parameter.ParameterBuilder;
import tmcit.api.Parameter.ValueType;
import tmcit.tampopo.edgeSolver.main.BeamEdgeMain;
import tmcit.tampopo.ui.SolverDetailPanel;
import tmcit.tampopo.ui.util.FolderNode;
import tmcit.tampopo.ui.util.ParameterNode;

public class ParameterLoader {
	
	public static String[] solvers = {"EdgeSolver","RotSolver"};
	
	public static void deleteParameter(ParameterNode node){
		String fileName = node.getTitle();
		FolderNode parant = (FolderNode) node.getParent();
		while(true){
			fileName = parant.getTitle() +"."+fileName;
			if(parant.getParent() == null)break;
			parant = (FolderNode) parant.getParent();
		}
		File dir = new File(tmcit.tampopo.main.Main.saveDir+"/"+"Parameters");
		if(!dir.exists())dir.mkdirs();
		File file = new File(tmcit.tampopo.main.Main.saveDir+"/"+"Parameters"+"/"+fileName);
		file.delete();
	}

	public static boolean saveParameter(ParameterNode node){
		try{
		// TODO Auto-generated method stub
		String fileName = node.getTitle();
		FolderNode parant = (FolderNode) node.getParent();
		while(true){
			fileName = parant.getTitle() +"."+fileName;
			if(parant.getParent() == null)break;
			parant = (FolderNode) parant.getParent();
		}
		String text = "";
		
		text += getTitleFromSolver(node.getSolver()) + "\n";
		for(Parameter parameter : node.getParameters()){
			text += parameterToString(parameter) + "\n";
		}
		File dir = new File(tmcit.tampopo.main.Main.saveDir+"/"+"Parameters");
		if(!dir.exists())dir.mkdirs();
		File file = new File(tmcit.tampopo.main.Main.saveDir+"/"+"Parameters"+"/"+fileName);
		if(!file.exists())file.createNewFile();
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write(text);
		fileWriter.close();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void loadAllParameter(FolderNode root) throws Exception{
		File dir = new File(tmcit.tampopo.main.Main.saveDir+"/"+"Parameters/");
		if(!dir.exists())dir.mkdirs();
		File[] files = dir.listFiles();
		for(File file : files){
			Scanner scan = new Scanner(file);
			String solverName = scan.nextLine();
			ISolver solver = getISolverInstance(solverName);
			List<Parameter> parameters = new ArrayList<Parameter>();
			String line;
			while(scan.hasNextLine()){
				line = scan.nextLine();
				parameters.add(stringToParameter(line));
			}
			scan.close();
			String[] route = file.getName().split("\\.");
			FolderNode parant = root;
			for(int i = 1;i < route.length - 1;i++){
				String name = route[i];
				FolderNode next = getChild(parant, name);
				if(next == null){
					next = new FolderNode(name);
					parant.add(next);
				}
				parant = next;
			}
			String title = route[route.length - 1];
			parameters = parameterMix(parameters, solver.getParameters());
			parant.add(new ParameterNode(title, solver, parameters));
		}
	}
	private static List<Parameter> parameterMix(List<Parameter> source, List<Parameter> add){
		List<Parameter> parameters = new ArrayList<Parameter>();
		for(Parameter p1 : source){
			///sourceに入っててaddに入ってないものは削除
			boolean found = false;
			for(Parameter p2 : add){
				if(p1.name.equals(p2.name) && p1.valueType.equals(p2.valueType)){
					found = true;
					break;
				}
			}
			if(found){
				parameters.add(p1);
			}
		}
		for(Parameter p1 : add){
			///addに入っててsourceに入ってないものは新規追加
			boolean found = false;
			for(Parameter p2 : source){
				if(p1.name.equals(p2.name) && p1.valueType.equals(p2.valueType)){
					found = true;
					break;
				}
			}
			if(!found){
				parameters.add(p1);
			}
		}
		return parameters;
	}
	private static FolderNode getChild(FolderNode node,String name){
		///nodeがnameという名前のFolderNodeを持っているか？
		int num = node.getChildCount();
		for(int i = 0;i < num;i++){
			if(!(node.getChildAt(i) instanceof FolderNode))continue;
			FolderNode folderNode = (FolderNode) node.getChildAt(i);
			if(folderNode.getTitle().equals(name))return folderNode;
		}
		return null;
	}
	public static String getTitleFromSolver(ISolver solver){
		if(solver instanceof tmcit.tampopo.edgeSolver.main.BeamEdgeMain){
			return "EdgeSolver";
		}else if(solver instanceof tmcit.procon27.main.RotSolverMain){
			return "RotSolver";
		}else{
			return null;
		}
	}
	
	public static String parameterToString(Parameter parameter){
		String ret = "";
		String name = parameter.name;
		String notes = parameter.notes;
		String valueType = parameter.valueType.toString();
		String valueString = "";
		Object value = parameter.value;
		if(value instanceof Integer){
			valueString = String.valueOf((int)value);
		}else if(value instanceof Double){
			valueString = String.valueOf((double)value);
		}else if(value instanceof Boolean){
			valueString = String.valueOf((boolean)value);
		}else if(value instanceof String){
			valueString = (String)value;
		}
		if(notes.equals(""))notes = "null";
		ret += name+"<S>"+valueType+"<S>"+valueString+"<S>"+notes;
		return ret;
	}
	
	public static Parameter stringToParameter(String string){
		String[] split = string.split("<S>");
		String name = split[0];
		String notes = split[3];
		ValueType valueType = ValueType.valueOf(split[1]);
		String valueString = split[2];
		Object value;
		if(valueType == ValueType.Integer){
			value = Integer.parseInt(valueString);
		}else if(valueType == ValueType.Double){
			value = Double.parseDouble(valueString);
		}else if(valueType == ValueType.Boolean){
			value = Boolean.parseBoolean(valueString);
		}else if(valueType == ValueType.String){
			value = valueString;
		}else{
			return null;
		}
		if(notes.equals("null"))notes = "";
		ParameterBuilder parameterBuilder = new ParameterBuilder();
		try {
			return parameterBuilder.setName(name)
							.setValueType(valueType)
							.setDefaultValue(value)
							.setNotes(notes).build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ISolver getISolverInstance(String solverName){
		if(solverName.equals("EdgeSolver")){
			BeamEdgeMain solver = new tmcit.tampopo.edgeSolver.main.BeamEdgeMain();
			solver.getParameters();
			return solver;
		}else if(solverName.equals("RotSolver")){
			return new tmcit.procon27.main.RotSolverMain();
		}else{
			return null;
		}
	}
}
