package tmcit.tampopo.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import tmcit.tampopo.util.Answer;
import tmcit.tampopo.util.PuzzleImage;

public class BigImagePanel extends JPanel implements ChangeListener{
	
	public static final int IMAGESIZE = SolverPanel.CENTER_BIGIMAGE_SIZE;
	public static final Color backGround = new Color(230, 230, 240);
	public String title;
	public Answer answer;
	public JLabel imageLabel;
	
	public boolean frameDegree = false
				  ,frameLength = true
				  ,pieceDegree = false
				  ,pieceLength = false
				  ,frameIndex = false
				  ,pieceIndex = false;
	
	public BigImagePanel(String title,Answer answer){
		this.title = title;
		this.answer = answer;
		initPanel();
		makePanel();
		imageReload();
	}
	
	public void imageReload(){
		PuzzleImage puzzleImage = new PuzzleImage(answer.frames, answer.pieces);
		puzzleImage.paint(IMAGESIZE, frameDegree, frameLength, pieceDegree, pieceLength, frameIndex, pieceIndex);
		BufferedImage bigImage = puzzleImage.getImage();
		imageLabel.setIcon(new ImageIcon(bigImage));
		this.repaint();
	}
	
	@Override
	public void stateChanged(ChangeEvent event) {
		JCheckBox checkBox = (JCheckBox) event.getSource();
		String name = checkBox.getName();
		boolean next = checkBox.isSelected();
		switch (name) {
		case "frameDegree":
			frameDegree = next;
			break;
		case "frameLength":
			frameLength = next;
			break;
		case "frameIndex":
			frameIndex = next;
			break;
		case "pieceDegree":
			pieceDegree = next;
			break;
		case "pieceLength":
			pieceLength = next;
			break;
		case "pieceIndex":
			pieceIndex = next;
			break;
		default:
			break;
		}
		imageReload();
	}
	
	public void makePanel(){
		imageLabel = new JLabel();
		imageLabel.setPreferredSize(new Dimension(IMAGESIZE, IMAGESIZE));
		imageLabel.setBorder(new LineBorder(Color.GRAY, 3, true));
		this.add(imageLabel);
		
		JLabel label1 = new JLabel("                                                                                       Frame:");
		JCheckBox cb1 = new JCheckBox("Degree", frameDegree);
		JCheckBox cb2 = new JCheckBox("Length", frameLength);
		JCheckBox cb3 = new JCheckBox("Index", frameIndex);
		JLabel label2 = new JLabel("                                                                                         Piece:");
		JCheckBox cb4 = new JCheckBox("Degree", frameDegree);
		JCheckBox cb5 = new JCheckBox("Length", frameLength);
		JCheckBox cb6 = new JCheckBox("Index", frameIndex);
		cb1.setName("frameDegree");
		cb2.setName("frameLength");
		cb3.setName("frameIndex");
		cb4.setName("pieceDegree");
		cb5.setName("pieceLength");
		cb6.setName("pieceIndex");
		cb1.addChangeListener(this);
		cb2.addChangeListener(this);
		cb3.addChangeListener(this);
		cb4.addChangeListener(this);
		cb5.addChangeListener(this);
		cb6.addChangeListener(this);
		
		this.add(label1);
		this.add(cb1);
		this.add(cb2);
		this.add(cb3);
		this.add(label2);
		this.add(cb4);
		this.add(cb5);
		this.add(cb6);
	}
	
	public void initPanel(){
		this.setLayout(new FlowLayout());
		this.setBackground(backGround);
//		this.setFont(new Font("メイリオ", Font.PLAIN, 16));
	}



}
