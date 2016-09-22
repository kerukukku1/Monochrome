package Mahito6.Main;

import java.awt.Color;

public class Constants{

	public static final int solveThread = 4;
    public static final int divideImageOffset = 100; //スキャン端の大きな影を省く為のオフセット
    public static final int divideImageGarbageThreshold = 2000; //分割したときの点の数がこれ以下ならゴミとして除去
    public static int dividePixelLookingForDist = 20; //分割するとき幅優先探索で回りを見る距離
    public static int clearNoiseThreshold = 200; //ノイズを除去する島の点の数の閾値 waku:1200 piece:200
    public static final int imagePositionOffset = 600;

	public static final boolean isOutputDebugOval = false;
	public static final String uiTitle = "Monochrome";
	public static final boolean debugImage = true;
	public static final boolean outputStream = false;
	public static boolean modeWaku = false;
	public static final int queueSize = 20000000;

	public static final Color polyColor = Color.YELLOW;
	public static final Color plotColor = Color.GREEN;
	public static final Color onPlotColor = Color.RED;
	public static final Color coordColor = Color.WHITE;
	public static final Color targetColor = Color.BLUE;
	public static final Color backgroundColor = Color.BLACK;
	public static final Color rangeRectColor = Color.GREEN.brighter();
	public static final Color newLineColor = Color.RED.brighter().brighter();

	public static final int targetOvalRadius = 4;
	public static final int plotOvalRadius = 4;
	//VisualizePanelで描画した線の長さを伸ばすオフセット
	public static final int expandOffset = 7;
	public static final int Nameraka = 1;
}