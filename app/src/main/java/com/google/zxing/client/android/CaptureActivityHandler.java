/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import freesbell.demo.client.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import freesbell.demo.client.CaptureActivityPortrait;
import freesbell.demo.utils.DatabaseUtil;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.android.camera.CameraManager;

/**
 * 处理二维码的回调handler
 */
public final class CaptureActivityHandler extends Handler {
	private DatabaseUtil dbUtil = null;
	private static final String TAG = CaptureActivityHandler.class
			.getSimpleName();

	private final CaptureActivityPortrait activity;
	private final DecodeThread decodeThread;// 检测线程
	private State state;
	
	private enum State {
		PREVIEW, SUCCESS, DONE
	}

	private List<ScanResult> listScan;
	private List<Integer> list;
	public CaptureActivityHandler(CaptureActivityPortrait activity,
			Vector<BarcodeFormat> decodeFormats, String characterSet,List<ScanResult> listScan,Context ctxt) {
		this.activity = activity;
		dbUtil = new DatabaseUtil(ctxt);
		decodeThread = new DecodeThread(activity, decodeFormats, characterSet,
				new ViewfinderResultPointCallback(activity.getViewfinderView()));
		decodeThread.start();
		state = State.SUCCESS;
		
		// Start ourselves capturing previews and decoding.
		CameraManager.get().startPreview();
		restartPreviewAndDecode();
		this.listScan = listScan;
	}

	

	@Override
	public void handleMessage(Message message) {
		switch (message.what) {
		case R.id.auto_focus:
			if (state == State.PREVIEW) {
				CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
			}
			break;
		case R.id.restart_preview:
			Log.d(TAG, "Got restart preview message");
			restartPreviewAndDecode();
			break;
		case R.id.decode_succeeded: {
			// 成功取得二维码

			Log.d(TAG, "Got decode succeeded message");
			state = State.SUCCESS;
			Bundle bundle = message.getData();
			Bitmap barcode = bundle == null ? null : (Bitmap) bundle
					.getParcelable(DecodeThread.BARCODE_BITMAP);
			activity.handleDecode((Result) message.obj, barcode);
			break;

		}
		case R.id.decode_failed:
			// We're decoding as fast as possible, so when one decode fails,
			// start another.
			state = State.PREVIEW;
			CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
					R.id.decode);
			break;
		case R.id.return_scan_result:
			Log.d(TAG, "Got return scan result message");
			activity.setResult(Activity.RESULT_OK, (Intent) message.obj);
			activity.finish();
			break;
		case R.id.launch_product_query:
			Log.d(TAG, "Got product query message");
			String url = (String) message.obj;
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			activity.startActivity(intent);
			break;
//		case 1:
//			list = (List<Integer>) message.obj;
//			List<ScanResult> listdialog = new ArrayList<ScanResult>();
//			ScanResult sr;
//			for (int i = 0; i < list.size(); i++) {
//				sr = listScan.get(list.get(i));
//				listdialog.add(sr);
//			}
//			Log.i("wifiinfo","筛检得到了多少条符合信息:"+listdialog.size());
//			activity.onCreateDialog(listdialog);
//			break;
		} 
	}
	
	public void quitSynchronously() {
		state = State.DONE;
		CameraManager.get().stopPreview();
		Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
		quit.sendToTarget();
		try {
			decodeThread.join();
		} catch (InterruptedException e) {
			// continue
		}

		// Be absolutely sure we don't send any queued up messages
		removeMessages(R.id.decode_succeeded);
		removeMessages(R.id.decode_failed);
	}

	private void restartPreviewAndDecode() {
		if (state == State.SUCCESS) {
			state = State.PREVIEW;
			CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
					R.id.decode);
			CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
			activity.drawViewfinder();
		}
	}

}
