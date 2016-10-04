package tmcit.tampopo.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

import tmcit.tampopo.edgeSolver.solver.util.Puzzle;
import tmcit.tampopo.geometry.util.Piece;
import tmcit.tampopo.ui.util.ExVertex;
import tmcit.tampopo.util.Answer;
import tmcit.tampopo.util.PuzzleImage;

public class BigImagePanel extends JPanel implements ChangeListener , MouseListener{
	
	public static final int IMAGESIZE = SolverPanel.CENTER_BIGIMAGE_SIZE;
	public static final Color backGround = new Color(230, 230, 240);
	
	public DetailPanel detailPanel;
	public String title;
	public Answer answer;
	public JLabel imageLabel;
	public PuzzleImage puzzleImage;
	
	public int selectedFrame = -1;
	public int selectedVertex = -1;
	
	public JCheckBox cb1,cb2,cb3,cb4,cb5,cb6;
	
	public static boolean frameDegree = false
				  ,frameLength = true
				  ,pieceDegree = false
				  ,pieceLength = false
				  ,frameIndex = false
				  ,pieceIndex = false;
	
	public BigImagePanel(String title,Answer answer, DetailPanel detailPanel){
		this.detailPanel = detailPanel;
		this.title = title;
		this.answer = answer;
		initPanel();
		makePanel();
		imageReload();
	}
	
	public void imageReload(){
		puzzleImage = new PuzzleImage(answer.frames, answer.pieces);
		puzzleImage.paint(IMAGESIZE, frameDegree, frameLength, pieceDegree, pieceLength, frameIndex, pieceIndex,selectedFrame,selectedVertex);
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
		this.selectedFrame = exVertex.frame;
		this.selectedVertex = exVertex.index;
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
		this.add(imageLabel);
		
		JLabel label1 = new JLabel("                                                                                       Frame:");
		cb1 = new JCheckBox("Degree", frameDegree);
		cb2 = new JCheckBox("Length", frameLength);
		cb3 = new JCheckBox("Index", frameIndex);
		JLabel label2 = new JLabel("                                                                                         Piece:");
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
