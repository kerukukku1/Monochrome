package tmcit.tampopo.ui.util;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.TransferHandler;

import tmcit.procon27.main.Solver;
import tmcit.tampopo.ui.SolverPanel;
import tmcit.tampopo.util.Problem;
import tmcit.tampopo.util.ProblemReader;

public class DropFileHandler extends TransferHandler{
	public SolverPanel solverPanel;

	public DropFileHandler(SolverPanel solverPanel){
		this.solverPanel = solverPanel;
	}
	
	public void dropProblemTextFile(File file){
		ProblemReader reader = new ProblemReader(file);
		Problem problem;
		try {
			problem = reader.load();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		System.out.println(file.getAbsolutePath());
		if(problem == null)return;
		solverPanel.setProblem(problem);
	}

	@Override
    public boolean canImport(TransferSupport support) {
		if(!support.isDrop()){
			return false;
		}
        return true;
    }

	@Override
	public boolean importData(TransferSupport support){
		if(!canImport(support)){
			return false;
		}
		Transferable t = support.getTransferable();
		try {
			@SuppressWarnings("unchecked")
			List<File> files = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
			if(files.size()==1){
				File file = files.get(0);
				if(isText(file)){
					dropProblemTextFile(file);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean isText(File file){
		String fileName = file.getName();
		if(fileName.indexOf(".txt")!=-1){
			return true;
		}
		return false;
	}
}
