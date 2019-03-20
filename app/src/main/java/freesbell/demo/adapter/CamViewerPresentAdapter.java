package freesbell.demo.adapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import freesbell.demo.client.R;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import freesbell.demo.client.CameraViewerActivity;
import freesbell.demo.content.ContentCommon;
import freesbell.demo.utils.DatabaseUtil;
/**
 * 
 * @author hujian
 */
public class CamViewerPresentAdapter extends BaseAdapter {
	private final String TAG = "CamViewerPresentAdapter";
   private Context context;
   private String strDID;
   private CameraViewerActivity playactivity;
   private DatabaseUtil dbUtil;
   //private int[] setarray;
   private int pos;
//   private  String[] getarray;
   public ViewHolder holder =null;
//   public final int listposotion;
   private String path;
//   public ArrayList<String> listpath;
   public ArrayList<PresetBean> mPresetList = new ArrayList<PresetBean>();
   
	public CamViewerPresentAdapter(Context context,CameraViewerActivity play,String strDID,ArrayList<PresetBean> list){
		this.context= context;
		this.strDID = strDID;
		this.playactivity = play;
//		this.listposotion = listposoiton;

		dbUtil = new DatabaseUtil(context);

		mPresetList = list;
	}
	
	public int getCount() {
		if(mPresetList == null){
			return 0;
		}
		return mPresetList.size(); 
	}

	@Override
	public Object getItem(int arg0) {
		if(mPresetList == null){
			return null;
		}
		return mPresetList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		synchronized (this) {
			PresetBean bean = mPresetList.get(arg0);
			if(bean == null){
				return null;
			}
			if(arg1 == null){
				holder = new ViewHolder();
				arg1= LayoutInflater.from(context).inflate(R.layout.persentadapter_list_cell, null);
				holder.img = (ImageView) arg1.findViewById(R.id.presentadapter_img);
				holder.setpresent = (ImageButton) arg1.findViewById(R.id.presentadapter_set);
				holder.getpresent = (TextView) arg1.findViewById(R.id.presentadapter_get);
				arg1.setTag(holder);
			}else{
				holder = (ViewHolder) arg1.getTag();
			}
			holder.getpresent.setText(bean.name);
			
			String fpath = getpath(bean.pid);
			Log.d(TAG,"fpath" + bean.pid + ":" + fpath);
		    if(fpath != null){
		    	int index = fpath.lastIndexOf("/") + 1;
		    	String mess = fpath.substring(index,fpath.length()-1);
				if(mess.equals(strDID)){
					bean.snapshot = getsdcard(fpath);
				}
		    }
		    if(bean.snapshot == null){
				holder.img.setBackgroundResource(R.drawable.vidicon);
			}else{
				holder.img.setBackgroundDrawable(bean.snapshot);
			}
			PresentOnClickListener setonClickListener = new PresentOnClickListener(
					bean.pid);
			holder.setpresent.setOnClickListener(setonClickListener);
			
			MyClickListener myclicklistener = new MyClickListener(bean.pid);
			holder.img.setOnClickListener(myclicklistener);
			holder.getpresent.setOnClickListener(myclicklistener);
		}
		return arg1;
	}
	
	public final class ViewHolder{
		ImageView img;
		ImageButton setpresent;
		TextView getpresent;
		
	}
	
	public class MyClickListener implements OnClickListener{
		private int presetId;
        public MyClickListener(int pid){
        	this.presetId = pid;
        }
		@Override
		public void onClick(View v) {
			System.out.println(v.getId());
			switch(v.getId()){
			case R.id.presentadapter_img:
					playactivity.showgetImage(presetId);
				break;
			case R.id.presentadapter_get:
					playactivity.showgetImage(presetId);
				break;
			}
		}
	}
	public class PresentOnClickListener implements OnClickListener{
		private int pid;
		public ImageButton img;
        public PresentOnClickListener(int pid){
        	this.pid = pid;
        }
		@Override
		public void onClick(View v) {
			img = (ImageButton) v;
			playactivity.show(pid);
		}
		
	}
	private void addsdcard(int pid, Drawable drawable) {
		synchronized (this) {
			File div = new File(Environment.getExternalStorageDirectory(),
					"present/photo"+pid);
			if (!div.exists()) {
				div.mkdirs();
			}
			File file = new File(div, strDID+pid);//+".jpg")
			path = file.getAbsolutePath();
			System.out.println("addsdcard......................"+path);
			try {
				FileOutputStream fos = new FileOutputStream(file);
				BitmapDrawable bd = (BitmapDrawable) drawable;
				Bitmap bmp = bd.getBitmap();
				if (bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)) {
					fos.flush();
					dbUtil.open();
					dbUtil.createPresent(strDID, pid+"", path);
					dbUtil.close();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	private Drawable getsdcard(String filepath){
		synchronized (this) {
			Drawable drawable =null;
			if(filepath !=null){
				File file = new File(filepath);
				if(file.exists()){
					drawable = Drawable.createFromPath(filepath);
				}
			}
			return drawable;
		}
			
		
	}
	private String getpath(int position){
		synchronized (this) {
//			ArrayList<String> list = new ArrayList<String>();
			String filepath = null;
			dbUtil.open();
			Cursor cursor = dbUtil.queryAllPresent(strDID, position+"");
			while (cursor.moveToNext()) {
				filepath = cursor.getString(cursor.getColumnIndex("filepath"));
			}
			dbUtil.close();
			return filepath;
		}
		
	}
	/*private String getpath(int position){
		synchronized (this) {
			String filepath = null;
			File div = new File(Environment.getExternalStorageDirectory(),"photo"+position);
			if(div.exists()){
				File file = new File(div, strDID+position+".jpg");
				filepath = file.getAbsolutePath();
				if(!listpath.contains(filepath)){
					listpath.add(filepath);
				}
			}
			return filepath;
		}
		
	}*/

	public void deletePreset(int pid){
		for(PresetBean bean:mPresetList){
			if(bean.pid == pid){
				String path = getpath(bean.pid);
				if(path!=null){
					File file = new File(path);  
				    // 路径为文件且不为空则进行删除  
				    if (file.isFile() && file.exists()) {  
				        file.delete();
				    }
				}
			    
			    synchronized (this) {
				    dbUtil.open();
					dbUtil.deleteOnePresent(strDID, pid+"");
					dbUtil.close();
			    }
			    
				mPresetList.remove(bean);
				break;
			}
		}
	}
	public boolean add(int pid,Drawable drawable){
		PresetBean bean = getPresetBean(pid);
		if(bean == null){
			bean = new PresetBean();
			mPresetList.add(pid,bean);
		}else{
			return updater(pid,drawable);
		}
		
		bean.pid = pid;
//		bean.snapshot = drawable;

		synchronized (this) {
			addsdcard(pid, drawable);
			bean.snapshot = getsdcard(path);
		}
		
		return true;
	}
	
	public boolean updater(int pid,Drawable drawable){
		PresetBean bean = getPresetBean(pid);
		if(bean == null){
			bean = new PresetBean();
			mPresetList.add(pid,bean);
		}
		synchronized (this) {
			dbUtil.open();
			dbUtil.deleteOnePresent(strDID, pid+"");
			dbUtil.close();
			addsdcard(pid, drawable);
			bean.pid = pid;
			bean.snapshot = getsdcard(path);
			
			notifyDataSetChanged();
		}
		return true;
	}
	public PresetBean getPresetBean(int pid){
		for(PresetBean bean:mPresetList){
			if(bean.pid == pid){
				return bean;
			}
		}
		return null;
	}
	
	public class PresetBean{
		public String name;
//		String path;
		public Drawable snapshot;
		public int pid;
	}
}
