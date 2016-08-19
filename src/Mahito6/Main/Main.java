/**
 * 
 */
/**
 * @author fujinomahito
 *
 */
package Mahito6.Main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;

import Mahito6.Solver.CrossAlgorithm;
import Mahito6.Solver.Edge;
import Mahito6.Solver.EdgeFinder;
import Mahito6.UI.MainFrame;

public class Main {
	private MainFrame mainFrame;
	
	public Main(){
		mainFrame = new MainFrame("Monochrome");
	}
	
	public static void main(String[] args){
		new Main();
	}
}
