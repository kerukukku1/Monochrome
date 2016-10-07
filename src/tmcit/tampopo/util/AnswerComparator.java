package tmcit.tampopo.util;

import java.util.Comparator;

public class AnswerComparator implements Comparator<Answer>{

	private boolean smaller;

	public AnswerComparator(boolean smaller){
		this.smaller = smaller;
	}

	@Override
	public int compare(Answer a1, Answer a2) {
		int ret = 0;
		double a = a1.getArea();
		double b = a2.getArea();
		if(a > b){
			if(smaller){
				ret = 1;
			}else{
				ret = -1;
			}
		}else if(a < b){
			if(smaller){
				ret = -1;
			}else{
				ret = 1;
			}
		}else{
			ret = 0;
		}
		return ret;
	}
}
