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

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;

import Mahito6.Main.ProblemManager;
import Main.UI.Util.ImageManager;
import Main.UI.Util.MyKeyListener;

public class InputPanel extends JPanel implements ActionListener{
	public int x, y;
	public static JButton loadButton, runButton;
	public JTextField inputForm;
	public InputPanel(int x, int y){
		this.x = x;
		this.y = y;
		setUtils();
		launchItems();
		this.addKeyListener(new MyKeyListener());
	}

	private void setInputForm(){
		inputForm = new JTextField();
		inputForm.addActionListener(this);
		inputForm.setBounds(0, 0, 540, 30);
		inputForm.setTransferHandler(new DropFileHandler());
		this.add(inputForm);		
	}
	
	private void setLoadButton(){
		loadButton = new JButton("Load");
		loadButton.setBounds(540, 0, 80, 30);
		loadButton.addActionListener(this);
		this.add(loadButton);
		
		runButton = new JButton("Run");
		runButton.setBounds(620, 0, 80, 30);
		runButton.addActionListener(this);
		this.add(runButton);		
	}
	
	private void launchItems(){
		setLoadButton();
		setInputForm();
	}
	
	private void setUtils(){
		this.setLayout(null);
		this.setBackground(Color.RED.darker().darker());
		this.setBounds(x, y, 700, 30);
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
}
