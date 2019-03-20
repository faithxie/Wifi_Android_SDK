package freesbell.demo.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import freesbell.demo.client.R;
import fenzhi.nativecaller.NativeCaller;
import freesbell.demo.adapter.CamViewerPresentAdapter;
import freesbell.demo.adapter.CamViewerPresentAdapter.PresetBean;
import freesbell.demo.bean.CameraParamsBean;
import freesbell.demo.bean.DateBean;
import freesbell.demo.bean.JSONStructProtocal;
import freesbell.demo.bean.LiveVideoBean;
import freesbell.demo.bean.VideoRecordAbstractor;
import freesbell.demo.client.BridgeService.ControllerBinder;
import freesbell.demo.bean.JSONStructProtocal.IPCNETPrePointList_st;
import freesbell.demo.bean.JSONStructProtocal.IPCNetCamColorCfg_st;
import freesbell.demo.bean.JSONStructProtocal.IPCPtzCmd_st;
import freesbell.demo.content.ContentCommon;
import freesbell.demo.utils.AccGyroSensorUtils;
import freesbell.demo.utils.AudioPlayer;
import freesbell.demo.utils.CamViewCustomVideoRecord;
import freesbell.demo.utils.Cmds;
import freesbell.demo.utils.CustomAudioRecorder;
import freesbell.demo.utils.CustomBuffer;
import freesbell.demo.utils.CustomBufferData;
import freesbell.demo.utils.CustomBufferHead;
import freesbell.demo.utils.DatabaseUtil;
import freesbell.demo.utils.LiveStreamVideoView;
import freesbell.demo.utils.ServiceStub;
import freesbell.demo.utils.VideoAudioInterface;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

//import android.annotation.SuppressLint;

public class CameraViewerActivity extends Activity implements OnTouchListener,
		OnGestureListener, CustomAudioRecorder.AudioRecordResult,
		OnClickListener, OnDoubleTapListener,VideoRecordAbstractor,VideoAudioInterface{

	private static final String TAG = "CameraViewerActivity";
	/**
	 * 播放模式
	 * **/
	private static final int FULLSCREEN = 0;
	private static final int STANDARD = 1;
	private static final int MAGNIFY = 2;
	private int playmode = 1;

	private static final int AUDIO_BUFFER_START_CODE = 0xff00ff;

	public byte[] videodatas = null;
	public int videoDataLens = 0;
	public int nVideoWidths = 0;
	public int nVideoHeights = 0;
	public int flip = 0;// 水平 垂直 参数
	
	private boolean mPlayStarted = false;
	private boolean bProgress = true;

	private GestureDetector gt = new GestureDetector(this);
//	@SuppressWarnings("unused")
	private int nSurfaceHeight = 0;
	private int nSurfaceWidth = 0;

	private int nResolution = 0;
	private int nEnvMode = 0;//0:in door, 1:out door.
	@SuppressWarnings("unused")
	private int nMode = 0;
	@SuppressWarnings("unused")
	private int nFlip = 0;
	@SuppressWarnings("unused")
	private int nFramerate = 0;
	private boolean bInitCameraParam = false;
	private boolean bManualExit = false;
	private boolean mChangeOrientation = false;
	private boolean mHWDecode = false;
	private TextView textCodec = null;

	private ViewPager viewPager = null;
	private ArrayList<View> listView;
	private List<View> listDots;
	
	private int streamType = 1;
	
	private Button refresh_btn;

	private ArrayList<String> mUUIDList;
    private ArrayList<String> mNameList;
    private ArrayList<String> mStartedUUID;
    private ArrayList<Integer> mPositonList;
	private ArrayList<Integer> mP2PModeList;
	private int mCurrentVideoPage = 0;
	private int mCurrentVideoIndex = 0;
	private int mTotalVideoPage = 0; 
	private ViewPager mVideoViewPager;
	private List<View> mListVideoView;
	private int mVideosPerScreen = 9;
	private List<LiveVideoBean> mLiveVideos;
    private static Map<Integer,ArrayList<String>> startUID;
    private boolean mAutoChangeOrientation = true;

	private boolean bDisplayFinished = true;

	private int nPlayCount = 0;

	private CustomBuffer AudioBuffer = null;
	private AudioPlayer audioPlayer = null;
	private CustomAudioRecorder customAudioRecorder = null;
	private int nStreamCodecType;
	private int nP2PMode = ContentCommon.P2P_MODE_P2P_NORMAL;

	// add
	private boolean isTakeVideo = false;
	private long videotime = 0;// 录每张图片的时间
	private boolean isTakePicture = false;
	private boolean isLeftRight = false;
	private boolean isUpDown = false;
	private PopupWindow mPopupWindowProgress;
//	private static boolean isHorizontalMirror = false;
//	private static boolean isVerticalMirror = false;
//	private boolean isUpDownPressed = false;
	private boolean isShowtoping = false;

	// private GridView gd;
	private ListView presentlist;
	private ListView morePtzSettingList;

	public static Drawable d;

	private JSONStructProtocal mJSONStructProtocal = new JSONStructProtocal();
	private ArrayList<IPCPtzCmd_st> mIPCPtzCmdList;

	private PopupWindow imagePopupWindow = null;
	public Bitmap bmp;

	private DatabaseUtil dbUtil;

	private View ptzOtherSetView;
	private View ptzOtherSet;
	private Button ptzUpDown;
	private Button ptzTakepicBtn;

//	private ImageButton ptzPlayMode;
	private Button ptzTakeVideoBtn;

	private Animation showAnim;
	private boolean isTalking = false;
	private boolean isMcriophone = false;
	private boolean bAudioStart = false;
	private boolean bAudioRecordStart = false;
	private boolean isPTZPrompt;
	public BridgeService mBridgeService;
	private CamViewerPresentAdapter presentadapter = null;
	private ImageButton btnBack;
	private Button play_btn,menu_btn,resolution_btn,record_btn,mic_btn,voice_btn;
	private PopupWindow ptzPopupWindow;
	private PopupWindow resolutionPopWindow;
	private PopupWindow morePopupWindow;
	private PopupWindow presetPopupWindow;
	private PopupWindow presetNameSettingPopupWindow;
	private Animation dismissAnim;
	private View ptzOtherSetAnimView;
	private View ptzBogtop;
	private MyStatusBroadCast broadcast;
	
	private LinearLayout ptzPlatform_land;
	private LinearLayout topbg;
	private Animation showTopAnim;
	private Animation dismissTopAnim;

	private LiveStreamVideoView mVideoView;
	private CamViewCustomVideoRecord myvideoRecorder;
	public boolean isH264 = true;
	private boolean isport;
	
	private final int GSENSOR_REPORT_DATA_MSG = 1530;
	
	private IPCNETPrePointList_st mIPCNETPrePointList_st;
	private IPCNetCamColorCfg_st mIPCNetCamColorCfg_st;
	
	private String MultiStreamValue = 4 + "";

	// end
	private ServiceConnection mConn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			ControllerBinder myBinder = (ControllerBinder) service;
			mBridgeService = myBinder.getBridgeService();
			mBridgeService.setServiceStub(mServiceStub);
			mBridgeService.setLivestreamReciver(CameraViewerActivity.this);
			
			Log.d(TAG,"mStartedUUID:" + mStartedUUID.size());
			for(int i = 0; i < mStartedUUID.size() ; i++){
				String uuid = mStartedUUID.get(i);
				Log.d(TAG,"start uuid:" + mStartedUUID.get(i));
				getLiveVideoBean(uuid).startLiveStream();
				
				Cmds.getDevPtz(mServiceStub,uuid);
			}
			mPlayStarted = true;
			
			for(String uuid:mStartedUUID){
				mServiceStub.setResponseTarget(uuid,GSENSOR_REPORT_DATA_MSG);
				Cmds.getVideoOrien(mServiceStub,uuid);
			}
		}
	};
	private ServiceStub mServiceStub = new ServiceStub(){
		@Override
		public void onMessageRecieve(String uuid ,int cmd, String json) {
			Log.d(TAG, "msg:" + cmd);
			
			Bundle bd = new Bundle();
			Message msg = P2PMsgHandler.obtainMessage();
			msg.what = cmd;
			bd.putString("json", json);
			bd.putString("uuid", uuid);
			msg.setData(bd);
			
			P2PMsgHandler.sendMessage(msg);
		}

		@Override
		public void onMessageRecieve(String uuid, int msg, byte[] str) {
			// TODO Auto-generated method stub
			
		}
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getDataFromOther();
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.camera_viewer);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
			//透明状态栏  
	        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}

		mHWDecode = BridgeService.mSelf.getDecodeMode();
		int version = android.os.Build.VERSION.SDK_INT;
		if (version < Build.VERSION_CODES.JELLY_BEAN){//allways software decode.
			mHWDecode = false;
		}		
		
		mPlayStarted = true;
		gt.setIsLongpressEnabled(true);

		// initSame();
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		int oreation = display.getOrientation();
		if (oreation == Surface.ROTATION_0 || oreation == Surface.ROTATION_180) {
			Log.i(TAG, "Surface.ROTATION_0");// 竖屏
			setportlayout();
		} else {
			setlandlayout();
		}

		broadcast = new MyStatusBroadCast();
		IntentFilter filter = new IntentFilter(ContentCommon.CAMERA_INTENT_STATUS_CHANGE);
		registerReceiver(broadcast, filter);
	}
	
	@TargetApi(19)     
    private void setTranslucentStatus(boolean on) {    
        Window win = getWindow();    
        WindowManager.LayoutParams winParams = win.getAttributes();    
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        //WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        if (on) {    
            winParams.flags |= bits;    
        } else {    
            winParams.flags &= ~bits;    
        }    
        win.setAttributes(winParams);    
    }

	private boolean mStartThreadEnter = false;
	class startThread implements Runnable{
        
		@Override
		public void run() {
			synchronized(this){
				if(mStartThreadEnter){
					return;
				}
				mStartThreadEnter = true;
			Log.d(TAG,"startThread ThreadId:" +Thread.currentThread().getId());
			Log.d(TAG,"startThread mCurrentVideoPage:" +mCurrentVideoPage + " mCurrentVideoIndex:" + mCurrentVideoIndex + " mVideosPerScreen:" + mVideosPerScreen);
			
			//mStartedUUID.clear();
			dividerDID(mVideosPerScreen);
			
			if(mVideosPerScreen>0){
				mCurrentVideoPage = (mCurrentVideoIndex + mVideosPerScreen - 1)/mVideosPerScreen;
			}
			Log.d(TAG,"mCurrentVideoPage:" +mCurrentVideoPage);
			if(mCurrentVideoPage>=startUID.size()){
				mCurrentVideoPage = startUID.size()-1;
			}
			ArrayList<String> goingToStart = (ArrayList<String>) startUID.get(mCurrentVideoPage);
			if(mStartedUUID.size()!=0){
//				Log.d(TAG,"mStartedUUID SIZE:" + mStartedUUID.size() + " " + mStartedUUID.get(0) +" "+ mStartedUUID.get(1));
				for(int i=0;i<mStartedUUID.size();i++){
					String uuid = mStartedUUID.get(i);
					for(String gsuuid:goingToStart){
						if(uuid.contentEquals(gsuuid)){
							mStartedUUID.remove(i);
							i--;
						}
					}
				}

				for(int i = 0 ; i<mStartedUUID.size() ; i++){
					getLiveVideoBean(mStartedUUID.get(i)).stopLiveStream();
				}
				mStartedUUID.clear();
			}
			
			mStartedUUID = goingToStart;//(ArrayList<String>) startUID.get(mCurrentVideoPage);
			if(mCurrentVideoPage >0 && mCurrentVideoPage< mTotalVideoPage){
				for(int i = 0; i < mStartedUUID.size() ; i++){
//					Log.d(TAG,"start uuid:" + mStartedUUID.get(i));
					getLiveVideoBean(mStartedUUID.get(i)).startLiveStream();;
				}
			}else if(mCurrentVideoPage == 0){
				if(mBridgeService == null){
					Intent intent = new Intent();
					intent.setClass(CameraViewerActivity.this, BridgeService.class);
					bindService(intent, mConn, Context.BIND_AUTO_CREATE);
				}
				else{
					for(int i = 0; i < mStartedUUID.size() ; i++){
//						Log.d(TAG,"start uuid:" + mStartedUUID.get(i));
						getLiveVideoBean(mStartedUUID.get(i)).startLiveStream();
					}
				}
			}
			
			Log.d(TAG,"leave startThread");
			mStartThreadEnter = false;
			}
		}
	}
	
	void dividerDID(int mode){
//		Log.d(TAG,"MODE:"+mode);
		if(mode!=1 && mode != 4 && mode != 9 && mode != 16)
			mode = 4;
		startUID = new HashMap<Integer, ArrayList<String>>();
		
		int page = (mUUIDList.size() + mode -1)/mode;
//		Log.d(TAG,"mode:"+mode + " page:" + page);
		for(int i =0 ; i<page;i++){
			ArrayList<String> list = new ArrayList<String>();
			for(int j = 0;j<mode && mUUIDList.size()>(i*mode + j);j++){
				list.add(mUUIDList.get(i*mode + j));
//				Log.d(TAG,"page:" + i + " uuid["+j+"]:"+mUUIDList.get(i*mode + j));
			}
			startUID.put(i, list);
		}
		mTotalVideoPage = page;
    }
	private void setMultiStreamValue(String did, String value) {
		String url = did + "_MultiStream";
		SharedPreferences sp = getSharedPreferences(url, MODE_PRIVATE);
		sp.edit().putString(did, value).commit();
	}

	private void getMultiStreamValue(String did) {
		String url = did + "_MultiStream";
		SharedPreferences sp = getSharedPreferences(url, MODE_PRIVATE);
		MultiStreamValue = sp.getString(did, 4 + "");
	}

	private void setExitResult(){
		Intent data = new Intent();
		bmp = getCurrentLiveVideoBean().captureVideoBitmap(false);
		if(bmp!=null){
			int btmWidth = bmp.getWidth();
			int btmHeight = bmp.getHeight();
			float scaleW = ((float) 70) / btmWidth;
			float scaleH = ((float) 50) / btmHeight;
			Matrix matrix = new Matrix();
			matrix.postScale(scaleW, scaleH);
			
			Bitmap bt = Bitmap.createBitmap(bmp, 0, 0, btmWidth,
					btmHeight, matrix, true);
	
			data.putExtra("bmp", bt);
		}else{
			byte[] h264keyframe = getCurrentLiveVideoBean().getH264KeyFrame();
			int width = getCurrentLiveVideoBean().getVideoWidth();
			int height = getCurrentLiveVideoBean().getVideoHeight();
			if(h264keyframe!=null && width!=0 && height!=0){
				data.putExtra("h264", h264keyframe);
				data.putExtra("width", width);
				data.putExtra("height", height);
			}
		}
		
		data.putExtra("did", getCurrentLiveVideoBean().mUUID);
		setResult(2, data);
	}
	@SuppressWarnings("static-access")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(TAG,"onKeyDown");
		if (mPopupWindowProgress != null && mPopupWindowProgress.isShowing()) {
			mPopupWindowProgress.dismiss();

		}
		if (resolutionPopWindow != null && resolutionPopWindow.isShowing()) {
			resolutionPopWindow.dismiss();
		}
		if (morePopupWindow != null && morePopupWindow.isShowing()) {
			morePopupWindow.dismiss();
		}
		if (presetPopupWindow != null && presetPopupWindow.isShowing()) {
			presetPopupWindow.dismiss();

		}
		//
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			bManualExit = true;
			if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE ||
					getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) 
			{
				goPlayMode();
			}else{
				bProgress = false;
				setExitResult();
				finish();
			}
			
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (ptzPopupWindow != null && ptzPopupWindow.isShowing()) {
				Log.d(TAG, "isPtzShutDown");
				ptzPopupWindow.dismiss();
				//if (!cameraType65.equals("65")) {
					//textosd.setVisibility(View.VISIBLE);
					//textTimeStamp.setVisibility(View.VISIBLE);
				//}

			} else {

			}
//			showTop();
//			showBottom();
//			showLandscapeFloatMenu();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void defaultVideoParams() {
//		mIPCNetCamColorCfg_st.Brightness = 128;
//		mIPCNetCamColorCfg_st.Contrast = 128;
//		mIPCNetCamColorCfg_st.Saturtion = 128;
//		mIPCNetCamColorCfg_st.Acutance = 128;
//		mIPCNetCamColorCfg_st.Chroma = 128;
		if(mIPCNetCamColorCfg_st == null){
			IPCNetCamColorCfg_st tIPCNetCamColorCfg_st = mJSONStructProtocal.new IPCNetCamColorCfg_st();
			mCurCameraPicConf = -1;
			tIPCNetCamColorCfg_st.ViCh = 0;
			tIPCNetCamColorCfg_st.SetDefault = true;
			Cmds.P2PCameraGetPicCfg(mServiceStub,getCurrentLiveVideoBean().mUUID,tIPCNetCamColorCfg_st.toJSONString());
			return;
		}
		mIPCNetCamColorCfg_st.SetDefault = true;//ignore value set above.
		mIPCNetCamColorCfg_st.ViCh = 0;//channel
		
//		NativeCaller.PPPPCameraControl(getCurrentLiveVideoBean().mUUID, 1, 0);
//		NativeCaller.PPPPCameraControl(getCurrentLiveVideoBean().mUUID, 2, 128);
//		Cmds.P2PCameraPicCfg(mServiceStub,getCurrentLiveVideoBean().mUUID,0, ContentCommon.CAM_CFG_BRIGHTNESS, nBrightness);
//		Cmds.P2PCameraPicCfg(mServiceStub,getCurrentLiveVideoBean().mUUID,0, ContentCommon.CAM_CFG_CONTRAST, nContrast);
//		Cmds.P2PCameraPicCfg(mServiceStub,getCurrentLiveVideoBean().mUUID,0, ContentCommon.CAM_CFG_SATURATION, nSaturation);
		Cmds.P2PCameraPicCfg(mServiceStub,getCurrentLiveVideoBean().mUUID,mIPCNetCamColorCfg_st.toJSONString());
		showToast(R.string.ptz_default_vedio_params);
	}

	private ImageView forWardImage;
	private PopupWindow forWardPopupWindow = null;

	private void setViewVisible() {
		if (bProgress) {
			bProgress = false;
			//progressView.setVisibility(View.INVISIBLE);
			//osdView.setVisibility(View.VISIBLE);
			if (nP2PMode == ContentCommon.P2P_MODE_P2P_RELAY) {
				//startTimeout();
				forWardImage = new ImageView(CameraViewerActivity.this);
				forWardImage.setImageResource(R.drawable.forward);
				LinearLayout layout = new LinearLayout(CameraViewerActivity.this);
				layout.addView(forWardImage);
				AlphaAnimation alpha = new AlphaAnimation(0.1f, 1.0f);
				alpha.setDuration(500);
				alpha.setRepeatCount(Animation.INFINITE);
				alpha.setRepeatMode(Animation.REVERSE);
				forWardImage.setAnimation(alpha);
				alpha.start();
				imagePopupWindow = new PopupWindow(layout,
						WindowManager.LayoutParams.WRAP_CONTENT,
						WindowManager.LayoutParams.WRAP_CONTENT);
			}
//			getCameraParams(getCurrentLiveVideoBean().mUUID);
		}
	}

	// 拍照
	private void takePicture(final Bitmap bmp) {
		new Thread() {
			public void run() {
				savePicToSDcard(bmp);
			}
		}.start();
	}

	private synchronized void savePicToSDcard(final Bitmap bmp) {
		String strDate = getStrDate();
		dbUtil.open();
		Cursor cursor = dbUtil.queryVideoOrPictureByDate(getCurrentLiveVideoBean().mUUID, strDate,
				DatabaseUtil.TYPE_PICTURE);
		Log.d(TAG, "takepicture cursor.size:" + cursor.getCount());
		int seri = cursor.getCount() + 1;
		Log.d(TAG, "takepicture seri:" + seri);
		Log.d(TAG, "takepicture strDate:" + strDate);
		dbUtil.close();
		FileOutputStream fos = null;
		try {
			File div = new File(Environment.getExternalStorageDirectory(),
					"ipcam/takepic");
			if (!div.exists()) {
				div.mkdirs();
			}
			File file = new File(div, strDate + "_" + seri + getCurrentLiveVideoBean().mUUID + ".jpg");
			fos = new FileOutputStream(file);
			if (bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)) {
				fos.flush();
				Log.d(TAG, "takepicture success");
				mBridgeService.createVideoOrPic(getCurrentLiveVideoBean().mUUID,DatabaseUtil.TYPE_PICTURE, file.getAbsolutePath(),
						 strDate);
				
				//notify the ui update picture information.
				Intent intent = new Intent(ContentCommon.CAMERA_INTENT_MEDIA_CHANGE);
				intent.putExtra(ContentCommon.STR_CAMERA_ID, getCurrentLiveVideoBean().mUUID);
				sendBroadcast(intent);
				
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(
								CameraViewerActivity.this,
								getResources().getString(
										R.string.ptz_takepic_ok), 0).show();
					}
				});
			}

		} catch (Exception e) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(
							CameraViewerActivity.this,
							getResources().getString(R.string.ptz_takepic_fail),
							0).show();
				}
			});

			Log.d(TAG, "exception:" + e.getMessage());
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				fos = null;
			}
		}
	}

	private String getStrDate() {
		Date d = new Date();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd_HH_mm");
		String strDate = f.format(d);
		return strDate;
	}

	private DateBean dateBean;

	private Handler msgStreamCodecHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (nStreamCodecType == ContentCommon.P2P_STREAM_TYPE_JPEG) {
				// textCodec.setText("JPEG");
			} else {
				// textCodec.setText("H.264");
			}
		}
	};

	private String touchtalk = "tt";
	private String touchmicro = "tttt";
	public static Map<String, Map<Object, Object>> istouchlist = new HashMap<String, Map<Object, Object>>();
	public static Map<String, Map<Object, Object>> istouchlist1 = new HashMap<String, Map<Object, Object>>();

	void addtouchlist(String str, String istouch) {
		if (istouchlist.size() != 0) {
			if (istouchlist.containsKey(getCurrentLiveVideoBean().mUUID)) {
				istouchlist.remove(getCurrentLiveVideoBean().mUUID);
			}
		}
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put(str, istouch);
		istouchlist.put(getCurrentLiveVideoBean().mUUID, map);
	}

	void getTouch() {
		if (istouchlist.containsKey(getCurrentLiveVideoBean().mUUID)) {
			Map<Object, Object> map = istouchlist.get(getCurrentLiveVideoBean().mUUID);
			Object mess = map.get(touchtalk);
			if (mess.equals("true")) {
//				istouchtalk = true;
			} else {
//				istouchtalk = false;
			}
		}
	}

	void addtouchmilist(String str, String istouch) {
		if (istouchlist1.size() != 0) {
			if (istouchlist1.containsKey(getCurrentLiveVideoBean().mUUID)) {
				istouchlist1.remove(getCurrentLiveVideoBean().mUUID);
			}
		}
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put(str, istouch);
		istouchlist1.put(getCurrentLiveVideoBean().mUUID, map);
	}

	void getMicro() {
		if (istouchlist1.containsKey(getCurrentLiveVideoBean().mUUID)) {
			Map<Object, Object> map = istouchlist1.get(getCurrentLiveVideoBean().mUUID);
			Object mess = map.get(touchmicro);
			if (mess.equals("true")) {
//				istouchmicro = true;
			} else {
//				istouchmicro = false;
			}
		}
	}

	private void setlandlayout() {
		// 横屏
		Log.d(TAG, "setlandlayout now........");
		setFullScreen(true);
		Resources res = getResources();
		d = res.getDrawable(R.drawable.vidicon);

		findView();
		
		findViewById(R.id.potrait_framelayout).setVisibility(View.GONE);
//		findViewById(R.id.landscape_layout).setVisibility(View.VISIBLE);
		
//		findViewById(R.id.ptz_mutilscreen_panel).setOnClickListener(this);
//		ptzPlatform_land.setVisibility(View.VISIBLE);
//		ptzPlatform.setVisibility(View.GONE);
//		ptzPlatform1.setVisibility(View.GONE);

		InitParams();
		dbUtil = new DatabaseUtil(this);
		AudioBuffer = new CustomBuffer();
		audioPlayer = new AudioPlayer(AudioBuffer);

		// customAudioRecorder = new CustomAudioRecorder(this);
		myvideoRecorder = new CamViewCustomVideoRecord(this, getCurrentLiveVideoBean().mUUID);

//		playHolder = playSurface.getHolder();
//		playHolder.setFormat(PixelFormat.RGB_565);
//		playHolder.addCallback(videoCallback);
//
//		playSurface.setOnTouchListener(this);
//		playSurface.setLongClickable(true);

		// streamType = 10;
		// // new add
		// Intent intent = new Intent();
		// intent.setClass(PlayActivity.this, BridgeService.class);
		// bindService(intent, mConn, Context.BIND_AUTO_CREATE);

//		getCameraParams(getCurrentLiveVideoBean().mUUID);
//		getTalkorMicrophone();
		dismissTopAnim = AnimationUtils.loadAnimation(this,
				R.anim.ptz_top_anim_dismiss);
		showTopAnim = AnimationUtils.loadAnimation(this,
				R.anim.ptz_top_anim_show);
		showAnim = AnimationUtils.loadAnimation(this,
				R.anim.ptz_otherset_anim_show);
		dismissAnim = AnimationUtils.loadAnimation(this,
				R.anim.ptz_otherset_anim_dismiss);

		// prompt user how to control ptz when first enter play
		SharedPreferences sharePreferences = getSharedPreferences("ptzcontrol",
				MODE_PRIVATE);
		isPTZPrompt = sharePreferences.getBoolean("ptzcontrol", false);
		if (!isPTZPrompt) {
			Editor edit = sharePreferences.edit();
			edit.putBoolean("ptzcontrol", true);
			edit.commit();
		}
		
		playmode = FULLSCREEN;
	}
	
	private void setportlayout() {
		// 竖屏
		Log.d(TAG, "setportlayout now........");
		setFullScreen(false);
		Resources res = getResources();
		d = res.getDrawable(R.drawable.vidicon);
		findView();
		findViewById(R.id.potrait_framelayout).setVisibility(View.VISIBLE);

		InitParams();
		dbUtil = new DatabaseUtil(this);
		AudioBuffer = new CustomBuffer();
		audioPlayer = new AudioPlayer(AudioBuffer);

		myvideoRecorder = new CamViewCustomVideoRecord(this, getCurrentLiveVideoBean().mUUID);

		// prompt user how to control ptz when first enter play
		SharedPreferences sharePreferences = getSharedPreferences("ptzcontrol",
				MODE_PRIVATE);
		isPTZPrompt = sharePreferences.getBoolean("ptzcontrol", false);
		if (!isPTZPrompt) {
			Editor edit = sharePreferences.edit();
			edit.putBoolean("ptzcontrol", true);
			edit.commit();
		}
		
		playmode = MAGNIFY;
	}

	String cameraType65 = null;

	private void getDataFromOther() {		
		Intent intent = getIntent();
		if(intent != null){
			mUUIDList = new ArrayList<String>();
			mNameList = new ArrayList<String>();
			mStartedUUID = new ArrayList<String>();
			mPositonList = new ArrayList<Integer>();
			mP2PModeList = new ArrayList<Integer>();
			
			mUUIDList = intent.getStringArrayListExtra("didlist");
			mNameList = intent.getStringArrayListExtra("namelist");
			mPositonList = intent.getIntegerArrayListExtra("positionlist");
			mP2PModeList = intent.getIntegerArrayListExtra("stypelist");
			mVideosPerScreen = (Integer)intent.getExtras().get("playmode");
			mTotalVideoPage = (mUUIDList.size() + mVideosPerScreen - 1)/mVideosPerScreen;
		}else{
			finish();
		}
		
		mIPCPtzCmdList = new ArrayList<IPCPtzCmd_st>();
		for(String uuid:mUUIDList){
			mIPCPtzCmdList.add(mJSONStructProtocal.new IPCPtzCmd_st());
		}
	}

	private void InitParams() {
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		nSurfaceWidth = dm.widthPixels;
		nSurfaceHeight = dm.heightPixels;
		//textosd.setText(strName);
		// cameraName.setText(strName);
	}

	private void StartAudio() {
		synchronized (this) {
			AudioBuffer.ClearAll();
			audioPlayer.AudioPlayStart();
			NativeCaller.P2PStartAudio(getCurrentLiveVideoBean().mUUID);
			
			Cmds.requestVideoStreamDirectly(mServiceStub, getCurrentLiveVideoBean().mUUID, 0, mHighQuality,ContentCommon.STREAM_TYPE_COMP , true);
		}
	}

	private void StopAudio() {
		synchronized (this) {
			audioPlayer.AudioPlayStop();
			AudioBuffer.ClearAll();
			
			Cmds.requestVideoStreamDirectly(mServiceStub, getCurrentLiveVideoBean().mUUID, 0, mHighQuality,ContentCommon.STREAM_TYPE_AUDIO , false);
			
			NativeCaller.P2PStopAudio(getCurrentLiveVideoBean().mUUID);
		}
	}

	private void StartTalk() {
		if(customAudioRecorder == null){
			customAudioRecorder = new CustomAudioRecorder(this);
		}
		if (customAudioRecorder != null) {
			Log.i(TAG, "startTalk");
			customAudioRecorder.StartRecord();
			NativeCaller.P2PStartTalk(getCurrentLiveVideoBean().mUUID);
			
			Cmds.start2Talk(mServiceStub,getCurrentLiveVideoBean().mUUID,true);
		}
	}

	private void StopTalk() {
		if (customAudioRecorder != null) {
			Log.i(TAG, "stopTalk");
			customAudioRecorder.StopRecord();

			Cmds.start2Talk(mServiceStub,getCurrentLiveVideoBean().mUUID,false);
			
			NativeCaller.P2PStopTalk(getCurrentLiveVideoBean().mUUID);
		}
	}

	private void releaseTalk() {
		if(customAudioRecorder!=null){
			customAudioRecorder.releaseRecord();
			customAudioRecorder = null;
		}
		Log.i(TAG, "releaseTalk");
	}

	private void findView() {
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
			Log.d(TAG, "findView ORIENTATION_LANDSCAPE");
		}else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
		{
			Log.d(TAG, "findView ORIENTATION_PORTRAIT");
//			ptzPlayMode = (ImageButton) findViewById(R.id.ptz_playmode);
//			ptzPlayMode.setOnClickListener(this);
			mVideoViewPager = (ViewPager) findViewById(R.id.multi_video_viewpager);
			
		}
		bProgress = true;

		mVideoView = (LiveStreamVideoView)findViewById(R.id.showimage);
//		imgDown = (ImageView) findViewById(R.id.imgdown);
//		imgDown.setOnTouchListener(this);
//		imgLeft = (ImageView) findViewById(R.id.imgleft);
//		imgLeft.setOnTouchListener(this);
//		imgRight = (ImageView) findViewById(R.id.imgright);
//		imgRight.setOnTouchListener(this);
//		imgUp = (ImageView) findViewById(R.id.imgup);
//		imgUp.setOnTouchListener(this);
		
//		imgpresentcenter = (ImageView) findViewById(R.id.imgprsentcenter);

		voice_btn = (Button) findViewById(R.id.voice_btn);
		voice_btn.setOnClickListener(this);
//		ptz_audio_btn = (ImageButton) findViewById(R.id.ptz_audio_btn);
//		ptz_audio_btn.setOnClickListener(this);
		ptzTakepicBtn = (Button) findViewById(R.id.takepic_btn);
		ptzTakepicBtn.setOnClickListener(this);
		ptzTakeVideoBtn = (Button) findViewById(R.id.takevideo_btn);
		ptzTakeVideoBtn.setOnClickListener(this);
		menu_btn = (Button) findViewById(R.id.menu_btn);
		menu_btn.setOnClickListener(this);
//		findViewById(R.id.ptz_reversal_picture).setOnClickListener(this);

		btnBack = (ImageButton) findViewById(R.id.back);
		btnBack.setOnClickListener(this);
		play_btn = (Button) findViewById(R.id.play_btn);
		play_btn.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		
		if(mLiveVideos == null){
			mLiveVideos = new ArrayList<LiveVideoBean>();
			mListVideoView = new ArrayList<View>();
		}

    	initVideoViews();
    	
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && viewPager == null)
		{
			viewPager = (ViewPager) findViewById(R.id.viewpager);
	    	listView = new ArrayList<View>();
	    	LayoutInflater inflater = getLayoutInflater();
	    	View lay1 = inflater.inflate(R.layout.lay1, null);
	    	listView.add(lay1);
	    	record_btn = (Button) lay1.findViewById(R.id.record_btn);
			record_btn.setOnClickListener(this);
			mic_btn = (Button) lay1.findViewById(R.id.mic_btn);
			mic_btn.setOnClickListener(this);
			resolution_btn = (Button) lay1.findViewById(R.id.resolution_btn);
			resolution_btn.setOnClickListener(this);
			if(mHighQuality){
				resolution_btn.setBackgroundResource(R.drawable.btn_hd);
			}else{
				resolution_btn.setBackgroundResource(R.drawable.btn_sd);
			}
			refresh_btn = (Button) lay1.findViewById(R.id.camviewer_pic_btn);
			refresh_btn.setOnClickListener(this);
			
			View lay2 = inflater.inflate(R.layout.lay2, null);
	    	listView.add(lay2);
	    	lay2.findViewById(R.id.ptz_left).setOnTouchListener(this);
	    	lay2.findViewById(R.id.ptz_right).setOnTouchListener(this);
	    	lay2.findViewById(R.id.ptz_up).setOnTouchListener(this);
	    	lay2.findViewById(R.id.ptz_down).setOnTouchListener(this);
	    	
//	    	View lay3 = inflater.inflate(R.layout.lay3, null);
//	    	lay3.findViewById(R.id.ptz_one_video_btn).setOnClickListener(this);
//	    	lay3.findViewById(R.id.ptz_four_video_btn).setOnClickListener(this);
//	    	lay3.findViewById(R.id.ptz_nine_video_btn).setOnClickListener(this);
//	    	lay3.findViewById(R.id.ptz_sixteen_video_btn).setOnClickListener(this);
//	    	listView.add(lay3);
	    	
	    	for (int i = 0; i < listView.size(); i++) {
	    		View view = (View) listView.get(i);
	    		view.setOnTouchListener(this);
	    	}
	    	
	    	listDots = new ArrayList<View>();
	    	listDots.add(findViewById(R.id.dot01));
	    	listDots.add(findViewById(R.id.dot02));
//	    	listDots.add(findViewById(R.id.dot03));
	    	
	    	viewPager.setAdapter(new MyPagerAdapter(listView));
	    	MyPagerChangeListener myPagerChangeListener = new MyPagerChangeListener();
	    	myPagerChangeListener.setMaxCount(listView.size());
	    	viewPager.setOnPageChangeListener(myPagerChangeListener);
	    	viewPager.setCurrentItem(listView.size() * 100);
	    	viewPager.getAdapter().notifyDataSetChanged();
		}
	}

	private void initVideoViews(){
		mListVideoView.clear();
		
		int videos_page_num = (mUUIDList.size() + mVideosPerScreen - 1)/mVideosPerScreen;
		if(mCurrentVideoIndex >= mVideosPerScreen){
			mCurrentVideoPage = (mCurrentVideoIndex + mVideosPerScreen - 1)/mVideosPerScreen;
		}else{
			mCurrentVideoPage = 0;
		}
		
//		for(int i=0;i<videos_page_num;i++){
//	    	LayoutInflater inflater = getLayoutInflater();
//	    	View lay = null;
//	    	
//	    	if(mVideosPerScreen == 9){
//	    		lay = inflater.inflate(R.layout.multi_video_lay9, null);
//	    	}else if(mVideosPerScreen == 16){
//	    		lay = inflater.inflate(R.layout.multi_video_lay16, null);
//	    	}else if(mVideosPerScreen == 4){
//	    		lay = inflater.inflate(R.layout.multi_video_lay4, null);
//	    	}else{
//	    		lay = inflater.inflate(R.layout.multi_video_lay1, null);
//	    		mVideosPerScreen = 1;
//	    	}

	    	for(int j=0;j<mVideosPerScreen;j++){
	    		LiveVideoBean bean;
	    		int index = j;
//				View view1 = LayoutInflater.from(this).inflate(R.layout.video_view, null);
				if(index >= mUUIDList.size()){
					bean = new LiveVideoBean(null,null,ContentCommon.P2P_MODE_P2P_NORMAL
							,(LiveStreamVideoView) findViewById(R.id.video_view));
							//,(LiveStreamVideoView) lay.findViewById(R.id.showimage1+j));
				}else{
					if(mLiveVideos.size()>index){
						bean = mLiveVideos.get(index);//bean should not be null.
						if(bean.mVideoView!=null){
//							bean.mVideoView.onDetachedFromWindow();
//							bean.mVideoView.clearCanvas();
							bean.mVideoView.setVisibility(View.INVISIBLE);
						}
						bean.mVideoView = (LiveStreamVideoView) findViewById(R.id.showimage);
					}else{
						bean = new LiveVideoBean(mUUIDList.get(index),mNameList.get(index),
								mP2PModeList.get(index),(LiveStreamVideoView) findViewById(R.id.showimage));
					}
				}
				bean.mIndex = index;
				bean.layout = findViewById(R.id.video_view);
				if(index == mCurrentVideoIndex && mVideosPerScreen!=1)//init selected view
					bean.layout.setBackgroundResource(R.color.green);
				bean.mPausePic = (ImageView) findViewById(R.id.video_pause);
				bean.par = (ProgressBar) findViewById(R.id.showpp);
				bean.tvname = (TextView) findViewById(R.id.showname);
				bean.tv = (TextView) findViewById(R.id.limittime);
//				bean.layout.addView(view1);
				if(bean.mVideoView != null){
					bean.mVideoView.setOnClickListener(this);
					bean.mVideoView.setTag(bean);
				}
				
				bean.init();
				
				if(mLiveVideos.size() <= index)
					mLiveVideos.add(bean);
	    	}
			
	    	mListVideoView.add(findViewById(R.id.video_view));
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// setContentView(R.layout.play);
		super.onConfigurationChanged(newConfig);
		Log.d(TAG,"onConfigurationChanged");
		
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			Log.i(TAG, "竖屏");
			if (morePopupWindow != null && morePopupWindow.isShowing()) {
				morePopupWindow.dismiss();
				morePopupWindow = null;
			}
			isShowtoping = false;
			setportlayout();

		} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			Log.i(TAG, "横屏--");
			// 调scrollView中子类的位置
			if (morePopupWindow != null && morePopupWindow.isShowing()) {
				morePopupWindow.dismiss();
				morePopupWindow = null;
			}
			isShowtoping = false;
			setlandlayout();
		}
		
		if (mAutoChangeOrientation && (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT || 
				getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
		}
		mChangeOrientation = false;
	}

	private boolean existSdcard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void onDestroy() {
		mPlaying = false;
		if (mPopupWindowProgress != null && mPopupWindowProgress.isShowing()) {
			mPopupWindowProgress.dismiss();
		}
		for(LiveVideoBean lvb:mLiveVideos){
			if(lvb != null && lvb.mUUID != null){
				//NativeCaller.StopPPPPLivestream(lvb.mUUID);
				lvb.stopLiveStream(true);
			}
		}
		StopAudio();
		StopTalk();
		releaseTalk();
		
		if(broadcast!=null){
			unregisterReceiver(broadcast);
			broadcast = null;
		}
		
		super.onDestroy();
		mBridgeService.unbindSetNull(mServiceStub);
		mBridgeService.unbindSetNull("CameraViewerActivity");
		unbindService(mConn);
		if (myvideoRecorder != null) {
			myvideoRecorder.stopRecordVideo();
		}
		Log.d(TAG, "PlayActivity onDestroy");
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch(event.getAction()& MotionEvent.ACTION_MASK) { 
		  case MotionEvent.ACTION_DOWN:{
			switch(v.getId()){
			case R.id.ptz_up:
//			case R.id.imgup:
			{
				IPCPtzCmd_st ptzcmd = mIPCPtzCmdList.get(mCurrentVideoIndex);
				ptzcmd.MotoCmd = ContentCommon.CMD_PTZ_UP;
				ptzcmd.PtzBaseMsg.PtzEnable = 1;
				Cmds.setDevPtz(mServiceStub,mUUIDList.get(mCurrentVideoIndex),ptzcmd);
				Log.d(TAG, "up");
			}break;
			case R.id.ptz_down:
//			case R.id.imgdown:
			{
				IPCPtzCmd_st ptzcmd = mIPCPtzCmdList.get(mCurrentVideoIndex);
				ptzcmd.MotoCmd = ContentCommon.CMD_PTZ_DOWN;
				ptzcmd.PtzBaseMsg.PtzEnable = 1;
				Cmds.setDevPtz(mServiceStub,mUUIDList.get(mCurrentVideoIndex),ptzcmd);
				Log.d(TAG, "down");
			}break;
			case R.id.ptz_left:
//			case R.id.imgleft:
			{
				IPCPtzCmd_st ptzcmd = mIPCPtzCmdList.get(mCurrentVideoIndex);
				ptzcmd.MotoCmd = ContentCommon.CMD_PTZ_LEFT;
				ptzcmd.PtzBaseMsg.PtzEnable = 1;
				Cmds.setDevPtz(mServiceStub,mUUIDList.get(mCurrentVideoIndex),ptzcmd);
				Log.d(TAG, "left");
			}break;
			case R.id.ptz_right:
//			case R.id.imgright:
			{
				IPCPtzCmd_st ptzcmd = mIPCPtzCmdList.get(mCurrentVideoIndex);
				ptzcmd.MotoCmd = ContentCommon.CMD_PTZ_RIGHT;
				ptzcmd.PtzBaseMsg.PtzEnable = 1;
				Cmds.setDevPtz(mServiceStub,mUUIDList.get(mCurrentVideoIndex),ptzcmd);
				Log.d(TAG, "right");
			}break;
			}
		  }break;
		  case MotionEvent.ACTION_UP:{
			  switch(v.getId()){
				case R.id.ptz_up:
//				case R.id.imgup:
				{
					IPCPtzCmd_st ptzcmd = mIPCPtzCmdList.get(mCurrentVideoIndex);
					ptzcmd.MotoCmd = ContentCommon.CMD_PTZ_UP_STOP;
					Cmds.setDevPtz(mServiceStub,mUUIDList.get(mCurrentVideoIndex),ptzcmd);
					Log.d(TAG, "up");
//					onFlowButtonClick = true;
				}break;
				case R.id.ptz_down:
//				case R.id.imgdown:
				{
					IPCPtzCmd_st ptzcmd = mIPCPtzCmdList.get(mCurrentVideoIndex);
					ptzcmd.MotoCmd = ContentCommon.CMD_PTZ_DOWN_STOP;
					Cmds.setDevPtz(mServiceStub,mUUIDList.get(mCurrentVideoIndex),ptzcmd);
					Log.d(TAG, "down");
//					onFlowButtonClick = true;
				}break;
				case R.id.ptz_left:
//				case R.id.imgleft:
				{
					IPCPtzCmd_st ptzcmd = mIPCPtzCmdList.get(mCurrentVideoIndex);
					ptzcmd.MotoCmd = ContentCommon.CMD_PTZ_LEFT_STOP;
					Cmds.setDevPtz(mServiceStub,mUUIDList.get(mCurrentVideoIndex),ptzcmd);
					Log.d(TAG, "left");
//					onFlowButtonClick = true;
				}break;
				case R.id.ptz_right:
//				case R.id.imgright:
				{
					IPCPtzCmd_st ptzcmd = mIPCPtzCmdList.get(mCurrentVideoIndex);
					ptzcmd.MotoCmd = ContentCommon.CMD_PTZ_RIGHT_STOP;
					Cmds.setDevPtz(mServiceStub,mUUIDList.get(mCurrentVideoIndex),ptzcmd);
					Log.d(TAG, "right");
//					onFlowButtonClick = true;
				}break;
			}
		  }break;
			  default:;
		}
		 
		return gt.onTouchEvent(event);
	}

	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;

	private int mode = NONE;
	private float oldDist;
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	private PointF start = new PointF();
	private PointF mid = new PointF();
	float mMaxZoom = 2.0f;// �?��缩放比例
	float mMinZoom = 0.3125f;// �?��缩放比例
	float originalScale;
	float baseValue;
	protected Matrix mBaseMatrix = new Matrix();
	protected Matrix mSuppMatrix = new Matrix();
	private Matrix mDisplayMatrix = new Matrix();
	private final float[] mMatrixValues = new float[9];

	protected void zoomTo(float scale, float centerX, float centerY) {
		Log.d("zoomTo", "zoomTo scale:" + scale);
		if (scale > mMaxZoom) {
			scale = mMaxZoom;
		} else if (scale < mMinZoom) {
			scale = mMinZoom;
		}

		float oldScale = getScale();
		float deltaScale = scale / oldScale;
		Log.d("deltaScale", "deltaScale:" + deltaScale);
		mSuppMatrix.postScale(deltaScale, deltaScale, centerX, centerY);
	}

	protected Matrix getImageViewMatrix() {
		mDisplayMatrix.set(mBaseMatrix);
		mDisplayMatrix.postConcat(mSuppMatrix);
		return mDisplayMatrix;
	}

	protected float getScale(Matrix matrix) {
		return getValue(matrix, Matrix.MSCALE_X);
	}

	protected float getScale() {
		return getScale(mSuppMatrix);
	}

	protected float getValue(Matrix matrix, int whichValue) {
		matrix.getValues(mMatrixValues);
		return mMatrixValues[whichValue];
	}

	// 缩放到一定大�?
	private float spacing(MotionEvent event) {
		try {
			float x = event.getX(0) - event.getX(1);
			float y = event.getY(0) - event.getY(1);
			return (float) Math.sqrt(x * x + y * y);
		} catch (Exception e) {
		}
		return 0;
	}

	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		Log.d(TAG,"onDown");
		if (resolutionPopWindow != null && resolutionPopWindow.isShowing()) {
			resolutionPopWindow.dismiss();
		}
		if (mPopupWindowProgress != null && mPopupWindowProgress.isShowing()) {
			mPopupWindowProgress.dismiss();
		}
		if (presetPopupWindow != null && presetPopupWindow.isShowing()) {
			presetPopupWindow.dismiss();
		}
		if (morePopupWindow != null && morePopupWindow.isShowing()) {
			morePopupWindow.dismiss();
		}

		return false;
	}
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	private boolean mShowFloatOptionMenu = true;
	@SuppressWarnings("static-access")
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		Log.d(TAG,"onSingleTapUp");
		if (mPopupWindowProgress != null && mPopupWindowProgress.isShowing()) {
			mPopupWindowProgress.dismiss();
		}
		if (presetPopupWindow != null && presetPopupWindow.isShowing()) {
			presetPopupWindow.dismiss();

		}
		if (morePopupWindow != null && morePopupWindow.isShowing()) {
			morePopupWindow.dismiss();
		}

		if (ptzPopupWindow != null && ptzPopupWindow.isShowing()) {
			Log.d(TAG, "isPtzShutDown");
			ptzPopupWindow.dismiss();

		} else {
			Log.d(TAG, "isPtzShowing");

		}

		return false;
	}
	
	private LiveVideoBean getCurrentLiveVideoBean(){
		if(mLiveVideos.size()<=mCurrentVideoIndex){
			Log.e(TAG,"mCurrentVideoIndex:" + mCurrentVideoIndex + " mLiveVideos.size:" + mLiveVideos.size());
			return null;
		}
		return mLiveVideos.get(mCurrentVideoIndex);
	}
	private LiveVideoBean getLiveVideoBean(String uuid){
		if(mLiveVideos == null)
			return null;
		for(LiveVideoBean lvb:mLiveVideos){
			if(lvb != null && lvb.mUUID != null){
				if(lvb.mUUID.contentEquals(uuid))
					return lvb;
			}
		}
		return null;
	}
	/**
	 * BridgeService Feedback execute
	 * 
	 * **/
	public void setVideoData(String did, byte[] videobuf, int h264Data,
			int len, int width, int height) {
		
		if(mChangeOrientation || !mPlaying)
			return;
		if(mVideosPerScreen>1){
			LiveVideoBean lvb = getLiveVideoBean(did);
			if(lvb == null){
				Log.d(TAG, "getLiveVideoBean null[" + did + "]");
				return;
			}
			lvb.setVideoData(videobuf, h264Data, len, width, height);
		}else{
			LiveVideoBean lvb = getCurrentLiveVideoBean();
			
			if(lvb == null){
				Log.d(TAG, "getLiveVideoBean null[" + did + "]");
				return;
			}
			if(!lvb.mUUID.contentEquals(did))
				return;
			lvb.setVideoData(videobuf, h264Data, len, width, height);
		}
	}

	/**
	 * BridgeService Feedback execute
	 * **/
	public void setAudioData(byte[] pcm, int len) {
		Log.d("new", "PlayActivity setAudioData AudioData: len :+ " + len);
		if (!audioPlayer.isAudioPlaying()) {
			return;
		}
		CustomBufferHead head = new CustomBufferHead();
		CustomBufferData data = new CustomBufferData();
		head.length = len;
		head.startcode = AUDIO_BUFFER_START_CODE;
		data.head = head;
		data.data = pcm;
		AudioBuffer.addData(data);
	}

	/**
	 * BridgeService Feedback execute
	 * **/
	public void callBackH264Data(String uuid ,byte[] h264, int type, int size,int width, int height) {

		if(mHWDecode)
			setVideoData(uuid, h264, 1,	size, width, height);
		//
		if (isTakeVideo) {
			Date date = new Date();
			long time = date.getTime();
			int tspan = (int) (time - videotime);
			Log.d(TAG, "play  tspan:" + tspan);
			videotime = time;
			if (videoRecorder != null) {
				videoRecorder.VideoRecordData(type, h264, size, 0, tspan);
			}
		}
	}

	@Override
	public void AudioRecordData(byte[] data, int len) {
		if (bAudioRecordStart && len > 0) {
			Log.i(TAG, "AudioRecordData:" + len);
			NativeCaller.P2PTalkAudioData(getCurrentLiveVideoBean().mUUID, data, len);
		}
	}

	private boolean dismissPopupWindow(){
		if(mPopupWindowProgress!=null && mPopupWindowProgress.isShowing()){
			mPopupWindowProgress.dismiss();
			return true;
		}
		if (morePopupWindow!=null && morePopupWindow.isShowing()) {
			morePopupWindow.dismiss();
			return true;
		}
		if (presetPopupWindow!=null&&presetPopupWindow.isShowing()) {
			presetPopupWindow.dismiss();
			return true;
		}
		
		return false;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.showimage:
//			Log.d(TAG,"showimage");
//			dismissPopupWindow();
//			
//			LiveVideoBean lvb = (LiveVideoBean) v.getTag();
//			if(lvb!=null && lvb.mUUID != null){
//				if(mCurrentVideoIndex == lvb.mIndex){
//					if(mVideosPerScreen == 1){
//					}else{
//						mVideosPerScreen = 1;
//						mChangeOrientation = true;
//						initVideoViews();
//						mChangeOrientation = false;
//						
//						startThread s = new startThread();
//						new Thread(s).start();
//					}
//				}else{
//					LiveVideoBean clvb = getCurrentLiveVideoBean();
//					clvb.layout.setBackgroundResource(R.color.white);
//					mCurrentVideoIndex = lvb.mIndex;
//					lvb.layout.setBackgroundResource(R.color.green);
//				}
//			}
//			break;
//		case R.id.ptz_one_video_btn:
////		case R.id.ptz_fl_one:
//		{
//			if(mVideosPerScreen == 1)
//				break;
//			mVideosPerScreen = 1;
//			mChangeOrientation = true;
//			initVideoViews();
//			mChangeOrientation = false;
//			
//			startThread s = new startThread();
//			new Thread(s).start();
//		}break;
//		case R.id.ptz_four_video_btn:
////		case R.id.ptz_fl_four:
//		{
//			if(mVideosPerScreen == 4)
//				break;
//			mVideosPerScreen = 4;
//			mChangeOrientation = true;
//			initVideoViews();
//			mChangeOrientation = false;
//			
//			startThread s = new startThread();
//			new Thread(s).start();
//		}break;
//		case R.id.ptz_nine_video_btn:
////		case R.id.ptz_fl_nine:
//		{
//			if(mVideosPerScreen == 9)
//				break;
//			mVideosPerScreen = 9;
//			mChangeOrientation = true;
//			initVideoViews();
//			mChangeOrientation = false;
//			
//			startThread s = new startThread();
//			new Thread(s).start();
//		}break;
//		case R.id.ptz_sixteen_video_btn:
////		case R.id.ptz_fl_sixteen:
//		{
//			if(mVideosPerScreen == 16)
//				break;
//			mVideosPerScreen = 16;
//			mChangeOrientation = true;
//			initVideoViews();
//			mChangeOrientation = false;
//			
//			startThread s = new startThread();
//			new Thread(s).start();
//		}break;
//		case R.id.hw_res_btn:{
//			Intent intent = new Intent(this,
//					SettingGPIOActivity.class);
//			intent.putExtra(ContentCommon.STR_CAMERA_ID, getCurrentLiveVideoBean().mUUID);
//			startActivityForResult(intent, 200);
//		}break;
		case R.id.menu_btn:{
			CameraParamsBean cpb = mBridgeService.getCamera(getCurrentLiveVideoBean().mUUID);
			if(cpb == null)
				break;
			
			String pos = "0";
			Intent intentevent = new Intent(this,
					DeviceAttributeActivity.class);
			intentevent.putExtra(ContentCommon.STR_CAMERA_ID, getCurrentLiveVideoBean().mUUID);
			intentevent.putExtra("position", pos);
			startActivity(intentevent);
		}break;
		case R.id.record_btn:
			if(mBridgeService.getVideoPath()==null)
				break;
			Intent intentrec=new Intent(this,ShowLocalVideoActivity.class);
			intentrec.putExtra("did", getCurrentLiveVideoBean().mUUID);
			intentrec.putExtra("filepath", mBridgeService.getVideoPath());
//			intentrec.putExtra("position", 0);
//			intentrec.putExtra("videotime", "");
//			intentrec.putExtra("arrayList", getCurrentLiveVideoBean().mUUID);
			startActivity(intentrec);
			break;
		case R.id.back:
			bManualExit = true;
			setExitResult();
			if (isUpDown) {
				isUpDown = false;
			}
			if (isLeftRight) {
			}
			finish();
			break;
//		case R.id.ptz_narrow:
//		case R.id.ptz_narrow1:
//		case R.id.ptz_narrow1_l:
//		case R.id.ptz_narrow_l:
//			goNarrow();
//			break;
//		case R.id.ptz_big:
//		case R.id.ptz_big_l:
//		case R.id.ptz_big1_l:
//		case R.id.ptz_big1:
//			goBig();
//			break;
//		case R.id.preset:
//		case R.id.preset_l:
//		case R.id.ptz_preset:
//			if(dismissPopupWindow())
//				return;
//			showPresetPopupWindow();
//			break;
		case R.id.ptz_hori_mirror:
			dismissPopupWindow();
			Cmds.PPPPCameraControl(mServiceStub,getCurrentLiveVideoBean().mUUID ,0 , true, false);
			break;
		case R.id.ptz_vert_mirror:
			dismissPopupWindow();
			Cmds.PPPPCameraControl(mServiceStub,getCurrentLiveVideoBean().mUUID ,0 , false, true);
			break;
//		case R.id.ptz_reversal_picture:
//			if(dismissPopupWindow())
//				return;
//			mAutoChangeOrientation = false;
//			goPlayMode();
//			break;
//		case R.id.moreptz:
//		case R.id.moreptz_l:
//		case R.id.ptz_menu_more:
//			if(dismissPopupWindow())
//				return;
//			showMorePopupWindow();
//			break;
		case R.id.voice_btn:
			goAudio();
			break;
//		case R.id.ptz_audio_btn:
//			goAudiL();
//			break;
//		case R.id.ptz_microphone:
//		case R.id.ptz_microphone_l:
		case R.id.mic_btn:
			goMicroPhone();
			break;
//		case R.id.ptz_takepic:
		case R.id.takepic_btn:
//		case R.id.ptz_take_picture:
			if(dismissPopupWindow())
				return;
			if (existSdcard()) {// 拍照
				bmp = this.getCurrentLiveVideoBean().captureVideoBitmap(true);
				takePicture(bmp);
			} else {
				showToast(R.string.ptz_takepic_save_fail);
			}
			break;
//		case R.id.ptz_takevideo:
//		case R.id.ptz_takevideo_l:
		case R.id.takevideo_btn:
			goTakeVideo();
			break;
//		case R.id.ptz_takevideo1:
//		case R.id.ptz_takevideo1_l:
//		case R.id.ptz_take_video:
//			goTakeVideoL();
//			break;
//		case R.id.ptz_brightness:
//			if (morePopupWindow != null && morePopupWindow.isShowing()) {
//				morePopupWindow.dismiss();
//			}
//			if (mPopupWindowProgress != null
//					&& mPopupWindowProgress.isShowing()) {
//				mPopupWindowProgress.dismiss();
//				mPopupWindowProgress = null;
//			}
//			setCameraPicConf(ContentCommon.CAM_CFG_BRIGHTNESS);
//			break;
		case R.id.ptz_saturation:
			if (morePopupWindow.isShowing()) {
				morePopupWindow.dismiss();
			}
			if (mPopupWindowProgress != null
					&& mPopupWindowProgress.isShowing()) {
				mPopupWindowProgress.dismiss();
				mPopupWindowProgress = null;
			}
			setCameraPicConf(ContentCommon.CAM_CFG_SATURATION);
			break;
		case R.id.ptz_acutance:
			if (morePopupWindow.isShowing()) {
				morePopupWindow.dismiss();
			}
			if (mPopupWindowProgress != null
					&& mPopupWindowProgress.isShowing()) {
				mPopupWindowProgress.dismiss();
				mPopupWindowProgress = null;
			}
			setCameraPicConf(ContentCommon.CAM_CFG_ACUTANCE);
			break;
		case R.id.ptz_contrast:
			if (morePopupWindow.isShowing()) {
				morePopupWindow.dismiss();
			}
			if (mPopupWindowProgress != null
					&& mPopupWindowProgress.isShowing()) {
				mPopupWindowProgress.dismiss();
				mPopupWindowProgress = null;
			}
			setCameraPicConf(ContentCommon.CAM_CFG_CONTRAST);
			break;
		case R.id.camviewer_pic_btn:
			Intent intent = new Intent(CameraViewerActivity.this,
					TestSnapshot.class);
			intent.putExtra(ContentCommon.STR_CAMERA_ID, this.getCurrentLiveVideoBean().mUUID);
			startActivityForResult(intent,2);
			break;
		case R.id.resolution_btn:
//		case R.id.resolution_btn_lan:
			changeResolution();
			break;
//		case R.id.ptz_playmode:
//		case R.id.ptz_playmode1:
//		case R.id.ptz_playmode_l:
//		case R.id.ptz_playmode1_l:
//			if(dismissPopupWindow())
//				return;
//			mAutoChangeOrientation = false;
//			goPlayMode();
//			break;
		case R.id.ptz_default_set:
			if (morePopupWindow.isShowing()) {
				morePopupWindow.dismiss();
			}
			dismissBrightAndContrastProgress();
			defaultVideoParams();
			break;
		case R.id.hz50:
			if (morePopupWindow.isShowing()) {
				morePopupWindow.dismiss();
			}
			//Cmds.PPPPCameraControl(getCurrentLiveVideoBean().mUUID, 3, 0);
			showToast(R.string.hz50);
			break;
		case R.id.hz60:
			if (morePopupWindow.isShowing()) {
				morePopupWindow.dismiss();
			}
			//Cmds.PPPPCameraControl(getCurrentLiveVideoBean().mUUID, 3, 1);
			showToast(R.string.hz60);
			break;
		case R.id.ir_off:
			if (morePopupWindow.isShowing()) {
				morePopupWindow.dismiss();
			}
			//Cmds.PPPPCameraControl(getCurrentLiveVideoBean().mUUID, 14, 0);
			Cmds.P2PCamIRCutOp(mServiceStub,getCurrentLiveVideoBean().mUUID,0,false);
			showToast(R.string.ir_off);
			break;
		case R.id.ir_on:
			if (morePopupWindow.isShowing()) {
				morePopupWindow.dismiss();
			}
			//Cmds.PPPPCameraControl(getCurrentLiveVideoBean().mUUID, 14, 1);
			Cmds.P2PCamIRCutOp(mServiceStub,getCurrentLiveVideoBean().mUUID,1,true);
			showToast(R.string.ir_on);
			break;
		case R.id.mode_outdoor:
			if (morePopupWindow.isShowing()) {
				morePopupWindow.dismiss();
			}
			nEnvMode = nEnvMode == 1?0:1;
			Cmds.P2PCameraEnvSetting(mServiceStub,getCurrentLiveVideoBean().mUUID,nEnvMode,0,7);
			//Cmds.PPPPCameraControl(getCurrentLiveVideoBean().mUUID, 3, 2);
			if(nEnvMode == 1){
				showToast(R.string.mode_outdoor_open);
			}else{
				showToast(R.string.mode_indoor_open);
			}
			break;
		case R.id.play_btn:
		{
//			playOrPause();
			mAccGyroSensorUtils.setWorkMode(!mAccGyroSensorUtils.getWorkMode());
			if(mAccGyroSensorUtils.getWorkMode()){
				play_btn.setText("自用");
			}else{
				play_btn.setText("他用");
			}
		}break;
//		case R.id.ptz_mutilscreen_panel:{
//			View ptzmpv = findViewById(R.id.ptz_mutilscreen_panel_lay);
//			if(ptzmpv!=null){
//				if(ptzmpv.getVisibility() == View.VISIBLE){
//					ptzmpv.setVisibility(View.GONE);
//				}else{
//					ptzmpv.setVisibility(View.VISIBLE);
//				}
//			}
//		}break;
		}
	}
	
	private boolean mPlaying = true;
	private void playOrPause(){
		if(mPlaying){
			mPlaying = false;
			play_btn.setBackgroundResource(R.drawable.btn_play_normal);
//			ptzVideoPlay.setBackgroundResource(R.drawable.btn_tool_pause_normal);
			getCurrentLiveVideoBean().pause();
		}else{
			mPlaying = true;
			play_btn.setBackgroundResource(R.drawable.btn_pause_selected);
//			ptzVideoPlay.setBackgroundResource(R.drawable.btn_tool_play_normal);
			getCurrentLiveVideoBean().play();
		}
	}
	private int getHorizontalFlip() {
		int flipValue = 0;
		if (flip == 0) {
			flipValue = 2;
		} else if (flip == 1) {
			flipValue = 3;
		} else if (flip == 2) {
			flipValue = 0;
		} else {
			flipValue = 1;
		}
		flip = flipValue;
		Log.i("new", "水平flipValue:" + flip);
		return flipValue;

	}

	private int getVerticlFlip() {
		int flipValue = 0;
		if (flip == 0) {
			flipValue = 1;
		} else if (flip == 1) {
			flipValue = 0;
		} else if (flip == 2) {
			flipValue = 3;
		} else {
			flipValue = 2;
		}
		flip = flipValue;
		Log.i("new", "垂直flipValue:" + flip);
		return flipValue;
	}

	private void setFullScreen(boolean changefull){
		if (changefull) {
			WindowManager.LayoutParams params = getWindow().getAttributes();  
			params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;  
			getWindow().setAttributes(params);  
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		} else {
			WindowManager.LayoutParams params = getWindow().getAttributes();  
			params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);  
			getWindow().setAttributes(params);  
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);  
		}
	}
	private void goPlayMode() {
		Log.d(TAG,"isport:" + isport);
		mChangeOrientation = true;
		if (isport) {
			if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE ||
					getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				Log.d(TAG,"Orientation1:" + "SCREEN_ORIENTATION_LANDSCAPE");
				playmode = FULLSCREEN;
				
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				findViewById(R.id.potrait_framelayout).setVisibility(View.VISIBLE);
//				findViewById(R.id.landscape_layout).setVisibility(View.GONE);
				if (isImagePopupwindow) {
					if (imagePopupWindow.isShowing()
							&& imagePopupWindow != null) {
						imagePopupWindow.dismiss();
						AlphaAnimation alpha = new AlphaAnimation(0.1f, 1.0f);
						alpha.setDuration(500);
						alpha.setRepeatCount(Animation.INFINITE);
						alpha.setRepeatMode(Animation.REVERSE);
						image.setAnimation(alpha);
						imagePopupWindow.showAtLocation(getCurrentLiveVideoBean().mVideoView,
								Gravity.RIGHT | Gravity.TOP, -133, 302);
						imagePopupWindow.update();
					}

				}

			} else if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ||
					getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				Log.d(TAG,"Orientation1:" + "SCREEN_ORIENTATION_PORTRAIT");
				playmode = MAGNIFY;
		
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				findViewById(R.id.potrait_framelayout).setVisibility(View.GONE);
//				findViewById(R.id.landscape_layout).setVisibility(View.VISIBLE);
				if (isImagePopupwindow) {
					if (imagePopupWindow.isShowing()
							&& imagePopupWindow != null) {
						imagePopupWindow.dismiss();
						AlphaAnimation alpha = new AlphaAnimation(0.1f, 1.0f);
						alpha.setDuration(500);
						alpha.setRepeatCount(Animation.INFINITE);
						alpha.setRepeatMode(Animation.REVERSE);
						image.setAnimation(alpha);
						imagePopupWindow.showAtLocation(getCurrentLiveVideoBean().mVideoView,
								Gravity.RIGHT | Gravity.TOP, 0, 48);
						imagePopupWindow.update();
					}
				}

			}
		} else {
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				playmode = MAGNIFY;
				Log.d(TAG,"Orientation2:" + "ORIENTATION_LANDSCAPE");
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				findViewById(R.id.potrait_framelayout).setVisibility(View.VISIBLE);
//				findViewById(R.id.landscape_layout).setVisibility(View.GONE);
				isport = true;
				if (isImagePopupwindow) {
					if (imagePopupWindow.isShowing()
							&& imagePopupWindow != null) {
						imagePopupWindow.dismiss();
						AlphaAnimation alpha = new AlphaAnimation(0.1f, 1.0f);
						alpha.setDuration(500);
						alpha.setRepeatCount(Animation.INFINITE);
						alpha.setRepeatMode(Animation.REVERSE);
						image.setAnimation(alpha);
						imagePopupWindow.showAtLocation(getCurrentLiveVideoBean().mVideoView,
								Gravity.RIGHT | Gravity.TOP, -133, 302);
						imagePopupWindow.update();
					}
				}
			} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				Log.d(TAG,"Orientation2:" + "SCREEN_ORIENTATION_PORTRAIT");
				playmode = FULLSCREEN;
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				findViewById(R.id.potrait_framelayout).setVisibility(View.GONE);
//				findViewById(R.id.landscape_layout).setVisibility(View.VISIBLE);
				isport = true;
				if (isImagePopupwindow) {
					updateland();
				}
			}

		}
	}

	private void goTakeVideoL() {
		dismissBrightAndContrastProgress();

		if (isTakeVideo) {
			showToast(R.string.ptz_takevideo_end);
			Log.d(TAG, "停止录像");
			NativeCaller.RecordLocal(getCurrentLiveVideoBean().mUUID,0);
			isTakeVideo = false;
//			ptzTakeVideo.setBackgroundResource(R.drawable.btn_tool_record);
			myvideoRecorder.stopRecordVideo();
			if (imagePopupWindow.isShowing() && imagePopupWindow != null) {
				imagePopupWindow.dismiss();
			}
		} else {
			isTakeVideo = true;
			showToast(R.string.ptz_takevideo_begin);
			Log.d(TAG, "开始录像");
			videotime = (new Date()).getTime();
//			ptzTakeVideo.setBackgroundResource(R.drawable.btn_tool_record_highlighted);
			NativeCaller.RecordLocal(getCurrentLiveVideoBean().mUUID,1);
			if (isH264) {
				myvideoRecorder.startRecordVideo(1);
			} else {
				myvideoRecorder.startRecordVideo(2);
			}

			image = new ImageView(CameraViewerActivity.this);
			image.setImageResource(R.drawable.red);
			LinearLayout layout = new LinearLayout(CameraViewerActivity.this);
			layout.addView(image);
			AlphaAnimation alpha = new AlphaAnimation(0.1f, 1.0f);
			alpha.setDuration(500);
			alpha.setRepeatCount(Animation.INFINITE);
			alpha.setRepeatMode(Animation.REVERSE);
			image.setAnimation(alpha);
			alpha.start();
			imagePopupWindow = new PopupWindow(layout,
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.WRAP_CONTENT);
			isImagePopupwindow = true;
			if (playmode == FULLSCREEN) {
				imagePopupWindow.showAtLocation(getCurrentLiveVideoBean().mVideoView,
						Gravity.RIGHT | Gravity.TOP, 0, 48);
				imagePopupWindow.update();
			} else {
				int x = getCurrentLiveVideoBean().mVideoView.getLeft();
				int y = getCurrentLiveVideoBean().mVideoView.getTop();
				imagePopupWindow.showAtLocation(getCurrentLiveVideoBean().mVideoView,
						Gravity.RIGHT | Gravity.TOP, x, y);
			}

		}
	}

	private void goTakeVideo() {
		dismissBrightAndContrastProgress();

		if (isTakeVideo) {
			showToast(R.string.ptz_takevideo_end);
			Log.d(TAG, "停止录像");
			NativeCaller.RecordLocal(getCurrentLiveVideoBean().mUUID,0);
			isTakeVideo = false;
			ptzTakeVideoBtn.setBackgroundResource(R.drawable.btn_record);
			myvideoRecorder.stopRecordVideo();
			if (imagePopupWindow.isShowing() && imagePopupWindow != null) {
				imagePopupWindow.dismiss();
			}
		} else {
			isTakeVideo = true;
			showToast(R.string.ptz_takevideo_begin);
			Log.d(TAG, "开始录像");
			videotime = (new Date()).getTime();
			ptzTakeVideoBtn.setBackgroundResource(R.drawable.btn_recording);
			NativeCaller.RecordLocal(getCurrentLiveVideoBean().mUUID,1);
			if (isH264) {
				myvideoRecorder.startRecordVideo(1);
			} else {
				myvideoRecorder.startRecordVideo(2);
			}

			image = new ImageView(CameraViewerActivity.this);
			image.setImageResource(R.drawable.red);
			
			LinearLayout layout = new LinearLayout(CameraViewerActivity.this);
			layout.addView(image);
			AlphaAnimation alpha = new AlphaAnimation(0.1f, 1.0f);
			alpha.setDuration(500);
			alpha.setRepeatCount(Animation.INFINITE);
			alpha.setRepeatMode(Animation.REVERSE);
			image.setAnimation(alpha);
			alpha.start();
			imagePopupWindow = new PopupWindow(layout,
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.WRAP_CONTENT);
			isImagePopupwindow = true;
			if (playmode == FULLSCREEN) {
				imagePopupWindow.showAtLocation(getCurrentLiveVideoBean().mVideoView,
						Gravity.RIGHT | Gravity.BOTTOM, 0, 48);
				imagePopupWindow.update();
			} else {
				int x = getCurrentLiveVideoBean().mVideoView.getLeft();
				int y = getCurrentLiveVideoBean().mVideoView.getTop();
				imagePopupWindow.showAtLocation(getCurrentLiveVideoBean().mVideoView,
						Gravity.RIGHT | Gravity.TOP, 10, 155);
			}
		}
	}

	private void goMicroPhone() {
		dismissBrightAndContrastProgress();
		if (!isTalking) {
			if (bAudioRecordStart) {
				Log.d(TAG, "停止说话");
				isMcriophone = false;
				bAudioRecordStart = false;
				addMicrophone(mcriophone, "false");
				addAudioRecord(audiostart, "false");
				mic_btn.setBackgroundResource(R.drawable.btn_talk_disabled);
				StopTalk();
			} else {
				Log.d(TAG, "开始说话");
				isMcriophone = true;
				bAudioRecordStart = true;
				addMicrophone(mcriophone, "true");
				addAudioRecord(audiostart, "true");
				mic_btn.setBackgroundResource(R.drawable.btn_talk_normal);
				StartTalk();
			}
		} else {
			isTalking = false;
			bAudioStart = false;
			addTalking(talk, "false");
			addAudioStart(audio, "false");
			voice_btn.setBackgroundResource(R.drawable.btn_voice_normal);
			StopAudio();
			isMcriophone = true;
			bAudioRecordStart = true;
			addMicrophone(mcriophone, "true");
			addAudioRecord(audiostart, "true");
			mic_btn.setBackgroundResource(R.drawable.btn_talk_normal);
			StartTalk();
		}
		addtouchlist(touchtalk, "true");
		addtouchmilist(touchmicro, "true");
	}

	private void goMicroPhoneL() {
		dismissBrightAndContrastProgress();
		if (!isTalking) {
			if (bAudioRecordStart) {
				Log.d(TAG, "停止说话");
				isMcriophone = false;
				bAudioRecordStart = false;
				addMicrophone(mcriophone, "false");
				addAudioRecord(audiostart, "false");
				StopTalk();
			} else {
				Log.d(TAG, "开始说话");
				isMcriophone = true;
				bAudioRecordStart = true;
				addMicrophone(mcriophone, "true");
				addAudioRecord(audiostart, "true");
				StartTalk();
			}
		} else {
			isTalking = false;
			bAudioStart = false;
			addTalking(talk, "false");
			addAudioStart(audio, "false");
			StopAudio();
			isMcriophone = true;
			bAudioRecordStart = true;
			addMicrophone(mcriophone, "true");
			addAudioRecord(audiostart, "true");
			StartTalk();
		}
		addtouchlist(touchtalk, "true");
		addtouchmilist(touchmicro, "true");
	}

	private void goAudio() {
		dismissBrightAndContrastProgress();
		if (!isMcriophone) {
			if (bAudioStart) {
				Log.d(TAG, "goAudio 没有声音");
				isTalking = false;
				bAudioStart = false;
				addTalking(talk, "false");
				addAudioStart(audio, "false");
				voice_btn.setBackgroundResource(R.drawable.btn_voice_normal);
//				ptz_audio_btn.setBackgroundResource(R.drawable.btn_tool_voice_normal);
				StopAudio();
			} else {
				Log.d(TAG, "有声");
				isTalking = true;
				bAudioStart = true;
				addTalking(talk, "true");
				addAudioStart(audio, "true");
				voice_btn.setBackgroundResource(R.drawable.btn_voice_selected);
//				ptz_audio_btn.setBackgroundResource(R.drawable.btn_tool_voice_pressed);
				StartAudio();
			}

		} else {
			isMcriophone = false;
			bAudioRecordStart = false;
			addMicrophone(mcriophone, "false");
			addAudioRecord(audiostart, "false");
			mic_btn.setBackgroundResource(R.drawable.btn_talk_disabled);
			StopTalk();
			isTalking = true;
			bAudioStart = true;
			addTalking(talk, "true");
			addAudioStart(audio, "true");
			voice_btn.setBackgroundResource(R.drawable.btn_voice_selected);
//			ptz_audio_btn.setBackgroundResource(R.drawable.btn_tool_voice_pressed);
			StartAudio();
		}
		addtouchmilist(touchmicro, "true");
		addtouchlist(touchtalk, "true");
	}

	private void goAudiL() {
		dismissBrightAndContrastProgress();
		if (!isMcriophone) {
			if (bAudioStart) {
				Log.d(TAG, "没有声音");
				isTalking = false;
				bAudioStart = false;
				addTalking(talk, "false");
				addAudioStart(audio, "false");
				voice_btn.setBackgroundResource(R.drawable.btn_voice_normal);
//				ptz_audio_btn.setBackgroundResource(R.drawable.btn_tool_voice_normal);
				StopAudio();
			} else {
				Log.d(TAG, "有声");
				isTalking = true;
				bAudioStart = true;
				addTalking(talk, "true");
				addAudioStart(audio, "true");
				voice_btn.setBackgroundResource(R.drawable.btn_voice_selected);
//				ptz_audio_btn.setBackgroundResource(R.drawable.btn_tool_voice_pressed);
				StartAudio();
			}

		} else {
			isMcriophone = false;
			bAudioRecordStart = false;
			addMicrophone(mcriophone, "false");
			addAudioRecord(audiostart, "false");
			mic_btn.setBackgroundResource(R.drawable.btn_talk_disabled);
			StopTalk();
			isTalking = true;
			bAudioStart = true;
			addTalking(talk, "true");
			addAudioStart(audio, "true");
			voice_btn.setBackgroundResource(R.drawable.btn_voice_selected);
//			ptz_audio_btn.setBackgroundResource(R.drawable.btn_tool_voice_pressed);
			StartAudio();
		}
		addtouchmilist(touchmicro, "true");
		addtouchlist(touchtalk, "true");
	}

	private void goVertTour() {
		if (isUpDown) {
			isUpDown = false;
			Cmds.PPPPPTZControl(getCurrentLiveVideoBean().mUUID,
					ContentCommon.CMD_PTZ_UP_DOWN_STOP);
			Log.d(TAG, "垂直巡视停止:" + ContentCommon.CMD_PTZ_UP_DOWN_STOP);
		} else {
			isUpDown = true;
			Cmds.PPPPPTZControl(getCurrentLiveVideoBean().mUUID, ContentCommon.CMD_PTZ_UP_DOWN);
			Log.d(TAG, "垂直巡视�?��:" + ContentCommon.CMD_PTZ_UP_DOWN);
		}
	}

	private void goHoriTour() {
		if (isLeftRight) {
			isLeftRight = false;
			Cmds.PPPPPTZControl(getCurrentLiveVideoBean().mUUID,
					ContentCommon.CMD_PTZ_LEFT_RIGHT_STOP);
			Log.d(TAG, "水平巡视停止:" + ContentCommon.CMD_PTZ_LEFT_RIGHT_STOP);
		} else {
			isLeftRight = true;
			Cmds.PPPPPTZControl(getCurrentLiveVideoBean().mUUID,
					ContentCommon.CMD_PTZ_LEFT_RIGHT);
			Log.d(TAG, "水平巡视�?��:" + ContentCommon.CMD_PTZ_LEFT_RIGHT);
		}
	}

	private void goBig() {
		//Cmds.PPPPCameraControl(getCurrentLiveVideoBean().mUUID, 18,1);
		Toast.makeText(getApplicationContext(),
				getResources().getText(R.string.big), 500).show();
	}

	private void goNarrow() {
		//Cmds.PPPPCameraControl(getCurrentLiveVideoBean().mUUID, 17,1);
		Toast.makeText(getApplicationContext(),
				getResources().getText(R.string.narrow), 500).show();
	}

	private ImageView image;
	private boolean isImagePopupwindow = false;

	void updateport() {
		if (imagePopupWindow.isShowing() && imagePopupWindow != null) {
			imagePopupWindow.dismiss();
			int x = getCurrentLiveVideoBean().mVideoView.getLeft();
			int y = getCurrentLiveVideoBean().mVideoView.getTop();
			imagePopupWindow.showAtLocation(getCurrentLiveVideoBean().mVideoView, Gravity.RIGHT
					| Gravity.TOP, x, y);
			imagePopupWindow.update();
		}
	}

	void updateland() {
		if (imagePopupWindow.isShowing() && imagePopupWindow != null) {
			imagePopupWindow.dismiss();
			AlphaAnimation alpha = new AlphaAnimation(0.1f, 1.0f);
			alpha.setDuration(500);
			alpha.setRepeatCount(Animation.INFINITE);
			alpha.setRepeatMode(Animation.REVERSE);
			image.setAnimation(alpha);
			imagePopupWindow.showAtLocation(getCurrentLiveVideoBean().mVideoView, Gravity.RIGHT
					| Gravity.TOP, 0, 48);
			imagePopupWindow.update();
		}
	}

	private void dismissBrightAndContrastProgress() {
		if (mPopupWindowProgress != null && mPopupWindowProgress.isShowing()) {
			mPopupWindowProgress.dismiss();
			mPopupWindowProgress = null;
		}
	}

	public void showgetImage(int position) {
		if (presetPopupWindow.isShowing()) {
			presetPopupWindow.dismiss();
		}
		
		PresetBean bean = presentadapter.getPresetBean(position);
		Cmds.setOperatePreset(mServiceStub, getCurrentLiveVideoBean().mUUID,bean.pid,0);
//		switch (position) {
//		case 0:
//			Cmds.PPPPPTZControl(getCurrentLiveVideoBean().mUUID,
//					ContentCommon.CMD_PTZ_PREFAB_BIT_RUN0);
//			break;
//		case 1:
//			Cmds.PPPPPTZControl(getCurrentLiveVideoBean().mUUID,
//					ContentCommon.CMD_PTZ_PREFAB_BIT_RUN1);
//			break;
//		case 2:
//			Cmds.PPPPPTZControl(getCurrentLiveVideoBean().mUUID,
//					ContentCommon.CMD_PTZ_PREFAB_BIT_RUN2);
//			break;
//		case 3:
//			Cmds.PPPPPTZControl(getCurrentLiveVideoBean().mUUID,
//					ContentCommon.CMD_PTZ_PREFAB_BIT_RUN3);
//			break;
//		case 4:
//			Cmds.PPPPPTZControl(getCurrentLiveVideoBean().mUUID,
//					ContentCommon.CMD_PTZ_PREFAB_BIT_RUN4);
//			break;
//		}
	}

	public Drawable scaleBitmap() {
		bmp = getCurrentLiveVideoBean().captureVideoBitmap(true);
		if(bmp == null)
			return null;
		int btmWidth = bmp.getWidth();
		int btmHeight = bmp.getHeight();
		float scaleW = ((float) 50) / btmWidth;
		float scaleH = ((float) 40) / btmHeight;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleW, scaleH);
		Bitmap bt = Bitmap.createBitmap(bmp, 0, 0, btmWidth, btmHeight, matrix,
				true);
		Drawable drawable = new BitmapDrawable(bt);
		return drawable;
	}

	private void operatePreset(final int position,final int op){
		String title = null;
		if(op == 0){//replace
			title = getResources().getString(R.string.replace_present);
		}else if(op == 1){//delete
			title = getResources().getString(R.string.delete_present);
		}
		AlertDialog.Builder adb = new AlertDialog.Builder(CameraViewerActivity.this);
		adb.setMessage(title)
				.setPositiveButton(
						getResources().getString(R.string.str_ok),
						new DialogInterface.OnClickListener() {
		
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								PresetBean bean = presentadapter.getPresetBean(position);
								switch(op){
								case 0:
									Cmds.setPresetConfig(mServiceStub, getCurrentLiveVideoBean().mUUID, bean.pid,7,bean.name);
									
									Drawable bitmap1 = scaleBitmap();
									presentadapter.add(position, bitmap1);
									break;
								case 1://delete
									Cmds.setOperatePreset(mServiceStub, getCurrentLiveVideoBean().mUUID,bean.pid,1);
									
									if(mIPCNETPrePointList_st!=null){
										mIPCNETPrePointList_st.deletePreset(bean.pid);
									}
									presentadapter.deletePreset(bean.pid);
									break;
								}
								presentadapter.notifyDataSetChanged();
								
							}
						})
				.setNegativeButton(
						getResources().getString(R.string.str_cancel),
						new DialogInterface.OnClickListener() {
		
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}
						}).create().show();
	}
	public void show(final int position) {
		new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.preset)).setItems(
						new String[] { getResources().getString(R.string.replace_present_tile),
								getResources().getString(R.string.operation_delete) }, new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								operatePreset(position,which);
								dialog.dismiss();
							}}).show();
			

	}

	private ListAdapter getSimpleAdapter(int[] image_array, String[] name_array) {
		ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < name_array.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("image", image_array[i]);
			map.put("name", name_array[i]);
			data.add(map);
		}
		SimpleAdapter simpleadapter = new SimpleAdapter(this, data,
				R.layout.presetpopcell, new String[] { "image", "name" },
				new int[] { R.id.imagepop, R.id.namepop });
		return simpleadapter;
	}

	public static Map<String, Map<Object, Object>> talklist = new HashMap<String, Map<Object, Object>>();
	public static Map<String, Map<Object, Object>> audiostartlist = new HashMap<String, Map<Object, Object>>();
	public static Map<String, Map<Object, Object>> phonelist = new HashMap<String, Map<Object, Object>>();
	public static Map<String, Map<Object, Object>> audiorecodlist = new HashMap<String, Map<Object, Object>>();

	public String talk = "talk";
	public String audio = "audiostart";
	public String mcriophone = "MicroPhone";
	public String audiostart = "audiorecod";

	void addAudioRecord(String String, String istalk) {
		if (audiorecodlist.size() != 0) {
			if (audiorecodlist.containsKey(getCurrentLiveVideoBean().mUUID)) {
				audiorecodlist.remove(getCurrentLiveVideoBean().mUUID);
			}
		}
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put(String, istalk);
		audiorecodlist.put(getCurrentLiveVideoBean().mUUID, map);
	}

	void getAudioRecord() {
		if (audiorecodlist.containsKey(getCurrentLiveVideoBean().mUUID)) {
			Map<Object, Object> map = audiorecodlist.get(getCurrentLiveVideoBean().mUUID);
			if (map.containsKey(audiostart)) {
				Object mess = map.get(audiostart);
				if (mess.equals("true")) {
					bAudioRecordStart = true;
				} else {
					bAudioRecordStart = false;
				}
			}
		}
	}

	void addMicrophone(String String, String istalk) {
		if (phonelist.size() != 0) {
			if (phonelist.containsKey(getCurrentLiveVideoBean().mUUID)) {
				phonelist.remove(getCurrentLiveVideoBean().mUUID);
			}
		}
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put(String, istalk);
		phonelist.put(getCurrentLiveVideoBean().mUUID, map);
	}

	void getMicrophone() {
		if (phonelist.containsKey(getCurrentLiveVideoBean().mUUID)) {
			Map<Object, Object> map = phonelist.get(getCurrentLiveVideoBean().mUUID);
			if (map.containsKey(mcriophone)) {
				Object mess = map.get(mcriophone);
				if (mess.equals("true")) {
					isMcriophone = true;
				} else {
					isMcriophone = false;
				}
			}
		}
	}

	void addAudioStart(String String, String istalk) {
		if (audiostartlist.size() != 0) {
			if (audiostartlist.containsKey(getCurrentLiveVideoBean().mUUID)) {
				audiostartlist.remove(getCurrentLiveVideoBean().mUUID);
			}
		}
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put(String, istalk);
		audiostartlist.put(getCurrentLiveVideoBean().mUUID, map);
	}

	void getAudioStart() {
		if (audiostartlist.containsKey(getCurrentLiveVideoBean().mUUID)) {
			Map<Object, Object> map = audiostartlist.get(getCurrentLiveVideoBean().mUUID);
			if (map.containsKey(audio)) {
				Object mess = map.get(audio);
				if (mess.equals("true")) {
					bAudioStart = true;
				} else {
					bAudioStart = false;
				}
			}
		}
	}

	/**
	 * 添加
	 * 
	 * @param String
	 * @param istalk
	 *            isTalking的�?
	 */
	void addTalking(String String, String istalk) {
		if (talklist.size() != 0) {
			if (talklist.containsKey(getCurrentLiveVideoBean().mUUID)) {
				talklist.remove(getCurrentLiveVideoBean().mUUID);
			}
		}
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put(String, istalk);
		talklist.put(getCurrentLiveVideoBean().mUUID, map);
	}

	void getTalking() {
		if (talklist.containsKey(getCurrentLiveVideoBean().mUUID)) {
			Map<Object, Object> map = talklist.get(getCurrentLiveVideoBean().mUUID);
			if (map.containsKey(talk)) {
				Object mess = map.get(talk);
				if (mess.equals("true")) {
					isTalking = true;
				} else {
					isTalking = false;
				}
			}
		}
	}

	public static Map<String, Map<Object, Object>> reslutionlist = new HashMap<String, Map<Object, Object>>();

	/**
	 * 增加reslution
	 */
	private void addReslution(String mess, boolean isfast) {
		if (reslutionlist.size() != 0) {
			if (reslutionlist.containsKey(getCurrentLiveVideoBean().mUUID)) {
				reslutionlist.remove(getCurrentLiveVideoBean().mUUID);
			}
		}
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put(mess, isfast);
		reslutionlist.put(getCurrentLiveVideoBean().mUUID, map);
	}
	
	@Override  
    protected void onResume() {
        super.onResume();

        startThread s = new startThread();
		new Thread(s).start();
    }

	@Override
    protected void onPause() {  
        super.onPause();
        
        for(LiveVideoBean lvb:mLiveVideos){
			if(lvb != null && lvb.mUUID != null){
				//NativeCaller.StopPPPPLivestream(lvb.mUUID);
				lvb.stopLiveStream(true);
			}
		}
    }
//	/**
//	 * 获取reslution
//	 */
//	private void getReslution() {
//		if (reslutionlist.containsKey(getCurrentLiveVideoBean().mUUID)) {
//			Map<Object, Object> map = reslutionlist.get(getCurrentLiveVideoBean().mUUID);
//			if (map.containsKey("qvga")) {
//				isqvga = true;
//			} else if (map.containsKey("vga")) {
//				isvga = true;
//			} else if (map.containsKey("qvga1")) {
//				isqvga1 = true;
//			} else if (map.containsKey("vga1")) {
//				isvga1 = true;
//			} else if (map.containsKey("p720")) {
//				isp720 = true;
//			} else if (map.containsKey("high")) {
//				ishigh = true;
//			} else if (map.containsKey("middle")) {
//				ismiddle = true;
//			} else if (map.containsKey("max")) {
//				ismax = true;
//			}
//		}
//	}

	private boolean mHighQuality = false;
	private void refreshStream(){
		Cmds.requestVideoStreamDirectly(mServiceStub, getCurrentLiveVideoBean().mUUID, 0, mHighQuality,ContentCommon.STREAM_TYPE_VIDEO , true);
		showToast(R.string.refresh_done);
	}
	private void changeResolution(){
			if(mHighQuality){
				mHighQuality = false;
				if(resolution_btn!=null)
				resolution_btn.setBackgroundResource(R.drawable.btn_sd);
//				if(ptzResolution_lan!=null)
//				ptzResolution_lan.setBackgroundResource(R.drawable.btn_tool_sd);
				Cmds.requestVideoStreamDirectly(mServiceStub, getCurrentLiveVideoBean().mUUID, 0, false ,ContentCommon.STREAM_TYPE_VIDEO , true);
			}else{
				mHighQuality = true;
				if(resolution_btn!=null)
				resolution_btn.setBackgroundResource(R.drawable.btn_hd);
//				if(ptzResolution_lan!=null)
//				ptzResolution_lan.setBackgroundResource(R.drawable.btn_tool_hd);
				Cmds.requestVideoStreamDirectly(mServiceStub, getCurrentLiveVideoBean().mUUID, 0, true, ContentCommon.STREAM_TYPE_VIDEO ,true);
			}
	}

	private boolean mOrientationPanelVisible = false;
	public void onOrientationPanelShow(View v){
		if(mOrientationPanelVisible){
			mOrientationPanelVisible = false;
//			findViewById(R.id.ptz_orientation_panel).setVisibility(View.VISIBLE);
		}else{
			mOrientationPanelVisible = true;
//			findViewById(R.id.ptz_orientation_panel).setVisibility(View.GONE);
		}
	}

	private int mCurCameraPicConf;
	private void setCameraPicConf(final int type) {
//		Log.i(TAG, "type:" + type + "  bInitCameraParam:"
//				+ bInitCameraParam);
//		if (!bInitCameraParam) {
//			return;
//		}
		if(mIPCNetCamColorCfg_st == null){
			IPCNetCamColorCfg_st tIPCNetCamColorCfg_st = mJSONStructProtocal.new IPCNetCamColorCfg_st();
			mCurCameraPicConf = type;
			tIPCNetCamColorCfg_st.ViCh = 0;
			Cmds.P2PCameraGetPicCfg(mServiceStub,getCurrentLiveVideoBean().mUUID,tIPCNetCamColorCfg_st.toJSONString());
			return;
		}
		mIPCNetCamColorCfg_st.SetDefault = false;

		int width = getWindowManager().getDefaultDisplay().getWidth();
		LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.brightprogress, null);
		SeekBar seekBar = (SeekBar) layout.findViewById(R.id.brightseekBar1);
		seekBar.setMax(255);
		switch (type) {
		case ContentCommon.CAM_CFG_BRIGHTNESS:
			seekBar.setProgress(mIPCNetCamColorCfg_st.Brightness);
			break;
		case ContentCommon.CAM_CFG_CONTRAST:
			seekBar.setProgress(mIPCNetCamColorCfg_st.Contrast);
			break;
		case ContentCommon.CAM_CFG_COLOR_DEPTH:
			break;
		case ContentCommon.CAM_CFG_SATURATION:
			seekBar.setProgress(mIPCNetCamColorCfg_st.Saturtion);
			break;
		case ContentCommon.CAM_CFG_ACUTANCE:
			seekBar.setProgress(mIPCNetCamColorCfg_st.Acutance);
			break;
		default:
			break;
		}
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int progress = seekBar.getProgress();
				switch (type) {
				case ContentCommon.CAM_CFG_BRIGHTNESS:// 亮度
					mIPCNetCamColorCfg_st.Brightness = progress;
//					NativeCaller.PPPPCameraControl(getCurrentLiveVideoBean().mUUID, BRIGHT, nBrightness);
//					Cmds.PPPPCameraControl(getCurrentLiveVideoBean().mUUID, BRIGHT, nBrightness);
//					Cmds.P2PCameraPicCfg(mServiceStub,getCurrentLiveVideoBean().mUUID,mIPCNetCamColorCfg_st.toJSONString());
					break;
				case ContentCommon.CAM_CFG_CONTRAST://
					mIPCNetCamColorCfg_st.Contrast = progress;
//					NativeCaller.PPPPCameraControl(getCurrentLiveVideoBean().mUUID, CONTRAST, nContrast);
					//Cmds.PPPPCameraControl(getCurrentLiveVideoBean().mUUID, CONTRAST, nContrast);
//					Cmds.P2PCameraPicCfg(mServiceStub,getCurrentLiveVideoBean().mUUID,mIPCNetCamColorCfg_st.toJSONString());
					break;
				case ContentCommon.CAM_CFG_SATURATION://
					mIPCNetCamColorCfg_st.Saturtion = progress;
//					Cmds.P2PCameraPicCfg(mServiceStub,getCurrentLiveVideoBean().mUUID,mIPCNetCamColorCfg_st.toJSONString());
					break;
				case ContentCommon.CAM_CFG_ACUTANCE://
					mIPCNetCamColorCfg_st.Acutance = progress;
					break;
				default:
					break;
				}
				Cmds.P2PCameraPicCfg(mServiceStub,getCurrentLiveVideoBean().mUUID,mIPCNetCamColorCfg_st.toJSONString());
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {

			}

			@Override
			public void onProgressChanged(SeekBar arg0, int progress,
					boolean arg2) {

			}
		});
	}
	public int dip2px(Context context, float dipValue)
	{
	float m=context.getResources().getDisplayMetrics().density ;
	return (int)(dipValue * m + 0.5f) ;
	}
	 
	public int px2dip(Context context, float pxValue)
	{
	   float m=context.getResources().getDisplayMetrics().density ;
	   return (int)(pxValue / m + 0.5f) ;
	}
	private Handler P2PMsgHandler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle bd = msg.getData();
			int msgType = msg.what;
			String json = bd.getString("json");
			String uuid = bd.getString("uuid");
			JSONObject jsonData = null;
			try {
				jsonData = new JSONObject(json);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if(jsonData == null)
				return;
			
			Log.d(TAG, "msgType:" + msgType);
			
			switch(msgType){
			case ContentCommon.IPCNET_PRESET_GET_PTZ_RESP:
				for(int i=0;i<mUUIDList.size();i++){
					String id = mUUIDList.get(i);
					if(id.contentEquals(uuid)){
						mIPCPtzCmdList.get(i).parseJSON(jsonData);
						break;
					}
				}
				break;
			case ContentCommon.IPCNET_PRESET_SET_PTZ_RESP:
//				showToast(R.string.preset_add_success);
				break;
			case ContentCommon.IPCNET_GET_PREPOINT_RESP:
				if(mIPCNETPrePointList_st == null){
					mIPCNETPrePointList_st = mJSONStructProtocal.new IPCNETPrePointList_st();
				}
				if(mIPCNETPrePointList_st.parseJSON(jsonData)){
				}
				break;
			case ContentCommon.IPCNET_SET_PREPOINT_RESP:
				Cmds.getPresetConfig(mServiceStub, getCurrentLiveVideoBean().mUUID);//refresh preset list.
				break;
			case ContentCommon.IPCNET_OPERATE_PREPOINT_RESP:
				Log.d(TAG,"IPCNET_OPERATE_PREPOINT_RESP");
				break;
			case ContentCommon.IPCNET_GET_CAM_PIC_CFG_RESP:
				if(mIPCNetCamColorCfg_st == null){
					mIPCNetCamColorCfg_st = mJSONStructProtocal.new IPCNetCamColorCfg_st();
				}
				mIPCNetCamColorCfg_st.parseJSON(jsonData);
				if(mCurCameraPicConf>=0){
					mCurCameraPicConf = -1;
					mIPCNetCamColorCfg_st.SetDefault = false;
					setCameraPicConf(mCurCameraPicConf);
				}
				break;
			case ContentCommon.IPCNET_GET_OVERTURN_RESP:
				try {
					JSONObject jspicorient = jsonData.getJSONObject("IspOverTurn.info");
					int vich = jspicorient.getInt("ViCh");
//					mHorizontal = jspicorient.getBoolean("Mirror");
//					mVertical = jspicorient.getBoolean("Flip");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			case GSENSOR_REPORT_DATA_MSG:
//				Log.e(TAG, "GSENSOR_REPORT_DATA_MSG:" + GSENSOR_REPORT_DATA_MSG);
				try {
					JSONObject jsgsensor = jsonData.getJSONObject("gSensorData.info");
					JSONObject jacc = jsgsensor.getJSONObject("AccData");
					int acc_x = jacc.getInt("data_x");
					int acc_y = jacc.getInt("data_y");
					int acc_z = jacc.getInt("data_z");
					
					JSONObject jgry = jsgsensor.getJSONObject("GyroData");
					int gyr_x = jgry.getInt("data_x");
					int gyr_y = jgry.getInt("data_y");
					int gyr_z = jgry.getInt("data_z");
					
					Log.w(TAG, "AccData x:" + acc_x + " y:" + acc_y + " z:" + acc_z);
					Log.w(TAG, "GyroData x:" + gyr_x + " y:" + gyr_y + " z:" + gyr_z);
					
//					Log.w(TAG, "mRotateX:" + mRotateX + " mRotateY:" + mRotateY + "  mRotateZ:" + mRotateZ);
//					gestureDetect(uuid,gyr_x,gyr_y,gyr_z);
//					changeVideoOrientation(uuid,acc_x/4,acc_y/4,acc_z/4);
					LiveVideoBean lvb = getLiveVideoBean(uuid);
					if(lvb == null){
						Log.d(TAG, "getLiveVideoBean null[" + uuid + "]");
						return;
					}
					boolean invert_it = mAccGyroSensorUtils.getWorkMode();//mAccGyroSensorUtils.getInvertMode(uuid,gyr_x,gyr_y,gyr_z);
					
					int average_cnt = 2;
					if(mAccVal[0] ==0){
						mAccVal[0] = acc_x;
					}else{
						mAccVal[0] = (mAccVal[0]*(average_cnt-1) + acc_x)/average_cnt;
					}
					
					if(mAccVal[1] ==0){
						mAccVal[1] = acc_y;
					}else{
						mAccVal[1] = (mAccVal[1]*(average_cnt-1) + acc_y)/average_cnt;
					}
					
					if(mAccVal[2] ==0){
						mAccVal[2] = acc_z;
					}else{
						mAccVal[2] = (mAccVal[2]*(average_cnt-1) + acc_z)/average_cnt;
					}
					
					if(Math.abs(gyr_x)>10000 || Math.abs(gyr_y)>10000 || Math.abs(gyr_z)>10000)
						break;
					
					float angle = mAccGyroSensorUtils.getRotateAngle(uuid,(int)(mAccVal[0]/4),(int)(mAccVal[1]/4),(int)(mAccVal[2]/4));
					lvb.mVideoView.setVideoDegrees(invert_it,angle);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
	};
	
	private double mAccVal[] = new double[3];
	private int mAccY[] = new int[3],mAccZ[] = new int[3];
	
	private AccGyroSensorUtils mAccGyroSensorUtils = new AccGyroSensorUtils();

	public void showToast(String content) {
		Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
	}

	public void showToast(int rid) {
		Toast.makeText(this, getResources().getString(rid), 3)
				.show();
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {

		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// Toast.makeText(this, "onDoubleTapEvent",0).show();
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		return false;
	}

//	// 录像用的接口
//	public void setVideoRecord(CamViewCustomVideoRecord videoRecorder) {
//		this.videoRecorder = videoRecorder;
//	}
	@Override
	public void setVideoRecord(Object ob) {
		this.videoRecorder = (CamViewCustomVideoRecord) ob;
	}

	public CamViewCustomVideoRecord videoRecorder;

	public interface VideoRecorder {
		// abstract public void VideoRecordData(/*int type,*/Bitmap bmp, int
		// time);
		abstract public void VideoRecordData(int type, byte[] videodata,
				int width, int height, int time);
	}

	public class MultiVideoViewPagerChangeListener implements OnPageChangeListener {

		private int mCount = 4;
		public void setMaxCount(int cnt){
			mCount = cnt;
		}
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}
//		private int thePos = 0; //当前的View的索引
//		private int oldPosition;
		public void onPageSelected(int position) {
//			if(mCount>0)
//				Log.d(TAG,"position:" + position + " mCount:" + mCount + " oldPosition:" + oldPosition + " position % mCount:" + position % mCount);

			mCurrentVideoPage = position%mCount;
			if( (mVideosPerScreen*mCurrentVideoPage)>mCurrentVideoIndex || (mVideosPerScreen*(mCurrentVideoPage+1))<=mCurrentVideoIndex ){
				mCurrentVideoIndex = mVideosPerScreen*mCurrentVideoPage;
			}
			
//			Log.d(TAG,"onPageSelected position:" + position + " mCurrentVideoPage:"+mCurrentVideoIndex);
			
			startThread s = new startThread();
			new Thread(s).start();
		}
    	
    }
	
	public class MultiVideoViewPagerAdapter extends PagerAdapter {
    	
    	private List<View> list;
    	
    	public MultiVideoViewPagerAdapter(List<View> l) {
    		list = l;
		}
    	
    	public void setVideoViewList(List<View> l){
    		list = l;
    	}
    	
		@Override
		public void destroyItem(View view, int index, Object arg2) {
			// TODO Auto-generated method stub
//			((ViewPager)view).removeView(list.get(index%list.size()));
		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public int getCount() {
			if(list.size() <= 1)
				return list.size();
			return Integer.MAX_VALUE;
		}

		@Override
		public Object instantiateItem(View view, int index) {
			if(list.size() == 0)
				return null;
			if(list.size() == 1){
				((ViewPager)view).addView(list.get(0), 0);
				return list.get(0);
			}
			
			try {
				((ViewPager)view).addView(list.get(index%list.size()), 0);
			}catch(Exception e){  
	            //handler something  
	        }  
			return list.get(index%list.size());
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view == (object);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub
			
		}
    	
    }
	
	public class MyPagerChangeListener implements OnPageChangeListener {

		private int mCount = 4;
		public void setMaxCount(int cnt){
			mCount = cnt;
		}
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}
		private int thePos = 0; //当前的View的索引
		private int oldPosition;
		public void onPageSelected(int position) {
			Log.d(TAG,"position:" + position + " mCount:" + mCount + " oldPosition:" + oldPosition + " position % mCount:" + position % mCount);
			((View)listDots.get(oldPosition%mCount)).setBackgroundResource(R.drawable.dot_normal);
			((View)listDots.get(position%mCount)).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
			thePos = position;
			System.out.println(thePos);
		}
    	
    }
	
	public class MyPagerAdapter extends PagerAdapter {
    	
    	private List<View> list;
    	
    	public MyPagerAdapter(List<View> list) {
    		this.list = list;
		}
    	
		@Override
		public void destroyItem(View view, int index, Object arg2) {
			// TODO Auto-generated method stub
//			((ViewPager)view).removeView(list.get(index%list.size()));
		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
		//	return list.size();
			return Integer.MAX_VALUE;
		}

		@Override
		public Object instantiateItem(View view, int index) {
			try {
				((ViewPager)view).addView(list.get(index%list.size()), 0);
			}catch(Exception e){  
	            //handler something  
	        }  
			return list.get(index%list.size());
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view == (object);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub
			
		}
    	
    }
	private class MyStatusBroadCast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.d(TAG,"BroadcastReceiver ACTION:" + action);
			if (ContentCommon.CAMERA_INTENT_STATUS_CHANGE.contentEquals(action)) {
				String did = intent.getStringExtra(ContentCommon.STR_CAMERA_ID);
				int status = intent.getIntExtra(ContentCommon.STR_PPPP_STATUS,ContentCommon.P2P_STATUS_UNKNOWN);
				if(status == ContentCommon.P2P_STATUS_ON_LINE && getCurrentLiveVideoBean().mUUID.contentEquals(did)){
		    		getCurrentLiveVideoBean().setPPPPReconnectNotify();
				}
			}else if(ContentCommon.CAMERA_INTENT_REMOTE_DEV_CONTROL.contentEquals(action)){
				String control_app = intent.getStringExtra(ContentCommon.STR_CAMERA_CONTROL_APP_ACTION);
				if(control_app!=null){
					if(control_app.contentEquals(ContentCommon.STR_CAMERA_CONTROL_APP_ACTION_TAKE_VIDEO)){
						int val = intent.getIntExtra(ContentCommon.STR_CAMERA_APP_ACTION_VAL,-1);
						if(val == 0){
							goTakeVideo();
						}else if(val == 1){
							goTakeVideo();
						}
					}else if(control_app.contentEquals(ContentCommon.STR_CAMERA_CONTROL_APP_ACTION_TAKE_PIC)){
						bmp = CameraViewerActivity.this.getCurrentLiveVideoBean().captureVideoBitmap(mHWDecode);
						takePicture(bmp);
					}
				}
			}
		}
	}

}