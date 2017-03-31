package com.jwkj.global;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jwkj.entity.Account;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class LogoutService extends Service{
	private Timer mTimer;
	private Context mContext;
	private RequestQueue mQueue;
	private String userID;
	private String phoneUUID;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mContext = getApplicationContext();
		phoneUUID = getMyUUID();
		mTimer = new Timer();
		setTimerdoAction(doActionHandler,mTimer);
	}

	private Handler doActionHandler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 2:
				Account account = AccountPersist.getInstance().getActiveAccountInfo(mContext);
				if(null!=account){
					userID = account.three_number;
					requestSearch(phoneUUID,userID);
				}
				break;
			default:
				break;
			}
		}
	};
	
	private void setTimerdoAction(final Handler oj,Timer t) { 
        t.schedule(new TimerTask() {  
            @Override  
            public void run() {
	    		Message message = new Message();	
				message = oj.obtainMessage();
				message.what = 2; 
	    		oj.sendMessage(message);
            }  
        }, 1000, 3000/* 表示1000毫秒之後，每隔1000毫秒執行一次 */);  
    } 
	
	private void requestSearch(final String phoneUUID,final String userID){
		System.out.println("....kk....");
		mQueue = Volley.newRequestQueue(mContext);  
		StringRequest stringRequest = new StringRequest(Method.POST, Constants.OLD_USERINFO_URL,  
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						if("YES".equalsIgnoreCase(response)){
							Intent canel=new Intent();
							canel.setAction(Constants.Action.ACTION_SWITCH_USER);
							mContext.sendBroadcast(canel);
						}
					}
		} , 
		new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("phoneUUID", phoneUUID+userID);
				map.put("userID", userID);
				return map;
			}
		};
		mQueue.add(stringRequest);
	}
	
	//生成mei
	private String getMyUUID(){
		final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);   
		final String tmDevice;   
		tmDevice = "" + tm.getDeviceId();  
		System.out.println("tmDevice="+tmDevice);
		return tmDevice;
	}
}
