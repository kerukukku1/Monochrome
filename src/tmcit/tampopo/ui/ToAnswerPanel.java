package tmcit.tampopo.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import tmcit.tampopo.geometry.util.Piece;
import tmcit.tampopo.geometry.util.Point;
import tmcit.tampopo.util.Answer;
import tmcit.tampopo.util.Problem;
import tmcit.tampopo.util.PuzzleImage;

public class ToAnswerPanel extends JPanel implements MouseListener, MouseMotionListener{
	
	public static final int BUG_OFFSET = 300;///300,300ずらさないとだめ
	public static final int CIRCLE_OFFSET = 30;///円を大き目に出す
	
	public static final int IMAGESIZE = MainUI.MAINTABPANE_HEIGHT - 30;
	public static final int RIGHTWIDTH = 300;///右側の幅のこと
	public static final Color backGround = new Color(230, 230, 240);
	public static final Color pieceLightColor = new Color(160, 160, 230);
	public static final Color pieceDarkColor = Color.GRAY;
	
	public Problem problem;
	public Answer answer;
	public JLabel imageLabel;
	public JLabel pieceMapLabel;
	public JLabel indexLabel;
	public PuzzleImage puzzleImage;
	public BufferedImage pieceMapSouce;
	public double bias;
	public int dw,dh;///右側の画像の大きさ
	
	public Piece targetPiece;
	
	public JCheckBox cb1,cb2,cb3,cb4,cb5,cb6;
	
	public boolean frameDegree = false
				  ,frameLength = false
				  ,pieceDegree = false
				  ,pieceLength = false
				  ,frameIndex = false
				  ,pieceIndex = false;
	public boolean[] isPiece2 = new boolean[50];
	public Piece[] realPiece2 = new Piece[50];
	
	public ToAnswerPanel(Problem problem,Answer answer){
		this.problem = problem;
		this.answer = answer;
		initAnswer();
		initPanel();
		makePanel();
		imageReload();
	}
	
	public void imageReload(){
		puzzleImage = new PuzzleImage(answer.frames, answer.pieces);
		puzzleImage.paint(IMAGESIZE, frameDegree, frameLength, pieceDegree, pieceLength, frameIndex, pieceIndex);
		BufferedImage bigImage = puzzleImage.getImage();
		imageLabel.setIcon(new ImageIcon(bigImage));
		this.repaint();
	}
	
	public void changeTargetPiece(){
		int id = targetPiece.getID();
		indexLabel.setText((id+1)+"");
		Piece realPiece = null;
		if(isPiece2[id]){
			realPiece = realPiece2[id];
		}
		
		BufferedImage overMap = new BufferedImage(dw, dh, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = overMap.createGraphics();
		g2d.drawImage(pieceMapSouce, null, 0, 0);
		if(realPiece != null){
			drawCircleImage(g2d,realPiece);
		}
		g2d.dispose();
		pieceMapLabel.setIcon(new ImageIcon(overMap));
		this.repaint();
	}
	
	public void drawCircleImage(Graphics2D g2d,Piece realPiece){
		int minx = 100000,miny = 100000;
		int maxx = 0,maxy = 0;
		for(Point point : realPiece.getPoints()){
			int x = (int)((point.x-BUG_OFFSET) * bias);
			int y = (int)((point.y-BUG_OFFSET) * bias);
			minx = Math.min(minx, x);
			miny = Math.min(miny, y);
			maxx = Math.max(maxx, x);
			maxy = Math.max(maxy, y);
		}
		minx -= CIRCLE_OFFSET;
		miny -= CIRCLE_OFFSET;
		maxx += CIRCLE_OFFSET;
		maxy += CIRCLE_OFFSET;
		int width = maxx-minx;
		int height = maxy-miny;
		g2d.setStroke(new BasicStroke(4.0f));
		g2d.drawOval(minx, miny, width, height);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		///ピース左クリックしたら色変える
		if(answer.frames.size() == 0)return;///枠がある解のみ
		Piece piece = puzzleImage.getPieceFromPoint(e.getX(), e.getY());
		if(piece == null)return;
		if(e.getButton() == 1){
			piece.setPieceColor(pieceDarkColor);
		}else if(e.getButton() == 3){
			piece.setPieceColor(pieceLightColor);
		}
		imageReload();
	}
	@Override
	public void mouseMoved(MouseEvent event) {
		Piece piece = puzzleImage.getPieceFromPoint(event.getX(), event.getY());
		if(piece == null || targetPiece == piece)return;
		targetPiece = piece;
		changeTargetPiece();
	}

	
	public void makePanel(){
		imageLabel = new JLabel();
		imageLabel.setBorder(new LineBorder(Color.GRAY, 3, true));
		imageLabel.addMouseListener(this);
		imageLabel.addMouseMotionListener(this);
		imageLabel.setBounds(2, 2, IMAGESIZE, IMAGESIZE);
		
		BufferedImage sourceImage = problem.piece2Image;
		bias = (double)RIGHTWIDTH/(double)sourceImage.getWidth();
		dw = RIGHTWIDTH;
		dh = (int)(sourceImage.getHeight() * bias);
		pieceMapSouce = reduceImage(sourceImage, dw, dh);
		pieceMapLabel = new JLabel(new ImageIcon(pieceMapSouce));
		pieceMapLabel.setBorder(new LineBorder(Color.GRAY, 3, true));
		pieceMapLabel.setBounds(IMAGESIZE + 4, 2, dw, dh);
		
		indexLabel = new JLabel("");
		indexLabel.setBounds(IMAGESIZE + 4, dh + 4, RIGHTWIDTH, 220);
		indexLabel.setFont(new Font("Arial", Font.BOLD, 240));
		indexLabel.setHorizontalAlignment(JLabel.CENTER);
		indexLabel.setVerticalAlignment(JLabel.CENTER);
		
		this.add(imageLabel);
		this.add(pieceMapLabel);
		this.add(indexLabel);
	}
	
	public static BufferedImage reduceImage(BufferedImage image, int dw, int dh) {
		  BufferedImage thumb = new BufferedImage(dw, dh, image.getType());
		  thumb.getGraphics().drawImage(image.getScaledInstance(dw, dh, Image.SCALE_AREA_AVERAGING), 0, 0, dw, dh, null);
		  return thumb;
	}
	public void initAnswer(){
		for(int i = 0;i < 50;i++){
			isPiece2[i] = false;
			realPiece2[i] = null;
		}
		for(Piece realPiece : problem.realPieces){
			int id = realPiece.getID();
			isPiece2[id] = true;
			realPiece2[id] = realPiece;
		}
		for(Piece piece : answer.pieces){
			piece.setPieceColor(pieceLightColor);
		}
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

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}



}
