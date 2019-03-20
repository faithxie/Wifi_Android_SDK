package freesbell.demo.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import fenzhi.nativecaller.NativeCaller;
import freesbell.demo.client.BridgeService;
import freesbell.demo.content.ContentCommon;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

/*
 *
 * author:hujian
 * date:2015/12/17
 */
public class LiveStreamVideoView extends SurfaceView implements SurfaceHolder.Callback{
	private final String TAG = "LiveStreamVideoView";
	private String mUUID;
	private SurfaceHolder holder;
	private ArrayList<VideoBuf> mVideoBufList = new ArrayList<VideoBuf>();
	public LiveStreamVideoView(Context context) {
		super(context);
		
		init(context);
	}
	public LiveStreamVideoView(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		
		init(paramContext);
	}

	public LiveStreamVideoView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
		
		init(paramContext);
	}
	
	private void init(Context context){
		holder = this.getHolder();
		holder.addCallback(this);
		
		mVideoBufList.clear();
	}
	public void setUUID(String uuid){
		mUUID = uuid;
	}
	
	public void startLiveStream(){
		
		if(mUUID!=null)
		NativeCaller.SetSoftDecode(mUUID,1);
//		Log.d(TAG,"startLiveStream mUUID:" + mUUID);
		//if(mStartReference>0)
		{
			final String uuid = mUUID;
			new Thread(new Runnable(){
				@Override
				public void run() {
					if(mUUID == null)
						return;
//					Log.d(TAG,"StartPPPPLivestream:" + mUUID);
					NativeCaller.StartP2PLivestream(uuid, 1,4);
				}
			}).start();
		}
	}
	
	public void stopLiveStream(){
		
		if(mVideoDisplayThread!=null)
		mVideoDisplayThread.mRuning=false;
		//if(mStartReference<=0)
		{
			new Thread(new Runnable(){
				@Override
				public void run() {
					if(mUUID == null)
						return;
					NativeCaller.StopP2PLivestream(mUUID);
				}
			}).start();
		}
	}

	private float mDegrees = 0;
	private boolean mInvert = false;
	public void setVideoDegrees(boolean in,float d){
		mDegrees = d;
		mInvert = in;
	}
	private Bitmap bitmap=null;
	private int dataType =2;
	private byte[] videodata = null;
	private int videoDataLen = 0;
	public int nVideoWidth = 0;
	public int nVideoHeight = 0;
	private int nVideoWidthSaved = 0;
	private Bitmap bmp  = null;
	class VideoDisplayThread implements Runnable{
		public boolean mRuning = true;
		@Override
		public void run() {
//			if(videodata == null || nVideoWidth == 0 || nVideoWidth == 0)
//				return ;
			while(mRuning){
				if(mVideoBufList.size()<=0){
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					continue;
				}
				
				VideoBuf vb = mVideoBufList.get(0);
				if(vb==null)continue;
				mVideoBufList.remove(0);
				switch (vb.dataType) {
				case 1:
					ByteBuffer buffer = ByteBuffer.wrap(vb.videobuf);
	
					if(bmp==null)
					bmp = Bitmap.createBitmap(vb.width, vb.height,
							Bitmap.Config.RGB_565);
					bmp.copyPixelsFromBuffer(buffer);
					
		        	Canvas canvas = mSurfaceHolder.lockCanvas();
		        	canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
			        Matrix matrix = new Matrix();
			        if(canvas==null || matrix == null)
			        	break;

			        int scaleh = bmp.getHeight();//(int) Math.sqrt(Math.pow(canvas.getWidth()*1.2, 2) + Math.pow(canvas.getHeight()*1.2,2));
			        int scalew = bmp.getWidth();//scaleh*canvas.getWidth()/canvas.getHeight();
//			        Log.e(TAG, "scalew:" + scalew + " scaleh:" + scaleh);
			        
			        matrix.postRotate(mDegrees, bmp.getWidth()/2,bmp.getHeight()/2);
			        
		        	
			        if(mInvert){
			        	matrix.postScale(-((float)scalew)/bmp.getWidth(), ((float)scaleh)/bmp.getHeight());
			        	matrix.postTranslate((canvas.getWidth()+scalew)/2, (canvas.getHeight() - scaleh)/2);
			        }else{
//				    matrix.postTranslate((canvas.getWidth()-scalew)/2, (canvas.getHeight()-scaleh)/2);
			        	matrix.postScale(((float)scalew)/bmp.getWidth(), ((float)scaleh)/bmp.getHeight());
				        matrix.postTranslate((canvas.getWidth()-scalew)/2, (canvas.getHeight()-scaleh)/2);
			        }
			        
//			        
//				    if(mInvert){
////				    	Bitmap invertbmp = Bitmap.createBitmap();
//			        	matrix.postScale(-1, 1);
//				    }
//			        matrix.postTranslate((canvas.getWidth()-scalew)/2, (canvas.getHeight()-scaleh)/2);
			        canvas.drawBitmap(bmp, matrix, null);
			        
			        mSurfaceHolder.unlockCanvasAndPost(canvas);
					break;
				case 0:
					bmp = BitmapFactory.decodeByteArray(videodata, 0, videoDataLen);
	
					LiveStreamVideoView.this.postInvalidate();
					break;
				}
				vb=null;
			}
		}
	}
	
	private Handler mhandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
////			Bundle bu = msg.getData();
////			String did = bu.getString("did");
			VideoDisplayThread mm = new VideoDisplayThread();
			new Thread(mm).start();
		}

	};

//	private boolean isKeyFrame(byte[] videobuf){
//		if(videobuf[2] == 1){
//			if((videobuf[3]&0x0f)==7){
//				return true;
//			}
//		}
//		if(videobuf[3] == 1){
//			if((videobuf[4]&0x0f)==7){
//				return true;
//			}
//		}
//		return false;
//	}
	/**
	 * CallBack Video data
	 * @param uuid
	 * @param videobuf
	 * @param h264Data
	 * @param len
	 * @param width
	 * @param height
	 */
	private byte[] mKeyVideoFrame = null;
	private VideoDisplayThread mVideoDisplayThread = null;
	public void setVideoData(String did ,byte[] videobuf,int h264Data,int len,int width,
		int height) {
//		Log.d(TAG, did+"videobuf:"+videobuf+"Call VideoData...h264Data: " + h264Data + " len: " +
//			     len + " videobuf len: " + len +"width: "+width+"height: "+height);
		if(mUUID == null || !did.contentEquals(mUUID) || mSurface==null)
			return;
		videoDataLen = len;
		nVideoWidth = width;
		nVideoHeight = height;

		if(nVideoWidthSaved!=width){
			nVideoWidthSaved = width;
			bmp = null;
		}
		videodata = videobuf;
//			videoDataLen = len;
//			nVideoWidth = width;
//			nVideoHeight = height;
		dataType = h264Data;
		
		VideoBuf vb = new VideoBuf();
		vb.videobuf = videobuf;
		vb.dataType = h264Data;
		vb.height = height;
		vb.width = width;
		vb.len = len;
		mVideoBufList.add(vb);
		
//		Message msg = new Message();

//		mhandler.sendMessage(msg);
		
		if(mVideoDisplayThread==null||mVideoDisplayThread.mRuning==false){
			mVideoDisplayThread = new VideoDisplayThread();
			new Thread(mVideoDisplayThread).start();
		}
	}
	public Bitmap getBitmapFromKeyFrame(){//(boolean decode_h264){
//		if(decode_h264){
//			if(mKeyVideoFrame!=null){
//				int size[] = new int[2];
//				byte[] yuvbuf = new byte[nVideoWidth*nVideoHeight*2];
//				NativeCaller.DecodeH264Frame(mKeyVideoFrame, 1,yuvbuf, mKeyVideoFrame.length, size);
//				
//				int w = size[0];
//				int h = size[1];
//				byte[] rgb = new byte[ w * h * 2];
//				NativeCaller.YUV4202RGB565(yuvbuf, rgb, w,h);
//				ByteBuffer buffer = ByteBuffer.wrap(rgb);
//				rgb = null;
//				bmp = Bitmap.createBitmap(w, h,
//						Bitmap.Config.RGB_565);
//				bmp.copyPixelsFromBuffer(buffer);
//				return bmp;
//			}
//		}else{
			return bmp;
//		}
//		
//		return null;
	}
	
	public byte[] getH264KeyFrame(){
		if(mKeyVideoFrame!=null){
			return mKeyVideoFrame;
		}
		return null;
	}
	
	public void setBitmap(Bitmap bm){
		bmp = bm;
	}
//	@Override  
//    protected void onDraw(Canvas canvas) {  
//        super.onDraw(canvas);
//
//        if(bmp!=null){
//        	Matrix matrix;
//	        matrix = new Matrix();
//	        matrix.postScale(((float)canvas.getWidth())/bmp.getWidth(), ((float)canvas.getHeight())/bmp.getHeight());
//	        
//	        Bitmap resizebp = Bitmap.createBitmap(bmp, 0, 0,
//	        		bmp.getWidth(), bmp.getHeight(),matrix, true);
//	        //canvas.rotate(90);
//	        RectF rectf = new RectF(0,0,canvas.getWidth(),canvas.getHeight());
//	        canvas.drawBitmap(resizebp,null, rectf, null);
//        }
//    }

	Surface mSurface = null;
	SurfaceHolder mSurfaceHolder;
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		mSurface = holder.getSurface();
		mSurfaceHolder = holder;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.e(TAG,"surfaceDestroyed");
		nVideoWidthSaved = 0;
		bmp = null;
	}
	
	class VideoBuf{
		public byte[] videobuf;
		public int dataType;
		public int len;
		public int width;
		public int height;
	}
}
