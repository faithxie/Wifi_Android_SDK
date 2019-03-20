package freesbell.demo.client;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import freesbell.demo.client.R;
import fenzhi.nativecaller.NativeCaller;
import freesbell.demo.bean.CameraParamsBean;
import freesbell.demo.bean.JSONStructProtocal;
import freesbell.demo.bean.JSONStructProtocal.IPCNetAlarmMsgReport_st;
import freesbell.demo.content.ContentCommon;
import freesbell.demo.utils.Cmds;
import freesbell.demo.utils.DatabaseUtil;
import freesbell.demo.utils.ServiceStub;
import freesbell.demo.utils.VideoAudioInterface;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * 
 * @author ????
 * @date 2012-02-28
 * **/
public class BridgeService extends Service {
	private static String TAG = BridgeService.class.getSimpleName();
	
	public static BridgeService mSelf = null;
	private AddDeviceActivity addDeviceActivity;
	private VideoAudioInterface mVideoAudioInterface;
	private PlayBackActivity playBackActivity;
	private MainActivity mMainActivity;
	private TestSnapshot mTestSnapshot;

//	private NotificationManager ntfManager;
	private Notification mNotify2;
	private DatabaseUtil camDBbUtil = null;
	private NotificationManager mCustomMgr;
	private boolean mHWDecode = false;
	
	private List<ServiceStub> mServiceStubList;
	
	public ArrayList<CameraParamsBean> mCameraArrayList = new ArrayList<CameraParamsBean>();
	private JSONStructProtocal mJSONStructProtocal = new JSONStructProtocal();
	private IPCNetAlarmMsgReport_st mIPCNetAlarmMsgReport_st = mJSONStructProtocal.new IPCNetAlarmMsgReport_st();

	public void unbindSetNull(ServiceStub stub){
		mServiceStubList.remove(stub);
	}
	public void unbindSetNull(String activityName) {
		if ("AddDeviceActivity".equals(activityName)) {
			addDeviceActivity = null;
		}else if ("MainActivity".equals(activityName)) {
			mMainActivity = null;
		}else if("CameraViewerActivity".equals(activityName)){
			mVideoAudioInterface = null;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return new ControllerBinder();
	}

	/**
     * 
     * **/
	public class ControllerBinder extends Binder {
		public BridgeService getBridgeService() {
			return BridgeService.this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate()");
		mSelf = this;
		
		mServiceStubList = new ArrayList<ServiceStub>();
		
		mCustomMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		camDBbUtil = new DatabaseUtil(this);
		NativeCaller.P2PSetCallbackContext(this);

        SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(this);  
        mHWDecode = shp.getBoolean("hw_decode", false);
        
		getCameraList();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("tag", "BridgeService onDestroy()");
		mCustomMgr.cancel(R.drawable.app);
		mSelf = null;
	}

	private static ServiceStub mRootStub = null;
	public static void setRootStub(ServiceStub stub){
		mRootStub = stub;
	}
	public static ServiceStub getRootStub(){
		return mRootStub;
	}
	public void getCameraSensorList(CameraParamsBean cpb){
		ArrayList<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		
		for(int i=0;i<16;i++){
			Map<String, Object> map = new HashMap<String, Object>(); 
			map.put(ContentCommon.STR_CAMERA_SNAPSHOT,R.drawable.category_icon_camera);
			map.put(ContentCommon.STR_CAMERA_NAME, "Senor" + i);
			map.put(ContentCommon.STR_CAMERA_ID, cpb.getDid());	
			
			synchronized(this){
				listItems.add(map);
			}
		}
		
		cpb.setSensorList(listItems);
	}
	public ArrayList<CameraParamsBean> getCameraList() {
		if(mCameraArrayList == null)
			return null;
		mCameraArrayList.clear();
		camDBbUtil.open();
		Cursor cursor = camDBbUtil.fetchAllCameras();
		if (cursor != null) {
			while (cursor.moveToNext()) {
				CameraParamsBean cpb = new CameraParamsBean(this);
				cpb.setName(cursor.getString(1));
				cpb.setDid(cursor.getString(2));//String did = cursor.getString(2);
				cpb.setUser(cursor.getString(3));//String user = cursor.getString(3);
				cpb.setPwd(cursor.getString(4));//String pwd = cursor.getString(4);
				cpb.setStatus(ContentCommon.P2P_STATUS_UNKNOWN);
				
				if(getCamera(cpb.getDid()) == null)
					mCameraArrayList.add(cpb);
			}
		}
		camDBbUtil.close();
		
		return mCameraArrayList;
	}
	
	public static final int UPDATE_UI_ALL = 0;
	public static final int UPDATE_UI_IMG = 1;
	public static final int UPDATE_UI_STATUS = 1;
	
	public CameraParamsBean getCamera(String uuid) {
		for(CameraParamsBean cpb:mCameraArrayList){
			if(cpb.getDid().contentEquals(uuid))
				return cpb;
		}
		return null;
	}
	
	static public CameraParamsBean getCameraBean(String uuid) {
		if(BridgeService.mSelf == null)
			return null;
		return BridgeService.mSelf.getCamera(uuid);
	}
	public void addCamera(String name, String did, String user, String pwd){
		CameraParamsBean cpb = new CameraParamsBean(this);
		cpb.setName(name);
		cpb.setDid(did);//String did = cursor.getString(2);
		cpb.setUser(user);//String user = cursor.getString(3);
		cpb.setPwd(pwd);//String pwd = cursor.getString(4);
		cpb.setStatus(ContentCommon.P2P_STATUS_UNKNOWN);
		mCameraArrayList.add(cpb);
		
		camDBbUtil.open();
		camDBbUtil.createCamera(name, did, user, pwd);
		camDBbUtil.close();
	}
	public boolean updateCameraStatus(String uuid,int status){
		Log.d(TAG,"updateCameraStatus:" + uuid + " status:" + status);
		for(int i=0;i<mCameraArrayList.size();i++){
			CameraParamsBean cpb = mCameraArrayList.get(i);
			if(cpb.getDid().contentEquals(uuid)){
				if(cpb.status == status){
					return false;
				}
				cpb.setStatus(status);
				return true;
			}
		}
		return false;
	}

	public boolean UpdataCamera2db(String oldDID, String name, String did,
			String user, String pwd) {
		boolean bRes = false;
		camDBbUtil.open();
		if (camDBbUtil.updateCamera(oldDID, name, did, user, pwd)) {
			bRes = true;
		}
		camDBbUtil.close();
		
		for(int i=0;i<mCameraArrayList.size();i++){
			CameraParamsBean cpb = mCameraArrayList.get(i);
			if(cpb.getDid().contentEquals(oldDID)){
				cpb.setDid(did);
				break;
			}
		}

		return bRes;
	}
	
	public int getPicCnt(String uuid){
		int cnt;
		camDBbUtil.open();
		Cursor cursor = camDBbUtil.queryAllPicture(uuid);
		cnt = cursor.getCount();
		camDBbUtil.close();
		return cnt;
	}
	public int getVideoCnt(String uuid){
		int cnt;
		camDBbUtil.open();
		Cursor cursor = camDBbUtil.queryAllVideo(uuid);
		cnt = cursor.getCount();
		camDBbUtil.close();
		return cnt;
	}
	
	public void createVideoOrPic(String uuid,String type,String path,String strDate){
		if(uuid==null||type==null||path==null||strDate==null)
			return;

		camDBbUtil.open();
		camDBbUtil.createVideoOrPic(uuid, path,
				type, strDate);
		camDBbUtil.close();
		
		for(int i=0;i<mCameraArrayList.size();i++){
			CameraParamsBean cpb = mCameraArrayList.get(i);
			if(cpb.getDid().contentEquals(uuid)){
				if(type.contentEquals(DatabaseUtil.TYPE_PICTURE)){
					cpb.mPicNum++;
				}else if(type.contentEquals(DatabaseUtil.TYPE_VIDEO)){
					cpb.mVideoNum++;
				}
				break;
			}
		}
	}
	
	public boolean checkHaveVideoInDb(String did) {
		camDBbUtil.open();
		Cursor cursor = camDBbUtil.queryAllVideo(did);
		if (cursor.getCount() > 0) {
			return true;
		}
		return false;
	}
	
	private boolean delCameraFromdb(String did) {
		camDBbUtil.open();
		//Log.e(TAG,"delCameraFromdb:"+did);
		boolean bRes = camDBbUtil.deleteCamera(did);
		camDBbUtil.close();
		return bRes;
	}
	
	public void deleteCamera(String did) {
		//Log.e(TAG,"deleteCamera:"+did);
		for(int i=0;i<mCameraArrayList.size();i++){
			CameraParamsBean cpb = mCameraArrayList.get(i);
			if(cpb.getDid().contentEquals(did)){
				mCameraArrayList.remove(i);
				break;
			}
		}
		
		if(delCameraFromdb(did)){
			NativeCaller.StopP2P(did);
		}
	 }
	
	public void closeCameraDB(){
		camDBbUtil.close();
	}

	public boolean checkHavePicInDb(String did) {
		camDBbUtil.open();
		Cursor cursor = camDBbUtil.queryAllPicture(did);
		if (cursor.getCount() > 0) {
			return true;
		}
		return false;
	}
	public boolean getDecodeMode(){
        return mHWDecode;
	}
	public void setDecodeMode(boolean hw_decode){
		if(hw_decode!=mHWDecode){
			SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(this);  
			Editor ed = shp.edit();
			ed.putBoolean("hw_decode", hw_decode);
			ed.commit();
	        mHWDecode = hw_decode;
		}
	}
	
	public int startMultiLivestream(VideoAudioInterface playActivity_four,
			final ArrayList<String> list, final int streamid,
			final int substreamid) {
		mVideoAudioInterface = playActivity_four;
		for (int i = 0; i < list.size(); i++) {
			final String mess = list.get(i);
			new Thread(new Runnable() {
				@Override
				public void run() {
					Log.d(TAG,"StartPPPPLivestream:"+mess+" streamid:" + streamid + " substreamid:" + substreamid);
					NativeCaller.StartP2PLivestream(mess, streamid,
							substreamid);

				}
			}).start();
		}

		return 1;
	}
	public void setLivestreamReciver(VideoAudioInterface cva)
	{
		mVideoAudioInterface = cva;
	}
	
	/**
	 * SearchActivity
	 * */
	public void startSearch(AddDeviceActivity addCameraActivity) {
		Log.d(TAG, "startSearch()");
		this.addDeviceActivity = addCameraActivity;
		new Thread(new SearchThread()).start();
	}

	public void startSearch(boolean flag) {
//		isMainSearchCamera = flag;
		new Thread(new SearchThread()).start();
	}

	class SearchThread implements Runnable {
		@Override
		public void run() {
			NativeCaller.StartSearch(/* BridgeService.this */);
		}
	}

	public void MainActivityInitNativeCaller(MainActivity ma){
		mMainActivity = ma;
		NativeCaller.Init(/* BridgeService.this */);
	}

	public void setPlayBackVideo(PlayBackActivity playBackActivity) {
		this.playBackActivity = playBackActivity;
	}

	//call by jni.
	public void VideoData(String did, byte[] videobuf, int h264Data, int len,
			int width, int height, int timestamp, short milistamp, int sessid,
			int version, int originFrameLen) {
//		Log.d(TAG, "BridgeService Call VideoData...h264Data: " + h264Data
//				+ " len: " + len + " videobuf len: " + len + "width: " + width
//				+ "height: " + height + "--time:" + timestamp + "--videobuf"
//				+ videobuf);
		if(mVideoAudioInterface != null){
			mVideoAudioInterface.setVideoData(did, videobuf, h264Data, len, width,
					height);
		}
	}

	/**
	 * PlayActivity feedback method
	 * 
	 * AudioData
	 * 
	 * @param pcm
	 * @param len
	 */
	public void AudioData(byte[] pcm, int len) {
		Log.d("info", "AudioData: len :+ " + len + "---pcm:" + pcm);
		if(mVideoAudioInterface != null)
			mVideoAudioInterface.setAudioData(pcm, len);
	}

	/**
	 * 
	 * p2p connection status notify
	 * 
	 * @param msgtype
	 * @param param
	 */

	public void StatusMsgNotify(String did, int type, int param) {
		Log.d(TAG, "did:" + did + " type:" + type + " param:" + param);
		
		if (mMainActivity != null) {
			mMainActivity.setStatusMsgNotify(did, type, param);
		}
	}

	/***
	 * SearchActivity feedback method
	 * 
	 * **/
	public void SearchResult(int cameraType, String strMac, String strName,
			String strDeviceID, String strIpAddr, int port, int devType) {
		Log.d(TAG, "SearchResult: " + strIpAddr + " " + port);
		if (strDeviceID.length() == 0) {
			return;
		}

		if (addDeviceActivity != null) {
			addDeviceActivity.setSearchResultData(cameraType, strMac, strName,
					strDeviceID, strIpAddr, port,devType);
		}
	}

	public void setSnapshotCallback(TestSnapshot ts){
		mTestSnapshot = ts;
	}
	//deprecated,we can get snapshot from h264 video stream.
	// 抓图回调
	public void CallBack_Snapshot(String did,int type, byte[] data, int len,long timestamp) {
		Log.d(TAG, "CallBack_Snapshot did:" + did + " data len:" + data.length + " len:" + len);
		if (mTestSnapshot != null) {
			mTestSnapshot.setPPPPSnapshotNotify(did, data, len);
		}
	}
	
	public void CallBack_DownloadFileData(String did,byte[]data,int offset)
	{
		
	}

	public void CallBack_PlaybackVideoData(String did, byte[] videobuf,
			int h264Data, int len, int width, int height, int time,
			int Streamid, int FrameType, int originFrameLen) {
		Log.d("info", "---playback  len:" + len + " width:" + width
				+ " height:" + height + " h264Data:" + h264Data + " time:" + time);
		if(FrameType == 100){
			System.out.println("==frameType =100");
			return;
		}
		if (FrameType != 6 && FrameType != 100) {
			if (playBackActivity != null) {
				playBackActivity.CallBack_PlaybackVideoData(videobuf, h264Data,
						len, width, height,time);
			}
		}
		
	}

	/**
	 * H264 video stream call back by jni,use for video record and hardware decode.
	 * */
	public void CallBack_H264Data(String did, byte[] h264, int type, int size,
			int timestamp, short milistamp, int width, int height) {
		
//		Log.d(TAG, "CallBack_H264Data----type:"+type + " width:" + width + " height:" + height);
		if(mVideoAudioInterface != null){
			mVideoAudioInterface.callBackH264Data(did,h264, type, size,width, height);
		}

	}

//	public void CallBackOriFramLen(String did,int originFrameLen){
//	//System.out.println("====originFrameLen PlayBack="+originFrameLen);	
//	}
	
	public void CallBack_PlaybackAudioData(String did, byte[] videobuf,
			int len,int time,int Streamid, int FrameType, int originFrameLen) {
	}
	
	public void JavaCallBackProcessCommand(String uuid,int msg,byte[] data,int data_len,long timestamp){
		String datastr = null;
		Log.i(TAG,"MSG:" + msg + "  len:" + data_len);
		try {
			//datastr = new String(data,"ISO-8859-1");
			datastr = new String(data,"UTF-8");
//			Log.d(TAG,"Response:" + datastr);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		};
		
		switch(msg){
		case ContentCommon.IPCNET_MCU_INFO_RESP:{
			for(ServiceStub serstub:mServiceStubList){
				if(serstub != null){
					serstub.setRecievedMessage(uuid, msg, data);
				}
			}
		}return;
		case ContentCommon.IPCNET_ALARM_REPORT_RESP:{
			//devinfo,json str
			JSONObject jsonData = null;
			try {
				jsonData = new JSONObject(datastr);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Log.d(TAG,"ContentCommon.IPCNET_ALARM_REPORT_RESP");
			if(jsonData!=null && mIPCNetAlarmMsgReport_st.parseJSON(jsonData)){
				switch(mIPCNetAlarmMsgReport_st.AlarmType){
				case ContentCommon.IPCNET_ALARM_IO_INPUT://IO输入报警
					String strGpioAlarm = getResources().getString(
							R.string.alarm_gpio_alarm);
					break;
				case ContentCommon.IPCNET_ALARM_LOST_VIDEO://视频丢失报警
					break;
				case ContentCommon.IPCNET_ALARM_MOVE_DETECT:
					String strMotionAlarm = getResources().getString(
							R.string.alarm_motion_alarm);
					break;
				case ContentCommon.IPCNET_ALARM_TAKE_PIC:{
					//send broadcast to take picture.
					Intent intent = new Intent(ContentCommon.CAMERA_INTENT_REMOTE_DEV_CONTROL);
					intent.putExtra(ContentCommon.STR_CAMERA_CONTROL_APP_ACTION, ContentCommon.STR_CAMERA_CONTROL_APP_ACTION_TAKE_PIC);
					sendBroadcast(intent);
				}break;
				case ContentCommon.IPCNET_ALARM_TAKE_VIDEO:{
					//send broadcast to operate video.
					Intent intent = new Intent(ContentCommon.CAMERA_INTENT_REMOTE_DEV_CONTROL);
					intent.putExtra(ContentCommon.STR_CAMERA_CONTROL_APP_ACTION, ContentCommon.STR_CAMERA_CONTROL_APP_ACTION_TAKE_VIDEO);
					intent.putExtra(ContentCommon.STR_CAMERA_APP_ACTION_VAL, mIPCNetAlarmMsgReport_st.Val);
					sendBroadcast(intent);
				}break;
				}
			}
		}break;
		}
		
		for(ServiceStub serstub:mServiceStubList){
			if(serstub != null){
				serstub.setRecievedMessage(uuid, msg, datastr);
			}
		}
	}

	public void setServiceStub(ServiceStub ss){
		for(ServiceStub serstub:mServiceStubList){
			if(serstub == ss)
				return;
		}
		mServiceStubList.add(ss);
	}
	public void removeServiceStub(ServiceStub ss){
		for(ServiceStub serstub:mServiceStubList){
			if(serstub == ss)
				mServiceStubList.remove(ss);
		}
	}
	public File getRecordPath() {
		return Environment.getExternalStorageDirectory();
	}
	
	private String mvideopath = null;
	public void setVideoPath(String videopath) {
		mvideopath = videopath;
	}
	public String getVideoPath() {
		return mvideopath;
	}
}
