package Mahito6.UI;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Main.UI.Util.ImageManager;

public class NumberingFrame extends JFrame{
	private JPanel earth;
	public NumberingFrame(String path){
		this.setLayout(null);
		this.setTitle(path);
		earth = new JPanel();
		earth.setLayout(null);
		BufferedImage buf = null;
		System.out.println(path);
		try {
			buf = ImageIO.read(new File(path));
			System.out.println("OK");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int h = (int) d.getHeight() - 100;
		int bh = buf.getHeight();
		System.out.println(bh);
		double scale = (double)h / (double)bh;
		ImageIcon icn = new ImageIcon(ImageManager.rescaleImage(scale, buf));
		JLabel imlabel = new JLabel();
		imlabel.setIcon(icn);
		imlabel.setBounds(0, 0, (int)(scale * (double)buf.getWidth()), (int)(scale * (double)buf.getHeight()));
		earth.add(imlabel);
		earth.setBounds(0, 0, (int)(scale * (double)buf.getWidth()), (int)(scale * (double)buf.getHeight()));
		this.add(earth);
		this.setSize(new Dimension((int)(scale * (double)buf.getWidth()), (int)(scale * (double)buf.getHeight())));
//		this.setResizable(false);
		this.setVisible(true);
	}
}
