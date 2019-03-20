package freesbell.demo.client;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import freesbell.demo.client.R;
import fenzhi.nativecaller.NativeCaller;
import freesbell.demo.adapter.DeviceAttributeListAdapter;
import freesbell.demo.adapter.RecorderScheduleListAdapter.RecorderSchedule;
import freesbell.demo.bean.CameraParamsBean;
import freesbell.demo.bean.DateBean;
import freesbell.demo.bean.JSONStructProtocal;
import freesbell.demo.bean.WifiScanBean;
import freesbell.demo.bean.JSONStructProtocal.DeviceCustomFirmwareUpateInfo;
import freesbell.demo.bean.JSONStructProtocal.DeviceSystemFirmwareUpateInfo;
import freesbell.demo.bean.JSONStructProtocal.DeviceVendorFirmwareUpateInfo;
import freesbell.demo.bean.JSONStructProtocal.IPCNETMoveAlarmCfg_st;
import freesbell.demo.bean.JSONStructProtocal.IPCNetAntiFlickerInfo_st;
import freesbell.demo.bean.JSONStructProtocal.IPCNetDeNoiseInfo_st;
import freesbell.demo.bean.JSONStructProtocal.IPCNetEmailCfg_st;
import freesbell.demo.bean.JSONStructProtocal.IPCNetEthConfig_st;
import freesbell.demo.bean.JSONStructProtocal.IPCNetExpType_st;
import freesbell.demo.bean.JSONStructProtocal.IPCNetFtpCfg_st;
import freesbell.demo.bean.JSONStructProtocal.IPCNetGetOsdCfg_st;
import freesbell.demo.bean.JSONStructProtocal.IPCNetMobileNetworkInfo_st;
import freesbell.demo.bean.JSONStructProtocal.IPCNetOsdCfg_st;
import freesbell.demo.bean.JSONStructProtocal.IPCNetPicColorInfo_st;
import freesbell.demo.bean.JSONStructProtocal.IPCNetRecordGetCfg_st;
import freesbell.demo.bean.JSONStructProtocal.IPCNetTimeCfg_st;
import freesbell.demo.bean.JSONStructProtocal.IPCNetUpgradeCfg_st;
import freesbell.demo.bean.JSONStructProtocal.IPCNetUpgradeInfo_st;
import freesbell.demo.bean.JSONStructProtocal.IPCNetWdrInfo_st;
import freesbell.demo.bean.JSONStructProtocal.IPCNetWhBalance_st;
import freesbell.demo.bean.JSONStructProtocal.IPCNetWifiApItem;
import freesbell.demo.bean.JSONStructProtocal.IPCNetWifiAplist;
import freesbell.demo.bean.JSONStructProtocal.IPCNetWirelessConfig_st;
import freesbell.demo.client.BridgeService.ControllerBinder;
import freesbell.demo.content.ContentCommon;
import freesbell.demo.utils.Cmds;
import freesbell.demo.utils.DatabaseUtil;
import freesbell.demo.utils.ServiceStub;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsoluteLayout.LayoutParams;

/**
 * 
 * @author �޸Ĺ�
 * 
 */
public class DeviceAttributeActivity extends Activity implements OnItemClickListener,
		OnClickListener {
	private final int RESULT_CODE = 200;
	private static final String TAG = "DeviceAttributeActivity";
	private ListView mListView = null;
//	private ListView editListView = null;
	private List<DeviceAttrBean> listItems;
//	private List<String> elistItems;
	
	private final int DEV_MAIL = 0;
	private final int DEV_FTP = 1;
	private final int DEV_SDCard = 2;
	private final int DEV_ALARM  = 3;
	private final int DEV_WIFI = 4;
	private final int DEV_NETWORK = 5;
	private final int DEV_W3G4G = 6;
	private final int DEV_USER = 7;
	private final int DEV_DATETIME = 8;
	private final int DEV_OTHER_SETTING = 9;
	private final int DEV_CPU = 10;
	private final int DEV_DSP = 11;
	private final int DEV_MODEL = 12;
	private final int DEV_SENSOR = 13;
	private final int DEV_ONVIF = 14;
	private final int DEV_GB28181 = 15;
	private final int DEV_KERNEL = 16;
	private final int DEV_OS = 18;
	private final int DEV_HARDWARE_RES = 19;
	private final int DEV_SUB_DEV = 20;
	private final int DEV_OSD = 21;
	
	private CameraParamsBean mCameraParamsBean;
	private int selectPosition;
	private String strDID;
	private String cameraName;
	private String user;
	private String pwd;
	private TextView tvCameraName;
	private ImageButton back;
	private int status;
//	private TextView tvSet;
	private DatabaseUtil dbUtil = null;
	Context context;
	String temp = "temp";
	Bundle b = new Bundle();
	Intent it = new Intent();

	private JSONStructProtocal mJSONStructProtocal = new JSONStructProtocal();
	private IPCNetRecordGetCfg_st mIPCNetRecordGetCfg_st = mJSONStructProtocal.new IPCNetRecordGetCfg_st();
	private IPCNetPicColorInfo_st mIPCNetPicColorInfo_st = mJSONStructProtocal.new IPCNetPicColorInfo_st();
	private IPCNetEthConfig_st mIPCNetEthConfig_st = mJSONStructProtocal.new IPCNetEthConfig_st();
	private IPCNetMobileNetworkInfo_st mIPCNetMobileNetworkInfo_st = mJSONStructProtocal.new IPCNetMobileNetworkInfo_st();
	private IPCNetOsdCfg_st mIPCNetOsdCfg_st = mJSONStructProtocal.new IPCNetOsdCfg_st();
	private IPCNetExpType_st mIPCNetExpType_st = mJSONStructProtocal.new IPCNetExpType_st();
	private IPCNetAntiFlickerInfo_st mIPCNetAntiFlickerInfo_st = mJSONStructProtocal.new IPCNetAntiFlickerInfo_st();
	private IPCNetWhBalance_st mIPCNetWhBalance_st = mJSONStructProtocal.new IPCNetWhBalance_st();
	private IPCNetDeNoiseInfo_st mIPCNetDeNoiseInfo_st = mJSONStructProtocal.new IPCNetDeNoiseInfo_st();
	private IPCNetWdrInfo_st mIPCNetWdrInfo_st = mJSONStructProtocal.new IPCNetWdrInfo_st();
	private IPCNetWirelessConfig_st mIPCNetWirelessConfig_st = mJSONStructProtocal.new IPCNetWirelessConfig_st();
	private IPCNetWifiAplist mIPCNetWifiAplist = mJSONStructProtocal.new IPCNetWifiAplist();
	private IPCNetUpgradeCfg_st mIPCNetUpgradeCfg_st = mJSONStructProtocal.new IPCNetUpgradeCfg_st();
	private DeviceSystemFirmwareUpateInfo mDeviceSystemFirmwareUpateInfo = mJSONStructProtocal.new DeviceSystemFirmwareUpateInfo();
	private DeviceCustomFirmwareUpateInfo mDeviceCustomFirmwareUpateInfo = mJSONStructProtocal.new DeviceCustomFirmwareUpateInfo();
	private DeviceVendorFirmwareUpateInfo mDeviceVendorFirmwareUpateInfo = mJSONStructProtocal.new DeviceVendorFirmwareUpateInfo();
	private IPCNetUpgradeInfo_st mIPCNetUpgradeInfo_st = mJSONStructProtocal.new IPCNetUpgradeInfo_st();
	private IPCNETMoveAlarmCfg_st mIPCNETMoveAlarmCfg = mJSONStructProtocal.new IPCNETMoveAlarmCfg_st();
	private IPCNetEmailCfg_st jsIPCNetEmailCfg_st = mJSONStructProtocal.new IPCNetEmailCfg_st();
	private IPCNetFtpCfg_st jsIPCNetFtpCfg_st = mJSONStructProtocal.new IPCNetFtpCfg_st();
	private IPCNetTimeCfg_st mIPCNetTimeCfg_st = mJSONStructProtocal.new IPCNetTimeCfg_st();
	private BridgeService mBridgeService;
	
	private ServiceConnection mConn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			ControllerBinder myBinder = (ControllerBinder) service;
			mBridgeService = myBinder.getBridgeService();
			mBridgeService.setServiceStub(mServiceStub);
			(new Thread(new Runnable(){
				@Override
				public void run() {
					Cmds.getDevInfo(mServiceStub,strDID);
				}
			})).start();
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
	
	DeviceAttributeListAdapter mAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getDataFromOther();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.device_attribute);
		initData();
		findView();

		mAdapter = new DeviceAttributeListAdapter(this, listItems);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		tvCameraName.setText(cameraName + " "
				+ getResources().getString(R.string.dev_attribute));
		
		Intent intent=new Intent(this,BridgeService.class);
		bindService(intent, mConn, Context.BIND_AUTO_CREATE);
	}

	private boolean isDismiss = false;

	private void showDelSureDialog(final String did) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				DeviceAttributeActivity.this);
		builder.setMessage(R.string.del_alert);
		builder.setPositiveButton(R.string.str_ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Log.i("info", "@@@@@@@@@__did" + strDID);
						dbUtil.open();
						dbUtil.deleteCamera(did);
						dbUtil.close();
						isDismiss = true;
						setBackData();
					}
				});
		builder.setNegativeButton(R.string.str_cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				});
		builder.show();
	}

	private void getDataFromOther() {
		Intent intent = getIntent();
		selectPosition = intent.getIntExtra("selectPosition", 0);
		strDID = intent.getStringExtra(ContentCommon.STR_CAMERA_ID);
//		Log.i(TAG, "DID-----" + strDID);
		mCameraParamsBean = BridgeService.getCameraBean(strDID);
		cameraName = mCameraParamsBean.name;
		user = mCameraParamsBean.user;
		pwd = mCameraParamsBean.pwd;
		status = mCameraParamsBean.status;
	}

	private void initData() {

		dbUtil = new DatabaseUtil(this);
		updateSettingListItem();
	}

	String model = "IPC201601201815";
	String cpu = "Hi3518E (ARM9@Max. 440MHz)";
	String dsp = "H.264(720P@25fps)+JPEG(720P@3fs)";
	String sensor = "OV9712+(720P@30fps)";
	String wifi = "RTL8188ETV(AP&STA Coexistence)";
	String eth = "RTL8201F(100Mbps)";
	String mobile = "ME3760_V2(4G)";
	
	String datetime = null;
	
	String osd_tips = "OSD Supported";
	String onvif = "Support 2.4 and Partially 2.6";
	String gb28181 = "0.1";
	String mail = "Ver3.16";
	String ftp = "Ver1.26";
	String storage = "Tatol:128GB Availble:61GB(128GB Supported)";
	String alarm = "Ver6.03";
	String OSver = "iCamera-1.68";
	String Kern = "Linux-3.08";
	String firm = null;
	private String getDevAttrStr(int id){
		switch(id){
		case DEV_MAIL:
			return mail;
		case DEV_FTP:
			return ftp;
		case DEV_SDCard:
			return storage;
		case DEV_OSD:
			return osd_tips;
		case DEV_ALARM:
			return alarm;
		case DEV_WIFI:
			return wifi;
		case DEV_NETWORK:
			return eth;
		case DEV_W3G4G:
			return mobile;
		case DEV_USER:
			return "Name:" + mCameraParamsBean.name + "\nUUID:" + 
			mCameraParamsBean.did;
		case DEV_DATETIME:{
			if(datetime == null){
			TimeZone timeZone=TimeZone.getDefault();
			int tz = timeZone.getRawOffset()/720000;

			Calendar calendar=Calendar.getInstance();

			int year = calendar.get(Calendar.YEAR);;
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			int mon = calendar.get(Calendar.MONTH) + 1;
			int hour = calendar.get(Calendar.HOUR);
			int min = calendar.get(Calendar.MINUTE);
			int sec = calendar.get(Calendar.SECOND);
			return year + "/" + mon +"/"+ day + "  " + hour + ":" + min + ":" + sec + "  " + SettingDateActivity.getTimeZoneString(tz);
			}else{
				return datetime;
			}
		}
		case DEV_OTHER_SETTING:
			if(firm == null){
			return mCameraParamsBean.mDevVer;
			}else{
				return firm;
			}
		case DEV_CPU:
			return cpu;
		case DEV_DSP:
			return dsp;
		case DEV_MODEL:
			return model;
		case DEV_SENSOR:
			return sensor;
		case DEV_ONVIF:
			return onvif;
		case DEV_GB28181:
			return gb28181;
		case DEV_KERNEL:
			return Kern;
		case DEV_OS:
			return OSver;
		case DEV_HARDWARE_RES:
			return mCameraParamsBean.gpio_num + " GPIO(s)";
//		case DEV_SUB_DEV:
//			return mDeviceData.getSubdevNum() + " Subdev(s)";
		default:; 
		}
		
		return "unknown";
	}
	private void updateSettingListItem(){
		Resources resources = getResources();
		if(listItems == null){
			listItems = new ArrayList<DeviceAttrBean>();
			listItems.add(new DeviceAttrBean(resources.getString(R.string.setting_model),getDevAttrStr(DEV_MODEL),DEV_MODEL));
			listItems.add(new DeviceAttrBean(resources.getString(R.string.setting_cpu),getDevAttrStr(DEV_CPU),DEV_CPU));
			listItems.add(new DeviceAttrBean(resources.getString(R.string.setting_dsp),getDevAttrStr(DEV_DSP),DEV_DSP));
			listItems.add(new DeviceAttrBean(resources.getString(R.string.setting_sensor),getDevAttrStr(DEV_SENSOR),DEV_SENSOR));
			
			listItems.add(new DeviceAttrBean(resources.getString(R.string.setting_wifi),getDevAttrStr(DEV_WIFI),DEV_WIFI));
			listItems.add(new DeviceAttrBean(resources.getString(R.string.setting_ethernet),getDevAttrStr(DEV_NETWORK),DEV_NETWORK));
			listItems.add(new DeviceAttrBean(resources.getString(R.string.setting_3g4g),getDevAttrStr(DEV_W3G4G),DEV_W3G4G));
			listItems.add(new DeviceAttrBean(resources.getString(R.string.setting_time),getDevAttrStr(DEV_DATETIME),DEV_DATETIME));
			if(mCameraParamsBean.gpio_num>0)
				listItems.add(new DeviceAttrBean(resources.getString(R.string.hw_res),getDevAttrStr(DEV_HARDWARE_RES),DEV_HARDWARE_RES));
			
//			listItems.add(new DeviceAttrBean(resources.getString(R.string.hw_subdev),getDevAttrStr(DEV_SUB_DEV),DEV_SUB_DEV));
	
			listItems.add(new DeviceAttrBean(resources.getString(R.string.setting_user),getDevAttrStr(DEV_USER),DEV_USER));
	
			listItems.add(new DeviceAttrBean(resources.getString(R.string.setting_onvif),getDevAttrStr(DEV_ONVIF),DEV_ONVIF));
			listItems.add(new DeviceAttrBean(resources.getString(R.string.setting_gb28181),getDevAttrStr(DEV_GB28181),DEV_GB28181));
			
			listItems.add(new DeviceAttrBean(resources.getString(R.string.setting_osd),getDevAttrStr(DEV_OSD),DEV_OSD));
			listItems.add(new DeviceAttrBean(resources.getString(R.string.setting_mail),getDevAttrStr(DEV_MAIL),DEV_MAIL));
			listItems.add(new DeviceAttrBean(resources.getString(R.string.setting_ftp),getDevAttrStr(DEV_FTP),DEV_FTP));
			listItems.add(new DeviceAttrBean(resources.getString(R.string.setting_sdcard),getDevAttrStr(DEV_SDCard),DEV_SDCard));
			listItems.add(new DeviceAttrBean(resources.getString(R.string.setting_alarm),getDevAttrStr(DEV_ALARM),DEV_ALARM));
			//if (status == ContentCommon.P2P_STATUS_ON_LINE) {
				listItems.add(new DeviceAttrBean(resources.getString(R.string.setting_os),getDevAttrStr(DEV_OS),DEV_OS));
				listItems.add(new DeviceAttrBean(resources.getString(R.string.setting_kernel),getDevAttrStr(DEV_KERNEL),DEV_KERNEL));
				listItems.add(new DeviceAttrBean(resources.getString(R.string.setting_version),getDevAttrStr(DEV_OTHER_SETTING) ,DEV_OTHER_SETTING));
				//listItems.add(resources.getString(R.string.ringtone_setting));
			//}
		}else{
			for(DeviceAttrBean da:listItems){
				da.info = getDevAttrStr(da.id);
			}
			mAdapter.notifyDataSetChanged();
		}
	}
	private void findView() {
		mListView = (ListView) findViewById(R.id.settinglist);
		tvCameraName = (TextView) findViewById(R.id.tv_camera_setting);

		back = (ImageButton) findViewById(R.id.back);
		back.setOnClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long id) {
		Intent intent;
		int funcid = listItems.get(position).id;
		switch (funcid) {
		case DEV_NETWORK:{
//			intent = new Intent(this, SettingEthernetActivity.class);
//			intent.putExtra(ContentCommon.STR_CAMERA_ID, strDID);
//			intent.putExtra(ContentCommon.STR_CAMERA_NAME, cameraName);
//			startActivity(intent);
			(new Thread(new Runnable(){
				@Override
				public void run() {
					Cmds.getEthernetConfig(mServiceStub,strDID,"null");
				}
			})).start();
		}
			break;
		case DEV_W3G4G:{
//			intent = new Intent(this, SettingMobileNetworkActivity.class);
//			intent.putExtra(ContentCommon.STR_CAMERA_ID, strDID);
//			startActivity(intent);
			(new Thread(new Runnable(){
				@Override
				public void run() {
					Cmds.get3G4GConfig(mServiceStub,strDID);
				}
			})).start();
		}break;
		case DEV_WIFI:{
//			intent = new Intent(this, SettingWifiActivity.class);
//			intent.putExtra(ContentCommon.STR_CAMERA_ID, strDID);
//			intent.putExtra(ContentCommon.STR_CAMERA_NAME, cameraName);
//			startActivity(intent);
			new Thread(new Runnable(){
				@Override
				public void run() {
					Log.d(TAG,"onServiceConnected Cmds.getWifiConfig");
					Cmds.getWifiConfig(mServiceStub,strDID,"null");
				}
				
			}).start();
		}
			break;
		case DEV_USER:
			Intent in = new Intent(this,
					AddDeviceActivity.class);
			in.putExtra(ContentCommon.CAMERA_OPTION,
					ContentCommon.EDIT_CAMERA);
			in.putExtra(ContentCommon.STR_CAMERA_NAME, cameraName);
			in.putExtra(ContentCommon.STR_CAMERA_ID, strDID);
			in.putExtra(ContentCommon.STR_CAMERA_USER, user);
			in.putExtra(ContentCommon.STR_CAMERA_PWD, pwd);
			startActivity(in);
			break;

		case DEV_ALARM:
//			Intent intent2 = new Intent(this, SettingAlarmActivity.class);
//			intent2.putExtra(ContentCommon.STR_CAMERA_ID, strDID);
//			intent2.putExtra(ContentCommon.STR_CAMERA_NAME, cameraName);
//			startActivity(intent2);
			Cmds.getMoveAlarmSetting(mServiceStub,strDID);
			break;
		case DEV_DATETIME:
			intent = new Intent(this, SettingDateActivity.class);
			intent.putExtra(ContentCommon.STR_CAMERA_ID, strDID);
			intent.putExtra(ContentCommon.STR_CAMERA_NAME, cameraName);
			startActivity(intent);
//			Cmds.getDevTimeInfo(mServiceStub, strDID);
			break;
		case DEV_OSD:
//			intent = new Intent(this, SettingOSDActivity.class);
//			intent.putExtra(ContentCommon.STR_CAMERA_ID, strDID);
//			intent.putExtra(ContentCommon.STR_CAMERA_NAME, cameraName);
//			startActivity(intent);
			IPCNetGetOsdCfg_st mIPCNetGetOsdCfg_st = mJSONStructProtocal.new IPCNetGetOsdCfg_st();
			mIPCNetGetOsdCfg_st.Vich = 0;
			Cmds.getOsdCfg(mServiceStub,strDID,mIPCNetGetOsdCfg_st.toJSONString());
			break;
		 case DEV_MAIL:
//			 Intent intent4=new Intent(this,SettingMailActivity.class);
//			 intent4.putExtra(ContentCommon.STR_CAMERA_ID, strDID);
//			 intent4.putExtra(ContentCommon.STR_CAMERA_NAME, cameraName);
//			 startActivity(intent4);
			 Cmds.getDevMailCfg(mServiceStub, strDID);
		 break;
		 case DEV_FTP:
//			 Intent intent5=new Intent(this,SettingFtpActivity.class);
//			 intent5.putExtra(ContentCommon.STR_CAMERA_ID, strDID);
//			 intent5.putExtra(ContentCommon.STR_CAMERA_NAME, cameraName);
//			 startActivity(intent5);
			 Cmds.getDevFtpCfg(mServiceStub, strDID);
		 break;
		case DEV_SDCard:
//			Intent intent6 = new Intent(this, SettingSDCardActivity.class);
//			intent6.putExtra(ContentCommon.STR_CAMERA_ID, strDID);
//			intent6.putExtra(ContentCommon.STR_CAMERA_NAME, cameraName);
//			startActivity(intent6);
			new Thread(new Runnable(){
				@Override
				public void run() {
					mIPCNetRecordGetCfg_st.ViCh = 0;//mSensorIndex;
					mIPCNetRecordGetCfg_st.Path = "/mnt/s0/media/sensor0";
					Cmds.getAvRecorderConf(mServiceStub,strDID,mIPCNetRecordGetCfg_st.toJSONString());
				}
			}).start();
		break;
		case DEV_OTHER_SETTING:
//			Intent i = new Intent(this,OthersSettingActivity.class);
//			i.putExtra(ContentCommon.STR_CAMERA_NAME, cameraName);
//			i.putExtra(ContentCommon.STR_CAMERA_ID, strDID);
//			startActivity(i);
			Cmds.getGetUpgradeCfg(mServiceStub,strDID);
			break;
//		case DEV_SUB_DEV:{
//			intent = new Intent(this,
//					SettingSubdevActivity.class);
//			intent.putExtra(ContentCommon.STR_CAMERA_ID, strDID);
//			startActivityForResult(intent, 200);
//		}break;
		case DEV_HARDWARE_RES:{
			intent = new Intent(this,
					SettingGPIOActivity.class);
			intent.putExtra(ContentCommon.STR_CAMERA_ID, strDID);
			startActivityForResult(intent, 200);
		}break;
		case DEV_SENSOR:{
//			intent = new Intent(this,
//					SettingSensorActivity.class);
//			intent.putExtra(ContentCommon.STR_CAMERA_ID, strDID);
//			startActivityForResult(intent, 200);
			(new Thread(new Runnable(){
				@Override
				public void run() {
					Cmds.getPicColorInfo(mServiceStub,strDID);
					
					Cmds.getExpType(mServiceStub, strDID);
					
					Cmds.getAntlFLickerInfo(mServiceStub, strDID);
					
					Cmds.getWhBalance(mServiceStub, strDID);
					
					Cmds.getEntiNoiseInfo(mServiceStub, strDID);
					
					Cmds.getWdrInfo(mServiceStub, strDID);
				}
			})).start();
		}break;
		}

		}

	public void setBackData() {
		if (isDismiss) {
			b.putInt("backPosition", selectPosition);
			b.putString("DID", strDID);
		}
		it.putExtras(b);
		this.setResult(RESULT_CODE, it);
		Log.i("info", "back�������OK");
		finish();

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			b.putString("temp", temp);
			setBackData();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		b.putString("temp", temp);
		setBackData();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//mBridgeService.removeServiceStub(mServiceStub);
		mBridgeService.unbindSetNull(mServiceStub);
		unbindService(mConn);
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
			case ContentCommon.IPCNET_GET_DEV_INFO_RESP:{
				try {
					JSONObject jsroot = jsonData.getJSONObject("Dev.Info");
					if(jsroot!=null){
						model = jsroot.getString("Model");
						cpu = jsroot.getString("CPU");
						dsp = jsroot.getString("DSP");
						sensor = jsroot.getString("Sensor");
						wifi = jsroot.getString("Wifi");
						eth = jsroot.getString("Eth");
						mobile = jsroot.getString("Mobile");
						
						JSONObject jsdate = jsroot.getJSONObject("Date");
						JSONObject jstime = jsroot.getJSONObject("Time");
						int tz = jsroot.getInt("TimeZone");
						{
							Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/London"));
							calendar.set(jsdate.getInt("Year"), jsdate.getInt("Mon"), jsdate.getInt("Day"),jstime.getInt("Hour")
									,jstime.getInt("Min"),jstime.getInt("Sec"));
							
							SimpleDateFormat localFromat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							localFromat.setTimeZone(TimeZone.getDefault());
							localFromat.format(calendar.getTime());
							calendar = localFromat.getCalendar();
							datetime = calendar.get(Calendar.YEAR) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DAY_OF_MONTH) 
							+ "  " + (calendar.get(Calendar.HOUR_OF_DAY) + 1) + ":" + calendar.get(Calendar.MINUTE) + ":" + 
							calendar.get(Calendar.SECOND) + " " + SettingDateActivity.getTimeZoneString(tz);
						}
//						datetime = jsdate.getInt("Year") + "/" + jsdate.getInt("Mon") + "/" + jsdate.getInt("Day") + " " + 
//								jstime.getInt("Hour") + ":" + jstime.getInt("Min") + ":" + jstime.getInt("Sec") + "  " + 
//								SettingDateActivity.getTimeZoneString(tz);
						
						onvif = jsroot.getString("Onvif");
						gb28181 = jsroot.getString("GB28181");
						mail = jsroot.getString("Mail");
						ftp = jsroot.getString("FTP");
						storage = jsroot.getString("Storage");
						alarm = jsroot.getString("Alarm");
						OSver = jsroot.getString("OSver");
						Kern = jsroot.getString("Kern");
						firm = jsroot.getString("Firm");
						
						updateSettingListItem();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			case ContentCommon.IPC_NETWORK_ETH_GET_RESP:
				if(mIPCNetEthConfig_st.parseJSON(jsonData)){
//					mIPCNetEthConfig_st.IP;
//					mIPCNetEthConfig_st.DhcpEnble;
//					mIPCNetEthConfig_st.Netmask;
//					mIPCNetEthConfig_st.Getway;
//					mIPCNetEthConfig_st.DNS1;
//					mIPCNetEthConfig_st.DNS2;
//					mIPCNetEthConfig_st.DhcpEnble;
				}
				break;
			case ContentCommon.IPC_NETWORK_ETH_SET_RESP:
				break;
			case ContentCommon.IPC_NETWORK_MOBILE_GET_RESP:
				if(mIPCNetMobileNetworkInfo_st.parseJSON(jsonData)){
					//mIPCNetMobileNetworkInfo_st.enable;
					//mIPCNetMobileNetworkInfo_st.ip;
					//mIPCNetMobileNetworkInfo_st.type;
				}
				break;
			case ContentCommon.IPC_NETWORK_MOBILE_SET_RESP:
				
				break;
			case ContentCommon.IPCNET_GET_OSD_RESP:
				if(mIPCNetOsdCfg_st.parseJSON(jsonData)){
					
				}
				break;
			case ContentCommon.IPCNET_SET_OSD_RESP:
				mIPCNetOsdCfg_st.SetDefault = false;
				break;
			case ContentCommon.IPC_AV_RECO_CONF_GET_RESP:{
				JSONObject recorder = null;
				try {
					recorder = jsonData.getJSONObject("Rec.Conf");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				if(recorder!=null){
//					JSONObject diskinfo = null;
//					if(progressDialog.isShowing())
//						progressDialog.dismiss();
//					try {
//						diskinfo = recorder.getJSONObject("DiskInfo");
//						int total = diskinfo.getInt("Total");
//						tvSdTotal.setText(total + "MB");
//						int free = diskinfo.getInt("Free");
//						tvSdRemain.setText(free + "MB");
//						
//						String rootdir = diskinfo.getString("Path");
//						if(rootdir!=null){
//							mStorageRootDir = rootdir;
//							Log.d(TAG,"mStorageRootDir:" + mStorageRootDir);
//						}
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					
//					try {
//						JSONArray RecMinsOption = recorder.getJSONArray("RecMinsOption");
//						int RecMins = recorder.getInt("RecMins");
//						int count = RecMinsOption.length();
//
//						mRecorDurationListStr = new String[count];
//						for(int i=0;i<count;i++){
//							mRecorDurationListStr[i] = RecMinsOption.getInt(i) + " min";
//						}
//						if(RecMins>=count)
//							RecMins = count-1;
//						mRecordDurationListView.setData(mRecorDurationListStr);
//						mRecordDurationListView.setCurPos(RecMins);
//						mRecordDurationListView.notifyDataSetChanged();
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					
//					try {
//						mAutoDelete = recorder.getBoolean("AutoDel");
//						cbxConverage.setChecked(mAutoDelete);
//					} catch (JSONException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//					
//					try {
//						JSONArray RecordTime = recorder.getJSONArray("RecTime");
//						int count = RecordTime.length();
//						count = mScheduleList.size()<count?mScheduleList.size():count;
//						for(int i =0;i<count;i++){
//							RecorderSchedule rs = mScheduleList.get(i);
//							JSONObject duration = RecordTime.getJSONObject(i);
//							
//							rs.enable = duration.getInt("En") == 0?false:true;							
//							rs.start1 = duration.getString("St1");
//							rs.end1 = duration.getString("Ed1");
//							rs.start2 = duration.getString("St2");
//							rs.end2 = duration.getString("Ed2");
//						}
//						
//						mAdapter.notifyDataSetChanged();
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					
//					try {
//						mPackageType = recorder.getInt("PackageType");
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					try {
//						mReserveSize = recorder.getInt("ReserveSize");
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
			}break;
			case ContentCommon.IPC_AV_RECO_OP_RESP:
//				try {
//					int ret= jsonData.getInt("ret");
//					//Log.d(TAG,"JIANLE_NETWORK_WIFI_SET_RESP:" + ret);
//					if(progressDialog.isShowing()){
//						progressDialog.dismiss();
//					}
//					JSONObject rfop= jsonData.getJSONObject("rfop");
//					if(rfop!=null){
//						int op = rfop.getInt("op");
//						switch(op){
//						case ContentCommon.AV_RECO_OP_SET_RECORDER_CONFIG_RESP:
//							
//							break;
//						case ContentCommon.AV_RECO_OP_GET_RECORDER_CONFIG_RESP:
//							
//							break;
//						}
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
				break;
			case ContentCommon.IPC_AV_RECO_CONF_SET_RESP:
//				try {
//					int ret= jsonData.getInt("ret");
//					if(progressDialog.isShowing()){
//						progressDialog.dismiss();
//					}
//					if(ret == 0 || ret == ContentCommon.JIANLE_NET_RET_OK){
//						Toast.makeText(SettingSDCardActivity.this, getResources().getString(R.string.setting_ok), 3).show();
//					}else{
//						Toast.makeText(SettingSDCardActivity.this, getResources().getString(R.string.setting_failed), 3).show();
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
				break;
			case ContentCommon.IPCNET_GET_PICOLOR_RESP:{
				if(mIPCNetPicColorInfo_st.parseJSON(jsonData)){
					//update ui.
//					updatePicColorInfo(mIPCNetPicColorInfo_st);
				}
			}break;
			case ContentCommon.IPCNET_GET_EXPOSURE_TYPE_RESP:{
				if(mIPCNetExpType_st.parseJSON(jsonData)){
					//update ui
//					updateExposureType(mIPCNetExpType_st);
				}
			}break;
			
			case ContentCommon.IPCNET_GET_ANTIFLICKER_RESP:{
				if(mIPCNetAntiFlickerInfo_st.parseJSON(jsonData)){
					//update ui
//					updateAntiFlickerInfo(mIPCNetAntiFlickerInfo_st);
				}
			}break;
			
			case ContentCommon.IPCNET_GET_WH_BLANCE_RESP:{
				if(mIPCNetWhBalance_st.parseJSON(jsonData)){
					//update ui
//					updateWhBalance(mIPCNetWhBalance_st);
				}
			}break;
			
			case ContentCommon.IPCNET_GET_DENOISE_RESP:{
				if(mIPCNetDeNoiseInfo_st.parseJSON(jsonData)){
					//update ui
//					updateEntiNoiseInfo(mIPCNetDeNoiseInfo_st);
				}
			}break;
			
			case ContentCommon.IPCNET_GET_WDR_RESP:{
				if(mIPCNetWdrInfo_st.parseJSON(jsonData)){
//					updateWdrInfo(mIPCNetWdrInfo_st);
				}
			}break;
			
			case ContentCommon.IPCNET_SET_EXPOSURE_TYPE_RESP:
			case ContentCommon.IPCNET_SET_PICOLOR_RESP:
			case ContentCommon.IPCNET_SET_ANTIFLICKER_RESP:
			case ContentCommon.IPCNET_SET_DENOISE_RESP:
			case ContentCommon.IPCNET_SET_WDR_RESP:
			case ContentCommon.IPCNET_SET_WH_BLANCE_RESP:
			{
//				noticeAccordingToRet(jsonData);
			}break;
			case ContentCommon.IPC_NETWORK_WIFI_GET_RESP:
				if(mIPCNetWirelessConfig_st.parseJSON(jsonData)){
					String ssid = mIPCNetWirelessConfig_st.SSID;//wifi.getString("SSID");
					String passwd = mIPCNetWirelessConfig_st.Password;//wifi.getString("Password");
					boolean enable = mIPCNetWirelessConfig_st.WirelessEnable;//wifi.getBoolean("WirelessEnable");
					boolean dhcp = mIPCNetWirelessConfig_st.DhcpEnble;//wifi.getBoolean("DhcpEnble");
					String encrypto = mIPCNetWirelessConfig_st.EncType;//wifi.getString("EncType");
					String ip = mIPCNetWirelessConfig_st.IP;//wifi.getString("IP");
					String netmask = mIPCNetWirelessConfig_st.Netmask;//wifi.getString("Netmask");
					String gateway = mIPCNetWirelessConfig_st.Getway;//wifi.getString("Getway");
					
//					wifiBean.setSsid(ssid);
//					wifiBean.SetPasswd(passwd);
//					wifiBean.enableDHCP(dhcp);
//					wifiBean.enableWiFi(enable);
//					wifiBean.SetIP(ip);
//					wifiBean.SetNetmask(netmask);
//					wifiBean.SetGateway(gateway);
//					wifiBean.SetEncrypt(encrypto);
//					
//					tvName.setText(ssid);
//					//tvPrompt;
//					tvSafe.setText(encrypto);
//					//tvSigal;
//					editPwd.setText(passwd);
//					
//					ip_addr_et.setText(ip);
//					dhcp_cb.setChecked(dhcp);
//					wifi_enable.setChecked(enable);
//					netmask_et.setText(netmask);
//					gateway_et.setText(gateway);
//					Log.d(TAG,"JIANLE_NET_CFG_GET_REQ");
//					enableWifi(enable,false);
//					
//					enableEditControls(dhcp);
					break;
				}
				break;
			case ContentCommon.IPC_NETWORK_WIFI_SET_RESP:
//				int ret = -255;
//				try {
//					ret = jsonData.getInt("ret");
//				} catch (JSONException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
				if(mIPCNetWirelessConfig_st.parseJSON(jsonData)){					
					String ssid = mIPCNetWirelessConfig_st.SSID;
					String passwd = mIPCNetWirelessConfig_st.Password;
					boolean enable = mIPCNetWirelessConfig_st.WirelessEnable;
					boolean dhcp = mIPCNetWirelessConfig_st.DhcpEnble;
					String encrypto = mIPCNetWirelessConfig_st.EncType;
					String ip = mIPCNetWirelessConfig_st.IP;
					String netmask = mIPCNetWirelessConfig_st.Netmask;
					String gateway = mIPCNetWirelessConfig_st.Getway;
//					
//					wifiBean.setSsid(ssid);
//					wifiBean.SetPasswd(passwd);
//					wifiBean.enableDHCP(dhcp);
//					wifiBean.enableWiFi(enable);
//					wifiBean.SetIP(ip);
//					wifiBean.SetNetmask(netmask);
//					wifiBean.SetGateway(gateway);
//					wifiBean.SetEncrypt(encrypto);
//					
//					tvName.setText(ssid);
//					//tvPrompt;
//					tvSafe.setText(encrypto);
//					//tvSigal;
//					editPwd.setText(passwd);
//					
//					ip_addr_et.setText(ip);
//					dhcp_cb.setChecked(dhcp);
//					wifi_enable.setChecked(enable);
//					netmask_et.setText(netmask);
//					gateway_et.setText(gateway);
//					Log.d(TAG,"JIANLE_NETWORK_WIFI_SET_RESP");
//					enableWifi(enable,false);
//					
//					enableEditControls(dhcp);
//					break;
				}
				break;
			case ContentCommon.IPC_NETWORK_WIFI_SEARCH_GET_RESP:
//				if(mIPCNetWifiAplist.parseJSON(jsonData)){
//					if(mIPCNetWifiAplist.ApItem!=null){
//						for(IPCNetWifiApItem it:mIPCNetWifiAplist.ApItem){
//							WifiScanBean bean=new WifiScanBean();
//							bean.setDid(strDID);
//							bean.setSsid(it.SSID);
//							bean.setSecurity(it.EncType);
//							bean.setDbm0(it.RSSI);
//							mAdapter.addWifiScan(bean);
//						}
//					}
//					mHandler.sendEmptyMessage(SCANPARAMS);
//				}
				break;
			case ContentCommon.IPCNET_UPGRADE_CFG_RESP:
				if(mIPCNetUpgradeCfg_st.parseJSON(jsonData)){
//					new Thread() {
//						public void run() {
//							String url = mIPCNetUpgradeCfg_st.UpgradeUrl + mIPCNetUpgradeCfg_st.SystemType + "msg.json";
//							if(getFirmwareVersion(url,FT_SYSTEM)){
//								url = mIPCNetUpgradeCfg_st.UpgradeUrl + mIPCNetUpgradeCfg_st.CustomType + "msg.json";
//								if(getFirmwareVersion(url,FT_CUSTOM)){
//									url = mIPCNetUpgradeCfg_st.UpgradeUrl + mIPCNetUpgradeCfg_st.VendorType + "msg.json";
//									if(getFirmwareVersion(url,FT_VENDOR)){
//										//update ui.
//										Message msgs = new Message();
//										msgs.obj = mDeviceCustomFirmwareUpateInfo.Direct[mDeviceCustomFirmwareUpateInfo.Direct.length-1].Version
//												+ "." + mDeviceVendorFirmwareUpateInfo.Direct[mDeviceVendorFirmwareUpateInfo.Direct.length-1].Version 
//												+ "." + mDeviceSystemFirmwareUpateInfo.Direct[mDeviceSystemFirmwareUpateInfo.Direct.length-1].Version;
//										sysVerhander.sendMessage(msgs);
//									}
//								}
//							}
//						}
//					}.start();
				}
				break;
			case ContentCommon.IPCNET_UPGRADE_AUTO_SET_RESP:{
				try {
					int ret = jsonData.getInt("ret");
//					if(ret == 100 || ret == 0){
//						Toast.makeText(OthersSettingActivity.this, getResources().getString(R.string.setting_ok), Toast.LENGTH_LONG).show();
//					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}break;
			case ContentCommon.IPC_UPGRADE_RESP:
				try {
					int ret = jsonData.getInt("ret");
					
					switch(ret){
					case ContentCommon.IPC_NET_RET_UPDATING:{
						int progress = jsonData.getInt("p");
//						update_progress.setProgress(progress);
//						
//						setting_upgrade_progress_tv.setText(getResources().getString(R.string.setting_upgrade_progress) + " " + progress + "%");
//						if(progress >= 100){
//							mDeviceData.isUpdateing = false;
//							Toast.makeText(OthersSettingActivity.this, getResources().getString(R.string.rebooting_now), Toast.LENGTH_LONG).show();
//							update_progress.setVisibility(View.GONE);
//						}
					}break;
					case 100:
					case 0:
//						if(update_progress_layout.getVisibility()!=View.VISIBLE){
//							update_progress_layout.setVisibility(View.VISIBLE);
//							setting_upgrade_progress_tv.setText(getResources().getString(R.string.setting_upgrade_progress) + " 0%");
//							Toast.makeText(OthersSettingActivity.this, getResources().getString(R.string.others_updateing), Toast.LENGTH_LONG).show();
//						}
						break;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case ContentCommon.IPC_MOVE_ALARM_SET_RESP:
//				showToast(R.string.eth_setparams_ok);
				break;
			case ContentCommon.IPC_MOVE_ALARM_GET_RESP:
				mIPCNETMoveAlarmCfg.parseJSON(jsonData);
				
//				updateDataToUI();
				break;
			case ContentCommon.IPCNET_GET_EMAIL_CFG_RESP:
				jsIPCNetEmailCfg_st.parseJSON(jsonData);
				
//				editSender.setText(jsIPCNetEmailCfg_st.SmtpSender);
//				editSmtpServer.setText(jsIPCNetEmailCfg_st.SmtpServer);
//				editTile.setText(jsIPCNetEmailCfg_st.MailTitle);
//				editContent.setText(jsIPCNetEmailCfg_st.MailContext);
//				editSmtpPort.setText(jsIPCNetEmailCfg_st.SmtpPort + "");
//				editSmtpUser.setText(jsIPCNetEmailCfg_st.SmtpUser);
//				editSmtpPwd.setText(jsIPCNetEmailCfg_st.SmtpPasswd);
//				cbxCheck.setChecked(jsIPCNetEmailCfg_st.SmtpUser.length()>0 && jsIPCNetEmailCfg_st.SmtpPasswd.length()>0);
//				editReceiver1.setText(jsIPCNetEmailCfg_st.SmtpReceiver1);
//				editReceiver2.setText(jsIPCNetEmailCfg_st.SmtpReceiver2);
//				editReceiver3.setText(jsIPCNetEmailCfg_st.SmtpReceiver3);
//				editReceiver4.setText(jsIPCNetEmailCfg_st.SmtpReceiver4);
//				
//				switch(jsIPCNetEmailCfg_st.EncType){
//				case 0:
//					editSSL.setText("NONE");
//					break;
//				case 1:
//					editSSL.setText("SSL");
//					break;
//				case 2:
//					editSSL.setText("TSL");
//					break;
//				}
				break;
			case ContentCommon.IPCNET_SET_EMAIL_CFG_RESP:
				try {
					int ret = jsonData.getInt("ret");
//					if(ret == 0){
//						showToast(R.string.mail_setting_success);
//					}else{
//						showToast(R.string.mail_setting_failed);
//					}
				}catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case ContentCommon.IPCNET_GET_FTP_CFG_RESP:
				jsIPCNetFtpCfg_st.parseJSON(jsonData);
				
//				editFtpServer.setText(jsIPCNetFtpCfg_st.FtpAddr);
//	            editFtpPort.setText(jsIPCNetFtpCfg_st.FtpPort + "");
//	            editFtpUser.setText(jsIPCNetFtpCfg_st.FtpUser);
//	            editFtpPwd.setText(jsIPCNetFtpCfg_st.FtpPasswd);
//	            editFtpUploadFolder.setText(jsIPCNetFtpCfg_st.FtpPath);
				break;
			case ContentCommon.IPCNET_SET_FTP_CFG_RESP:
				try {
					int ret = jsonData.getInt("ret");
//					if(ret == 0){
//						showToast(R.string.ftp_setting_success);
//					}else{
//						showToast(R.string.ftp_setting_failed);
//					}
				}catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case ContentCommon.IPCNET_GET_TIME_RESP:
				if(mIPCNetTimeCfg_st.parseJSON(jsonData)){
//					dateBean = new DateBean();
//					dateBean.ntpEnable = mIPCNetTimeCfg_st.NtpEnable;//jsroot.getBoolean("NtpEnable");
//					dateBean.ntpSer = mIPCNetTimeCfg_st.NtpServ;//jsroot.getString("NtpServ");
//					dateBean.timeZone = mIPCNetTimeCfg_st.TimeZone;//jsroot.getInt("TimeZone");
//					
//					dateBean.year = mIPCNetTimeCfg_st.Date.Year;//jsdate.getInt("Year");
//					dateBean.mon = mIPCNetTimeCfg_st.Date.Mon;//jsdate.getInt("Mon");
//					dateBean.day = mIPCNetTimeCfg_st.Date.Day;//jsdate.getInt("Day");
//					
//					dateBean.hour = mIPCNetTimeCfg_st.Time.Hour;//jstime.getInt("Hour");
//					dateBean.min = mIPCNetTimeCfg_st.Time.Min;//jstime.getInt("Min");
//					dateBean.sec = mIPCNetTimeCfg_st.Time.Sec;//jstime.getInt("Sec");
//					
//					Log.d(TAG,"ntpEnable:" + dateBean.ntpEnable + " ntpSer:" + dateBean.ntpSer + " timeZone:" + dateBean.timeZone + " " + 
//							dateBean.year + "-" + dateBean.mon + "-" + dateBean.day + " " + dateBean.hour + ":" + dateBean.min + ":" + dateBean.sec);
				}
				break;
			case ContentCommon.IPCNET_SET_TIME_RESP:
				try {
					int ret = jsonData.getInt("ret");
					if(ret == 0){
//						showToast(R.string.date_setting_success);
//						setTimeZone();
					}else{
//						showToast(R.string.date_setting_failed);
					}
				}catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG,"requestCode:" + requestCode + " resultCode:" + resultCode);
		if(requestCode == 200){
			if(resultCode == 1){
				updateSettingListItem();
				mAdapter.notifyDataSetChanged();
			}
		}
	}
	
	public class DeviceAttrBean{
		public DeviceAttrBean(String name,String info,int id){
			this.name = name;
			this.info = info;
			this.id = id;
		}
		public String name;
		public String info;
		public int id;
	}
}