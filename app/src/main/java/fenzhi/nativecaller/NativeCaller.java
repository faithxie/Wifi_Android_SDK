package fenzhi.nativecaller;


import android.content.Context;

public class NativeCaller {
	static {
		System.loadLibrary("fzp2p");
	}

	private static final String LOG_TAG = "NativeCaller";
	
	public native static int RecordLocal(String uid,int bRecordLocal); 
	
	public native static void StartSearch();

	public native static void StopSearch();
	
	public native static void Init();

	public native static void Free();
	
	public native static int StartP2P(String did, String user, String pwd, int bEnableLanSearch);

	public native static int StopP2P(String did);

	public native static int StopP2PLivestream(String did);

	public native static int StartP2PLivestream(String did, int streamid,int substreamid);

	public native static int P2PStartAudio(String did);

	public native static int P2PStopAudio(String did);

	public native static int P2PStartTalk(String did);

	public native static int P2PStopTalk(String did);

	public native static int P2PTalkAudioData(String did, byte[] data, int len);

	public native static int P2PNetworkDetect();

	public native static void P2PInitial(String svr);
	
	public native static int P2PDeInitialize();
	
	public native static void P2PInitialOther(String svr);

	public native static int P2PSetCallbackContext(Context object);

	public native static int StartSnapshot(String did);
	
	public native static int StartPlayBack(String did, String filename,
			int offset);

	public native static int StopPlayBack(String did);

	public native static int P2PGetSDCardRecordFileList(String did,
			int PageIndex, int PageSize);

	// takepicture
	public native static int YUV4202RGB565(byte[] yuv, byte[] rgb, int width,
			int height);

	public native static int SetSoftDecode(String did,int on);
	
	public native static int DecodeH264Frame(byte[] h264frame, int bIFrame,
			byte[] yuvbuf, int length, int[] size);

	public native static int P2PSendData(String did,int cmd, byte[] data, int len,int channel);
	
	public native static int sendJsonCmd(String did ,int cmd , String msg);
	
	public native static int LoginToIPC(String uuid,String username,String passwd,int tick);
	
	public native static int EncodePasswd(String passwd,byte[] encoded_pw,int len);
	public native static int DecodeURL(String encode,byte[] decode, int decoded_len);
}