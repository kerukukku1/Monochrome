package tmcit.tampopo.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import tmcit.tampopo.geometry.util.Piece;
import tmcit.tampopo.ui.util.ExVertex;
import tmcit.tampopo.util.Answer;
import tmcit.tampopo.util.PuzzleImage;

public class BigImagePanel extends JPanel implements ChangeListener , MouseListener{

	public static final int IMAGESIZE = SuperPanel.CENTER_BIGIMAGE_SIZE;
	public static final Color backGround = new Color(230, 230, 240);

	public String title;
	public Answer answer;
	public JLabel imageLabel;
	public PuzzleImage puzzleImage;

	public JCheckBox cb1,cb2,cb3,cb4,cb5,cb6;

	public static boolean frameDegree = false
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

	public int getSelectedFrameIndex(){
		return answer.targetFrameIndex;
	}
	public int getSelectedVertexIndex(){
		return answer.targetVertexIndex;
	}

	public void imageReload(){
		puzzleImage = new PuzzleImage(answer.frames, answer.pieces);
		puzzleImage.paint(IMAGESIZE, frameDegree, frameLength, pieceDegree, pieceLength, frameIndex, pieceIndex,getSelectedFrameIndex(),getSelectedVertexIndex());
		BufferedImage bigImage = puzzleImage.getImage();
		imageLabel.setIcon(new ImageIcon(bigImage));
		this.repaint();
		cb1.setSelected(frameDegree);
		cb2.setSelected(frameLength);
		cb3.setSelected(frameIndex);
		cb4.setSelected(pieceDegree);
		cb5.setSelected(pieceLength);
		cb6.setSelected(pieceIndex);
	}

	public void doClickVeretex(int x,int y){
		ExVertex exVertex = puzzleImage.getFrameAndVertexFromPoint(x, y);
		if(exVertex == null)return;
		answer.setTargetFrameIndex(exVertex.frame);
		answer.setTargetVertexIndex(exVertex.index);
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

	@Override
	public void mousePressed(MouseEvent e) {
		///ピース右クリックしたら消す
		if(e.getButton() == 3){
			if(answer.frames.size() == 0)return;///枠がある解のみ
			Piece piece = puzzleImage.getPieceFromPoint(e.getX(), e.getY());
			if(piece == null)return;
			answer.pieces.remove(piece);
			imageReload();
		}else if(e.getButton() == 1){
			///左クリックなら辺を選択
			doClickVeretex(e.getX(), e.getY());
			imageReload();
		}
	}

	public void makePanel(){
		imageLabel = new JLabel();
		imageLabel.setPreferredSize(new Dimension(IMAGESIZE, IMAGESIZE));
		imageLabel.setBorder(new LineBorder(Color.GRAY, 3, true));
		imageLabel.addMouseListener(this);
		imageLabel.setBounds(2, 2, IMAGESIZE, IMAGESIZE);
		this.add(imageLabel);

		JLabel pieceNumLabel = new JLabel(""+answer.pieces.size());
		pieceNumLabel.setFont(new Font("Arial", Font.BOLD, 60));
		pieceNumLabel.setHorizontalAlignment(JLabel.CENTER);
		pieceNumLabel.setVerticalAlignment(JLabel.CENTER);
		pieceNumLabel.setBounds(2, IMAGESIZE + 2, 100, 100);
		this.add(pieceNumLabel);

		JPanel checkboxPanel = new JPanel();
		checkboxPanel.setLayout(new GridLayout(2, 4));
		checkboxPanel.setOpaque(false);
		JLabel label1 = new JLabel("Frame:");
		cb1 = new JCheckBox("Degree", frameDegree);
		cb2 = new JCheckBox("Length", frameLength);
		cb3 = new JCheckBox("Index", frameIndex);
		JLabel label2 = new JLabel("Piece:");
		cb4 = new JCheckBox("Degree", pieceDegree);
		cb5 = new JCheckBox("Length", pieceLength);
		cb6 = new JCheckBox("Index", pieceIndex);
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

		checkboxPanel.add(label1);
		checkboxPanel.add(cb1);
		checkboxPanel.add(cb2);
		checkboxPanel.add(cb3);
		checkboxPanel.add(label2);
		checkboxPanel.add(cb4);
		checkboxPanel.add(cb5);
		checkboxPanel.add(cb6);
		checkboxPanel.setBounds(200, IMAGESIZE + 2, 300, 40);
		this.add(checkboxPanel);
	}

	public void initPanel(){
		this.setLayout(null);
		this.setBackground(backGround);
//		this.setFont(new Font("メイリオ", Font.PLAIN, 16));
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}



}
