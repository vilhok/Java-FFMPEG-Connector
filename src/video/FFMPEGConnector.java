package video;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

public class FFMPEGConnector{

	private Process videoOut;
	private OutputStream ffmpegInput;

	private static Path ffmpeg = FileSystems.getDefault().getPath("ffmpeg.exe");
	private Optional<File> logFile = Optional.empty();
	private List<String> args = new ArrayList<>();
	private boolean parametersAdded;

	private List<String> defaults;

	public FFMPEGConnector(File outputFile){
		defaults = Arrays.asList(ffmpeg.toString(), "-r", "60", "-i", "pipe:0", "-framerate", "60", "-f", "mp4", "-crf", "25", "-b:v", "65M", "-pix_fmt", "yuv420p", "-maxrate", "65M", "-bufsize", "65M", outputFile.getAbsolutePath());

	}

	/**
	 * assumes png format
	 */
	public void addImage(BufferedImage bf) throws IOException{
		addImage(bf, "png");
	}

	public void addImage(BufferedImage bf, String type) throws IOException{
		ImageIO.write(bf, type, ffmpegInput);
	}

	public static void setFFMPEGpath(File f){
		ffmpeg = f.toPath();
	}

	public FFMPEGConnector addParameter(String key, String value){
		args.add(key);
		args.add(value);
		parametersAdded = true;
		return this;
	}

	public FFMPEGConnector addParameter(String arg){
		args.add(arg);
		parametersAdded = true;
		return this;
	}

	public FFMPEGConnector addAlladdParameters(String[] params){
		addAlladdParameters(Arrays.asList(params));
		return this;
	}

	public FFMPEGConnector addAlladdParameters(List<String> params){
		args.addAll(params);
		parametersAdded = true;
		return this;
	}

	public FFMPEGConnector logFile(File f){
		logFile = Optional.of(f);
		return this;
	}

	public FFMPEGConnector initialize() throws IOException{
		if(logFile.isPresent()){
			videoOut = new ProcessBuilder().redirectInput(ProcessBuilder.Redirect.PIPE).redirectErrorStream(true).redirectOutput(logFile.get()).command(parametersAdded ? args : defaults).start();
		}else{

			videoOut = new ProcessBuilder().redirectInput(ProcessBuilder.Redirect.PIPE).command(parametersAdded ? args : defaults).start();
		}
		ffmpegInput = videoOut.getOutputStream();
		return this;
	}

	public void finish(){
		try{
			ffmpegInput.close();
		}catch (IOException e1){
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		boolean ready = false;

		while(!ready){
			try{
				videoOut.waitFor();
				ready = true;
			}catch (InterruptedException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
