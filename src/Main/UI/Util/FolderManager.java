package Main.UI.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FolderManager {
	public static final String currentPath = System.getProperty("user.home")+File.separator+"procon27" + File.separator;
	public static final String presetsPath = currentPath + File.separator + "presets" + File.separator;
	public FolderManager(){}
	
	public void buildDirectory(){
		buildCurrent();
		buildPresets();
	}

	private void buildCurrent(){ 
		if(!new File(currentPath).exists()){
			if(new File(currentPath).mkdir()){
				System.out.println("mkdir " + currentPath);
			}else{
				System.out.println("permission denied");
			}
		}		
	}
	
	private void buildPresets() {
		if(!new File(presetsPath).exists()){
			if(new File(presetsPath).mkdir()){
				System.out.println("mkdir " + presetsPath);
			}else{
				System.out.println("permission denied");
			}
		}		
	}
	
	public static File getPresetFile(String filename){
		String preset = presetsPath + filename;
		return new File(preset);
	}
	
	public static List<String> getPresetNames(){
		File dir = new File(presetsPath);
		List<String> ret = new ArrayList<String>();
	    File[] files = dir.listFiles();
	    for (int i = 0; i < files.length; i++) {
	    	if(!files[i].getName().matches(".*" + ".procon27" + ".*"))continue;
	        ret.add(files[i].getName());
	    }
	    return ret;
	}
}
