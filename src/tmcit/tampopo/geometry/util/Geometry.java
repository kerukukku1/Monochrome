package tmcit.tampopo.geometry.util;

public class Geometry {
	
	public static final int COUNTER_CLOCKWISE = 1;			//反時計回りの方向
	public static final int CLOCKWISE = -1;					//時計回りの方向
	
	//回転方向の判定(-1:時計回り, 1:反時計回り)
	public static int checkRotationDire(Piece piece){
		double s = 0;
		for(int i = 0;i < piece.getPointSize();i++){
			Segment segment = piece.getSegment(i);
			Point p1 = segment.p1;
			Point p2 = segment.p2;
			s += (p1.x * p2.y - p2.x * p1.y);
		}

		if(s < 0.0){
			return COUNTER_CLOCKWISE;
		}else{
			return CLOCKWISE;
		}
	}

}
