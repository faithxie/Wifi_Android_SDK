package freesbell.demo.utils; 
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class MulitPointTouchListener implements OnTouchListener { 
        Matrix matrix = new Matrix(); 
        Matrix savedMatrix = new Matrix(); 
        float x1,x2,y1,y2;
        static final int NONE = 0; 
        static final int DRAG = 1; 
        static final int ZOOM = 2; 
        int mode = NONE; 

        PointF start = new PointF(); 
        PointF mid = new PointF(); 
        float oldDist = 1f; 
        
        @Override 
        public boolean onTouch(View v, MotionEvent event) { 

                ImageView view = (ImageView) v; 
                view.setScaleType(ScaleType.MATRIX);
                switch (event.getAction() & MotionEvent.ACTION_MASK) { 
                case MotionEvent.ACTION_DOWN: 
                		
                        matrix.set(view.getImageMatrix()); 
                        savedMatrix.set(matrix); 
                        start.set(event.getX(), event.getY()); 
                        mode = DRAG; 
                       
                       
                        break; 
                case MotionEvent.ACTION_POINTER_DOWN: 
                        oldDist = spacing(event); 
                        if (oldDist > 10f) { 
                                savedMatrix.set(matrix); 
                                midPoint(mid, event); 
                                mode = ZOOM; 
                        } 
                        
                        break; 
                case MotionEvent.ACTION_UP: 
                case MotionEvent.ACTION_POINTER_UP: 
                	
                        mode = NONE; 

                        break; 
                case MotionEvent.ACTION_MOVE: 
                        if (mode == DRAG) { 
                                matrix.set(savedMatrix); 
                                matrix.postTranslate(event.getX() - start.x, event.getY() 
                                                - start.y); 
                        } else if (mode == ZOOM) { 
                                float newDist = spacing(event); 
                                if (newDist > 10f) { 
                                        matrix.set(savedMatrix); 
                                        float scale = newDist / oldDist; 
                                        matrix.postScale(scale, scale, mid.x, mid.y); 
                                } 
                        } 
                        break; 
                } 

                view.setImageMatrix(matrix); 
                return true; 
        } 

        
       
        private float spacing(MotionEvent event) { 
                float x = event.getX(0) - event.getX(1); 
                float y = event.getY(0) - event.getY(1); 
                return (float) Math.sqrt(x * x + y * y); 
        } 

       
        private void midPoint(PointF point, MotionEvent event) { 
                float x = event.getX(0) + event.getX(1); 
                float y = event.getY(0) + event.getY(1); 
                point.set(x / 2, y / 2); 
        } 
} 
