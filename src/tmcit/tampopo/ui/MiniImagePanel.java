package tmcit.tampopo.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import tmcit.tampopo.util.Answer;
import tmcit.tampopo.util.PuzzleImage;

public class MiniImagePanel extends JPanel{
	
	public MiniListPanel source;
	public int index;
	public int IMAGESIZE;
	public Answer answer;
	public JLabel imageLabel;
	
	public MiniImagePanel(Answer answer,int index,int IMAGESIZE,MiniListPanel source){
		this.source = source;
		this.answer = answer;
		this.index = index;
		this.IMAGESIZE = IMAGESIZE;
		initPanel();
//		showImage();
	}
	
	public String getTabTitle(){
		///タブに追加するときのタイトルをここで決める、難しい
		String ret = "(" + (index+1) + "/" + source.miniPanelNum + ")";
		return ret;
	}
	
	public void showImage(){
		if(imageLabel != null)return;
		imageLabel = new JLabel();
		PuzzleImage puzzleImage = new PuzzleImage(answer.frames, answer.pieces);
		puzzleImage.paint(IMAGESIZE-3, false, false, false, false, false, false);
		imageLabel.setIcon(new ImageIcon(puzzleImage.getImage()));
		imageLabel.setBounds(2, 2, IMAGESIZE - 4, IMAGESIZE - 4);
		this.add(imageLabel);
	}
	
	public void hideImage(){
		if(imageLabel == null)return;
		this.remove(imageLabel);
		imageLabel = null;
	}
	
	public void initPanel(){
		this.setLayout(null);
		this.setPreferredSize(new Dimension(IMAGESIZE, IMAGESIZE));
		this.setBorder(new LineBorder(Color.GRAY, 2, true));
	}

}
