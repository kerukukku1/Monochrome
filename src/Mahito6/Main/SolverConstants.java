package Mahito6.Main;

public class SolverConstants {
	public static final double gosa = 10.0;///線上にあるか判定するアルゴリズムの許容誤差
    public static final int kAngleSplits = 1024;   					   //0~3.14(pi)をどれだけ分割するか(デフォは4096分割、除算するので2^nの数を使おう)
    public static final int kMinCount = 15;       					   //ハフ変換での直線認識の閾値、この値より小さいと直線でないとみなす
    public static final double kTableConst = Math.PI / kAngleSplits;   //3.14(pi)をkAngleSplitsで割った値、単位角度的なやつ
    public static int edgeWidth = 6;   //エッジの太さ(この太さで２値画像から辺を消す)waku:12 piece:6
	public static final int MethodWidth = 12;
    public static int lrAddition = 30; //検出して切断したエッジを少しだけ伸ばす waku:50 piece:15
    public static final int divideImageOffset = 100; //スキャン端の大きな影を省く為のオフセット
    public static final int divideImageGarbageThreshold = 2000; //分割したときの点の数がこれ以下ならゴミとして除去
    public static int dividePixelLookingForDist = 20; //分割するとき幅優先探索で回りを見る距離
    public static int clearNoiseThreshold = 200; //ノイズを除去する島の点の数の閾値 waku:1200 piece:200
    public static final int imagePositionOffset = 600;
    public static final int doubleLineDetect = 40;
	public static final long hashkey = 100000;
	public static boolean modeWaku = false;
}
