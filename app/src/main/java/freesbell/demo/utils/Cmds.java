package freesbell.demo.utils;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import fenzhi.nativecaller.NativeCaller;
import freesbell.demo.bean.DateBean;
import freesbell.demo.bean.HwResBean;
import freesbell.demo.bean.WifiBean;
import freesbell.demo.bean.JSONStructProtocal.IPCPtzCmd_st;
import freesbell.demo.content.ContentCommon;

public class Cmds {
	private final static String TAG = "Cmds";
//	typedef struct{
//		char workname[32];
//		int salary;
//	}work_t;
//
//	typedef struct{
//		char name[32];
//		int num;
//		char height;
//		work_t work;
//	}member_t;
	
//member_t
//	{
//  "name": "dog",
//  "num":  6,
//  "height":       -86,
//  "work": {
//          "work_t":       {
//                  "workname":     [109, 105, 110, 105, 115, 116, 101, 114, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
//                  "salary":       -1294868531
//          }
//  }
//}
	public static int Login(String user,String passwd,String uuid){
		String json = null;
		//build MSG_LOGIN_t
//		typedef struct MSG_LOGIN
//		{
//		    T_S8 user[SEPCAM_MOST_STR_VAR_LEN];//用户名称
//		    T_S8 passwd[SEPCAM_MOST_STR_VAR_LEN];//md5加密后的密码
//		    T_U32 interval;//心跳间隔时间
//		}MSG_LOGIN_t;
		JSONObject MSG_LOGIN_t = new JSONObject(); 
		try {
			MSG_LOGIN_t.put("user", "jerry");
			MSG_LOGIN_t.put("passwd", "123456");
			MSG_LOGIN_t.put("interval", 10);
			json = MSG_LOGIN_t.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(json == null)
			return -1;
		//then send json string to device.
		NativeCaller.sendJsonCmd(uuid ,ContentCommon.REMOTE_MSG_LOGIN, json);
		return 0;
	}
	
	public static int requestVideoStreamDirectly(ServiceStub stub,String uuid,int viChannel,boolean reqMaster,int stream_type,boolean enable){
		String json = null;
		//build REQUEST_STREAM_CMD_t

		JSONObject REQUEST_STREAM_CMD_t = new JSONObject();
		try {
			JSONObject REQUEST_STREAM_CMD_t_entity = new JSONObject();
			REQUEST_STREAM_CMD_t_entity.put("channel",viChannel);
			REQUEST_STREAM_CMD_t_entity.put("flag",reqMaster?0:1);
			REQUEST_STREAM_CMD_t_entity.put("stream_type",stream_type);
			REQUEST_STREAM_CMD_t_entity.put("cmd_type",enable?1:2);
			REQUEST_STREAM_CMD_t_entity.put("reserve",0);
			
			REQUEST_STREAM_CMD_t.put("REQUEST_STREAM_CMD_t",REQUEST_STREAM_CMD_t_entity);
			json = REQUEST_STREAM_CMD_t.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		if(json == null)
			return -1;
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.REMOTE_MSG_REQ_STREAM, json);
	}
	
	public static int PPPPPTZControl(String uuid,int cmd){
		switch(cmd){
		case ContentCommon.CMD_PTZ_UP_DOWN_STOP:
			break;
		case ContentCommon.CMD_PTZ_UP_DOWN:
			break;
		case ContentCommon.CMD_PTZ_LEFT_RIGHT_STOP:
			break;
		case ContentCommon.CMD_PTZ_LEFT_RIGHT:
			break;
		case ContentCommon.CMD_PTZ_UP:
			break;
		case ContentCommon.CMD_PTZ_UP_STOP:
			break;
		case ContentCommon.CMD_PTZ_DOWN:
			break;
		case ContentCommon.CMD_PTZ_DOWN_STOP:
			break;
		case ContentCommon.CMD_PTZ_LEFT:
			break;
		case ContentCommon.CMD_PTZ_LEFT_STOP:
			break;
		case ContentCommon.CMD_PTZ_RIGHT:
			break;
		case ContentCommon.CMD_PTZ_RIGHT_STOP:
			break;
		}
		return 0;
	}
	
	public static int PPPPCameraControl(ServiceStub stub,String uuid,int vi,boolean hori_mirror,boolean vert_mirror){
		String json = null;
		JSONObject IPCNetPicOverTurnReq_st = new JSONObject();
		try {
			JSONObject IPCNetPicOverTurn_st = new JSONObject();
			IPCNetPicOverTurn_st.put("ViCh", vi);
			IPCNetPicOverTurn_st.put("Mirror", hori_mirror);
			IPCNetPicOverTurn_st.put("Flip", vert_mirror);

			IPCNetPicOverTurnReq_st.put("IspOverTurn.info", IPCNetPicOverTurn_st);
			json = IPCNetPicOverTurnReq_st.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		if(json == null)
			return -1;
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_SET_OVERTURN_REQ, json);
	}
	
//	public static int P2PCameraPicCfg(ServiceStub stub,String uuid,int vi,int type,int val){
//		String json = null;
//		JSONObject IPCNetPicOverTurnReq_st = new JSONObject();
//		try {
//			JSONObject IPCNetPicOverTurn_st = new JSONObject();
//			IPCNetPicOverTurn_st.put("ViCh", vi);
//			IPCNetPicOverTurn_st.put("Type", type);
//			IPCNetPicOverTurn_st.put("Val", val);
//
//			IPCNetPicOverTurnReq_st.put("CamCfg.info", IPCNetPicOverTurn_st);
//			json = IPCNetPicOverTurnReq_st.toString();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		
//		if(json == null)
//			return -1;
//		//then send json string to device.
//		Log.d(TAG,json);
//		return stub.sendMessage(uuid ,ContentCommon.IPCNET_SET_CAM_PIC_CFG_REQ, json);
//	}
	
	public static int P2PCameraGetPicCfg(ServiceStub stub,String uuid,String json){
		if(json == null)
			return -1;
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_GET_CAM_PIC_CFG_REQ, json);
	}
	public static int P2PCameraPicCfg(ServiceStub stub,String uuid,String json){
		if(json == null)
			return -1;
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_SET_CAM_PIC_CFG_REQ, json);
	}
	
	public static int P2PCameraEnvSetting(ServiceStub stub,String uuid,int mode,int lightMode,int level){
		String json = null;
		JSONObject IPCNetEnvInfoReq_st = new JSONObject();
		try {
			JSONObject IPCNetEnvInfo_st = new JSONObject();
			IPCNetEnvInfo_st.put("Mode", mode);
			IPCNetEnvInfo_st.put("LightMode", lightMode);
			IPCNetEnvInfo_st.put("Level", level);

			IPCNetEnvInfoReq_st.put("IspExpEnv.info", IPCNetEnvInfo_st);
			json = IPCNetEnvInfoReq_st.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		if(json == null)
			return -1;
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_SET_ENVIRONMENT_REQ, json);
	}
	
	public static int P2PCamIRCutOp(ServiceStub stub,String uuid,int op,boolean on){
		String json = null;
		JSONObject IPCNetIRCutCfgReq_st = new JSONObject();
		try {
			JSONObject IPCNetIRCutCfg_st = new JSONObject();
			IPCNetIRCutCfg_st.put("IRCutVal", op);
			IPCNetIRCutCfg_st.put("SetOppose", on?1:0);

			IPCNetIRCutCfgReq_st.put("IRCutCfg.info", IPCNetIRCutCfg_st);
			json = IPCNetIRCutCfgReq_st.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		if(json == null)
			return -1;
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_SET_IRCUT_REQ, json);
	}
	
	public static int enableWifiConfig(ServiceStub stub,String uuid,boolean enable){
		String json = null;
		JSONObject JianleGenerlReq_st = new JSONObject();
		try {
			JSONObject JianleNetworkWirelessConfig_st = new JSONObject();
			JianleNetworkWirelessConfig_st.put("WirelessEnable", enable);

			JianleGenerlReq_st.put("NetWork.Wireless", JianleNetworkWirelessConfig_st);
			json = JianleGenerlReq_st.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		if(json == null)
			return -1;
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPC_NETWORK_WIFI_SET_REQ, json);
	}
//	public static int setWifiConfig(ServiceStub stub,String uuid,WifiBean wifiBean){
//		String json = null;
//		JSONObject JianleGenerlReq_st = new JSONObject();
//		try {
//			JSONObject JianleNetworkWirelessConfig_st = new JSONObject();
//			JianleNetworkWirelessConfig_st.put("WirelessEnable", wifiBean.isWiFiEnable());
//			JianleNetworkWirelessConfig_st.put("EncType", wifiBean.getEncrypt());
//			JianleNetworkWirelessConfig_st.put("SSID", wifiBean.getSsid());
//			JianleNetworkWirelessConfig_st.put("Password", wifiBean.getPasswd());
//			JianleNetworkWirelessConfig_st.put("DhcpEnble", wifiBean.isDHCPEnable());
//			JianleNetworkWirelessConfig_st.put("IP", wifiBean.getIP());
//			JianleNetworkWirelessConfig_st.put("Netmask", wifiBean.getNetmask());
//			JianleNetworkWirelessConfig_st.put("Getway", wifiBean.getGateway());
//
//			JianleGenerlReq_st.put("NetWork.Wireless", JianleNetworkWirelessConfig_st);
//			json = JianleGenerlReq_st.toString();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		
//		if(json == null)
//			return -1;
//		//then send json string to device.
//		Log.d(TAG,json);
//		return stub.sendMessage(uuid ,ContentCommon.IPC_NETWORK_WIFI_SET_REQ, json);
//	}
	public static int setWifiConfig(ServiceStub stub,String uuid,String json){
		if(json == null)
			return -1;
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPC_NETWORK_WIFI_SET_REQ, json);
	}

	public static int getWifiConfig(ServiceStub stub,String uuid,String json){
//		String json = null;
//		//build JianleGenerlReq_st
////		typedef struct
////		{
////			char Name[JIANLE_NET_SRING_LEN];
////			char SessionID[JIANLE_NET_SRING_LEN];
////		}JianleGenerlReq_st;
//		JSONObject JianleGenerlReq_st = new JSONObject();
//		try {
//			JianleGenerlReq_st.put("Name", "NetWork.Wireless");
//			JianleGenerlReq_st.put("SessionID", "000000");
//			json = JianleGenerlReq_st.toString();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		
		if(json == null)
			return -1;
		//then send json string to device.
		Log.d(TAG,json);
		//return NativeCaller.sendJsonCmd(uuid ,ContentCommon.JIANLE_NET_CFG_GET_REQ, json);
		return stub.sendMessage(uuid ,ContentCommon.IPC_NETWORK_WIFI_GET_REQ, json);
	}
	public static int scanWifi(ServiceStub stub,String uuid,String json){
//		String json = null;
//		//build JianleGenerlReq_st
////		typedef struct
////		{
////			char Name[JIANLE_NET_SRING_LEN];
////			char SessionID[JIANLE_NET_SRING_LEN];
////		}JianleGenerlReq_st;
//		JSONObject JianleGenerlReq_st = new JSONObject();
//		try {
//			JianleGenerlReq_st.put("Name", "NetWork.WirelessSearch");
//			JianleGenerlReq_st.put("SessionID", "000000");
//			json = JianleGenerlReq_st.toString();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		
		if(json == null)
			return -1;
		//then send json string to device.
		Log.d(TAG,json);
		//return NativeCaller.sendJsonCmd(uuid ,ContentCommon.JIANLE_NET_CFG_GET_REQ, json);
		return stub.sendMessage(uuid ,ContentCommon.IPC_NETWORK_WIFI_SEARCH_GET_REQ, json);
	}
	public static int getEthernetConfig(ServiceStub stub,String uuid,String json){
//		String json = null;
//		//build JianleGenerlReq_st
////		typedef struct
////		{
////			char Name[JIANLE_NET_SRING_LEN];
////			char SessionID[JIANLE_NET_SRING_LEN];
////		}JianleGenerlReq_st;
//		JSONObject JianleGenerlReq_st = new JSONObject();
//		try {
//			JianleGenerlReq_st.put("Name", "NetWork.Eth");
//			JianleGenerlReq_st.put("SessionID", "000000");
//			json = JianleGenerlReq_st.toString();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		
		if(json == null)
			return -1;
		//then send json string to device.
		Log.d(TAG,json);
		//return NativeCaller.sendJsonCmd(uuid ,ContentCommon.JIANLE_NET_CFG_GET_REQ, json);
		return stub.sendMessage(uuid ,ContentCommon.IPC_NETWORK_ETH_GET_REQ, json);
	}
	
	public static int setEthernetConfig(ServiceStub stub,String uuid,String json){
//		String json = null;
//		JSONObject JianleGenerlReq_st = new JSONObject();
//		try {
//			JianleGenerlReq_st.put("Name", "NetWork.Eth");
//			JianleGenerlReq_st.put("SessionID", "000000");
//			JianleGenerlReq_st.put("NetWork.Eth", ether);
//			json = JianleGenerlReq_st.toString();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		
		if(json == null)
			return -1;
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPC_NETWORK_ETH_SET_REQ, json);
	}
	
	public static int get3G4GConfig(ServiceStub stub,String uuid){		
		return stub.sendMessage(uuid ,ContentCommon.IPC_NETWORK_MOBILE_GET_REQ,"null");
	}
	
	public static int set3G4GConfig(ServiceStub stub,String uuid,String json){
		if(json == null)
			return -1;
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPC_NETWORK_MOBILE_SET_REQ,json);
	}
	
	public static int getPresetConfig(ServiceStub stub,String uuid){		
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_GET_PREPOINT_REQ,"null");
	}
	
	public static int setPresetConfig(ServiceStub stub,String uuid,int bitId,int speed,String desc){
		String json = null;
		JSONObject jsroot = new JSONObject();
		try {
			JSONObject jsresult = new JSONObject();
			jsresult.put("BitID",bitId);
			jsresult.put("Speed",speed);
			try {
				jsresult.put("Desc",new String(desc.getBytes(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			jsroot.put("PtzPreBit.info", jsresult);
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}
		
		json = jsroot.toString();
		if(json == null)
			return -1;
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_SET_PREPOINT_REQ,json);
	}
	
	public static int setOperatePreset(ServiceStub stub,String uuid,int bitId,int op){
		String json = null;
		JSONObject jsroot = new JSONObject();
		try {
			JSONObject jsresult = new JSONObject();
			jsresult.put("BitID",bitId);
			jsresult.put("Type",op);
			jsroot.put("PtzOperateBit.info", jsresult);
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}
		
		json = jsroot.toString();
		if(json == null)
			return -1;
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_OPERATE_PREPOINT_REQ,json);
	}
	
//	public static int getGPIOConfig(ServiceStub stub,String uuid,String json){
////		String json = null;
////		//build JianleGenerlReq_st
//////		typedef struct
//////		{
//////			char Name[JIANLE_NET_SRING_LEN];
//////			char SessionID[JIANLE_NET_SRING_LEN];
//////		}JianleGenerlReq_st;
////		JSONObject JianleGenerlReq_st = new JSONObject();
////		try {
////			JianleGenerlReq_st.put("Name", "NetWork.Eth");
////			JianleGenerlReq_st.put("SessionID", "000000");
////			json = JianleGenerlReq_st.toString();
////		} catch (JSONException e) {
////			e.printStackTrace();
////		}
//		
//		if(json == null)
//			return -1;
//		//then send json string to device.
//		Log.d(TAG,json);
//		//return NativeCaller.sendJsonCmd(uuid ,ContentCommon.JIANLE_NET_CFG_GET_REQ, json);
//		return stub.sendMessage(uuid ,ContentCommon.JIANLE_GPIO_GET_REQ, json);
//	}
	public static int ListRemoteDirInfo(ServiceStub stub,String uuid,String path,int sensorIndex,int mode,int start,int end){
		String json = null;
		json = "{\"lir\":{\"p\":\""+ path + "\",\"si\":"+sensorIndex + ",\"m\":"+mode +",\"st\":"+start + ",\"e\":" + end+"}}";
		
//		if(json == null)
//			return -1;
		//then send json string to device.
		Log.d(TAG,">:" + json);
		return stub.sendMessage(uuid ,ContentCommon.IPC_AV_RECO_LIST_GET_REQ, json);
	}
	public static int ListRemotePageFile(ServiceStub stub,String uuid,String json){
		if(json == null)
			return -1;
		//then send json string to device.
		Log.d(TAG,">:" + json);
		return stub.sendMessage(uuid ,ContentCommon.IPC_AV_RECO_LIST_PAGE_GET_REQ, json);
	}
	public static int RemoteFileOp(ServiceStub stub,String uuid,int sensorIndex,String path,int op){
		String json = null;
//		if(json == null)
//			return -1;
		json = "{\"rfop\":{\"si\":" + sensorIndex +",\"t\":\""+uuid+"\",\"p\":\""+ path + "\",\"op\":" + op + "}}";
		
		//then send json string to device.
		Log.d(TAG,">:" + json);
		return stub.sendMessage(uuid ,ContentCommon.IPC_AV_RECO_OP_REQ, json);
	}
	
	public static int startUpdate(ServiceStub stub,String uuid,String json){
		if(json == null)
			return -1;
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPC_UPGRADE_REQ, json);
	}
	
	public static int getAvRecorderConf(ServiceStub stub,String uuid,String json){
		if(json == null)
			return -1;
		//then send json string to device.
		Log.d(TAG,">:" + json);
		return stub.sendMessage(uuid ,ContentCommon.IPC_AV_RECO_CONF_GET_REQ, json);
	}
	public static int setAvRecorderConf(ServiceStub stub,String uuid,String json){
		if(json == null)
			return -1;
		//then send json string to device.
		Log.d(TAG,">:" + json);
		return stub.sendMessage(uuid ,ContentCommon.IPC_AV_RECO_CONF_SET_REQ, json);
	}
	public static int setGPIO(ServiceStub stub,String uuid,HwResBean b){
		if(b == null)
			return -1;
		String json;
		JSONObject gpioSettingWrapper = new JSONObject();
		try {
			JSONObject gpioSetting = new JSONObject();
			gpioSetting.put("Index", b.index);
			gpioSetting.put("Mode", b.mode);
			gpioSetting.put("Val", b.value);
			gpioSetting.put("Tr", b.trigger);
			gpioSetting.put("Gate", b.gate);
			gpioSettingWrapper.put("GPIO", gpioSetting);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		json = gpioSettingWrapper.toString();
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPC_GPIO_SET_REQ, json);
	}
	public static int getGPIO(ServiceStub stub,String uuid,int index){
		String json;
		JSONObject gpioSettingWrapper = new JSONObject();
		try {
			JSONObject gpioSetting = new JSONObject();
			gpioSetting.put("Index", index);
			gpioSettingWrapper.put("GPIO", gpioSetting);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		json = gpioSettingWrapper.toString();
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPC_GPIO_GET_REQ, json);
	}
	
	public static int getSubdev(ServiceStub stub,String uuid,int index){
		String json;
		JSONObject subdevSettingWrapper = new JSONObject();
		try {
			JSONObject subdevSetting = new JSONObject();
			subdevSetting.put("Id", index);
			subdevSettingWrapper.put("subdev", subdevSetting);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		json = subdevSettingWrapper.toString();
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_GET_SUBDEV_REQ, json);
	}
	
//	public static int setSubdev(ServiceStub stub,String uuid,SubdevBean sdb,int op){
//		String json;
//		JSONObject subdevSettingWrapper = new JSONObject();
//		try {
//			JSONObject subdevSetting = new JSONObject();
//			subdevSetting.put("Id", sdb.id);
//			subdevSetting.put("Name", sdb.name);
//			subdevSetting.put("Type", sdb.type);
//			JSONArray hwresa = new JSONArray();
//			for(HwResBean hrbb:sdb.in){
//				JSONObject hwres = new JSONObject();
//				hwres.put("Idx", hrbb.index);
//				hwres.put("Mode", hrbb.mode);
//				hwres.put("Val", hrbb.value);
//				hwresa.put(hwres);
//			}
//			subdevSetting.put("In", hwresa);
//			hwresa = new JSONArray();
//			for(HwResBean hrbb:sdb.out){
//				JSONObject hwres = new JSONObject();
//				hwres.put("Idx", hrbb.index);
//				hwres.put("Mode", hrbb.mode);
//				hwres.put("Val", hrbb.value);
//				hwresa.put(hwres);
//			}
//			subdevSetting.put("Out", hwresa);
//			subdevSettingWrapper.put("subdev", subdevSetting);
//			subdevSettingWrapper.put("op", op);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		
//		json = subdevSettingWrapper.toString();
//		//then send json string to device.
//		Log.d(TAG,json);
//		return stub.sendMessage(uuid ,ContentCommon.IPCNET_SET_SUBDEV_REQ, json);
//	}
	
	public static int setEnableMobileNetwork(ServiceStub stub,String uuid,String json){
		if(json == null)
			return -1;
		//then send json string to device.
		Log.d(TAG,json);
		//return NativeCaller.sendJsonCmd(uuid ,ContentCommon.JIANLE_NET_CFG_GET_REQ, json);
		return stub.sendMessage(uuid,ContentCommon.IPC_NETWORK_MOBILE_SET_REQ, json);
	}
	
	public static int sendDataToBus(ServiceStub stub,String uuid,int type,int index,String data){
		if(data == null)
			return -1;
		//then send json string to device.
		Log.d(TAG,data);
		//return NativeCaller.sendJsonCmd(uuid ,ContentCommon.JIANLE_NET_CFG_GET_REQ, json);
		return stub.sendMessage(uuid,ContentCommon.IPCNET_SET_BUS_REQ, data);
	}
	
	public static int getDevTimeInfo(ServiceStub stub,String uuid){
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_GET_TIME_REQ, "null");
	}
	public static int getDevInfo(ServiceStub stub,String uuid){
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_GET_DEV_INFO_REQ, "null");
	}
	
	public static int setDevTimeInfo(ServiceStub stub,String uuid,DateBean date){
		String json;
		JSONObject jsroot = new JSONObject();
		try {
			JSONObject jsresult = new JSONObject();
			
			JSONObject jsdate = new JSONObject();
			jsdate.put("Year", date.year);
			jsdate.put("Mon", date.mon);
			jsdate.put("Day", date.day);
			jsresult.put("Date", jsdate);
			
			JSONObject jstime = new JSONObject();
			jstime.put("Hour", date.hour);
			jstime.put("Min", date.min);
			jstime.put("Sec", date.sec);
			jsresult.put("Time", jstime);
			
			jsresult.put("NtpEnable", date.ntpEnable);
			jsresult.put("NtpServ", date.ntpSer);
			jsresult.put("TimeZone", date.timeZone);
			
			jsroot.put("Time.Conf", jsresult);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		json = jsroot.toString();
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_SET_TIME_REQ, json);
	}
	
	public static int getDevMailCfg(ServiceStub stub,String uuid){
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_GET_EMAIL_CFG_REQ, "null");
	}
	public static int setDevMailCfg(ServiceStub stub,String uuid,String json){
		if(json==null)
			return -1;
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_SET_EMAIL_CFG_REQ, json);
	}
	
	public static int getDevFtpCfg(ServiceStub stub,String uuid){
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_GET_FTP_CFG_REQ, "null");
	}
	public static int setDevFtpCfg(ServiceStub stub,String uuid,String json){
		if(json==null)
			return -1;
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_SET_FTP_CFG_REQ, json);
	}

	public static int getDevPtz(ServiceStub stub,String uuid){
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_PRESET_GET_PTZ_REQ, "null");
	}
	
	public static int setDevPtz(ServiceStub stub,String uuid,IPCPtzCmd_st ptzcmd){
		String json;
		JSONObject jsroot = new JSONObject();
		try {
			JSONObject jsresult = new JSONObject();
			
			jsresult.put("MotoCmd", ptzcmd.MotoCmd);
			jsresult.put("OneStep", ptzcmd.OneStep);
			
			JSONObject jsPtzBaseMsg = new JSONObject();
			jsPtzBaseMsg.put("PtzEnable", ptzcmd.PtzBaseMsg.PtzEnable);
			jsPtzBaseMsg.put("BitEnable", ptzcmd.PtzBaseMsg.BitEnable);
			jsPtzBaseMsg.put("StartCenterEnable", ptzcmd.PtzBaseMsg.StartCenterEnable);
			jsPtzBaseMsg.put("Speed", ptzcmd.PtzBaseMsg.Speed);
			jsPtzBaseMsg.put("RunTimes", ptzcmd.PtzBaseMsg.RunTimes);
			jsPtzBaseMsg.put("LevelMaxTimes", ptzcmd.PtzBaseMsg.LevelMaxTimes);
			jsPtzBaseMsg.put("LevelMidTimes", ptzcmd.PtzBaseMsg.LevelMidTimes);
			jsPtzBaseMsg.put("VertMaxTimes", ptzcmd.PtzBaseMsg.VertMaxTimes);
			jsPtzBaseMsg.put("VertMidTimes", ptzcmd.PtzBaseMsg.VertMidTimes);
			jsresult.put("PtzBaseMsg", jsPtzBaseMsg);

			JSONObject jsPtzBitMsg = new JSONObject();
			jsPtzBitMsg.put("UseNum", ptzcmd.PtzBitMsg.UseNum);
			jsresult.put("PtzBitMsg", jsPtzBitMsg);
			
			jsroot.put("PTZInfo", jsresult);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		json = jsroot.toString();
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_PRESET_SET_PTZ_REQ, json);
	}
	
	public static int getMoveAlarmSetting(ServiceStub stub,String uuid){
		return stub.sendMessage(uuid ,ContentCommon.IPC_MOVE_ALARM_GET_REQ, "null");
	}
	public static int setMoveAlarmSetting(ServiceStub stub,String uuid,String json){
		if(json==null)
			return -1;
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPC_MOVE_ALARM_SET_REQ, json);
	}
	
	public static int getPicColorInfo(ServiceStub stub,String uuid){
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_GET_PICOLOR_REQ, "null");
	}
	public static int getVideoOrien(ServiceStub stub,String uuid){
		//then send json string to device.
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_GET_OVERTURN_REQ, "null");
	}
	public static int setPicColorInfo(ServiceStub stub,String uuid,String json){
		if(json==null)
			return -1;
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_SET_PICOLOR_REQ, json);
	}
	
	public static int getExpType(ServiceStub stub,String uuid){
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_GET_EXPOSURE_TYPE_REQ, "null");
	}
	public static int setExpType(ServiceStub stub,String uuid,String json){
		if(json==null)
			return -1;
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_SET_EXPOSURE_TYPE_REQ, json);
	}
	
	public static int getAntlFLickerInfo(ServiceStub stub,String uuid){
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_GET_ANTIFLICKER_REQ, "null");
	}
	public static int setAntlFLickerInfo(ServiceStub stub,String uuid,String json){
		if(json==null)
			return -1;
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_SET_ANTIFLICKER_REQ, json);
	}
	
	public static int getWhBalance(ServiceStub stub,String uuid){
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_GET_WH_BLANCE_REQ, "null");
	}
	public static int setWhBalance(ServiceStub stub,String uuid,String json){
		if(json==null)
			return -1;
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_SET_WH_BLANCE_REQ, json);
	}
	
	public static int getEntiNoiseInfo(ServiceStub stub,String uuid){
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_GET_DENOISE_REQ, "null");
	}
	public static int setEntiNoiseInfo(ServiceStub stub,String uuid,String json){
		if(json==null)
			return -1;
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_SET_DENOISE_REQ, json);
	}
	
	public static int getWdrInfo(ServiceStub stub,String uuid){
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_GET_WDR_REQ, "null");
	}
	public static int setWdrInfo(ServiceStub stub,String uuid,String json){
		if(json==null)
			return -1;
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_SET_WDR_REQ, json);
	}
	
	public static int start2Talk(ServiceStub stub,String uuid,boolean enable){
		String json;
		JSONObject start2TalkWrapper = new JSONObject();
		try {
			JSONObject start2Talk = new JSONObject();
			start2Talk.put("TalkEnable", enable);
			start2TalkWrapper.put("Talk.Req", start2Talk);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		json = start2TalkWrapper.toString();
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_TALK_REQ, json);
	}
	
	public static int getOsdCfg(ServiceStub stub,String uuid,String json){
		if(json == null)
			return -1;
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_GET_OSD_REQ, json);
	}
	public static int setOsdCfg(ServiceStub stub,String uuid,String json){
		if(json == null)
			return -1;
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_SET_OSD_REQ, json);
	}
	
	public static int setSetUserInfo(ServiceStub stub,String uuid,String json){
		if(json == null)
			return -1;
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_USER_SET_REQ, json);
	}
	
	public static int getGetUpgradeCfg(ServiceStub stub,String uuid){
//		if(json == null)
//			return -1;
		//then send json string to device.
//		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_UPGRADE_CFG_REQ, "null");
	}
	public static int setDevAutoUpdate(ServiceStub stub,String uuid,String json){
		if(json == null)
			return -1;
		//then send json string to device.
		Log.d(TAG,json);
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_UPGRADE_AUTO_SET_REQ, json);
	}
	
	public static int getSnapshot(ServiceStub stub,String uuid,String json){
		if(json == null)
			return -1;
		return stub.sendMessage(uuid ,ContentCommon.IPCNET_SNAP_SHOOT_REQ,json);
	}
}
