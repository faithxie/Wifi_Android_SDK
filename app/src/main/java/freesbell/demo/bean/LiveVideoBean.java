package freesbell.demo.bean;
/*
 * author:hujian
 * date:2015/12 
 * */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import fenzhi.nativecaller.NativeCaller;
import freesbell.demo.client.CameraViewerActivity;
import freesbell.demo.content.ContentCommon;
import freesbell.demo.utils.DatabaseUtil;
import freesbell.demo.utils.LiveStreamVideoView;
import freesbell.demo.client.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LiveVideoBean{
	private final String TAG = "LiveVideoBean";
	public String mUUID;
	public String mVideoName;
	public int nP2PMode = ContentCommon.P2P_MODE_P2P_NORMAL;
	public ProgressBar par;
	public TextView tv;
	public TextView tvname;
	public LiveStreamVideoView mVideoView;
	public View layout;
	public ImageView mPausePic;
	
	public int mIndex;
	private Bitmap bitmap;
	public byte[] videodata = null;
	public int videoDataLen = 0;
	public int nVideoWidth = 0;
	public int nVideoHeight = 0;
	private int dataType =2;
	public boolean mPlaying = false;
	private boolean mLiveStreamStarted = false;
	public boolean mEnableTalk = false,mEnableVoice = false;
	
	public LiveVideoBean(String uuid,String name,int p2p_mode,LiveStreamVideoView video){
		mUUID = uuid;
		mVideoName = name;
		nP2PMode = p2p_mode;
		mVideoView = video;
		if(mVideoView!=null && mUUID!=null)
			mVideoView.setTag(this);
	}
	
	public void init(){
		if(mUUID!=null){
			if(par!=null){
				par.setVisibility(View.VISIBLE);
			}
			mPlaying = true;
		}
		if(mVideoView!=null){
			mVideoView.setUUID(mUUID);
		}
	}
	
	public void startLiveStream(){
		if(mVideoView!=null&&!mLiveStreamStarted){
			mVideoView.startLiveStream();
		}
		mPlaying = true;
		mLiveStreamStarted = true;
		if(mPausePic!=null && mPausePic.getVisibility() == View.VISIBLE){
			mPausePic.setVisibility(View.GONE);
		}
	}

	public void pause(){
		mPlaying = false;
		if(mPausePic!=null && mPausePic.getVisibility() != View.VISIBLE){
			mPausePic.setVisibility(View.VISIBLE);
		}
	}
	
	public void play(){
		mPlaying = true;
		if(mPausePic!=null && mPausePic.getVisibility() == View.VISIBLE){
			mPausePic.setVisibility(View.GONE);
		}
		if(!mLiveStreamStarted){
			if(mVideoView!=null){
				mVideoView.startLiveStream();
			}
		}
		mLiveStreamStarted = true;
	}
	
	public void stopLiveStream(){
		mPlaying = false;
		if(mVideoView!=null&&mLiveStreamStarted){
			mVideoView.stopLiveStream();
		}
		mLiveStreamStarted = false;
	}
	
	public void stopLiveStream(boolean saveShot){
		if(mVideoView!=null&&mLiveStreamStarted){
			mPlaying = false;
			mLiveStreamStarted = false;
			mVideoView.stopLiveStream();
			if(saveShot&&bitmap!=null){
				FileOutputStream fos = null;
				try {
					File div = new File(Environment.getExternalStorageDirectory(),
							"ipcam/thumbnail");
					if (!div.exists()) {
						div.mkdirs();
					}
					File file = new File(div, "video_thumbnail_" + mUUID+ "0" + ".jpg");
					fos = new FileOutputStream(file);
					
					int btmWidth = bitmap.getWidth();
					int btmHeight = bitmap.getHeight();
					float scaleW = ((float) 70) / btmWidth;
					float scaleH = ((float) 50) / btmHeight;
					Matrix matrix = new Matrix();
					matrix.postScale(scaleW, scaleH);
					Bitmap bt = Bitmap.createBitmap(bitmap, 0, 0, btmWidth,
							btmHeight, matrix, true);
					if (bt.compress(Bitmap.CompressFormat.JPEG, 100, fos)) {
						fos.flush();
					}
				} catch (Exception e) {
					Log.d("tag", "exception:" + e.getMessage());
					e.printStackTrace();
				} finally {
					if (fos != null) {
						try {
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						fos = null;
					}
				}
			}
		}
	}
	
	public void setVideoData(final byte[] videobuf,final int h264Data,final int len,final int width,
			final int height){
		//Log.d(TAG,"setVideoData----------");
		if(!mPlaying || mVideoView==null || mUUID == null)
			return;
//		videodata = videobuf;
//		videoDataLen = len;
//		nVideoWidth = width;
//		nVideoHeight = height;
//		dataType = h264Data;
//		
//		Message msg = new Message();
//		
//		if (dataType == 1) {
//			msg.what = 1;
//		} else {
//			msg.what = 2;
//		}
//		mhandler.sendMessage(msg);
		Message msg1 = new Message();
		pahandler.sendMessage(msg1);
		mVideoView.setVideoData(mUUID, videobuf, h264Data, len, width, height);
	}
	
	private Handler mhandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			mThread mm = new mThread();
			new Thread(mm).start();
		}

	};
	
	class mThread implements Runnable{

		@Override
		public void run() {
			Bitmap bmp  = null;
			
//			Log.d(TAG, "dataType=" + dataType);
			switch (dataType) {
			case 1:
				byte[] rgb = new byte[nVideoWidth * nVideoHeight * 2];
				NativeCaller.YUV4202RGB565(videodata, rgb, nVideoWidth,
						nVideoHeight);
				ByteBuffer buffer = ByteBuffer.wrap(rgb);
				rgb = null;
				/* ByteBuffer buffer = ByteBuffer.wrap(videodata); */
//				Log.d(TAG,"nVideoWidth:" + nVideoWidth + " nVideoHeight:" + nVideoHeight +
//						" LiveVideoBean width:" + mVideoView.getWidth() + " height:" + mVideoView.getHeight());
				if (nVideoHeight != 0 && nVideoWidth != 0 && mVideoView.getWidth() != 0 && mVideoView.getHeight() != 0) {
					bmp = Bitmap.createBitmap(nVideoWidth, nVideoHeight,
							Bitmap.Config.RGB_565);
					bmp.copyPixelsFromBuffer(buffer);
					
					mVideoView.setBitmap(bmp);
					mVideoView.postInvalidate();
//					bitmap = Bitmap.createScaledBitmap(bmp, mVideoView.getWidth(),
//							mVideoView.getHeight(), true);
					
//					// 缩放的矩阵
//					Matrix scaleMatrix = new Matrix();
//					scaleMatrix.setScale((float)mVideoView.getWidth()/nVideoHeight, (float)mVideoView.getHeight()/nVideoHeight);
//					//实时变换的图片资源bitmap
//					bitmap = Bitmap.createBitmap(bmp, 
//							mVideoView.getWidth(),mVideoView.getHeight(), scaleMatrix, true);
				}
				break;
			case 0:
				bmp = BitmapFactory.decodeByteArray(videodata, 0, videoDataLen);
				if (bmp != null) {
					bitmap = Bitmap.createScaledBitmap(bmp, mVideoView.getWidth(),
							mVideoView.getHeight(), true);
				}
				break;
			}
			
			if(bmp != null){
				Message msg1 = new Message();
				pahandler.sendMessage(msg1);
			}
		}
		
	}
	
	private Handler pahandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			if(par.getVisibility() == View.VISIBLE)
			par.setVisibility(View.GONE);
			
			if(mVideoName!=null)
				tvname.setText(mVideoName == null?"":mVideoName);
			
	    	if(nP2PMode == ContentCommon.P2P_MODE_P2P_RELAY){
//	    		updatetime();
////				tv.setVisibility(View.VISIBLE);
//				startTime();
	    	}
//			mVideoView.setImageBitmap(bitmap);
			//bitmap = null;
		}
	};

	public void setPPPPReconnectNotify() {
		Message msg = new Message();
		msg.what = 2;

		msgHandler.sendMessage(msg);
	}
	
	private Handler msgHandler = new Handler() {

		public void handleMessage(Message msg) {
			if (msg.what == 1) {
			}else if(msg.what == 2){
				//retry
				Log.d(TAG,"+++++++++++++++++++++++ Restart Livestream ++++++++++++!!!!!++++++++++++");
				//wait for a while
				try {
					//Thread.currentThread();
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				mVideoView.startLiveStream();
			}
		}
	};

	public Bitmap captureVideoBitmap(boolean decode_h264) {
		return mVideoView.getBitmapFromKeyFrame();
	}
	public byte[] getH264KeyFrame(){
		return mVideoView.getH264KeyFrame();
	}
	public int getVideoWidth(){
		return mVideoView.nVideoWidth;
	}
	public int getVideoHeight(){
		return mVideoView.nVideoHeight;
	}
}

