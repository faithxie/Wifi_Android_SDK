
package freesbell.demo.content;


public class ContentCommon{
	public static final String STR_CAMERA_INFO_RECEIVER = "object.ipcam.client.camerainforeceiver" ;
	public static final String STR_CAMERA_ADDR = "camera_addr" ;
	public static final String STR_CAMERA_PORT = "camera_port" ;
	public static final String STR_CAMERA_NAME = "camera_name" ;
	public static final String STR_CAMERA_MAC = "camera_mac" ;
	public static final String STR_CAMERA_USER = "camera_user" ;
	public static final String STR_CAMERA_PWD = "camera_pwd" ;
	public static final String STR_CAMERA_ID = "cameraid";
	public static final String STR_DEVICE_TYPE = "device_type" ;
	public static final String STR_CAMERA_SNAPSHOT = "camera_snapshot" ;
	public static final String STR_CAMERA_SENSOR_SNAPSHOT = "sensor_snapshot" ;
	public static final String STR_CAMERA_USER_AUTHORITY="camera_user_authority";
	
	public static final String STR_CAMERA_CONTROL_APP_ACTION = "app_action";
	public static final String STR_CAMERA_CONTROL_APP_ACTION_TAKE_VIDEO = "TAKE_VIDEO";
	public static final String STR_CAMERA_CONTROL_APP_ACTION_TAKE_PIC = "TAKE_PIC";
	public static final String STR_CAMERA_APP_ACTION_VAL = "app_action_val";
	
	public static final String STR_HW_ID="hw_rc_id";
	public static final String STR_SUBDEV_ID="subdev_id";
	public static final String STR_ADD_SUBDEV_FLAG="subdev_add";
	
	public static final String STR_CAMERA_OLD_ADDR = "camera_old_addr" ;
	public static final String STR_CAMERA_OLD_PORT = "camera_old_port" ;
	public static final String STR_CAMERA_OLD_ID = "camera_old_id";
	
	public static final String STR_CAMERA_TYPE = "camera_type";
	public static final String STR_CAMERA_SENSOR_TYPE = "sensor_type";
	public static final String STR_STREAM_TYPE = "stream_type";
	public static final String STR_H264_MAIN_STREAM = "h264_main_stream";
	public static final String STR_H264_SUB_STREAM = "h264_sub_stream";
	public static final String STR_MJPEG_SUB_STREAM = "mjpeg_sub_stream";
	
	public static final String STR_CAMERA_ALARM_AREA = "camera_alarm_area";
	public static final String STR_CAMERA_ALARM_CHANNEL = "camera_alarm_ch";
	public static final String STR_CAMERA_ALARM_SENSITIVE = "camera_alarm_sen";
	
	public static final int DEFAULT_PORT = 81;
	public static final String DEFAULT_USER_NAME = "admin" ;
	public static final String DEFAULT_USER_PWD = "admin" ;
	
	public static final String CAMERA_OPTION = "camera_option" ;
	public static final int ADD_CAMERA = 1;
	public static final int EDIT_CAMERA = 2;
	public static final int CHANGE_CAMERA_USER=3;
	public static final int DEL_CAMERA=4;
	public static final int INVALID_OPTION = 0xffff;	
	
	public static final int CAMERA_TYPE_UNKNOW = 0;
	public static final int CAMERA_TYPE_MJPEG = 1;
	public static final int CAMERA_TYPE_H264 = 2;
	
	public static final int H264_MAIN_STREAM = 0;
	public static final int H264_SUB_STREAM = 1;
	public static final int MJPEG_SUB_STREAM = 3;
	
	public static final String CAMERA_INTENT_STATUS_CHANGE = "camera_status_change" ;
	public static final String CAMERA_INTENT_MEDIA_CHANGE = "camera_media_change" ;
	public static final String CAMERA_INTENT_REMOTE_DEV_CONTROL = "remote_dev_control" ;
	
	//=======pppppp==============================================
	
	public static final int PPPP_DEV_TYPE_UNKNOWN = 0xffffffff;
	
	public static final String STR_PPPP_STATUS = "pppp_status";	
	
	public static final int P2P_STATUS_CONNECTING = 0 ;/* connecting */
	public static final int P2P_STATUS_INITIALING = 1 ;/* initialing */
	public static final int P2P_STATUS_ON_LINE = 2 ;/* on line */
	public static final int P2P_STATUS_CONNECT_FAILED = 3 ;/* connect failed */
	public static final int P2P_STATUS_DISCONNECT = 4 ;/*connect is off*/
	public static final int P2P_STATUS_INVALID_ID = 5;
	public static final int P2P_STATUS_DEVICE_NOT_ON_LINE = 6;
	public static final int P2P_STATUS_CONNECT_TIMEOUT = 7;
	public static final int P2P_STATUS_INVALID_USER = 8;
	public static final int P2P_STATUS_INVALID_PWD = 9;
	public static final int P2P_STATUS_INVALID_USER_PWD = 10;
	public static final int P2P_STATUS_LOGINED = 11;
	public static final int P2P_STATUS_UNKNOWN = -1;
	
	
	public static final int P2P_MSG_TYPE_P2P_STATUS = 0;
	public static final int P2P_MSG_TYPE_P2P_MODE = 1;
	public static final int P2P_MSG_TYPE_STREAM = 2;
	public static final int P2P_MSG_TYPE_P2P_DEV_TYPE = 4;
	public static final int P2P_MSG_TYPE_INVALID_MSG = 0xffffffff;
	
	public static final int P2P_STREAM_TYPE_H264 = 0;
	public static final int P2P_STREAM_TYPE_JPEG = 1;
	
	public static final int P2P_MODE_UNKNOWN = 0xffffffff;
	public static final int P2P_MODE_P2P_NORMAL = 1;
	public static final int P2P_MODE_P2P_RELAY = 2;
	
	public static final int P2P_CLIENT_TYPE_USER = 1;
	public static final int P2P_CLIENT_TYPE_CAMERA = 2;
	public static final int P2P_CLIENT_TYPE_WIFI_COM_CONTROLLER = 3;


//	public static final int P2P_MCU_RESOURCE_REQ = 1;
//	public static final int P2P_MCU_RESOURCE_RESP = P2P_MCU_RESOURCE_REQ + 1;
//	
//	public static final int P2P_MCU_DATA_REQ = P2P_MCU_RESOURCE_REQ + 2;
//	public static final int P2P_MCU_DATA_RESP = P2P_MCU_RESOURCE_REQ + 3;
	//ptz control command ---------------------------------

	public static final int CMD_PTZ_UP = 0;
	public static final int CMD_PTZ_UP_STOP = 1;
	public static final int CMD_PTZ_DOWN = 2;
	public static final int CMD_PTZ_DOWN_STOP = 3;
	public static final int CMD_PTZ_LEFT = 4;
	public static final int CMD_PTZ_LEFT_STOP = 5;
	public static final int CMD_PTZ_RIGHT = 6;
	public static final int CMD_PTZ_RIGHT_STOP = 7;
	public static final int CMD_PTZ_CENTER=8;//����
	public static final int CMD_PTZ_UP_DOWN=9;//��ֱѲ��
	public static final int CMD_PTZ_UP_DOWN_STOP=10;//��ֱѲ��ֹͣ
	public static final int CMD_PTZ_LEFT_RIGHT= 11;//ˮƽѲ��
	public static final int CMD_PTZ_LEFT_RIGHT_STOP=12;
	public static final int CMD_NET_PTZ_PREFAB_BIT_SET=13;
	public static final int CMD_NET_PTZ_PREFAB_BIT_RUN=14;
	public static final int CMD_NET_PTZ_LEFT_UP=15;//15
	public static final int CMD_NET_PTZ_RIGHT_UP=16;    
	public static final int CMD_NET_PTZ_LEFT_DOWN=17;
	public static final int CMD_NET_PTZ_RIGHT_DOWN=18;
	public static final int CMD_NET_PTZ_IO_HIGH=19;
	public static final int CMD_NET_PTZ_IO_LOW=20;//20
	public static final int CMD_NET_PTZ_IRCUT_HIGH=21;
	public static final int CMD_NET_PTZ_IRCUT_LOW=22;
	public static final int CMD_NET_PTZ_IO_SPEED=23;
	public static final int CMD_NET_PTZ_MOTO_LEVEL_MAX_TIMES=24;
	public static final int CMD_NET_PTZ_MOTO_LEVEL_MIN_TIMES=25;//25
	public static final int CMD_NET_PTZ_MOTO_LEVEL_MID_TIMES=26;
	public static final int CMD_NET_PTZ_MOTO_VERT_MIN_TIMES=27;
	public static final int CMD_NET_PTZ_MOTO_VERT_MAX_TIMES=28;
	public static final int CMD_NET_PTZ_MOTO_VERT_MID_TIMES=29;
	public static final int CMD_NET_PTZ_MOTO_TIMES_START=30;//30
	public static final int CMD_NET_GET_PRESET=31;
	public static final int CMD_NET_SET_PRESET=32;
	public static final int CMD_NET_MAX_LEVEL_TIMES=33;
	public static final int CMD_NET_MAX_VERT_TIMES=34;
	public static final int CMD_NET_PTZ_MOTO_TEST=35;//35
	public static final int CMD_NET_PTZ_IMPORTANT_PARAM=36;
	public static final int CMD_NET_PTZ_NORMAL_PARAM=37;
	
	public static final int CMD_PTZ_ORIGINAL=0;//ԭʼλ��
	public static final int CMD_PTZ_VERTICAL_MIRROR=1;//��ֱ����
	public static final int CMD_PTZ_HORIZONAL_MIRROR=2;//ˮƽ����
	public static final int CMD_PTZ_VERHOR_MIRROR=3;//ˮƽ��ֱ��ת
	
	
	
	public static final int MSG_TYPE_GET_CAMERA_PARAMS = 0x2 ;//��ȡ��Ƶ����
	public static final int  MSG_TYPE_DECODER_CONTROL = 0x3; //��̨���� 
	public static final int MSG_TYPE_GET_PARAMS=0x4; //��ȡ���磬WIFI,�û���Ϣ��FTP,DNS,MAIL,DATETIME,������Ϣ����
	public static final int MSG_TYPE_SNAPSHOT =0x5; //ץͼ
	public static final int  MSG_TYPE_CAMERA_CONTROL=0x6; //��Ƶ��������,����
	public static final int  MSG_TYPE_SET_NETWORK=0x7; //��������
	public static final int MSG_TYPE_REBOOT_DEVICE = 0x8; //�����豸
	public static final int  MSG_TYPE_RESTORE_FACTORY=0x9 ;//�ָ���������
	public static final int  MSG_TYPE_SET_USER = 0xa ;//�����û�
	public static final int  MSG_TYPE_SET_WIFI = 0xb; //����wifi
	public static final int  MSG_TYPE_SET_DATETIME=  0xc ;//ʱ������
	public static final int  MSG_TYPE_GET_STATUS   =  0xd; //��ȡ�����״̬
	public static final int  MSG_TYPE_GET_PTZ_PARAMS = 0xe ;//PTZ����
	public static final int  MSG_TYPE_SET_DDNS = 0xf; //DDNS����
	public static final int  MSG_TYPE_SET_MAIL=0x10 ;//�ʼ�����
	public static final int  MSG_TYPE_SET_FTP=0x11; //FTP����
	public static final int  MSG_TYPE_SET_ALARM =0x12; //������Ϣ
	public static final int MSG_TYPE_SET_PTZ = 0x13 ;//PTZ����
	public static final int  MSG_TYPE_WIFI_SCAN= 0x14; //WIFIɨ��
	public static final int MSG_TYPE_GET_ALARM_LOG=0x15;
	public static final int MSG_TYPE_GET_RECORD =0x16;
	public static final int MSG_TYPE_GET_RECORD_FILE=0x17;
	public static final int MSG_TYPE_SET_PPPOE =0x18;
	public static final int MSG_TYPE_SET_UPNP =0x19;
	public static final int MSG_TYPE_DEL_RECORD_FILE=0x1a;
	public static final int MSG_TYPE_SET_MEDIA =0x1b;
	public static final int MSG_TYPE_SET_RECORD_SCH=0x1c;
	public static final int MSG_TYPE_CLEAR_ALARM_LOG=0x1d;
	public static final int MSG_TYPE_WIFI_PARAMS=0x1f;
	public static final int MSG_TYPE_MAIL_PARAMS=0x20;
	public static final int MSG_TYPE_FTP_PARAMS=0x21;
	public static final int MSG_TYPE_NETWORK_PARAMS=0x22;
	public static final int MSG_TYPE_USER_INFO=0x23;
	public static final int MSG_TYPE_DDNS_PARAMS =0x24;
	public static final int MSG_TYPE_DATETIME_PARAMS=0x25;
	public static final int MSG_TYPE_ALARM_PARAMS= 0x26;
	public static final int MSG_TYPE_SET_DEVNAME =0x27;
	
	//========= camera picture configuration =======
	public static final int CAM_CFG_BRIGHTNESS = 0;
	public static final int CAM_CFG_COLOR_DEPTH = 1;
	public static final int CAM_CFG_CONTRAST = 2;
	public static final int CAM_CFG_SATURATION = 3;
	public static final int CAM_CFG_ACUTANCE = 4;
	public static final int CAM_CFG_SET_DEFAULT = 100;
	
	//=========alarm=================
	public static final int IPCNET_ALARM_IO_INPUT = 0;   //IO输入报警
	public static final int IPCNET_ALARM_LOST_VIDEO = 1;      //视频丢失报警
	public static final int IPCNET_ALARM_MOVE_DETECT = 2;  //移动侦测报警    
	public static final int IPCNET_ALARM_TAKE_PIC = 3;
	public static final int IPCNET_ALARM_TAKE_VIDEO = 4;
	public static final int  MOTION_ALARM=0x01;
	public static final int GPIO_ALARM=0x02;

	//=========AV Recorder Operation=============
	public static final int AV_RECO_OP_PLAY_REQ = 0;
	public static final int AV_RECO_OP_PLAY_RESP = 1;
//			AV_RECO_OP_PAUSE,

	public static final int AV_RECO_OP_LEFT_REQ = 2;
	public static final int AV_RECO_OP_LEFT_RESP = 3;

	public static final int AV_RECO_OP_RIGHT_REQ = 4;
	public static final int AV_RECO_OP_RIGHT_RESP = 5;

	public static final int AV_RECO_OP_STOP_REQ = 6;
	public static final int AV_RECO_OP_STOP_RESP = 7;

	public static final int AV_RECO_OP_SEEK_REQ = 8;
	public static final int AV_RECO_OP_SEEK_RESP = 9;
	//=========File Operation=============
	public static final int AV_RECO_OP_DOWNLOAD_FILE_REQ = 10;
	public static final int AV_RECO_OP_DOWNLOAD_FILE_RESP = 11;
	
	public static final int AV_RECO_OP_FILE_ATTRIBUTE_REQ = 12;
	public static final int AV_RECO_OP_FILE_ATTRIBUTE_RESP = 13;
	
	public static final int AV_RECO_OP_DELETE_FILE_REQ = 14;
	public static final int AV_RECO_OP_DELETE_FILE_RESP = 15;
	
	public static final int AV_RECO_OP_SET_RECORDER_CONFIG_REQ = 16;
	public static final int AV_RECO_OP_SET_RECORDER_CONFIG_RESP = 17;
	
	public static final int AV_RECO_OP_GET_RECORDER_CONFIG_REQ = 18;
	public static final int AV_RECO_OP_GET_RECORDER_CONFIG_RESP = 19;
	
	//stream type
	public static final int  STREAM_TYPE_VIDEO = 0x01;      //开启视频
	public static final int  STREAM_TYPE_AUDIO = 0x02;      //开启音频
	public static final int  STREAM_TYPE_COMP = 0x03;       //开启复合流
	public static final int  DECODE_TYPE_VIDEO = 0x04;       //解码视频
	public static final int  DECODE_TYPE_AUDIO = 0x08;       //解码音频
	public static final int  DECODEM_TYPE_COMP = 0x0c;       //开启复合流
	//end

	public static final int IPCNET_RET_OK = 100;
	public static final int IPCNET_RET_UNKNOWN = IPCNET_RET_OK + 1;
	public static final int IPCNET_RET_VERSION_NOT_SUPPORT= IPCNET_RET_OK + 2;
	public static final int IPCNET_RET_REQ_ILLEGAL= IPCNET_RET_OK + 3;
	public static final int IPCNET_RET_LOGIN_EXSIST= IPCNET_RET_OK + 4;

	public static final int IPCNET_RET_UNLOGIN= IPCNET_RET_OK + 5;
	public static final int IPCNET_RET_PASSWARD_ERR= IPCNET_RET_OK + 6;
	public static final int IPCNET_RET_UNAUTHORISED= IPCNET_RET_OK + 7;
	public static final int IPCNET_RET_TIMEOUT= IPCNET_RET_OK + 8;
	public static final int IPCNET_RET_NOT_FOUND= IPCNET_RET_OK + 9;

	public static final int IPCNET_RET_FIND_AND_SEND_OVER = IPCNET_RET_OK + 10;
	public static final int IPCNET_RET_FIND_AND_SEND_APART = IPCNET_RET_OK + 11;

	public static final int IPCNET_RET_NEED_RESTART_APP = 602;
	public static final int IPCNET_RET_NEED_RESTART_SYS = IPCNET_RET_NEED_RESTART_APP + 1;
	public static final int IPCNET_RET_WRITE_FILE_ERR = IPCNET_RET_NEED_RESTART_APP + 2;

	public static final int IPCNET_RET_EQ_NOT_SUPPORT = IPCNET_RET_NEED_RESTART_APP + 3;	//= 605
	public static final int IPCNET_RET_NEED_VERIFY_ERR = IPCNET_RET_NEED_RESTART_APP + 4;
	public static final int IPCNET_RET_NEED_CONFIG_UNEIST = IPCNET_RET_NEED_RESTART_APP + 5;
	public static final int IPCNET_RET_NEED_PASER_ERR = IPCNET_RET_NEED_RESTART_APP + 6;

	public static final int IPC_NET_RET_NO_MEM = IPCNET_RET_NEED_RESTART_APP + 7;
	
	public static final int IPC_NET_RET_UPDATING = IPCNET_RET_NEED_RESTART_APP + 8;
	
	public static final int IPCNET_RET_REQ_MSG_ID_UNKNOWN = 1001; //请求命令存在但具体的消息不支持
	public static final int IPCNET_RET_REQ_MSG_NAME_UNKNOWN = IPCNET_RET_REQ_MSG_ID_UNKNOWN + 1;
	
	public static final int IPCNET_LOGIN_REQ = 1000;
	public static final int IPCNET_LOGIN_RESP = IPCNET_LOGIN_REQ + 1;

	public static final int IPCNET_KEEPALIVE_REQ = IPCNET_LOGIN_REQ + 2;
	public static final int IPCNET_KEEPALIVE_RESP = IPCNET_LOGIN_REQ + 3;
	
	public static final int IPCNET_STREAM_REQ = IPCNET_LOGIN_REQ + 4;
	public static final int IPCNET_STREAM_RESP = IPCNET_LOGIN_REQ + 5;
	
	public static final int IPCNET_TALK_REQ = IPCNET_LOGIN_REQ + 6;
	public static final int IPCNET_TALK_RESP = IPCNET_LOGIN_REQ + 7;
	
	public static final int IPCNET_SYSINFO_REQ = 1020;
	public static final int IPCNET_SYSINFO_RESP = IPCNET_SYSINFO_REQ + 1;
	
	public static final int IPCNET_USER_SET_REQ = IPCNET_SYSINFO_REQ + 2;
	public static final int IPCNET_USER_SET_RESP = IPCNET_SYSINFO_REQ + 3;
	
	public static final int IPCNET_MCU_INFO_REQ = IPCNET_SYSINFO_REQ + 4;
	public static final int IPCNET_MCU_INFO_RESP = IPCNET_SYSINFO_REQ + 5;
	
	//public static final int JIANLE__NET_CFG_NETWORK_ETH_SET_REQ = 1040;
	public static final int IPC_NETWORK_ETH_SET_REQ=1040;
	public static final int IPC_NETWORK_ETH_SET_RESP = IPC_NETWORK_ETH_SET_REQ + 1;
			
	public static final int IPC_NETWORK_ETH_GET_REQ = IPC_NETWORK_ETH_SET_REQ + 2;
	public static final int IPC_NETWORK_ETH_GET_RESP = IPC_NETWORK_ETH_SET_REQ + 3;
		    
	public static final int IPC_NETWORK_MOBILE_SET_REQ = IPC_NETWORK_ETH_SET_REQ + 4;
	public static final int IPC_NETWORK_MOBILE_SET_RESP = IPC_NETWORK_ETH_SET_REQ + 5;

	public static final int IPC_NETWORK_MOBILE_GET_REQ = IPC_NETWORK_ETH_SET_REQ + 6;
	public static final int IPC_NETWORK_MOBILE_GET_RESP = IPC_NETWORK_ETH_SET_REQ + 7;

	public static final int IPC_NETWORK_WIFI_SET_REQ = IPC_NETWORK_ETH_SET_REQ + 8;
	public static final int IPC_NETWORK_WIFI_SET_RESP = IPC_NETWORK_ETH_SET_REQ + 9;
		    
	public static final int IPC_NETWORK_WIFI_GET_REQ = IPC_NETWORK_ETH_SET_REQ + 10;
	public static final int IPC_NETWORK_WIFI_GET_RESP = IPC_NETWORK_ETH_SET_REQ + 11;
		    
	public static final int IPC_NETWORK_WIFI_SEARCH_GET_REQ = IPC_NETWORK_ETH_SET_REQ + 12;
	public static final int IPC_NETWORK_WIFI_SEARCH_GET_RESP = IPC_NETWORK_ETH_SET_REQ + 13;

	public static final int IPC_VIDEO_ENC_SET_REQ = IPC_NETWORK_ETH_SET_REQ + 14;
	public static final int IPC_VIDEO_ENC_SET_RESP = IPC_NETWORK_ETH_SET_REQ + 15;

	public static final int IPC_VIDEO_ENC_GET_REQ = IPC_NETWORK_ETH_SET_REQ + 16;
	public static final int IPC_VIDEO_ENC_GET_RESP = IPC_NETWORK_ETH_SET_REQ + 17;
		    
	public static final int IPC_AUDIO_ENC_SET_REQ = IPC_NETWORK_ETH_SET_REQ + 18;
	public static final int IPC_AUDIO_ENC_SET_RESP = IPC_NETWORK_ETH_SET_REQ + 19;

	public static final int IPC_AUDIO_ENC_GET_REQ = IPC_NETWORK_ETH_SET_REQ + 20;
	public static final int IPC_AUDIO_ENC_GET_RESP = IPC_NETWORK_ETH_SET_REQ + 21;
		    
	public static final int IPC_PTZ_GET_REQ = IPC_NETWORK_ETH_SET_REQ + 22;
	public static final int IPC_PTZ_GET_RESP = IPC_NETWORK_ETH_SET_REQ + 23;

	public static final int IPC_PTZ_SET_REQ = IPC_NETWORK_ETH_SET_REQ + 24;
	public static final int IPC_PTZ_SET_RESP = IPC_NETWORK_ETH_SET_REQ + 25;
		    
	public static final int IPC_MOVE_ALARM_SET_REQ = IPC_NETWORK_ETH_SET_REQ + 26;
	public static final int IPC_MOVE_ALARM_SET_RESP = IPC_NETWORK_ETH_SET_REQ + 27;

	public static final int IPC_MOVE_ALARM_GET_REQ = IPC_NETWORK_ETH_SET_REQ + 28;
	public static final int IPC_MOVE_ALARM_GET_RESP = IPC_NETWORK_ETH_SET_REQ + 29;
	
	public static final int IPC_AV_RECO_CONF_SET_REQ = IPC_NETWORK_ETH_SET_REQ + 30;
	public static final int IPC_AV_RECO_CONF_SET_RESP = IPC_NETWORK_ETH_SET_REQ + 31;

	public static final int IPC_AV_RECO_CONF_GET_REQ = IPC_NETWORK_ETH_SET_REQ + 32;
	public static final int IPC_AV_RECO_CONF_GET_RESP = IPC_NETWORK_ETH_SET_REQ + 33;
	
	public static final int IPC_AV_RECO_LIST_GET_REQ = IPC_NETWORK_ETH_SET_REQ + 34;
	public static final int IPC_AV_RECO_LIST_GET_RESP = IPC_NETWORK_ETH_SET_REQ + 35;
	
	//public static final int JIANLE_AV_RECO_BETWEEN_TIME_IN_PAGE_GET_REQ = JIANLE_NETWORK_ETH_SET_REQ + 36;
	//public static final int JIANLE_AV_RECO_BETWEEN_TIME_IN_PAGE_GET_RESP = JIANLE_NETWORK_ETH_SET_REQ + 37;
    
	public static final int IPC_AV_RECO_LIST_PAGE_GET_REQ = IPC_NETWORK_ETH_SET_REQ + 36;
	public static final int IPC_AV_RECO_LIST_PAGE_GET_RESP = IPC_NETWORK_ETH_SET_REQ + 37;
	
	public static final int IPC_AV_RECO_OP_REQ = IPC_NETWORK_ETH_SET_REQ + 38;
	public static final int IPC_AV_RECO_OP_RESP = IPC_NETWORK_ETH_SET_REQ + 39;
	
	public static final int IPC_UPGRADE_REQ = IPC_NETWORK_ETH_SET_REQ + 40;
	public static final int IPC_UPGRADE_RESP = IPC_NETWORK_ETH_SET_REQ + 41;
	
	public static final int IPC_GPIO_GET_REQ = IPC_NETWORK_ETH_SET_REQ + 42;
	public static final int IPC_GPIO_GET_RESP = IPC_NETWORK_ETH_SET_REQ + 43;
	
	public static final int IPC_GPIO_SET_REQ = IPC_NETWORK_ETH_SET_REQ + 44;
	public static final int IPC_GPIO_SET_RESP = IPC_NETWORK_ETH_SET_REQ + 45;
	
	public static final int IPCNET_GET_SUBDEV_REQ = IPC_NETWORK_ETH_SET_REQ + 46;
	public static final int IPCNET_GET_SUBDEV_RESP = IPC_NETWORK_ETH_SET_REQ + 47;
	
	public static final int IPCNET_SET_SUBDEV_REQ = IPC_NETWORK_ETH_SET_REQ + 48;
	public static final int IPCNET_SET_SUBDEV_RESP = IPC_NETWORK_ETH_SET_REQ + 49;
	
	public static final int IPCNET_GET_BUS_REQ = IPC_NETWORK_ETH_SET_REQ + 50;//1090
	public static final int IPCNET_GET_BUS_RESP = IPC_NETWORK_ETH_SET_REQ + 51;
	
	public static final int IPCNET_SET_BUS_REQ = IPC_NETWORK_ETH_SET_REQ + 52;
	public static final int IPCNET_SET_BUS_RESP = IPC_NETWORK_ETH_SET_REQ + 53;
	
	public static final int IPCNET_GET_DEV_INFO_REQ = IPC_NETWORK_ETH_SET_REQ + 54;
	public static final int IPCNET_GET_DEV_INFO_RESP = IPC_NETWORK_ETH_SET_REQ + 55;
	
	public static final int IPCNET_SET_DEV_INFO_REQ = IPC_NETWORK_ETH_SET_REQ + 56;
	public static final int IPCNET_SET_DEV_INFO_RESP = IPC_NETWORK_ETH_SET_REQ + 57;
	
	public static final int IPCNET_UPGRADE_CFG_REQ = IPC_NETWORK_ETH_SET_REQ + 58;
	public static final int IPCNET_UPGRADE_CFG_RESP = IPC_NETWORK_ETH_SET_REQ + 59;
	
	public static final int IPCNET_UPGRADE_AUTO_SET_REQ = IPC_NETWORK_ETH_SET_REQ + 60;//1100
	public static final int IPCNET_UPGRADE_AUTO_SET_RESP = IPC_NETWORK_ETH_SET_REQ + 61;
	
	public static final int IPCNET_SET_DEFAULT_REQ = IPC_NETWORK_ETH_SET_REQ + 62;
	public static final int IPCNET_SET_DEFAULT_RESP = IPC_NETWORK_ETH_SET_REQ + 63;
    
	public static final int IPCNET_CFG_PTZ_REQ = 1400;
	public static final int IPCNET_CFG_PTZ_RESP = IPCNET_CFG_PTZ_REQ + 1;
	
	public static final int IPCNET_GET_TIME_REQ = IPCNET_CFG_PTZ_REQ + 2;
	public static final int IPCNET_GET_TIME_RESP = IPCNET_CFG_PTZ_REQ + 3;
	
	public static final int IPCNET_SET_TIME_REQ = IPCNET_CFG_PTZ_REQ + 4;
	public static final int IPCNET_SET_TIME_RESP = IPCNET_CFG_PTZ_REQ + 5;
	
	public static final int IPCNET_PRESET_SET_PTZ_REQ = IPCNET_CFG_PTZ_REQ + 6;
	public static final int IPCNET_PRESET_SET_PTZ_RESP = IPCNET_CFG_PTZ_REQ + 7;

	public static final int IPCNET_PRESET_GET_PTZ_REQ = IPCNET_CFG_PTZ_REQ + 8;
	public static final int IPCNET_PRESET_GET_PTZ_RESP = IPCNET_CFG_PTZ_REQ + 9;

	//---控制PC灯光(普通照明) 
	public static final int IPCNET_GET_LAMP_REQ = IPCNET_CFG_PTZ_REQ + 10;
	public static final int IPCNET_GET_LAMP_RESP = IPCNET_CFG_PTZ_REQ + 11;
		
	public static final int IPCNET_SET_LAMP_REQ = IPCNET_CFG_PTZ_REQ + 12;
	public static final int IPCNET_SET_LAMP_RESP = IPCNET_CFG_PTZ_REQ + 13;

		//----图像翻转
	public static final int IPCNET_GET_OVERTURN_REQ = IPCNET_CFG_PTZ_REQ + 14;
	public static final int IPCNET_GET_OVERTURN_RESP = IPCNET_CFG_PTZ_REQ + 15;
		
	public static final int IPCNET_SET_OVERTURN_REQ = IPCNET_CFG_PTZ_REQ + 16;
	public static final int IPCNET_SET_OVERTURN_RESQ = IPCNET_CFG_PTZ_REQ + 17;
		
		/**设置曝光类型**/
	public static final int IPCNET_GET_EXPOSURE_TYPE_REQ = IPCNET_CFG_PTZ_REQ + 18;
	public static final int IPCNET_GET_EXPOSURE_TYPE_RESP = IPCNET_CFG_PTZ_REQ + 19;
		
	public static final int IPCNET_SET_EXPOSURE_TYPE_REQ = IPCNET_CFG_PTZ_REQ + 20;
	public static final int IPCNET_SET_EXPOSURE_TYPE_RESP = IPCNET_CFG_PTZ_REQ + 21;

		/**自动曝光**/
	public static final int IPCNET_GET_AUTO_EXPOSURE_REQ = IPCNET_CFG_PTZ_REQ + 22;
	public static final int IPCNET_GET_AUTO_EXPOSURE_RESP = IPCNET_CFG_PTZ_REQ + 23;

	public static final int IPCNET_SET_AUTO_EXPOSURE_REQ = IPCNET_CFG_PTZ_REQ + 24;
	public static final int IPCNET_SET_AUTO_EXPOSURE_RESP = IPCNET_CFG_PTZ_REQ + 25;
	
	/**手动曝光**/
	public static final int IPCNET_GET_MANUAL_EXPOSURE_REQ = IPCNET_CFG_PTZ_REQ + 26;
	public static final int IPCNET_GET_MANUAL_EXPOSURE_RESP = IPCNET_CFG_PTZ_REQ + 27;

	public static final int IPCNET_SET_MANUAL_EXPOSURE_REQ = IPCNET_CFG_PTZ_REQ + 28;
	public static final int IPCNET_SET_MANUAL_EXPOSURE_RESP = IPCNET_CFG_PTZ_REQ + 29;

	//----彩转黑方式
	public static final int IPCNET_GET_PICOLOR_REQ = IPCNET_CFG_PTZ_REQ + 30;	 /*获取彩转黑*/
	public static final int IPCNET_GET_PICOLOR_RESP = IPCNET_CFG_PTZ_REQ + 31;
	
	public static final int IPCNET_SET_PICOLOR_REQ = IPCNET_CFG_PTZ_REQ + 32; /*设置彩转黑*/
	public static final int IPCNET_SET_PICOLOR_RESP = IPCNET_CFG_PTZ_REQ + 33; 
	
	//----环境设置
	public static final int IPCNET_GET_ENVIRONMENT_REQ = IPCNET_CFG_PTZ_REQ + 34;		/*获取环境*///1--outdoor模式 0--indoor模式
	public static final int IPCNET_GET_ENVIRONMENT_RESP = IPCNET_CFG_PTZ_REQ + 35;
	
	public static final int IPCNET_SET_ENVIRONMENT_REQ = IPCNET_CFG_PTZ_REQ + 36;		/*设置环境*///1--outdoor模式 0--indoor模式
	public static final int IPCNET_SET_ENVIRONMENT_RESP = IPCNET_CFG_PTZ_REQ + 37;

	//---2D/3D降噪
	public static final int IPCNET_GET_DENOISE_REQ = IPCNET_CFG_PTZ_REQ + 38;		/*获取2D/3D 降噪*/	
	public static final int IPCNET_GET_DENOISE_RESP = IPCNET_CFG_PTZ_REQ + 39;
	
	public static final int IPCNET_SET_DENOISE_REQ = IPCNET_CFG_PTZ_REQ + 40;	/*设置2D/3D 降噪*/
	public static final int IPCNET_SET_DENOISE_RESP = IPCNET_CFG_PTZ_REQ + 41;

	//v1---宽动态设置
	public static final int IPCNET_GET_WDR_REQ = IPCNET_CFG_PTZ_REQ + 42;		/*获取WDR*/
	public static final int IPCNET_GET_WDR_RESP = IPCNET_CFG_PTZ_REQ + 43;
	
	public static final int IPCNET_SET_WDR_REQ = IPCNET_CFG_PTZ_REQ + 44;		/*设置WDR*/
	public static final int IPCNET_SET_WDR_RESP = IPCNET_CFG_PTZ_REQ + 45;		

	//--白平衡
	public static final int IPCNET_GET_WH_BLANCE_REQ = IPCNET_CFG_PTZ_REQ + 46;
	public static final int IPCNET_GET_WH_BLANCE_RESP = IPCNET_CFG_PTZ_REQ + 47;
	
	public static final int IPCNET_SET_WH_BLANCE_REQ = IPCNET_CFG_PTZ_REQ + 48;
	public static final int IPCNET_SET_WH_BLANCE_RESP = IPCNET_CFG_PTZ_REQ + 49;

	//--降帧
	public static final int IPCNET_GET_SLOW_FRAME_RATE_REQ = IPCNET_CFG_PTZ_REQ + 50;
	public static final int IPCNET_GET_SLOW_FRAME_RATE_RESP = IPCNET_CFG_PTZ_REQ + 51;
	
	public static final int IPCNET_SET_SLOW_FRAME_RATE_REQ = IPCNET_CFG_PTZ_REQ + 52;
	public static final int IPCNET_SET_SLOW_FRAME_RATE_RESP = IPCNET_CFG_PTZ_REQ + 53;
	
	//--设置默认值
	public static final int IPCNET_GET_EXPOSURE_DEFAULT_REQ = IPCNET_CFG_PTZ_REQ + 54;		//未用到
	public static final int IPCNET_GET_EXPOSURE_DEFAULT_RESP = IPCNET_CFG_PTZ_REQ + 55;
	
	public static final int IPCNET_SET_EXPOSURE_DEFAULT_REQ = IPCNET_CFG_PTZ_REQ + 56;
	public static final int IPCNET_SET_EXPOSURE_DEFAULT_RESP = IPCNET_CFG_PTZ_REQ + 57;

	//--坏点校正
	public static final int IPCNET_GET_BADPIXEL_DETECT_REQ = IPCNET_CFG_PTZ_REQ + 58; 	//未用到
	public static final int IPCNET_GET_BADPIXEL_DETECT_RESP = IPCNET_CFG_PTZ_REQ + 59;
	
	public static final int IPCNET_SET_BADPIXEL_DETECT_REQ = IPCNET_CFG_PTZ_REQ + 60;
	public static final int IPCNET_SET_BADPIXEL_DETECT_RESP = IPCNET_CFG_PTZ_REQ + 61;
	
	public static final int IPCNET_GET_ANTIFLICKER_REQ = IPCNET_CFG_PTZ_REQ + 62;	 	/*获取抗闪炼*/	
	public static final int IPCNET_GET_ANTIFLICKER_RESP = IPCNET_CFG_PTZ_REQ + 63;
	
	public static final int IPCNET_SET_ANTIFLICKER_REQ = IPCNET_CFG_PTZ_REQ + 64;		 /*设置抗闪炼*/
	public static final int IPCNET_SET_ANTIFLICKER_RESP = IPCNET_CFG_PTZ_REQ + 65;
	
	//v2---宽动态设置
	public static final int IPCNET_GET_WDR_V2_REQ = IPCNET_CFG_PTZ_REQ + 66;		/*获取WDR*/
	public static final int IPCNET_GET_WDR_V2_RESP = IPCNET_CFG_PTZ_REQ + 67;
	
	public static final int IPCNET_SET_WDR_V2_REQ = IPCNET_CFG_PTZ_REQ + 68;		/*设置WDR*/
	public static final int IPCNET_SET_WDR_V2_RESP = IPCNET_CFG_PTZ_REQ + 69;

	//----自动彩转黑手动设置光量值
	public static final int IPCNET_GET_PICOLOR4MANUlUAM_REQ = IPCNET_CFG_PTZ_REQ + 70;
	public static final int IPCNET_GET_PICOLOR4MANUlUAM_RESP = IPCNET_CFG_PTZ_REQ + 71;
	
	public static final int IPCNET_SET_PICOLOR4MANUlUAM_REQ = IPCNET_CFG_PTZ_REQ + 72;
	public static final int IPCNET_SET_PICOLOR4MANUlUAM_RESP = IPCNET_CFG_PTZ_REQ + 73;

	public static final int IPCNET_SNAP_SHOOT_REQ = IPCNET_CFG_PTZ_REQ + 74;
	public static final int IPCNET_SNAP_SHOOT_RESP = IPCNET_CFG_PTZ_REQ + 75;
	
	//预置点
	public static final int IPCNET_GET_PREPOINT_REQ = IPCNET_CFG_PTZ_REQ + 76;        //1476
	public static final int IPCNET_GET_PREPOINT_RESP = IPCNET_CFG_PTZ_REQ + 77;       //1477    

	public static final int IPCNET_SET_PREPOINT_REQ = IPCNET_CFG_PTZ_REQ + 78;        //1478
	public static final int IPCNET_SET_PREPOINT_RESP = IPCNET_CFG_PTZ_REQ + 79;       //1479
	    
	public static final int IPCNET_OPERATE_PREPOINT_REQ = IPCNET_CFG_PTZ_REQ + 80;        //1450
	public static final int IPCNET_OPERATE_PREPOINT_RESP = IPCNET_CFG_PTZ_REQ + 81;       //1451

	    //ftp参数设置
	public static final int IPCNET_SET_FTP_CFG_REQ = IPCNET_CFG_PTZ_REQ + 82;
	public static final int IPCNET_SET_FTP_CFG_RESP = IPCNET_CFG_PTZ_REQ + 83;

	public static final int IPCNET_GET_FTP_CFG_REQ = IPCNET_CFG_PTZ_REQ + 84;
	public static final int IPCNET_GET_FTP_CFG_RESP = IPCNET_CFG_PTZ_REQ + 85;

	    /***Email cfg***/
	public static final int IPCNET_SET_EMAIL_CFG_REQ = IPCNET_CFG_PTZ_REQ + 86;
	public static final int IPCNET_SET_EMAIL_CFG_RESP = IPCNET_CFG_PTZ_REQ + 87;

	public static final int IPCNET_GET_EMAIL_CFG_REQ = IPCNET_CFG_PTZ_REQ + 88;
	public static final int IPCNET_GET_EMAIL_CFG_RESP = IPCNET_CFG_PTZ_REQ + 89;
		
		/***Ddns cfg***/
	public static final int IPCNET_SET_DDNS_CFG_REQ = IPCNET_CFG_PTZ_REQ + 90;
	public static final int IPCNET_SET_DDNS_CFG_RESP = IPCNET_CFG_PTZ_REQ + 91;

	public static final int IPCNET_GET_DDNS_CFG_REQ = IPCNET_CFG_PTZ_REQ + 92;
	public static final int IPCNET_GET_DDNS_CFG_RESP = IPCNET_CFG_PTZ_REQ + 93;

	    /***设置亮度，对比度，饱和度***/
	public static final int IPCNET_SET_CAM_PIC_CFG_REQ = IPCNET_CFG_PTZ_REQ + 94;
	public static final int IPCNET_SET_CAM_PIC_CFG_RESP = IPCNET_CFG_PTZ_REQ + 95;

	public static final int IPCNET_GET_CAM_PIC_CFG_REQ = IPCNET_CFG_PTZ_REQ + 96;
	public static final int IPCNET_GET_CAM_PIC_CFG_RESP = IPCNET_CFG_PTZ_REQ + 97;
	
	/****IRCUT切换****/
	public static final int IPCNET_SET_IRCUT_REQ = IPCNET_CFG_PTZ_REQ + 98;
	public static final int IPCNET_SET_IRCUT_RESP = IPCNET_CFG_PTZ_REQ + 99;

	public static final int IPCNET_GET_IRCUT_REQ = IPCNET_CFG_PTZ_REQ + 100;
	public static final int IPCNET_GET_IRCUT_RESP = IPCNET_CFG_PTZ_REQ + 101;
	
	public static final int IPCNET_ALARM_REPORT_RESP = IPCNET_CFG_PTZ_REQ + 102;       // 1502主动上传报警信息

    /****OSD cfg****/
	public static final int IPCNET_SET_OSD_REQ = IPCNET_CFG_PTZ_REQ + 103;       //1503
	public static final int IPCNET_SET_OSD_RESP = IPCNET_CFG_PTZ_REQ + 104;      //1504
    
	public static final int IPCNET_GET_OSD_REQ = IPCNET_CFG_PTZ_REQ + 105;       //1505
	public static final int IPCNET_GET_OSD_RESP = IPCNET_CFG_PTZ_REQ + 106;      //1506
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//设备端和PC端通讯协议<H>
	public static final int REMOTE_BASE_MSG_START = 3000;       //消息的种类的起点
	public static final int REMOTE_BASE_SPE_MSG_START = 3600;       //

	public static final int REMOTE_BASE_RESP_MSG_START = 4000;       //回应消息的种类的起点
	public static final int REMOTE_BASE_SPE_RESP_MSG_START = 4600; 

	public static final int REMOTE_BASE_MSG_RESULT = 5000;       //消息返回的结果的起点
	public static final int REMOTE_MSG_RESP_OK = 0; //成功为0
////--------------------------------------------------------------------------------------
	public static final int REMOTE_MSG_INTERVAL = REMOTE_BASE_MSG_START;//心跳消息
	public static final int REMOTE_MSG_LOGIN = REMOTE_BASE_MSG_START + 1;// 客户端登陆
	public static final int REMOTE_MSG_LOGOUT = REMOTE_BASE_MSG_START +2;//  客户端注销
	public static final int REMOTE_MSG_ADD_USER = REMOTE_BASE_MSG_START + 3;// 添加用户
	public static final int REMOTE_MSG_MODIFY_USER = REMOTE_BASE_MSG_START + 4;//修改用户属性和权限

	public static final int REMOTE_MSG_DEL_USER = REMOTE_BASE_MSG_START + 5;//删除用户
	public static final int REMOTE_MSG_REQ_STREAM = REMOTE_BASE_MSG_START + 6;//图像预览请求
	public static final int REMOTE_MSG_GET_FIX_CONF = REMOTE_BASE_MSG_START + 7;//获取服务器基本固有配置，所有这些配置在出厂时固化，用户不能随意修改。
	public static final int  REMOTE_MSG_VERSION_INFO = REMOTE_BASE_MSG_START + 8;//获取服务器版本信息。（版本信息不能修改，升级后，会自动修改版本信息）。
	public static final int REMOTE_MSG_GET_SERVER = REMOTE_BASE_MSG_START + 9;//获取服务器网络配置信息    

	public static final int REMOTE_MSG_SET_SERVER = REMOTE_BASE_MSG_START + 10;//设置服务器网络配置信息 //10
	public static final int REMOTE_MSG_GET_NETWORK = REMOTE_BASE_MSG_START + 11;//获取网络配置信息    
	public static final int REMOTE_MSG_SET_NETWORK = REMOTE_BASE_MSG_START + 12;//设置网络配置信息
	public static final int REMOTE_MSG_GET_PPPOE  = REMOTE_BASE_MSG_START + 13;//获取PPPOE配置信息    
	public static final int REMOTE_MSG_SET_PPPOE = REMOTE_BASE_MSG_START + 14;//设置PPPOE配置信息
	    
	public static final int REMOTE_MSG_GET_WIRELESS = REMOTE_BASE_MSG_START + 15;//获取WIRELESS配置信息    
	public static final int REMOTE_MSG_SET_WIRELESS = REMOTE_BASE_MSG_START + 16;//设置WIRELESS配置信息
	public static final int REMOTE_MSG_GET_NTP  = REMOTE_BASE_MSG_START + 17;//获取NTP配置信息    
	public static final int REMOTE_MSG_SET_NTP = REMOTE_BASE_MSG_START + 18;//设置NTP配置信息
	public static final int REMOTE_MSG_GET_DDNS = REMOTE_BASE_MSG_START + 19;//获取NTP配置信息   
	    
	public static final int REMOTE_MSG_SET_DDNS = REMOTE_BASE_MSG_START + 20;//设置NTP配置信息  //20
	public static final int REMOTE_MSG_GET_UPNP = REMOTE_BASE_MSG_START + 21;//获取UPNP配置信息    
	public static final int REMOTE_MSG_SET_UPNP = REMOTE_BASE_MSG_START + 22;//设置UPNP配置信息
	public static final int REMOTE_MSG_GET_FTP = REMOTE_BASE_MSG_START + 23;//获取FTP配置信息    
	public static final int REMOTE_MSG_SET_FTP = REMOTE_BASE_MSG_START + 24;//设置FTP配置信息
	    
	public static final int REMOTE_MSG_GET_MAIL = REMOTE_BASE_MSG_START + 25;//获取MAIL配置信息    
	public static final int REMOTE_MSG_SET_MAIL = REMOTE_BASE_MSG_START + 26;//设置MAIL配置信息
	public static final int REMOTE_MSG_GET_AUDIO = REMOTE_BASE_MSG_START + 27;//获取音频配置信息  
	public static final int REMOTE_MSG_SET_AUDIO = REMOTE_BASE_MSG_START + 28;//设置音频配置信息
	public static final int REMOTE_MSG_GET_VIDEO_CH = REMOTE_BASE_MSG_START + 29;//获取视频通道配置信息    
	    
	public static final int REMOTE_MSG_SET_VIDEO_CH = REMOTE_BASE_MSG_START + 30;//设置视频通道配置信息 //30
	public static final int REMOTE_MSG_GET_IO_ALARM = REMOTE_BASE_MSG_START + 31;//获取IO告警配置信息   
	public static final int REMOTE_MSG_SET_IO_ALARM = REMOTE_BASE_MSG_START + 32;//设置IO告警配置信息
	public static final int REMOTE_MSG_GET_MOTION_ALARM = REMOTE_BASE_MSG_START + 33;//获取移动侦测配置信息    
	public static final int REMOTE_MSG_SET_MOTION_ALARM = REMOTE_BASE_MSG_START + 34;//设置移动侦测配置信息
	    
	public static final int REMOTE_MSG_GET_LOST_ALARM = REMOTE_BASE_MSG_START + 35;//获取视频丢失告警配置信息    
	public static final int REMOTE_MSG_SET_LOST_ALARM = REMOTE_BASE_MSG_START + 36;//设置视频丢失告警配置信息
	public static final int REMOTE_MSG_GET_MASK_ALARM = REMOTE_BASE_MSG_START + 37;//获取视频遮挡告警配置信息    
	public static final int REMOTE_MSG_SET_MASK_ALARM = REMOTE_BASE_MSG_START + 38;//设置视频遮挡告警配置信息
	public static final int REMOTE_MSG_GET_SERAIL = REMOTE_BASE_MSG_START + 39;//获取串口的配置信息  
	    
	public static final int REMOTE_MSG_SET_SERAIL = REMOTE_BASE_MSG_START + 40;//设置串口的配置信息       //40
	public static final int REMOTE_MSG_GET_LOG = REMOTE_BASE_MSG_START + 41;//获取日志的配置信息  
	public static final int REMOTE_MSG_SET_LOG = REMOTE_BASE_MSG_START + 42;//设置日志的配置信息 
	public static final int REMOTE_MSG_GET_PTZ = REMOTE_BASE_MSG_START + 43;//获取云台的配置信息 
	public static final int REMOTE_MSG_SET_PTZ = REMOTE_BASE_MSG_START + 44;//设置云台的配置信息 
	    
	public static final int REMOTE_MSG_FRAME_CMD = REMOTE_BASE_MSG_START + 45;//请求帧的命令
	public static final int REMOTE_MSG_GET_EX_NETWORK = REMOTE_BASE_MSG_START + 46; //请求扩展网络
	public static final int REMOTE_MSG_SET_EX_NETWORK = REMOTE_BASE_MSG_START + 47; //请求扩展网络
	public static final int REMOTE_MSG_GET_USER_INFO = REMOTE_BASE_MSG_START + 48;// 回应取用户的信息
	public static final int REMOTE_MSG_GET_VIDEO_ATTR = REMOTE_BASE_MSG_START + 49;// 请求取用户的视频属性信息

	public static final int REMOTE_MSG_SET_VIDEO_ATTR = REMOTE_BASE_MSG_START + 50;// 请求设置用户的视频属性信息 //50
	public static final int REMOTE_MSG_SET_FIX_CONF = REMOTE_BASE_MSG_START + 51;//请求设置用户的固有属性
	public static final int REMOTE_MSG_BRAODCAST = REMOTE_BASE_MSG_START + 52;//广播或多播
	public static final int REMOTE_MSG_PTZ_CONTROL_CMD  = REMOTE_BASE_MSG_START + 53;
	public static final int REMOTE_MSG_VIEW_LOG = REMOTE_BASE_MSG_START + 54;//查看管理日志
	    
	public static final int REMOTE_MSG_VIEW_ALARM_LOG = REMOTE_BASE_MSG_START + 55;//查看告警日志
	public static final int REMOTE_MSG_REQ_DECODE_AUDIO = REMOTE_BASE_MSG_START + 56;//请求解码
	public static final int REMOTE_MSG_REQ_COVER_PIC = REMOTE_BASE_MSG_START + 57;//请求视频遮挡图片
	public static final int REMOTE_MSG_DISK_INFO_REQ = REMOTE_BASE_MSG_START + 58;//获取硬盘信息
	public static final int REMOTE_MSG_DISK_PART_REQ = REMOTE_BASE_MSG_START + 59;//请求硬盘分区
	    
	public static final int REMOTE_MSG_DISK_FORMAT_REQ = REMOTE_BASE_MSG_START + 60;//请求硬盘格式化 //60
	public static final int REMOTE_MSG_SET_GPIO_DIR_REQ = REMOTE_BASE_MSG_START + 61;//请求设置GIOP的方向
	public static final int REMOTE_MSG_GET_GPIO_DIR_REQ = REMOTE_BASE_MSG_START + 62;
	public static final int REMOTE_MSG_SET_GPIO_VAL_REQ = REMOTE_BASE_MSG_START + 63;//请求设置GIOP的值
	public static final int REMOTE_MSG_GET_GPIO_VAL_REQ = REMOTE_BASE_MSG_START + 64;

	public static final int REMOTE_MSG_SET_RTC_LOCAL_TIME  = REMOTE_BASE_MSG_START + 65; //65
	public static final int REMOTE_MSG_GET_RTC_LOCAL_TIME  = REMOTE_BASE_MSG_START + 66;
	public static final int REMOTE_MSG_SET_CAMERA_LENS  = REMOTE_BASE_MSG_START + 67;
	public static final int REMOTE_MSG_GET_CAMERA_LENS  = REMOTE_BASE_MSG_START + 68;
	public static final int REMOTE_MSG_LOCAL_PLAY_REQ  = REMOTE_BASE_MSG_START + 69;//播放硬盘存储媒体流请求

	public static final int REMOTE_MSG_UPLOAD_PLAY_REQ  = REMOTE_BASE_MSG_START + 70;// 70 //PC发送媒体流给解码器播放请求 70
	public static final int REMOTE_MSG_LINK_DEC_REQ  = REMOTE_BASE_MSG_START + 71; //自动连接编码器（IPCAM），播放媒体流请求
	public static final int REMOTE_MSG_END_PLAY_REQ  = REMOTE_BASE_MSG_START + 72; //结束播放请求
	public static final int REMOTE_MSG_SOURCH_FILE  = REMOTE_BASE_MSG_START + 73; //  查找文件请求 
	public static final int REMOTE_MSG_UPLOAD_FILE  = REMOTE_BASE_MSG_START + 74;

	public static final int REMOTE_MSG_DELETE_FILE  = REMOTE_BASE_MSG_START + 75;// 75 删除文件
	public static final int REMOTE_MSG_START_MUTIL_PROCESS  = REMOTE_BASE_MSG_START + 76;
	public static final int REMOTE_MSG_STOP_MUTIL_PROCESS  = REMOTE_BASE_MSG_START + 77;
	public static final int REMOTE_MSG_UPDATE_REQ  = REMOTE_BASE_MSG_START + 78;//
	public static final int REMOTE_MSG_DOWN_UPDATE_DATE  = REMOTE_BASE_MSG_START + 79;

	public static final int REMOTE_MSG_REBOOT_REQ  = REMOTE_BASE_MSG_START + 80;// 80 
	public static final int REMOTE_MSG_DOWNLOAN_FILE  = REMOTE_BASE_MSG_START + 81;
	public static final int REMOTE_MSG_REAL_PLAY_FILE  = REMOTE_BASE_MSG_START + 82;
	public static final int REMOTE_MSG_REAL_PLAY_PAUSE  = REMOTE_BASE_MSG_START + 83;
	public static final int REMOTE_MSG_REAL_PLAY_END  = REMOTE_BASE_MSG_START + 84;

	public static final int REMOTE_MSG_SET_RECORD_TIME_INFO  = REMOTE_BASE_MSG_START + 85; //85
	public static final int REMOTE_MSG_GET_RECORD_TIME_INFO  = REMOTE_BASE_MSG_START + 86;
	public static final int REMOTE_MSG_LOCAL_PLAY_OR_PAUSE_REQ  = REMOTE_BASE_MSG_START + 87;
	public static final int REMOTE_MSG_LOCAL_PLAY_END_REQ  = REMOTE_BASE_MSG_START + 88;
	public static final int REMOTE_MSG_START_REC  = REMOTE_BASE_MSG_START + 89;
	    
	public static final int REMOTE_MSG_START_PRE_REC  = REMOTE_BASE_MSG_START + 90;//90
	public static final int REMOTE_MSG_GET_FREE_CAP_INFO  = REMOTE_BASE_MSG_START + 91;
	public static final int REMOTE_MSG_GET_TOUR_INFO   = REMOTE_BASE_MSG_START + 92;
	public static final int REMOTE_MSG_SET_TOUR_INFO   = REMOTE_BASE_MSG_START + 93;
	public static final int REMOTE_MSG_GET_NET_PTZ_INFO   = REMOTE_BASE_MSG_START + 94;

	public static final int REMOTE_MSG_MUTILCAST  = REMOTE_BASE_MSG_START + 95;//多播	//95
	public static final int REMOTE_MSG_REQ_I_FRAME  = REMOTE_BASE_MSG_START + 96;
	public static final int REMOTE_MSG_SYS_RESET  = REMOTE_BASE_MSG_START + 97;
	public static final int REMOTE_MSG_GET_ROI  = REMOTE_BASE_MSG_START + 98;
	public static final int REMOTE_MSG_SET_ROI  = REMOTE_BASE_MSG_START + 99;
	    
	public static final int REMOTE_MSG_GET_CORRIDOR  = REMOTE_BASE_MSG_START + 100;	//100  走廊模式
	public static final int REMOTE_MSG_SET_CORRIDOR  = REMOTE_BASE_MSG_START + 101;	//
	public static final int REMOTE_MSG_GET_LDC  = REMOTE_BASE_MSG_START + 102;				// 畸形矫正
	public static final int REMOTE_MSG_SET_LDC  = REMOTE_BASE_MSG_START + 103;
	public static final int REMOTE_MSG_GET_ANTIFOG  = REMOTE_BASE_MSG_START + 104;
	public static final int REMOTE_MSG_SET_ANTIFOG  = REMOTE_BASE_MSG_START + 105;

	public static final int REMOTE_MSG_GET_AUTO_UPGRADE  = REMOTE_BASE_MSG_START + 106;		/*106 自动升级*/
	public static final int REMOTE_MSG_SET_AUTO_UPGRADE  = REMOTE_BASE_MSG_START + 107;
	public static final int REMOTE_MSG_GET_CLOUD_STORAGE  = REMOTE_BASE_MSG_START + 108;		/*云存储*/
	public static final int REMOTE_MSG_SET_CLOUD_STORAGE  = REMOTE_BASE_MSG_START + 109;

	public static final int REMOTE_MSG_PTZ_3DCONTROL_CMD_BASE = 3200;
		//扩展命令从3200开始
	public static final int REMOTE_MSG_PTZ_3DCONTROL_CMD = REMOTE_MSG_PTZ_3DCONTROL_CMD_BASE;	//用于球机3D
	public static final int REMOTE_MSG_PTZ_SET_ABS_CMD = REMOTE_MSG_PTZ_3DCONTROL_CMD_BASE + 1;    //设置PTZ绝对坐标    
	public static final int REMOTE_MSG_PTZ_GET_ABS_CMD = REMOTE_MSG_PTZ_3DCONTROL_CMD_BASE + 2;	//读取PTZ绝对坐标
	public static final int REMOTE_MSG_CAM_SENSOR_CMD = REMOTE_MSG_PTZ_3DCONTROL_CMD_BASE + 3;		//3516环境、WDR、电子快门等设置
	public static final int REMOTE_MSG_WIFI_SEARCH = REMOTE_MSG_PTZ_3DCONTROL_CMD_BASE + 4;		//搜索WIFI
	    
	public static final int REMOTE_MSG_GET_NETWORK_V2 = REMOTE_MSG_PTZ_3DCONTROL_CMD_BASE + 5;  //05 读取备用DNS地址的信息  
	public static final int REMOTE_MSG_SET_NETWORK_V2 = REMOTE_MSG_PTZ_3DCONTROL_CMD_BASE + 6;  //06 设置备用DNS地址的信息
	public static final int REMOTE_MSG_BRAODCAST_CHANGEIP = REMOTE_MSG_PTZ_3DCONTROL_CMD_BASE + 7;		//以广播的方式修改IP
	public static final int REMOTE_MSG_GET_VIDEO_INFO_INDEX = REMOTE_MSG_PTZ_3DCONTROL_CMD_BASE + 8;//获取当前设备支持的分辨率索引
	public static final int REMOTE_MSG_GET_3G_INFO = REMOTE_MSG_PTZ_3DCONTROL_CMD_BASE + 9;//获取3G信息
	    
	public static final int REMOTE_MSG_SET_3G_INFO = REMOTE_MSG_PTZ_3DCONTROL_CMD_BASE + 10;	//10//设置3G信息
	public static final int REMOTE_MSG_GET_SNAP_INFO = REMOTE_MSG_PTZ_3DCONTROL_CMD_BASE + 11;	//获取抓拍信息
	public static final int REMOTE_MSG_SET_SNAP_INFO = REMOTE_MSG_PTZ_3DCONTROL_CMD_BASE + 12;		//设置抓拍信息
	public static final int REMOTE_MSG_SET_ENV4UPDATE = REMOTE_MSG_PTZ_3DCONTROL_CMD_BASE + 13;	//为了升级下一版本进行环境设置
	    
	public static final int REMOTE_MSG_GET_REBOOT_POLICY = REMOTE_MSG_PTZ_3DCONTROL_CMD_BASE + 14; /* 14, 获取定时重启的策略*/
	public static final int REMOTE_MSG_SET_REBOOT_POLICY  = REMOTE_MSG_PTZ_3DCONTROL_CMD_BASE + 15; /* 15, 设置定时重启的策略*/

	public static final int REMOTE_MSG_GET_P2P_INFO = REMOTE_MSG_PTZ_3DCONTROL_CMD_BASE + 16;		/* 16, 获取设备P2P的配置信息*/
	public static final int REMOTE_MSG_SET_P2P_INFO = REMOTE_MSG_PTZ_3DCONTROL_CMD_BASE + 17;		/* 17, 设置设备P2P的信息*/

	public static final int REMOTE_MSG_SET_LIGHTING = REMOTE_MSG_PTZ_3DCONTROL_CMD_BASE + 18;		/* 18 卡片机命令 : 设置照明灯*/
	public static final int REMOTE_MSG_CLICK_BUTTON_WPS = REMOTE_MSG_PTZ_3DCONTROL_CMD_BASE + 19;	/* 19 卡片机命令 : 模仿WPS按钮功能*/
		
	public static final int REMOTE_MSG_MOD_SYS_INFO = REMOTE_BASE_SPE_MSG_START;
	public static final int REMOTE_MSG_MOD_TOS_MODE_INFO = REMOTE_BASE_SPE_MSG_START + 1;
	public static final int REMOTE_MSG_ADD_LOCAL_FILE = REMOTE_BASE_SPE_MSG_START + 2;
	public static final int REMOTE_MSG_UNKNOWN_MSG_TYPE = REMOTE_BASE_SPE_MSG_START + 3;
	    
	//这部分用作消息回应的类型
	    public static final int REMOTE_MSG_RESP_INTERVAL = REMOTE_BASE_RESP_MSG_START;
	    public static final int REMOTE_MSG_RESP_FRAME_CMD = REMOTE_BASE_RESP_MSG_START + 1;//回应请求帧的命令
	    public static final int REMOTE_MSG_RESP_ERROR  = REMOTE_BASE_RESP_MSG_START + 2;//远程消息的命令错误返回
	    public static final int REMOTE_MSG_RESP_LOGIN  = REMOTE_BASE_RESP_MSG_START + 3; // 回应客户端登陆
	    public static final int REMOTE_MSG_RESP_LOGOUT = REMOTE_BASE_RESP_MSG_START + 4;//  回应客户端注销

	    public static final int REMOTE_MSG_RESP_ADD_USER = REMOTE_BASE_RESP_MSG_START + 5;// 回应添加用户
	    public static final int REMOTE_MSG_RESP_MODIFY_USER = REMOTE_BASE_RESP_MSG_START + 6;//回应修改用户属性和权限
	    public static final int REMOTE_MSG_RESP_DEL_USER  = REMOTE_BASE_RESP_MSG_START + 7;//回应删除用户
	    public static final int REMOTE_MSG_RESP_REQ_STREAM  = REMOTE_BASE_RESP_MSG_START + 8; //回应图像预览请求
	    public static final int REMOTE_MSG_RESP_GET_FIX_CONF = REMOTE_BASE_RESP_MSG_START + 9;//回应获取服务器基本固有配置，所有这些配置在出厂时固化，用户不能随意修改。

	    public static final int REMOTE_MSG_RESP_VERSION_INFO = REMOTE_BASE_RESP_MSG_START + 10;//  10 回应获取服务器版本信息。（版本信息不能修改，升级后，会自动修改版本信息）。
	    public static final int REMOTE_MSG_RESP_GET_SERVER = REMOTE_BASE_RESP_MSG_START + 11;//回应获取服务器网络配置信息   
	    public static final int REMOTE_MSG_RESP_SET_SERVER = REMOTE_BASE_RESP_MSG_START + 12;//回应设置服务器网络配置信息
	    public static final int REMOTE_MSG_RESP_GET_NETWORK  = REMOTE_BASE_RESP_MSG_START + 13;//回应获取网络配置信息    
	    public static final int REMOTE_MSG_RESP_SET_NETWORK = REMOTE_BASE_RESP_MSG_START + 14;//回应设置网络配置信息

	    public static final int REMOTE_MSG_RESP_GET_PPPOE  = REMOTE_BASE_RESP_MSG_START + 15;//回应获取PPPOE配置信息    
	    public static final int REMOTE_MSG_RESP_SET_PPPOE = REMOTE_BASE_RESP_MSG_START + 16;//回应设置PPPOE配置信息
	    public static final int REMOTE_MSG_RESP_GET_WIRELESS = REMOTE_BASE_RESP_MSG_START + 17;//回应获取WIRELESS配置信息    
	    public static final int REMOTE_MSG_RESP_SET_WIRELESS = REMOTE_BASE_RESP_MSG_START + 18;//回应设置WIRELESS配置信息
	    public static final int REMOTE_MSG_RESP_GET_NTP = REMOTE_BASE_RESP_MSG_START + 19;//回应获取NTP配置信息    
	    
	    public static final int REMOTE_MSG_RESP_SET_NTP = REMOTE_BASE_RESP_MSG_START + 20;//回应设置NTP配置信息 20
	    public static final int REMOTE_MSG_RESP_GET_DDNS = REMOTE_BASE_RESP_MSG_START + 21;//回应获取NTP配置信息    
	    public static final int REMOTE_MSG_RESP_SET_DDNS = REMOTE_BASE_RESP_MSG_START + 22;//回应设置NTP配置信息
	    public static final int REMOTE_MSG_RESP_GET_UPNP = REMOTE_BASE_RESP_MSG_START + 23;//回应获取NTP配置信息    
	    public static final int REMOTE_MSG_RESP_SET_UPNP = REMOTE_BASE_RESP_MSG_START + 24;//回应设置NTP配置信息
	    
	    public static final int REMOTE_MSG_RESP_GET_FTP  = REMOTE_BASE_RESP_MSG_START + 25;//回应获取NTP配置信息    
	    public static final int REMOTE_MSG_RESP_SET_FTP = REMOTE_BASE_RESP_MSG_START + 26;//回应设置NTP配置信息
	    public static final int REMOTE_MSG_RESP_GET_MAIL = REMOTE_BASE_RESP_MSG_START + 27;//回应获取NTP配置信息    
	    public static final int REMOTE_MSG_RESP_SET_MAIL = REMOTE_BASE_RESP_MSG_START + 28;//回应设置NTP配置信息
	    public static final int REMOTE_MSG_RESP_GET_AUDIO = REMOTE_BASE_RESP_MSG_START + 29;//回应获取音频配置信息  
	    
	    public static final int REMOTE_MSG_RESP_SET_AUDIO = REMOTE_BASE_RESP_MSG_START + 30;//回应设置音频配置信息 30
	    public static final int REMOTE_MSG_RESP_GET_VIDEO_CH = REMOTE_BASE_RESP_MSG_START + 31;//回应获取视频通道配置信息    
	    public static final int REMOTE_MSG_RESP_SET_VIDEO_CH = REMOTE_BASE_RESP_MSG_START + 32;//回应设置视频通道配置信息
	    public static final int REMOTE_MSG_RESP_GET_IO_ALARM = REMOTE_BASE_RESP_MSG_START + 33;//回应获取IO告警配置信息    
	    public static final int REMOTE_MSG_RESP_SET_IO_ALARM = REMOTE_BASE_RESP_MSG_START + 34;//回应设置IO告警配置信息

	    public static final int REMOTE_MSG_RESP_GET_MOTION_ALARM = REMOTE_BASE_RESP_MSG_START + 35;//回应获取移动侦测配置信息    
	    public static final int REMOTE_MSG_RESP_SET_MOTION_ALARM = REMOTE_BASE_RESP_MSG_START + 36;//回应设置移动侦测配置信息
	    public static final int REMOTE_MSG_RESP_GET_LOST_ALARM = REMOTE_BASE_RESP_MSG_START + 37;//回应获取视频丢失告警配置信息    
	    public static final int REMOTE_MSG_RESP_SET_LOST_ALARM = REMOTE_BASE_RESP_MSG_START + 38;//回应设置视频丢失告警配置信息
	    public static final int REMOTE_MSG_RESP_GET_MASK_ALARM = REMOTE_BASE_RESP_MSG_START + 39;//回应获取视频遮挡告警配置信息   
	    
	    public static final int REMOTE_MSG_RESP_SET_MASK_ALARM = REMOTE_BASE_RESP_MSG_START + 40;//回应设置视频遮挡告警配置信息  40
	    public static final int REMOTE_MSG_RESP_GET_SERAIL = REMOTE_BASE_RESP_MSG_START + 41;//回应获取串口的配置信息  
	    public static final int REMOTE_MSG_RESP_SET_SERAIL = REMOTE_BASE_RESP_MSG_START + 42;//回应设置串口的配置信息   
	    public static final int REMOTE_MSG_RESP_GET_LOG = REMOTE_BASE_RESP_MSG_START + 43;//回应获取日志的配置信息  
	    public static final int REMOTE_MSG_RESP_SET_LOG = REMOTE_BASE_RESP_MSG_START + 44;//回应设置日志的配置信息   

	    public static final int REMOTE_MSG_RESP_GET_PTZ = REMOTE_BASE_RESP_MSG_START + 45; //云台
	    public static final int REMOTE_MSG_RESP_SET_PTZ = REMOTE_BASE_RESP_MSG_START + 46;
	    public static final int REMOTE_MSG_RESP_GET_EX_NETWORK  = REMOTE_BASE_RESP_MSG_START + 47;//回应获取扩展网络配置信息    
	    public static final int REMOTE_MSG_RESP_SET_EX_NETWORK = REMOTE_BASE_RESP_MSG_START + 48;//回应设置扩展网络配置信息
	    public static final int REMOTE_MSG_RESP_GET_USER_INFO = REMOTE_BASE_RESP_MSG_START + 49;// 回应取用户的信息

	    public static final int REMOTE_MSG_RESP_GET_VIDEO_ATTR = REMOTE_BASE_RESP_MSG_START + 50;// 回应取用户的视频信息 50
	    public static final int REMOTE_MSG_RESP_SET_VIDEO_ATTR = REMOTE_BASE_RESP_MSG_START + 51;// 回应设置用户的视频信息
	    public static final int REMOTE_MSG_RESP_SET_FIX_CONF = REMOTE_BASE_RESP_MSG_START + 52;//回应设置服务器基本固有配置，所有这些配置在出厂时固化，用户不能随意修改。
	    public static final int REMOTE_MSG_RESP_BRAODCAST = REMOTE_BASE_RESP_MSG_START + 53;//广播或多播
	    public static final int REMOTE_MSG_RESP_ALARM_ACTION = REMOTE_BASE_RESP_MSG_START + 54;//主动发送告警消息
	    
	    public static final int REMOTE_MSG_RESP_PTZ_CONTROL_CMD = REMOTE_BASE_RESP_MSG_START + 55;
	    public static final int REMOTE_MSG_RESP_VIEW_LOG = REMOTE_BASE_RESP_MSG_START + 56;//查看管理日志
	    public static final int REMOTE_MSG_RESP_LOG_DATA = REMOTE_BASE_RESP_MSG_START + 57;//查看管理日志
	    public static final int REMOTE_MSG_RESP_VIEW_ALARM_LOG = REMOTE_BASE_RESP_MSG_START + 58;//查看告警日志
	    public static final int REMOTE_MSG_RESP_ALARM_LOG_DATA = REMOTE_BASE_RESP_MSG_START + 59;//查看告警日志数据

	    
	    public static final int REMOTE_MSG_RESP_SEND_LOG_DATA_OK = REMOTE_BASE_RESP_MSG_START + 60; //60
	    public static final int REMOTE_MSG_RESP_SEND_ALARM_LOG_DATA_OK = REMOTE_BASE_RESP_MSG_START + 61;
	    public static final int REMOTE_MSG_RESP_REQ_DECODE_AUDIO = REMOTE_BASE_RESP_MSG_START + 62;
	    public static final int REMOTE_MSG_RESP_END_DECODE_AUDIO = REMOTE_BASE_RESP_MSG_START + 63;
	    public static final int REMOTE_MSG_RESP_REQ_COVER_PIC = REMOTE_BASE_RESP_MSG_START + 64;//回应请求视频遮挡图片
	    
	    public static final int REMOTE_MSG_RESP_DISK_INFO_REQ = REMOTE_BASE_RESP_MSG_START + 65;//获取硬盘信息
	    public static final int REMOTE_MSG_RESP_DISK_PART_REQ = REMOTE_BASE_RESP_MSG_START + 66;//请求硬盘分区
	    public static final int REMOTE_MSG_RESP_DISK_FORMAT_REQ = REMOTE_BASE_RESP_MSG_START + 67;//请求硬盘格式化
	    public static final int REMOTE_MSG_RESP_SET_GPIO_DIR_REQ = REMOTE_BASE_RESP_MSG_START + 68;//回应设置GIOP的方向
	    public static final int REMOTE_MSG_RESP_GET_GPIO_DIR_REQ = REMOTE_BASE_RESP_MSG_START + 69;
	    
	    public static final int REMOTE_MSG_RESP_SET_GPIO_VAL_REQ = REMOTE_BASE_RESP_MSG_START + 70;//回一次设置GIOP的值 //70
	    public static final int REMOTE_MSG_RESP_GET_GPIO_VAL_REQ = REMOTE_BASE_RESP_MSG_START + 71;
	    public static final int REMOTE_MSG_RESP_SET_RTC_LOCAL_TIME = REMOTE_BASE_RESP_MSG_START + 72;// 72
	    public static final int REMOTE_MSG_RESP_GET_RTC_LOCAL_TIME = REMOTE_BASE_RESP_MSG_START + 73;
	    public static final int REMOTE_MSG_RESP_SET_CAMERA_LENS = REMOTE_BASE_RESP_MSG_START + 74;
	    
	    public static final int REMOTE_MSG_RESP_GET_CAMERA_LENS = REMOTE_BASE_RESP_MSG_START + 75;
	    public static final int REMOTE_MSG_RESP_LOCAL_PLAY_REQ = REMOTE_BASE_RESP_MSG_START + 76;//回应播放硬盘存储媒体流请求
	    public static final int REMOTE_MSG_RESP_UPLOAD_PLAY_REQ = REMOTE_BASE_RESP_MSG_START + 77;// 回应PC发送媒体流给解码器播放请求
	    public static final int REMOTE_MSG_RESP_LINK_DEC_REQ = REMOTE_BASE_RESP_MSG_START + 78; //回应自动连接编码器（IPCAM），播放媒体流请求
	    public static final int REMOTE_MSG_RESP_END_PLAY_REQ = REMOTE_BASE_RESP_MSG_START + 79; //回应结束播放请求

	    public static final int REMOTE_MSG_RESP_SOURCH_FILE = REMOTE_BASE_RESP_MSG_START + 80; // 80 回应查找文件 
	    public static final int REMOTE_MSG_RESP_UPLOAD_FILE = REMOTE_BASE_RESP_MSG_START + 81;
	    public static final int REMOTE_MSG_RESP_DELETE_FILE = REMOTE_BASE_RESP_MSG_START + 82;
	    public static final int REMOTE_MSG_RESP_START_MUTIL_PROCESS = REMOTE_BASE_RESP_MSG_START + 83;
	    public static final int REMOTE_MSG_RESP_STOP_MUTIL_PROCESS = REMOTE_BASE_RESP_MSG_START + 84;
	    
	    public static final int REMOTE_MSG_RESP_SET_PTZ_CONTROL_CMD = REMOTE_BASE_RESP_MSG_START + 85;//85
	    public static final int REMOTE_MSG_RESP_UPDATE_REQ = REMOTE_BASE_RESP_MSG_START +86;//
	    public static final int REMOTE_MSG_RESP_DOWN_UPDATE_DATE = REMOTE_BASE_RESP_MSG_START + 87;
	    public static final int REMOTE_MSG_RESP_REBOOT_REQ = REMOTE_BASE_RESP_MSG_START + 88;
	    public static final int REMOTE_MSG_RESP_DOWNLOAN_FILE = REMOTE_BASE_RESP_MSG_START +89;

	    public static final int REMOTE_MSG_RESP_GET_RECORD_INFO = REMOTE_BASE_RESP_MSG_START +90; //  90
	    public static final int REMOTE_MSG_RESP_SET_RECORD_INFO = REMOTE_BASE_RESP_MSG_START + 91;
	    public static final int REMOTE_MSG_RESP_LOCAL_PLAY_OR_PAUSE_REQ = REMOTE_BASE_RESP_MSG_START + 92;
	    public static final int REMOTE_MSG_RESP_LOCAL_PLAY_END_REQ = REMOTE_BASE_RESP_MSG_START + 93;
	    public static final int REMOTE_MSG_RESP_DECORD_PLAY_FILE_END = REMOTE_BASE_RESP_MSG_START + 94;

	    public static final int REMOTE_MSG_RESP_DECORD_DATA_FAST = REMOTE_BASE_RESP_MSG_START + 95;//95
	    public static final int REMOTE_MSG_RESP_START_REC = REMOTE_BASE_RESP_MSG_START + 96;
	    public static final int REMOTE_MSG_RESP_START_PRE_REC = REMOTE_BASE_RESP_MSG_START + 97;
	    public static final int REMOTE_MSG_RESP_GET_FREE_CAP_INFO = REMOTE_BASE_RESP_MSG_START + 98;
	    public static final int REMOTE_MSG_RESP_GET_TOUR_INFO = REMOTE_BASE_RESP_MSG_START + 99;


	    public static final int REMOTE_MSG_RESP_SET_TOUR_INFO = REMOTE_BASE_RESP_MSG_START + 100;// 100
	    public static final int REMOTE_MSG_RESP_GET_NET_PTZ_INFO = REMOTE_BASE_RESP_MSG_START + 101;
	    public static final int REMOTE_MSG_RESP_MUTILCAST = REMOTE_BASE_RESP_MSG_START + 102;//多播
	    public static final int REMOTE_MSG_RESP_GET_VIDEO_ROI = REMOTE_BASE_RESP_MSG_START + 103;	//4103
	    public static final int REMOTE_MSG_RESP_SET_VIDEO_ROI = REMOTE_BASE_RESP_MSG_START + 104;	//104
	    public static final int REMOTE_MSG_RESP_GET_VIDEO_CORRIDOR = REMOTE_BASE_RESP_MSG_START + 105;
	    public static final int REMOTE_MSG_RESP_SET_VIDEO_CORRIDOR = REMOTE_BASE_RESP_MSG_START + 106;
	    public static final int REMOTE_MSG_RESP_GET_VI_LDC = REMOTE_BASE_RESP_MSG_START + 107;
	    public static final int REMOTE_MSG_RESP_SET_VI_LDC = REMOTE_BASE_RESP_MSG_START + 108;
		public static final int REMOTE_MSG_RESP_GET_ANTIFOG = REMOTE_BASE_RESP_MSG_START + 109;
		public static final int REMOTE_MSG_RESP_SET_ANTIFOG = REMOTE_BASE_RESP_MSG_START + 110;
		public static final int REMOTE_MSG_RESP_GET_AUTO_UPGRADE = REMOTE_BASE_RESP_MSG_START + 111;//4111
		public static final int REMOTE_MSG_RESP_SET_AUTO_UPGRADE = REMOTE_BASE_RESP_MSG_START + 112;
		public static final int REMOTE_MSG_RESP_GET_CLOUD_STORAGE = REMOTE_BASE_RESP_MSG_START + 113;
		public static final int REMOTE_MSG_RESP_SET_CLOUD_STORAGE = REMOTE_BASE_RESP_MSG_START + 114;

		public static final int REMOTE_MSG_RESP_3DCONTROL_CMD_BASE = 4200;
	    //扩展命令从4200开始
		public static final int REMOTE_MSG_RESP_PTZ_3DCONTROL_CMD = REMOTE_MSG_RESP_3DCONTROL_CMD_BASE;	//用于球机3D
		public static final int REMOTE_MSG_RESP_PTZ_SET_ABS_CMD = REMOTE_MSG_RESP_3DCONTROL_CMD_BASE + 1;    //设置PTZ绝对坐标    
		public static final int REMOTE_MSG_RESP_PTZ_GET_ABS_CMD = REMOTE_MSG_RESP_3DCONTROL_CMD_BASE + 2;	//读取PTZ绝对坐标
		public static final int REMOTE_MSG_RESP_EXPOSURE_CMD = REMOTE_MSG_RESP_3DCONTROL_CMD_BASE + 3;	//3516环境、WDR、电子快门等设置
		public static final int REMOTE_MSG_RESP_WIFI_SEARCH = REMOTE_MSG_RESP_3DCONTROL_CMD_BASE + 4;	//搜索WIFI设备响应

		public static final int REMOTE_MSG_RESP_GET_NETWORK_V2 = REMOTE_MSG_RESP_3DCONTROL_CMD_BASE + 5;//05
		public static final int REMOTE_MSG_RESP_SET_NETWORK_V2 = REMOTE_MSG_RESP_3DCONTROL_CMD_BASE + 6;
		public static final int REMOTE_MSG_RESP_BRAODCAST_CHANGEIP = REMOTE_MSG_RESP_3DCONTROL_CMD_BASE + 7;		//以广播的方式修改IP
		public static final int REMOTE_MSG_RESP_GET_VIDEO_INFO_INDEX = REMOTE_MSG_RESP_3DCONTROL_CMD_BASE + 8;	//获取当前设备支持的分辨率索引
		public static final int REMOTE_MSG_RESP_GET_3G_INFO = REMOTE_MSG_RESP_3DCONTROL_CMD_BASE + 9;	//获取3G信息
	    
	    public static final int REMOTE_MSG_RESP_SET_3G_INFO = REMOTE_MSG_RESP_3DCONTROL_CMD_BASE + 10;	//10//设置3G信息
	    public static final int REMOTE_MSG_RESP_GET_SNAP_INFO = REMOTE_MSG_RESP_3DCONTROL_CMD_BASE + 11;		//获取抓拍信息
	    public static final int REMOTE_MSG_RESP_SET_SNAP_INFO = REMOTE_MSG_RESP_3DCONTROL_CMD_BASE + 12;		//设置抓拍信息
	    public static final int REMOTE_MSG_RESP_SET_ENV4UPDATE = REMOTE_MSG_RESP_3DCONTROL_CMD_BASE + 13;	//为了升级下一版本进行环境设置
	    public static final int REMOTE_MSG_RESP_CPY_UPDATE_FILE_LEN = REMOTE_MSG_RESP_3DCONTROL_CMD_BASE + 14;	//上报升级文件拷贝情况
	    
	    public static final int REMOTE_MSG_RESP_GET_REBOOT_POLICY = REMOTE_MSG_RESP_3DCONTROL_CMD_BASE + 15;		/*4215 回复获取定时重启策略*/
		public static final int REMOTE_MSG_RESP_SET_REBOOT_POLICY = REMOTE_MSG_RESP_3DCONTROL_CMD_BASE + 16;

		public static final int REMOTE_MSG_RESP_GET_P2P_INFO = REMOTE_MSG_RESP_3DCONTROL_CMD_BASE + 17;
		public static final int REMOTE_MSG_RESP_SET_P2P_INFO = REMOTE_MSG_RESP_3DCONTROL_CMD_BASE + 18;
		public static final int REMOTE_MSG_RESP_SET_LIGHTING = REMOTE_MSG_RESP_3DCONTROL_CMD_BASE + 19;
		public static final int REMOTE_MSG_RESP_CLICK_BUTTON_WPS = REMOTE_MSG_RESP_3DCONTROL_CMD_BASE + 20;		/*4220 回复按下WPS按钮*/
		
	    //not use for net user
		public static final int REMOTE_MSG_RESP_MOD_SYS_INFO 	= REMOTE_BASE_SPE_RESP_MSG_START;
	    public static final int REMOTE_MSG_RESP_MOD_TOS_INFO 	= REMOTE_BASE_SPE_RESP_MSG_START + 1;
	    public static final int REMOTE_MSG_RESP_ADD_LOCAL_FILE 	= REMOTE_BASE_SPE_RESP_MSG_START + 2;
	    
	    
	    //这部分用作消息返回的结果
	    public static final int REMOTE_MSG_WRITE_HD_ERROR 		= 		REMOTE_BASE_MSG_RESULT; //写磁盘失败
	    public static final int REMOTE_MSG_RD_FILE_ERROR 		= 		REMOTE_BASE_MSG_RESULT + 1;      // 读文件失败
	    public static final int REMOTE_MSG_MEM_FAIL 			= 		REMOTE_BASE_MSG_RESULT + 2;            //内存开辟失败
	    public static final int REMOTE_MSG_USER_NO_LICENCE 		= 		REMOTE_BASE_MSG_RESULT + 3;     //用户没有被授权许可
	    public static final int REMOTE_MSG_USER_PASSWD_UNVALID 	= 		REMOTE_BASE_MSG_RESULT + 4; //用户和密码错误 
	    
	    public static final int REMOTE_MSG_USER_NAME_EMPTY 		= 		REMOTE_BASE_MSG_RESULT + 5;     //用户名为空 //5
	    public static final int REMOTE_MSG_LOGIN_OK  			= 		REMOTE_BASE_MSG_RESULT + 6;           // 客户端已经登陆
	    public static final int REMOTE_MSG_USER_EXIST 			= 		REMOTE_BASE_MSG_RESULT + 7;          //用户已经存在
	    public static final int REMOTE_MSG_USER_FULL 			= 		REMOTE_BASE_MSG_RESULT + 8;           // 用户已经满了，达到最大用户的个数
	    public static final int REMOTE_MSG_USER_UNDEL 			= 		REMOTE_BASE_MSG_RESULT + 9;          //用户不可以删除
	    
	    public static final int REMOTE_MSG_USER_NOT_EXIST 		= 		REMOTE_BASE_MSG_RESULT + 10;     //用户不存在 //10
	    public static final int REMOTE_MSG_MODIFY_LAST 			= 		REMOTE_BASE_MSG_RESULT + 11;         //修改的信息已经是最新的
	    public static final int REMOTE_MSG_SERVER_BUSY 			= 		REMOTE_BASE_MSG_RESULT + 12;
	    public static final int REMOTE_MSG_PARAM_ERR 			= 		REMOTE_BASE_MSG_RESULT + 13;           //参数设置错误
	    public static final int REMOTE_MSG_FILE_NO_EXIST 		= 		REMOTE_BASE_MSG_RESULT + 14;           //文件不存在
	    
	    public static final int REMOTE_MSG_INSUFFICIENT_PRIVILEGES = 	REMOTE_BASE_MSG_RESULT + 15; //权限不足
	    public static final int REMOTE_MSG_RESP_FUNC_NO_EXIST 	= 		REMOTE_BASE_MSG_RESULT + 16;	//指示没有这个命令
	    public static final int REMOTE_MSG_RESP_TLV_NOT_EXIST 	= 		REMOTE_BASE_MSG_RESULT + 17;	//TLV不存在
	    public static final int REMOTE_MSG_RESP_NO_WIRELESS_DEV = 		REMOTE_BASE_MSG_RESULT + 18;		//设备的无线功能故障
}