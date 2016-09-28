package tmcit.tampopo.util;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import tmcit.tampopo.ui.SolverPanel;
import tmcit.tampopo.ui.SolverTree;

public class MyTreeCellRenderer implements TreeCellRenderer{
	
	public DefaultTreeCellRenderer defaultRenderer;
	
	public MyTreeCellRenderer() {
		this.defaultRenderer = new DefaultTreeCellRenderer();
		this.defaultRenderer.setBackgroundNonSelectionColor(SolverTree.backGround);
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		JLabel label = (JLabel)defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		if(value instanceof FolderNode){
			if(expanded){
				label.setIcon(IconUtil.getIcon("folderopen.png"));
			}else{
				label.setIcon(IconUtil.getIcon("folder.png"));
			}
		}else if(value instanceof ParameterNode){
			label.setIcon(IconUtil.getIcon("file.png"));
		}
		label.setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
		return label;
	}
	
	

}
