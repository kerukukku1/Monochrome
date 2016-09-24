package Mahito6.Main;

public class SolverConstants {
    public int kAngleSplits;   					   //0~3.14(pi)をどれだけ分割するか(デフォは4096分割、除算するので2^nの数を使おう)
    public int kMinCount;       					   //ハフ変換での直線認識の閾値、この値より小さいと直線でないとみなす
    public int edgeWidth;   //エッジの太さ(この太さで２値画像から辺を消す)waku:12 piece:6
	public int MethodWidth;
    public int lrAddition; //検出して切断したエッジを少しだけ伸ばす waku:50 piece:15
    public int doubleLineDetect;
	public double LongEdge;///線分がこれより長ければ端点を縮める
	public double ShorteningLength;///LongEdgeを縮める長さ
	
	public SolverConstants(){
	    kAngleSplits = 1024; 
	    kMinCount = 60;
	    edgeWidth = 5;
		MethodWidth = 5;
	    lrAddition = 30;
	    doubleLineDetect = 40;
		LongEdge = 70.0;
		ShorteningLength = 20.0;
	}
}
