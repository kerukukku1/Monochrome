package Mahito6.UI;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;

import Mahito6.Main.Constants;
import Mahito6.Main.Problem;
import Mahito6.Main.ProblemManager;
import Mahito6.Solver.BFS;
import Main.UI.Util.Coordinates;
import Main.UI.Util.ImageManager;

public class NumberingFrame extends JFrame{
	private JPanel earth;
	public NumberingFrame(Problem p){
//		System.out.println(ProblemManager.getQuestPath());
		System.out.println(p.getPath());
		this.setLayout(null);
		String path = p.getPath();
		List<Coordinates> coords = p.getCoords();
		this.setTitle(path);
		earth = new JPanel();
		earth.setLayout(null);
		System.out.println(path);
		System.out.println(p.getPath());
		Mat numbering = Highgui.imread(path);
		
		for(int i = 0; i < coords.size(); i++){
			Coordinates c = coords.get(i);
				if(!Constants.modeWaku){
					Core.putText(numbering, String.valueOf(i+1),
							new Point(Math.abs(numbering.cols() - (c.maxx+50)), (c.maxy+c.miny)/2),
							Core.FONT_HERSHEY_SIMPLEX, 8f, 
							new Scalar(0, 0, 0), 25);
					Core.putText(numbering, String.valueOf(i+1),
							new Point(Math.abs(numbering.cols() - (c.maxx+50)), (c.maxy+c.miny)/2), 
							Core.FONT_HERSHEY_SIMPLEX, 8f,
							new Scalar(0, 255, 255), 15);
	//			}
				System.out.println("Hello");
			}
		}
		BufferedImage buf = ImageManager.MatToBufferedImageBGR(numbering);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int h = (int) d.getHeight() - 100;
		int bh = buf.getHeight();
		System.out.println(bh);
		double scale = (double)h / (double)bh;
		ImageIcon icn = new ImageIcon(ImageManager.rescaleImage(scale, buf));
		JLabel imlabel = new JLabel();
		imlabel.setIcon(icn);
		imlabel.setBounds(0, 0, icn.getIconWidth(), icn.getIconHeight());
		earth.add(imlabel);
		earth.setBounds(0, 0, icn.getIconWidth(), icn.getIconHeight());
		this.add(earth);
		this.setSize(icn.getIconWidth(), icn.getIconHeight());
//		this.setResizable(false);
		this.setVisible(true);
			
	}


}
