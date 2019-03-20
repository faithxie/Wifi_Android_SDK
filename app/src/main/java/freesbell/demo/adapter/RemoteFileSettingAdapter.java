package freesbell.demo.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import freesbell.demo.client.R;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import freesbell.demo.bean.Option_method;
import freesbell.demo.content.ContentCommon;
/**
 * 
 * @author �޸Ĺ��� ding fangchao
 *
 */
public class RemoteFileSettingAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater inflater;
	private List<Integer> mTileList = new ArrayList<Integer>();
	private List<Integer> mImgList = new ArrayList<Integer>();
//	private List<Option_method> mFuncList = new ArrayList<Option_method>();
//	private String[] ss;
//	private int[]  imgs;
	//private DeviceData mDeviceData;
    public RemoteFileSettingAdapter(Context context){
    	this.context=context;
    	inflater = LayoutInflater.from(context);
    	
    	mTileList.add(R.string.play);
    	mTileList.add(R.string.download);
    	mTileList.add(R.string.operation_delete);
    	mTileList.add(R.string.attribute_of_file);
    	mImgList.add(R.drawable.operation_button_info);
    	mImgList.add(R.drawable.operation_button_info);
    	mImgList.add(R.drawable.operation_button_info);
    	mImgList.add(R.drawable.operation_button_info);

    }
	@Override
	public int getCount() {
//		return ss.length;
		return mTileList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.mainsetting_listitem, null);
			holder=new ViewHolder();
			holder.img=(ImageView)convertView.findViewById(R.id.img);
			holder.tv=(TextView)convertView.findViewById(R.id.text);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		
		if(mTileList.size()==0){
			convertView.setBackgroundResource(R.drawable.listitem_one_pressed_selector);
		}else if(position==0){
			convertView.setBackgroundResource(R.drawable.listitem_pressed_top_corner_selector);
		}else if(position==mTileList.size()-1){
			convertView.setBackgroundResource(R.drawable.listitem_pressed_bottom_corner_selector);
		}else{
			convertView.setBackgroundResource(R.drawable.listitem_pressed_selector);
		}
		Resources resources = context.getResources();
		holder.tv.setText(resources.getString(mTileList.get(position)));
		holder.img.setBackgroundResource(mImgList.get(position));
		
		return convertView;
	}
    private class ViewHolder{
    	TextView tv;
    	ImageView img;
    }
}
