package tmcit.tampopo.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border; 
import javax.swing.border.LineBorder;

import tmcit.tampopo.util.Answer;
import tmcit.tampopo.util.PuzzleImage;

public class DetailPanel extends JPanel implements MouseListener{
	///解の複数保持用にも使いますよ
	
	public static final int IMAGESIZE = SolverPanel.LEFT_IMAGE_SIZE;
	public static final int TITLE_OFFSET = 20;
	
	public SolverPanel source;
	public String title;
	public List<Answer> answers;///1個でもこっち使う
	
	public JLabel titleLabel;
	
	private boolean listOn = false;///中央のリストに追加されているか
	
	public DetailPanel(String title,Answer answer,SolverPanel source){
		this.source = source;
		this.answers = new ArrayList<Answer>();
		this.answers.add(answer);
		this.title = title;
		initPanel();
		makePanel();
		addLabels();
	}
	public DetailPanel(String title,List<Answer> answers,SolverPanel source){
		this.source = source;
		this.answers = answers;
		this.title = title;
		initPanel();
		makePanel();
		addLabels();
	}
	
	public boolean isListOn(){
		return listOn;
	}
	public void listOn(){
		listOn = true;
	}
	public void listOff(){
		listOn = false;
	}
	
	public void addLabels(){
		titleLabel = new JLabel(title);
		titleLabel.setBounds(2, 2, SolverPanel.LEFT_WIDTH - 20, TITLE_OFFSET);
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		titleLabel.addMouseListener(this);
		this.add(titleLabel);
	}
	
	public BufferedImage getImage(){
		///answerからサムネ作る、複数ある場合は数も入れる
		Answer answer = answers.get(0);
		PuzzleImage puzzleImage = new PuzzleImage(answer.frames, answer.pieces);
		puzzleImage.paint(IMAGESIZE-4, false, false, false, false, false, false,-1,-1);
		BufferedImage ret = puzzleImage.getImage();
		if(answers.size() != 1){
			Graphics2D graphics2d = ret.createGraphics();
			graphics2d.setFont(new Font("メイリオ", Font.PLAIN, 18));
			graphics2d.setColor(Color.WHITE);
			int keta = (int)Math.log10(answers.size())+1;
			graphics2d.drawString(""+answers.size(), IMAGESIZE - (keta*12) - 6, IMAGESIZE - 10);
		}
		return ret;
	}
	
	public void makePanel(){
		JLabel imgLabel = new JLabel();
		imgLabel.setIcon(new ImageIcon(getImage()));
		imgLabel.setBounds(2, 2 + TITLE_OFFSET, IMAGESIZE -4, IMAGESIZE -4);
		this.addMouseListener(source);
		this.add(imgLabel);
	}
	
	public void initPanel(){
		this.setBorder(new LineBorder(Color.GRAY));
		this.setLayout(null);
		int width = SolverPanel.LEFT_WIDTH;
		int height;
//		if(answers.size() == 1){
			height = IMAGESIZE + TITLE_OFFSET;
//		}else{
//			height = 60;
//		}
		this.setPreferredSize(new Dimension(width, height));
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if(title.equals("Problem")||title.equals("Master"))return;
		String newTitle = JOptionPane.showInputDialog("rename");
		if(newTitle == null)return;
		this.title = newTitle;
		titleLabel.setText(newTitle);
		this.repaint();
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
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
