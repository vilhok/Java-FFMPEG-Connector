package test;

import java.io.File;
import java.io.IOException;

import video.FFMPEGConnector;

public class MultiThreadTest{

	public static void main(String[] args){

		FFMPEGConnector.setFFMPEGpath(new File("C:/ffmpeg/bin/ffmpeg.exe"));
		MultiThreadTest mtt = new MultiThreadTest();
		for(int i = 0; i < 6; i++){
			new Thread(mtt.new Renderer(i)).start();
			try{
				Thread.sleep(10);
			}catch (InterruptedException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	class Renderer implements Runnable{

		int i;

		public Renderer(int num){
			// TODO Auto-generated 
			this.i = num;
		}

		@Override
		public void run(){
			FFMPEGConnector video;
			try{
				video = new FFMPEGConnector(new File("test" + System.currentTimeMillis()
						+ ".mp4")).logFile(new File("log" + i + ".txt")).initialize();

				Test.renderImages(video);

				video.finish();
			}catch (IOException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("ready");

		}

	}
}
