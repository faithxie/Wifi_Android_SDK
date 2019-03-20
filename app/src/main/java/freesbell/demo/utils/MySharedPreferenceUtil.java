package freesbell.demo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * store package Name
 * @author Administrator
 *
 */
public class MySharedPreferenceUtil {

	private static SharedPreferences prefer;
	private final static String PRENAME = "prefer_packa_name";
	public static MySharedPreferenceUtil mysharedpreferenceutil;
	
	//public final static String FILECOVER = "filecover";  //录像文件覆盖的标识
//	public final static String FILEWARNCOVER = "filewarncover"; //警报文件覆盖的标识
	
//	public final static String WARN = "warnsetting"; //警报文件覆盖的标识
	
	
	
	public static boolean savePackName(Context context,String KeyName, String packName)
	{
		prefer = context.getSharedPreferences(PRENAME, Context.MODE_PRIVATE);
		Editor editor = prefer.edit();
		editor.putString(KeyName, packName);
		System.out.println("保存： "+KeyName+" "+packName);
		return editor.commit();
	}
	
	public static String getPackName(Context context, String keyName,String defValue)
	{
		prefer = context.getSharedPreferences(PRENAME, Context.MODE_PRIVATE);
		return prefer.getString(keyName, defValue);
	}
	
	public static boolean delPackName(Context context, String did)
	{
		prefer = context.getSharedPreferences(PRENAME, Context.MODE_PRIVATE);
		Editor editor = prefer.edit();
		editor.remove(did + "warn");
		editor.remove(did);
		return editor.commit();
	}
	
	public static boolean putRecoverFile(Context context,String key, String value)
	{
		prefer = context.getSharedPreferences(PRENAME, Context.MODE_PRIVATE);
		Editor editor = prefer.edit();
		editor.putString(key, value);
		System.out.println("保存： "+key+" "+value);
		return editor.commit();
	}
	
	public static String getRecoverFile(Context context,String key, String defaultValue)
	{
		prefer = context.getSharedPreferences(PRENAME, Context.MODE_PRIVATE);
		return prefer.getString(key, defaultValue);
	}
	public static boolean putString(Context context,String key, String value)
	{
		prefer = context.getSharedPreferences(PRENAME, Context.MODE_PRIVATE);
		Editor editor = prefer.edit();
		editor.putString(key, value);
		System.out.println("保存： "+key+" "+value);
		return editor.commit();
	}
	
	public static String getString(Context context,String key, String defaultValue)
	{
		prefer = context.getSharedPreferences(PRENAME, Context.MODE_PRIVATE);
		return prefer.getString(key, defaultValue);
	}
	
	public static boolean putBoolean(Context context,String key, boolean value)
	{
		prefer = context.getSharedPreferences(PRENAME, Context.MODE_PRIVATE);
		Editor editor = prefer.edit();
		editor.putBoolean(key, value);
		System.out.println("保存： "+key+" "+value);
		return editor.commit();
	}
	
	public static boolean getBoolean(Context context,String key, boolean defaultValue)
	{
		prefer = context.getSharedPreferences(PRENAME, Context.MODE_PRIVATE);
		return prefer.getBoolean(key, defaultValue);
	}
	public static boolean putLong(Context context,String key, long value)
	{
		prefer = context.getSharedPreferences(PRENAME, Context.MODE_PRIVATE);
		Editor editor = prefer.edit();
		editor.putLong(key, value);
		System.out.println("保存： "+key+" "+value);
		return editor.commit();
	}
	
	public static long getLong(Context context,String key, long defaultValue)
	{
		prefer = context.getSharedPreferences(PRENAME, Context.MODE_PRIVATE);
		return prefer.getLong(key, defaultValue);
	}
}

