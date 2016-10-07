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
import javax.swing.border.LineBorder;

import tmcit.tampopo.util.Answer;
import tmcit.tampopo.util.PuzzleImage;

public class DetailPanel extends JPanel implements MouseListener{
	///解の複数保持用にも使いますよ

	public static final int IMAGESIZE = SuperPanel.RIGHT_IMAGE_SIZE;
	public static final int TITLE_OFFSET = 20;

	public SuperPanel source;
	public String title;
	public List<Answer> answers;///1個でもこっち使う

	public JLabel titleLabel;

	public DetailPanel(String title,Answer answer,SuperPanel source){
		this.source = source;
		this.answers = new ArrayList<Answer>();
		this.answers.add(answer);
		this.title = title;
		initPanel();
		makePanel();
		addLabels();
	}
	public DetailPanel(String title,List<Answer> answers,SuperPanel source){
		this.source = source;
		this.answers = answers;
		this.title = title;
		initPanel();
		makePanel();
		addLabels();
	}

	public void addLabels(){
		titleLabel = new JLabel(title);
		titleLabel.setBounds(2, 2, SuperPanel.RIGHT_WIDTH - 20, TITLE_OFFSET);
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
		this.addMouseListener(this);
		this.add(imgLabel);
	}

	public void initPanel(){
		this.setBorder(new LineBorder(Color.GRAY));
		this.setLayout(null);
		int width = SuperPanel.RIGHT_WIDTH;
		int height;
//		if(answers.size() == 1){
			height = IMAGESIZE + TITLE_OFFSET;
//		}else{
//			height = 60;
//		}
		this.setPreferredSize(new Dimension(width, height));
	}

	public void clickDetail(int button,boolean isCtrl,boolean isShift){
		///パネルをクリックしたときの動きをここに書く、右クリックで削除、左クリックで真ん中に表示
		if(button == 1){
			if(source.showBigImageOrMiniList(this)){
				///既に表示中
				return;
			}
			source.makeBigImageOrMiniList(this,isCtrl,isShift);
		}else if(button == 3){
			if(title.equals("Problem")||title.equals("Master"))return;
			source.removeDetailPanel(this);
		}
	}
	@Override
	public void mousePressed(MouseEvent event) {
		if(event.getSource() == this){
			clickDetail(event.getButton(),event.isControlDown(),event.isShiftDown());
		}
	}
	@Override
	public void mouseClicked(MouseEvent event) {
		if(event.getSource() == titleLabel){
			if(title.equals("Problem")||title.equals("Master"))return;
			String newTitle = JOptionPane.showInputDialog("rename");
			if(newTitle == null)return;
			this.title = newTitle;
			titleLabel.setText(newTitle);
			this.repaint();
		}
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
