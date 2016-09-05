package Main.UI.Util;

public class MeasureTimer {
	public static long start, end;
	public static void start(){
		MeasureTimer.start = System.nanoTime();
	}
	public static void end(){
		MeasureTimer.end = System.nanoTime();
	}
	public static void call(){
		System.out.println((MeasureTimer.end - MeasureTimer.start) / 1000000f + "ms");
	}
}
