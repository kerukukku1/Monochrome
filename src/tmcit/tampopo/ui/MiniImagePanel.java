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
	
	public Answer answer;
	public int IMAGESIZE;
	public JLabel imageLabel;
	
	public MiniImagePanel(Answer answer,int IMAGESIZE){
		this.answer = answer;
		this.IMAGESIZE = IMAGESIZE;
		initPanel();
		makePanel();
	}
	
	public void makePanel(){
		imageLabel = new JLabel();
		PuzzleImage puzzleImage = new PuzzleImage(answer.frames, answer.pieces);
		puzzleImage.paint(IMAGESIZE-3, false, false, false, false, false, false);
		imageLabel.setIcon(new ImageIcon(puzzleImage.getImage()));
		imageLabel.setBounds(2, 2, IMAGESIZE - 4, IMAGESIZE - 4);
		this.add(imageLabel);
	}
	
	public void initPanel(){
		this.setLayout(null);
		this.setPreferredSize(new Dimension(IMAGESIZE, IMAGESIZE));
		this.setBorder(new LineBorder(Color.GRAY, 2, true));
	}

}
