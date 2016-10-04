package tmcit.tampopo.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicCheckBoxMenuItemUI;

import tmcit.api.AnswerChangeEvent;
import tmcit.api.AnswerChangeListener;
import tmcit.procon27.main.Solver;
import tmcit.tampopo.ui.util.DropFileHandler;
import tmcit.tampopo.ui.util.ExVertex;
import tmcit.tampopo.util.Answer;
import tmcit.tampopo.util.Problem;
import tmcit.tampopo.util.ProblemReader;

public class SolverPanel extends JPanel implements MouseListener , AnswerChangeListener{
	
	
	public static final int LEFT_WIDTH = 150;
	public static final int LEFT_HEIGHT = 664;
	public static final int CENTER_WIDTH = 520;
	public static final int CENTER_HEIGHT = 664;
	public static final int RIGHT_WIDTH = 280;
	public static final int RIGHT_HEIGHT = 664;
	
	public static final int LEFT_IMAGE_SIZE = LEFT_WIDTH - 16;///左側のリストの画像サイズ
	public static final int CENTER_BIGIMAGE_SIZE = CENTER_WIDTH - 10;///真ん中のでっかい画像サイズ
	public static final int CENTER_MINIIMAGE_SIZE = 124;///リストの小さい画像
	
	public SolverPanel own;
	
	public AnswerListPanel left;
	public AnswerListTab center;
	public SolverDetailPanel right;
	
	private DetailPanel masterDetailPanel = null;
	private Problem problem = null;///問題データ
	private Answer master = null;///マスター解
	
	public SolverPanel(){
		this.own = this;
		ProblemReader.setSolverPanel(this);
		initPanel();
		makePanels();
	}
	
	public void doMergeForMaster(){
		if(this.master == null)return;
		Answer newMaster = this.master.getCopy();///これがマージ先,必ずコピー
		Answer source = center.getViewingAnswer();
		if(source == null)return;
		source = source.getCopy();///一応コピー
		if(!newMaster.mergeAnotherAnswer(source)){
			///失敗
			System.out.println("マージに失敗しました！！！");
			return;
		}
		System.out.println("マージに成功しました！！！");
		replaceMaster(newMaster);
	}
	public void doOverwriteForMaster(){
		Answer source = center.getViewingAnswer();
		if(source == null)return;
		replaceMaster(source);
	}
	private void replaceMaster(Answer newAnswer){
		///本来のMasterを削除してnewAnswerに置き換える
		left.removeAnswerPanel(masterDetailPanel);
		master = newAnswer;
		masterDetailPanel = left.addAnswer("Master", master,0);
	}
	
	@Override
	public void answerChangeEvent(AnswerChangeEvent event) {
		///解が更新されたら呼ばれる
		List<String> answerTexts = event.getAnswer();
		List<Double> answerScores = event.getScores();
		List<Answer> answers = new ArrayList<Answer>();
		for(int i = 0;i < answerTexts.size();i++){
			String ansText = answerTexts.get(i);
			double score = answerScores.get(i);
			Answer answer = ProblemReader.convertSolvedAnswer(ansText,score);
			answers.add(answer);
		}
		if(answers.size() == 0){
			///解なし
			return;
		}
		if(answers.size() == 1){
			left.addAnswer("KAI", answers.get(0));
		}else{
			left.addAnswer("KAI", answers);
		}
	}
	
	public void allClear(){
		///全て初期化する
		this.problem = null;
		ProblemReader.stopAllSolver();
		left.clearPanels();
		center.clearPanels();
	}
	
	public Answer getViewingAnswer(){
		///中央に表示してるデカアンサーを返す
		return center.getViewingAnswer();
	}
	
	public BigImagePanel getViewingBigImagePanel(){
		return center.getViewingBigImagePanel();
	}
	
	public Problem getProblem(){
		return problem;
	}
	
	public void setProblem(Problem problem){
		allClear();
		this.problem = problem;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				master = own.problem.getEmptyAnswer();
				masterDetailPanel = left.addAnswer("Master", master);
				left.addAnswer("Problem",own.problem.convertAllAnswerList());
				leftDetailClick(masterDetailPanel,1);
			}
		});
	}

	private void initPanel() {
		this.setLayout(new FlowLayout());
		this.setTransferHandler(new DropFileHandler(this));
	}
	
	private void makePanels(){
		left = new AnswerListPanel(LEFT_WIDTH,LEFT_HEIGHT,this);
		center = new AnswerListTab(CENTER_WIDTH,CENTER_HEIGHT);
		right = new SolverDetailPanel(RIGHT_WIDTH,RIGHT_HEIGHT);
		
		this.add(right);
		this.add(center);
		this.add(left);
	}
	
	public void tabClose(){
		///今開いてるタブを閉じる
		center.deleteOpeningTab(true);
	}
	
	private void leftDetailClick(DetailPanel detailPanel,int button){
		String title = detailPanel.title;
		List<Answer> answers = detailPanel.answers;
		if(button == 1 || button == 2){
			///解が1個ならビッグ、複数ならリスト表示
			if(center.getTabFromComponent(detailPanel) != null){
				///すでに表示されているので
				center.setView(detailPanel);
				return;
			}
			JPanel newPanel;
			if(detailPanel.answers.size() == 1){
				newPanel = new BigImagePanel(title,answers.get(0),detailPanel);
			}else{
				MiniListPanel newMiniListPanel = new MiniListPanel(title, answers,this,detailPanel);
				title = newMiniListPanel.getTabTitle();
				newPanel = newMiniListPanel;
			}
			if(button == 1){
				center.addTabAndSelect(title, newPanel, detailPanel);
			}else if(button == 2){
				center.addTab(title, newPanel, detailPanel);
			}
			detailPanel.listOn();
		}else{
			///右クリックなら要素削除
			String detailTitle = detailPanel.title;
			if(detailTitle.equals("Problem")){
				///これは消しちゃダメ
				return;
			}
			if(detailTitle.equals("Master")){
				int res = JOptionPane.
						showConfirmDialog(this, "Masterを初期化しますか？","TITLE", JOptionPane.OK_CANCEL_OPTION);
				if(res != 0)return;
				replaceMaster(own.problem.getEmptyAnswer());
			}else{
				left.removeAnswerPanel(detailPanel);
			}
		}
	}
	private void centerMiniImageClick(MiniImagePanel miniImagePanel,int button){
		String title = miniImagePanel.getTabTitle();
		Answer answer = miniImagePanel.answer;
		if(button == 1 || button == 2){
			///解が1個ならビッグ、複数ならリスト表示
			if(center.getTabFromComponent(miniImagePanel) != null){
				///既に表示されているので
				center.setView(miniImagePanel);
				return;
			}
			BigImagePanel newPanel = new BigImagePanel(title,answer,null);
			if(button == 1){
				center.addTabAndSelect(title, newPanel, miniImagePanel);
			}else if(button == 2){
				center.addTab(title, newPanel, miniImagePanel);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

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
		int button = e.getButton();
		if(e.getSource() instanceof DetailPanel){
			leftDetailClick((DetailPanel)e.getSource(), button);
		}else if(e.getSource() instanceof MiniImagePanel){
			centerMiniImageClick((MiniImagePanel)e.getSource(), button);
		}
	}



}
