package tmcit.tampopo.ui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import tmcit.tampopo.ui.util.ParameterNode;
import tmcit.tampopo.util.Answer;
import tmcit.tampopo.util.AnswerComparator;
import tmcit.tampopo.util.Problem;

public class SuperPanel extends JPanel{

	public static final int RIGHT_WIDTH = 150;
	public static final int RIGHT_HEIGHT = 634;
	public static final int CENTER_WIDTH = 520;
	public static final int CENTER_HEIGHT = 634;

	public static final int RIGHT_IMAGE_SIZE = RIGHT_WIDTH - 16;///左側のリストの画像サイズ
	public static final int CENTER_BIGIMAGE_SIZE = CENTER_WIDTH - 10;///真ん中のでっかい画像サイズ
	public static final int CENTER_MINIIMAGE_SIZE = 124;///リストの小さい画像

	public SolverPanel source;
	public AnswerListPanel right;
	public AnswerListTab center;
	public ParameterNode parameterNode;
	public DetailPanel masterDetailPanel;

	public SuperPanel(ParameterNode parameterNode,SolverPanel source){
		this.source = source;
		this.parameterNode = parameterNode;
		initPanel();
		makePanel();
	}
	public SuperPanel(SolverPanel source){
		this.source = source;
		this.parameterNode = null;
		initPanel();
		makePanel();
	}

	public void setProblem(Problem problem){
		center.clearPanels();
		right.clearPanels();
		masterDetailPanel = right.addAnswer("Master", problem.getEmptyAnswer());
		right.addAnswer("Problem", problem.convertAllAnswerList());
		makeBigImageOrMiniList(masterDetailPanel);
	}

	public void setAnswer(Answer answer){
		overwriteAnswerToMaster(answer);
		if(!showBigImageOrMiniList(masterDetailPanel)){
			makeBigImageOrMiniList(masterDetailPanel);
		}
	}

	public void addAnswer(Answer answer){
		right.addAnswer("COPY", answer,2);
	}

	public BigImagePanel getViewingBigImagePanel(){
		return center.getViewingBigImagePanel();
	}

	public void overwriteAnswerToMaster(Answer answer){
		removeDetailPanel(masterDetailPanel);
		masterDetailPanel = right.addAnswer("Master", answer.getCopy(),0);
	}

	public void removeDetailPanel(DetailPanel detailPanel){
		center.deleteTabFromRoot(detailPanel,false);
		right.removeAnswerPanel(detailPanel);
	}
	public void removeViewingBigImageOrMiniList(){
		center.deleteOpeningTab(false);
	}

	public boolean showBigImageOrMiniList(Component rootComponent){
		///中央のタブに表示されているならば移動,trueが返るとき表示中
		return center.setView(rootComponent);
	}

	public void makeBigImageOrMiniList(DetailPanel detailPanel){
		makeBigImageOrMiniList(detailPanel,false,false);
	}

	public void makeBigImageOrMiniList(DetailPanel detailPanel,boolean isCtrl,boolean isShift){
		///中央のタブに新しく作る
		String title = detailPanel.title;
		List<Answer> answers = detailPanel.answers;
		if(answers.size() == 1){
			BigImagePanel bigImagePanel = new BigImagePanel(title, answers.get(0));
			center.addTabAndSelect(title,bigImagePanel,detailPanel);
		}else{
			if(isCtrl || isShift){
				List<Answer> newArray = new ArrayList<Answer>();
				for(Answer ans : answers){
					newArray.add(ans);
				}
				if(isCtrl){
					Collections.sort(newArray,new AnswerComparator(false));
				}else{
					Collections.sort(newArray,new AnswerComparator(true));
				}
				answers = newArray;
			}
			MiniListPanel miniImagePanel = new MiniListPanel(title, answers,this, detailPanel);
			center.addTabAndSelect(title,miniImagePanel,detailPanel);
		}
	}
	public void makeBigImageFromMiniImage(MiniImagePanel miniImagePanel){
		String title = miniImagePanel.getTabTitle();
		Answer answer = miniImagePanel.answer;
		BigImagePanel bigImagePanel = new BigImagePanel(title, answer);
		center.addTabAndSelect(title,bigImagePanel,miniImagePanel);
	}

	public void makePanel(){
		right = new AnswerListPanel(RIGHT_WIDTH, RIGHT_HEIGHT, this);
		center = new AnswerListTab(CENTER_WIDTH,CENTER_HEIGHT);
		center.setBounds(2,2,CENTER_WIDTH,CENTER_HEIGHT);
		right.setBounds(CENTER_WIDTH+2, 2, RIGHT_WIDTH, RIGHT_HEIGHT);
		this.add(right);
		this.add(center);
	}

	public void initPanel(){
		this.setLayout(null);
	}

	public ParameterNode getParameterNode(){
		return parameterNode;
	}

}
