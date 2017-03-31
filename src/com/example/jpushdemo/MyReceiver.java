package com.example.jpushdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import com.jwkj.data.AlarmRecord;
import com.jwkj.data.DataManager;
import com.jwkj.data.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
		
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
                        
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        	//processCustomMessage(context, bundle);
        	
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            SharedPreferencesManager sp = SharedPreferencesManager.getInstance();
			String flagStr = sp.getData(context, "alarm", "push_time");
			String alarmID = sp.getData(context, "alarm", "alarm_id");
			
			String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
			String regex = "\\d*";
	        Pattern p = Pattern.compile(regex);
	        Matcher m = p.matcher(title);
	        List<String> list = new ArrayList<String>();
	        while (m.find()) {
	        	if (!"".equals(m.group()))
	        	list.add(m.group());
		    }
		    String cameraId=list.get(0);
			
			if(null!=flagStr&&flagStr.length()>0){
				long lodTime = Long.parseLong(flagStr);
				long timeStr = System.currentTimeMillis();
				long newTime = (timeStr-lodTime)/1000;
				if(newTime > 1*60||!cameraId.equalsIgnoreCase(alarmID)){
					Intent ii = new Intent(context, com.jwkj.activity.AlarmFireLinkActivity.class);
		            ii.putExtras(bundle);
		            ii.putExtra("insert", "yes");
		            ii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        	context.startActivity(ii);
				}
			}else{
				Intent ii = new Intent(context, com.jwkj.activity.AlarmFireLinkActivity.class);
	            ii.putExtras(bundle);
	            ii.putExtra("insert", "yes");
	            ii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        	context.startActivity(ii);
			}
			

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            
        	//打开自定义的Activity
            Intent ii = new Intent(context, com.jwkj.activity.AlarmFireLinkActivity.class);
            ii.putExtras(bundle);
            ii.putExtra("insert", "no");
            ii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	context.startActivity(ii);
        	
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        	
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        	Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
        	Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
	}
}
