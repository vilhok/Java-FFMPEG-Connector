package video;

public enum Resolution {

	//@formatter:off
	res640x480(640, 480), 
	res1920x1080(1920, 1080),
	res2560x1440(2560, 1440), 
	res3840x2160(3840,2160);
	//@formatter:on

	public final int width;
	public final int height;

	private Resolution(int width, int height){
		this.width = width;
		this.height = height;
	}

}