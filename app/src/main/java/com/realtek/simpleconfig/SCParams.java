package com.realtek.simpleconfig;

import android.net.wifi.ScanResult;

public class SCParams {
	public static boolean IsOpenNetwork = false; // Default has password, not open network
	public static String  ConnectedSSID;         // Connected WIFI's SSID
	public static String  ConnectedBSSID;         // Connected WIFI's BSSID
	public static String  ConnectedPasswd;       // Connected WIFI's Password(inputed)
	public static String  StoredPasswd;          // WIFI's Password got from store file
	public static String  PasswdText;            // WIFI's Password got from editText
	public static String  SecurityType;          // WIFI's security type

	/** For adding new wifi network */
	public static boolean isHiddenSSID;
	public static boolean addNewNetwork;
	public static ScanResult reBuiltScanResult;

	public static boolean mConnectQuiet;
	public static boolean DiscoveredNew;

	/** UDP Payload Format Flag */
	public static class Flag{

		private Flag(){
		}

		/** Flag */
		public static final int Version = (0x00<<6); // 2 bits
		public static final int RequestFlag = (0<<5); // 1 bit
		public static final int ResponseFlag = (1<<5);
		/** Request */
		public static final int Discover = 0x00;  	// 5 bits
		public static final int SaveProf = 0x01;
		public static final int DelProf = 0x02;
		public static final int RenameDev = 0x03;
		public static final int ReturnACK = 0x04;
		public static final int PackFail = 0x06;

		/** Response */
		public static final int CfgSuccessACK = 0x00;  // 5 bits
		public static final int DiscoverACK = 0x01;
		public static final int SaveProfACK = 0x02;
		public static final int DelProfACK = 0x03;
		public static final int RenameDevACK = 0x04;
		public static final int CFGTimeSendBack = 0x05;
	}
}
