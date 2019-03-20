package freesbell.demo.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import freesbell.demo.client.BridgeService;

public class CameraParamsBean implements Serializable{
 public String did;
 public String user;
 public String pwd;
 public String name;
 public int gpio_num = 0;
 public int status;
 public int devType;
 public int p2p_mode;
 public boolean isLogining = false;
 public boolean isUpdateing = false;
 public int mLoginTryTimes = 0;
 public String mDevVer = "0.0.0.0.1";
 public int mVideoNum,mPicNum,mAlarmNum;
 public String mStorageRootDir = "/mnt/s0/media/sensor0";
 private BridgeService mBridgeService;
 public CameraParamsBean(BridgeService bs){
	 mVideoNum = -1;
	 mPicNum = -1;
	 mAlarmNum = -1;
	 mBridgeService = bs;
 }
 
 public void updateCameraInfo(boolean force){
	 if(mBridgeService !=null){
		 if(mVideoNum<0 || force){
			 mVideoNum = mBridgeService.getVideoCnt(did);
		 }
		 if(mPicNum<0 || force){
			 mPicNum = mBridgeService.getPicCnt(did);
		 }
		 //mAlarmNum = mBridgeService.get;
	 }
 }
 private ArrayList<Map<String, Object>> mSensorList = null;
 
	public ArrayList<Map<String, Object>> getSensorList(){
		return mSensorList;
	}
	public void setSensorList(ArrayList<Map<String, Object>> sl){
		mSensorList = sl;
	}
public int getP2PMode(){
	return p2p_mode;
}
public void setP2PMode(int mode){
	p2p_mode = mode;
}
public int getStatus() {
	return status;
}
public void setStatus(int status) {
	this.status = status;
}
public String getDid() {
	return did;
}
public void setDid(String did) {
	this.did = did;
}
public String getUser() {
	return user;
}
public void setUser(String user) {
	this.user = user;
}
public String getPwd() {
	return pwd;
}
public void setPwd(String pwd) {
	this.pwd = pwd;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
@Override
public String toString() {
	return "CameraParamsBean [did=" + did + ", user=" + user + ", pwd=" + pwd
			+ ", name=" + name + "]";
}

public HwResBean getHwRes(int mHwId) {
	// TODO Auto-generated method stub
	return null;
}

public HwResBean getHwResByOrder(int i) {
	// TODO Auto-generated method stub
	return null;
}

public int getHwResNum() {
	// TODO Auto-generated method stub
	return 0;
}

public int getHwResListNum() {
	// TODO Auto-generated method stub
	return 0;
}

public void addHwRes(HwResBean hrb) {
	// TODO Auto-generated method stub
	
}
 
}
