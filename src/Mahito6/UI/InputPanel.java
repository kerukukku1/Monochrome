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

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
	public static JButton loadButton, clearButton, addButton, saveButton, scanButton;
	public JTextField inputForm;
//	public ButtonGroup switType = new ButtonGroup();
//	private JRadioButton piece;
//	private JRadioButton frame;
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
		inputForm.setBounds(0, 0, width-400, height);
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

		clearButton = new JButton("Clear");
		clearButton.setBounds(width-80, 0, 80, height);
		clearButton.addActionListener(this);
		this.add(clearButton);

		scanButton = new JButton("Scan");
		scanButton.setBounds(width-400, 0, 80, height);
		scanButton.addActionListener(this);
		this.add(scanButton);
	}

	private void launchItems(){
//		setRadioButton();
		setButtons();
		setInputForm();
	}

	private void setUtils(){
		this.setLayout(null);
		this.setBackground(Color.RED.darker().darker());
		this.setBounds(x, y, width, height);
		this.setOpaque(true);
	}

	private boolean LoadFiles(String path){
		if(!ProblemManager.imageManager.setPath(path)){
			JOptionPane.showMessageDialog(this, "Wrong Path", "WARNING_MESSAGE", 
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		System.out.println("Load Files");
		return true;
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
			FolderManager.questSave();
			FolderManager.indexSave();
		}else if(cmd.equals("Add")){
			if(!LoadFiles(inputForm.getText()))return;
		    String selectvalues[] = {"Piece1", "Piece2", "Frame", "Cancel"};

		    int select = JOptionPane.showOptionDialog(this,
		      "Select Type : ",
		      "!!!Warning!!!",
		      JOptionPane.YES_NO_OPTION,
		      JOptionPane.QUESTION_MESSAGE,
		      null,
		      selectvalues,
		      selectvalues[0]
		    );
		    if (select == JOptionPane.CLOSED_OPTION || selectvalues[select].equals("Cancel")){
		    	return;
		    }else{
		    	if(selectvalues[select].equals("Piece1") || selectvalues[select].equals("Piece2")){
		    		stateChangePiece(select);
//		    		piece.setSelected(true);
		    	}else{
		    		stateChangeFrame(select);
//		    		frame.setSelected(true);
		    	}
		    }
			System.out.println("All Noise Clear");
			long start = System.nanoTime();
			InputPanel.loadButton.setEnabled(false);
			InputPanel.clearButton.setEnabled(false);
			ProblemManager.generatePieceDatas();
			InputPanel.loadButton.setEnabled(true);
			InputPanel.clearButton.setEnabled(true);
			long end = System.nanoTime();
			System.out.println((end - start) / 1000000f + "ms");
		}else if(cmd.equals("Load")){
			LoadFiles(inputForm.getText());
		}else if(cmd.equals("Clear")){
			//runの時は保持されているproblemデータを全てリセット
		    int option = JOptionPane.showConfirmDialog(this, "Clear Problem?",
		    	      "!!!Warning!!!", JOptionPane.YES_NO_OPTION,
		    	      JOptionPane.WARNING_MESSAGE);
		    if(option != JOptionPane.YES_OPTION){
		    	return;
		    }
			ProblemManager.resetImageManager();
			Mahito6.Main.Main.pieceView.initializePanel();
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

//	private void setRadioButton(){
//		piece = new JRadioButton("Piece");
//		frame = new JRadioButton("Frame");
//		piece.setOpaque(false);
//		frame.setOpaque(false);
//
//
//		piece.setBounds(width-480, 0, 80, height);
//		frame.setBounds(width-400, 0, 80, height);
//
//		piece.setForeground(Color.red);
//		frame.setForeground(Color.red);
//
//		piece.addChangeListener(this);
//		frame.addChangeListener(this);
//
//		piece.setSelected(true);
//
//		switType.add(piece);
//		switType.add(frame);
//
//		this.add(piece);
//		this.add(frame);
//	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
//		if(piece.isSelected()){
//			stateChangePiece();
//		}else{
//			stateChangeFrame();
//		}
	}

	private void stateChangeFrame(int type) {
		ProblemManager.setSwitchType(type);
		SolverConstants consts = ProblemManager.getConstants();
		consts.edgeWidth = 6;
		consts.lrAddition = 100;

		Constants.clearNoiseThreshold = 1200;
		Constants.dividePixelLookingForDist = 1;
		Constants.modeWaku = true;
	}

	private void stateChangePiece(int type){
		ProblemManager.setSwitchType(type);
		SolverConstants consts = ProblemManager.getConstants();
		consts.edgeWidth = 6;
		//consts.lrAddition = 50;
		consts.lrAddition = 60;

		Constants.dividePixelLookingForDist = 20;
		Constants.clearNoiseThreshold = 200;
		Constants.modeWaku = false;
	}
}
