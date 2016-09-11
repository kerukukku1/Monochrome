package Mahito6.Main;

public class SolverConstants {
	
	public static final double gosa = 10.0;///線上にあるか判定するアルゴリズムの許容誤差
    public static final int kAngleSplits = 1024;   					   //0~3.14(pi)をどれだけ分割するか(デフォは4096分割、除算するので2^nの数を使おう)
    public static final int kMinCount = 15;       					   //ハフ変換での直線認識の閾値、この値より小さいと直線でないとみなす
    public static final double kTableConst = Math.PI / kAngleSplits;   //3.14(pi)をkAngleSplitsで割った値、単位角度的なやつ
    public static int edgeWidth = 6;   //エッジの太さ(この太さで２値画像から辺を消す)waku:12 piece:6
	public static final int MethodWidth = 12;
    public static int lrAddition = 30; //検出して切断したエッジを少しだけ伸ばす waku:50 piece:15
    public static final int doubleLineDetect = 40;
	public static final long hashkey = 100000;
	public static final double LongEdge = 300.0;///線分がこれより長ければ端点を縮める
	public static final double ShorteningLength = 60.0;///LongEdgeを縮める長さ
	
	public SolverConstants(){
		
	}
}
