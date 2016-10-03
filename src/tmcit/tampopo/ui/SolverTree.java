package tmcit.tampopo.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import tmcit.api.ISolver;
import tmcit.api.Parameter;
import tmcit.procon27.main.Solver;
import tmcit.tampopo.ui.util.FolderNode;
import tmcit.tampopo.ui.util.MyTreeCellRenderer;
import tmcit.tampopo.ui.util.ParameterNode;
import tmcit.tampopo.util.ParameterLoader;

public class SolverTree extends JTree implements MouseListener , ActionListener{

	public static final Color backGround = new Color(230, 230, 240);
	private static FolderNode rootNode = new FolderNode("ROOT");
	public static String[] solvers = {"EdgeSolver","RotSolver"};
	
	public SolverDetailPanel tabPane;
	public DefaultTreeModel model;

	public SolverTree(SolverDetailPanel tabPane){
		super(rootNode);
		this.tabPane = tabPane;
		initPanel();
		initTree();
	}

	public void initTree(){
		this.model = (DefaultTreeModel)this.getModel();
		TreeCellRenderer renderer = this.getCellRenderer();
		this.setCellRenderer(new MyTreeCellRenderer());
		this.addMouseListener(this);
		try {
			ParameterLoader.loadAllParameter(rootNode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		expandAllNodes(this, 0, this.getRowCount());
	}

	public void initPanel(){
		this.setBackground(backGround);
	}

	public void addParameterNode(FolderNode targetNode){
		///新しくソルバ用のパラメータを追加！
		Object ret = JOptionPane.showInputDialog(this, "Solverを選択してください。",
			      "solver", JOptionPane.INFORMATION_MESSAGE,
			      null, solvers, solvers[0]);
		if(ret == null)return;
		String title = JOptionPane.showInputDialog("パラメータ名を入力してください。");
		if(title == null || title.equalsIgnoreCase(""))return;

		String solverName = (String)ret;
		ISolver solver = null;
		if(solverName.equalsIgnoreCase("EdgeSolver")){
			solver = new tmcit.tampopo.edgeSolver.main.BeamEdgeMain();
		}else if(solverName.equalsIgnoreCase("RotSolver")){
			solver = new tmcit.procon27.main.RotSolverMain();
		}
		if(solver == null){
			System.out.println("SOLVER NULL");
			return;
		}
		ParameterNode node = new ParameterNode(title, solver);
		targetNode.add(node);
		model.nodeStructureChanged(targetNode);
		node.save();///ツリーにaddした後じゃないと意味ない
	}
	
	public void copyParameterNode(ParameterNode node){
		String title = JOptionPane.showInputDialog("パラメータ名を入力してください。");
		if(title == null || title.equalsIgnoreCase(""))return;
		ISolver solver = node.getSolver();
		if(solver instanceof tmcit.tampopo.edgeSolver.main.BeamEdgeMain){
			solver = new tmcit.tampopo.edgeSolver.main.BeamEdgeMain();
		}else if(solver instanceof tmcit.procon27.main.RotSolverMain){
			solver = new tmcit.procon27.main.RotSolverMain();
		}
		List<Parameter> parameters = node.getParameters();
		ParameterNode copyNode = new ParameterNode(title, solver, parameters);
		DefaultMutableTreeNode parantNode = (DefaultMutableTreeNode) node.getParent();
		parantNode.add(copyNode);
		model.nodeStructureChanged(parantNode);
		copyNode.save();
	}

	public void addFolder(FolderNode targetNode){
		///フォルダノードに新しくフォルダ追加
		String title = JOptionPane.showInputDialog("フォルダ名を入力してください。");
		if(title == null || title.equalsIgnoreCase(""))return;
		FolderNode newFolder = new FolderNode(title);
		targetNode.add(newFolder);
		model.nodeStructureChanged(targetNode);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String title = ((JMenuItem)event.getSource()).getText();
		if(this.getSelectionPaths() == null)return;
		if(title.equalsIgnoreCase("フォルダ作成")){
			FolderNode folderNode = (FolderNode) this.getSelectionPath().getLastPathComponent();
			addFolder(folderNode);
		}else if(title.equalsIgnoreCase("パラメータ作成")){
			FolderNode folderNode = (FolderNode) this.getSelectionPath().getLastPathComponent();
			addParameterNode(folderNode);
		}else if(title.equalsIgnoreCase("削除")){
			removeSelectionNode();
		}else if(title.equalsIgnoreCase("コピー")){
			ParameterNode parameterNode = (ParameterNode) this.getSelectionPath().getLastPathComponent();
			copyParameterNode(parameterNode);
		}
		this.repaint();
	}

	private void showPopup(int x,int y,boolean folder,boolean root){///ポップアップを表示する
		JPopupMenu menu = new JPopupMenu();
		JMenuItem item1 = new JMenuItem("フォルダ作成");
		JMenuItem item2 = new JMenuItem("パラメータ作成");
		JMenuItem item4 = new JMenuItem("コピー");
		JMenuItem item3 = new JMenuItem("削除");
		if(folder){
			menu.add(item1);
			menu.add(item2);
		}else{
			menu.add(item4);
		}
		if(!root){
			menu.add(item3);
		}
		item1.addActionListener(this);
		item2.addActionListener(this);
		item3.addActionListener(this);
		item4.addActionListener(this);
		menu.show(this, x, y);
	}
	
	private void rightClick(int x,int y){
		int tarRow = this.getRowForLocation(x, y);
		if(tarRow == -1)return;
		boolean folder = true;
		boolean root = false;
		if(this.getSelectionPaths() == null||this.getSelectionPaths().length == 1){
			this.setSelectionRow(tarRow);///右クリックした場所を選択させる
			if(this.getSelectionPath().getLastPathComponent() instanceof ParameterNode){
				folder = false;
			}
			if(this.getSelectionPath().getLastPathComponent().equals(rootNode)){
				root = true;
			}
		}
		showPopup(x, y, folder,root);
	}
	
	private void leftDoubleClick(int x,int y){
		int tarRow = this.getRowForLocation(x, y);
		if(tarRow == -1)return;
		if(this.getSelectionPaths() != null&&this.getSelectionPaths().length != 1)return;
		this.setSelectionRow(tarRow);
		if(this.getSelectionPath().getLastPathComponent() instanceof ParameterNode){
			ParameterNode parameterNode = (ParameterNode) this.getSelectionPath().getLastPathComponent();
			parameterNode.showDetail(tabPane);
		}
	}

	@Override
	public void mousePressed(MouseEvent event) {
		int button = event.getButton();
		if(button == 3){
			///右クリ
			rightClick(event.getX(), event.getY());
		}else if(button == 1 && event.getClickCount() == 2){
			///左ダブルクリック
			leftDoubleClick(event.getX(), event.getY());
		}
		
	}
	
	private void expandAllNodes(JTree tree, int startingIndex, int rowCount){
	    for(int i=startingIndex;i<rowCount;++i){
	        tree.expandRow(i);
	    }

	    if(tree.getRowCount()!=rowCount){
	        expandAllNodes(tree, rowCount, tree.getRowCount());
	    }
	}
	
	public void removeSelectionNode(){
		List<DefaultMutableTreeNode> tar = new ArrayList<DefaultMutableTreeNode>();
		for(TreePath path : this.getSelectionPaths()){///スタートノードは全部試す、あとは再帰
			dfs((DefaultMutableTreeNode)path.getLastPathComponent(),tar);
		}
		
		if(tar.size() == 0)return;
		int ret = JOptionPane.showConfirmDialog(this, (tar.size())+"個の項目を削除しますがよろしいですか？","",JOptionPane.OK_CANCEL_OPTION);
		if(ret != 0)return;
		for(DefaultMutableTreeNode node : tar){
			if(node instanceof ParameterNode){
				removeNode(node);
			}
		}
		for(DefaultMutableTreeNode node : tar){
			if(node instanceof FolderNode){
				removeNode(node);
			}
		}
		model.reload();
	}
	
	private void dfs(DefaultMutableTreeNode now,List<DefaultMutableTreeNode> set){///再帰的に削除すべきノードを探す
		if(!set.contains(now)){
			set.add(now);
		}
		for(int i = 0;i < now.getChildCount();i++){
			DefaultMutableTreeNode next = (DefaultMutableTreeNode) now.getChildAt(i);
			dfs(next,set);
		}
	}
	
	private void removeNode(DefaultMutableTreeNode node){
		if(node.equals(rootNode))return;
		if(node instanceof ParameterNode){
			ParameterNode parameterNode = (ParameterNode) node;
			parameterNode.delete();
		}
		((DefaultMutableTreeNode)node.getParent()).remove(node);
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



}
