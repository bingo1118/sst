package com.jwkj.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;
import com.jwkj.activity.ImageCheckActivity;
import com.jwkj.data.AlarmMessage;
import com.jwkj.global.Constants;
import com.test.jpushServer.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.sax.StartElementListener;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VolleyImageViewListAdapter extends BaseAdapter{
	private static final String TAG = "VolleyListAdapter";  
    
    private Context mContext;  
    private ImageLoader mImageLoader; 
    private ImageLoader mImageLoader1; 
    private ImageLoader mImageLoader2; 
    List<AlarmMessage> listAlarmMessage;
    List<String> oneList;
    List<String> twoList;
    List<String> threeList;
      
    public VolleyImageViewListAdapter(Context context,List<AlarmMessage> listAlarmMessage) {  
        this.mContext = context;  
        this.listAlarmMessage = listAlarmMessage;
  
        RequestQueue mQueue = Volley.newRequestQueue(context);  
        RequestQueue mQueue1 = Volley.newRequestQueue(context);  
        RequestQueue mQueue2 = Volley.newRequestQueue(context);  
        mImageLoader = new ImageLoader(mQueue, (ImageCache) new BitmapCache());
        mImageLoader1 = new ImageLoader(mQueue1, (ImageCache) new BitmapCache());
        mImageLoader2 = new ImageLoader(mQueue2, (ImageCache) new BitmapCache());
    }  
      
  
    @Override  
    public int getCount() {  
        return listAlarmMessage.size();  
    }  
  
    @Override  
    public Object getItem(int position) {  
        return position;  
    }  
  
    @Override  
    public long getItemId(int position) {  
        return position;  
    }  
  
    @Override  
    public View getView(final int position, View convertView, ViewGroup parent) {  
  
        ViewHolder holder = null;  
        if (convertView == null) {  
        	convertView = LayoutInflater.from(mContext).inflate(R.layout.list_alarm_record_item, null);
			holder = new ViewHolder();
			holder.setRobotId((TextView) convertView.findViewById(R.id.robot_id));
			holder.setAllarmType((TextView) convertView.findViewById(R.id.allarm_type));
			holder.setAllarmTime((TextView) convertView.findViewById(R.id.allarm_time));
			holder.setLayout_extern((LinearLayout)convertView.findViewById(R.id.layout_extern));
			holder.setText_group((TextView) convertView.findViewById(R.id.text_group));
			holder.setText_item((TextView) convertView.findViewById(R.id.text_item));
			holder.setText_type((TextView)convertView.findViewById(R.id.tv_type));
			holder.setImage_one((ImageView)convertView.findViewById(R.id.image_one));
			holder.setImage_two((ImageView)convertView.findViewById(R.id.image_two));
			holder.setImage_three((ImageView)convertView.findViewById(R.id.image_three));
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
        imageLoad(listAlarmMessage);
        AlarmMessage mAlarmMessage = listAlarmMessage.get(position);
        String url = ""; 
        String url2 = ""; 
        String url3 = ""; 
        url = Constants.DOWNLOADIMAGES_URL+oneList.get(position % oneList.size());
        url2 = Constants.DOWNLOADIMAGES_URL+twoList.get(position % twoList.size());
        url3 = Constants.DOWNLOADIMAGES_URL+threeList.get(position % threeList.size()); 
        ImageListener listener = ImageLoader.getImageListener(holder.image_one, android.R.drawable.ic_menu_rotate, android.R.drawable.ic_delete);
        ImageListener listener1 = ImageLoader.getImageListener(holder.image_two, android.R.drawable.ic_menu_rotate, android.R.drawable.ic_delete);
        ImageListener listener2 = ImageLoader.getImageListener(holder.image_three, android.R.drawable.ic_menu_rotate, android.R.drawable.ic_delete);
        mImageLoader.get(url, listener);
        mImageLoader1.get(url2, listener1);
        mImageLoader2.get(url3, listener2);
        
        holder.getRobotId().setText(mAlarmMessage.getDeiceId());
        holder.getAllarmTime().setText(mAlarmMessage.getRecordTime());
        holder.getAllarmType().setText(mAlarmMessage.getAlarmType());
        if("移动侦测".equals(mAlarmMessage.getAlarmType())){
        	holder.getLayout_extern().setVisibility(View.GONE);
        }
        holder.getText_group().setText("防区:"+mAlarmMessage.getAlarmArea());
        holder.getText_item().setText("通道:"+mAlarmMessage.getAlarmChannel());
        holder.image_one.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new MyTask().execute(Constants.DOWNLOADIMAGES_URL+oneList.get(position % oneList.size()));
			}
		});
		holder.image_two.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new MyTask().execute(Constants.DOWNLOADIMAGES_URL+twoList.get(position % twoList.size()));
			}
		});
		holder.image_three.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new MyTask().execute(Constants.DOWNLOADIMAGES_URL+threeList.get(position % threeList.size()));
			}
		});
        return convertView;  
    }  
    
    public void imageLoad(List<AlarmMessage> listAlarmM){
    	oneList = new ArrayList<String>();
    	twoList = new ArrayList<String>();
    	threeList = new ArrayList<String>();
		for(AlarmMessage alarmMessage : listAlarmM){
			oneList.add(alarmMessage.getDeiceId()+"/"+alarmMessage.getImageNameOne());
			twoList.add(alarmMessage.getDeiceId()+"/"+alarmMessage.getImageNameTwo());
			threeList.add(alarmMessage.getDeiceId()+"/"+alarmMessage.getImageNameThree());
		}
    } 
  
    static class ViewHolder {  
    	private TextView robotId;
    	private TextView allarmType;
    	private TextView allarmTime;
    	private LinearLayout layout_extern;
    	private TextView text_group;
    	private TextView text_item;
    	private TextView text_type;
    	private ImageView image_one;
    	private ImageView image_two;
    	private ImageView image_three;
    	
		public TextView getRobotId() {
			return robotId;
		}
		public void setRobotId(TextView robotId) {
			this.robotId = robotId;
		}
		public TextView getAllarmType() {
			return allarmType;
		}
		public void setAllarmType(TextView allarmType) {
			this.allarmType = allarmType;
		}
		public TextView getAllarmTime() {
			return allarmTime;
		}
		public void setAllarmTime(TextView allarmTime) {
			this.allarmTime = allarmTime;
		}
		public LinearLayout getLayout_extern() {
			return layout_extern;
		}
		public void setLayout_extern(LinearLayout layout_extern) {
			this.layout_extern = layout_extern;
		}
		public TextView getText_group() {
			return text_group;
		}
		public void setText_group(TextView text_group) {
			this.text_group = text_group;
		}
		public TextView getText_item() {
			return text_item;
		}
		public void setText_item(TextView text_item) {
			this.text_item = text_item;
		}
		public TextView getText_type() {
			return text_type;
		}
		public void setText_type(TextView text_type) {
			this.text_type = text_type;
		}
		public ImageView getImage_one() {
			return image_one;
		}
		public void setImage_one(ImageView image_one) {
			this.image_one = image_one;
		}
		public ImageView getImage_two() {
			return image_two;
		}
		public void setImage_two(ImageView image_two) {
			this.image_two = image_two;
		}
		public ImageView getImage_three() {
			return image_three;
		}
		public void setImage_three(ImageView image_three) {
			this.image_three = image_three;
		}
		
		
    }  
      
      
    public class BitmapCache implements ImageCache {    
        private LruCache<String, Bitmap> mCache;    
            
        public BitmapCache() {    
            int maxSize = 10 * 1024 * 1024;    
            mCache = new LruCache<String, Bitmap>(maxSize) {    
                @Override    
                protected int sizeOf(String key, Bitmap value) {    
                    return value.getRowBytes() * value.getHeight();    
                }     
            };    
        }    
        
        @Override    
        public Bitmap getBitmap(String url) {    
            return mCache.get(url);    
        }    
        
        @Override    
        public void putBitmap(String url, Bitmap bitmap) {    
            mCache.put(url, bitmap);    
        }    
        
    }
    
    @Override  
    public boolean areAllItemsEnabled() {  
        return false;  
    }  
      
    @Override  
    public boolean isEnabled(int position) {  
        return false;  
    } 
    
    class MyTask extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... arg0) {
			String urlStr = arg0[0];
			boolean ifExit = getBitmapFromNetwork(urlStr);
			if(true==ifExit){
				return urlStr;
			}else{
				return "false";
			}
			
		}
    	
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if("false".equals(result)){
				Toast.makeText(mContext, "该图片不存在", 1).show();
			}else{
				Intent i = new Intent(mContext,ImageCheckActivity.class);
				i.putExtra("imageName", result);
				mContext.startActivity(i);
			}
		}
    }
    	
    public boolean getBitmapFromNetwork(String strUrl) {
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
            	is.close();
            	return true;
            }else{
            	 return false;
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	return false;
        } 
	}

}
