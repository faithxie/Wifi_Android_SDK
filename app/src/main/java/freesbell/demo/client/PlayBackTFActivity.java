package freesbell.demo.client;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import freesbell.demo.client.R;
import fenzhi.nativecaller.NativeCaller;
import freesbell.demo.adapter.RemoteFileListAdapter;
import freesbell.demo.bean.CameraParamsBean;
import freesbell.demo.bean.FileItem;
import freesbell.demo.bean.JSONStructProtocal;
import freesbell.demo.bean.PlayBackBean;
import freesbell.demo.bean.WifiScanBean;
import freesbell.demo.bean.JSONStructProtocal.IPCNetRecordCfg_st;
import freesbell.demo.bean.JSONStructProtocal.IPCNetRecordGetCfg_st;
import freesbell.demo.client.BridgeService.ControllerBinder;
import freesbell.demo.content.ContentCommon;
import freesbell.demo.dialog.RemoteMediaFileDialog;
import freesbell.demo.dialog.RemoteMediaFileDialog.OnListener;
import freesbell.demo.utils.Cmds;
import freesbell.demo.utils.ServiceStub;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * @author 摄像机SD卡录像观看
 * */
public class PlayBackTFActivity extends BaseActivity implements
		OnItemClickListener, OnClickListener {
	// private ListView listView;
	private final String TAG = "PlayBackTFActivity";
//	private List<PlayBackBean> lists;
//	private ArrayList<String> groupList;
//	private Map<String, ArrayList<PlayBackBean>> childMap;
//	private ExpandableListView expandlistview;

	private TextView tvNoVideo;
	private BridgeService mBridgeService;
	private ProgressDialog progressDialog;
	// private PlayBackAdapter mAdapter;
	private int TIMEOUT = 30000;
	private final int PARAMS = 1;
	private boolean successFlag = false;
	private long startTime = 0;
	private long endTime = 0;
	private String strName;
	private String strDID;
	private TextView tvTitle;
	private TextView editDateBegin,editTimeBegin;
	private TextView editDateEnd,editTimeEnd;
	public View loadMoreView;
	private Button loadMoreButton;
	
	private CameraParamsBean mCameraParamsBean;
//	private String mStorageRootDir = "/mnt/sdcard0/media/sensor0";
	private RemoteFileListAdapter mAdapter;
	private FileItem mRootDir;
	private FileItem mCurrentDir;
	private FileItem mSearchDir;
	ArrayList<FileItem> list;
	private int mRequestNeededCount = 0;
	private ArrayList<ListPageBean> mListPageBean;
	private int mCurListPageBeanSearchIndex = 0;
	private int mSensorIndex = 0;
	
	private String mStorageRootDir;
	
	private JSONStructProtocal mJSONStructProtocal = new JSONStructProtocal();
	private IPCNetRecordGetCfg_st mIPCNetRecordGetCfg_st = mJSONStructProtocal.new IPCNetRecordGetCfg_st();
	private IPCNetRecordCfg_st mIPCNetRecordCfg_st = mJSONStructProtocal.new IPCNetRecordCfg_st();
	
	private ServiceConnection mConn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
//			mBridgeService.removeServiceStub(mServiceStub);
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			ControllerBinder myBinder = (ControllerBinder) service;
			mBridgeService = myBinder.getBridgeService();
//			mBridgeService.getTFRecordFileList(PlayBackTFActivity.this, strDID,
//					0, 0);
//			Log.d("tag", "onServiceConnected()");
			mBridgeService.setServiceStub(mServiceStub);
			
			new Thread(new Runnable(){
				@Override
				public void run() {
					Log.d(TAG,"onServiceConnected Cmds.ListRemoteDir");
					//String listCmd = "{\"p\":\"/mnt/sdcard0/media/sensor0\"}";
//					mRootDir = new FileItem();
//					mRootDir.type = 4;
//					mRootDir.name = listCmd;
					//ListRemoteDirInfo(ServiceStub stub,String uuid,String path,int sensorIndex,int mode,int start,int end)
					
					mIPCNetRecordGetCfg_st.ViCh = mSensorIndex;
					mIPCNetRecordGetCfg_st.Path = mCameraParamsBean.mStorageRootDir;
					Cmds.getAvRecorderConf(mServiceStub,strDID,mIPCNetRecordGetCfg_st.toJSONString());
					
//					mCurrentDir = mRootDir;
//					Cmds.ListRemoteDirInfo(mServiceStub,strDID,BridgeService.getDeviceData(strDID).mStorageRootDir,mSensorIndex,1,0,240000);
				}
				
			}).start();
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

	private Handler handler = new Handler();
	private ListView mListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDataFromOther();
		setContentView(R.layout.playbacktf);

//		lists = new ArrayList<PlayBackBean>();
//		childMap = new HashMap<String, ArrayList<PlayBackBean>>();
//		groupList = new ArrayList<String>();

		findView();
		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(getString(R.string.remote_video_getparams));
		progressDialog.show();
//		mHandler.postDelayed(runnable, TIMEOUT);
		setListener();
		tvTitle.setText(strName);
		initDate();
		// mAdapter = new PlayBackAdapter(PlayBackTFActivity.this,this);

		//BridgeService.setPlayBackTFInterface(this);
		//NativeCaller.PPPPGetSDCardRecordFileList(strDID, 0, 500);

//		adapter = new MyAdapter(this);
//		expandlistview.setAdapter(adapter);

//		list = new ArrayList<FileItem>();
		mRootDir = new FileItem();
		mRootDir.type = 4;
		mRootDir.name = "/mnt/s0/media/sensor0";//"{\"dir\":\"/mnt/sdcard0/media/sensor0\"}";
		mRootDir.path = "/mnt/s0/media/sensor0";
		mRootDir.list = new ArrayList<FileItem>();

		mAdapter = new RemoteFileListAdapter(this,mRootDir.list);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		// if(mAdapter.getCount()>=500){
		// listView.addFooterView(loadMoreView);
		// mAdapter.notifyDataSetChanged();
		// }

		// if (totalSize - 500 > 0) {
		// listView.addFooterView(loadMoreView);
		// }

		 Intent intent=new Intent(this,BridgeService.class);
		 bindService(intent, mConn, BIND_AUTO_CREATE);
	}

	private void initDate() {
		int byear = 0;
		int bmonth = 0;
		int bday = 0;

		Calendar calendar = Calendar.getInstance();
		int eyear = calendar.get(Calendar.YEAR);
		int emonth = calendar.get(Calendar.MONTH);
		int eday = calendar.get(Calendar.DAY_OF_MONTH);
		if (eday == 1) {// �ϸ��µ����һ��
			Calendar ca2 = new GregorianCalendar(calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH) - 1, 1);
			byear = ca2.get(Calendar.YEAR);
			bmonth = ca2.get(Calendar.MONTH);
			bday = ca2.getActualMaximum(Calendar.DAY_OF_MONTH);
		} else {
			byear = eyear;
			bmonth = emonth;
			bday = eday - 1;
		}
		Calendar bca = new GregorianCalendar(byear, bmonth, bday);
		Calendar eca = new GregorianCalendar(eyear, emonth, eday);
		Date bdate = bca.getTime();
		Date edate = eca.getTime();
		startTime = bdate.getTime();
		endTime = edate.getTime();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strDateBegin = f.format(bdate);
		String strDateEnd = f.format(edate);
		//SimpleDateFormat timef = new SimpleDateFormat("HH:mm:ss");
		//String strTimeBegin = f.format(bdate);
		//String strTimeEnd = f.format(edate);
		
		editDateBegin.setText(strDateBegin.substring(0, 10));
		editDateEnd.setText(strDateEnd.substring(0, 10));
		editTimeBegin.setText(strDateBegin.substring(11, strDateBegin.length()));
		editTimeEnd.setText(strDateEnd.substring(11, strDateEnd.length()));
	}

	private void getDataFromOther() {
		Intent intent = getIntent();
		strName = intent.getStringExtra(ContentCommon.STR_CAMERA_NAME);
		strDID = intent.getStringExtra(ContentCommon.STR_CAMERA_ID);
		mCameraParamsBean = BridgeService.getCameraBean(strDID);
		// String strPwd = intent.getStringExtra(ContentCommon.STR_CAMERA_PWD);
		// String strUser =
		// intent.getStringExtra(ContentCommon.STR_CAMERA_USER);
		Log.d("tag", "PlayBackTFActivity  strName:" + strName + " strDID:"
				+ strDID);
	}

	protected void onPause() {
		// overridePendingTransition(R.anim.out_to_right,
		// R.anim.in_from_left);// �˳�����
		super.onPause();
	}

	private void setListener() {
		// listView.setOnItemClickListener(this);
		btnBack.setOnClickListener(this);
		editDateBegin.setOnClickListener(this);
		editDateEnd.setOnClickListener(this);
		editTimeBegin.setOnClickListener(this);
		editTimeEnd.setOnClickListener(this);
		refresh_btn.setOnClickListener(this);
		progressDialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_BACK) {
					return true;
				}
				return false;
			}

		});

//		expandlistview.setOnChildClickListener(new OnChildClickListener() {
//
//			@Override
//			public boolean onChildClick(ExpandableListView parent, View v,
//					int groupPosition, int childPosition, long id) {
//				// TODO Auto-generated method stub
//				PlayBackBean playBean = childMap.get(
//						groupList.get(groupPosition)).get(childPosition);
//				String filepath = playBean.getPath();
//				String mess = filepath.substring(0, 14);
//				Intent intent = new Intent(PlayBackTFActivity.this,
//						PlayBackActivity.class);
//				intent.putExtra("did", playBean.getDid());
//				intent.putExtra("filepath", playBean.getPath());
//				intent.putExtra("videotime", mess);
//				startActivity(intent);
//				overridePendingTransition(R.anim.in_from_right,
//						R.anim.out_to_left);
//				return false;
//			}
//		});

	}

	private ImageButton btnBack,search_btn,storage_setting_btn;
	private Button up_dir_btn,refresh_btn;
//	private RelativeLayout up_dir_layout;
	private TextView file_count_tv,disk_capacity_tv;
	private ProgressBar disk_capacity_info_pb;
	private void findView() {
		mListView = (ListView) findViewById(R.id.listview);
		//expandlistview = (ExpandableListView) findViewById(R.id.listview);
		btnBack = (ImageButton)findViewById(R.id.back);
		btnBack.setOnClickListener(this);
		tvNoVideo = (TextView) findViewById(R.id.no_video);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		editDateBegin = (TextView) findViewById(R.id.edit_date_begin);
		editDateEnd = (TextView) findViewById(R.id.edit_date_end);
		editTimeBegin = (TextView) findViewById(R.id.edit_time_begin);
		editTimeEnd = (TextView) findViewById(R.id.edit_time_end);
		up_dir_btn = (Button)findViewById(R.id.up_dir_btn);
		refresh_btn = (Button)findViewById(R.id.camviewer_pic_btn);
		search_btn = (ImageButton)findViewById(R.id.search_btn);
		storage_setting_btn = (ImageButton)findViewById(R.id.storage_setting_btn);
		file_count_tv = (TextView)findViewById(R.id.file_count_tv);
		loadMoreView = getLayoutInflater()
				.inflate(R.layout.loadmorecount, null);
		loadMoreButton = (Button) loadMoreView.findViewById(R.id.btn_load);
		
		disk_capacity_info_pb = (ProgressBar)findViewById(R.id.disk_capacity_info_pb);
		disk_capacity_tv = (TextView)findViewById(R.id.disk_capacity_tv);
		
		loadMoreView.setVisibility(View.GONE);
//		expandlistview.addFooterView(loadMoreView);

		loadMoreButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

//				LoadMoreData();
			}
		});
		
		up_dir_btn.setOnClickListener(this);
		search_btn.setOnClickListener(this);
		storage_setting_btn.setOnClickListener(this);
		
//		RelativeLayout layout = (RelativeLayout) findViewById(R.id.top);
//		 Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.top_bg);
//	     BitmapDrawable drawable = new BitmapDrawable(bitmap);
//	     drawable.setTileModeXY(TileMode.REPEAT , TileMode.REPEAT );
//	     drawable.setDither(true);
//	     layout.setBackgroundDrawable(drawable);

	}

	private RemoteMediaFileDialog dialog3;
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Log.d("playBackTFActivity...", "!!!!!!!!!!" + position);
		final FileItem fi = mAdapter.getFileItem(position);
		if(fi.type == 4){
			mCurrentDir = fi;
			if(fi.list!=null&&fi.list.size()>0){
				setFileCount(fi.list.size());
				mAdapter.setFileItemList(mCurrentDir.list);
				mAdapter.notifyDataSetChanged();
			}else{
				//String listCmd = "{\"p\":\"" + fi.path + "\"}";
				//Cmds.ListRemoteDirInfo(mServiceStub,strDID,listCmd);
				progressDialog.show();
				Cmds.ListRemoteDirInfo(mServiceStub,strDID,fi.path,mSensorIndex,1,0,240000);
			}
			
		}else{
			Log.d(TAG, "Select " + fi.path);
//			RemoteMediaFileDialog dialog=new RemoteMediaFileDialog(this,300,200);
//			dialog.setCanceledOnTouchOutside(true);
//			dialog.show();
			int width = getWindowManager().getDefaultDisplay().getWidth();
			int height = getWindowManager().getDefaultDisplay().getHeight();
			dialog3 = new RemoteMediaFileDialog(this,fi.name, width,
					height);
			dialog3.mRoot.setOnClickListener(this);
			dialog3.setOnClickListener(new OnListener() {

				@Override
				public void onItemClick(int itemposition, int count) {
					Log.i(TAG, "position" + itemposition);
					switch (itemposition) {
					case 0: {//play
						Intent in = new Intent(PlayBackTFActivity.this,
								PlayBackActivity.class);
//						strDID = intent.getStringExtra("did");
//						strFilePath = intent.getStringExtra("filepath");
//						videotime = intent.getStringExtra("videotime");
				    	in.putExtra("filepath", fi.path);
				    	in.putExtra("did",strDID);
				    	in.putExtra("duration", fi.duration);
				    	
				    	if(fi.time!=null){
				    		in.putExtra("videotime", fi.time);
				    	}else{
				    		in.putExtra("videotime", "null");
				    	}
				    	PlayBackTFActivity.this.startActivityForResult(in, 201);
					}break;
					case 1:{//download
						//Cmds.RemoteFileOp(mServiceStub,strDID,fi.path,ContentCommon.AV_RECO_OP_DOWNLOAD_FILE_REQ);
					}break;
					case 2:{//operation_delete
						Cmds.RemoteFileOp(mServiceStub,strDID,mSensorIndex,fi.path,ContentCommon.AV_RECO_OP_DELETE_FILE_REQ);
					}break;
					case 3:{//attribute_of_file
						//Cmds.RemoteFileOp(mServiceStub,strDID,fi.path,ContentCommon.AV_RECO_OP_FILE_ATTRIBUTE_REQ);
					}break;
					}
				}
			});
			dialog3.show();
		}
	}

	private boolean yearEq(int year,Calendar ca){
		return year == ca.get(Calendar.YEAR);
	}
	
	private boolean yearMonEq(int year,int month,Calendar ca){
		return year == ca.get(Calendar.YEAR) && month==ca.get(Calendar.MONTH);
	}
	private boolean yearMonDayEq(int year,int month,int day,Calendar ca){
		return year == ca.get(Calendar.YEAR) && month==ca.get(Calendar.MONTH) && day==ca.get(Calendar.DAY_OF_MONTH);
	}
	private void initListPageBeanMon(int year,Calendar sca,Calendar eca){
		if(yearEq(year,sca)){
			if(yearEq(year,eca)){
				for(int k=sca.get(Calendar.MONTH);k<eca.get(Calendar.MONTH)+1;k++){
					initListPageBeanDay(year,k,sca,eca);
				}
			}else{
				for(int k=sca.get(Calendar.MONTH);k<12;k++){
					initListPageBeanDay(year,k,sca,eca);
				}
			}
		}else if(yearEq(year,eca)){
			for(int k=0;k<eca.get(Calendar.MONTH)+1;k++){
				initListPageBeanDay(year,k,sca,eca);
			}
		}else{
			for(int k=0;k<12;k++){
				initListPageBeanDay(year,k,sca,eca);
			}
		}
	}
	private void initListPageBeanDay(int year,int month,Calendar sca,Calendar eca){
		ListPageBean listPageBean=null;
		if(yearMonEq(year, month,sca)){
			if(yearMonEq(year, month,eca)){
				for(int k=sca.get(Calendar.DAY_OF_MONTH);k<eca.get(Calendar.DAY_OF_MONTH)+1;k++){
					listPageBean = new ListPageBean(); 
					listPageBean.path = mCameraParamsBean.mStorageRootDir + "/" + String.format("%04d",year) + String.format("%02d",month+1) + "/" + String.format("%02d",k);
					initListPageBeanTime(listPageBean,year,month,k,sca,eca);
//					if(!listPageBean.start.contentEquals(listPageBean.end))
					if(listPageBean.start!=listPageBean.end)
						mListPageBean.add(listPageBean);
				}
			}else{
				for(int k=sca.get(Calendar.DAY_OF_MONTH);k<32;k++){
					listPageBean = new ListPageBean(); 
					listPageBean.path = mCameraParamsBean.mStorageRootDir + "/" + String.format("%04d",year) + String.format("%02d",month+1) + "/" + String.format("%02d",k);
					initListPageBeanTime(listPageBean,year,month,k,sca,eca);
//					Log.d(TAG,"listPageBean.start:" + listPageBean.start + " end:"+listPageBean.end);
//					if(!listPageBean.start.contentEquals(listPageBean.end))
					if(listPageBean.start!=listPageBean.end)
						mListPageBean.add(listPageBean);
				}
			}
			
		}else if(yearMonEq(year, month,eca)){
			for(int k=1;k<eca.get(Calendar.DAY_OF_MONTH)+1;k++){
				listPageBean = new ListPageBean(); 
				listPageBean.path = mCameraParamsBean.mStorageRootDir + "/" + String.format("%04d",year) + String.format("%02d",month+1) + "/" + String.format("%02d",k);
				initListPageBeanTime(listPageBean,year,month,k,sca,eca);
//				if(!listPageBean.start.contentEquals(listPageBean.end))
				if(listPageBean.start!=listPageBean.end)
					mListPageBean.add(listPageBean);
			}
		}else{
			for(int k=1;k<32;k++){
				listPageBean = new ListPageBean(); 
				listPageBean.path = mCameraParamsBean.mStorageRootDir + "/" + String.format("%04d",year) + String.format("%02d",month+1) + "/" + String.format("%02d",k);
				initListPageBeanTime(listPageBean,year,month,k,sca,eca);
//				if(!listPageBean.start.contentEquals(listPageBean.end))
				if(listPageBean.start!=listPageBean.end)
					mListPageBean.add(listPageBean);
			}
		}
	}
	private void initListPageBeanTime(ListPageBean listPageBean,int year,int month,int day,Calendar sca,Calendar eca){
		if(yearMonDayEq(year,month,day,sca)){
			if(yearMonDayEq(year,month,day,eca)){
//				listPageBean.start = String.format("%02d%02d%02d",sca.get(Calendar.HOUR_OF_DAY) , sca.get(Calendar.MINUTE) , sca.get(Calendar.SECOND));
//				listPageBean.end = String.format("%02d%02d%02d",eca.get(Calendar.HOUR_OF_DAY) , eca.get(Calendar.MINUTE) , eca.get(Calendar.SECOND));
				listPageBean.start = sca.get(Calendar.HOUR_OF_DAY)*10000 + sca.get(Calendar.MINUTE)*100 + sca.get(Calendar.SECOND);
				listPageBean.end = eca.get(Calendar.HOUR_OF_DAY)*10000 + eca.get(Calendar.MINUTE)*100 + eca.get(Calendar.SECOND);
			}else{
//				listPageBean.start = String.format("%02d%02d%02d",sca.get(Calendar.HOUR_OF_DAY) , sca.get(Calendar.MINUTE) , sca.get(Calendar.SECOND));
//				listPageBean.end = "240000";
				listPageBean.start = sca.get(Calendar.HOUR_OF_DAY)*10000 + sca.get(Calendar.MINUTE)*100 + sca.get(Calendar.SECOND);
				listPageBean.end = 240000;
			}
		}else if(yearMonDayEq(year,month,day,eca)){
//			listPageBean.start = "000000";
//			listPageBean.end = String.format("%02d%02d%02d",eca.get(Calendar.HOUR_OF_DAY) , eca.get(Calendar.MINUTE) , eca.get(Calendar.SECOND));
			listPageBean.start = 000000;
			listPageBean.end = eca.get(Calendar.HOUR_OF_DAY)*10000 + eca.get(Calendar.MINUTE)*100 + eca.get(Calendar.SECOND);
		}else{
//			listPageBean.start = "000000";
//			listPageBean.end = "240000";
			listPageBean.start = 000000;
			listPageBean.end = 240000;
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.root:
			if(dialog3!=null&&dialog3.isShowing()){
				dialog3.dismiss();
			}
			break;
		case R.id.back:
			finish();
			// overridePendingTransition(R.anim.out_to_right,
			// R.anim.in_from_left);
			break;
		case R.id.edit_date_begin:
			String strBd = editDateBegin.getText().toString() + " 00:00:00";
			date(strBd, 0);//
			break;
		case R.id.edit_date_end://
			String strEd = editDateEnd.getText().toString() + " 00:00:00";
			date(strEd, 1);
			break;
		case R.id.edit_time_begin:
			strBd = "0000-00-00 " + editTimeBegin.getText().toString();
			date(strBd, 2);//
			break;
		case R.id.edit_time_end:
			strEd = "0000-00-00 " + editTimeEnd.getText().toString();
			date(strEd, 3);
			break;
		case R.id.camviewer_pic_btn:{
			if(mCurrentDir!=null&&mCurrentDir.list!=null){
				setFileCount(0);
				mCurrentDir.list.clear();
				if(mCurrentDir == mSearchDir && mListPageBean!=null&&mListPageBean.size()>0){
					mCurListPageBeanSearchIndex = 0;
					ListPageBean lpb = mListPageBean.get(mCurListPageBeanSearchIndex++);
					mSearchDir.name = lpb.path;
					mSearchDir.path = lpb.path;
					Cmds.ListRemoteDirInfo(mServiceStub,strDID,lpb.path,mSensorIndex,0,lpb.start,lpb.end);
				}else{
					Cmds.ListRemoteDirInfo(mServiceStub,strDID,mCurrentDir.path,mSensorIndex,1,0,240000);
				}
				progressDialog.show();
			}
			
		}break;
		case R.id.search_btn:
			String datepath = null;
			//int start = 0,end = 0;
			String startDateStr = editDateBegin.getText().toString();
			String endDateStr = editDateEnd.getText().toString();
			String startStr = startDateStr + " " + editTimeBegin.getText().toString();
			String endStr = endDateStr + " " + editTimeEnd.getText().toString();
			final SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date sdate = null,edate = null;
			try {
				sdate = f.parse(startStr);
				edate = f.parse(endStr);
			} catch (ParseException e) {
				e.printStackTrace();
				return;
			}
			Calendar sca = Calendar.getInstance();
			Calendar eca = Calendar.getInstance();
			sca.setTime(sdate);
			startStr = String.format("%1$02d%2$02d%3$02d",sca.get(Calendar.HOUR_OF_DAY),sca.get(Calendar.MINUTE),sca.get(Calendar.SECOND));
			String sym = String.format("%1$04d%2$02d",sca.get(Calendar.YEAR),sca.get(Calendar.MONTH));
			String sday = String.format("%1$02d",sca.get(Calendar.DAY_OF_MONTH));
			
			eca.setTime(edate);
			endStr = String.format("%1$02d%2$02d%3$02d",eca.get(Calendar.HOUR_OF_DAY),eca.get(Calendar.MINUTE),eca.get(Calendar.SECOND));
			String eym = String.format("%1$04d%2$02d",eca.get(Calendar.YEAR),eca.get(Calendar.MONTH));
			String eday = String.format("%1$02d",eca.get(Calendar.DAY_OF_MONTH));
			
			Log.d(TAG,"start:" + sym + "-" + (sday+1) + " "+ startStr + " endStr:" + eym + "-" + (eday+1) + " "+ endStr);
			
			if(mSearchDir == null){
				mSearchDir = new FileItem();
			}
			if(mListPageBean == null){
				mListPageBean = new ArrayList<ListPageBean>();
//				mListPageBean.clear();
			}
			mListPageBean.clear();
			
			for(int i=sca.get(Calendar.YEAR);i<eca.get(Calendar.YEAR)+1;i++){
				initListPageBeanMon(i,sca, eca);
			}
			Log.d(TAG,"mListPageBean size:" + mListPageBean.size());
			for(ListPageBean lpb:mListPageBean){
				Log.d(TAG,"mListPageBean path:" + lpb.path + " " + lpb.start + "-" + lpb.end);
			}
			mCurListPageBeanSearchIndex = 0;
			if(mListPageBean.size()>0){
				ListPageBean lpb = mListPageBean.get(mCurListPageBeanSearchIndex++);
				//String listCmd = "{\"lpbt\":{\"p\":\"" + lpb.path + "\",\"s\":" + lpb.start + ",\"e\":" + lpb.end +"}}";//json string
				//Cmds.ListRemotePageFilterByTimeFile(mServiceStub,strDID,listCmd);
				//clear the list.
				mSearchDir.type = 4;
				mSearchDir.name = lpb.path;
				mSearchDir.path = lpb.path;
				if(mCurrentDir != mSearchDir)
					mSearchDir.parent = mCurrentDir;
				if(mSearchDir.list==null){
					mSearchDir.list = new ArrayList<FileItem>();
				}
				mSearchDir.list.clear();
				mCurrentDir = mSearchDir;
				mAdapter.setFileItemList(mSearchDir.list);
				mAdapter.notifyDataSetChanged();
				progressDialog.show();
				Cmds.ListRemoteDirInfo(mServiceStub,strDID,lpb.path,mSensorIndex,0,lpb.start,lpb.end);
			}
			break;
		case R.id.up_dir_btn:
			if(mCurrentDir!=null && mCurrentDir.parent!=null && mCurrentDir.parent.list!=null){
				mCurrentDir = mCurrentDir.parent;
				
				setFileCount(mCurrentDir.list.size());
				mAdapter.setFileItemList(mCurrentDir.list);
				mAdapter.notifyDataSetChanged();
			}else{
				finish();
			}
			break;
		case R.id.storage_setting_btn:
//			Intent intent6 = new Intent(this, SettingSDCardActivity.class);
//			intent6.putExtra(ContentCommon.STR_CAMERA_ID, strDID);
//			intent6.putExtra(ContentCommon.STR_CAMERA_NAME, strName);
//			startActivity(intent6);
			break;
		default:
			break;
		}
	}

	private void date(String d, final int flag) {
		final SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = f.parse(d);
		} catch (ParseException e) {
			e.printStackTrace();
			return;
		}
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		if(flag == 0 || flag == 1){
		DatePickerDialog dialog = new DatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year, int month,
							int day) {
						Calendar ca = new GregorianCalendar(year, month, day);
						Date date2 = ca.getTime();

						String strDate = f.format(date2);
					switch (flag) {
					case 0:{//date begin
						String strE = editDateEnd.getText().toString();
						int result = strDate.compareTo(strE);
						Log.d("tag", "��ʼ result:" + result);
						if (result > 0) {
							showToast(R.string.remote_start_prompt);
						} else {
							startTime = date2.getTime();
							editDateBegin.setText(strDate.substring(0,10));
						}
					}break;
					case 1: {//date end
						String strB = editDateBegin.getText().toString();
						int result = strDate.compareTo(strB);
						if (result < 0) {
							showToast(R.string.remote_end_prompt);
						} else {
							endTime = date2.getTime();
							editDateEnd.setText(strDate.substring(0,10));
						}
					}break;
					}
					}
				}, ca.get(Calendar.YEAR), ca.get(Calendar.MONTH),
				ca.get(Calendar.DAY_OF_MONTH));
		dialog.show();
		}else if(flag == 2 || flag == 3){
			TimePickerDialog dialog = new TimePickerDialog(this,
					new TimePickerDialog.OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							Calendar ca = new GregorianCalendar(1970, 1, 1, hourOfDay, minute, 0);
							//ca.set(1970, 1, 1, hourOfDay, minute, 0);
							Date date2 = ca.getTime();

							String strDate = f.format(date2);
							Log.d(TAG,"Time:" + strDate);
						switch (flag) {
						case 2:{//time begin
							String strE = "1970-01-01 " + editTimeEnd.getText().toString();
							int result = strDate.compareTo(strE);
							Log.d("tag", "��ʼ result:" + result);
							{
//								startTime = date2.getTime();
								
								editTimeBegin.setText(strDate.substring(11, strDate.length()));
							}
						}break;
						case 3:{//time end
							String strB = "1970-01-01 " + editTimeBegin.getText().toString();
							int result = strDate.compareTo(strB);
							{
//								endTime = date2.getTime();
								editTimeEnd.setText(strDate.substring(11, strDate.length()));
							}
						}break;
						default:;
						}
						}
						
					},ca.get(Calendar.HOUR_OF_DAY), ca.get(Calendar.MINUTE),true);
			dialog.show();
		}
	}

	public void showToast(String content) {
		Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
	}

	public void showToast(int rid) {
		Toast.makeText(this, getResources().getString(rid), Toast.LENGTH_LONG)
				.show();
	}

	/**
	 * SD卡文件总数
	 */
	private int fileTFCount = 0;

	/**
	 * 当前第几页
	 */
	private int getCurrentPageIndex = 0;

	private boolean isViewMore = false;
	/**
	 * 当前页返回的数量
	 */
	private int TotalPageSize = 0;

//	public void LoadMoreData() {
//		int count = lists.size();
//		Log.e("info", "getCurrentPageIndex:" + getCurrentPageIndex
//				+ ",TotalPageSize:" + TotalPageSize + ",fileTFCount:"
//				+ fileTFCount + ",count:" + count);
//		if (count + 500 <= fileTFCount
//				&& getCurrentPageIndex + 1 <= TotalPageSize) {
//			getCurrentPageIndex += 1;
//			NativeCaller.PPPPGetSDCardRecordFileList(strDID,
//					getCurrentPageIndex, 500);
//			loadMoreButton.setText(R.string.remote_getmorefile);
//			adapter.notifyDataSetChanged();
//		} else {
//			int filecount = fileTFCount - count;
//			getCurrentPageIndex += 1;
//			NativeCaller.PPPPGetSDCardRecordFileList(strDID,
//					getCurrentPageIndex, 500);
//			adapter.notifyDataSetChanged();
//			loadMoreButton.setText(R.string.remote_getover);
//			loadMoreButton.setVisibility(View.GONE);
//		}
//	}

	private Handler viewhandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				loadMoreView.setVisibility(View.VISIBLE);
				break;
			}
		}

	};

//	MyAdapter adapter;

//	class MyAdapter extends BaseExpandableListAdapter {
//		Context context;
//		LayoutInflater mlayoutInflater;
//
//		MyAdapter(Context context) {
//			this.context = context;
//			mlayoutInflater = LayoutInflater.from(context);
//		}
//
//		public class chlidPicture {
//			ImageView chlidPic;
//			TextView chlidText;
//			TextView chlidText_type;
//		}

//		@Override
//		public Object getChild(int arg0, int arg1) {
//			// TODO Auto-generated method stub
//			return childMap.get(groupList.get(arg0)).get(arg1);
//		}
//
//		@Override
//		public long getChildId(int arg0, int arg1) {
//			// TODO Auto-generated method stub
//			return arg1;
//		}

//		@Override
//		public View getChildView(int groupPosition, int childPosition,
//				boolean isLastChild, View convertView, ViewGroup parent) {
//			// TODO Auto-generated method stub
//			chlidPicture chlidpic = null;
//			if (convertView == null) {
//				chlidpic = new chlidPicture();
//				convertView = mlayoutInflater.inflate(
//						R.layout.sdcard_video_child_item, null);
//				chlidpic.chlidPic = (ImageView) convertView
//						.findViewById(R.id.img_tip);
//				chlidpic.chlidText = (TextView) convertView
//						.findViewById(R.id.tv_name);
//				chlidpic.chlidText_type = (TextView) convertView
//						.findViewById(R.id.tv_type);
//				convertView.setTag(chlidpic);
//			} else {
//				chlidpic = (chlidPicture) convertView.getTag();
//			}
//			// 20140505152128_110.h264
//			PlayBackBean bean = childMap.get(groupList.get(groupPosition)).get(
//					childPosition);
//			// String groupDate =
//			// childMap.get(groupList.get(groupPosition)).get(
//			// childPosition);
//			String groupDate = bean.getPath();
//			chlidpic.chlidText.setText(getTime(groupDate));
//			int type = getModel(getTip(groupDate));
//			if (type == 2) {
//				chlidpic.chlidText_type
//						.setText(getString(R.string.sdcard_video_move));
//				chlidpic.chlidPic.setBackgroundResource(R.drawable.icon_motion);
//			} else if (type == 3) {
//				chlidpic.chlidText_type
//						.setText(getString(R.string.sdcard_video_gpio));
//				chlidpic.chlidPic.setBackgroundResource(R.drawable.icon_gpio);
//			} else if (type == 4) {
//				chlidpic.chlidText_type
//						.setText(getString(R.string.sdcard_video_sensor));
//				chlidpic.chlidPic.setBackgroundResource(R.drawable.icon_sensor);
//			} else {
//				chlidpic.chlidText_type
//						.setText(getString(R.string.sdcard_video_time));
//				chlidpic.chlidPic.setBackgroundResource(R.drawable.icon_rec);
//			}
//			Log.i("info", "date：" + groupDate);
//
//			return convertView;
//
//		}

		public String getTip(String xx) {
			int x1 = xx.indexOf("_") + 1;
			int x2 = xx.indexOf(".");
			String xxx = xx.substring(x1, x2);
			return xxx;
		}

		public int getModel(String mess) {
			// String m = mess.substring(mess.length() - 1, mess.length());
			// String n = mess.substring(1, 2);
			// String j = mess.substring(0, 1);

			String a1 = mess.substring(1, 2);
			String a2 = mess.substring(2, 3);
			String a3 = "0";
			if (mess.length() > 3) {
				a3 = mess.substring(3, 4);
			}
			int c = 1;// time
			if (a1.equals("1") && a2.equals("1")) {
				c = 3;// gpio
			}
			if (a1.equals("1") && a2.equals("0")) {
				c = 2;// move
			}
			if (a3.equals("1")) {
				c = 4;// sensor
			}

			return c;

			// if (m.equals("1")) {
			// return "a";
			// } else if (n.equals("1") && m.equals("0")) {
			// return "b";
			// } else if (n.equals("1") && m.equals("1")) {
			// return "b";
			// } else {
			// return "c";
			// }
		}

		public String getTime(String time) {
			String mess = time.substring(0, 14);
			String year = mess.substring(0, 4);
			String mouth = mess.substring(4, 6);
			String day = mess.substring(6, 8);

			String hour = mess.substring(8, 10);
			String min = mess.substring(10, 12);
			String sec = mess.substring(12, 14);

			return year + "-" + mouth + "-" + day + " " + hour + ":" + min
					+ ":" + sec;
		}

//		@Override
//		public int getChildrenCount(int arg0) {
//			// TODO Auto-generated method stub
//			return childMap.get(groupList.get(arg0)).size();
//		}
//
//		@Override
//		public Object getGroup(int arg0) {
//			// TODO Auto-generated method stub
//			return groupList.get(arg0);
//		}
//
//		@Override
//		public int getGroupCount() {
//			// TODO Auto-generated method stub
//			return groupList.size();
//		}
//
//		@Override
//		public long getGroupId(int arg0) {
//			// TODO Auto-generated method stub
//			return arg0;
//		}
//
//		@Override
//		public View getGroupView(int groupPosition, boolean isExpanded,
//				View convertView, ViewGroup parent) {
//			// TODO Auto-generated method stub
//			convertView = mlayoutInflater.inflate(
//					R.layout.sdcard_video_parent_item, null);
//			TextView textParent = (TextView) convertView
//					.findViewById(R.id.textparent);
//			ImageView img = (ImageView) convertView.findViewById(R.id.image);
//			String text = groupList.get(groupPosition);
//			String t1 = text.substring(0, 4);
//			String t2 = text.substring(4, 6);
//			String t3 = text.substring(6);
//			textParent.setText(getResources().getString(R.string.alarmtime) + t1 + "-" + t2 + "-" + t3);
//			if (isExpanded) {
//				img.setImageResource(R.drawable.expanded);
//			} else {
//				img.setImageResource(R.drawable.collapse);
//			}
//
//			return convertView;
//		}
//
//		@Override
//		public boolean hasStableIds() {
//			// TODO Auto-generated method stub
//			return false;
//		}
//
//		@Override
//		public boolean isChildSelectable(int arg0, int arg1) {
//			// TODO Auto-generated method stub
//			return true;
//		}
//
//	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mBridgeService.removeServiceStub(mServiceStub);
//		mBridgeService.unbindSetNull("SettingWifiActivity");
		unbindService(mConn);
	}
	/**
	 * BridgeService callback
	 * 
	 * recordcount 总数量 pagecount 可分成的页数 pageindex 当前页的下标 pagesize 当前页的数量
	 * **/

//	public void callBackRecordFileSearchResult(String did, String filename,
//			int size, int recordcount, int pagecount, int pageindex,
//			int pagesize, int bEnd) {
//		Log.d("tag", "CallBack_RecordFileSearchResult did: " + did
//				+ " filename: " + filename + " size: " + size
//				+ " recordcount :" + recordcount + "pagecount: " + pagecount
//				+ "pageindex:" + pageindex + "pagesize: " + pagesize + "bEnd:"
//				+ bEnd);
//		if (strDID.equals(did)) {
//
//			fileTFCount = recordcount;
//			getCurrentPageIndex = pageindex;
//			TotalPageSize = pagecount;
//
//			PlayBackBean bean = new PlayBackBean();
//			bean.setDid(did);
//			bean.setPath(filename);
//			// mAdapter.addPlayBean(bean);
//
//			lists.add(bean);
//
//			if (bEnd == 1) {
//				mHandler.sendEmptyMessage(PARAMS);
//			}
//		}
//	}
	private int mNextStartIndex = 0;
	private final int REQ_NUM_MAX = 30;
	private void getPageFileList(String path){
		int reqnum;
		Log.d(TAG,"mRequestNeededCount:" + mRequestNeededCount);
		if(mRequestNeededCount<=0){
			if(mSearchDir == mCurrentDir && mCurListPageBeanSearchIndex<mListPageBean.size()){
				ListPageBean lpb = mListPageBean.get(mCurListPageBeanSearchIndex++);
				mCurrentDir.name = lpb.path;
				mCurrentDir.path = lpb.path;
				Log.e(TAG,"path:" + lpb.path);
				//start the other search.
				Cmds.ListRemoteDirInfo(mServiceStub,strDID,lpb.path,mSensorIndex,0,lpb.start,lpb.end);
			}else{
				if(progressDialog.isShowing()){
					progressDialog.dismiss();
				}
			}
			return;
		}

		if(mRequestNeededCount >REQ_NUM_MAX){
			mRequestNeededCount -= REQ_NUM_MAX;
			reqnum = REQ_NUM_MAX;
		}else{
			reqnum = mRequestNeededCount;
			mRequestNeededCount = 0;
		}
		
		String listCmd = "{\"lp\":{\"p\":\""+path+"\",\"s\":" + mNextStartIndex
							+ ",\"c\":" + reqnum +"}}";
		mNextStartIndex += reqnum;
		Cmds.ListRemotePageFile(mServiceStub,strDID,listCmd);
	}
	private void setFileCount(int c){
		file_count_tv.setText(PlayBackTFActivity.this.getString(R.string.sum_pic) + c + 
				PlayBackTFActivity.this.getString(R.string.video_sum));
	}
	
	private void setDiskInfo(int total,int used){
		disk_capacity_tv.setText(used + "M/" + total + "M");
		disk_capacity_info_pb.setMax(total);
		disk_capacity_info_pb.setProgress(used);
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
			case ContentCommon.IPC_AV_RECO_CONF_GET_RESP:{
				if(mIPCNetRecordCfg_st.parseJSON(jsonData)){
					mRootDir.name = mRootDir.path = mIPCNetRecordCfg_st.DiskInfo.Path;
					mCameraParamsBean.mStorageRootDir = mIPCNetRecordCfg_st.DiskInfo.Path;
					mCurrentDir = mRootDir;
					mAdapter.setRootDir(mIPCNetRecordCfg_st.DiskInfo.Path);
					Log.d(TAG,"Change Storage RootDir To:" + mRootDir.path);
					Cmds.ListRemoteDirInfo(mServiceStub,strDID,mCameraParamsBean.mStorageRootDir,mSensorIndex,1,0,240000);
				}
			}break;
			case ContentCommon.IPC_AV_RECO_LIST_GET_RESP:
				JSONObject listinfo;
				try {
					listinfo = jsonData.getJSONObject("li");
					if(listinfo!=null){
						int ln = listinfo.getInt("n");
						int total = listinfo.getInt("t");
						int used = total - listinfo.getInt("u");
						//int page = listinfo.getInt("p");
						//Log.d(TAG,"Total:" + ln + " Page:" + page);
						Log.d(TAG,"Total:" + ln);
						mRequestNeededCount = ln;
						mNextStartIndex = 0;
						
						if(mSearchDir == mCurrentDir){
							setFileCount(mCurrentDir.list.size() + mRequestNeededCount);
						}else{
							setFileCount(mRequestNeededCount);
						}

						getPageFileList(mCurrentDir.path);
						setDiskInfo(mIPCNetRecordCfg_st.DiskInfo.Total,mIPCNetRecordCfg_st.DiskInfo.Free);
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				break;
			case ContentCommon.IPC_AV_RECO_LIST_PAGE_GET_RESP:
				try {
					//String netname= jsonData.getString("Name");
					//if(netname.contentEquals("NetWork.Wireless")){
						JSONObject dir= jsonData.getJSONObject("dir");
						if(dir!=null){
							JSONArray list = dir.getJSONArray("l");
							int i = 0;
							String ln = dir.getString("n");
							//FileItem fi = getFileItemByName(mRootDir,ln);
//							Log.d(TAG,"get:" + ln);
							FileItem fi;
							if(mSearchDir == mCurrentDir){
								fi = mCurrentDir;
							}else 
							if(mCurrentDir.name.contentEquals(ln)){
								fi = mCurrentDir;
							}else{
								fi = getFileItemByPath(mRootDir,ln);
								if(fi == null){
									break;
								}
							}
							Log.e(TAG,"path:" + mCurrentDir.name);
							for(;i<list.length();i++){
								JSONObject target = (JSONObject) list.getJSONObject(i);
								int type = target.getInt("t");
								String name = target.getString("n");
								long size=0;
								if(type!=4){
									size = (long)target.getInt("sl");
									size |= ((long)target.getInt("sh"))<<32;
								}
//								Log.d(TAG,"type:" + type + " name:" + name);
								
								FileItem nfi = new FileItem();
								nfi.type = type;
								nfi.name = name;
								nfi.size = size;
//								Log.d(TAG,"1type:" + type + " parent path:" + fi.path);
								nfi.path = fi.path + "/" + name;
//								Log.d(TAG,"2type:" + type + " name:" + name);
								nfi.parent = fi;
								if(fi.list == null){
									fi.list = new ArrayList<FileItem>();
								}
//								Log.d(TAG,"3type:" + type + " path:" + nfi.path);
								fi.list.add(nfi);
							}
							//if(mCurrentDir.path.contentEquals(ln)){
							//	mCurrentDir = fi;
							mAdapter.setFileItemList(fi.list);
							//}
//							expandlistview.setVisibility(View.VISIBLE);
							Log.d(TAG,"AV_RECO_LIST_GET_RESP ------------- >");
							mAdapter.notifyDataSetChanged();
							
							getPageFileList(mCurrentDir.path);
							break;
						}
//					}else if(netname.contentEquals("NetWork.WirelessSearch")){
//					Log.e(TAG,"NetWork.WirelessSearch recieve!");
//					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				break;
			case ContentCommon.IPC_AV_RECO_OP_REQ:
				try {
					int ret= jsonData.getInt("ret");
					Log.d(TAG,"JIANLE_NETWORK_WIFI_SET_RESP:" + ret);
					if(progressDialog.isShowing()){
						progressDialog.dismiss();
					}
				JSONObject wifi= jsonData.getJSONObject("NetWork.Wireless");
				if(wifi!=null){
					break;
				}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};
	
	private FileItem getFileItemByName(FileItem dir,String name){
		if(dir != null && dir.name.contentEquals(name)){
			return dir;
		}
		
		if(dir.type == 4 && dir.list != null){
			for(FileItem fi:dir.list){
				if(fi.name.contentEquals(name)){
					return fi;
				}
			}
		}
		return null;
	}
	private FileItem getFileItemByPath(FileItem dir,String path){
		if(dir != null && dir.path.contentEquals(path)){
			return dir;
		}
		
		if(dir.type == 4 && dir.list != null){
			for(FileItem fi:dir.list){
				if(fi.path.contentEquals(path)){
					return fi;
				}
				if(fi.type == 4 && fi.list != null){
					FileItem retfi = getFileItemByPath(fi,path);
					if(retfi!=null)
						return retfi;
				}
			}
		}
		return null;
	}
	
	private class ListPageBean{
		public String path;
		public int start;
		public int end;
	}
}
