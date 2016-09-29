package tmcit.tampopo.ui.util;

import javax.swing.tree.DefaultMutableTreeNode;

public class FolderNode extends DefaultMutableTreeNode{
	
	private String title;
	
	public FolderNode(String title){
		super(title);
		this.title = title;
	}
	
	public String getTitle(){
		return title;
	}

}
