package Mahito6.UI;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Mahito6.Main.Problem;
import Main.UI.Util.Status;
public class PieceBox extends JPanel implements ChangeListener{
	private JRadioButton p1;
	private JRadioButton p2;
	private JRadioButton fr;
	private Problem link;
	private ButtonGroup buttonGroup;
	public PieceBox(Problem link){
	    JLabel icon = new JLabel("TYPE");
	    this.link = link;
		this.setLayout(null);
		this.setBorder(new CheckBorderBox(icon, this, BorderFactory.createEtchedBorder()));
	    p1 = new JRadioButton("Piece1");
	    p2 = new JRadioButton("Piece2");
	    fr = new JRadioButton("Frame");
	    p1.addChangeListener(this);
	    p2.addChangeListener(this);
	    fr.addChangeListener(this);
	    p1.setOpaque(false);
	    p2.setOpaque(false);
	    fr.setOpaque(false);
	    buttonGroup = new ButtonGroup();
		buttonGroup.add(p1);
		buttonGroup.add(p2);
		buttonGroup.add(fr);
		
		if(link.getType() == Status.Type.PIECE1){
			p1.setSelected(true);
		}else if(link.getType() == Status.Type.PIECE2){
			p2.setSelected(true);
		}else{
			fr.setSelected(true);
		}
		
		p1.setBounds(80,0,70,30);
		p2.setBounds(150,0,70,30);
		fr.setBounds(220,0,70,30);
		this.add(p1);
		this.add(p2);
		this.add(fr);
	}
	

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		if(p1.isSelected()){
			link.setType(Status.Type.PIECE1);
		}else if(p2.isSelected()){
			link.setType(Status.Type.PIECE2);
		}else if(fr.isSelected()){
			link.setType(Status.Type.FRAME);
		}
	}
	
	private class CheckBorderBox extends MouseAdapter implements Border, SwingConstants {
	    private static final int OFFSET = 5;
	    private final Component comp;
	    private final JComponent cont;
	    private final Border border;

	    protected CheckBorderBox(Component comp, JComponent container, Border border) {
	        super();
	        this.comp   = comp;
	        this.border = border;
	        this.cont = container;
	        if (comp instanceof JComponent) {
	            ((JComponent) comp).setOpaque(true);
	        }
	        container.addMouseListener(this);
	        container.addMouseMotionListener(this);
	    }

	    @Override public boolean isBorderOpaque() {
	        return true;
	    }

	    @Override public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
	        if (c instanceof Container) {
	            Insets borderInsets = border.getBorderInsets(c);
	            Insets insets = getBorderInsets(c);
	            int temp = (insets.top - borderInsets.top) / 2;
	            border.paintBorder(c, g, x, y + temp, width, height - temp);
	            Dimension size = comp.getPreferredSize();
	            Rectangle rect = new Rectangle(OFFSET, 0, size.width, size.height);
	            SwingUtilities.paintComponent(g, comp, (Container) c, rect);
	            comp.setBounds(rect);
	        }
	    }

	    @Override public Insets getBorderInsets(Component c) {
	        Dimension size = comp.getPreferredSize();
	        Insets insets = border.getBorderInsets(c);
	        insets.top = Math.max(insets.top, size.height);
	        return insets;
	    }
	    
	    public JComponent getContainer(){
	    	return cont;
	    }

	    private void dispatchEvent(MouseEvent e) {
	        Component src = e.getComponent();
	        comp.dispatchEvent(SwingUtilities.convertMouseEvent(src, e, comp));
	        src.repaint();
	    }
	    @Override public void mouseClicked(MouseEvent e) {
	        dispatchEvent(e);
	    }
	    @Override public void mouseEntered(MouseEvent e) {
	        dispatchEvent(e);
	    }
	    @Override public void mouseExited(MouseEvent e) {
	        dispatchEvent(e);
	    }
	    @Override public void mousePressed(MouseEvent e) {
	        dispatchEvent(e);
	    }
	    @Override public void mouseReleased(MouseEvent e) {
	        dispatchEvent(e);
	    }
	    @Override public void mouseMoved(MouseEvent e) {
	        dispatchEvent(e);
	    }
	    @Override public void mouseDragged(MouseEvent e) {
	        dispatchEvent(e);
	    }
	}
}