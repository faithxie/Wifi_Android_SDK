	package freesbell.demo.client;
	
	import java.util.ArrayList;
import java.util.Timer;

import org.json.JSONException;
import org.json.JSONObject;

import freesbell.demo.client.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import freesbell.demo.adapter.HardwareResListAdapter;
import freesbell.demo.bean.CameraParamsBean;
import freesbell.demo.bean.HwResBean;
import freesbell.demo.bean.JSONStructProtocal;
import freesbell.demo.bean.JSONStructProtocal.IPCNetGpioInfo_st;
import freesbell.demo.client.BridgeService.ControllerBinder;
import freesbell.demo.content.ContentCommon;
import freesbell.demo.utils.Cmds;
import freesbell.demo.utils.ServiceStub;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;
	
	public class SettingGPIOActivity extends Activity implements OnClickListener, OnCheckedChangeListener,OnGroupClickListener{
	private String TAG="SettingGPIOActivity";
	private static SettingGPIOActivity mSelf;
	private String strDID;
	private String cameraName;
	private boolean changeWifiFlag=false;//�ж��Ƿ���wifi
	private boolean successFlag=false;//��ȡ�����õĽ��
	private static final int INITTIMEOUT = 9000 ;
	private final int END=1;//wifi scan end flag
	private int result;
	private final int WIFIPARAMS=1;//getwifi params
	private final int SCANPARAMS=2;//scan wifi params
	private final int OVER=3;//set wifi over
	private final int TIMEOUT=4;
	private int CAMERAPARAM=0xffffffff;//����״̬

	private CameraParamsBean mCameraParamsBean;
	private Timer mTimerTimeOut;
	private boolean isTimerOver=false;
	private int mCurRequestingHwRes = 0;
//	private ImageView imgDropDown;
	private Button btnOk;
	private ImageButton btnCancel;

	private PopupWindow popupWindow;
//	private EditText ip_addr_et;
	private HardwareResListAdapter mAdapter;
	private ArrayList<HwResBean> list;
	private JSONStructProtocal mJSONStructProtocal = new JSONStructProtocal();
	private IPCNetGpioInfo_st mIPCNetGpioInfo_st = mJSONStructProtocal.new IPCNetGpioInfo_st();
	private ProgressDialog scanDialog;
//	private View signalView;
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
					HwResBean hrb = mCameraParamsBean.getHwRes(0);
					if(hrb==null||mCameraParamsBean.getHwResListNum()!=mCameraParamsBean.getHwResNum()){
						Log.d(TAG,"mDeviceData.getHwRes(0) null");
						
						Cmds.getGPIO(mServiceStub,strDID,mCurRequestingHwRes);
					}else{
					Log.d(TAG,"mDeviceData.getHwRes(0) --" + hrb.index);
					}
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getDataFromOther();
		requestWindowFeature(Window.FEATURE_NO_TITLE);   
		setContentView(R.layout.setting_gpio);
		mSelf = this;
		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(getString(R.string.eth_getparams));

		mAdapter = new HardwareResListAdapter(this,list);
		findView();
		setListener();
		Intent intent=new Intent(this,BridgeService.class);
		bindService(intent, mConn, Context.BIND_AUTO_CREATE);
	}
	/**
	 * wifi getParams and Scaned
	 * 
	 * **/
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case TIMEOUT:
				if(scanDialog.isShowing()){
					scanDialog.cancel();
				}
				//showToast(R.string.wifi_scan_failed);
				if(progressDialog!=null&&progressDialog.isShowing()){
					progressDialog.dismiss();
				}
				break;
			
			default:
				break;
			}
		}
	};
	
	/**
	 * Listitem click 
	 * **/
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
		}
	};
	private ProgressDialog progressDialog;
	
	
	private Runnable runnable=new Runnable() {
		
		@Override
		public void run() {
			if(!successFlag){
				if(progressDialog.isShowing()){
					progressDialog.dismiss();
				}
			
				//showToast(R.string.wifi_getparams_failed);
			}
		}
	};
	
	public static HwResBean getHwResBean(int index){
		if(mSelf==null || index<0 || index>=mSelf.list.size()){
			return null;
		}
		return mSelf.list.get(index);
	}
	
	@Override
	protected void onPause() {
		overridePendingTransition(R.anim.out_to_right, R.anim.in_from_left);
		super.onPause();
	}
	private void getDataFromOther() {
		Intent intent = getIntent();
		strDID = intent.getStringExtra(ContentCommon.STR_CAMERA_ID);
		cameraName=intent.getStringExtra(ContentCommon.STR_CAMERA_NAME);
		mCameraParamsBean = BridgeService.getCameraBean(strDID);
		list = new ArrayList<HwResBean>();
		if(mCameraParamsBean.getHwResByOrder(0)!=null){
			for(int i=0;i<mCameraParamsBean.getHwResNum();i++){
				HwResBean hrb = mCameraParamsBean.getHwResByOrder(i);
				if(hrb!=null){
					list.add(hrb);
				}
			}
		}
//		else{
//			for(int i=0;i<mDeviceData.getHwResNum();i++){
//				HwResBean hrb = new HwResBean();
//				hrb.index = i;
//				hrb.mode = 0;
//				hrb.value = 0;
//				hrb.trigger = 1;
//				hrb.name = "gpio" + i;
//				list.add(hrb);
//				mDeviceData.addHwRes(hrb);
//			}
//		}
	}
	
	private void setListener() {
		btnOk.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
//		dhcp_cb.setOnCheckedChangeListener(this);
//		save_configuration.setOnClickListener(this);
		resource_lv.setAdapter(mAdapter);
		resource_lv.setOnGroupClickListener(this);
		mAdapter.setOnClickListener(this);
		resource_lv.setGroupIndicator(null);
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

	ExpandableListView resource_lv;
	private void findView() {
		btnOk = (Button)findViewById(R.id.btn_ok);
		btnCancel = (ImageButton)findViewById(R.id.back);
		resource_lv = (ExpandableListView)findViewById(R.id.resource_lv);
	}
	@Override
		public boolean onTouchEvent(MotionEvent event) {
			if(popupWindow!=null&&popupWindow.isShowing()){
				popupWindow.dismiss();
			}
		
		return super.onTouchEvent(event);
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
			case ContentCommon.IPC_GPIO_GET_RESP:
//				try {
					if(mIPCNetGpioInfo_st.parseJSON(jsonData)){
//					JSONObject gpio= jsonData.getJSONObject("GPIO");
//					if(gpio!=null){
						int index = mIPCNetGpioInfo_st.Index;//gpio.getInt("Index");
						HwResBean hrb = null;//list.get(gpio.getInt("Index"));
						for(HwResBean h:list){
							if(h.index == index){
								hrb = h;
								break;
							}
						}
						
						if(hrb==null){
							hrb = new HwResBean();
							hrb.index = index;
							list.add(hrb);
							mCameraParamsBean.addHwRes(hrb);
						}
						
						hrb.value = mIPCNetGpioInfo_st.Val;//gpio.getInt("Val");
						hrb.mode = mIPCNetGpioInfo_st.Mode;//gpio.getInt("Mode");
						hrb.trigger = mIPCNetGpioInfo_st.Tr;//gpio.getInt("Tr");
						hrb.gate = mIPCNetGpioInfo_st.Gate;//gpio.getInt("Gate");
						
						if(++mCurRequestingHwRes<mCameraParamsBean.getHwResNum()){
							//String jsonstr = "{\"GPIO\":{\"Index\":" + mCurRequestingHwRes + "}}";
							Cmds.getGPIO(mServiceStub,strDID,mCurRequestingHwRes);
						}
						mAdapter.notifyDataSetChanged();
					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
				break;
			}
		}
	};
	
	
	@Override
	public void onClick(View v) {
	  switch (v.getId()) {
//	  case R.id.save_configuration:
//		  String json = null;
//		  progressDialog.setMessage(getString(R.string.eth_setparams));
//		  progressDialog.show();
//		  mHandler.postDelayed(runnable,INITTIMEOUT);
//		  
//		  break;
	case R.id.back:
		finish();
		break;
	case R.id.hw_res_setting:{
		HwResBean hrb = (HwResBean) v.getTag();
		Log.d(TAG,"HwResBean:" + hrb.index);
		Intent in = new Intent(this,
				HardwareResourceSettingActivity.class);
		in.putExtra(ContentCommon.STR_CAMERA_ID, strDID);
		in.putExtra(ContentCommon.STR_HW_ID, hrb.index);
		startActivityForResult(in,100);
	}break;
//	case R.id.hw_res_value:{
//		HwResBean hrb = (HwResBean) v.getTag();
//		mAdapter.getEditTextFocus(hrb);
//	}break;
	default:
		break;
	}
	}

	 private Runnable settingRunnable=new Runnable() {
			
			@Override
			public void run() {
				if(!successFlag){
					showToast(R.string.wifi_set_failed);
				}
			}
		};

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//		enableEditControls(isChecked);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		mBridgeService.removeServiceStub(mServiceStub);
		mBridgeService.unbindSetNull(mServiceStub);
		unbindService(mConn);
		mSelf = null;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100) {
			if (resultCode == 1) {
				mAdapter.notifyDataSetChanged();
			}
		}
	}
	
	public void showToast(String content){
		Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
	}
	public void showToast(int rid){
		Toast.makeText(this, getResources().getString(rid), 3).show();
	}
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//		HwResBean hrb = (HwResBean) mAdapter.getGroup(position);
//		//do some work here.
//		hrb.opHw();
//		//
//		mAdapter.setHwImg(position);
//	}
	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		HwResBean hrb = (HwResBean) mAdapter.getGroup(groupPosition);
		//do some work here.
		hrb.opHw(mServiceStub,strDID);
		mAdapter.setHwImg(groupPosition);
		
		return false;
	}
	
	}
