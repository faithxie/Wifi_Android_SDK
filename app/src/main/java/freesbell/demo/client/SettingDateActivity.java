package freesbell.demo.client;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import freesbell.demo.client.R;
import fenzhi.nativecaller.NativeCaller;
import freesbell.demo.bean.DateBean;
import freesbell.demo.bean.JSONStructProtocal;
import freesbell.demo.bean.JSONStructProtocal.IPCNetTimeCfg_st;
import freesbell.demo.client.BridgeService.ControllerBinder;
import freesbell.demo.content.ContentCommon;
import freesbell.demo.utils.Cmds;
import freesbell.demo.utils.ServiceStub;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class SettingDateActivity extends Activity implements OnClickListener, OnTouchListener, OnCheckedChangeListener {
	private static String TAG="SettingDateActivity";
	private String strDID;
	private String cameraName;
	private final int FAIL=0;
	private final int SUCCESS=1;
	private final int PARAMS=3;
	private final int TIMEOUT=3000;
	private boolean successFlag;
	private JSONStructProtocal mJSONStructProtocal = new JSONStructProtocal();
	private IPCNetTimeCfg_st mIPCNetTimeCfg_st = mJSONStructProtocal.new IPCNetTimeCfg_st();
	private ProgressDialog progressDialog;
	private BridgeService mBridgeService;
	private ServiceConnection mConn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName arg0) {

		}

		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			ControllerBinder myBinder = (ControllerBinder) arg1;
			mBridgeService = myBinder.getBridgeService();
			mBridgeService.setServiceStub(mServiceStub);
			Cmds.getDevTimeInfo(mServiceStub, strDID);
//			bridgeService.getDateParams(SettingDateActivity.this, strDID, ContentCommon.MSG_TYPE_GET_PARAMS);
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
		setContentView(R.layout.settingdate);
		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(getString(R.string.date_get_params));
		progressDialog.show();
		mHandler.postDelayed(runnable,TIMEOUT);
		findView();
		setListener();
		dateBean = new DateBean();
		
		Intent intent=new Intent(this,BridgeService.class);
		bindService(intent, mConn, BIND_AUTO_CREATE);
		tvCameraName.setText(cameraName+"  "+getResources().getString(R.string.setting_time));
	}
	
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case FAIL:
				showToast(R.string.date_setting_failed);
				break;
			case SUCCESS:
				showToast(R.string.date_setting_success);
				finish();
				break;
			case PARAMS:
				break;

			default:
				break;
			}
		}

	};

	public final static int GMT_NEGA_12=0;
	public final static int GMT_NEGA_11_30=1;
	public final static int GMT_NEGA_11=2;
	public final static int GMT_NEGA_10_30=3;
	public final static int GMT_NEGA_10=4;
	public final static int GMT_NEGA_9_30=5;
	public final static int GMT_NEGA_9=6;
	public final static int GMT_NEGA_8_30=7;
	public final static int GMT_NEGA_8=8;
	public final static int GMT_NEGA_7_30=9;
	public final static int GMT_NEGA_7=10;
	public final static int GMT_NEGA_6_30=11;
	public final static int GMT_NEGA_6=12;
	public final static int GMT_NEGA_5_30=13;
	public final static int GMT_NEGA_5=14;
	public final static int GMT_NEGA_4_30=15;
	public final static int GMT_NEGA_4=16;
	public final static int GMT_NEGA_3_30=17;
	public final static int GMT_NEGA_3=18;
	public final static int GMT_NEGA_2_30=19;
	public final static int GMT_NEGA_2=20;
	public final static int GMT_NEGA_1_30=21;
	public final static int GMT_NEGA_1=22;
	public final static int GMT_NEGA_0_30=23;
	public final static int GMT_0=24;
	public final static int GMT_PLUS_0_30=25;
	public final static int GMT_PLUS_1=26;
	public final static int GMT_PLUS_1_30=27;
	public final static int GMT_PLUS_2=28;
	public final static int GMT_PLUS_2_30=29;
	public final static int GMT_PLUS_3=30;
	public final static int GMT_PLUS_3_30=31;
	public final static int GMT_PLUS_4=32;
	public final static int GMT_PLUS_4_30=33;
	public final static int GMT_PLUS_5=34;
	public final static int GMT_PLUS_5_30=35;
	public final static int GMT_PLUS_6=36;
	public final static int GMT_PLUS_6_30=37;
	public final static int GMT_PLUS_7=38;
	public final static int GMT_PLUS_7_30=39;
	public final static int GMT_PLUS_8 = 40;//北京时间,默认值
	public final static int GMT_PLUS_8_30 = 41;
	public final static int GMT_PLUS_9=42;
	public final static int GMT_PLUS_9_30=43;
	public final static int GMT_PLUS_10=44;
	public final static int GMT_PLUS_10_30=45;
	public final static int GMT_PLUS_11=46;
	public final static int GMT_PLUS_11_30=47;
	public final static int GMT_PLUS_12=48;
	
		private void setTimeZone() {
			String time = "";
			switch (dateBean.timeZone) {
			case GMT_NEGA_11:
				time = (setDeviceTime(dateBean,"GMT-11:00"));
				editTimeZone.setText(R.string.date_middle_island);
				break;
			case GMT_NEGA_10:
				time = (setDeviceTime(dateBean,"GMT-10:00"));
				editTimeZone.setText(R.string.date_hawaii);
				break;
			case GMT_NEGA_9:
				time = (setDeviceTime(dateBean,"GMT-09:00"));
				editTimeZone.setText(R.string.date_alaska);
				break;
			case GMT_NEGA_8:
				time = (setDeviceTime(dateBean,"GMT-08:00"));
				editTimeZone.setText(R.string.date_pacific_time);
				break;
			case GMT_NEGA_7:
				time = (setDeviceTime(dateBean,"GMT-07:00"));
				editTimeZone.setText(R.string.date_mountain_time);
				break;
			case GMT_NEGA_6:
				time = (setDeviceTime(dateBean,"GMT-06:00"));
				editTimeZone.setText(R.string.date_middle_part_time);
				break;
			case GMT_NEGA_5:
				time = (setDeviceTime(dateBean,"GMT-05:00"));
				editTimeZone.setText(R.string.date_eastern_time);
				break;
			case GMT_NEGA_4:
				time = (setDeviceTime(dateBean,"GMT-04:00"));
				editTimeZone.setText(R.string.date_ocean_time);
				break;
			case GMT_NEGA_3_30:
				time = (setDeviceTime(dateBean,"GMT-03:30"));
				editTimeZone.setText(R.string.date_newfoundland);
				break;
			case GMT_NEGA_3:
				time = (setDeviceTime(dateBean,"GMT-03:00"));
				editTimeZone.setText(R.string.date_brasilia);
				break;
			case GMT_NEGA_2:
				time = (setDeviceTime(dateBean,"GMT-02:00"));
				editTimeZone.setText(R.string.date_center_ocean);
				break;
			case GMT_NEGA_1:
				time = (setDeviceTime(dateBean,"GMT-01:00"));
				editTimeZone.setText(R.string.date_cape_verde_island);
				break;
			case GMT_0:
				time = (setDeviceTime(dateBean,"GMT"));
				editTimeZone.setText(R.string.date_greenwich);
				break;
			case GMT_PLUS_1:
				time = (setDeviceTime(dateBean,"GMT+01:00"));
				editTimeZone.setText(R.string.date_brussels);
				break;
			case GMT_PLUS_2:
				time = (setDeviceTime(dateBean,"GMT+02:00"));
				editTimeZone.setText(R.string.date_athens);
				break;
			case GMT_PLUS_3:
				time = (setDeviceTime(dateBean,"GMT+03:00"));
				editTimeZone.setText(R.string.date_nairobi);
				break;
			case GMT_PLUS_3_30:
				time = (setDeviceTime(dateBean,"GMT+03:30"));
				editTimeZone.setText(R.string.date_teheran);
				break;
			case GMT_PLUS_4:
				time = (setDeviceTime(dateBean,"GMT+04:00"));
				editTimeZone.setText(R.string.date_baku);
				break;
			case GMT_PLUS_4_30:
				time = (setDeviceTime(dateBean,"GMT+04:30"));
				editTimeZone.setText(R.string.date_kebuer);
				break;
			case GMT_PLUS_5:
				time = (setDeviceTime(dateBean,"GMT+05:00"));
				editTimeZone.setText(R.string.date_islamabad);
				break;
			case GMT_PLUS_5_30:
				time = (setDeviceTime(dateBean,"GMT+05:30"));
				editTimeZone.setText(R.string.date_calcutta);
				break;
				
			case GMT_PLUS_6:
				time = (setDeviceTime(dateBean,"GMT+06:00"));
				editTimeZone.setText(R.string.date_alamotu);
				break;
			case GMT_PLUS_7:
				time = (setDeviceTime(dateBean,"GMT+07:00"));
				editTimeZone.setText(R.string.date_bangkok);
				break;
			case GMT_PLUS_8:
				time = (setDeviceTime(dateBean,"GMT+08:00"));
				editTimeZone.setText(R.string.date_beijing);
				break;
			case GMT_PLUS_9:
				time = (setDeviceTime(dateBean,"GMT+09:00"));
				editTimeZone.setText(R.string.date_seoul);
				break;
			case GMT_PLUS_9_30:
				time = (setDeviceTime(dateBean,"GMT+09:30"));
				editTimeZone.setText(R.string.date_darwin);
				break;
			case GMT_PLUS_10:
				time = (setDeviceTime(dateBean,"GMT+10:00"));
				editTimeZone.setText(R.string.date_guam);
				break;
			case GMT_PLUS_11:
				time = (setDeviceTime(dateBean,"GMT+11:00"));
				editTimeZone.setText(R.string.date_suolumen);
				break;
			case GMT_PLUS_12:
				time = (setDeviceTime(dateBean,"GMT+12:00"));
				editTimeZone.setText(R.string.date_auckland);
				break;
			default:
				break;
			}
			
			tvDeviceTime.setText(time);
		}
	
	public static String getTimeZoneString(int timeZone) {
		String time = "";
		switch (timeZone) {
		case GMT_NEGA_11:
			time = "GMT-11:00";
			break;
		case GMT_NEGA_10:
			time = "GMT-10:00";
			break;
		case GMT_NEGA_9:
			time = "GMT-09:00";
			break;
		case GMT_NEGA_8:
			time = "GMT-08:00";
			break;
		case GMT_NEGA_7:
			time = "GMT-07:00";
			break;
		case GMT_NEGA_6:
			time = "GMT-06:00";
			break;
		case GMT_NEGA_5:
			time = "GMT-05:00";
			break;
		case GMT_NEGA_4:
			time = "GMT-04:00";
			break;
		case GMT_NEGA_3_30:
			time = "GMT-03:30";
			break;
		case GMT_NEGA_3:
			time = "GMT-03:00";
			break;
		case GMT_NEGA_2:
			time = "GMT-02:00";
			break;
		case GMT_NEGA_1:
			time = "GMT-01:00";
			break;
		case GMT_0:
			time = "GMT";
			break;
		case GMT_PLUS_1:
			time = "GMT+01:00";
			break;
		case GMT_PLUS_2:
			time = "GMT+02:00";
			break;
		case GMT_PLUS_3:
			time = "GMT+03:00";
			break;
		case GMT_PLUS_3_30:
			time = "GMT+03:30";
			break;
		case GMT_PLUS_4:
			time = "GMT+04:00";
			break;
		case GMT_PLUS_4_30:
			time = "GMT+04:30";
			break;
		case GMT_PLUS_5:
			time = "GMT+05:00";
			break;
		case GMT_PLUS_5_30:
			time = "GMT+05:30";
			break;
			
		case GMT_PLUS_6:
			time = "GMT+06:00";
			break;
		case GMT_PLUS_7:
			time = "GMT+07:00";
			break;
		case GMT_PLUS_8:
			time = "GMT+08:00";
			break;
		case GMT_PLUS_9:
			time = "GMT+09:00";
			break;
		case GMT_PLUS_9_30:
			time = "GMT+09:30";
			break;
		case GMT_PLUS_10:
			time = "GMT+10:00";
			break;
		case GMT_PLUS_11:
			time = "GMT+11:00";
			break;
		case GMT_PLUS_12:
			time = "GMT+12:00";
			break;
		default:
			break;
		}
		return(time);
	}
	
	private Button tvDeviceTime;
	private EditText editTimeZone;
	private EditText editNtpServer;
	private CheckBox cbxCheck;
	private ImageButton imgTimeZoneDown;
	private ImageButton imgNtpServerDown;
	private View ntpView;
    private PopupWindow timeZonePopWindow;
    private PopupWindow ntpServerPopWindow;
	private ScrollView scrollView;
	private DateBean dateBean;
	private Button btnOk;
	private ImageButton btnCancel;
	private Button btnCheckOut;
	private TextView tvCameraName;
	private static String setDeviceTime(DateBean date,String tz) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/London"));
		calendar.set(date.year, date.mon, date.day,date.hour,date.min,date.sec);
		
		SimpleDateFormat localFromat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		localFromat.setTimeZone(TimeZone.getDefault());
		localFromat.format(calendar.getTime());
		calendar = localFromat.getCalendar();

		return calendar.get(Calendar.YEAR) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "  " + (calendar.get(Calendar.HOUR_OF_DAY) + 1) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND) + " " + tz;
	}
	
	private Runnable runnable=new Runnable() {
		
		@Override
		public void run() {
			if(!successFlag){
				progressDialog.dismiss();
				//showToast(R.string.wifi_getparams_failed);
			}
		}
	};
	private void setListener() {
		imgTimeZoneDown.setOnClickListener(this);
		imgNtpServerDown.setOnClickListener(this);
		scrollView.setOnTouchListener(this);
		editTimeZone.setOnClickListener(this);
		editNtpServer.setOnClickListener(this);
		btnOk.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		cbxCheck.setOnCheckedChangeListener(this);
		btnCheckOut.setOnClickListener(this);
		progressDialog.setOnKeyListener(new OnKeyListener(){

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				
				if(keyCode == KeyEvent.KEYCODE_BACK){
					return true;
				}
				return false;
			}
			
		});
	}

	private void findView() {
		btnOk = (Button)findViewById(R.id.date_ok);
		btnCancel = (ImageButton)findViewById(R.id.date_cancel);
		btnCheckOut = (Button)findViewById(R.id.date_btn_checkout);
		
		tvDeviceTime = (Button)findViewById(R.id.date_tv_device_time);
		editTimeZone = (EditText)findViewById(R.id.date_edit_timezone);
		editNtpServer = (EditText)findViewById(R.id.date_edit_ntp_server);
		cbxCheck = (CheckBox)findViewById(R.id.date_cbx_check);
		
		imgTimeZoneDown = (ImageButton)findViewById(R.id.date_img_timezone_down);
		imgNtpServerDown = (ImageButton)findViewById(R.id.date_img_ntp_server_down);
		
		ntpView = findViewById(R.id.date_ntp_view);
		
		scrollView = (ScrollView)findViewById(R.id.scrollView1);
		tvCameraName = (TextView)findViewById(R.id.tv_camera_setting);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.date_ok:
//			setDate();
			Cmds.setDevTimeInfo(mServiceStub, strDID,dateBean);
			break;
		case R.id.date_cancel:
			finish();
			break;
		case R.id.date_btn_checkout:
			checkDeviceAsPhoneTime();
			break;
		case R.id.date_edit_timezone:
		case R.id.date_img_timezone_down:
			if(ntpServerPopWindow!=null&&ntpServerPopWindow.isShowing()){
				ntpServerPopWindow.dismiss();
				ntpServerPopWindow=null;
			}
			showTimeZonePopWindow();
			break;
		case R.id.date_edit_ntp_server:
		case R.id.date_img_ntp_server_down:
			if(timeZonePopWindow!=null&&timeZonePopWindow.isShowing()){
				timeZonePopWindow.dismiss();
				timeZonePopWindow=null;
			}
			showNtpServerPopWindow();
			break;
			///ntpServer
		case R.id.date_ntpserver_kriss:
			ntpServerPopWindow.dismiss();
			dateBean.setNtp_ser(getResources().getString(R.string.date_ntp_server_time_kriss_re_kr));
			editNtpServer.setText(R.string.date_ntp_server_time_kriss_re_kr);
			break;
		case R.id.date_ntpserver_nist:
			ntpServerPopWindow.dismiss();
			dateBean.setNtp_ser(getResources().getString(R.string.date_ntp_server_time_nist_gov));
			editNtpServer.setText(R.string.date_ntp_server_time_nist_gov);
			break;
		case R.id.date_ntpserver_nuri:
			ntpServerPopWindow.dismiss();
			dateBean.setNtp_ser(getResources().getString(R.string.date_ntp_server_time_nuri_net));
			editNtpServer.setText(R.string.date_ntp_server_time_nuri_net);
			break;
		case R.id.date_ntpserver_windows:
			ntpServerPopWindow.dismiss();
			dateBean.setNtp_ser(getResources().getString(R.string.date_ntp_server_time_windows_com));
			editNtpServer.setText(R.string.date_ntp_server_time_windows_com);
			break;
			
			//timezone
		case R.id.date_zone_middle_island:
			Log.d("tag","��;�� 1");
			timeZonePopWindow.dismiss();
			dateBean.timeZone = GMT_NEGA_11;
			editTimeZone.setText(R.string.date_middle_island);
			Log.d("tag","��;�� 2");
			break;
		case R.id.date_zone_hawaii:
			timeZonePopWindow.dismiss();
			dateBean.timeZone = GMT_NEGA_10;
			editTimeZone.setText(R.string.date_hawaii);
			break;
		case R.id.date_zone_alaska:
			timeZonePopWindow.dismiss();
			dateBean.timeZone = GMT_NEGA_9;
//			Long nowAlaska=new Long(dateBean.getNow());
//			time = (setDeviceTime(nowAlaska*1000, "GMT-09:00"));
			editTimeZone.setText(R.string.date_alaska);
			break;
		case R.id.date_zone_pacific_time:
			timeZonePopWindow.dismiss();
			dateBean.timeZone = GMT_NEGA_8;
//			Long nowPacific=new Long(dateBean.getNow());
//			time = (setDeviceTime(nowPacific*1000, "GMT-08:00"));
			editTimeZone.setText(R.string.date_pacific_time);
			break;
		case R.id.date_zone_mountain_time:
			timeZonePopWindow.dismiss();
			dateBean.timeZone = GMT_NEGA_7;
//			Long nowMountain=new Long(dateBean.getNow());
//			time = (setDeviceTime(nowMountain*1000, "GMT-07:00"));
			editTimeZone.setText(R.string.date_mountain_time);
			break;
		case R.id.date_zone_middle_part_time:
			timeZonePopWindow.dismiss();
			dateBean.timeZone = GMT_NEGA_6;
//			Long nowMiddlePart=new Long(dateBean.getNow());
//			time = (setDeviceTime(nowMiddlePart*1000, "GMT-06:00"));
			editTimeZone.setText(R.string.date_middle_part_time);
			break;
		case R.id.date_zone_eastern_time:
			timeZonePopWindow.dismiss();
			dateBean.timeZone = GMT_NEGA_5;
//			Long nowEastern=new Long(dateBean.getNow());
//			time = (setDeviceTime(nowEastern*1000, "GMT-05:00"));
			editTimeZone.setText(R.string.date_eastern_time);
			break;
		case R.id.date_zone_ocean_time:
			timeZonePopWindow.dismiss();
			dateBean.timeZone = GMT_NEGA_4;
//			Long nowOcean=new Long(dateBean.getNow());
//			time = (setDeviceTime(nowOcean*1000, "GMT-04:00"));
			editTimeZone.setText(R.string.date_ocean_time);
			break;
		case R.id.date_zone_newfoundland:
			timeZonePopWindow.dismiss();
			dateBean.timeZone = GMT_NEGA_3_30;
//			Long nowNewfoundland=new Long(dateBean.getNow());
//			time = (setDeviceTime(nowNewfoundland*1000, "GMT-03:30"));
			editTimeZone.setText(R.string.date_newfoundland);
			break;
		case R.id.date_zone_brasilia:
			timeZonePopWindow.dismiss();
			dateBean.timeZone = GMT_NEGA_3;
//			Long nowBrasilia=new Long(dateBean.getNow());
//			time = (setDeviceTime(nowBrasilia*1000, "GMT-03:00"));
			editTimeZone.setText(R.string.date_brasilia);
			break;
		case R.id.date_zone_center_ocean:
			timeZonePopWindow.dismiss();
			dateBean.timeZone = GMT_NEGA_2;
//			Long nowCenterOcean=new Long(dateBean.getNow());
//			time = (setDeviceTime(nowCenterOcean*1000, "GMT-02:00"));
			editTimeZone.setText(R.string.date_center_ocean);
			break;
		case R.id.date_zone_cap_verde_island:
			timeZonePopWindow.dismiss();
			dateBean.timeZone = GMT_NEGA_1;
//			Long nowCapeVerde=new Long(dateBean.getNow());
//			time = (setDeviceTime(nowCapeVerde*1000, "GMT-01:00"));
			editTimeZone.setText(R.string.date_cape_verde_island);
			break;
		case R.id.date_zone_greenwich:
			timeZonePopWindow.dismiss();
			dateBean.timeZone = GMT_0;
//			Long nowGreenwich=new Long(dateBean.getNow());
//			time = (setDeviceTime(nowGreenwich*1000, "GMT"));
			editTimeZone.setText(R.string.date_greenwich);
			break;
		case R.id.date_zone_brussels:
			timeZonePopWindow.dismiss();
			dateBean.timeZone = GMT_PLUS_1;
//			Long nowBrussels=new Long(dateBean.getNow());
//			time = (setDeviceTime(nowBrussels*1000, "GMT+01:00"));
			editTimeZone.setText(R.string.date_brussels);
			break;
		case R.id.date_zone_athens:
			timeZonePopWindow.dismiss();
			dateBean.timeZone = GMT_PLUS_2;
//			Long nowAthens=new Long(dateBean.getNow());
//			time = (setDeviceTime(nowAthens*1000, "GMT+02:00"));
			editTimeZone.setText(R.string.date_athens);
			break;
		case R.id.date_zone_nairobi:
			timeZonePopWindow.dismiss();
			dateBean.timeZone = GMT_PLUS_3;
//			Long nowNairobi=new Long(dateBean.getNow());
//			time = (setDeviceTime(nowNairobi*1000, "GMT+03:00"));
			editTimeZone.setText(R.string.date_nairobi);
			break;
		case R.id.date_zone_teheran:
			timeZonePopWindow.dismiss();
			dateBean.timeZone = GMT_PLUS_3_30;
//			Long nowTeheran=new Long(dateBean.getNow());
//			time = (setDeviceTime(nowTeheran*1000, "GMT+03:30"));
			editTimeZone.setText(R.string.date_teheran);
			break;
		case R.id.date_zone_baku:
			timeZonePopWindow.dismiss();
			dateBean.timeZone = GMT_PLUS_4;
//			Long nowBaku=new Long(dateBean.getNow());
//			time = (setDeviceTime(nowBaku*1000, "GMT+04:00"));
			editTimeZone.setText(R.string.date_baku);
			break;
		case R.id.date_zone_kebuer:
			timeZonePopWindow.dismiss();
			dateBean.timeZone = GMT_PLUS_4_30;
//			Long nowKebuer=new Long(dateBean.getNow());
//			time = (setDeviceTime(nowKebuer*1000, "GMT+04:30"));
			editTimeZone.setText(R.string.date_kebuer);
			break;
		case R.id.date_zone_islamabad:
			timeZonePopWindow.dismiss();
			dateBean.timeZone = GMT_PLUS_5;
//			Long nowIslamabad=new Long(dateBean.getNow());
//			time = (setDeviceTime(nowIslamabad*1000, "GMT+05:00"));
			editTimeZone.setText(R.string.date_islamabad);
			break;
		case R.id.date_zone_calcutta:
			timeZonePopWindow.dismiss();
			dateBean.timeZone = GMT_PLUS_5_30;
//			Long nowCalcutta=new Long(dateBean.getNow());
//			time = (setDeviceTime(nowCalcutta*1000, "GMT+05:30"));
			editTimeZone.setText(R.string.date_calcutta);
			break;
		case R.id.date_zone_alamotu:
			timeZonePopWindow.dismiss();
			dateBean.timeZone = GMT_PLUS_6;
//			Long nowAlamotu=new Long(dateBean.getNow());
//			time = (setDeviceTime(nowAlamotu*1000, "GMT+06:00"));
			editTimeZone.setText(R.string.date_alamotu);
			break;
		case R.id.date_zone_bangkok:
			timeZonePopWindow.dismiss();
			dateBean.timeZone = GMT_PLUS_7;
//			Long nowBangkok=new Long(dateBean.getNow());
//			time = (setDeviceTime(nowBangkok*1000, "GMT+07:00"));
			editTimeZone.setText(R.string.date_bangkok);
			break;
		case R.id.date_zone_beijing:
			timeZonePopWindow.dismiss();
			dateBean.timeZone = GMT_PLUS_8;
//			Long nowBeijing=new Long(dateBean.getNow());
//			time = (setDeviceTime(nowBeijing*1000, "GMT+08:00"));
			editTimeZone.setText(R.string.date_beijing);
			break;
		case R.id.date_zone_seoul:
			timeZonePopWindow.dismiss();
			dateBean.timeZone = GMT_PLUS_9;
//			Long nowSeoul=new Long(dateBean.getNow());
//			time = (setDeviceTime(nowSeoul*1000, "GMT+09:00"));
			editTimeZone.setText(R.string.date_seoul);
			break;
		case R.id.date_zone_darwin:
			timeZonePopWindow.dismiss();
			dateBean.timeZone = GMT_PLUS_9_30;
//			Long nowDarwin=new Long(dateBean.getNow());
//			time = (setDeviceTime(nowDarwin*1000, "GMT+09:30"));
			editTimeZone.setText(R.string.date_darwin);
			break;
		case R.id.date_zone_guam:
			timeZonePopWindow.dismiss();
			dateBean.timeZone = GMT_PLUS_10;
//			Long nowGuam=new Long(dateBean.getNow());
//			time = (setDeviceTime(nowGuam*1000, "GMT+10:00"));
			editTimeZone.setText(R.string.date_guam);
			break;
		case R.id.date_zone_soulumen:
			timeZonePopWindow.dismiss();
			dateBean.timeZone = GMT_PLUS_11;
//			Long nowSoulmen=new Long(dateBean.getNow());
//			time = (setDeviceTime(nowSoulmen*1000, "GMT+11:00"));
			editTimeZone.setText(R.string.date_suolumen);
			break;
		case R.id.date_zone_auckland:
			timeZonePopWindow.dismiss();
			dateBean.timeZone = GMT_PLUS_12;
//			Long nowAuckland=new Long(dateBean.getNow());
//			time = (setDeviceTime(nowAuckland*1000, "GMT+12:00"));
			editTimeZone.setText(R.string.date_auckland);
			break;
		default:
			break;
		}
	}
//	private void setDate() {
//		Log.d("info","now:0  tz:"+dateBean.getTz()+" enable:"+dateBean.ntpEnable+" server:"+dateBean.getNtp_ser());
//		NativeCaller.PPPPDatetimeSetting(strDID, 0, dateBean.getTz(), dateBean.ntpEnable, dateBean.getNtp_ser());
//	}
	private void checkDeviceAsPhoneTime() {
		TimeZone timeZone=TimeZone.getDefault();
		dateBean.timeZone = timeZone.getRawOffset()/720000;

		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+0"));//Calendar.getInstance(TimeZone.getTimeZone("Europe/London"));

		dateBean.year = calendar.get(Calendar.YEAR);;
		dateBean.day = calendar.get(Calendar.DAY_OF_MONTH);
		dateBean.mon = calendar.get(Calendar.MONTH) + 1;
		dateBean.hour = calendar.get(Calendar.HOUR_OF_DAY);
		dateBean.min = calendar.get(Calendar.MINUTE);
		dateBean.sec = calendar.get(Calendar.SECOND);
		
		Log.d(TAG,"tz:"+dateBean.timeZone +",Ntp_enable():"+ dateBean.ntpEnable+",dateBean.getNtp_ser():"+dateBean.getNtp_ser());
//		NativeCaller.PPPPDatetimeSetting(strDID, now, tz, dateBean.getNtp_enable(),dateBean.getNtp_ser());
		
		Cmds.setDevTimeInfo(mServiceStub, strDID,dateBean);
	}
	
	private void showNtpServerPopWindow() {
		if(ntpServerPopWindow!=null&&ntpServerPopWindow.isShowing()){
			return;
		}
		LinearLayout layout=(LinearLayout)LayoutInflater.from(this).inflate(R.layout.settingdate_ntpserver_popwindow, null);
		Button ntpServerKriss=(Button)layout.findViewById(R.id.date_ntpserver_kriss);
		Button ntpServerNist=(Button)layout.findViewById(R.id.date_ntpserver_nist);
		Button ntpServerNuri=(Button)layout.findViewById(R.id.date_ntpserver_nuri);
		Button ntpServerWindows=(Button)layout.findViewById(R.id.date_ntpserver_windows);
		ntpServerKriss.setOnClickListener(this);
		ntpServerNist.setOnClickListener(this);
		ntpServerNuri.setOnClickListener(this);
		ntpServerWindows.setOnClickListener(this);
		ntpServerPopWindow=new PopupWindow(layout,WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
		ntpServerPopWindow.showAsDropDown(imgNtpServerDown, -200, 0);
	}

	private void showTimeZonePopWindow() {
		if(timeZonePopWindow!=null&&timeZonePopWindow.isShowing()){
			return;
		}
		LinearLayout layout=(LinearLayout)LayoutInflater.from(this).inflate(R.layout.settingdate_timezone_popwindow, null);
		Button zoneMiddleIsland=(Button)layout.findViewById(R.id.date_zone_middle_island);
		Button zoneHawaii=(Button)layout.findViewById(R.id.date_zone_hawaii);
		Button zoneAlaska=(Button)layout.findViewById(R.id.date_zone_alaska);
		Button zonePacificTime=(Button)layout.findViewById(R.id.date_zone_pacific_time);
		Button zoneMountainTime=(Button)layout.findViewById(R.id.date_zone_mountain_time);
		Button zoneMiddlePartTime=(Button)layout.findViewById(R.id.date_zone_middle_part_time);
		Button zoneEasternTime=(Button)layout.findViewById(R.id.date_zone_eastern_time);
		Button zoneOceanTime=(Button)layout.findViewById(R.id.date_zone_ocean_time);
		Button zoneNewfoundland=(Button)layout.findViewById(R.id.date_zone_newfoundland);
		Button zoneBrasilia=(Button)layout.findViewById(R.id.date_zone_brasilia);
		Button zoneCenterOcean=(Button)layout.findViewById(R.id.date_zone_center_ocean);
		Button zoneCapeVerdeIsland=(Button)layout.findViewById(R.id.date_zone_cap_verde_island);
		Button zoneGreenWich=(Button)layout.findViewById(R.id.date_zone_greenwich);
		Button zoneBrussels=(Button)layout.findViewById(R.id.date_zone_brussels);
		Button zoneAthens=(Button)layout.findViewById(R.id.date_zone_athens);
		Button zoneNairobi=(Button)layout.findViewById(R.id.date_zone_nairobi);
		Button zoneTeheran=(Button)layout.findViewById(R.id.date_zone_teheran);
		Button zoneBaku=(Button)layout.findViewById(R.id.date_zone_baku);
		Button zoneKebuer=(Button)layout.findViewById(R.id.date_zone_kebuer);
		Button zoneIslamambad=(Button)layout.findViewById(R.id.date_zone_islamabad);
		Button zoneIslamabad=(Button)layout.findViewById(R.id.date_zone_calcutta);
		Button zoneAlamotu=(Button)layout.findViewById(R.id.date_zone_alamotu);
		Button zoneBangkok=(Button)layout.findViewById(R.id.date_zone_bangkok);
		Button zoneBeijing=(Button)layout.findViewById(R.id.date_zone_beijing);
		Button zoneSeoul=(Button)layout.findViewById(R.id.date_zone_seoul);
		Button zoneDarwin=(Button)layout.findViewById(R.id.date_zone_darwin);
		Button zoneGuam=(Button)layout.findViewById(R.id.date_zone_guam);
		Button zoneSoulumen=(Button)layout.findViewById(R.id.date_zone_soulumen);
		Button zoneAuckland=(Button)layout.findViewById(R.id.date_zone_auckland);
		
		zoneMiddleIsland.setOnClickListener(this);
		zoneHawaii.setOnClickListener(this);
		zoneAlaska.setOnClickListener(this);
		zonePacificTime.setOnClickListener(this);
		zoneMountainTime.setOnClickListener(this);
		zoneMiddlePartTime.setOnClickListener(this);
		zoneEasternTime.setOnClickListener(this);
		zoneOceanTime.setOnClickListener(this);
		zoneNewfoundland.setOnClickListener(this);
		zoneBrasilia.setOnClickListener(this);
		zoneCenterOcean.setOnClickListener(this);
		zoneCapeVerdeIsland.setOnClickListener(this);
		zoneGreenWich.setOnClickListener(this);
		zoneBrussels.setOnClickListener(this);
		zoneAthens.setOnClickListener(this);
		zoneNairobi.setOnClickListener(this);
		zoneTeheran.setOnClickListener(this);
		zoneBaku.setOnClickListener(this);
		zoneKebuer.setOnClickListener(this);
		zoneIslamambad.setOnClickListener(this);
		zoneIslamabad.setOnClickListener(this);
		zoneAlamotu.setOnClickListener(this);
		zoneBangkok.setOnClickListener(this);
		zoneBeijing.setOnClickListener(this);
		zoneSeoul.setOnClickListener(this);
		zoneDarwin.setOnClickListener(this);
		zoneGuam.setOnClickListener(this);
		zoneSoulumen.setOnClickListener(this);
		zoneAuckland.setOnClickListener(this);
		
		
		timeZonePopWindow=new PopupWindow(layout,WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
		timeZonePopWindow.showAsDropDown(imgTimeZoneDown, -310,0);
	}

	private void getDataFromOther() {
		Intent intent = getIntent();
		strDID = intent.getStringExtra(ContentCommon.STR_CAMERA_ID);
		cameraName=intent.getStringExtra(ContentCommon.STR_CAMERA_NAME);
	}
//	/**
//	 * BridgeService callback
//	 * **/
//	public void CallBack_DatetimeParams(String did, int now, int tz, int ntp_enable, String ntp_svr){
//		Log.d("info", "CallBack_DatetimeParams"+",now:"+now+",tz:"+tz+",ntp_enable:"+ntp_enable+",ntp_svr:"+ntp_svr);
////		dateBean.setNow(now);
////		dateBean.timeZone = (tz);
////		dateBean.setNtp_enable(ntp_enable);
////		dateBean.setNtp_ser(ntp_svr);
////		mHandler.sendEmptyMessage(PARAMS);
//	}
	
	public DateBean parseTimeJson(JSONObject jsonData){
		DateBean dateBean = null;
//		JSONObject jsonData = null;
//		try {
//			jsonData = new JSONObject(json);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		if(jsonData == null)
			return null;
//		try {
			if(mIPCNetTimeCfg_st.parseJSON(jsonData)){
//			JSONObject jsroot = jsonData.getJSONObject("Time.Conf");
//			if(jsroot!=null){
				dateBean = new DateBean();
				dateBean.ntpEnable = mIPCNetTimeCfg_st.NtpEnable;//jsroot.getBoolean("NtpEnable");
				dateBean.ntpSer = mIPCNetTimeCfg_st.NtpServ;//jsroot.getString("NtpServ");
				dateBean.timeZone = mIPCNetTimeCfg_st.TimeZone;//jsroot.getInt("TimeZone");
				
//				JSONObject jsdate = jsroot.getJSONObject("Date");
				dateBean.year = mIPCNetTimeCfg_st.Date.Year;//jsdate.getInt("Year");
				dateBean.mon = mIPCNetTimeCfg_st.Date.Mon;//jsdate.getInt("Mon");
				dateBean.day = mIPCNetTimeCfg_st.Date.Day;//jsdate.getInt("Day");
				
//				JSONObject jstime = jsroot.getJSONObject("Time");
				dateBean.hour = mIPCNetTimeCfg_st.Time.Hour;//jstime.getInt("Hour");
				dateBean.min = mIPCNetTimeCfg_st.Time.Min;//jstime.getInt("Min");
				dateBean.sec = mIPCNetTimeCfg_st.Time.Sec;//jstime.getInt("Sec");
				
				Log.d(TAG,"ntpEnable:" + dateBean.ntpEnable + " ntpSer:" + dateBean.ntpSer + " timeZone:" + dateBean.timeZone + " " + 
						dateBean.year + "-" + dateBean.mon + "-" + dateBean.day + " " + dateBean.hour + ":" + dateBean.min + ":" + dateBean.sec);
				
			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		
		return dateBean;
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
			case ContentCommon.IPCNET_GET_TIME_RESP:
				dateBean = parseTimeJson(jsonData);
				if(dateBean!=null){
					if(progressDialog.isShowing()){
						progressDialog.dismiss();
					}
				}
				cbxCheck.setChecked(dateBean.ntpEnable);
				editNtpServer.setText(dateBean.getNtp_ser());
				setTimeZone();
				break;
			case ContentCommon.IPCNET_SET_TIME_RESP:
				try {
					int ret = jsonData.getInt("ret");
					if(ret == 0){
						showToast(R.string.date_setting_success);
						setTimeZone();
					}else{
						showToast(R.string.date_setting_failed);
					}
				}catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};
	/**
	 * BridgeService Feedback execute
	 * **/
//	public void CallBack_SetSystemParamsResult(String did, int paramType, int result){
//		Log.d("info","result:"+result+" paramType:"+paramType);
//		mHandler.sendEmptyMessage(result);
//	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mBridgeService.unbindSetNull(mServiceStub);
		unbindService(mConn);
	}
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if(timeZonePopWindow!=null&&timeZonePopWindow.isShowing()){
			timeZonePopWindow.dismiss();
			timeZonePopWindow=null;
		}
    	if(ntpServerPopWindow!=null&&ntpServerPopWindow.isShowing()){
			ntpServerPopWindow.dismiss();
			ntpServerPopWindow=null;
		}
    	return super.onTouchEvent(event);
    }
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		if(timeZonePopWindow!=null&&timeZonePopWindow.isShowing()){
			timeZonePopWindow.dismiss();
			timeZonePopWindow=null;
		}
		if(ntpServerPopWindow!=null&&ntpServerPopWindow.isShowing()){
			ntpServerPopWindow.dismiss();
			ntpServerPopWindow=null;
		}
		return false;
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
		if(isChecked){
			dateBean.ntpEnable = true;
			ntpView.setVisibility(View.VISIBLE);
		}else{
			dateBean.ntpEnable = false;
			ntpView.setVisibility(View.GONE);
		}
	}

	public void showToast(String content){
		Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
	}
	public void showToast(int rid){
		Toast.makeText(this, getResources().getString(rid), Toast.LENGTH_LONG).show();
	}
}
