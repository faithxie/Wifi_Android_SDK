package freesbell.demo.bean;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fenzhi.nativecaller.NativeCaller;
import android.util.Log;

public class JSONStructProtocal {
	private static final String TAG = "JSONStructProtocal";
	public class IPCNetTime_st{
		public int Hour;
		public int Min;
		public int Sec;
	}
	public class IPCNetDate_st{
		public int Year;
		public int Mon;
		public int Day;
	}
	public class IPCNetTimePeriod_st{
		public IPCNetTime_st Start = new IPCNetTime_st();
		public IPCNetTime_st End = new IPCNetTime_st();
	}
	public class MSG_LOGIN_t
	{
	    public String user;//用户名称
	    public String passwd;//md5加密后的密码
	    public int interval;//心跳间隔时间
	    public boolean parseJSON(JSONObject jsdata){
	    	try {
				JSONObject jsroot= jsdata.getJSONObject("MSG_LOGIN_t");
				if(jsroot!=null){
					user = jsroot.getString("user");
					passwd = jsroot.getString("passwd");
					interval = jsroot.getInt("interval");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    public String toJSONString(){
			JSONObject jsroot = new JSONObject();
			try {
				JSONObject jsethnetwork = new JSONObject();
				jsethnetwork.put("user", user);
				jsethnetwork.put("passwd", passwd);
				jsethnetwork.put("interval", interval);
				jsroot.put("MSG_LOGIN_t", jsethnetwork);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			return jsroot.toString();
	    }
	};
	public class IPCNetSnapShoot_st{
		public int ViCh;
		public boolean parseJSON(JSONObject jsdata){
	    	try {
				JSONObject jsroot= jsdata.getJSONObject("SnapShoot.info");
				if(jsroot!=null){
					ViCh = jsroot.getInt("ViCh");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    public String toJSONString(){
			JSONObject jsroot = new JSONObject();
			try {
				JSONObject jsethnetwork = new JSONObject();
				jsethnetwork.put("ViCh", ViCh);
				
				jsroot.put("SnapShoot.info", jsethnetwork);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			return jsroot.toString();
	    }
	}
	public class IPCNetDefendPeriodInfo_st//布防时间段
	{
		public int  Flag;
		public IPCNetTimePeriod_st TimePeriod[] = new IPCNetTimePeriod_st[2];
//		public int TimePeriodCount;
	}
	public class IPCNetVideoMoveInfo_st{
		public int  MdEnable;
		public int  Sensitive;  
//		public int  Columns;
//		public int  Rows;
		public int  Area[] = new int[256];
		public int  AreaCount;
	}
	public class JianleAlarmIoOutPutInfo_st{
		public int   Delay;
		public int   EmailEnable;
	}
	public class JianleAlarmRecInfo_st{
		public int RecordTime;
		public int FtpEnable;
		public int EmailEnable;
	}
	public class IPCNetAlarmSnapInfo_st{
		public int PictureNum;
		public int Sec;
		public int Msec;
		public int FtpEnable;
		public int EmailEnable;
	}
	public class IPCNetAlarmIoOutputInfo_st{
		public int Delay;
		public int EmailEnable;
	}
	public class IPCNetAlarmRecordInfo_st{
		public int RecordTime;
		public int FtpEnable;
		public int EmailEnable;
	}

	public class IPCNetAlarmPresetInfo_st{
		public int PresetId;
		public String PresetName;
	}
	public class IPCNetAlarmLinkagePolicyInfo_st{
		public IPCNetAlarmIoOutputInfo_st IoOutputInfo = new IPCNetAlarmIoOutputInfo_st();
		public IPCNetAlarmRecordInfo_st RecordInfo = new IPCNetAlarmRecordInfo_st();
		public IPCNetAlarmSnapInfo_st SnapInfo = new IPCNetAlarmSnapInfo_st();
		public IPCNetAlarmPresetInfo_st PresetInfo = new IPCNetAlarmPresetInfo_st();
	}
	public class IPCNETMoveAlarmCfg_st{ 
		public int ViCh;
	    public IPCNetDefendPeriodInfo_st Week[] = new IPCNetDefendPeriodInfo_st[8];
	    public IPCNetVideoMoveInfo_st MoveInfo = new IPCNetVideoMoveInfo_st();
	    public IPCNetAlarmLinkagePolicyInfo_st PolicyInfo = new IPCNetAlarmLinkagePolicyInfo_st();
	    
	    public boolean parseJSON(JSONObject jsdata){
	    	try {
				JSONObject jsroot= jsdata.getJSONObject("Alarm.MoveAlarm");
				if(jsroot!=null){
					ViCh = jsroot.getInt("ViCh");
					JSONArray jsWeek = jsroot.getJSONArray("Week");
					for(int i=0;i<jsWeek.length()&&i<8;i++){
						JSONObject jsIPCNetDefendPeriodInfo_st = jsWeek.getJSONObject(i);
						Week[i] = new IPCNetDefendPeriodInfo_st();
						Week[i].Flag = jsIPCNetDefendPeriodInfo_st.getInt("Flag");
						
						JSONArray jsTimePeriod = jsIPCNetDefendPeriodInfo_st.getJSONArray("TimePeriod");
						for(int j=0;j<jsTimePeriod.length()&&j<2;j++){
							JSONObject jsIPCNetTimePeriod_st = jsTimePeriod.getJSONObject(j);
							Week[i].TimePeriod[j] = new IPCNetTimePeriod_st();
							JSONObject jsEnd = jsIPCNetTimePeriod_st.getJSONObject("End");
							Week[i].TimePeriod[j].End.Hour = jsEnd.getInt("Hour");
							Week[i].TimePeriod[j].End.Min = jsEnd.getInt("Min");
							Week[i].TimePeriod[j].End.Sec = jsEnd.getInt("Sec");
							
							JSONObject jsStart = jsIPCNetTimePeriod_st.getJSONObject("Start");
							Week[i].TimePeriod[j].Start.Hour = jsStart.getInt("Hour");
							Week[i].TimePeriod[j].Start.Min = jsStart.getInt("Min");
							Week[i].TimePeriod[j].Start.Sec = jsStart.getInt("Sec");
						}
						
//						Week[i].TimePeriodCount = jsIPCNetDefendPeriodInfo_st.getInt("TimePeriodCount");
					}
					
					JSONObject jsMoveInfo = jsroot.getJSONObject("MoveInfo");
//					MoveInfo.AreaCount = jsMoveInfo.getInt("AreaCount");
					MoveInfo.MdEnable = jsMoveInfo.getInt("MdEnable");
					MoveInfo.Sensitive = jsMoveInfo.getInt("Sensitive");
//					MoveInfo.Columns = jsMoveInfo.getInt("Columns");
//					MoveInfo.Rows = jsMoveInfo.getInt("Rows");
					JSONArray jsArea = jsMoveInfo.getJSONArray("Area");
					for(int i=0;i<jsArea.length()&&i<256;i++){
						MoveInfo.Area[i] = jsArea.getInt(i);
					}
					
					JSONObject jsPolicyInfo = jsroot.getJSONObject("PolicyInfo");
					
					JSONObject jsIoOutputInfo = jsPolicyInfo.getJSONObject("IoOutputInfo");
					PolicyInfo.IoOutputInfo.Delay = jsIoOutputInfo.getInt("Delay");
					PolicyInfo.IoOutputInfo.EmailEnable = jsIoOutputInfo.getInt("EmailEnable");
					
					JSONObject jsRecordInfo = jsPolicyInfo.getJSONObject("RecordInfo");
					PolicyInfo.RecordInfo.EmailEnable = jsRecordInfo.getInt("EmailEnable");
					PolicyInfo.RecordInfo.RecordTime = jsRecordInfo.getInt("RecordTime");
					PolicyInfo.RecordInfo.FtpEnable = jsRecordInfo.getInt("FtpEnable");
					
					JSONObject jsSnapInfo = jsPolicyInfo.getJSONObject("SnapInfo");
					PolicyInfo.SnapInfo.EmailEnable = jsSnapInfo.getInt("EmailEnable");
					PolicyInfo.SnapInfo.PictureNum = jsSnapInfo.getInt("PictureNum");
					PolicyInfo.SnapInfo.Sec = jsSnapInfo.getInt("Sec");
					PolicyInfo.SnapInfo.Msec = jsSnapInfo.getInt("Msec");
					PolicyInfo.SnapInfo.FtpEnable = jsSnapInfo.getInt("FtpEnable");
					
					JSONObject jsPresetInfo = jsPolicyInfo.getJSONObject("PresetInfo");
					PolicyInfo.PresetInfo.PresetId = jsPresetInfo.getInt("PresetId");
					PolicyInfo.PresetInfo.PresetName = jsPresetInfo.getString("PresetName");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    public String toJSONString(){
	    	JSONObject jsresult = new JSONObject();
			try {
				JSONObject jsroot = new JSONObject();
				jsroot.put("ViCh",ViCh);
				
				JSONArray jsWeek = new JSONArray();
				for(int i=0;i<8;i++){
					JSONObject jsIPCNetDefendPeriodInfo_st = new JSONObject();
					
					jsIPCNetDefendPeriodInfo_st.put("Flag",Week[i].Flag);
					
					JSONArray jsTimePeriod = new JSONArray();
					for(int j=0;j<2;j++){
						JSONObject jsIPCNetTimePeriod_st = new JSONObject();
						
						JSONObject jsEnd = new JSONObject();
						jsEnd.put("Hour",Week[i].TimePeriod[j].End.Hour);
						jsEnd.put("Min",Week[i].TimePeriod[j].End.Min);
						jsEnd.put("Sec",Week[i].TimePeriod[j].End.Sec);
						jsIPCNetTimePeriod_st.put("End", jsEnd);
						
						JSONObject jsStart = new JSONObject();
						jsStart.put("Hour",Week[i].TimePeriod[j].Start.Hour);
						jsStart.put("Min",Week[i].TimePeriod[j].Start.Min);
						jsStart.put("Sec",Week[i].TimePeriod[j].Start.Sec);
						jsIPCNetTimePeriod_st.put("Start", jsStart);
						
						jsTimePeriod.put(jsIPCNetTimePeriod_st);
					}
					jsIPCNetDefendPeriodInfo_st.put("TimePeriod", jsTimePeriod);
					
//					jsIPCNetDefendPeriodInfo_st.put("TimePeriodCount",Week[i].TimePeriodCount);
					
					jsWeek.put(jsIPCNetDefendPeriodInfo_st);
				}
				jsroot.put("Week", jsWeek);
				
				JSONObject jsMoveInfo = new JSONObject();
//				jsMoveInfo.put("AreaCount",MoveInfo.AreaCount);
				jsMoveInfo.put("MdEnable",MoveInfo.MdEnable);
				jsMoveInfo.put("Sensitive",MoveInfo.Sensitive);
//				jsMoveInfo.put("Columns",MoveInfo.Columns);
//				jsMoveInfo.put("Rows",MoveInfo.Rows);
				JSONArray jsArea = new JSONArray();
				for(int i=0;i<256;i++){
					jsArea.put(MoveInfo.Area[i]);
				}
				jsMoveInfo.put("Area",jsArea);
				
				jsroot.put("MoveInfo",jsMoveInfo);
				
				JSONObject jsPolicyInfo = new JSONObject();
				
				JSONObject jsIoOutputInfo = new JSONObject();
				jsIoOutputInfo.put("Delay",PolicyInfo.IoOutputInfo.Delay);
				jsIoOutputInfo.put("EmailEnable",PolicyInfo.IoOutputInfo.EmailEnable);
				jsPolicyInfo.put("IoOutputInfo",jsIoOutputInfo);
				
				JSONObject jsRecordInfo = new JSONObject();
				jsRecordInfo.put("EmailEnable",PolicyInfo.RecordInfo.EmailEnable);
				jsRecordInfo.put("RecordTime",PolicyInfo.RecordInfo.RecordTime);
				jsRecordInfo.put("FtpEnable",PolicyInfo.RecordInfo.FtpEnable);
				jsPolicyInfo.put("RecordInfo",jsRecordInfo);
				
				JSONObject jsSnapInfo = new JSONObject();
				jsSnapInfo.put("EmailEnable",PolicyInfo.SnapInfo.EmailEnable);
				jsSnapInfo.put("PictureNum",PolicyInfo.SnapInfo.PictureNum);
				jsSnapInfo.put("Sec",PolicyInfo.SnapInfo.Sec);
				jsSnapInfo.put("Msec",PolicyInfo.SnapInfo.Msec);
				jsSnapInfo.put("FtpEnable",PolicyInfo.SnapInfo.FtpEnable);
				jsPolicyInfo.put("SnapInfo",jsSnapInfo);
				
				JSONObject jsPresetInfo = new JSONObject();
				jsPresetInfo.put("PresetId",PolicyInfo.PresetInfo.PresetId);
				jsPresetInfo.put("PresetName",PolicyInfo.PresetInfo.PresetName);
				jsPolicyInfo.put("PresetInfo",jsPresetInfo);

				jsroot.put("PolicyInfo",jsPolicyInfo);
				
				jsresult.put("Alarm.MoveAlarm", jsroot);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			
			return jsresult.toString();
	    }
	}
	public class IPCNetEthConfig_st{
		public boolean DhcpEnble;
		public String IP;
		public String Netmask;
		public String Getway;
		public String DNS1;
		public String DNS2;
		public boolean parseJSON(JSONObject jsdata){
	    	try {
				JSONObject jsroot= jsdata.getJSONObject("NetWork.Eth");
				if(jsroot!=null){
					DhcpEnble = jsroot.getBoolean("DhcpEnble");
					IP = jsroot.getString("IP");
					Netmask = jsroot.getString("Netmask");
					Getway = jsroot.getString("Getway");
					DNS1 = jsroot.getString("DNS1");
					DNS2 = jsroot.getString("DNS2");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    public String toJSONString(){
			JSONObject jsroot = new JSONObject();
			try {
				JSONObject jsethnetwork = new JSONObject();
				jsethnetwork.put("DhcpEnble", DhcpEnble);
				jsethnetwork.put("IP", IP);
				jsethnetwork.put("Netmask", Netmask);
				jsethnetwork.put("Getway", Getway);
				jsethnetwork.put("DNS1", DNS1);
				jsethnetwork.put("DNS2", DNS2);
				jsroot.put("NetWork.Eth", jsethnetwork);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			return jsroot.toString();
	    }
	}
	public class IPCNetFtpCfg_st{
		public String FtpAddr;
		public int FtpPort;
		public String FtpUser;
		public String FtpPasswd;
		public String FtpPath;
	    
	    public boolean parseJSON(JSONObject jsdata){
	    	try {
				JSONObject jsroot= jsdata.getJSONObject("FtpCfg.info");
				if(jsroot!=null){
					FtpPort = jsroot.getInt("FtpPort");
					FtpAddr = jsroot.getString("FtpAddr");
					FtpUser = jsroot.getString("FtpUser");
					FtpPasswd = jsroot.getString("FtpPasswd");
					FtpPath = jsroot.getString("FtpPath");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    
	    public String toJSONString()
	    {
	    	JSONObject jsroot = new JSONObject();
	    	if(jsroot!=null){
	    		JSONObject jsIPCNetEmailCfg_st = new JSONObject();
	    		
	    		try {
	    			jsIPCNetEmailCfg_st.put("FtpAddr", FtpAddr);
	    			jsIPCNetEmailCfg_st.put("FtpPort", FtpPort);
	    			jsIPCNetEmailCfg_st.put("FtpUser",FtpUser);
	    			jsIPCNetEmailCfg_st.put("FtpPasswd", FtpPasswd);
	    			jsIPCNetEmailCfg_st.put("FtpPath", FtpPath);
	    			
					jsroot.put("FtpCfg.info", jsIPCNetEmailCfg_st);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
	    	}
	    	return jsroot.toString();
	    }
	};
	public class IPCNetGetGpioInfo_st{
	    public int Index;
	    public boolean parseJSON(JSONObject jsdata){
	    	try {
				JSONObject jsroot= jsdata.getJSONObject("GPIO");
				if(jsroot!=null){
					Index = jsroot.getInt("Index");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    public String toJSONString(){
			JSONObject jsroot = new JSONObject();
			try {
				JSONObject jsresult = new JSONObject();
				
				jsresult.put("Index", Index);
				
				jsroot.put("GPIO", jsresult);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			
			return jsroot.toString();
	    }
	}
	public class IPCNetGpioInfo_st{
	    public int Index;
	    public int  Mode;
	    public int  Val;
	    public int  Tr;
	    public int  Gate;
	    public boolean parseJSON(JSONObject jsdata){
	    	try {
				JSONObject jsroot= jsdata.getJSONObject("GPIO");
				if(jsroot!=null){
					Index = jsroot.getInt("Index");
					Mode = jsroot.getInt("Mode");
					Val = jsroot.getInt("Val");
					Tr = jsroot.getInt("Tr");
					Gate = jsroot.getInt("Gate");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    public String toJSONString(){
			JSONObject jsroot = new JSONObject();
			try {
				JSONObject jsresult = new JSONObject();
				
				jsresult.put("Index", Index);
				jsresult.put("Mode", Mode);
				jsresult.put("Val", Val);
				jsresult.put("Tr", Tr);
				jsresult.put("Gate", Gate);
				
				jsroot.put("GPIO", jsresult);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			
			return jsroot.toString();
	    }
	}
	public class IPCNetEmailCfg_st{
		public int SmtpPort;
		public String SmtpServer = "";
//	    public boolean EnVerify;//enable verify the smtp user name and password.
		public int EncType;//encrypt type
		public String SmtpUser = "";
		public String SmtpPasswd = "";
		public String SmtpSender = "";
		public String MailTitle = "";
		public String MailContext = "";
		public String SmtpReceiver1 = "";
		public String SmtpReceiver2 = "";
		public String SmtpReceiver3 = "";
		public String SmtpReceiver4 = "";
	    
	    public boolean parseJSON(JSONObject jsdata){
	    	try {
				JSONObject jsroot= jsdata.getJSONObject("EmailCfg.info");
				if(jsroot!=null){
					SmtpPort = jsroot.getInt("SmtpPort");
				    SmtpServer = jsroot.getString("SmtpServer");
//				    EnVerify = jsroot.getBoolean("EnVerify");
				    EncType = jsroot.getInt("EncType");
				    SmtpUser = jsroot.getString("SmtpUser");
				    SmtpPasswd = jsroot.getString("SmtpPasswd");
				    SmtpSender = jsroot.getString("SmtpSender");
				    MailTitle = jsroot.getString("MailTitle");
				    MailContext = jsroot.getString("MailContext");
				    SmtpReceiver1 = jsroot.getString("SmtpReceiver1");
				    SmtpReceiver2 = jsroot.getString("SmtpReceiver2");
				    SmtpReceiver3 = jsroot.getString("SmtpReceiver3");
				    SmtpReceiver4 = jsroot.getString("SmtpReceiver4");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    
	    public String toJSONString()
	    {
	    	JSONObject jsroot = new JSONObject();
	    	if(jsroot!=null){
	    		JSONObject jsIPCNetEmailCfg_st = new JSONObject();
	    		
	    		try {
	    			jsIPCNetEmailCfg_st.put("SmtpPort", SmtpPort);
	    			jsIPCNetEmailCfg_st.put("SmtpServer", SmtpServer);
//	    			jsIPCNetEmailCfg_st.put("EnVerify",EnVerify);
	    			jsIPCNetEmailCfg_st.put("EncType",EncType);
	    			jsIPCNetEmailCfg_st.put("SmtpUser", SmtpUser);
	    			jsIPCNetEmailCfg_st.put("SmtpPasswd", SmtpPasswd);
	    			jsIPCNetEmailCfg_st.put("SmtpSender", SmtpSender);
	    			jsIPCNetEmailCfg_st.put("MailTitle", MailTitle);
	    			jsIPCNetEmailCfg_st.put("MailContext", MailContext);
	    			jsIPCNetEmailCfg_st.put("SmtpReceiver1", SmtpReceiver1);
	    			jsIPCNetEmailCfg_st.put("SmtpReceiver2", SmtpReceiver2);
	    			jsIPCNetEmailCfg_st.put("SmtpReceiver3", SmtpReceiver3);
	    			jsIPCNetEmailCfg_st.put("SmtpReceiver4", SmtpReceiver4);
	    			
					jsroot.put("EmailCfg.info", jsIPCNetEmailCfg_st);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
	    	}
	    	return jsroot.toString();
	    }
	}
	public class IPCNetMobileNetworkInfo_st{
		public boolean enable;	//使能3g模块，0-不生效，1-生效
		public int type;		// 3G模块类型，0-电信evdo,1-联通wcdma
		public String ip;	// 3G模块获取的IP地址
		public boolean parseJSON(JSONObject jsdata){
	    	try {
				JSONObject jsroot= jsdata.getJSONObject("Net.Mobile");
				if(jsroot!=null){
					type = jsroot.getInt("type");
					enable = jsroot.getBoolean("enable");
					ip = jsroot.getString("ip");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    public String toJSONString(){
			JSONObject jsroot = new JSONObject();
			try {
				JSONObject jsresult = new JSONObject();
				jsresult.put("type", type);
				jsresult.put("enable", enable);
				jsresult.put("ip", ip);
				jsroot.put("Net.Mobile", jsresult);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			return jsroot.toString();
	    }
	}
	public class IPCNetWifiApItem{
		public String SSID = "";
		public String EncType = "";
		public int RSSI;
	}
	public class IPCNetWifiAplist{
		public IPCNetWifiApItem ApItem[];// = new IPCNetWifiApItem[1];
		public boolean parseJSON(JSONObject jsdata){
	    	try {
				JSONObject jsroot= jsdata.getJSONObject("NetWork.WirelessSearch");
				if(jsroot!=null){
					JSONArray Aplist = jsroot.getJSONArray("Aplist");
					if(Aplist!=null){
						int apcount = Aplist.length();
						Log.d(TAG,"ap num:" + apcount);
						ApItem = new IPCNetWifiApItem[apcount];
						
						for(int i=0;i<apcount;i++){
							JSONObject ap = Aplist.getJSONObject(i);
							IPCNetWifiApItem apitem = ApItem[i];
							apitem.SSID = ap.getString("SSID");
							apitem.EncType = ap.getString("EncType");
							apitem.RSSI = ap.getInt("RSSI");
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    public String toJSONString(){
			return null;
	    }
	}
	public class IPCNetTimeCfg_st{
	    public IPCNetDate_st Date;
	    public IPCNetTime_st Time;
		public boolean NtpEnable;
		public String NtpServ;
	    public int TimeZone;
	    public boolean parseJSON(JSONObject jsdata){
	    	try {
				JSONObject jsroot= jsdata.getJSONObject("Time.Conf");
				if(jsroot!=null){
					NtpEnable = jsroot.getBoolean("NtpEnable");
					NtpServ = jsroot.getString("NtpServ");
					TimeZone = jsroot.getInt("TimeZone");
					
					JSONObject jsTime = jsroot.getJSONObject("Time");
					Time.Hour = jsTime.getInt("Hour");
					Time.Min = jsTime.getInt("Min");
					Time.Sec = jsTime.getInt("Sec");
					
					JSONObject info = jsroot.getJSONObject("Date");
					Date.Day = info.getInt("Day");
					Date.Mon = info.getInt("Mon");
					Date.Year = info.getInt("Year");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    public String toJSONString(){
			JSONObject jsroot = new JSONObject();
			try {
				JSONObject jsresult = new JSONObject();
				
				jsresult.put("NtpEnable", NtpEnable);
				jsresult.put("NtpServ", NtpServ);
				jsresult.put("TimeZone",TimeZone);
				
				JSONObject jsTime = new JSONObject();
				jsTime.put("Hour", Time.Hour);
				jsTime.put("Min", Time.Min);
				jsTime.put("Sec", Time.Sec);
				jsresult.put("Time", jsTime);
				
				JSONObject info = new JSONObject();
				info.put("Day",Date.Day);
				info.put("Mon",Date.Mon);
				info.put("Year",Date.Year);
				jsresult.put("Date", info);
				
				jsroot.put("Time.Conf", jsresult);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			
			return jsroot.toString();
	    }
	}
	public class IPCNetWirelessConfig_st{
		public boolean WirelessEnable;
		public int WirelessStatus;
		public String SsidSetMode;
		public String EncType;
		public String SSID;
		public String Password;
		public boolean DhcpEnble;
		public String IP;
		public String Netmask;
		public String Getway;
		public boolean parseJSON(JSONObject jsdata){
	    	try {
				JSONObject jsroot= jsdata.getJSONObject("NetWork.Wireless");
				if(jsroot!=null){
					WirelessEnable = jsroot.getBoolean("WirelessEnable");
					WirelessStatus = jsroot.getInt("WirelessStatus");
					SsidSetMode = jsroot.getString("SsidSetMode");
					EncType = jsroot.getString("EncType");
					SSID = jsroot.getString("SSID");
					Password = jsroot.getString("Password");
					DhcpEnble = jsroot.getBoolean("DhcpEnble");
					IP = jsroot.getString("IP");
					Netmask = jsroot.getString("Netmask");
					Getway = jsroot.getString("Getway");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    public String toJSONString(){
			JSONObject jsroot = new JSONObject();
			try {
				JSONObject jsresult = new JSONObject();
				jsresult.put("WirelessEnable", WirelessEnable);
				jsresult.put("WirelessStatus", WirelessStatus);
				jsresult.put("SsidSetMode", SsidSetMode);
				jsresult.put("EncType", EncType);
				jsresult.put("SSID", SSID);
				jsresult.put("Password", Password);
				jsresult.put("DhcpEnble", DhcpEnble);
				jsresult.put("IP", IP);
				jsresult.put("Netmask", Netmask);
				jsresult.put("Getway", Getway);
				jsroot.put("NetWork.Wireless", jsresult);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			return jsroot.toString();
	    }
	}
	public class IPCNetPicColorInfo_st{
		public int Type;	//彩转黑类型

		//自动黑白-彩色转换有效
		public int SwitchLevel;//切换灵敏度，0-低，1-中，2-高
		public int SwitchTime;	//切换时间，单位s

		//以下参数仅在定时彩转黑有效
		public int TimerOpera;	//定时动作//可设定时任务外的彩转黑方式
//		public int OuttimeOpera;	//定时外动作//可设定时任务外的彩转黑方式
		public IPCNetTime_st Start = new IPCNetTime_st();
		public IPCNetTime_st End = new IPCNetTime_st();
		
		public boolean parseJSON(JSONObject jsdata){
	    	try {
				JSONObject jsroot= jsdata.getJSONObject("IspPicColor.info");
				if(jsroot!=null){
					Type = jsroot.getInt("Type");
					SwitchLevel = jsroot.getInt("SwitchLevel");
					SwitchTime = jsroot.getInt("SwitchTime");
					
					TimerOpera = jsroot.getInt("TimerOpera");
//					OuttimeOpera = jsroot.getInt("OuttimeOpera");
					
					JSONObject jsStart = jsroot.getJSONObject("Start");
					Start.Hour = jsStart.getInt("Hour");
					Start.Min = jsStart.getInt("Min");
					Start.Sec = jsStart.getInt("Sec");
					
					JSONObject jsEnd = jsroot.getJSONObject("End");
					End.Hour = jsEnd.getInt("Hour");
					End.Min = jsEnd.getInt("Min");
					End.Sec = jsEnd.getInt("Sec");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    public String toJSONString(){
			JSONObject jsroot = new JSONObject();
			try {
				JSONObject jsresult = new JSONObject();
				
				jsresult.put("Type", Type);
				jsresult.put("SwitchLevel", SwitchLevel);
				jsresult.put("SwitchTime",SwitchTime);
				
				jsresult.put("TimerOpera",TimerOpera);
//				jsresult.put("OuttimeOpera",OuttimeOpera);
				
				JSONObject jsStart = new JSONObject();
				jsStart.put("Hour", Start.Hour);
				jsStart.put("Min", Start.Min);
				jsStart.put("Sec", Start.Sec);
				jsresult.put("Start", jsStart);
				
				JSONObject jsEnd = new JSONObject();
				jsEnd.put("Hour", End.Hour);
				jsEnd.put("Min", End.Min);
				jsEnd.put("Sec", End.Sec);
				jsresult.put("End", jsEnd);
				
				jsroot.put("IspPicColor.info", jsresult);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			
			return jsroot.toString();
	    }
	}
	
	public class IPCNetExpType_st{
		public int type;
		public boolean parseJSON(JSONObject jsdata){
	    	try {
				JSONObject jsroot= jsdata.getJSONObject("IspExpType.info");
				if(jsroot!=null){
					type = jsroot.getInt("type");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    public String toJSONString(){
			JSONObject jsroot = new JSONObject();
			try {
				JSONObject jsresult = new JSONObject();
				jsresult.put("type", type);
				jsroot.put("IspExpType.info", jsresult);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			return jsroot.toString();
	    }
	}
	
	public class IPCNetWhBalance_st{
		public int Type;	//0-自动, 1-手动，（自动以下参数无效）
		public int AutoRG; //MAX Value
		public int AutoBG; //MAX Value 0-255
		public int ManualRG;
		public int ManualBG;//0-65535
		public boolean parseJSON(JSONObject jsdata){
	    	try {
				JSONObject jsroot= jsdata.getJSONObject("IspWhBalance.info");
				if(jsroot!=null){
					Type = jsroot.getInt("Type");
					AutoRG = jsroot.getInt("AutoRG");
					AutoBG = jsroot.getInt("AutoBG");
					ManualRG = jsroot.getInt("ManualRG");
					ManualBG = jsroot.getInt("ManualBG");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    public String toJSONString(){
			JSONObject jsroot = new JSONObject();
			try {
				JSONObject jsresult = new JSONObject();
				jsresult.put("Type", Type);
				jsresult.put("AutoRG", AutoRG);
				jsresult.put("AutoBG", AutoBG);
				jsresult.put("ManualRG", ManualRG);
				jsresult.put("ManualBG", ManualBG);
				jsroot.put("IspWhBalance.info", jsresult);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			return jsroot.toString();
	    }
	}
	public class IPCNetDeNoiseInfo_st{
		public boolean En2DEntiNoise;
		public boolean En3DEntiNoise;
		public int Val3D;
		public boolean parseJSON(JSONObject jsdata){
	    	try {
				JSONObject jsroot= jsdata.getJSONObject("IspDenoise.info");
				if(jsroot!=null){
					En2DEntiNoise = jsroot.getBoolean("En2DEntiNoise");
					En3DEntiNoise = jsroot.getBoolean("En3DEntiNoise");
					Val3D = jsroot.getInt("Val3D");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    public String toJSONString(){
			JSONObject jsroot = new JSONObject();
			try {
				JSONObject jsresult = new JSONObject();
				jsresult.put("En2DEntiNoise", En2DEntiNoise);
				jsresult.put("En3DEntiNoise", En3DEntiNoise);
				jsresult.put("Val3D", Val3D);
				jsroot.put("IspDenoise.info", jsresult);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			return jsroot.toString();
	    }
	}
	public class IPCNetWdrInfo_st{
		public boolean WdrEn;
		public boolean WdrManEn;//手动宽动态使能
		public int WdrVal;
		public boolean parseJSON(JSONObject jsdata){
	    	try {
				JSONObject jsroot= jsdata.getJSONObject("IspWdr.info");
				if(jsroot!=null){
					WdrEn = jsroot.getBoolean("WdrEn");
					WdrManEn = jsroot.getBoolean("WdrManEn");
					WdrVal = jsroot.getInt("WdrVal");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    public String toJSONString(){
			JSONObject jsroot = new JSONObject();
			try {
				JSONObject jsresult = new JSONObject();
				jsresult.put("WdrEn", WdrEn);
				jsresult.put("WdrManEn", WdrManEn);
				jsresult.put("WdrVal", WdrVal);
				jsroot.put("IspWdr.info", jsresult);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			return jsroot.toString();
	    }
	}
	
	public class IPCNetCamColorCfg_st{
	    public boolean SetDefault = false;    //是否设置默认值
	    public int ViCh;//max=3
	    public int Brightness;//max=255 def:0
	    public int Chroma;//max=255 def:0
	    public int Contrast;//max=255 def:0x80
	    
	    public int Saturtion;//max=255 def:0
	    public int Acutance;//max=255 def:0
	    public boolean parseJSON(JSONObject jsdata){
	    	try {
				JSONObject jsroot= jsdata.getJSONObject("CamCfg.info");
				ViCh = jsroot.getInt("ViCh");
				Brightness = jsroot.getInt("Brightness");
				Chroma = jsroot.getInt("Chroma");
				Contrast = jsroot.getInt("Contrast");
				Saturtion = jsroot.getInt("Saturtion");
				Acutance = jsroot.getInt("Acutance");
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    public String toJSONString(){
			JSONObject jsroot = new JSONObject();
			try {
				JSONObject jsresult = new JSONObject();
				jsresult.put("SetDefault",SetDefault);
				jsresult.put("ViCh",ViCh);
				jsresult.put("Brightness",Brightness);
				jsresult.put("Chroma",Chroma);
				jsresult.put("Contrast",Contrast);
				jsresult.put("Saturtion",Saturtion);
				jsresult.put("Acutance",Acutance);
				jsroot.put("CamCfg.info", jsresult);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			
			return jsroot.toString();
	    }
	}
	public class IPCNETPointInfo_st{
		public int BitID;
		public int Speed;
		public String Desc = "";
		public boolean parseJSON(JSONObject jsdata){
	    	try {
				JSONObject jsroot= jsdata.getJSONObject("PtzPreBit.info");
				BitID = jsroot.getInt("BitID");
				Speed = jsroot.getInt("Speed");
				Desc = jsroot.getString("Desc");
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    public String toJSONString(){
			JSONObject jsroot = new JSONObject();
			try {
				JSONObject jsresult = new JSONObject();
				jsresult.put("BitID",BitID);
				jsresult.put("Speed",Speed);
				jsresult.put("Desc",Desc);
				jsroot.put("PtzPreBit.info", jsresult);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			
			return jsroot.toString();
	    }
	}
	public class IPCNETPrePointList_st{
		public int PointsCount;
	    public IPCNETPointInfo_st Points[] = new IPCNETPointInfo_st[32];
	    public void deletePreset(int id){
	    	for(int i=0;i<PointsCount;i++){
	    		if(Points[i]!=null&&Points[i].BitID == id){
	    			Points[i] = null;
	    			PointsCount--;
	    			break;
	    		}
	    	}
	    }
	    public boolean parseJSON(JSONObject jsdata){
	    	try {
				JSONObject jsroot= jsdata.getJSONObject("PrePointList.info");
				if(jsroot!=null){
					JSONArray jsaPoints = jsroot.getJSONArray("Points");
					PointsCount = jsaPoints.length();
					for(int i=0;i<PointsCount;i++){
						JSONObject jsPoints = (JSONObject) jsaPoints.get(i);
						JSONObject jppi = jsPoints.getJSONObject("PtzPreBit.info");
						if(jppi!=null){
							Points[i] = new IPCNETPointInfo_st();
							Points[i].BitID = jppi.getInt("BitID");
	//						Points[i].Speed = jppi.getInt("Speed");
							Points[i].Desc = jppi.getString("Desc");
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	Log.d(TAG,"PointsCount:" + PointsCount);
	    	return true;
	    }
	    public String toJSONString(){
			JSONObject jsroot = new JSONObject();
			try {
				JSONObject jsresult = new JSONObject();
				
				JSONArray jsaPoints = new JSONArray();
				for(int i=0;i<PointsCount;i++){
					JSONObject jsPoints = new JSONObject();
					jsPoints.put("BitID",Points[i].BitID);
//					jsPoints.put("Speed",Points[i].Speed);
					jsPoints.put("Desc",Points[i].Desc);
					
					JSONObject jppi = new JSONObject();
					jppi.put("PtzPreBit.info", jsPoints);
					
					jsaPoints.put(jppi);
				}
				jsresult.put("Points", jsaPoints);
				jsresult.put("PointsCount", PointsCount);
				
				jsroot.put("PrePointList.info", jsresult);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			
			return jsroot.toString();
	    }
	}
	public class IPCPtzBaseMsg_st{
		public int PtzEnable;//ptz 是否启用
		public int BitEnable;//预置位是否启用
		public int StartCenterEnable;//重启后自动居中
		public int Speed;
		public int RunTimes;
		public int LevelMaxTimes;
		public int LevelMidTimes;
		public int VertMaxTimes;
		public int VertMidTimes;             
	}
	public class IPCPtzBitMsg_st{
		public int UseNum;     
	}
	public class IPCPtzCmd_st{
	    public int MotoCmd;            
	    public int OneStep;
	    public IPCPtzBaseMsg_st PtzBaseMsg = new IPCPtzBaseMsg_st();
	    public IPCPtzBitMsg_st  PtzBitMsg = new IPCPtzBitMsg_st();
	    
	    public boolean parseJSON(JSONObject jsdata){
	    	try {
				JSONObject jsroot= jsdata.getJSONObject("PTZInfo");
				if(jsroot!=null){
					MotoCmd = jsroot.getInt("MotoCmd");
					OneStep = jsroot.getInt("OneStep");
					
					JSONObject jsPtzBaseMsg = jsroot.getJSONObject("PtzBaseMsg");
					PtzBaseMsg.PtzEnable = jsPtzBaseMsg.getInt("PtzEnable");
					PtzBaseMsg.BitEnable = jsPtzBaseMsg.getInt("BitEnable");
					PtzBaseMsg.StartCenterEnable = jsPtzBaseMsg.getInt("StartCenterEnable");
					PtzBaseMsg.Speed = jsPtzBaseMsg.getInt("Speed");
					PtzBaseMsg.RunTimes = jsPtzBaseMsg.getInt("RunTimes");
					PtzBaseMsg.LevelMaxTimes = jsPtzBaseMsg.getInt("LevelMaxTimes");
					PtzBaseMsg.LevelMidTimes = jsPtzBaseMsg.getInt("LevelMidTimes");
					PtzBaseMsg.VertMaxTimes = jsPtzBaseMsg.getInt("VertMaxTimes");
					PtzBaseMsg.VertMidTimes = jsPtzBaseMsg.getInt("VertMidTimes");

					JSONObject jsPtzBitMsg = jsroot.getJSONObject("PtzBitMsg");
					PtzBitMsg.UseNum = jsPtzBitMsg.getInt("UseNum");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    public String toJSONString(){
			JSONObject jsroot = new JSONObject();
			try {
				JSONObject jsresult = new JSONObject();
				
				jsresult.put("MotoCmd", MotoCmd);
				jsresult.put("OneStep", OneStep);
				
				JSONObject jsPtzBaseMsg = new JSONObject();
				jsPtzBaseMsg.put("PtzEnable", PtzBaseMsg.PtzEnable);
				jsPtzBaseMsg.put("BitEnable", PtzBaseMsg.BitEnable);
				jsPtzBaseMsg.put("StartCenterEnable", PtzBaseMsg.StartCenterEnable);
				jsPtzBaseMsg.put("Speed", PtzBaseMsg.Speed);
				jsPtzBaseMsg.put("RunTimes", PtzBaseMsg.RunTimes);
				jsPtzBaseMsg.put("LevelMaxTimes", PtzBaseMsg.LevelMaxTimes);
				jsPtzBaseMsg.put("LevelMidTimes", PtzBaseMsg.LevelMidTimes);
				jsPtzBaseMsg.put("VertMaxTimes", PtzBaseMsg.VertMaxTimes);
				jsPtzBaseMsg.put("VertMidTimes", PtzBaseMsg.VertMidTimes);
				jsresult.put("PtzBaseMsg", jsPtzBaseMsg);

				JSONObject jsPtzBitMsg = new JSONObject();
				jsPtzBitMsg.put("UseNum", PtzBitMsg.UseNum);
				jsresult.put("PtzBitMsg", jsPtzBitMsg);
				
				jsroot.put("PTZInfo", jsresult);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			
			return jsroot.toString();
	    }
	}
	
	public class IPCNetGetOsdCfg_st{
	    public int Vich;
	    public boolean parseJSON(JSONObject jsdata){
	    	try {
				JSONObject jsroot= jsdata.getJSONObject("OsdGetCfg.info");
				if(jsroot!=null){
					Vich = jsroot.getInt("Vich");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    public String toJSONString(){
			JSONObject jsroot = new JSONObject();
			try {
				JSONObject jsresult = new JSONObject();
				jsresult.put("Vich", Vich);
				
				jsroot.put("OsdGetCfg.info", jsresult);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			
			return jsroot.toString();
	    }
	}
	
	public class IPCNetOsdConf_st{
	    public boolean Enable;       //是否启用
	    public int DisplayMode;  //字体与背景是否反色，0-不反钯，1-反色
	    public int Xcord;        //x坐标0-704
	    public int Ycord;        //y坐标0-576
	}

	public class IPCNetOsdName_st{
		public String NameText = "";
		public IPCNetOsdConf_st NameConf = new IPCNetOsdConf_st();
	}

	public class IPCNetOsdCfg_st{
	    public boolean SetDefault;
	    public int Vich;
	    public IPCNetOsdName_st OsdNameInfo = new IPCNetOsdName_st();
	    public IPCNetOsdConf_st OsdDateInfo = new IPCNetOsdConf_st();
	    public IPCNetOsdConf_st OsdRateInfo = new IPCNetOsdConf_st();
	    public boolean parseJSON(JSONObject jsdata){
	    	try {
				JSONObject jsroot = jsdata.getJSONObject("OsdCfg.info");
				if(jsroot!=null){
					SetDefault = jsroot.getBoolean("SetDefault");
					Vich = jsroot.getInt("Vich");
					
					JSONObject jsOsdNameInfo = jsroot.getJSONObject("OsdNameInfo");
					OsdNameInfo.NameText = jsOsdNameInfo.getString("NameText");
					JSONObject jsOsdNameInfo_NameConf = jsOsdNameInfo.getJSONObject("NameConf");
					OsdNameInfo.NameConf.Enable = jsOsdNameInfo_NameConf.getBoolean("Enable");       //是否启用
					OsdNameInfo.NameConf.DisplayMode = jsOsdNameInfo_NameConf.getInt("DisplayMode");  //字体与背景是否反色，0-不反钯，1-反色
					OsdNameInfo.NameConf.Xcord = jsOsdNameInfo_NameConf.getInt("Xcord");        //x坐标0-704
					OsdNameInfo.NameConf.Ycord = jsOsdNameInfo_NameConf.getInt("Ycord");
				    
					JSONObject jsOsdDateInfo = jsroot.getJSONObject("OsdDateInfo");
					OsdDateInfo.Enable = jsOsdDateInfo.getBoolean("Enable");       //是否启用
					OsdDateInfo.DisplayMode = jsOsdDateInfo.getInt("DisplayMode");  //字体与背景是否反色，0-不反钯，1-反色
					OsdDateInfo.Xcord = jsOsdDateInfo.getInt("Xcord");        //x坐标0-704
					OsdDateInfo.Ycord = jsOsdDateInfo.getInt("Ycord");
					
					JSONObject jsOsdRateInfo = jsroot.getJSONObject("OsdRateInfo");
					OsdRateInfo.Enable = jsOsdRateInfo.getBoolean("Enable");       //是否启用
					OsdRateInfo.DisplayMode = jsOsdRateInfo.getInt("DisplayMode");  //字体与背景是否反色，0-不反钯，1-反色
					OsdRateInfo.Xcord = jsOsdRateInfo.getInt("Xcord");        //x坐标0-704
					OsdRateInfo.Ycord = jsOsdRateInfo.getInt("Ycord");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    public String toJSONString(){
			JSONObject jsroot = new JSONObject();
			try {
				JSONObject jsresult = new JSONObject();
				jsresult.put("Vich", Vich);
				jsresult.put("SetDefault",SetDefault);
				
				JSONObject jsOsdNameInfo = new JSONObject();

				try {
					jsOsdNameInfo.put("NameText",new String(OsdNameInfo.NameText.getBytes(), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				JSONObject jsOsdNameInfo_NameConf = new JSONObject();
				jsOsdNameInfo_NameConf.put("Enable",OsdNameInfo.NameConf.Enable);       //是否启用
				jsOsdNameInfo_NameConf.put("DisplayMode",OsdNameInfo.NameConf.DisplayMode);  //字体与背景是否反色，0-不反钯，1-反色
				jsOsdNameInfo_NameConf.put("Xcord",OsdNameInfo.NameConf.Xcord);        //x坐标0-704
				jsOsdNameInfo_NameConf.put("Ycord",OsdNameInfo.NameConf.Ycord);
				jsOsdNameInfo.put("NameConf",jsOsdNameInfo_NameConf);
				
				jsresult.put("OsdNameInfo",jsOsdNameInfo);
				
				JSONObject jsOsdDateInfo = new JSONObject();
				jsOsdDateInfo.put("Enable",OsdDateInfo.Enable);       //是否启用
				jsOsdDateInfo.put("DisplayMode",OsdDateInfo.DisplayMode);  //字体与背景是否反色，0-不反钯，1-反色
				jsOsdDateInfo.put("Xcord",OsdDateInfo.Xcord);        //x坐标0-704
				jsOsdDateInfo.put("Ycord",OsdDateInfo.Ycord);
				jsresult.put("OsdDateInfo",jsOsdDateInfo);
				
				JSONObject jsOsdRateInfo = new JSONObject();
				jsOsdRateInfo.put("Enable",OsdRateInfo.Enable);       //是否启用
				jsOsdRateInfo.put("DisplayMode",OsdRateInfo.DisplayMode);  //字体与背景是否反色，0-不反钯，1-反色
				jsOsdRateInfo.put("Xcord",OsdRateInfo.Xcord);        //x坐标0-704
				jsOsdRateInfo.put("Ycord",OsdRateInfo.Ycord);
				jsresult.put("OsdRateInfo",jsOsdRateInfo);
				
				jsroot.put("OsdCfg.info", jsresult);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			
			return jsroot.toString();
	    }
	}
	public class IPCNetAntiFlickerInfo_st{
		public boolean Enable; 	  /**是否开启抗闪烁模式**/ 
		public int Frequency; /****0-60hz,1-50hz*****/
		public int Mode;     /***0-灯光环境，1-无灯光高亮度环境****/
	    public boolean parseJSON(JSONObject jsdata){
	    	try {
				JSONObject jsroot= jsdata.getJSONObject("IspAntiFlk.info");
				if(jsroot!=null){
					Enable = jsroot.getBoolean("Enable");
					Frequency = jsroot.getInt("Frequency");
					Mode = jsroot.getInt("Mode");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    public String toJSONString(){
			JSONObject jsroot = new JSONObject();
			try {
				JSONObject jsresult = new JSONObject();
				jsresult.put("Enable", Enable);
				jsresult.put("Frequency", Frequency);
				jsresult.put("Mode", Mode);
				
				jsroot.put("IspAntiFlk.info", jsresult);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			
			return jsroot.toString();
	    }
	}
	public class IPCNetUserInfo_st{
	    public String Op = "Change";//32bytes
	    public String Passwd = "admin";//64bytes.
	    public boolean parseJSON(JSONObject jsdata){
	    	try {
				JSONObject jsroot= jsdata.getJSONObject("User.Cfg");
				if(jsroot!=null){
					Op = jsroot.getString("Op");
					Passwd = jsroot.getString("Passwd");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    public String toJSONString(){
			JSONObject jsroot = new JSONObject();
			try {
				JSONObject jsresult = new JSONObject();
				jsresult.put("Op", Op);
				
				//encode password
				int encoded_pw_len = Passwd.length()*2;
				byte[] encoded_pw = new byte[encoded_pw_len];
				String encoded_pw_str = "null";
				NativeCaller.EncodePasswd(Passwd, encoded_pw,encoded_pw_len);
				try {
					encoded_pw_str = new String(encoded_pw,"UTF-8");
					Log.d(TAG,"encoded_pw:" + encoded_pw_str);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				jsresult.put("Passwd", encoded_pw_str);
				
				jsroot.put("User.Cfg", jsresult);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			
			return jsroot.toString();
	    }
	}
	
	public class IPCNetAutoUpgrade_st{
	    public boolean AutoUpgrade;
	    public boolean parseJSON(JSONObject jsdata){
	    	try {
				JSONObject jsroot= jsdata.getJSONObject("AutoUpgradeSet.info");
				if(jsroot!=null){
					AutoUpgrade = jsroot.getBoolean("AutoUpgrade");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    public String toJSONString(){
			JSONObject jsroot = new JSONObject();
			try {
				JSONObject jsresult = new JSONObject();
				jsresult.put("AutoUpgrade", AutoUpgrade);
				
				jsroot.put("AutoUpgradeSet.info", jsresult);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			
			return jsroot.toString();
	    }
	}

	public class IPCNetUpgradeCfg_st{
		public String  UpgradeUrl;
		public String  SystemType;
		public String  CustomType;
		public String  VendorType;
		public IPCNetAutoUpgrade_st AutoUpgrade = new IPCNetAutoUpgrade_st();
	    
	    public boolean parseJSON(JSONObject jsdata){
	    	try {
				JSONObject jsroot= jsdata.getJSONObject("UpgradeCfg.info");
				if(jsroot!=null){
					UpgradeUrl = jsroot.getString("UpgradeUrl");
					byte[] decode = new byte[256];
					NativeCaller.DecodeURL(UpgradeUrl, decode, decode.length);
					int i=0;
					for(;i<decode.length;i++){
						if(decode[i]==0)break;
					}
					byte[] strbin = new byte[i];
					System.arraycopy(decode, 0, strbin, 0, i);
					UpgradeUrl = new String(strbin);					
//					Log.d(TAG,"i:" + i + " UpgradeUrl:" + UpgradeUrl);
					SystemType = jsroot.getString("SystemType");
					CustomType = jsroot.getString("CustomType");
					VendorType = jsroot.getString("VendorType");
					
					JSONObject jAutoUpgrade = jsroot.getJSONObject("AutoUpgrade");
					AutoUpgrade.AutoUpgrade = jAutoUpgrade.getBoolean("AutoUpgrade");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    public String toJSONString(){
			JSONObject jsroot = new JSONObject();
			try {
				JSONObject jsresult = new JSONObject();
				jsresult.put("UpgradeUrl", UpgradeUrl);
				jsresult.put("SystemType", SystemType);
				jsresult.put("CustomType", CustomType);
				jsresult.put("VendorType", VendorType);
				
				JSONObject jAutoUpgrade = new JSONObject();
				jAutoUpgrade.put("AutoUpgrade", AutoUpgrade.AutoUpgrade);
				jsresult.put("AutoUpgrade", jAutoUpgrade);
				
				jsroot.put("UpgradeCfg.info", jsresult);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			
			return jsroot.toString();
	    }
	}
	
	public class ServerSystemUpgradeFileMsg_st{
		public String Version;
		public String UsrCheck;
		public String SystemCheck;
		public String WebCheck;
		public String AutoUpdateTimeMode;
		public int AutoUpdateTime;
		public boolean ForceFlag;
		public String ForceStart;
		public String ForceEnd;
		public String InstructionName;
	}
	public class DeviceSystemFirmwareUpateInfo{				
		public ServerSystemUpgradeFileMsg_st[] Direct = new ServerSystemUpgradeFileMsg_st[2];
		
		public boolean parseJSON(JSONObject jsdata){
	    	try {
	    		JSONArray jsroot= jsdata.getJSONArray("Direct");
				if(jsroot!=null){
					Direct = new ServerSystemUpgradeFileMsg_st[jsroot.length()];
					for(int i=0;i<jsroot.length();i++){
						JSONObject jServerUpgradeFileMsg_st = jsroot.getJSONObject(i);
						Direct[i] = new ServerSystemUpgradeFileMsg_st();
						Direct[i].Version = jServerUpgradeFileMsg_st.getString("Version");
						Direct[i].UsrCheck = jServerUpgradeFileMsg_st.getString("UsrCheck");
						Direct[i].SystemCheck = jServerUpgradeFileMsg_st.getString("SystemCheck");
						Direct[i].WebCheck = jServerUpgradeFileMsg_st.getString("WebCheck");
						Direct[i].AutoUpdateTimeMode = jServerUpgradeFileMsg_st.getString("AutoUpdateTimeMode");
						Direct[i].AutoUpdateTime = jServerUpgradeFileMsg_st.getInt("AutoUpdateTime");
						Direct[i].ForceFlag = jServerUpgradeFileMsg_st.getBoolean("ForceFlag");
						Direct[i].ForceStart = jServerUpgradeFileMsg_st.getString("ForceStart");
						Direct[i].ForceEnd = jServerUpgradeFileMsg_st.getString("ForceEnd");
						Direct[i].InstructionName = jServerUpgradeFileMsg_st.getString("InstructionName");
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    public String toJSONString(){
			JSONObject jsroot = new JSONObject();
			try {
				JSONArray jDirect = new JSONArray();
				for(int i=0;i<Direct.length;i++){
					JSONObject jsresult = new JSONObject();
					jsresult.put("Version",Direct[i].Version);
					jsresult.put("UsrCheck",Direct[i].UsrCheck);
					jsresult.put("SystemCheck",Direct[i].SystemCheck);
					jsresult.put("WebCheck",Direct[i].WebCheck);
					jsresult.put("AutoUpdateTimeMode",Direct[i].AutoUpdateTimeMode);
					jsresult.put("AutoUpdateTime",Direct[i].AutoUpdateTime);
					jsresult.put("ForceFlag",Direct[i].ForceFlag);
					jsresult.put("ForceStart",Direct[i].ForceStart);
					jsresult.put("ForceEnd",Direct[i].ForceEnd);
					jsresult.put("InstructionName",Direct[i].InstructionName);
					
					jDirect.put(i, jsresult);
				}
				jsroot.put("Direct", jDirect);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			
			return jsroot.toString();
	    }
	}
	
	public class ServerCustomUpgradeFileMsg_st{
		public String Version;
		public boolean ForceFlag;
		public String ForceStart;
		public String ForceEnd;
		public String CustomCheck;
		public String InstructionName;
	}
	public class DeviceCustomFirmwareUpateInfo{				
		public ServerCustomUpgradeFileMsg_st[] Direct = new ServerCustomUpgradeFileMsg_st[2];
		
		public boolean parseJSON(JSONObject jsdata){
	    	try {
	    		JSONArray jsroot= jsdata.getJSONArray("Direct");
				if(jsroot!=null){
					Direct = new ServerCustomUpgradeFileMsg_st[jsroot.length()];
					for(int i=0;i<jsroot.length();i++){
						JSONObject jServerUpgradeFileMsg_st = jsroot.getJSONObject(i);
						Direct[i] = new ServerCustomUpgradeFileMsg_st();
						Direct[i].Version = jServerUpgradeFileMsg_st.getString("Version");
						Direct[i].ForceFlag = jServerUpgradeFileMsg_st.getBoolean("ForceFlag");
						Direct[i].ForceStart = jServerUpgradeFileMsg_st.getString("ForceStart");
						Direct[i].ForceEnd = jServerUpgradeFileMsg_st.getString("ForceEnd");
						Direct[i].CustomCheck = jServerUpgradeFileMsg_st.getString("CustomCheck");
						Direct[i].InstructionName = jServerUpgradeFileMsg_st.getString("InstructionName");
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    public String toJSONString(){
			JSONObject jsroot = new JSONObject();
			try {
				JSONArray jDirect = new JSONArray();
				for(int i=0;i<Direct.length;i++){
					JSONObject jsresult = new JSONObject();
					jsresult.put("Version",Direct[i].Version);
					jsresult.put("ForceFlag",Direct[i].ForceFlag);
					jsresult.put("ForceStart",Direct[i].ForceStart);
					jsresult.put("ForceEnd",Direct[i].ForceEnd);
					jsresult.put("CustomCheck",Direct[i].CustomCheck);
					jsresult.put("InstructionName",Direct[i].InstructionName);
					
					jDirect.put(i, jsresult);
				}
				jsroot.put("Direct", jDirect);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			
			return jsroot.toString();
	    }
	}
	
	public class ServerVendorUpgradeFileMsg_st{
		public String Version;
		public boolean ForceFlag;
		public String ForceStart;
		public String ForceEnd;
		public String VendorCheck;
		public String InstructionName;
	}
	public class DeviceVendorFirmwareUpateInfo{				
		public ServerVendorUpgradeFileMsg_st[] Direct = new ServerVendorUpgradeFileMsg_st[2];
		
		public boolean parseJSON(JSONObject jsdata){
	    	try {
	    		JSONArray jsroot= jsdata.getJSONArray("Direct");
				if(jsroot!=null){
					Direct = new ServerVendorUpgradeFileMsg_st[jsroot.length()];
					for(int i=0;i<jsroot.length();i++){
						JSONObject jServerUpgradeFileMsg_st = jsroot.getJSONObject(i);
						Direct[i] = new ServerVendorUpgradeFileMsg_st();
						Direct[i].Version = jServerUpgradeFileMsg_st.getString("Version");
						Direct[i].ForceFlag = jServerUpgradeFileMsg_st.getBoolean("ForceFlag");
						Direct[i].ForceStart = jServerUpgradeFileMsg_st.getString("ForceStart");
						Direct[i].ForceEnd = jServerUpgradeFileMsg_st.getString("ForceEnd");
						Direct[i].VendorCheck = jServerUpgradeFileMsg_st.getString("VendorCheck");
						Direct[i].InstructionName = jServerUpgradeFileMsg_st.getString("InstructionName");
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    public String toJSONString(){
			JSONObject jsroot = new JSONObject();
			try {
				JSONArray jDirect = new JSONArray();
				for(int i=0;i<Direct.length;i++){
					JSONObject jsresult = new JSONObject();
					jsresult.put("Version",Direct[i].Version);
					jsresult.put("ForceFlag",Direct[i].ForceFlag);
					jsresult.put("ForceStart",Direct[i].ForceStart);
					jsresult.put("ForceEnd",Direct[i].ForceEnd);
					jsresult.put("VendorCheck",Direct[i].VendorCheck);
					jsresult.put("InstructionName",Direct[i].InstructionName);
					
					jDirect.put(i, jsresult);
				}
				jsroot.put("Direct", jDirect);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			
			return jsroot.toString();
	    }
	}
	
	public class JianleUpgradeSystemInfo_st{
	    public String version;
	    public String usrcheck;
	    public String systemcheck;
	    public String webcheck;
	}
	public class JianleUpgradeCustomInfo_st{
		public String version;
		public String customcheck;
	}
	public class JianleUpgradeVendorInfo_st{
		public String version;
		public String vendorcheck;
	}
	public class IPCNetUpgradeInfo_st{
		public JianleUpgradeSystemInfo_st UpgradeSystemInfo = new JianleUpgradeSystemInfo_st();
		public JianleUpgradeCustomInfo_st UpgradeCustomInfo = new JianleUpgradeCustomInfo_st();
	    public JianleUpgradeVendorInfo_st UpgradeVendorInfo = new JianleUpgradeVendorInfo_st();
	    public boolean parseJSON(JSONObject jsdata){
	    	try {
	    		JSONObject jsroot= jsdata.getJSONObject("Upgrade.info");
				if(jsroot!=null){
					JSONObject info = jsroot.getJSONObject("UpgradeSystemInfo");
					UpgradeSystemInfo.version = info.getString("version");
					UpgradeSystemInfo.usrcheck = info.getString("usrcheck");
					UpgradeSystemInfo.systemcheck = info.getString("systemcheck");
					UpgradeSystemInfo.webcheck = info.getString("webcheck");
					
					info = jsroot.getJSONObject("UpgradeCustomInfo");
					UpgradeCustomInfo.version = info.getString("version");
					UpgradeCustomInfo.customcheck = info.getString("customcheck");
					
					info = jsroot.getJSONObject("UpgradeVendorInfo");
					UpgradeVendorInfo.version = info.getString("version");
					UpgradeVendorInfo.vendorcheck = info.getString("vendorcheck");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    public String toJSONString(){
			JSONObject jsroot = new JSONObject();
			try {
				JSONObject jresult = new JSONObject();
				
				JSONObject info = new JSONObject();
				info.put("version",UpgradeSystemInfo.version);
				info.put("usrcheck",UpgradeSystemInfo.usrcheck);
				info.put("systemcheck",UpgradeSystemInfo.systemcheck);
				info.put("webcheck",UpgradeSystemInfo.webcheck);
				jresult.put("UpgradeSystemInfo", info);
				
				info = new JSONObject();
				info.put("version",UpgradeCustomInfo.version);
				info.put("customcheck",UpgradeCustomInfo.customcheck);
				jresult.put("UpgradeCustomInfo", info);
				
				info = new JSONObject();
				info.put("version",UpgradeVendorInfo.version);
				info.put("vendorcheck",UpgradeVendorInfo.vendorcheck);
				jresult.put("UpgradeVendorInfo", info);
				
				jsroot.put("Upgrade.info", jresult);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			
			return jsroot.toString();
	    }
	}
	
	public class IPCNetAlarmMsgReport_st{
		public int AlarmType;
		public int Idx;
		public int Val;
		public IPCNetDate_st AlarmDate = new IPCNetDate_st();
		public IPCNetTime_st AlarmTime = new IPCNetTime_st();
		public boolean parseJSON(JSONObject jsdata){
	    	try {
	    		JSONObject jsroot= jsdata.getJSONObject("AlarmReport.info");
				if(jsroot!=null){
					AlarmType = jsroot.getInt("AlarmType");
					Idx = jsroot.getInt("Idx");
					Val = jsroot.getInt("Val");
					
					JSONObject info = jsroot.getJSONObject("AlarmDate");
					AlarmDate.Day = info.getInt("Day");
					AlarmDate.Mon = info.getInt("Mon");
					AlarmDate.Year = info.getInt("Year");
					
					info = jsroot.getJSONObject("AlarmTime");
					AlarmTime.Hour = info.getInt("Hour");
					AlarmTime.Min = info.getInt("Min");
					AlarmTime.Sec = info.getInt("Sec");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    public String toJSONString(){
			JSONObject jsroot = new JSONObject();
			try {
				JSONObject jresult = new JSONObject();
				
				JSONObject info = new JSONObject();
				info.put("Day",AlarmDate.Day);
				info.put("Mon",AlarmDate.Mon);
				info.put("Year",AlarmDate.Year);
				jresult.put("AlarmDate", info);
				
				info = new JSONObject();
				info.put("Hour",AlarmTime.Hour);
				info.put("Min",AlarmTime.Min);
				info.put("Sec",AlarmTime.Sec);
				jresult.put("AlarmTime", info);
				
				jsroot.put("AlarmReport.info", jresult);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			
			return jsroot.toString();
	    }
	}
	
	public class IPCNetRecordGetCfg_st{
	    public int ViCh;//sensor index.
	    public String Path;
	    public boolean parseJSON(JSONObject jsdata){
	    	try {
	    		JSONObject jsroot= jsdata.getJSONObject("Rec.Conf");
				if(jsroot!=null){
					ViCh = jsroot.getInt("ViCh");
					Path = jsroot.getString("Path");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
	    	return true;
	    }
	    public String toJSONString(){
			JSONObject jsroot = new JSONObject();
			try {
				JSONObject jresult = new JSONObject();
				jresult.put("ViCh", ViCh);
				jresult.put("Path", Path);
				
				jsroot.put("Rec.Conf", jresult);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			
			return jsroot.toString();
	    }
	}
	
	public class IPCNetRecordTiming_st{
	    public boolean En;
	    public String St1;
	    public String Ed1;
	    public String St2;
	    public String Ed2;
	}
	public class IPCNetDiskInfo_st{
		public String Path;//media file path the recorder to store.
	    public boolean isValid;
		public int Type;//sdcard,hard disk,net disk or ?
	    public int Total;//mega bytes.
		public int Free;//mega bytes.
	}

	public class IPCNetRecordCfg_st{
	    public int ViCh;
	    public boolean AutoDel;
	    public int RecMins;
	    public int RecMinsOption[] = new int[8];//[8]
	    public IPCNetRecordTiming_st RecTime[] = new IPCNetRecordTiming_st[8];//[8];
		public IPCNetDiskInfo_st DiskInfo = new IPCNetDiskInfo_st();
		
		public int PackageType;//0:avi,1:mjpg(vi.encode_type == 1 is needed.),2:mkv
		public int Mode;//0:length means time, 1:length means file size.	
		//T_S32 Duration;//if mode is 0,then 1000 means 00:10:00.	
		public long  ReserveSize;
//		#define RECYCLE_MODE_OVER_WRITE 0
//		#define RECYCLE_MODE_KEEP 1	
		//short RecycleMode;
		
		public boolean parseJSON(JSONObject jsonData){
	    	
    		JSONObject recorder = null;
			try {
				recorder = jsonData.getJSONObject("Rec.Conf");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(recorder!=null){
				JSONObject diskinfo = null;
				try {
					diskinfo = recorder.getJSONObject("DiskInfo");
					DiskInfo.Total = diskinfo.getInt("Total");
					DiskInfo.Free = diskinfo.getInt("Free");
					DiskInfo.Path = diskinfo.getString("Path");
					DiskInfo.Type = diskinfo.getInt("Type");
					DiskInfo.isValid = diskinfo.getBoolean("isValid");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					JSONArray jRecMinsOption = recorder.getJSONArray("RecMinsOption");
					RecMins = recorder.getInt("RecMins");
					
					int count = jRecMinsOption.length();
					for(int i=0;i<count;i++){
						RecMinsOption[i] = jRecMinsOption.getInt(i);
					}
					if(RecMins>=count)
						RecMins = count-1;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					AutoDel = recorder.getBoolean("AutoDel");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				try {
					JSONArray jRecordTime = recorder.getJSONArray("RecTime");
					int count = jRecordTime.length();
					for(int i =0;i<count;i++){
						RecTime[i] = new IPCNetRecordTiming_st();
						
						JSONObject duration = jRecordTime.getJSONObject(i);
						RecTime[i].En = duration.getInt("En") == 0?false:true;
						RecTime[i].St1 = duration.getString("St1");
						RecTime[i].Ed1 = duration.getString("Ed1");
						RecTime[i].St2 = duration.getString("St2");
						RecTime[i].Ed2 = duration.getString("Ed2");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					PackageType = recorder.getInt("PackageType");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					ReserveSize = recorder.getInt("ReserveSize");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
	    	return true;
	    }
	    public String toJSONString(){
//			JSONObject jsroot = new JSONObject();
//			try {
//				JSONObject jresult = new JSONObject();
//				jresult.put("ViCh", ViCh);
//				
//				jsroot.put("Rec.Conf", jresult);
//			} catch (JSONException e) {
//				e.printStackTrace();
//				return null;
//			}
//			
//			return jsroot.toString();
	    	return null;
	    }
	}
}
