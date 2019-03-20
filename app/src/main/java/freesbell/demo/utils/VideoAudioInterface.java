package freesbell.demo.utils;

public interface VideoAudioInterface {
	public void setVideoData(String did, byte[] videobuf, int h264Data,
			int len, int width, int height);
	public void setAudioData(byte[] pcm, int len);
	public void callBackH264Data(String uuid ,byte[] h264, int type, int size,int width, int height);
}
