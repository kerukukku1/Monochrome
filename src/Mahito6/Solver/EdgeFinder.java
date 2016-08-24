package Mahito6.Solver;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import Mahito6.Main.Constants;
import Mahito6.Main.Tuple2;

public class EdgeFinder {
    // �ｽﾏ撰ｿｽ
    private ArrayList<Double> sin_table, cos_table;  ///探索処理高速化のためにsin,cosは全て単位角度で前計算
    private ArrayList<Integer> cross;
	private BufferedImage image,             ///入力画像
							save_image,      ///tmp画像(エッジ消したりするやつ)
							save_image_line; ///ans画像

	private int w,h,
				diagonal, ///対角線
				d2;       ///対角線の２倍

	private boolean[][] binary_image;///２値情報([w][h]、false:黒  true:白)
	private ArrayList<Edge> edges;///検出したエッジを入れる(つまりans)

	public EdgeFinder(BufferedImage image){///imageは２値化された画像
		this.image = image;
		this.w = image.getWidth();
		this.h = image.getHeight();
		cross = new ArrayList<Integer>();
		init();
	}

	public BufferedImage getResult(){
		return save_image;
	}
	public BufferedImage getResult_line(){
		return save_image_line;
	}
	public List<Edge> getResult_edge(){
		return edges;
	}




	public void edgeFind() throws Exception{///これを呼ぶとエッジが検出される
		long start = System.currentTimeMillis();

		edges = new ArrayList<Edge>();
		int c = 0;
		LeastSquareMethod lsm = new LeastSquareMethod(save_image);///最小二乗法のソルバ―
		while(true){

			binary_image = toBinaryImage(save_image);///入力画像を配列に落とす
			Tuple2<Double,Double> target = calcHoughLine(binary_image, false);///ハフ変換実行!(ここだけ重い)
			if(target == null)break;

			Edge preAns = split(save_image,target.t1,target.t2);///ハフ変換で得た直線を正しい長さにスプリットする
			drawColorLine(save_image_line,preAns,Color.RED);///とりあえずpreAnsを赤い線で描画

			Tuple2<Double,Double> ansConverted = lsm.detectAndConvert(preAns);///preAnsを最小二乗法によって精度上げる
			if(ansConverted == null){
				///最小二乗法 or split失敗により強制終了、無限ループ回避用
				break;
			}
			Edge ans = split(save_image,ansConverted.t1,ansConverted.t2);///正しい長さにスプリットする
			drawColorLine(save_image_line,ans,Color.BLUE);///最小二乗法で得た線を青色で描画

			edges.add(ans);
			removeLine(save_image,ans);///抽出した線を画像から消す

//			ImageIO.write(save_image_line, "png", new File(c+".png"));///途中経過を出力
			c++;
		}
		//lsm.finish();///最小二乗法で抽出された白点をデバッグ

		for(int i = 0;i < edges.size();i++){
			for(int j = i + 1;j < edges.size();j++){
				///全てのエッジの交点を描画する。
				Edge e1 = edges.get(i);
				Edge e2 = edges.get(j);
				Tuple2<Double,Double> cross = Edge.getCross(e1,e2);
				if(e1.onLine(cross)&&e2.onLine(cross)){///両方のエッジ上にある
					drawVertex(save_image_line,(int)cross.t1.doubleValue(),(int)cross.t2.doubleValue());
				}
			}
		}
		long end = System.currentTimeMillis();
		System.out.println((end - start)+"ms");
	}

	private void init(){///色々初期化
        save_image = new BufferedImage(w, h, BufferedImage.TYPE_INT_BGR);
        save_image_line = new BufferedImage(w, h, BufferedImage.TYPE_INT_BGR);
        Graphics2D g2d = (Graphics2D)save_image.getGraphics();
        g2d.drawImage(image, 0, 0, null);
        Graphics2D g2d2 = (Graphics2D)save_image_line.getGraphics();
        g2d2.drawImage(image, 0, 0, null);

        sin_table = new ArrayList<Double>(Constants.kAngleSplits);
        cos_table = new ArrayList<Double>(Constants.kAngleSplits);
        diagonal = (int)Math.sqrt(w * w + h * h) + 2;
        d2 = diagonal * 2;

        for(int t = 0; t < Constants.kAngleSplits; t++){
            sin_table.add(Math.sin(Constants.kTableConst * t));
            cos_table.add(Math.cos(Constants.kTableConst * t));
        }
	}

	private void drawVertex(BufferedImage image,int x,int y){
		Graphics2D g2d = (Graphics2D) image.getGraphics();
		g2d.setColor(Color.YELLOW);
		g2d.fillOval(x-5, y-5, 10, 10);
	}

	private boolean checkSquare(BufferedImage image,int x,int y){
		for(int i = -2;i <= 2;i++)
		for(int j = -2;j <= 2;j++){
			int tx = x + j;
			int ty = y + i;
			if(tx<0||ty<0||tx>=w||ty>=h)continue;
			if(image.getRGB(tx, ty) == -1){
				return true;
			}
		}
		return false;
	}

	private Edge split(BufferedImage image,double theta,double radius){///直線を正しい長さにする(オーダー軽いけど実装難しい)
		double r = radius;
		double sint = Math.sin(theta);
		double cost = Math.cos(theta);
		double kx = 0.0,ky = 0.0;
		ky = (r / sint);
		kx = (r / cost);
		if(Math.abs(ky) > Math.abs(kx)){
			ky = 0.0;
		}else{
			kx = 0.0;
		}
		System.out.println("kx = "+kx+", ky = "+ky);

        boolean[] as = new boolean[100000];
        if(sint != 0){
            for(double x = 0; x < w; x += 0.25){
                double y = ((r - x * cost) / sint);
                if(y < 0 || y >= h) continue;
                double dist = Math.sqrt(Math.pow(kx-x, 2.0)+Math.pow(ky-y,2.0));
                as[(int)dist] = checkSquare(image,(int)x,(int)y);
            }
        }
        if(cost != 0){
            for(double y = 0; y < h; y += 0.25){
                double x = ((r - y * sint) / cost);
                if(x < 0 || x >= w) continue;
                double dist = Math.sqrt(Math.pow(kx-x, 2.0)+Math.pow(ky-y,2.0));
                as[(int)dist] = checkSquare(image,(int)x,(int)y);
            }
        }

        ArrayList<Tuple2<Integer,Integer>> comp = new ArrayList<Tuple2<Integer,Integer>>();
        boolean flag = as[0];
        as[as.length - 1] = true;///逡ｪ蜈ｵ
        int length = 1;
        for(int i = 1;i < as.length;i++){
        	if(as[i] == flag){
        		length++;
        	}else{
        		if(flag && length <= 10){///逋ｽ繝槭�繧ｸ
        			comp.add(new Tuple2<Integer,Integer>(length,flag?0:1));
        		}else if(!flag && length <= 30 && !comp.isEmpty()){///蜊��繧後※繧九□縺代▲縺ｽ縺�1蛟狗岼縺ｯfalse縺悟ｰ上＆縺��縺ｧ豕ｨ諢�
        			comp.add(new Tuple2<Integer,Integer>(length,flag?0:1));
        		}else{
        			comp.add(new Tuple2<Integer,Integer>(length,flag?1:0));
        		}
        		System.out.println(flag+","+length);
        		length = 1;
        		flag = !flag;
        	}
        }
        for(int i = 0;i < comp.size() - 1;i++){
        	Tuple2<Integer,Integer> now = comp.get(i);
        	Tuple2<Integer,Integer> next = comp.get(i + 1);
        	if(now.t2 == next.t2){
        		comp.get(i).t1 += next.t1;
        		comp.remove(i + 1);
        		i--;
        	}
        }
        int maxi = 0;
        int left = 0,right = 0;
        int sum = 0;
        for(int i = 0;i < comp.size();i++){
        	Tuple2<Integer,Integer> now = comp.get(i);
        	if(now.t2 == 1 && maxi < now.t1){
        		left = sum;
        		right = sum + now.t1;
        		maxi = now.t1;
        	}
        	sum += now.t1;
        	System.out.println(now.t2+","+now.t1);
        }
        System.out.println(left+"-"+right);
        System.out.println("");
        left = Math.max(0, left - Constants.lrAddition);
        right += Constants.lrAddition;
        double kx1 = 0,ky1 = 0,kx2 = 0,ky2 = 0;
        if(sint != 0){
            for(double x = 0; x < w; x += 0.25){
                double y = ((r - x * cost) / sint);
                double dist = Math.sqrt(Math.pow(kx-x, 2.0)+Math.pow(ky-y,2.0));
                if((int)dist == left){
                	kx1 = x;
                	ky1 = y;
                }else if((int)dist == right){
                	kx2 = x;
                	ky2 = y;
                }
            }
        }
        if(cost != 0){
            for(double y = 0; y < h; y += 0.25){
                double x = ((r - y * sint) / cost);
                if(x < 0.0)continue;
                double dist = Math.sqrt(Math.pow(kx-x, 2.0)+Math.pow(ky-y,2.0));
                if((int)dist == left){
                	kx1 = x;
                	ky1 = y;
                }else if((int)dist == right){
                	kx2 = x;
                	ky2 = y;
                }
            }
        }
        return new Edge(r,theta,kx1,ky1,kx2,ky2);
	}

    private void erase(Graphics2D target,int x,int y){///x,yの周りの白点を消す(黒にする)
    	for(int i = -Constants.edgeWidth;i <= Constants.edgeWidth;i++){
    		for(int j = -Constants.edgeWidth;j <= Constants.edgeWidth;j++){
    			int tx = x + j;
    			int ty = y + i;
    			if(tx<0||ty<0||tx>=w||ty>=h)continue;
    			target.drawRect(tx, ty, 0, 0);
    		}
    	}
    }

    private void removeLine(BufferedImage image, Edge edge){///検出したエッジを画像から消し去る
        Graphics2D g2d = (Graphics2D)image.getGraphics();
        g2d.setColor(Color.BLACK);
        double sint = Math.sin(edge.theta);
        double cost = Math.cos(edge.theta);
        double r = edge.r;
        if(sint != 0){
            for(int x = 0; x < w; x++){
                int y = (int)((r - x * cost) / sint);
                if(y < 0 || y >= h) continue;
                if(!edge.onLine(x, y))continue;
                erase(g2d,x,y);
            }
        }
        if(cost != 0){
            for(int y = 0; y < h; y++){
                int x = (int)((r - y * sint) / cost);
                if(x < 0 || x >= w) continue;
                if(!edge.onLine(x, y))continue;
                erase(g2d,x,y);
            }
        }
    }
    private void drawColorLine(BufferedImage image, Edge edge, Color color){///画像に線を描画
        Graphics2D g2d = (Graphics2D)image.getGraphics();
        g2d.setColor(color);
        double sint = Math.sin(edge.theta);
        double cost = Math.cos(edge.theta);
        double r = edge.r;
        if(sint != 0){
            for(int x = 0; x < w; x++){
                int y = (int)((r - x * cost) / sint);
                if(y < 0 || y >= h) continue;
                if(!edge.onLine(x, y))continue;
                g2d.drawRect(x, y, 0, 0);
            }
        }
        if(cost != Constants.kAngleSplits / 2){
            for(int y = 0; y < h; y++){
                int x = (int)((r - y * sint) / cost);
                if(x < 0 || x >= w) continue;
                if(!edge.onLine(x, y))continue;
                g2d.drawRect(x, y, 0, 0);
            }
        }
    }

    private Tuple2<Double,Double> calcHoughLine(boolean[][] src_image, boolean save_flg){///ハフ変換で最も評価値の高い直線を返す
        int[][] counter = getHoughLine(src_image);
        int max_count = 0;
        int t_max = 0, r_max = 0;
        for(int r = 0; r < d2; r++){
            for(int t = 0; t < Constants.kAngleSplits; t++){
                int cnt = counter[r][t];
                if(max_count < cnt){
                    max_count = cnt;
                    t_max = t;
                    r_max = r;
                }
            }
        }
        if(max_count < Constants.kMinCount) return null;
        double realTheta = (double)t_max * Constants.kTableConst;
        double realR = r_max - diagonal;
        return new Tuple2<Double, Double>(realTheta, realR);
    }

    private int[][] getHoughLine(boolean[][] src_image){///ハフ変換の計算をするO(WH*kAngleSplits)(ここが一番計算量重い)
        int[][] dst_image = new int[d2][Constants.kAngleSplits];
        for(int r = 0; r < d2; r++)
        for(int t = 0; t < Constants.kAngleSplits; t++){
            dst_image[r][t] = 0;
        }

        for(int y = 0; y < h; y++)
        for(int x = 0; x < w; x++){
            if(src_image[x][y] == false) continue;///黒色ならコンティニュー

            for(int t = 0; t < Constants.kAngleSplits; t++){
                int r = (int)(x * cos_table.get(t) + y * sin_table.get(t) + 0.5);///intにキャストするためここで誤差出る
                int rindex = r + diagonal;///rは-diagonal~diagonalの範囲で存在するため、これで正の値にする
                dst_image[rindex][t] += 1;
            }
        }
        return dst_image;
    }

    private boolean[][] toBinaryImage(BufferedImage src_image){///画像src_imageから黒白の情報を抜き出す
        boolean[][] dst_image = new boolean[w][h];
        for(int y = 0; y < h; y++){
            for(int x = 0; x < w; x++){
                int color = src_image.getRGB(x, y);
                int r = (color & 0xff0000) >> 16;
                int g = (color & 0xff00) >> 8;
                int b = color & 0xff;
                double Y = 0.299 * r + 0.587 * g + 0.114 * b;   //YCbCr
                if(Y > 127.5)
                    dst_image[x][y] = true;///白
                else
                	dst_image[x][y] = false;///黒
            }
        }
        return dst_image;
    }

}

