package com.jwkj.activity;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.test.jpushServer.R;
import com.jwkj.global.Constants;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class ImageCheckActivity extends Activity implements OnTouchListener{
	// 放大缩小
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();

	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist;

	// 模式
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;

    private ImageView image;
    private String imageUrl ;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_image_check);
    	String imageName = getIntent().getExtras().getString("imageName");
    	imageUrl = imageName;
        image = (ImageView)findViewById(R.id.imageView1);
        image.setOnTouchListener(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = getBitmapFromNetwork(imageUrl);
                if(null!=bitmap){
                	Message msg = Message.obtain();
                    msg.obj = bitmap;
                    msg.what = 0x001;
                    handler.sendMessage(msg);
                }else{
                	finish();
                }
            }
        }).start();
    }
    private Handler handler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	            super.handleMessage(msg);
	            switch (msg.what) {
	                case 0x001:
	                    Bitmap bitmap = (Bitmap) msg.obj;
	                    image.setImageBitmap(bitmap);
	                    break;
	            }
	        }
	    };

	 public Bitmap getBitmapFromNetwork(String strUrl) {
        Bitmap bitmap = null;
        InputStream is = null;
        try {
            URL url = new URL(strUrl);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            is = conn.getInputStream();//获得图片的数据流
            if(null!=is){
            	bitmap = BitmapFactory.decodeStream(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
            	if(null!=is){
            		is.close();
            	}
                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return bitmap;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		ImageView myImageView = (ImageView) v;
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		// 设置拖拉模式
		case MotionEvent.ACTION_DOWN:
			matrix.set(myImageView.getImageMatrix());
			savedMatrix.set(matrix);
			start.set(event.getX(), event.getY());
			mode = DRAG;
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
		mode = NONE;
		break;
	
		// 设置多点触摸模式
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			if (oldDist > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
			}
		break;
		// 若为DRAG模式，则点击移动图片
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX() - start.x, event.getY()
						- start.y);
			}
			// 若为ZOOM模式，则点击触摸缩放
			else if (mode == ZOOM) {
				float newDist = spacing(event);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float scale = newDist / oldDist;
					// 设置硕放比例和图片的中点位置
					matrix.postScale(scale, scale, mid.x, mid.y);
				}
			}
			break;
		}
		myImageView.setImageMatrix(matrix);
		return true;
	}

	// 计算移动距离
	private float spacing(MotionEvent event) {
	float x = event.getX(0) - event.getX(1);
	float y = event.getY(0) - event.getY(1);
	return FloatMath.sqrt(x * x + y * y);
	}
	
	// 计算中点位置
	private void midPoint(PointF point, MotionEvent event) {
	float x = event.getX(0) + event.getX(1);
	float y = event.getY(0) + event.getY(1);
	point.set(x / 2, y / 2);
	}

}
