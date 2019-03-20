package freesbell.demo.utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class HLCompViewPager extends ViewPager {
	private final String TAG = "HLCompViewPager";
	private float mDX,mDY,mLX,mLY;
	private boolean mIntercept = false;
	private int mLastAct;
	public HLCompViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public HLCompViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN :
                mDX = mDY = 0f;
                mLX = ev.getX();
                mLY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE :
                final float X = ev.getX();
                final float Y = ev.getY();
                mDX += Math.abs(X - mLX);
                mDY += Math.abs(Y - mLY);
                mLX = X;
                mLY = Y;
//                if (mIntercept && mLastAct == MotionEvent.ACTION_MOVE) {
//                    return false;
//                }
//                if (mDX > mDY) {
//                    mIntercept = true;
//                    mLastAct = MotionEvent.ACTION_MOVE;
//                    return false;
//                }
                break;
//            case MotionEvent.ACTION_UP :
//                mDX = mDY = 0f;
//                mLX = ev.getX();
//                mLY = ev.getY();
//                break;
            default:;
        }
//        mLastAct = ev.getAction();
//        mIntercept = false;
//        return super.onInterceptTouchEvent(ev);
		boolean rb = super.onInterceptTouchEvent(ev);
//		Log.d(TAG,"super.onInterceptTouchEvent(ev):"+rb);
//		Log.d(TAG,"mDX:" + mDX + " mDY:" + mDY + " mLX:" + mLX + " mLY:" + mLY);
		return rb;
    }
}
