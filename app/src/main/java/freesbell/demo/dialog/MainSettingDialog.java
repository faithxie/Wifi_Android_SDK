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
import freesbell.demo.adapter.MainSettingAdapter;

public class MainSettingDialog extends Dialog implements
		android.widget.AdapterView.OnItemClickListener {
	public LinearLayout mRoot;
	private Handler handler = new Handler();
	private MainSettingAdapter mAdapter;

	public MainSettingDialog(Context context, int width, int height, int status) {
		super(context);

		this.getContext()
				.setTheme(android.R.style.Theme_Translucent_NoTitleBar);
		super.setContentView(R.layout.mainsettingdialog);
		LinearLayout contextLy = (LinearLayout) findViewById(R.id.context_layout);

		mAdapter = new MainSettingAdapter(context, status);
		ListView listView = (ListView) findViewById(R.id.context_listview);
		listView.setAdapter(mAdapter);

		int totalHeight = 0;
		for (int i = 0, len = mAdapter.getCount(); i < len; i++) { // listAdapter.getCount()������������Ŀ
			View listItem = mAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // ��������View �Ŀ��
			totalHeight += listItem.getMeasuredHeight(); // ͳ������������ܸ߶�
		}
		totalHeight += (listView.getDividerHeight() * (mAdapter.getCount() - 1));

		int count = mAdapter.getCount() + 1;
		Log.d("tag", "count:" + count);
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

	public MainSettingDialog(Context context, int width, int height) {
		super(context);
		this.getContext()
				.setTheme(android.R.style.Theme_Translucent_NoTitleBar);
		super.setContentView(R.layout.mainsettingdialog);
		LinearLayout contextLy = (LinearLayout) findViewById(R.id.context_layout);
		
	}

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
	}
}
