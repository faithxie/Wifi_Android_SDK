package freesbell.demo.dialog;

import freesbell.demo.client.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import freesbell.demo.adapter.RemoteFileSettingAdapter;

public class RemoteMediaFileDialog extends Dialog implements
		android.widget.AdapterView.OnItemClickListener {
	private final static String TAG = "RemoteMediaFileDialog";
	public LinearLayout mRoot;
	private RemoteFileSettingAdapter mAdapter;
//	private DeviceData mDeviceData;
	private Context mActivity;
	public RemoteMediaFileDialog(Context context, String tile, int width, int height) {
		super(context);
		mActivity = context;
		this.getContext()
				.setTheme(android.R.style.Theme_Translucent_NoTitleBar);
		super.setContentView(R.layout.mainsettingdialog);
		LinearLayout contextLy = (LinearLayout) findViewById(R.id.context_layout);
//		mDeviceData = dd;

		mAdapter = new RemoteFileSettingAdapter(context);
		ListView listView = (ListView) findViewById(R.id.context_listview);
		listView.setAdapter(mAdapter);
		TextView tiletv = (TextView)findViewById(R.id.tile_tv);
		tiletv.setVisibility(View.VISIBLE);
		tiletv.setText(tile);
		//divider line
		findViewById(R.id.setting_divider).setVisibility(View.VISIBLE);
		
		int totalHeight = 0;
		int oneMoreHeight = 0;
		for (int i = 0, len = mAdapter.getCount(); i < len; i++) { // listAdapter.getCount()������������Ŀ
			View listItem = mAdapter.getView(i, null, listView);
			listItem.measure(0, 0); //
			totalHeight += listItem.getMeasuredHeight(); //
			oneMoreHeight = listItem.getMeasuredHeight();
		}
		totalHeight += oneMoreHeight;
		totalHeight += (listView.getDividerHeight() * (mAdapter.getCount() - 1 ));
//		Log.d(TAG,"TILE Height" + oneMoreHeight + " totalHeight" + totalHeight);
		int count = mAdapter.getCount() + 1;
//		Log.d(TAG, "count:" + count);
		LinearLayout.LayoutParams lpp = new LinearLayout.LayoutParams(
				width * 3 / 4, totalHeight);
		contextLy.setLayoutParams(lpp);

		listView.setOnItemClickListener(this);

		mRoot = (LinearLayout) findViewById(R.id.root);
		Window window = this.getWindow();
		window.setGravity(Gravity.CENTER);
		android.view.WindowManager.LayoutParams lp = window.getAttributes();
		window.setLayout(width, height);

	}

//	public RemoteMediaFileDialog(Context context, int width, int height) {
//		super(context);
//		this.getContext()
//				.setTheme(android.R.style.Theme_Translucent_NoTitleBar);
//		super.setContentView(R.layout.mainsettingdialog);
//		LinearLayout contextLy = (LinearLayout) findViewById(R.id.context_layout);
//		
//	}

	@Override
	public void dismiss() {
		if (isShowing()) {
			superDismiss();
		}
	}

	@Override
	public void show() {
		super.show();
	}

	private void superDismiss() {
		super.dismiss();
	}

	public void setOnClickListener(OnListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	private OnListener onClickListener;

	public interface OnListener {
		public void onItemClick(int itemposition, int count);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		superDismiss();
		if (onClickListener != null) {
			onClickListener.onItemClick(position, mAdapter.getCount());
		}
//		Option_method om = mAdapter.getItemFunc(position);
//		if(om!=null)
//			om.func(mActivity);
//		else{
//			Log.d(TAG,"No item func defined!");
//		}
	}
}
