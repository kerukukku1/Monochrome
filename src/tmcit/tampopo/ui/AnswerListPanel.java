package tmcit.tampopo.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import tmcit.api.AnswerChangeEvent;
import tmcit.api.AnswerChangeListener;
import tmcit.tampopo.util.Answer;
import tmcit.tampopo.util.ProblemReader;

public class AnswerListPanel extends JPanel implements AnswerChangeListener{
	public SuperPanel source;
	public PanelListManager panelListManager;
	public int WIDTH,HEIGHT;
	
	public AnswerListPanel(int WIDTH,int HEIGHT,SuperPanel source){
		this.source = source;
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		initPanel();
		makePanel();
	}
	
	public void clearPanels(){
		///クリアする
		panelListManager.allClear();
	}
	
	public DetailPanel addAnswer(String title,Answer answer){
		DetailPanel detailPanel = new DetailPanel(title, answer, source);
		panelListManager.addPanel(detailPanel);
		return detailPanel;
	}
	
	public DetailPanel addAnswer(String title,List<Answer> answers){
		DetailPanel detailPanel = new DetailPanel(title, answers, source);
		panelListManager.addPanel(detailPanel);
		return detailPanel;
	}
	public DetailPanel addAnswer(String title, Answer answer, int index) {
		///割り込み
		DetailPanel detailPanel = new DetailPanel(title, answer, source);
		panelListManager.addPanel(detailPanel,index);
		return detailPanel;
	}
	public DetailPanel addAnswer(String title,List<Answer> answers,int index){
		DetailPanel detailPanel = new DetailPanel(title, answers, source);
		panelListManager.addPanel(detailPanel,index);
		return detailPanel;
	}

	
	public void removeAnswerPanel(DetailPanel component){
		panelListManager.removePanel(component);
	}
	
	public void makePanel(){
		panelListManager = new PanelListManager(WIDTH, HEIGHT,false,this);
		panelListManager.setBounds(0, 0, WIDTH, HEIGHT);
		this.add(panelListManager);
	}
	
	public void initPanel(){
		this.setLayout(null);
		this.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.white, Color.black));
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
			addAnswer("KAI", answers.get(0),2);
		}else{
			addAnswer("KAI", answers,2);
		}
	}


}
