package freesbell.demo.client;

import java.nio.ByteBuffer;

import org.json.JSONException;
import org.json.JSONObject;

import freesbell.demo.client.R;
import fenzhi.nativecaller.NativeCaller;
import freesbell.demo.client.BridgeService.ControllerBinder;
import freesbell.demo.content.ContentCommon;
import freesbell.demo.utils.Cmds;
import freesbell.demo.utils.ServiceStub;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * */
public class PlayBackActivity extends Activity implements OnTouchListener,
		OnGestureListener {
	private final String TAG = "PlayBackActivity";
	private ImageView playImg;
	private BridgeService mBridgeService;
	private TextView showtftime,playback_title,currenttime,sumtime;
	private String strDID;
	private String strFilePath;
	private String videotime;
	private ImageButton btnBack;
	private final int VIDEO = 0;
	private byte[] videodata = null;
	private int videoDataLen = 0;
	private RelativeLayout top,bottom;
	private GestureDetector gt = new GestureDetector(this);
	private int nVideoWidth = 0;
	private int nVideoHeight = 0;
	private boolean isPlaySeekBar = false;
	private LinearLayout layoutConnPrompt;
	private boolean isPortriat = true;
	private SeekBar playSeekBar;
	private int mSensorIndex = 0;
	//private ProgressBar playProgressBar;
	private ServiceConnection mConn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			ControllerBinder myBinder = (ControllerBinder) service;
			Log.d("tag", "playback serviceconnnect");
			
			mBridgeService = myBinder.getBridgeService();
			mBridgeService.setServiceStub(mServiceStub);
			mBridgeService.setPlayBackVideo(PlayBackActivity.this);
//			NativeCaller.StartPlayBack(strDID, strFilePath, 0);
			startPlayBack(strDID, strFilePath);
			Log.d("tag", "onServiceConnected()");
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
			msg.setData(bd);
			
			P2PMsgHandler.sendMessage(msg);
		}

		@Override
		public void onMessageRecieve(String uuid, int msg, byte[] str) {
			// TODO Auto-generated method stub
			
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getDataFromOther();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		//		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.playback);
		findView();
		setListener();
		Intent intent = new Intent(this, BridgeService.class);
		bindService(intent, mConn, BIND_AUTO_CREATE);
		Log.d(TAG, " playback onCreate d");
	}

	private void startPlayBack(String uuid,String file){
		NativeCaller.StartPlayBack(uuid, strFilePath, 0);
		Cmds.RemoteFileOp(mServiceStub,uuid,mSensorIndex,file,ContentCommon.AV_RECO_OP_PLAY_REQ);
	}
	private void stopPlayBack(String uuid){
		NativeCaller.StopPlayBack(uuid);
		Cmds.RemoteFileOp(mServiceStub,strDID,mSensorIndex,"null",ContentCommon.AV_RECO_OP_STOP_REQ);
	}
	
	private Handler P2PMsgHandler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle bd = msg.getData();
			int msgType = msg.what;
			String json = bd.getString("json");
			JSONObject jsonData = null;
			try {
				jsonData = new JSONObject(json);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if(jsonData == null)
				return;
			
			switch(msgType){
			case ContentCommon.AV_RECO_OP_PLAY_REQ:
				break;
			case ContentCommon.AV_RECO_OP_STOP_REQ:
				break;
			}
		}
	};
	
	Bitmap bitmap = null;
	Bitmap bmp = null;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			layoutConnPrompt.setVisibility(View.GONE);

			Bundle b = msg.getData();
			videodata = (byte[]) msg.obj;
			nVideoWidth = b.getInt("width");
			nVideoHeight = b.getInt("height");
			videoDataLen = b.getInt("len");
			int time = b.getInt("t");
			int h,m,s;
			h = time/3600;
			m = time/60%60;
			s = time%60;
			//DecimalFormat df = new DecimalFormat();
			//df.applyPattern("##");
			//String ct = df.format(h) + ":" + df.format(m) + ":" + df.format(s);
			String ct = h + ":" + m + ":" + s;
			currenttime.setText(ct);
			
			playSeekBar.setProgress(time);
			//playProgressBar.setProgress(time);
			
			switch (msg.what) {
			case 1: {// h264
				if (nVideoHeight == 0 || nVideoWidth == 0) {
					Log.e("info", "无效数据");
					return;
				}

//				if (bitmap != null && !bitmap.isRecycled()) {
//					Log.e("infp", "bitmap recycled");
//					bitmap.recycle();
//					bitmap = null;
//				}

//				if (bmp != null && !bmp.isRecycled()) {
//					Log.e("info", "bmp recycled");
//					bmp.recycle();
//					bmp = null;
//				}

				byte[] rgb = new byte[nVideoWidth * nVideoHeight * 2];
				NativeCaller.YUV4202RGB565(videodata, rgb, nVideoWidth,
						nVideoHeight);
				ByteBuffer buffer = ByteBuffer.wrap(rgb);

				rgb = null;
				
				bmp.copyPixelsFromBuffer(buffer);

				if (bmp == null)
					return;

				// Drawable drawable = new BitmapDrawable(bmp);

				// int width =
				// getWindowManager().getDefaultDisplay().getWidth();
				// int height =
				// getWindowManager().getDefaultDisplay().getHeight();
				// if (getResources().getConfiguration().orientation ==
				// Configuration.ORIENTATION_PORTRAIT) {
				// try {
				// bitmap = Bitmap.createScaledBitmap(bmp, width,
				// width * 3 / 4, true);
				// } catch (OutOfMemoryError e) {
				// // TODO: handle exception
				// System.out.println("=======out=======");
				// }
				//
				// } else if (getResources().getConfiguration().orientation ==
				// Configuration.ORIENTATION_LANDSCAPE) {
				// bitmap = Bitmap
				// .createScaledBitmap(bmp, width, height, true);
				// }
				// if (!bmp.isRecycled()) {
				// bmp.recycle();
				// bmp = null;
				// }
				// if (bitmap == null)
				// return;

				playImg.setImageBitmap(bmp);

			}
				break;
			case 2: {// jpeg
				bmp = BitmapFactory.decodeByteArray(videodata, 0, videoDataLen);
				Log.i("info", "bmp:" + bmp);
				if (bmp == null) {
					return;
				}
				// Drawable drawable = new BitmapDrawable(bmp);
				// Bitmap bitmap = null;
				int width = getWindowManager().getDefaultDisplay().getWidth();
				int height = getWindowManager().getDefaultDisplay().getHeight();
				// if (getResources().getConfiguration().orientation ==
				// Configuration.ORIENTATION_PORTRAIT) {
				// bitmap = Bitmap.createScaledBitmap(bmp, width,
				// width * 3 / 4, true);
				// } else if (getResources().getConfiguration().orientation ==
				// Configuration.ORIENTATION_LANDSCAPE) {
				// bitmap = Bitmap
				// .createScaledBitmap(bmp, width, height, true);
				// }
				playImg.setImageBitmap(bitmap);
			}
				break;

			default:
				break;
			}

		}
	};

	private void setListener() {
		playSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int progress = seekBar.getProgress();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {

			}
		});
		btnBack.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				NativeCaller.StopPlayBack(strDID);
//				finish();
				exit();
			}
		});

	}
	
	private  void exit(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//NativeCaller.StopPlayBack(strDID);
				stopPlayBack(strDID);
				mHandler.removeCallbacksAndMessages(null);
			}
		}).start();
		finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(isPortriat)
			return false;
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			if (isPlaySeekBar) {
				isPlaySeekBar = false;
				playSeekBar.setVisibility(View.GONE);
			} else {
				isPlaySeekBar = true;
				playSeekBar.setVisibility(View.VISIBLE);
			}
			break;
		case MotionEvent.ACTION_DOWN:
			if (isShow) {
				isShow = false;
				top.setVisibility(View.GONE);
				bottom.setVisibility(View.GONE);
			} else {
				isShow = true;
				top.setVisibility(View.VISIBLE);
				bottom.setVisibility(View.VISIBLE);
			}
			break;

		default:
			break;
		}

		return false;
	}

	private int mDuration;
	private void getDataFromOther() {
		Intent intent = getIntent();
		strDID = intent.getStringExtra("did");
		strFilePath = intent.getStringExtra("filepath");
		videotime = intent.getStringExtra("videotime");
		mDuration = intent.getIntExtra("duration",0);
		Log.i("info", "time:" + videotime);
		Log.i("info", "strFilePath:" + strFilePath);
	}

	private String getTime(String time) {
//		String mess = time.substring(0, 4);
//		String mes = time.substring(4, 6);
//		String me = time.substring(6, 8);
//		String hou = time.substring(8, 10);
//		String min = time.substring(10, 12);
//		String miao = time.substring(12, 14);
//		return mess + "-" + mes + "-" + me + " " + hou + ":" + min + ":" + miao;
		return time;
	}

	private void findView() {
		playImg = (ImageView) findViewById(R.id.playback_img);
		layoutConnPrompt = (LinearLayout) findViewById(R.id.layout_connect_prompt);// connection
		playSeekBar = (SeekBar) findViewById(R.id.playback_seekbar);
		//playProgressBar = (ProgressBar)findViewById(R.id.progressBar2);
		currenttime = (TextView) findViewById(R.id.currenttime);
		sumtime = (TextView) findViewById(R.id.sumtime);
		showtftime = (TextView) findViewById(R.id.showvideotimetf);
		showtftime.setText(getTime(videotime));
		btnBack = (ImageButton) findViewById(R.id.back);
		playback_title = (TextView) findViewById(R.id.playback_title);
		if(strFilePath!=null){
			playback_title.setText(strFilePath.substring(strFilePath.lastIndexOf("/")+1));
		}

		
		int h,m,s;
		h = mDuration/3600;
		m = mDuration/60%60;
		s = mDuration%60;
		String duration = h + ":" + m + ":" + s;
		sumtime.setText(duration);
		playSeekBar.setMax(mDuration);
		//playProgressBar.setMax(mDuration);
		
		top = (RelativeLayout) findViewById(R.id.top);
		bottom = (RelativeLayout) findViewById(R.id.bottom);
//		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
//				R.drawable.top_bg);
//		BitmapDrawable drawable = new BitmapDrawable(bitmap);
//		drawable.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
//		drawable.setDither(true);
//		top.setBackgroundDrawable(drawable);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		isPortriat = !isPortriat;
		if(isPortriat){
			if(top.getVisibility()!=View.VISIBLE)
				top.setVisibility(View.VISIBLE);
			if(bottom.getVisibility()!=View.VISIBLE)
				bottom.setVisibility(View.VISIBLE);
//			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
//					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		}else{
//			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
//		Log.d(TAG,"onConfigurationChanged--------------------");
	}

	public boolean isCreate = false;

	/**
	 * BridgeService callback video
	 * **/
	public void CallBack_PlaybackVideoData(byte[] videobuf, int h264Data,
			int len, int width, int height,int time) {
//		Log.d(TAG, "playback  len:" + len + " width:" + width + " height:"
//				+ height + " h264Data:"+h264Data);
		// videodata = videobuf;
		// videoDataLen = len;
		// nVideoWidth = width;
		// nVideoHeight = height;
		// if (h264Data == 1) { // H264
		// mHandler.sendEmptyMessage(1);
		// } else { // MJPEG
		// mHandler.sendEmptyMessage(2);
		// }
		if( !isCreate){
			isCreate=true;
			bmp = Bitmap.createBitmap(width, height,
					Bitmap.Config.RGB_565);
		}
		Message msg = new Message();
		Bundle b = new Bundle();
		b.putInt("height", height);
		b.putInt("width", width);
		b.putInt("len", len);
		b.putInt("t", time);
		msg.obj = videobuf;
		msg.setData(b);

		if (h264Data == 1) { // H264
			msg.what = 1;
		} else { // MJPEG
			msg.what = 2;
		}
		mHandler.sendMessage(msg);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		NativeCaller.StopPlayBack(strDID);
		mBridgeService.unbindSetNull(mServiceStub);
		unbindService(mConn);
		mBridgeService.unbindSetNull("PlayBackActivity");
		Log.d(TAG, "PlayBackActivity  onDestroy()");
	}


	private boolean isShow = true;

	@Override
	public boolean onDown(MotionEvent e) {// ���ؼ�
		// TODO Auto-generated method stub
		Log.e("info", "onDown-------------");
		if (isShow) {
			isShow = false;
			top.setVisibility(View.GONE);
			bottom.setVisibility(View.GONE);
		} else {
			isShow = true;
			top.setVisibility(View.VISIBLE);
			bottom.setVisibility(View.VISIBLE);
		}
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
