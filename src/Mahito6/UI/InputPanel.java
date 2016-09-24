package Mahito6.UI;
import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Mahito6.Main.Constants;
import Mahito6.Main.Problem;
import Mahito6.Main.ProblemManager;
import Mahito6.Main.SolverConstants;
import Mahito6.Main.Tuple2;
import Main.UI.Util.Coordinates;
import Main.UI.Util.FolderManager;
import Main.UI.Util.MyKeyListener;

public class InputPanel extends JPanel implements ActionListener, ChangeListener{
	public int x, y, width, height;
	public static JButton loadButton, runButton, addButton, saveButton;
	public JTextField inputForm;
	public ButtonGroup switType = new ButtonGroup();
	private JRadioButton piece;
	private JRadioButton frame;
	public InputPanel(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		setUtils();
		launchItems();
		this.addKeyListener(new MyKeyListener());
	}

	private void setInputForm(){
		inputForm = new JTextField();
		inputForm.addActionListener(this);
		inputForm.setBounds(0, 0, width-480, height);
		inputForm.setTransferHandler(new DropFileHandler());
		this.add(inputForm);
	}

	private void setButtons(){
		saveButton = new JButton("Save");
		saveButton.setBounds(width-320, 0, 80, height);
		saveButton.addActionListener(this);
		this.add(saveButton);

		loadButton = new JButton("Load");
		loadButton.setBounds(width-240, 0, 80, height);
		loadButton.addActionListener(this);
		this.add(loadButton);

		addButton = new JButton("Add");
		addButton.setBounds(width-160, 0, 80, height);
		addButton.addActionListener(this);
		this.add(addButton);

		runButton = new JButton("Run");
		runButton.setBounds(width-80, 0, 80, height);
		runButton.addActionListener(this);
		this.add(runButton);
	}

	private void launchItems(){
		setRadioButton();
		setButtons();
		setInputForm();
	}

	private void setUtils(){
		this.setLayout(null);
		this.setBackground(Color.RED.darker().darker());
		this.setBounds(x, y, width, height);
		this.setOpaque(true);
	}

	private void LoadFiles(String path){
		if(!ProblemManager.imageManager.setPath(path)){
			inputForm.setText("Illegal Path");
			return;
		}
		System.out.println("Load Files");
	}


	private class DropFileHandler extends TransferHandler {
		@Override
		public boolean canImport(TransferSupport support) {
			if (!support.isDrop()) {
		        return false;
		    }

			if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
		        return false;
		    }

			return true;
		}

		@Override
		public boolean importData(TransferSupport support) {
			if (!canImport(support)) {
		        return false;
		    }

			Transferable t = support.getTransferable();
			try {
				List<File> files = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
				inputForm.setText(files.get(0).getAbsolutePath());
				LoadFiles(inputForm.getText());
			} catch (UnsupportedFlavorException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String cmd = e.getActionCommand();
		if(cmd.equals("Save")){
			List<Problem> problems = ProblemManager.getProblems();
			List<String> ansFrame = new ArrayList<String>();
			ansFrame.add("dummy");
			List<String> ansPiece = new ArrayList<String>();
			ansPiece.add("dummy");
			
			int frameSize = 0;
			int pieceSize = 0;
			
			for(int i = 0; i < problems.size(); i++){
				Problem p = problems.get(i);
				if(p.isWaku()){
					List<List<Tuple2<Double, Double>>> v = p.getVertex();
					for(int j = 0; j < v.size(); j++){
						List<Tuple2<Double, Double>> v2 = v.get(j);
						if(v2.size() < 3){
							//図形が構成されていない場合は除去
							continue;
						}
						Coordinates c = p.getCoord(j);
						ansFrame.add(String.valueOf(v2.size()));
						for(int k = 0; k < v2.size(); k++){
							double x = (v2.get(k).t1+c.minx) * (Constants.unitInch / ProblemManager.dpi);
							double y = (v2.get(k).t2+c.miny) * (Constants.unitInch / ProblemManager.dpi);
							ansFrame.add(String.valueOf(x) + " " + String.valueOf(y));
						}
						frameSize++;
					}
				}else{
					List<List<Tuple2<Double, Double>>> v = p.getVertex();
					for(int j = 0; j < v.size(); j++){
						List<Tuple2<Double, Double>> v2 = v.get(j);
						if(v2.size() < 3){
							//図形が構成されていない場合は除去
							continue;
						}
						ansPiece.add(String.valueOf(v2.size()));
						for(int k = 0; k < v2.size(); k++){
							double x = v2.get(k).t1 * (Constants.unitInch / ProblemManager.dpi);
							double y = v2.get(k).t2 * (Constants.unitInch / ProblemManager.dpi);
							ansPiece.add(String.valueOf(x) + " " + String.valueOf(y));
						}
						pieceSize++;
					}
				}
			}
			//dummyを個数に置き換え
			ansFrame.set(0, String.valueOf(frameSize));
			ansPiece.set(0, String.valueOf(pieceSize));
			FolderManager.fileSave(ansFrame, ansPiece);

		}else if(cmd.equals("Add")){
			System.out.println("All Noise Clear");
			long start = System.nanoTime();
			InputPanel.loadButton.setEnabled(false);
			InputPanel.runButton.setEnabled(false);
			ProblemManager.generatePieceDatas();
			InputPanel.loadButton.setEnabled(true);
			InputPanel.runButton.setEnabled(true);
			long end = System.nanoTime();
			System.out.println((end - start) / 1000000f + "ms");
		}else if(cmd.equals("Load")){
			LoadFiles(inputForm.getText());
		}else if(cmd.equals("Run")){
			System.out.println("All Noise Clear");
			long start = System.nanoTime();
			InputPanel.loadButton.setEnabled(false);
			InputPanel.runButton.setEnabled(false);
			//runの時は保持されているproblemデータを全てリセット
			ProblemManager.resetImageManager();
			ProblemManager.generatePieceDatas();
			InputPanel.loadButton.setEnabled(true);
			InputPanel.runButton.setEnabled(true);
			long end = System.nanoTime();
			System.out.println((end - start) / 1000000f + "ms");

//			new Thread(new Runnable(){
//				@Override
//				public void run(){
//					System.out.println("All Noise Clear");
//					long start = System.nanoTime();
//					InputPanel.loadButton.setEnabled(false);
//					InputPanel.runButton.setEnabled(false);
//					images.getPieces();
//					InputPanel.loadButton.setEnabled(true);
//					InputPanel.runButton.setEnabled(true);
//					long end = System.nanoTime();
//					System.out.println((end - start) / 1000000f + "ms");
//				}
//			}).start();
		}
	}

	private void setRadioButton(){
		piece = new JRadioButton("Piece");
		frame = new JRadioButton("Frame");
		piece.setOpaque(false);
		frame.setOpaque(false);


		piece.setBounds(width-480, 0, 80, height);
		frame.setBounds(width-400, 0, 80, height);

		piece.setForeground(Color.red);
		frame.setForeground(Color.red);

		piece.addChangeListener(this);
		frame.addChangeListener(this);

		piece.setSelected(true);

		switType.add(piece);
		switType.add(frame);

		this.add(piece);
		this.add(frame);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		SolverConstants consts = ProblemManager.getConstants();
		if(piece.isSelected()){
			consts.edgeWidth = 6;
			//consts.lrAddition = 50;
			consts.lrAddition = 60;

			Constants.dividePixelLookingForDist = 20;
			Constants.clearNoiseThreshold = 200;
			Constants.modeWaku = false;
		}else{
			consts.edgeWidth = 6;
			consts.lrAddition = 100;

			Constants.clearNoiseThreshold = 1200;
			Constants.dividePixelLookingForDist = 3;
			Constants.modeWaku = true;
		}
	}

}
