package freesbell.demo.client;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import freesbell.demo.client.R;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import freesbell.demo.bean.CameraParamsBean;
import freesbell.demo.bean.HwResBean;
import freesbell.demo.client.BridgeService.ControllerBinder;
import freesbell.demo.client.ComboBox.ListViewItemClickListener;
import freesbell.demo.content.ContentCommon;
import freesbell.demo.utils.Cmds;
import freesbell.demo.utils.ServiceStub;

public class HardwareResourceSettingActivity extends Activity implements OnClickListener{
	private final String TAG = "HardwareResourceSettingActivity";
	private String mUUID;
	private int mHwId;
	private TextView mTitle;
	private Button mSaveSetting;
	private ImageButton mExit;
	private CameraParamsBean mCameraParamsBean;
	private ComboBox mModeType,mTrigger;
	private EditText mHwValue ,mGateVaule;
	private HwResBean mHwResBean;
//	private DeviceData mDeviceData;
	private String[] mModeTypeList;
	private String[] mTriggerTypeList;
	private int mCurSelModeType,mCurSelTriggerType;
	private BridgeService mBridgeService;
	
	private ServiceConnection mConn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
//			mBridgeService.removeServiceStub(mServiceStub);
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			ControllerBinder myBinder = (ControllerBinder) service;
			mBridgeService = myBinder.getBridgeService();
			mBridgeService.setServiceStub(mServiceStub);
			(new Thread(new Runnable(){
				@Override
				public void run() {
//					Cmds.getGPIOConfig(mServiceStub,strDID,"null");
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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.hw_res_setting);
		getDataFromOther();
		
		findView();
		setListener();
		initViewData();

		Intent intent=new Intent(this,BridgeService.class);
		bindService(intent, mConn, Context.BIND_AUTO_CREATE);
	}
	
	private void getDataFromOther() {
		Intent intent = getIntent();
		mUUID = intent.getStringExtra(ContentCommon.STR_CAMERA_ID);
		mCameraParamsBean = BridgeService.getCameraBean(mUUID);
		mHwId = intent.getIntExtra(ContentCommon.STR_HW_ID,-1);
		if(mHwId<0){
			finish();
		}
		mHwResBean = mCameraParamsBean.getHwRes(mHwId);
		if(mHwResBean==null){
			finish();
		}
		
		mModeTypeList = new String[5];
		mModeTypeList[0] = this.getResources().getString(R.string.hw_res_out);
		mModeTypeList[1] = this.getResources().getString(R.string.hw_res_in);
		mModeTypeList[2] = this.getResources().getString(R.string.hw_res_adc);
		mModeTypeList[3] = this.getResources().getString(R.string.hw_res_dac);
		mModeTypeList[4] = this.getResources().getString(R.string.hw_res_pwm);
		mTriggerTypeList = new String[4];
		mTriggerTypeList[0] = getResources().getString(R.string.trigger_high);
		mTriggerTypeList[1] = getResources().getString(R.string.trigger_low);
		mTriggerTypeList[2] = getResources().getString(R.string.trigger_rise_edge);
		mTriggerTypeList[3] = getResources().getString(R.string.trigger_fall_edge);
	}

	private void findView() {
		mExit = (ImageButton)findViewById(R.id.back);
		mTitle = (TextView) findViewById(R.id.hw_res_setting_title);
		mSaveSetting = (Button) findViewById(R.id.btn_save);
		mModeType = (ComboBox) findViewById(R.id.hw_res_mode_type);
		mHwValue = (EditText) findViewById(R.id.hw_res_value);
		mTrigger = (ComboBox) findViewById(R.id.hw_res_trigger_mode);
		mGateVaule = (EditText) findViewById(R.id.hw_res_gate_value);
	}
	private void setListener() {
		mExit.setOnClickListener(this);
		mSaveSetting.setOnClickListener(this);
		mModeType.setListViewOnClickListener(new ModeTypeItemClickListener());
		mTrigger.setListViewOnClickListener(new TriggerTypeItemClickListener());
	}

	private void initViewData(){
		mTitle.setText(mHwResBean.name);
		mModeType.setData(mModeTypeList);
		mTrigger.setData(mTriggerTypeList);
		mHwValue.setText(mHwResBean.value + "");
		mModeType.setCurPos(mHwResBean.mode);
		mTrigger.setCurPos(mHwResBean.trigger);
		mGateVaule.setText(mHwResBean.gate + "");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();

//		mBridgeService.removeServiceStub(mServiceStub);
		mBridgeService.unbindSetNull(mServiceStub);
		unbindService(mConn);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		  case R.id.back:
			  finish();
			  break;
		  case R.id.btn_save:
			  mHwResBean.mode = mCurSelModeType;
			  String values = mHwValue.getText().toString();
			  mHwResBean.value = Integer.parseInt(values.length()>0?values:"-1");
			  mHwResBean.trigger = mCurSelTriggerType;
			  String gates = mGateVaule.getText().toString();
			  mHwResBean.gate = Integer.parseInt(gates.length()>0?gates:"-1");
			  //send to device.
			  Cmds.setGPIO(mServiceStub,mUUID,mHwResBean);
			  
			  setResult(1);
			  finish();
			  break;
		}
	}

//	private void getGPIOSetting(int index){
//		String json;
//		JSONObject gpioSettingWrapper = new JSONObject();
//		try {
//			JSONObject gpioSetting = new JSONObject();
//			gpioSetting.put("Index", index);
//			gpioSettingWrapper.put("GPIO", gpioSetting);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		
//		json = gpioSettingWrapper.toString();
//		Cmds.getGPIO(mServiceStub,mUUID,json);
//	}
	
	private class ModeTypeItemClickListener implements ListViewItemClickListener {

		@Override
		public void onItemClick(int position) {
			mCurSelModeType = position;
		}
	}
	private class TriggerTypeItemClickListener implements ListViewItemClickListener {

		@Override
		public void onItemClick(int position) {
			mCurSelTriggerType = position;
		}
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
			case ContentCommon.IPC_GPIO_SET_RESP:
//				if(progressDialog!=null&&progressDialog.isShowing()){
//					progressDialog.dismiss();
//				}
//				
				try {
					int ret = jsonData.getInt("ret");
//					
//						if(ret == ContentCommon.JIANLE_NET_RET_NEED_RESTART_APP){
//							showToast(R.string.eth_setparams_ok_and_restart_device);
//						}else// if(ret == ContentCommon.JIANLE_NET_RET_OK)
//						{
//							showToast(R.string.eth_setparams_ok);
//						}
////						else if(ret == ContentCommon.JIANLE_NET_RET_REQ_ILLEGAL){
////							showToast(R.string.eth_setparams_ok);
////						}
//						return;
//					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				
				break;
			}
		}
	};
}
