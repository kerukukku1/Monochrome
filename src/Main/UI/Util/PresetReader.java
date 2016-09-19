package Main.UI.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Mahito6.Main.SolverConstants;
import Mahito6.UI.InputParamPanel;

public class PresetReader {
	private File source;
	private SolverConstants consts;
	private HashMap<String, Object> memo;
	public PresetReader(File source){
		this.source = source;
		consts = new SolverConstants();
		createHash();
	}
	
	public SolverConstants getSolverConsts() throws IllegalArgumentException, IllegalAccessException{
        for (Field field : consts.getClass().getDeclaredFields()) {
        	String title = field.getName();
        	if(memo.containsKey(title)){
				field.set(consts, memo.get(title));
        	}
        }
		return consts;
	}
	
	private void createHash(){
		memo = new HashMap<String, Object>();
		List<String> list = new ArrayList<String>();
		try {
			list = readTxt(source);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(String tar : list){
			String[] spl = tar.split(":");
			Object obj = new Object();
			if(spl[0].equals("int")){
				obj = (Object)Integer.valueOf(spl[2]);
			}else if(spl[0].equals("double")){
				obj = (Object)Double.valueOf(spl[2]);				
			}
			memo.put(spl[1], obj);
		}
	}
	
	private List<String> readTxt(File file) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(file));
		List<String> in = new ArrayList<String>();
		String str = br.readLine();
		while(str != null){in.add(str);str = br.readLine();}
		br.close();
		return in;
	}
}
