package Mahito6.UI;
import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Mahito6.Main.Constants;
import Mahito6.Main.ProblemManager;
import Main.UI.Util.ImageManager;
import Main.UI.Util.MyKeyListener;

public class InputPanel extends JPanel implements ActionListener, ChangeListener{
	public int x, y, width, height;
	public static JButton loadButton, runButton;
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
		inputForm.setBounds(0, 0, width-320, height);
		inputForm.setTransferHandler(new DropFileHandler());
		this.add(inputForm);		
	}
	
	private void setLoadButton(){
		loadButton = new JButton("Load");
		loadButton.setBounds(width-160, 0, 80, height);
		loadButton.addActionListener(this);
		this.add(loadButton);
		
		runButton = new JButton("Run");
		runButton.setBounds(width-80, 0, 80, height);
		runButton.addActionListener(this);
		this.add(runButton);		
	}
	
	private void launchItems(){
		setRadioButton();
		setLoadButton();
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
		if(cmd.equals("Load")){
			LoadFiles(inputForm.getText());	
		}else if(cmd.equals("Run")){
			System.out.println("All Noise Clear");
			long start = System.nanoTime();
			InputPanel.loadButton.setEnabled(false);
			InputPanel.runButton.setEnabled(false);
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
		
		
		piece.setBounds(width-240, 0, 80, height);
		frame.setBounds(width-320, 0, 80, height);
		
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
		if(piece.isSelected()){
			Constants.clearNoiseThreshold = 200;
			Constants.edgeWidth = 6;
			//Constants.lrAddition = 50;
			Constants.lrAddition = 60;
			Constants.dividePixelLookingForDist = 20;
			Constants.modeWaku = false;
		}else{
			Constants.clearNoiseThreshold = 1200;
			Constants.edgeWidth = 6;
			Constants.lrAddition = 100;
			Constants.dividePixelLookingForDist = 3;
			Constants.modeWaku = true;
		}
	}

}
