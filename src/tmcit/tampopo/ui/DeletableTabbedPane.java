package tmcit.tampopo.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import tmcit.tampopo.util.IconUtil;

public class DeletableTabbedPane extends JTabbedPane{
	private final ImageIcon closeIcon = IconUtil.getIcon("close.png");
	private final Dimension buttonSize = new Dimension(closeIcon.getIconWidth(), closeIcon.getIconHeight());
	private ActionListener listener;
	public DeletableTabbedPane() {
	}
	public void setListener(ActionListener listener){
		this.listener = listener;
	}
	public void addTab(String title,Component content) {
		JPanel tab = new JPanel(new BorderLayout());
		tab.setOpaque(false);
		JLabel label = new JLabel(title);
		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
		JButton button = new DeletableMyButton(closeIcon,content);
		button.setPreferredSize(buttonSize);
		button.addActionListener(listener);
		tab.add(label,  BorderLayout.WEST);
		tab.add(button, BorderLayout.EAST);
		tab.setBorder(BorderFactory.createEmptyBorder(2, 1, 1, 1));
		super.addTab(null, content);
		setTabComponentAt(getTabCount() - 1, tab);
	}
	public void addTab(String title,Icon icon,Component content) {
		System.out.println(title);
		JPanel tab = new JPanel(new BorderLayout());
		tab.setOpaque(false);
		JLabel label = new JLabel(title);
		label.setIcon(icon);
		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
		JButton button = new DeletableMyButton(closeIcon,content);
		button.setPreferredSize(buttonSize);
		button.addActionListener(listener);
		tab.add(label,  BorderLayout.WEST);
		tab.add(button, BorderLayout.EAST);
		tab.setBorder(BorderFactory.createEmptyBorder(2, 1, 1, 1));
		super.addTab(null, content);
		setTabComponentAt(getTabCount() - 1, tab);
	}
}
