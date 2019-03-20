/*
 * Simple Config Demo
 *
 * Copyright (c) 2014 Realtek (lynn_pu@realsil.com.cn)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 **/

package com.realtek.simpleconfig;

import com.exception.uncaughthandler.ExceptionHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.realtek.simpleconfiglib.SCLibrary;
//import com.zxing.activity.CaptureActivity;





import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.Process;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import freesbell.demo.client.R;
import android.widget.AdapterView.OnItemClickListener;

public class SmartLinkActivity extends Activity {
	private static final String TAG = "MainActivity";

	/** ViewPager */
	private ViewPager SCViewPager;
	private LayoutInflater SCInflater;
	private List<View> listViews;
	private View sc_config_iface, sc_control_iface;
	private int CurrentView = 0;

	/** For Config Page */
	private Button scan_wifi;
	private Button start_config;
	private Button pin_enable;
	private ListView wifiListView;
	private BaseAdapter wifiListItemAdapter;
	private List<HashMap<String, Object>> wifiArrayList = new ArrayList<HashMap<String, Object>>();
	private List<ScanResult> mScanResults;
	private ScanResult mScanResult;
	private ProgressDialog cfgProgressDialog;
	private AlertDialog CfgResultDialog;
	private boolean ResultShowable = true;
	private boolean RenamingOfConfig = false;

	/** For Control Page */
	private Button discov_devs;
	private ListView devListView;
	private BaseAdapter devListItemAdapter;
	private List<HashMap<String, Object>> DevInfo;
	private List<HashMap<String, Object>> devArrayList = new ArrayList<HashMap<String, Object>>();
	private List<String> pinArrayList = new ArrayList<String>();
	private ProgressDialog discovDialog;
	private ProgressDialog delProfDialog;
	private ProgressDialog renameDevDialog;
	private AlertDialog CtlMenuDialog;
	private boolean DelProfFirstShow = true;
	private boolean RenameDevFirstShow = true;
	private String CurrentControlDev;
	private String CurrentControlIP;
	private boolean DiscovEnable = false;
	private int CurrentItem;
	private boolean CtlUsePin = true; // Default Use PIN

	private String PINGet;
	private String PINSet;
	private boolean PINEnabled = false;
	private EditText PINEditText;
	private ImageButton QRCodeScan;
	private String QRCodeScanResult;
	private boolean WifiConnecting = false;
	private boolean WifiConnected = false;
	private boolean WifiDisconnected = false;
	private String WifiConnectStat = new String();
	private String ReNameStr;
	private boolean needReconnectWhenWifiChanged = false;

	private EditText wifi_ssid_et,wifi_pwd_et;
	private CheckBox wifi_pwd_show_chb,wifi_show_more_chb;
	
	private long startConfigTime = 0;
	private long currentTime = 0;
	private long timeElasped = 0;
	private boolean configFinished = true;

	private SCLibrary SCLib = new SCLibrary();
	private FileOps fileOps = new FileOps();

	private boolean isActivityAlive;

	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		// ע��δ�����쳣������
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		/* just for test!!!
		Button button = null;
		button.setVisibility(View.VISIBLE);
		*/
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	/** For Multiple Page */
        setContentView(R.layout.sc_view_pager);
        SCViewPager = (ViewPager)findViewById(R.id.viewPagerLayout);
        SCInflater = LayoutInflater.from(this);
//        sc_config_iface = SCInflater.inflate(R.layout.sc_config_iface, null);
//        sc_control_iface = SCInflater.inflate(R.layout.sc_control_iface, null);
        ViewGroup parent = (ViewGroup)findViewById(R.id.sc_view_container);
        sc_config_iface = SCInflater.inflate(R.layout.sc_config_iface, parent, false);
        sc_control_iface = SCInflater.inflate(R.layout.sc_control_iface, parent, false);
		listViews=new ArrayList<View>();
		listViews.add(sc_config_iface);
		listViews.add(sc_control_iface);
		SCViewPager.setCurrentItem(0);
		SCViewPager.setAdapter(new PageAdpt());
		SCViewPager.setOnPageChangeListener(PageChangeEvent);

    	/** For Config Page */
        scan_wifi = (Button)sc_config_iface.findViewById(R.id.scan_wifi);
        pin_enable = (Button)sc_config_iface.findViewById(R.id.pin_enable);
        start_config = (Button)sc_config_iface.findViewById(R.id.start_config);
        
        wifi_ssid_et = (EditText) sc_config_iface.findViewById(R.id.wifi_ssid_et);
        wifi_pwd_et = (EditText) sc_config_iface.findViewById(R.id.wifi_pwd_et);
        wifi_pwd_show_chb = (CheckBox)sc_config_iface.findViewById(R.id.wifi_pwd_show_chb);
        wifi_pwd_show_chb.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if(isChecked){
					wifi_pwd_et.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD); 
				}else{
					wifi_pwd_et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); 
				}
			}
        	
        });
        wifi_show_more_chb = (CheckBox)sc_config_iface.findViewById(R.id.wifi_show_more_chb);
        wifi_show_more_chb.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if(isChecked){
					scan_wifi.setVisibility(View.VISIBLE);
					pin_enable.setVisibility(View.VISIBLE);
					wifiListView.setVisibility(View.VISIBLE);
				}else{
					scan_wifi.setVisibility(View.GONE);
					pin_enable.setVisibility(View.GONE);
					wifiListView.setVisibility(View.GONE);
				}
			}
        	
        });
        scan_wifi.setTextSize(20);
        pin_enable.setTextSize(20);
        start_config.setTextSize(20);
        /* Ϊ  Config Button ��ӵ���¼� */
        scan_wifi.setOnClickListener(new ButtonListener());
    	pin_enable.setOnClickListener(new ButtonListener());
        start_config.setOnClickListener(new ButtonListener());
    	/* Ϊ  Config ListView ��Ŀ��ӵ���¼� */
        wifiListView = (ListView)sc_config_iface.findViewById(R.id.all_wifi_network);
        wifiListView.setOnItemClickListener(wifiItemOnClick);
    	/* Config ListView ������ */
        wifiListItemAdapter = new listBaseAdapter(this, wifiArrayList);
    	/* ���ý���� */
		cfgProgressDialog = new ProgressDialog(this);

    	/** For Control Page */
		discov_devs = (Button)sc_control_iface.findViewById(R.id.discov_devs);
		discov_devs.setTextSize(20);
        /* Ϊ  Control Button ��ӵ���¼� */
        discov_devs.setOnClickListener(new ButtonListener());
    	/* Ϊ  Control ListView ��Ŀ��ӵ���¼� */
        devListView = (ListView)sc_control_iface.findViewById(R.id.all_connected_devs);
        devListView.setOnItemClickListener(devItemOnClick);
    	/* Control ListView ������ */
        devListItemAdapter = new listBaseAdapter(this, devArrayList);
    	/* ���ƽ���� */
    	discovDialog = new ProgressDialog(this);
    	delProfDialog = new ProgressDialog(this);
    	renameDevDialog = new ProgressDialog(this);

    	mWifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
    	WifiInfo info = mWifiManager.getConnectionInfo();
    	if(info!=null){
    		String ssid = info.getSSID();
    		if(ssid!=null){
    			if(ssid.charAt(0) == '"')
    				wifi_ssid_et.setText(ssid.subSequence(1, ssid.length()-1));
    			else
    			wifi_ssid_et.setText(ssid);
    		}
    	}
    	
    	/** Simple Config ��ʼ�� */
		SCLib.rtk_sc_init();
		SCLib.TreadMsgHandler = new MsgHandler();

    	/** ���������ʼ�� */
        SCLib.WifiInit(this);        

    	fileOps.SetKey(SCLib.WifiGetMacStr());
    	fileOps.UpgradeSsidPasswdFile();

    	Log.i(TAG, "Build.MANUFACTURER: " + Build.MANUFACTURER);
    	Log.i(TAG, "Build.MODEL: " + Build.MODEL);
	}

	private WifiManager mWifiManager = null;

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onRestart() {
		super.onRestart();
	}

	@Override
	public void onResume() {
		final IntentFilter filter = new IntentFilter();

        if(SCLib.WifiStatus() != WifiManager.WIFI_STATE_ENABLED) {
//			Toast.makeText(SCTest.this, "Wi-Fi is not enabled", Toast.LENGTH_SHORT).show();
        	OpenWifiPopUp(); //������WIFI
        } else {
//			Toast.makeText(SCTest.this, "Wi-Fi is enabled", Toast.LENGTH_SHORT).show();
			SCLib.WifiStartScan();
	    	GetAllWifiList(); //����������ʾ
        }

		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		registerReceiver(mReceiver, filter);

		isActivityAlive = true;
		super.onResume();
	}

	@Override
	public void onPause() {
//		SCParams.ConnectedSSID = null;
//		SCParams.ConnectedBSSID = null;
		unregisterReceiver(mReceiver);
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
    }

	@Override
	public void onDestroy() {
		super.onDestroy();
		SCLib.rtk_sc_exit();
		SCParams.ConnectedSSID = null;
		SCParams.ConnectedBSSID = null;
		SCParams.ConnectedPasswd = null;
		SCParams.PasswdText = null;
    	DiscovEnable = false;
    	isActivityAlive = false;
    }

	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//		 	builder.setTitle("Warning: ")
//		 	.setMessage("Are you sure to exit this program?")
//		 	.setIcon(android.R.drawable.ic_dialog_info)
//			.setCancelable(false)
//		 	.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//		 		public void onClick(DialogInterface dialog, int id) {
//		 			dialog.dismiss();
//		 			finish();
//	           }
//		 	})
//		 	.setNegativeButton("No", new DialogInterface.OnClickListener() {
//		 		public void onClick(DialogInterface dialog, int id) {
//		 			dialog.dismiss();
//	           }
//		 	});
//		 	builder.show();
		}

		return super.onKeyDown(keyCode, event);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			return true;
		case R.id.action_settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			try {
				this.startActivity(intent);
			} catch (ActivityNotFoundException e) {
				Log.e(TAG, "Got ActivityNotFoundException");
			}
			return true;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
////		getSupportMenuInflater().inflate(R.menu.application, menu);
//		 getMenuInflater().inflate(R.menu.application, menu);
//		return true;
//	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
//			Log.d(TAG, "======> getAction(): " + intent.getAction());
			if(intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){
				boolean isCurrentConnectBSSID = false;
				NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
				WifiConnectStat = info.getState().toString();
//				Log.d(TAG, "WifiConnectStat: " + WifiConnectStat);

				String getBSSID = SCLib.getConnectedWifiBSSID();
//				if(getBSSID == null) {
//					Log.e(TAG, "Get BSSID Error");
//					return;
//				}
//				Log.d(TAG, "Connected BSSID: " + getBSSID);
//				Log.d(TAG, "Clicked SSID: " + SCParams.ConnectedSSID);
//				Log.d(TAG, "Clicked BSSID: " + SCParams.ConnectedBSSID);

				/* Determine if it is the current clicked BSSID */
		        if( SCParams.ConnectedBSSID!=null && SCParams.ConnectedBSSID.length()!=0
		        		&& getBSSID!=null // Safety check
		        		 && (getBSSID.equals(new String("\"" + SCParams.ConnectedBSSID + "\""))
		        				 || getBSSID.equals(new String(SCParams.ConnectedBSSID))) ) {
		        	isCurrentConnectBSSID = true;
		        } else {
		        	isCurrentConnectBSSID = false;
		        }
//				Log.d(TAG, "isCurrentConnectBSSID: " + isCurrentConnectBSSID);

		        /* System many try to connect to another BSSID, force re-connect to the current one. */
		        if( SCParams.ConnectedBSSID!=null && SCParams.ConnectedBSSID.length()!=0
		        		&& isBssidExist(SCParams.ConnectedBSSID) && !isCurrentConnectBSSID
		        			&& needReconnectWhenWifiChanged ) {
		 			Log.w(TAG, "Launch Wi-Fi connecter retry...");
		 			SCParams.mConnectQuiet = true;
		 			launchWifiConnecter(SmartLinkActivity.this, mScanResult);
		 			needReconnectWhenWifiChanged = false;
		        }

				/* Network is connected */
				if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
		        	WifiInfo wi = mWifiManager.getConnectionInfo();
		        	if(wi!=null&&wifi_ssid_et!=null){
		        		String connectedssid = wi.getSSID();
		        		if(connectedssid!=null){
		        			if(connectedssid.charAt(0) == '"')
		        				wifi_ssid_et.setText(connectedssid.subSequence(1, connectedssid.length()-1).toString());
		        		}
		        	}
		        	
			        if(isCurrentConnectBSSID) {
						Toast.makeText(SmartLinkActivity.this, "Wi-Fi network is connected.", Toast.LENGTH_LONG).show();
						/* Store SSID and password to file */
				        fileOps.UpdateSsidPasswdFile(); //connect successful, update file

						/* Config time not expired, and the clicked SSID is re-connected, so re-config */
				        if(!configFinished) {
				        	boolean needReConfig = false;

				        	currentTime = System.currentTimeMillis();
				        	timeElasped = currentTime-startConfigTime;
				        	if(timeElasped < SCLibrary.OldModeConfigTimeMs) {
				        		SCLibrary.OldModeConfigTimeMs -= timeElasped;
				        		SCLibrary.TotalConfigTimeMs -= timeElasped;
				        		needReConfig = true;
				        	} else if (timeElasped < SCLibrary.TotalConfigTimeMs) {
				        		SCLibrary.OldModeConfigTimeMs = 0;
				        		SCLibrary.TotalConfigTimeMs -= timeElasped;
				        		needReConfig = true;
				        	} else {
				        		configFinished = true;
				        	}

				        	if(needReConfig) {
								if(CfgResultDialog!=null && CfgResultDialog.isShowing())
									CfgResultDialog.dismiss();
									
								// wlan interface name, phone ip
				        		SCLib.rtk_sc_start("",""); // Generate profile and start simple configure
				            	StartConfigPopUp();
				        	}
				        }
			        } else {
			        	WifiConnectStat = "DISCONNECTED";
			        }
				}

				/* Network is disconnected or changed, so stop config. */
    	    	if( (info.getState().equals(NetworkInfo.State.DISCONNECTED) || !isCurrentConnectBSSID)
    	    			&& cfgProgressDialog.isShowing() ) {
					Toast.makeText(SmartLinkActivity.this, "The selected AP is disconnected.", Toast.LENGTH_LONG).show();
					Log.w(TAG, "The selected AP is disconnected.");
    	    		cfgProgressDialog.dismiss();
    				SCLib.rtk_sc_stop();
    				ShowResultPopUpSafe();
    	    	}

		        SCLib.WifiStartScan();
		        GetAllWifiList();
			}
		}
	};

	public boolean isBssidExist(String bssid){
		List<ScanResult> ScanResults;
		ScanResult Result;

		scan_wifi.setEnabled(false);
		ScanResults = SCLib.WifiGetScanResults();

		if(ScanResults == null) {
			Log.e(TAG, "No scan result.");
			return false;
		}

		for(int i=0; i<ScanResults.size(); i++) {
			Result = ScanResults.get(i);
			if(Result.BSSID.equals(bssid)) {
//				Log.d(TAG, "BSSID \"" + bssid + "\" exists.");
				return true;
			}
		}

		scan_wifi.setEnabled(true);

//		Log.d(TAG, "BSSID \"" + bssid + "\" does not exist.");
		return false;
    }

	public void GetAllWifiList(){
		scan_wifi.setEnabled(false);
		// �õ�ɨ����
		List<ScanResult> getScanResults = SCLib.WifiGetScanResults();
		if(getScanResults==null) {
			Log.e(TAG, "Get scan results error.");
			scan_wifi.setEnabled(true);
    		wifiArrayList.clear();
        	wifiListView.setAdapter(wifiListItemAdapter);
			return;
		}
		int getSize = getScanResults.size();
//		Log.d(TAG, "getSize: " + getSize);
		List<ScanResult> subScanResults = new ArrayList<ScanResult>();
		int subSize = 0;
		ScanResult selectResult = null;
		ScanResult tmpResult = null;

		WifiConnecting = false;
		WifiConnected = false;
		WifiDisconnected = false;
    	if(getScanResults != null){
    		for(int i=0; i<getSize; i++){
//    			Log.d(TAG, "Get SSID: " + getScanResults.get(i).SSID + " (" + getScanResults.get(i).BSSID + ")");
    			if(getScanResults.get(i).level <= -100) { //���˵������ź�Wi-Fi
//    				Log.d(TAG, "Ignored extra poor signal(" + getScanResults.get(i).level + "dBm) Wi-Fi.\n");
    				continue;
    			}
    	    	if(getScanResults.get(i).SSID==null || getScanResults.get(i).SSID.length()==0) {
    	    		continue; // ���˵� Hidden SSID
    	    	}
	    		if((SCParams.ConnectedBSSID!=null) &&
	    				(SCParams.ConnectedBSSID.length()>0) &&
	    					(SCParams.ConnectedBSSID.equals(getScanResults.get(i).BSSID)) ) {

//	        		Log.d(TAG, "WifiConnectStat: " + WifiConnectStat);
	    			if(WifiConnectStat.equals("CONNECTED")) {
//		        		Log.d(TAG, "Find out the connected SSID");
	    				WifiConnected = true;
	    			} else if(WifiConnectStat.equals("CONNECTING")) {
	    				WifiConnecting = true;
	    			} else if(WifiConnectStat.equals("DISCONNECTED")) {
	    				WifiDisconnected = true;
//						Log.e(TAG, "Connect error, please check the password and the AP status, then try again.");
	    			}
	    			selectResult = getScanResults.get(i); // Find out the selected SSID
	    		} else {
	    			subScanResults.add(getScanResults.get(i)); // Store the other SSIDs
	    			subSize++;
	    		}
    		}
//    		Log.d(TAG, "subSize: " + subSize);

    		// �� other SSIDs �����ź�ǿ�Ƚ�������
    		Collections.sort(subScanResults, new Comparator<ScanResult>() {
    			@Override
    		    public int compare(ScanResult a, ScanResult b) {
    				return (String.format("%d", a.level)).compareTo(String.format("%d", b.level)); //����
    				//return (String.format("%d", b.level)).compareTo(String.format("%d", a.level)); //����
    		    }
    		});

    		mScanResults = new ArrayList<ScanResult>();
    		HashMap<String, Object> hmap;
    		wifiArrayList.clear();
    		if(WifiConnected || WifiConnecting || WifiDisconnected) {
    			// Set the connected/connecting SSID to the top
				mScanResults.add(selectResult);
				hmap = new HashMap<String, Object>();

				hmap.put("list_item_upper", selectResult.SSID);
				if(WifiConnected) {
		    		hmap.put("list_item_below", selectResult.BSSID + "  " +
		    						selectResult.level + "dBm" + "  Connected");
				} else if (WifiConnecting) {
		    		hmap.put("list_item_below", selectResult.BSSID + "  " +
		    						selectResult.level + "dBm" + "  Connecting");
				} else {
		    		hmap.put("list_item_below", selectResult.BSSID + "  " +
    								selectResult.level + "dBm" + "  Disconnected");
				}
	    		wifiArrayList.add(hmap);

        		for(int i=0; i<subSize; i++){ // Set the other SSIDs to bellow
        			tmpResult = subScanResults.get(i);
    				mScanResults.add(tmpResult);
    				hmap = new HashMap<String, Object>();
    				hmap.put("list_item_upper", tmpResult.SSID);
    	    		hmap.put("list_item_below", tmpResult.BSSID + "  " + tmpResult.level + "dBm");
    	    		wifiArrayList.add(hmap);
        		}

				hmap = new HashMap<String, Object>();
				hmap.put("list_item_upper", "Add more network......");
				hmap.put("list_item_below", ""); // add to avoid null exception
	    		wifiArrayList.add(hmap);
    		} else {
        		for(int i=0; i<subSize; i++){ // Set the whole SSIDs
        			tmpResult = subScanResults.get(i);
    				mScanResults.add(tmpResult);
    				hmap = new HashMap<String, Object>();
    				hmap.put("list_item_upper", tmpResult.SSID);
    	    		hmap.put("list_item_below", tmpResult.BSSID + "  " + tmpResult.level + "dBm");
    	    		wifiArrayList.add(hmap);
        		}

				hmap = new HashMap<String, Object>();
				hmap.put("list_item_upper", "Add more network......");
				hmap.put("list_item_below", ""); // add to avoid null exception
	    		wifiArrayList.add(hmap);
    		}

//    		for(int i=0; i<wifiArrayList.size(); i++) {
//    			Log.d(TAG, "Show SSID: " + wifiArrayList.get(i).get("list_item_upper").toString() +
//    						" (" + wifiArrayList.get(i).get("list_item_below").toString() + ")");
//    		}
    	}

    	wifiListView.setAdapter(wifiListItemAdapter);
		scan_wifi.setEnabled(true);
    }

	/** WIFI���õ����� */
	public void OpenWifiPopUp()
	{
	 	AlertDialog.Builder builder = new AlertDialog.Builder(this);

	 	builder.setTitle("Warning: Wi-Fi Disabled!")
	 	.setIcon(R.drawable.ic_dialog_icon)
		.setCancelable(false)
	 	.setPositiveButton("Turn on Wi-Fi", new DialogInterface.OnClickListener() {
	 		public void onClick(DialogInterface dialog, int id) {
//	 			startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
	 			SCLib.WifiOpen();
        		SCLib.WifiStartScan();
            	GetAllWifiList();
           }
	 	})
	 	.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
	 		public void onClick(DialogInterface dialog, int id) {
	 			SmartLinkActivity.this.finish();
           }
	 	});
	 	builder.show();
	}

	/** ����PIN Code */
	public void SetPINPopUp(final boolean DevControlEnable)
	{
		LayoutInflater inflater = getLayoutInflater();
		View PinView = inflater.inflate(R.layout.sc_pincode_dialog,
		     (ViewGroup)findViewById(R.id.pin_code_dialog));

		QRCodeScan = (ImageButton)PinView.findViewById(R.id.qrcode_scan);
		QRCodeScan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/* ��ɨ�����ɨ����������ά�� */
//				Intent openCameraIntent = new Intent(MainActivity.this, CaptureActivity.class);
//				startActivityForResult(openCameraIntent, 0);
			}
		});

	 	PINEditText = (EditText)PinView.findViewById(R.id.pin_input);
	 	PINEditText.setTextSize(20);
	 	PINEditText.setText(PINSet);
		if(PINEnabled) {
			QRCodeScan.setEnabled(true);
			PINEditText.setEnabled(true);
			PINEditText.setFocusable(true);
			PINEditText.setFocusableInTouchMode(true);
		} else {
			QRCodeScan.setEnabled(false);
			PINEditText.setEnabled(false);
			PINEditText.setFocusable(false);
			PINEditText.setFocusableInTouchMode(false);
		}

	 	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	 	builder.setTitle("Set PIN Code:")
	 	.setIcon(R.drawable.ic_dialog_icon)
		.setCancelable(false)
	 	.setView(PinView)
	 	.setMultiChoiceItems(new String[] {"Enable PIN"}, new boolean[]{PINEnabled},
	 			new DialogInterface.OnMultiChoiceClickListener() {
	 		@Override
	 		public void onClick(DialogInterface dialog, int which, boolean isChecked) {
	 			if(isChecked) {
	 				PINEnabled = true;
	 				QRCodeScan.setEnabled(true);
	 				PINEditText.setEnabled(true);
	 				PINEditText.setFocusable(true);
	 				PINEditText.setFocusableInTouchMode(true);
	 			} else {
		 			PINEnabled = false;
		 			QRCodeScan.setEnabled(false);
					PINEditText.setEnabled(false);
					PINEditText.setFocusable(false);
					PINEditText.setFocusableInTouchMode(false);
	 			}
	 		}
	 	})
	 	.setNegativeButton(null, null)
	 	.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	 		@Override
	 		public void onClick(DialogInterface dialog, int id) {
	 			if(PINEnabled) {
 					PINGet = PINEditText.getText().toString();
// 					Log.d(TAG, "PIN Code Get��" + PINGet);

	 				byte[] pinget = PINGet.getBytes();
	 				byte[] pinset;
	 				if(pinget.length>0) {
	 					if(pinget.length<8) {
		 					pinset = new byte[8];
	 						System.arraycopy(pinget, 0, pinset, 0, pinget.length);
	 						for(int i=pinget.length; i<8; i++) {
	 							pinset[i] = '0';
	 						}
	 					} else if(pinget.length>=8 && pinget.length<=64){
		 					pinset = new byte[pinget.length];
	 						System.arraycopy(pinget, 0, pinset, 0, pinget.length);
	 					} else {
		 					pinset = new byte[64];
	 						System.arraycopy(pinget, 0, pinset, 0, 64);
	 					}
	 					PINSet = new String(pinset);
	 				} else {
	 					PINSet = new String(pinget);
	 				}
	 			} else {
	 				PINSet = null;
	 			}
	 			fileOps.UpdateCfgPinFile((PINSet!=null && PINSet.length()>0) ? PINSet : "null");
// 				Log.d(TAG, "PIN Code Set: " + PINSet);

 				if(DevControlEnable) {
 					pinArrayList.set(CurrentItem, (PINSet!=null && PINSet.length()>0) ? PINSet : "null");
 					if(PINSet==null || PINSet.length()==0) {
 						Log.e(TAG, "No PIN is inputed");
 						Toast.makeText(SmartLinkActivity.this, "No PIN is inputed", Toast.LENGTH_LONG).show();
 						return;
 					}
 					ControlPopUp(PINSet);
 				}
           }
	 	});
	 	builder.show();
	}

	/** ��ȡQRCodeɨ���� */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/* ����QRCodeɨ���� */
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			QRCodeScanResult = bundle.getString("result");
			Log.d(TAG, "QRCodeScanResult: " + QRCodeScanResult);

			if(QRCodeScanResult.length()>0 && QRCodeScanResult.length()<64) {
				PINEditText.setText(QRCodeScanResult);
			} else {
				Log.e(TAG, "Wrong QRCode!");
			}
		}
	}

	/** �����豸 */
	@SuppressWarnings("deprecation")
	public void DiscoverDevPopUp(final int milliSeconds)
	{
		discovDialog.setTitle("Wi-Fi: " + SCLib.getConnectedWifiSSID());
		discovDialog.setMessage("    Discovering devices......");
		discovDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		discovDialog.setCancelable(false);
		discovDialog.setButton("Cancel", new DialogInterface.OnClickListener(){
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        	DiscovEnable = false;
	            dialog.cancel();
	        }
	    });
		discovDialog.show();

		new Thread(new Runnable(){
            int count = 0;
    		public void run() {
    			Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);
    			byte[] DiscovCmdBuf = SCCtlOps.rtk_sc_gen_discover_packet(SCLib.rtk_sc_get_default_pin());
    			long startTime = System.currentTimeMillis();
    			long endTime = System.currentTimeMillis();
                //Log.e(TAG,"send discover begin................");
    			while(DiscovEnable && (endTime-startTime)<milliSeconds) {                   
        		 	SCLib.rtk_sc_send_discover_packet(DiscovCmdBuf, "255.255.255.255");
        			endTime = System.currentTimeMillis();
                    //Log.e(TAG,"send discover count = "+ count);
                    count++;
    			}
               // Log.e(TAG,"send discover end................");
    			Log.i(TAG, "Discover Time Elapsed: " + (endTime-startTime) + "ms");

				// Update Status
				Message msg = Message.obtain();
				msg.obj = null;
				msg.what = ~SCParams.Flag.DiscoverACK; //timeout
				SCLib.TreadMsgHandler.sendMessage(msg);
    		}
    	}).start();
	}

	/** ���Ϳ����豸���� */
	public void SendCtlDevPacket(final int flag, final String pin, final String ip, final String new_name)
	{
//		Log.d(TAG, "ip: " + ip);
//		Log.d(TAG, "pin: " + pin);
//		Log.d(TAG, "name: " + new_name);

		new Thread(new Runnable(){
    		int count = 0;
    		public void run() {
    			Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);
				byte[] buf = SCCtlOps.rtk_sc_gen_control_packet(flag, SCLib.rtk_sc_get_default_pin(), pin, new_name);

//				String dbgStr = new String();
//				for(int i=0; i<buf.length; i++)
//					dbgStr += String.format("%02x ", buf[i]);
//				Log.d(TAG, "SendCtlDevPacket: " + dbgStr);

    			while(count<15) {
    			 	SCLib.rtk_sc_send_control_packet(buf, ip);
    				try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
    			 	count++;
    			}
    		}
    	}).start();
	}

	/** ��ʾɾ��Profile */
	@SuppressWarnings("deprecation")
	public void DelProfProgressPopUp()
	{
		DelProfFirstShow = true;

		delProfDialog.setTitle(CurrentControlDev);
		delProfDialog.setMessage("    Removing......");
		delProfDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		delProfDialog.setCancelable(false);
		delProfDialog.setButton("Cancel", new DialogInterface.OnClickListener(){
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            dialog.cancel();
	        }
	    });
		delProfDialog.show();
	}

	/** �������豸 */
	@SuppressWarnings("deprecation")
	public void RenameDevProgressPopUp(final String input_pin, final String ip, final String dev_name)
	{
	 	final EditText editText = new EditText(SmartLinkActivity.this);

	 	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	 	builder.setTitle(dev_name)
	 	.setIcon(R.drawable.ic_dialog_icon)
		.setCancelable(false)
	 	.setMessage("Please input the new name:")
	 	.setView(editText)
	 	.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	 		@Override
	 		public void onClick(DialogInterface dialog, int id) {
	 			ReNameStr = editText.getText().toString();
	 			if(ReNameStr.length()>0) {

					RenameDevFirstShow = true;
					renameDevDialog.setTitle(dev_name);
	 				if(ip.equals("0.0.0.0")) {
	 					renameDevDialog.setMessage("    Client Getting IP......");
	 				} else {
	 					SendCtlDevPacket(SCParams.Flag.RenameDev, input_pin, ip, ReNameStr);
	 					renameDevDialog.setMessage("    Renaming......");
	 				}
					renameDevDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					renameDevDialog.setCancelable(false);
					renameDevDialog.setButton("Cancel", new DialogInterface.OnClickListener(){
				        @Override
				        public void onClick(DialogInterface dialog, int which) {
				            dialog.cancel();
				        }
				    });
					renameDevDialog.show();
	 			}
           }
	 	})
	 	.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	 		@Override
	 		public void onClick(DialogInterface dialog, int id) {
	            dialog.cancel();
	 		}
	 	});
	 	builder.show();
	}

	/** �豸����״̬ */
	public void CtlDevStatPopUp(String title)
	{
	 	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	 	builder.setTitle(title)
	 	.setIcon(android.R.drawable.ic_dialog_info)
		.setCancelable(false)
	 	.setPositiveButton("OK", new DialogInterface.OnClickListener(){
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
            	SCCtlOps.rtk_sc_control_reset();
            	DiscovEnable = true;
            	DiscoverDevPopUp(5000);
				discovDialog.dismiss();
            	ShowConnectedDevs();

				ResultShowable = true;
    			if(CurrentView==0) { // in configuration progress
    				SCViewPager.setCurrentItem(1);
    				SCViewPager.setAdapter(new PageAdpt());
    			}
	            dialog.cancel();
	        }
	    })
	 	.setNegativeButton(null, null);
	 	builder.show();
	}

	/** ��ʾ����ɨ���� */
	public void ShowResultPopUp()
	{
	 	final int num;
	 	final List<HashMap<String, Object>> InfoList = new ArrayList<HashMap<String, Object>>();
	 	final List<String> MacList = new ArrayList<String>();

	 	num = SCLib.rtk_sc_get_connected_sta_num();
		if(num==0) {
			MacList.add("None");
		} else {
			SCLib.rtk_sc_get_connected_sta_info(InfoList);
			for(int i=0; i<num; i++) {
			 	if(InfoList.get(i).get("Name")==null) {
					MacList.add((String)InfoList.get(i).get("MAC"));
			 	} else {
					MacList.add((String)InfoList.get(i).get("Name"));
			 	}
			}
		}

		/** Refresh PIN of MAC and store in file */
		if(InfoList.size()>0) {
			fileOps.UpdateCtlPinFile(InfoList.get(0).get("MAC").toString(),
				(PINSet!=null && PINSet.length()>0) ? PINSet : "null");
		}

	 	AlertDialog.Builder CfgBuilder = new AlertDialog.Builder(this);
	 	CfgBuilder.setTitle("Client list:")
	 	.setIcon(R.drawable.ic_dialog_icon)
		.setCancelable(false)
	 	.setItems(MacList.toArray(new String[MacList.size()]), null);

		if(num>0) {
			CfgBuilder.setPositiveButton("Rename", new DialogInterface.OnClickListener(){
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
					SCLib.rtk_sc_stop();
			    	configFinished = true;

					if(PINSet==null) {
						Log.d(TAG, "Null PIN");
			    	    Toast.makeText(SmartLinkActivity.this, "Null PIN", Toast.LENGTH_LONG).show();
						return;
					}
					if(PINSet.length()==0) {
						Log.d(TAG, "No PIN, can not rename.");
			    	    Toast.makeText(SmartLinkActivity.this, "No PIN, can not rename.", Toast.LENGTH_LONG).show();
						return;
					}

					List<HashMap<String, Object>> InfoReGet = new ArrayList<HashMap<String, Object>>();
					SCLib.rtk_sc_get_connected_sta_info(InfoReGet);
					String ip = InfoReGet.get(0).get("IP").toString();
					String name = MacList.get(0).toString();
					if(ip.equals("0.0.0.0")) {
						RenamingOfConfig = true;
						RenameDevProgressPopUp(PINSet, ip, name);
					} else {
						RenameDevProgressPopUp(PINSet, ip, name);
					}

		            dialog.cancel();
		        }
		    });
		}

		CfgBuilder.setNegativeButton("Finish", new DialogInterface.OnClickListener(){
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        	ResultShowable = true;
	            dialog.cancel();
	        }
	    });

		CfgResultDialog = CfgBuilder.show();
	}

	public boolean ShowResultPopUpSafe()
	{
//		Log.d(TAG, "CurrentView: " + CurrentView);
		if(CurrentView==0 && !RenamingOfConfig) {
			ResultShowable = false;
			if(CfgResultDialog!=null && CfgResultDialog.isShowing())
				CfgResultDialog.dismiss();
			if(isActivityAlive) {
				ShowResultPopUp();
			} else {
				Log.e(TAG, "Activity is not alive");
				return false;
			}
//			removeDialog();
		}

		return true;
	}

	/** ��ʾ���ù�� */
	@SuppressWarnings("deprecation")
	public void StartConfigPopUp()
	{
		cfgProgressDialog.setTitle("Wi-Fi: " + SCLib.getConnectedWifiSSID());
		cfgProgressDialog.setMessage("    Configuring......");
		/* ����ProgressDialog����ʾ��ʽ: Բ�ν���� */
		cfgProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		cfgProgressDialog.setCancelable(false);
		cfgProgressDialog.setButton("Pause", new DialogInterface.OnClickListener(){
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
				SCLib.rtk_sc_stop();
		    	configFinished = true;
				ShowResultPopUp();
	            dialog.cancel();
	        }
	    });
		cfgProgressDialog.show();
	}

	/** �������� */
	public void StartConfig()
	{
		SharedPreferences settings;
		boolean noSendSsid, pack_type;
		String setVal;

		/* Get settings */
		settings = getSharedPreferences("com.realtek.simpleconfig_preferences", MODE_PRIVATE);

		/* Flag used to no send broadcast(no hidden) SSID */
		noSendSsid = settings.getBoolean("not_send_bcast_ssid", false);
		Log.d(TAG, "noSendSsid: " + noSendSsid);

        pack_type = settings.getBoolean("pack_type", false);
		Log.d(TAG, "pack_type: " + pack_type);
        SCLib.rtk_sc_set_packet_type(pack_type);
//		if (pack_type)
//			SCLibrary.PackType = 1;
//		else
//			SCLibrary.PackType = 0;

		/* Profile(SSID+PASSWORD, contain many packets) sending total time(ms). */
		setVal = settings.getString("config_max_time", "120000");
		if(setVal!=null && setVal.length()>0) {
			SCLibrary.TotalConfigTimeMs = Integer.parseInt(setVal);
		}
//		Log.d(TAG, "ProfileSendTimeMillis: " + SCLibrary.ProfileSendTimeMillis);

		/* Configuring by using old mode(0~max_time) before new mode(the remaining time) */
		setVal = settings.getString("old_mode_config_time", "0");
		if(setVal!=null && setVal.length()>0) {
			SCLibrary.OldModeConfigTimeMs = Integer.parseInt(setVal);
		}
//		Log.d(TAG, "OldModeSendTimeMs: " + SCLibrary.OldModeSendTimeMs);

		/* Profile continuous sending rounds. */
		setVal = settings.getString("profile_rounds_val", "1");
		if(setVal!=null && setVal.length()>0) {
			SCLibrary.ProfileSendRounds = (byte)Integer.parseInt(setVal);
		}
//		Log.d(TAG, "ProfileSendRounds: " + SCLibrary.ProfileSendRounds);

		/* Time interval(ms) between sending two rounds of profiles. */
		setVal = settings.getString("profile_inteval_val", "1000");
		if(setVal!=null && setVal.length()>0) {
			SCLibrary.ProfileSendTimeIntervalMs = Integer.parseInt(setVal);
		}
//		Log.d(TAG, "ProfileSendTimeIntervalMs: " + SCLibrary.ProfileSendTimeIntervalMs);

		/* Time interval(ms) between sending two packets. */
		setVal = settings.getString("packet_inteval_val", "0");
		if(setVal!=null && setVal.length()>0) {
			SCLibrary.PacketSendTimeIntervalMs = Integer.parseInt(setVal);
		}
//		Log.d(TAG, "PacketSendTimeIntervalMs: " + SCLibrary.PacketSendTimeIntervalMs);

		// Handling exception android device(eg: Samsung S5)
		if( (Build.MANUFACTURER.equalsIgnoreCase("Samsung")) &&
			(Build.MODEL.equalsIgnoreCase("G9008"))){
			SCLibrary.PacketSendTimeIntervalMs  = 10; //10ms
		}

		/* Each packet sending counts. */
		setVal = settings.getString("packet_counts_val", "1");
		if(setVal!=null && setVal.length()>0) {
			SCLibrary.EachPacketSendCounts = (byte)Integer.parseInt(setVal);
		}
//		Log.d(TAG, "EachPacketSendCounts: " + SCLibrary.EachPacketSendCounts);


		WifiInfo info = mWifiManager.getConnectionInfo();
		if(info == null)
			return;
		String connectedssid = info.getSSID();
		if(connectedssid!=null){
			if(connectedssid.charAt(0) == '"')
				connectedssid = connectedssid.subSequence(1, connectedssid.length()-1).toString();
		}
//		SCParams.ConnectedSSID = info.getSSID();
//		SCParams.ConnectedBSSID = info.getBSSID();
		String inputssid = wifi_ssid_et.getText().toString();
		if(inputssid.length()<1 || !inputssid.contentEquals(connectedssid))
			return;
		
		SCParams.ConnectedSSID = inputssid;//wifi_ssid_et.getText().toString();
		SCParams.ConnectedPasswd = wifi_pwd_et.getText().toString();
		SCParams.ConnectedBSSID = info.getBSSID();
        
//		if(SCParams.ConnectedSSID==null || SCParams.ConnectedBSSID==null){
//	        Log.e(TAG, "Please select a Wi-Fi Network First");
//    	    Toast.makeText(SmartLinkActivity.this, "Please select a Wi-Fi Network First", Toast.LENGTH_SHORT).show();
//    	    return;
//		}

		int connect_count = 10;
//		while(connect_count>0)
//		{
////			Log.d(TAG, "connect_count: " + connect_count);
//			GetAllWifiList();
//			if(WifiConnected)
//				break;
//			try {
//				Thread.sleep(300);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			connect_count--;
//		}
//		if(!WifiConnected){
//			Log.e(TAG, "Wi-Fi not connected");
//    	    Toast.makeText(SmartLinkActivity.this, "Wi-Fi not connected, please wait a moment", Toast.LENGTH_SHORT).show();
//    	    return;
//		}

		connect_count = 10;
		int wifiIP = SCLib.WifiGetIpInt();
		while(connect_count>0 && wifiIP==0)
		{
			wifiIP = SCLib.WifiGetIpInt();
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			connect_count--;
		}
		if(wifiIP == 0)
		{
			Log.e(TAG, "Allocating IP");
    	    Toast.makeText(SmartLinkActivity.this, "Allocating IP, please wait a moment", Toast.LENGTH_SHORT).show();
			return;
		}

		SCLib.rtk_sc_reset();

		if(PINSet == null)
			SCLib.rtk_sc_set_default_pin("57289961");
		SCLib.rtk_sc_set_pin(PINSet);

		if(noSendSsid && (SCParams.isHiddenSSID==false)) {
			SCLib.rtk_sc_set_ssid("");
		} else {
			SCLib.rtk_sc_set_ssid(SCParams.ConnectedSSID);
		}

    	if(!SCParams.IsOpenNetwork) {
        	if(SCParams.ConnectedPasswd == null) {
    	        Log.e(TAG, "Please Enter Password");
        	    Toast.makeText(SmartLinkActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        	    return;
        	}
        	SCLib.rtk_sc_set_password(SCParams.ConnectedPasswd);
    	} else {
        	SCLib.rtk_sc_set_password("");
    	}

//		SCLib.rtk_sc_set_ip(wifiIP); // no needed any more
//		SCLib.rtk_sc_build_profile(); // no needed any more
    	SCLib.rtk_sc_set_bssid(SCParams.ConnectedBSSID); //BSSID of connected AP

    	startConfigTime = System.currentTimeMillis();
    	configFinished = false;
		
		// wlan interface name, phone ip
		SCLib.rtk_sc_start("",""); // Generate profile and start simple configure



		RenamingOfConfig = false;
    	StartConfigPopUp();
	}

    /** Button ����¼� */
	private class ButtonListener implements OnClickListener{
		@Override
		public void onClick(View v) {

		    switch (v.getId()) {
            case R.id.scan_wifi:
        		SCLib.WifiStartScan();
            	GetAllWifiList();
				break;
            case R.id.start_config:
            	StartConfig();
				break;
            case R.id.pin_enable:
            	PINSet = fileOps.ParseCfgPinFile();
            	if(PINSet!=null && !PINSet.equals("null") && PINSet.length()>0) {
                	PINEnabled = true;
            	} else {
            		PINSet=null;
            	}
            	SetPINPopUp(false);
				break;
            case R.id.discov_devs:
            	String ssid = SCLib.getConnectedWifiSSID();
            	if(ssid==null || ssid.length()==0 || ssid.equals("0x")) {
        	        Log.e(TAG, "Please connect a Wi-Fi Network First");
    		    	Toast.makeText(SmartLinkActivity.this, "Please connect a Wi-Fi Network First", Toast.LENGTH_SHORT).show();
    				SCViewPager.setCurrentItem(0);
    				SCViewPager.setAdapter(new PageAdpt());
            		return;
            	}
            	SCCtlOps.rtk_sc_control_reset();
            	DiscovEnable = true;
            	DiscoverDevPopUp(5000);
            	ShowConnectedDevs();
				break;
			default:
				break;
			}

		    if(SCLib.WifiStatus() == 2) {
		    	Toast.makeText(SmartLinkActivity.this, "Wi-Fi is enabling", Toast.LENGTH_SHORT).show();
		    }
		}
    }


	/** �����豸 */
	public void ControlPopUp(final String input_pin)
	{
		LayoutInflater inflater = getLayoutInflater();
		View CtlView = inflater.inflate(R.layout.sc_control_dev_dialog,
		     (ViewGroup)findViewById(R.id.dev_control_dialog));

		Button RenameDev = (Button)CtlView.findViewById(R.id.rename_dev);
		Button DelProf = (Button)CtlView.findViewById(R.id.del_prof);

	 	AlertDialog.Builder CtlBuilder = new AlertDialog.Builder(this);
	 	CtlBuilder.setTitle(CurrentControlDev)
	 	.setIcon(R.drawable.ic_dialog_icon)
		.setCancelable(false)
	 	.setView(CtlView)
	 	.setNegativeButton(null, null)
	 	.setPositiveButton("Return", new DialogInterface.OnClickListener() {
	 		@Override
	 		public void onClick(DialogInterface dialog, int id) {
	 			dialog.cancel();
	 		}
	 	});
//	 	CtlMenuDialog = CtlBuilder.create();
//	 	CtlMenuDialog.show();
	 	CtlMenuDialog = CtlBuilder.show();

		RenameDev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(CurrentControlIP.equals("0.0.0.0")) {
					Toast.makeText(SmartLinkActivity.this, "The client has no IP.", Toast.LENGTH_LONG).show();
					return;
				}
			 	RenameDevProgressPopUp(input_pin, CurrentControlIP, CurrentControlDev);
			}
		});

		DelProf.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(CurrentControlIP.equals("0.0.0.0")) {
					Toast.makeText(SmartLinkActivity.this, "The client has no IP.", Toast.LENGTH_LONG).show();
					return;
				}
				SendCtlDevPacket(SCParams.Flag.DelProf, input_pin, CurrentControlIP, null);
				DelProfProgressPopUp();
			}
		});
	}

	private void addNetworkPopup()
	{
		LayoutInflater inflater = getLayoutInflater();
		View addNetworkView = inflater.inflate(R.layout.add_network_content,
		     (ViewGroup)findViewById(R.id.add_network_dialog));

		final EditText network_name_edit;
		final Spinner  encrypt_type_spinner;
		final ArrayAdapter<String> encrypt_adapter;
		final String[] encryption_types = {"NONE", "WEP", "WAPI", "WPA-PSK", "WPA2-PSK", "WPA_EAP"};
		final CheckBox is_hidden_ssid;

		network_name_edit = (EditText)addNetworkView.findViewById(R.id.network_name_edit);

		encrypt_type_spinner = (Spinner)addNetworkView.findViewById(R.id.encrypt_type);
		encrypt_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, encryption_types);
		encrypt_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		encrypt_type_spinner.setAdapter(encrypt_adapter);
		encrypt_type_spinner.setOnItemSelectedListener(null);
		encrypt_type_spinner.setVisibility(View.VISIBLE);

		is_hidden_ssid =  (CheckBox)addNetworkView.findViewById(R.id.is_hidden_ssid);
		is_hidden_ssid.setChecked(true);

	 	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	 	builder.setTitle("Add new network:")
	 	.setIcon(R.drawable.ic_dialog_icon)
//	 	.setIcon(android.R.drawable.ic_dialog_info)
	 	.setView(addNetworkView)
	 	.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	 		@Override
	 		public void onClick(DialogInterface dialog, int id) {
	 			String ssid_name = network_name_edit.getText().toString();
	 			String encrypt_type = encrypt_type_spinner.getSelectedItem().toString();
	 			SCParams.isHiddenSSID = is_hidden_ssid.isChecked();
//	 			Log.d(TAG, "network_name_edit: " + ssid_name);
//	 			Log.d(TAG, "encrypt_type: " + encrypt_type);
//	 			Log.d(TAG, "is_hidden_ssid: " + SCParams.isHiddenSSID);
	 			if(encrypt_type.equals("NONE")) {
	 				encrypt_type = "";
	 			} else {
	 				encrypt_type = "[" + encrypt_type + "]";
	 			}
//	 			Log.d(TAG, "encrypt_type: " + encrypt_type);

	 			String jsonSsidStr = "{" +
	 					"\"SSID\":\"" + ssid_name + "\"" +
	 					",\"BSSID\":\"" + "\"" +
	 					",\"capabilities\":\"" + encrypt_type + "[ESS]\"" +
	 			    	",\"level\":" + 0 +
	 			    	",\"frequency\":" + 0 +
	 			    	"}";
//	   	 		Log.i(TAG, "jsonSsidStr: " + jsonSsidStr);
	 			Gson gson = new Gson();
	 			SCParams.reBuiltScanResult = gson.fromJson(jsonSsidStr,
	 									new TypeToken<ScanResult>(){}.getType());

//	 			Log.i(TAG, "reBuiltScanResult: " + SCParams.reBuiltScanResult);
//	 			Log.d(TAG, "reBuiltScanResult.SSID: " + SCParams.reBuiltScanResult.SSID);
//	 			Log.d(TAG, "reBuiltScanResult.BSSID: " + SCParams.reBuiltScanResult.BSSID);
//	 			Log.d(TAG, "reBuiltScanResult.capabilities: " + SCParams.reBuiltScanResult.capabilities);
//	 			Log.d(TAG, "reBuiltScanResult.level: " + SCParams.reBuiltScanResult.level);
//	 			Log.d(TAG, "reBuiltScanResult.frequency: " + SCParams.reBuiltScanResult.frequency);

	 			SCParams.mConnectQuiet = false;
				launchWifiConnecter(SmartLinkActivity.this, SCParams.reBuiltScanResult);
				needReconnectWhenWifiChanged = true;
            }
	 	})
	 	.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	 		@Override
	 		public void onClick(DialogInterface dialog, int id) {
	 			dialog.cancel();
            }
	 	});
	 	builder.show();
	}

	private OnItemClickListener wifiItemOnClick = new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	    	if(wifiArrayList.get(position).get("list_item_upper").equals("Add more network......")) {
	    		SCParams.addNewNetwork = true;
	    		addNetworkPopup();
	    		return;
	    	}
	    	SCParams.addNewNetwork = false;

	    	mScanResult = mScanResults.get(position);
	    	Log.i(TAG, "Clicked SSID: " + mScanResult.SSID);
	    	fileOps.ParseSsidPasswdFile(mScanResult.SSID);

        	String ssid = SCLib.getConnectedWifiSSID();
 			//New AP which has the same SSID as the previous one.
 			if( ssid!=null &&
 					(ssid.equals(new String("\"" + mScanResult.SSID + "\""))
 							|| ssid.equals(new String(mScanResult.SSID))) ) {
 	 			WifiManager mWifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
	 			mWifiManager.disconnect(); //Disconnect to avoid get incorrect IP.
 			}

 			SCParams.mConnectQuiet = false;
			launchWifiConnecter(SmartLinkActivity.this, mScanResult);
			needReconnectWhenWifiChanged = true;
	    }
	};

	/**
	 * Try to launch Wifi Connecter with {@link #hostspot}.
	 * @param activity
	 * @param hotspot
	 */
	private void launchWifiConnecter(final Activity activity, final ScanResult hotspot) {
		final Intent openWifiItemIntent = new Intent("com.wifi.connecter.FB_WIFI_CONNECT_OR_EDIT");
		openWifiItemIntent.putExtra("com.wifi.connecter.HOTSPOT", hotspot);
		try {
			activity.startActivity(openWifiItemIntent);
		} catch(ActivityNotFoundException e) {
			Toast.makeText(activity, "Wifi Connecter is not installed.", Toast.LENGTH_LONG).show();
	        Log.e(TAG, "Wifi Connecter is not installed.");
		}
	}

	private OnItemClickListener devItemOnClick = new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	    	CurrentItem = position;
	    	CurrentControlDev = (String)devArrayList.get(CurrentItem).get("list_item_upper");
	    	CurrentControlIP = (String)DevInfo.get(CurrentItem).get("IP");

//	    	Log.d(TAG, "CurrentItem: " + CurrentItem);
//	        Log.d(TAG, "CurrentControlDev: " + CurrentControlDev);
//	        Log.d(TAG, "CurrentControlIP: " + CurrentControlIP);

	    	if(DevInfo.get(CurrentItem).get("PIN") != null) {
	    		CtlUsePin = (Boolean)DevInfo.get(CurrentItem).get("PIN");
	    	}

			if(CtlUsePin) {
		    	PINSet = fileOps.ParseCtlPinFile(DevInfo.get(CurrentItem).get("MAC").toString());
				PINEnabled = true;
				SetPINPopUp(true);
			} else {
				ControlPopUp(SCLib.rtk_sc_get_default_pin());
			}
	    }
	};

	/** ��ʾ�����������豸 */
	public void ShowConnectedDevs()
	{
	 	int i;
		HashMap<String, Object> hmap;

	 	devArrayList.clear();
	 	pinArrayList.clear();
	 	DevInfo = new ArrayList<HashMap<String, Object>>();
	 	SCCtlOps.rtk_sc_get_discovered_dev_info(DevInfo);
		for(i=0; i<SCCtlOps.rtk_sc_get_discovered_dev_num(); i++) {
		 	hmap = new HashMap<String, Object>();
		 	if(DevInfo.get(i).get("Name")==null) {
				hmap.put("list_item_upper", DevInfo.get(i).get("MAC"));
				hmap.put("list_item_below",
						DevInfo.get(i).get("Status") + "      " +
								DevInfo.get(i).get("Name") + " ");
		 	} else {
				hmap.put("list_item_upper", DevInfo.get(i).get("Name"));
				hmap.put("list_item_below",
						DevInfo.get(i).get("Status") + "      " +
								DevInfo.get(i).get("MAC") + " ");
		 	}
			devArrayList.add(hmap);
			pinArrayList.add("null");
		}
    	devListView.setAdapter(devListItemAdapter);
	}

	private void SendCtlDevConfirmPacket(final int flag)
	{
		new Thread(new Runnable(){
			String pin;
			int count = 0;

    		public void run() {
    			Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);

    			if(CtlUsePin) {
	    			if(CurrentView==0) {
	//    				pin = PINSet;
	    				pin = (PINSet!=null && PINSet.length()>0) ? PINSet : "none";
	    			} else {
	    				if(pinArrayList.size()>0){
	        				pin = pinArrayList.get(CurrentItem).toString();
	    				} else {
	    					Log.e(TAG, "PIN array list is null");
	        				return;
	    				}
	    			}
    			} else {
    				pin = SCLib.rtk_sc_get_default_pin();
    			}

//    			Log.d(TAG, "Comfirm PIN: " + pin);
//    			Log.d(TAG, "Comfirm IP: " + CurrentControlIP);
    			byte[] buf = SCCtlOps.rtk_sc_gen_control_confirm_packet(flag,
    					SCLib.rtk_sc_get_default_pin(), pin);
    			while(count<15) {
        			SCLib.rtk_sc_send_control_packet(buf, CurrentControlIP);
//                    Log.e(TAG,"send control ack, "+count);
    				try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
    			 	count++;
    			}
    		}
    	}).start();
	}

	/** Handler class to receive send/receive message */
	private class MsgHandler extends Handler{
		byte ret;

		@Override
		public void handleMessage(Message msg){
//			Log.d(TAG, "msg.what: " + msg.what);
			switch(msg.what) {
			case ~SCParams.Flag.CfgSuccessACK:
    	    	if(cfgProgressDialog.isShowing()) {
		    	    Toast.makeText(SmartLinkActivity.this, "Config Timeout", Toast.LENGTH_SHORT).show();
    	    		cfgProgressDialog.dismiss(); // re-operable
    	    	}
				SCLib.rtk_sc_stop();
		    	configFinished = true;
				ShowResultPopUpSafe();
				break;
			case SCParams.Flag.CfgSuccessACK:
				if(!ResultShowable) {
//					Log.d(TAG, "Not Showable");
					break;
				}
    	    	if(cfgProgressDialog.isShowing()) {
    	    		cfgProgressDialog.dismiss();
    	    	}
				SCLib.rtk_sc_stop();
		    	configFinished = true;
				if(!ShowResultPopUpSafe())
					return;

				if(!RenamingOfConfig)
					break;
    		 	List<HashMap<String, Object>> InfoList = new ArrayList<HashMap<String, Object>>();
    			SCLib.rtk_sc_get_connected_sta_info(InfoList);
    			String ip = InfoList.get(0).get("IP").toString();
	    		if(!ip.equals("0.0.0.0")) {// Client Got IP
					Log.d(TAG, "Client Got IP");
					renameDevDialog.setMessage("    Renaming......");
	 				SendCtlDevPacket(SCParams.Flag.RenameDev, PINSet, ip, ReNameStr);
	 				RenamingOfConfig = false;
	    		}
				break;
			case SCParams.Flag.DiscoverACK:
				discovDialog.dismiss();
				DiscovEnable = false;
				SCCtlOps.handle_discover_ack((byte[])msg.obj);
				if(SCParams.DiscoveredNew) {
					ShowConnectedDevs();
				}
				break;
			case ~SCParams.Flag.DiscoverACK:
				discovDialog.dismiss();
				DiscovEnable = false;
				Log.d(TAG, "Discovery timeout.");
				break;
			case SCParams.Flag.DelProfACK:
				SendCtlDevConfirmPacket(SCParams.Flag.DelProf); // SCParams.Flag.DelProf: for encrypt, not return ack
    	    	if(delProfDialog.isShowing())
    	    		delProfDialog.dismiss();
        	    if(DelProfFirstShow) {
        	    	DelProfFirstShow = false;
        	    	if(CtlMenuDialog!=null && CtlMenuDialog.isShowing())
        	    		CtlMenuDialog.dismiss();
            	    ret = ((byte[])msg.obj)[3]; // response.status
    				if(ret==1){
    					try {                            
							Thread.sleep(1500); // Wait for remove finished(1.5 sec)
						} catch (InterruptedException e) {
							e.printStackTrace();
						}                        
        				CtlDevStatPopUp("Remove Device Success");
    				} else {
        				CtlDevStatPopUp("Remove Device Failed");
        				if(pinArrayList.size()>0)
        					pinArrayList.set(CurrentItem, "null");
//        				else
//        					Log.d(TAG, "pinArrayList null");
    				}
        	    }
				break;
			case SCParams.Flag.RenameDevACK:
                Log.e(TAG, "send control ack-ack start...........");
				SendCtlDevConfirmPacket(SCParams.Flag.RenameDev);
                Log.e(TAG, "send control ack-ack end...........");
    	    	if(renameDevDialog.isShowing()){
                    Log.e(TAG, "rename dialog is showing, and dismiss...........");
    	    		renameDevDialog.dismiss();
                }
        	    if(RenameDevFirstShow) {
                   // Log.e(TAG, "send control ack-ack start...........");
    				//SendCtlDevConfirmPacket(SCParams.Flag.RenameDev);
                    //Log.e(TAG, "send control ack-ack end...........");
                    
                    Log.e(TAG, "rename dialog is first show...........");
        	    	RenameDevFirstShow = false;
        	    	if(CtlMenuDialog!=null && CtlMenuDialog.isShowing())
        	    		CtlMenuDialog.dismiss();
                    Log.e(TAG, "get receive ack status value...........");
            	    ret = ((byte[])msg.obj)[3];
    				if(ret==1){
        				CtlDevStatPopUp("Rename Device Success");
    				} else {
        				CtlDevStatPopUp("Rename Device Failed");
        				if(pinArrayList.size()>0)
        					pinArrayList.set(CurrentItem, "null");
//        				else
//        					Log.d(TAG, "pinArrayList null");
    				}
        	    }
				break;
             case SCParams.Flag.PackFail:
                  Log.d(TAG, "packet use wrong!");
                  if(cfgProgressDialog.isShowing()) {
		    	    Toast.makeText(SmartLinkActivity.this, "Wrong Profile Length!", Toast.LENGTH_SHORT).show();
    	    		cfgProgressDialog.dismiss();
    	    	}
				SCLib.rtk_sc_stop();
		    	configFinished = true;
				break;
				
             case SCParams.Flag.CFGTimeSendBack:
 	    	    Toast.makeText(SmartLinkActivity.this, "Config time elapsed: " + msg.obj + "ms", Toast.LENGTH_LONG).show();
 				break;

			default:
				break;
			}
		}
	}

    public final class ViewHolder {
        public TextView titleText;
        public TextView infoText;
    }

	public class listBaseAdapter extends BaseAdapter {
	    private LayoutInflater mInflater;
	    private List<HashMap<String, Object>> listDevs;

	    public listBaseAdapter(Context context, List<HashMap<String, Object>> getListDevs) {
	        mInflater = LayoutInflater.from(context);
	        listDevs = getListDevs;
	    }

	    public int getCount() {
	        return listDevs.size();
	    }

	    public Object getItem(int arg0) {
	        return listDevs.get(arg0);
	    }

	    public long getItemId(int arg0) {
	        return arg0;
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {
	        ViewHolder holder = null;
	        if (convertView == null) {
	            holder = new ViewHolder();
//	            convertView = mInflater.inflate(R.layout.sc_list_items, null);
	            convertView = mInflater.inflate(R.layout.sc_list_items, parent, false);
	            holder.titleText = (TextView)convertView.findViewById(R.id.list_item_upper);
	            holder.infoText = (TextView)convertView.findViewById(R.id.list_item_below);
	            convertView.setTag(holder);
	        } else {
	            holder = (ViewHolder)convertView.getTag();
	        }

	        String list_item_upper = (String)listDevs.get(position).get("list_item_upper");
	        String list_item_below = (String)listDevs.get(position).get("list_item_below");
	        holder.titleText.setText(list_item_upper);
	        holder.infoText.setText(list_item_below);

	        return convertView;
	    }
	}

	private OnPageChangeListener PageChangeEvent = new OnPageChangeListener()
	{
		@Override
		public void onPageSelected(int arg0)
		{
//			Log.d(TAG, "onPageSelected: " + arg0);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2)
		{
			CurrentView = arg0;
//			Log.d(TAG, "onPageScrolled: " + arg0 + ", " + arg1 + ", " + arg2);
		}

		@Override
		public void onPageScrollStateChanged(int arg0)
		{
//			Log.d(TAG, "onPageScrollStateChanged: " + arg0);
		}
	};

	private class PageAdpt extends PagerAdapter
	{
		@Override
		public void startUpdate(View arg0)
		{
		}

		@Override
		public void finishUpdate(View arg0)
		{
		}

		@Override
		public int getCount()
		{
			return listViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1)
		{
			((ViewPager)arg0).addView(listViews.get(arg1));
			return listViews.get(arg1);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2)
		{
			((ViewPager) arg0).removeView(listViews.get(arg1));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1)
		{
			return arg0 == arg1;
		}

		@Override
		public Parcelable saveState()
		{
			return null;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1)
		{
			Log.d(TAG, "restoreState");
		}
	}
}
