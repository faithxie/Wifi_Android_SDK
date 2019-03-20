package freesbell.demo.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import freesbell.demo.client.R;
import fenzhi.nativecaller.NativeCaller;
import freesbell.demo.content.ContentCommon;
import freesbell.demo.utils.DatabaseUtil;
import freesbell.demo.utils.MySharedPreferenceUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.CaptureActivityHandler;
import com.google.zxing.client.android.DecodeFormatManager;
import com.google.zxing.client.android.FinishListener;
import com.google.zxing.client.android.InactivityTimer;
import com.google.zxing.client.android.ViewfinderView;
import com.google.zxing.client.android.camera.CameraManager;
import com.google.zxing.common.HybridBinarizer;

public final class CaptureActivityPortrait extends Activity implements
		SurfaceHolder.Callback, OnClickListener{

	private static final float BEEP_VOLUME = 0.10f;
	private static final long VIBRATE_DURATION = 200L;
	private String tag = CaptureActivityPortrait.this.getClass()
			.getSimpleName();

	private static final Set<ResultMetadataType> DISPLAYABLE_METADATA_TYPES;

	static {
		DISPLAYABLE_METADATA_TYPES = new HashSet<ResultMetadataType>(5);
		DISPLAYABLE_METADATA_TYPES.add(ResultMetadataType.ISSUE_NUMBER);
		DISPLAYABLE_METADATA_TYPES.add(ResultMetadataType.SUGGESTED_PRICE);
		DISPLAYABLE_METADATA_TYPES
				.add(ResultMetadataType.ERROR_CORRECTION_LEVEL);
		DISPLAYABLE_METADATA_TYPES.add(ResultMetadataType.POSSIBLE_COUNTRY);
	}

	private enum Source {
		NATIVE_APP_INTENT, PRODUCT_SEARCH_LINK, ZXING_LINK, NONE
	}

	private CaptureActivityHandler handler;
	
	private View textEntryView;
	private ViewfinderView viewfinderView;
	private MediaPlayer mediaPlayer;
	private Result lastResult;
	private boolean hasSurface;
	private boolean playBeep = true;
	private Source source;
	private String returnUrlTemplate;
	private String openId;
	private int isThirdLogin = -1; 
	private String user;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private Button mButtonChangeLaser;
	private ViewfinderView mVviewfinder_view;

	private ImageButton scan_top_back;
	private LinearLayout scan_back;
	
	
	//wifi搜索
	private List<ScanResult> listScanResult;
	private ScanResult nowScanResult;//保存当前连接的wifi
	private ScanResult needConnectScanResult;//需要连接的wifiScan
	private boolean isFristScan;//用于控制是否是第一次得到信号源
	private Pattern p = Pattern.compile("VSTC[0-9]{6}[A-Z]{5}");// 匹配的正则

	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};
	
	private List<Integer> listIndex;//匹配到的position
	private Dialog dialog;
	private DatabaseUtil dbUtil;
	private List<HashMap<String, Object>> searchitems = new ArrayList<HashMap<String,Object>>();
	private Handler stopSearchHandler = new Handler();
	private static final int SEARCH_TIME = 3000;
	private static final int PHOTO_PICKED_WITH_DATA = 3021;/*用来标识请求相册选择的activity*/  
	private WifiInfo nowWifiInfo;//当前连接的wifi
	MultiFormatReader multiFormatReader = new MultiFormatReader();

	
	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}
	
	@Override
	public void onCreate(Bundle icicle) { 
		super.onCreate(icicle);
		Log.i("CaptureActivityPortrait", "onCreate");
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.capture);
		//初始化数据
		handler = null;
		lastResult = null;
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		listScanResult = new ArrayList<ScanResult>();
		
		dbUtil = new DatabaseUtil(this);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		scan_top_back = (ImageButton)findViewById(R.id.scan_top_back);
		scan_top_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
			
		});
		mButtonChangeLaser = (Button) findViewById(R.id.buttonLaser);
		mButtonChangeLaser.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mVviewfinder_view.changeLaser();
			}
		});
		mVviewfinder_view = (ViewfinderView) findViewById(R.id.viewfinder_view);

	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		isFristScan = true; 
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onResume() {
		// 初始化扫描界面
		super.onResume();
		resetStatusView();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}

		source = Source.NONE;
		decodeFormats = null;
		characterSet = null;
		initBeepSound();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}
	
	

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (source == Source.NATIVE_APP_INTENT) {
				// setResult(RESULT_CANCELED);

				return true;
			} else if ((source == Source.NONE || source == Source.ZXING_LINK)
					&& lastResult != null) {
				resetStatusView();
				if (handler != null) {
					handler.sendEmptyMessage(R.id.restart_preview);
				}
				return true;
			}
//			Intent it = new Intent(this, AddNewCameraActivity.class);
//			startActivity(it);
			overridePendingTransition(R.anim.out_to_right, R.anim.in_from_left);
			finish();

		} else if (keyCode == KeyEvent.KEYCODE_FOCUS
				|| keyCode == KeyEvent.KEYCODE_CAMERA) {
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}
	
	
	/**
	 * 成功获取二维码，在handler中调用这个方法，回传bitmap
	 * 
	 * @param rawResult 
	 * @param barcode
	 */
	public void handleDecode(Result rawResult, Bitmap barcode) {
		String content = rawResult.getText();
		Log.i("info", "handleDecode:" + content);
		inactivityTimer.onActivity();
		lastResult = rawResult;
		if (barcode == null) {
			Log.i("info", "barcode == null");
			handleDecodeInternally(rawResult, null);
		} else {
			Log.i("info", "barcode != null");
			playBeepSoundAndVibrate();// 播放声音和振动代表成功获取二维码
			// drawResultPoints(barcode, rawResult);
			
			switch (source) {
			case NATIVE_APP_INTENT:
			case PRODUCT_SEARCH_LINK:
				handleDecodeExternally(rawResult, barcode);
				break;
			case ZXING_LINK:
				if (returnUrlTemplate == null) {
					handleDecodeInternally(rawResult, barcode);
				} else {
					handleDecodeExternally(rawResult, barcode);
				}
				break;
			case NONE: {
				handleDecodeInternally(rawResult, barcode);
			}
				break;
			}
		}
	}

	/**
	 * 把图片截图下来之后,标记二维码所在的点 Superimpose a line for 1D or dots for 2D to highlight
	 * the key features of the barcode.
	 * 
	 * @param barcode
	 *            A bitmap of the captured image.
	 * @param rawResult
	 *            The decoded results which contains the points to draw.
	 */
	private void drawResultPoints(Bitmap barcode, Result rawResult) {
		ResultPoint[] points = rawResult.getResultPoints();
		if (points != null && points.length > 0) {
			Canvas canvas = new Canvas(barcode);
			Paint paint = new Paint();
			paint.setColor(getResources().getColor(R.color.result_image_border));
			paint.setStrokeWidth(3.0f);
			paint.setStyle(Paint.Style.STROKE);
			Rect border = new Rect(2, 2, barcode.getWidth() - 2,
					barcode.getHeight() - 2);
			canvas.drawRect(border, paint);
			paint.setColor(getResources().getColor(R.color.result_points));
			if (points.length == 2) {
				paint.setStrokeWidth(4.0f);
				drawLine(canvas, paint, points[0], points[1]);
			} else if (points.length == 4
					&& (rawResult.getBarcodeFormat()
							.equals(BarcodeFormat.UPC_A))
					|| (rawResult.getBarcodeFormat()
							.equals(BarcodeFormat.EAN_13))) {
				drawLine(canvas, paint, points[0], points[1]);
				drawLine(canvas, paint, points[2], points[3]);
			} else {
				paint.setStrokeWidth(10.0f);
				for (ResultPoint point : points) {
					canvas.drawPoint(point.getX(), point.getY(), paint);
				}
			}
		}
	}

	private static void drawLine(Canvas canvas, Paint paint, ResultPoint a,
			ResultPoint b) {
		canvas.drawLine(a.getX(), a.getY(), b.getX(), b.getY(), paint);
	}

	/**
	 * 二维码处理成功
	 * 
	 * @param rawResult
	 * @param barcode
	 */
	private void handleDecodeInternally(Result rawResult, Bitmap barcode) {
		
		viewfinderView.setVisibility(View.GONE);
		String content = rawResult.getText();
		Log.i("info", "content:" + content);
		// ================
		Intent b=new Intent();
		b.putExtra("did", content);
		setResult(2014, b);
		finish();
	}

	private void handleDecodeExternally(Result rawResult, Bitmap barcode) {
		viewfinderView.drawResultBitmap(barcode);
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);
			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		vibrator.vibrate(VIBRATE_DURATION);
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			displayFrameworkBugMessageAndExit();
			return;
		} catch (RuntimeException e) {
			displayFrameworkBugMessageAndExit();
			return;
		}
		
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats, 
					characterSet,listScanResult,CaptureActivityPortrait.this);
		}
	} 

	private void displayFrameworkBugMessageAndExit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.app_name));
		builder.setMessage("Camera malfunction");
		builder.setPositiveButton("OK", new FinishListener(this));
		builder.setOnCancelListener(new FinishListener(this));
		builder.show();
	}

	private void resetStatusView() {
		viewfinderView.setVisibility(View.VISIBLE);
		lastResult = null;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
//		case R.id.scan_top_flash_layout:
//			openEye();
//			break;
//		case R.id.scan_top_picture_layout:
//			doPickPhotoFromGallery();// 从相册中去获取  
//			break;
		case R.id.scan_top_back_layout:
			overridePendingTransition(R.anim.out_to_right, R.anim.in_from_left);
			finish();
			break;
//		case R.id.open_net:
//			BridgeService.setsearchResultInterface(null);
//			Runnable run = new Runnable() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					NativeCaller.StopSearch();
//				}
//			};
//			new Thread(run).start();
//			Intent it = new Intent(this, AddCameraByLocalNet.class);
//			startActivity(it);
//			finish();
//			break;
//		case R.id.open_hand:
//			Intent i = new Intent(this, AddCameraByHand.class);
//			startActivity(i);
//			finish();
//			break;
		
//		case R.id.open_flash:
//			openEye();
//			break;
//		case R.id.open_write:
//			Intent i = new Intent(this, AddCameraByHand.class);
//			startActivity(i);
//
//			finish();
//			break;
//		case R.id.open_back:
//			Intent it = new Intent(this, AddNewCameraActivity.class);
//			startActivity(it);
//			overridePendingTransition(R.anim.out_to_right, R.anim.in_from_left);
//			finish();
//			break;
		default:
			break;
		}
	}

	private boolean isOpen = false;
	private Camera m_Camera;

	private void openEye() {
		if (!isOpen) {
			isOpen = true;
			CameraManager.get().openLight();
		} else {
			isOpen = false;
			CameraManager.get().closeDrivers();
		}
	}
	
	//检测wifi的状态信息
//	class NetworkConnectChangedReceiver extends BroadcastReceiver {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			if (WifiManager.WIFI_STATE_CHANGED_ACTION
//					.equals(intent.getAction())) {
//				int wifiState = intent.getIntExtra(
//						WifiManager.EXTRA_WIFI_STATE, 0);
//				switch (wifiState) {
//				case WifiManager.WIFI_STATE_DISABLED:
//					Log.i("info", "wifi关闭状态");
//					break;
//				case WifiManager.WIFI_STATE_DISABLING:
//					System.out.println("wifi关闭中状态 ");
//					break;
//				case WifiManager.WIFI_STATE_ENABLED:
//						System.out.println("wifi打开可用");
//						if (null != wifiManagerUtil ) {
//							wifiManagerUtil.startScan();//wifi打开可用开始搜索wifi列表
//						}
//					break;
//				case WifiManager.WIFI_STATE_ENABLING:
//					System.out.println("wifi打开中");
//					break;
//				case WifiManager.WIFI_STATE_UNKNOWN:
//					System.out.println("wifi未知状态");
//					break;
//				}
//			}
//			if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent
//					.getAction())) {
//				//wifi获得新信号源的监听
//				if (isWifiConnect()) {//确保wifi连接
//					nowWifiInfo = wifiManagerUtil.getWifiInfo();//保存当前的wifi
//					if (isFristScan && null != nowWifiInfo) {
//						Log.i("wifiinfo", "获得信号源");
//						isFristScan = false;//是否是第一次获得信号源
//						getWifiDevice();//得到wifi列表
//						new Thread(new MateVSTCRunable()).start();
//					}
//				}
//				
//			}
//			
//		} 
//	}
	public class MateVSTCRunable implements Runnable {
		public void run() {
			Log.i("wifiinfo", "进入正则匹配");
			for (int i = 0; i < 3; i++) {
				List<Integer> list = isMateSuccess(listScanResult);
				if (list.size() != 0) {
					Log.i("wifiinfo", "匹配到多少条数据："+list.size());
					if (null !=handler) {
						Message mes = handler.obtainMessage();
						mes.what = 1;
						mes.obj =list;
						mes.sendToTarget();
					}
					return;
				}
				try {
					Thread.sleep(1000);
					Log.i("wifiinfo", "匹配失败，重新匹配");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if ( 2 == i) {
					if (null !=handler) {
						Message mes = handler.obtainMessage();
						mes.what = 1;
						mes.obj = new ArrayList<Integer>();
						mes.sendToTarget();
					}
					
				}
			}
		}
	}
	
	public List<Integer> isMateSuccess(List<ScanResult> list) {
		listIndex = new ArrayList<Integer>();
		for (int i = 0; i < list.size(); i++) {//这一步是为了能够得到当前wifi的ScanResult,因为里面有加密累心信息,而wifiinfo里面木有
			String nowwifi = nowWifiInfo.getSSID();
			if (null != nowwifi) {
				String ssid = list.get(i).SSID;
				Log.i(tag, "ssid:"+ssid+"nowwifi:"+nowwifi);
//				nowwifi = nowwifi.substring(1,nowwifi.length()-1);
				if (nowwifi.equals(ssid)) {	
					nowScanResult = list.get(i);
				}else{
					nowwifi = nowwifi.substring(1,nowwifi.length()-1);
					if (nowwifi.equals(ssid)) {
						nowScanResult = list.get(i);
					}
				}
			}
			Matcher m = p.matcher(list.get(i).SSID); 
			if (m.matches()) {
				listIndex.add(i);
			}
		}
		return listIndex;
	};
	
	 public boolean isWifiConnect() {
	       ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
	       NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	       return mWifi.isConnected();
	  }
	
//	public void onCreateDialog(List<ScanResult> list) {
//		LogTools.LogWe("onCreateDialog------------------------------");
//		dialog = new MyDialog(CaptureActivityPortrait.this,R.style.MyDialog,list);
//		if (0 < list.size()) {
//			dialog.show();
//		}else{
//			if (0 < searchitems.size()) { 
//				dialog.show();
//			}
//		}
//    }
	
//	public class MyDialog extends Dialog{
//		private TextView tv_tip_1;
//		private ListView lv_wifilist_1;
//		private RelativeLayout capture_wifilist_rl;
//		private LinearLayout ll_capture_wifilist_cancle;
//		private String password;
//		private List<ScanResult> list;
//		private List<HashMap<String, Object>> listItems = new ArrayList<HashMap<String,Object>>();
//		private Context ctxt;
//		public MyDialog(Context context,int theme,List<ScanResult> list) {
//			super(context,theme); 
//			// TODO Auto-generated constructor stub
//			this.list = list; 
//			this.ctxt = context;
//		} 
//		@Override
//		public void onCreate(Bundle savedInstanceState) {
//			// TODO Auto-generated method stub
//			super.onCreate(savedInstanceState);
//			setContentView(R.layout.captrue_wifilist);
//			tv_tip_1 = (TextView) findViewById(R.id.tv_capture_wifidialog_tip_1);
//			lv_wifilist_1 = (ListView) findViewById(R.id.lv_capture_wifidialog_list_1);
//			capture_wifilist_rl = (RelativeLayout) findViewById(R.id.rl_capture_wifilist);
//			ll_capture_wifilist_cancle = (LinearLayout) findViewById(R.id.ll_capture_wifilist_cancle);
//			ll_capture_wifilist_cancle.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					if (null != dialog) {
//						dialog.dismiss();
//					}
//				}
//			});
//			 
////			searchListAdapter = new SearchListAdapter(ctxt);
////			lv_wifilist_2.setAdapter(searchListAdapter);
//			
//			listItems = new ArrayList<HashMap<String,Object>>();
//			
//			HashMap<String, Object> map ;
//			for (int i = 0; i < list.size(); i++) {
//				map = new HashMap<String, Object>();
//				map.put(ContentCommon.STR_CAMERA_ID, list.get(i).SSID);
//				map.put(ContentCommon.STR_CAMERA_NEW, true);
////				aPModeSearchListAdapter.AddCamera(list.get(i).SSID);
//				listItems.add(map);
//			}
//			listItems.addAll(searchitems);
//			aPModeSearchListAdapter = new APModeSearchListAdapter(ctxt,listItems);
//			
//			lv_wifilist_1.setAdapter(aPModeSearchListAdapter);
//			
//			lv_wifilist_1.setOnItemClickListener(new OnItemClickListener() {
//					@Override
//					public void onItemClick(AdapterView<?> parent, View view,
//							int position, long id) {
//						// TODO Auto-generated method stub
////						Message msg = updateListHandler.obtainMessage(); 
////						msg.what = 3; 
////						msg.obj = list.get(position);
////						msg.sendToTarget();
////						if (null != dialog) {
////							dialog.dismiss();
////						}
//						
//						Map map = listItems.get(position);
//						boolean isNew = (Boolean) map.get(ContentCommon.STR_CAMERA_NEW);
//						if (isNew) {
//							Intent intent = new Intent(CaptureActivityPortrait.this,APModeSettingActivity.class);
//							dbUtil.open();
//							Cursor cur = dbUtil.findSomeoneCameras(list.get(position).SSID);
//							if (cur.getCount()>0) {
//								intent.putExtra("ishavein", true);
//							}else{
//								intent.putExtra("ishavein", false);
//							}
//							while(cur.moveToNext()){
//								password = cur.getString(cur.getColumnIndex(DatabaseUtil.KEY_PWD));
//							}
//							dbUtil.close();
//							
//							if (isThirdLogin == 1) {
//								intent.putExtra("openid", openId);
//							}else{
//								intent.putExtra("accname", user);
//							}
//							intent.putExtra("needconnect", list.get(position));
//							intent.putExtra("nowconnect", nowScanResult);
//							intent.putExtra("isThirdLogin", isThirdLogin);
//							if (null != password) {
//								intent.putExtra("havapw", password);
//							}
//							startActivity(intent);
//						}else{
//							String strName = (String) map.get(ContentCommon.STR_CAMERA_NAME);
//							String strDID = (String) map.get(ContentCommon.STR_CAMERA_ID);
//							
//							Intent it = new Intent(CaptureActivityPortrait.this, AddCameraInfoActivity.class);
//							it.putExtra("did", strDID);
//							it.putExtra("name", strName);
//							startActivity(it);
//							finish();
//						}
//						if (null != dialog) {
//							dialog.dismiss();
//						}
//						finish();
//					}
//				  }); 
//		}
//	}
//	
//	@Override
//	public void CallBackSearchResult(String strMac, String strName,
//			String strDeviceID) {
//		if(CheckCameraInfo(strMac)){
//			dbUtil.open();
//			Cursor cur = dbUtil.findSomeoneCameras(strDeviceID);
//			if (cur.getCount() <= 0) {
//				System.out.println("strname:" + strName + ",did:" + strDeviceID);
//				Log.i("wifiinfo", "进入了回调CallResult");
//				HashMap<String, Object> map = new HashMap<String, Object>();
//				map.put(ContentCommon.STR_CAMERA_MAC, strMac);
//				map.put(ContentCommon.STR_CAMERA_ID, strDeviceID);
//				map.put(ContentCommon.STR_CAMERA_NAME, strName);
//				map.put(ContentCommon.STR_CAMERA_NEW, false);
//				searchitems.add(map);
//			}
//			dbUtil.close();
//		}
//	}
	
	private boolean CheckCameraInfo(String mac) {
		// TODO Auto-generated method stub
		int size = searchitems.size();
		int i;
		for(i = 0; i < size; i++){
			String strMac = (String)searchitems.get(i).get(ContentCommon.STR_CAMERA_MAC);
			if(mac.equals(strMac)){
				return false;
			}
		}
		return true;
	}
	
	public void showToast(int rid) {
		Toast.makeText(this, getResources().getString(rid), Toast.LENGTH_LONG).show();
	}
	
	Runnable run = new Runnable() {
		 
		@Override
		public void run() {
			// TODO Auto-generated method stub
			NativeCaller.StopSearch();
//			updateListHandler.sendEmptyMessage(1);
		}

	};
  
	// 请求Gallery程序  
	protected void doPickPhotoFromGallery() {  
		try {  
			multiFormatReader = new MultiFormatReader();

			// 解码的参数
			Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
			// 可以解析的编码类型
			Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
			if (decodeFormats == null || decodeFormats.isEmpty()) {
				decodeFormats = new Vector<BarcodeFormat>();

				// 这里设置可扫描的类型，我这里选择了都支持
				decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
				decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
				decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
			}
			hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats); 

			// 设置继续的字符编码格式为UTF8
			hints.put(DecodeHintType.CHARACTER_SET, "UTF8");  
			
			// 设置解析配置参数
			multiFormatReader.setHints(hints);
			// Launch picker to choose photo for selected contact
			final Intent intent = getPhotoPickIntent();
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}  
	}   
  
	// 封装请求Gallery的intent  
	public static Intent getPhotoPickIntent() {  
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.setType("image/*");  
		intent.putExtra("crop", "true");  
		intent.putExtra("aspectX", 1);   
		intent.putExtra("aspectY", 1.5);  
		intent.putExtra("outputX", 100);
		intent.putExtra("outputY", 100);
		intent.putExtra("return-data", true);  
//		Intent wrapperIntent = Intent.createChooser(intent, "选择二维码图片"); 
		return intent;
	}  
  
	// 因为调用了Camera和Gally所以要判断他们各自的返回情况,他们启动时是这样的startActivityForResult  
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
//		if (resultCode != RESULT_OK)  
//			return;  
//		switch (requestCode) {
//			case PHOTO_PICKED_WITH_DATA: {// 调用Gallery返回的  
//				Bitmap photo = data.getParcelableExtra("data");  
//				// 下面就是显示照片了  
//				System.out.println(photo);
//				//缓存用户选择的图片  
////      	    img = getBitmapByte(photo);
////        	    mEditor.setPhotoBitmap(photo);
//				Result rawResult = null;
//					   try {
//						 BitmapLuminanceSource bls =   new BitmapLuminanceSource(photo);
//						 HybridBinarizer hb = new HybridBinarizer(bls);
//						 BinaryBitmap bb = new BinaryBitmap(hb);
//						 rawResult =  multiFormatReader.decodeWithState(bb);
////						rawResult = multiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(new BitmapLuminanceSource(photo))));
//						handleDecodeInternally(rawResult, photo);
//					   } catch (com.google.zxing.NotFoundException e) {
//						e.printStackTrace();
////						Toast.makeText(CaptureActivityPortrait.this, R.string.apmode_fail, Toast.LENGTH_SHORT).show();
//					   }
//				break; 
//			}  
//			}  
//    }

	
	
}
