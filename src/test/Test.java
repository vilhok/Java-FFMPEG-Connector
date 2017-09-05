package test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import video.FFMPEGConnector;
import video.Resolution;

public class Test{
	public static void main(String[] args) throws IOException{
		FFMPEGConnector.setFFMPEGpath(new File("C:/ffmpeg/bin/ffmpeg.exe"));
		//@formatter:off
		FFMPEGConnector video = new FFMPEGConnector(new File("test"+System.currentTimeMillis()+ ".mp4"))
				.logFile(new File("log.txt")).initialize();
		//@formatter:on
		renderImages(video);

		video.finish();
		System.out.println("ready");

	}

	public static void renderImages(FFMPEGConnector video) throws IOException{
		Resolution res = Resolution.res3840x2160;
		int max = 300;

		class Pair<T, K> {
			T first;
			K second;

			public Pair(T first, K second){
				super();
				this.first = first;
				this.second = second;
			}

		}

		ArrayList<Pair<Integer, Integer>> pairs = new ArrayList<>();

		int squareSize = 5;

		for(int i = 0; i < res.width; i += squareSize){
			for(int j = 0; j < res.height; j += squareSize){
				pairs.add(new Pair<Integer, Integer>(i, j));
			}
		}
		System.out.println(pairs.size());
		Collections.shuffle(pairs);
		double squaresperframe = pairs.size() / (double) max;
		for(int i = 0; i < max; i++){

			Random r = new Random(12);
			//			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			//			GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
			//			BufferedImage bf = gc.createCompatibleImage(res.width, res.height, Transparency.TRANSLUCENT);
			BufferedImage bf = new BufferedImage(res.width, res.height, BufferedImage.TYPE_3BYTE_BGR);

			bf.setAccelerationPriority(1);
			Graphics2D g = bf.createGraphics();
			g.setBackground(Color.black);
			g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 200));
			int squares = (int) (squaresperframe * (i + 1));
			System.out.println("Filling " + squares + "/" + pairs.size() + " squares");
			for(int j = 0; j < squares; j++){

				g.setColor(getRandomColor(r));
				g.fillRect(pairs.get(j).first, pairs.get(j).second, squareSize, squareSize);

			}
			g.setColor(Color.WHITE);
			g.drawString(i + 1 + "", res.width / 2, res.height / 2);
			g.dispose();
			video.addImage(bf);
			System.out.println("added " + (i + 1) + "/" + max + " " + bf.getAccelerationPriority());
		}

	}

	private static Color getRandomColor(Random r){
		return new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));

	}
}
