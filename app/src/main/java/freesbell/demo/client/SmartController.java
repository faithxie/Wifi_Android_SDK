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

public class SmartController extends Activity implements OnClickListener,VideoAudioInterface {

	private static final String TAG = "SmartController";

	private String mUUID = "";

	public static Drawable d;

	private JSONStructProtocal mJSONStructProtocal = new JSONStructProtocal();
	private ArrayList<IPCPtzCmd_st> mIPCPtzCmdList;

	private PopupWindow imagePopupWindow = null;
	public Bitmap bmp;

	private DatabaseUtil dbUtil;

	public BridgeService mBridgeService;
	private ImageButton btnBack;

	private MyStatusBroadCast broadcast;

	private LiveStreamVideoView mVideoView;
	private CamViewCustomVideoRecord myvideoRecorder;
	public boolean isH264 = true;
	
	private IPCNETPrePointList_st mIPCNETPrePointList_st;
	private IPCNetCamColorCfg_st mIPCNetCamColorCfg_st;
	
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
			mBridgeService.setLivestreamReciver(SmartController.this);
			
//			Log.d(TAG,"mStartedUUID:" + mStartedUUID.size());
//			for(int i = 0; i < mStartedUUID.size() ; i++){
//				String uuid = mStartedUUID.get(i);
//				Log.d(TAG,"start uuid:" + mStartedUUID.get(i));
//				getLiveVideoBean(uuid).startLiveStream();
//				
//				Cmds.getDevPtz(mServiceStub,uuid);
//			}
//			mPlayStarted = true;
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

//		mHWDecode = BridgeService.mSelf.getDecodeMode();
//		int version = android.os.Build.VERSION.SDK_INT;
//		if (version < Build.VERSION_CODES.JELLY_BEAN){//allways software decode.
//			mHWDecode = false;
//		}		
		
//		gt.setIsLongpressEnabled(true);

		// initSame();
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();

		broadcast = new MyStatusBroadCast();
		IntentFilter filter = new IntentFilter(ContentCommon.CAMERA_INTENT_STATUS_CHANGE);
		registerReceiver(broadcast, filter);
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
			}
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
		Cursor cursor = dbUtil.queryVideoOrPictureByDate(mUUID, strDate,
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
			File file = new File(div, strDate + "_" + seri + mUUID + ".jpg");
			fos = new FileOutputStream(file);
			if (bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)) {
				fos.flush();
				Log.d(TAG, "takepicture success");
				mBridgeService.createVideoOrPic(mUUID,DatabaseUtil.TYPE_PICTURE, file.getAbsolutePath(),
						 strDate);
				
				//notify the ui update picture information.
				Intent intent = new Intent(ContentCommon.CAMERA_INTENT_MEDIA_CHANGE);
				intent.putExtra(ContentCommon.STR_CAMERA_ID, mUUID);
				sendBroadcast(intent);
				
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(
								SmartController.this,
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
							SmartController.this,
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
	private void getDataFromOther() {		
		Intent intent = getIntent();
		if(intent != null){
			mUUID = intent.getStringExtra("uuid");
		}else{
			finish();
		}
	}
	/**
	 * BridgeService Feedback execute
	 * 
	 * **/
	public void setVideoData(String did, byte[] videobuf, int h264Data,
			int len, int width, int height) {
//		
//		if(mChangeOrientation || !mPlaying)
//			return;
//		if(mVideosPerScreen>1){
//			LiveVideoBean lvb = getLiveVideoBean(did);
//			if(lvb == null){
//				Log.d(TAG, "getLiveVideoBean null[" + did + "]");
//				return;
//			}
//			lvb.setVideoData(videobuf, h264Data, len, width, height);
//		}else{
//			LiveVideoBean lvb = getCurrentLiveVideoBean();
//			
//			if(lvb == null){
//				Log.d(TAG, "getLiveVideoBean null[" + did + "]");
//				return;
//			}
//			if(!lvb.mUUID.contentEquals(did))
//				return;
//			lvb.setVideoData(videobuf, h264Data, len, width, height);
//		}
	}

	/**
	 * BridgeService Feedback execute
	 * **/
	public void setAudioData(byte[] pcm, int len) {
		Log.d("new", "PlayActivity setAudioData AudioData: len :+ " + len);
//		if (!audioPlayer.isAudioPlaying()) {
//			return;
//		}
//		CustomBufferHead head = new CustomBufferHead();
//		CustomBufferData data = new CustomBufferData();
//		head.length = len;
//		head.startcode = AUDIO_BUFFER_START_CODE;
//		data.head = head;
//		data.data = pcm;
//		AudioBuffer.addData(data);
	}

	/**
	 * BridgeService Feedback execute
	 * **/
	public void callBackH264Data(String uuid ,byte[] h264, int type, int size,int width, int height) {
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		}
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
			
			switch(msgType){
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
//			case ContentCommon.IPCNET_SET_PREPOINT_RESP:
//				Cmds.getPresetConfig(mServiceStub, getCurrentLiveVideoBean().mUUID);//refresh preset list.
//				break;
			case ContentCommon.IPCNET_OPERATE_PREPOINT_RESP:
				Log.d(TAG,"IPCNET_OPERATE_PREPOINT_RESP");
				break;
			}
		}
	};
	
	public void showToast(String content) {
		Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
	}

	public void showToast(int rid) {
		Toast.makeText(this, getResources().getString(rid), 3)
				.show();
	}

	public interface VideoRecorder {
		// abstract public void VideoRecordData(/*int type,*/Bitmap bmp, int
		// time);
		abstract public void VideoRecordData(int type, byte[] videodata,
				int width, int height, int time);
	}
	private class MyStatusBroadCast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.d(TAG,"BroadcastReceiver ACTION:" + action);
//			if (ContentCommon.CAMERA_INTENT_STATUS_CHANGE.contentEquals(action)) {
//				String did = intent.getStringExtra(ContentCommon.STR_CAMERA_ID);
//				int status = intent.getIntExtra(ContentCommon.STR_PPPP_STATUS,ContentCommon.P2P_STATUS_UNKNOWN);
//				if(status == ContentCommon.P2P_STATUS_ON_LINE && getCurrentLiveVideoBean().mUUID.contentEquals(did)){
//		    		getCurrentLiveVideoBean().setPPPPReconnectNotify();
//				}
//			}
		}
	}
}